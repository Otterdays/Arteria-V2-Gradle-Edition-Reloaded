@echo off
setlocal EnableExtensions
rem Run Gradle with JDK 26 (overrides org.gradle.java.home in gradle.properties).
rem Usage: build-with-jdk26.bat [gradlew args...]
rem Example: build-with-jdk26.bat :app:assembleDebug

set "JDK_HOME=C:\Program Files\Java\jdk-26"
if not exist "%JDK_HOME%\bin\java.exe" (
    echo ERROR: java.exe not found under "%JDK_HOME%"
    exit /b 1
)

cd /d "%~dp0"
set "APK_DIR=%CD%\app\build\outputs\apk\debug"
set "APK_PATH=%APK_DIR%\app-debug.apk"
set "DIST_DIR=%CD%\dist"

if "%~1"=="" (
    echo No Gradle task supplied. Defaulting to :app:assembleDebug
    call gradlew.bat -Dorg.gradle.java.home="%JDK_HOME%" :app:assembleDebug
) else (
    call gradlew.bat -Dorg.gradle.java.home="%JDK_HOME%" %*
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
