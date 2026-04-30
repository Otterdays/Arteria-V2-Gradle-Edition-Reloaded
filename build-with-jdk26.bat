@echo off
setlocal EnableExtensions
rem Run Gradle with JDK 26 (overrides org.gradle.java.home in gradle.properties).
rem Usage: build-with-jdk26.bat [gradlew args...]
rem Example: build-with-jdk26.bat :app:assembleDebug
rem
rem Optional: set JDK26_HOME=\path\to\jdk-26 (or jdk-26.x.x-hotspot) before calling.

set "JDK_HOME="
if defined JDK26_HOME (
    set "JDK_HOME=%JDK26_HOME:"=%"
)
if not defined JDK_HOME (
    if exist "%ProgramFiles%\Java\jdk-26\bin\java.exe" (
        set "JDK_HOME=%ProgramFiles%\Java\jdk-26"
    )
)
if not defined JDK_HOME (
    rem Oracle-style versioned dirs: jdk-26, jdk-26.0.x, jdk-26.0.x-hotspot ...
    if exist "%ProgramFiles%\Java\" (
        for /d %%J in ("%ProgramFiles%\Java\jdk-26*") do (
            if exist "%%J\bin\java.exe" (
                set "JDK_HOME=%%J"
                goto __arteria_jdk26_pick_done
            )
        )
    )
)
if not defined JDK_HOME (
    if exist "%ProgramFiles%\Eclipse Adoptium\" (
        for /d %%J in ("%ProgramFiles%\Eclipse Adoptium\jdk-26*") do (
            if exist "%%J\bin\java.exe" (
                set "JDK_HOME=%%J"
                goto __arteria_jdk26_pick_done
            )
        )
    )
)
:__arteria_jdk26_pick_done

if not defined JDK_HOME (
    echo ERROR: JDK 26 not found.
    echo.
    echo Checked: JDK26_HOME, "%ProgramFiles%\Java\jdk-26*", "%ProgramFiles%\Eclipse Adoptium\jdk-26*".
    echo Install Temurin 26 https://adoptium.net/temurin/releases/?version=26 or set JDK26_HOME.
    echo.
    echo If JDK 26 is not required: use plain gradlew.bat — this repo resolves a full JDK 21 under
    echo "%ProgramFiles%\Java\jdk-*" automatically when JAVA_HOME is not a broken JRE.
    echo Examples for your machine if "jdk-21" exists there:
    echo   gradlew.bat :app:assembleDebug
    echo   build-apk-for-transfer.ps1
    exit /b 1
)
if not exist "%JDK_HOME%\bin\java.exe" (
    echo ERROR: java.exe not found under "%JDK_HOME%"
    exit /b 1
)
if not exist "%JDK_HOME%\bin\jlink.exe" (
    echo ERROR: jlink.exe not found under "%JDK_HOME%\bin"
    echo        Android AGP needs a full JDK ^(not a JRE^). Install Temurin JDK 21+ or Oracle JDK.
    exit /b 1
)

rem Cursor/VS Code often sets JAVA_HOME to an extension JRE without jlink; AGP's JdkImageTransform
rem follows JAVA_HOME. Force the same full JDK as Gradle's JVM.
set "JAVA_HOME=%JDK_HOME%"
set "PATH=%JDK_HOME%\bin;%PATH%"

cd /d "%~dp0"
set "APK_DIR=%CD%\app\build\outputs\apk\debug"
set "APK_PATH=%APK_DIR%\app-debug.apk"
set "DIST_DIR=%CD%\dist"

rem Drop stale daemons that may have started under a JRE-only JAVA_HOME (jlink path sticks).
call gradlew.bat -Dorg.gradle.java.home="%JDK_HOME%" --stop >nul 2>&1

if "%~1"=="" (
    echo No Gradle task supplied. Defaulting to :app:assembleDebug
    call gradlew.bat --no-daemon -Dorg.gradle.java.home="%JDK_HOME%" :app:assembleDebug
) else (
    call gradlew.bat --no-daemon -Dorg.gradle.java.home="%JDK_HOME%" %*
)

if errorlevel 1 exit /b %errorlevel%

echo.
echo Build output directory:
echo   %APK_DIR%
if exist "%APK_PATH%" (
    echo Built APK:
    echo   %APK_PATH%
    if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"
    for /f %%i in ('powershell -NoProfile -Command "(Get-Date).ToString('yyyyMMdd-HHmmss')"') do (
        copy /Y "%APK_PATH%" "%DIST_DIR%\ArteriaV2-%%i.apk" >nul
        if errorlevel 1 (
            echo ERROR: Failed to create named APK copy in dist folder.
            exit /b 1
        )
        echo Named APK copy:
        echo   %DIST_DIR%\ArteriaV2-%%i.apk
    )
) else (
    echo NOTE: APK file not found at expected debug path.
    echo       If you built a different variant, check app\build\outputs\apk\
)
