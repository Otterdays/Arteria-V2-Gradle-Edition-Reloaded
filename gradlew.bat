@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem AGP (androidJdkImage / JdkImageTransform) needs jlink.exe. Cursor/VS Code Red Hat Java
@rem sets JAVA_HOME to a JRE (no jlink). If we clear JAVA_HOME but no jdk-* exists under
@rem Program Files\Java, gradlew falls through to java.exe on PATH — still the bad JRE.
@rem So: strip poisoned JAVA_HOME, scan common full JDK installs, then refuse PATH fallback
@rem for Android builds (forces a clear install-JDK error instead of cryptic jlink failure).
if defined JAVA_HOME (
  set "_ART_JH=%JAVA_HOME:"=%"
  echo.%_ART_JH%| findstr /I ".cursor redhat.java" >nul && set "JAVA_HOME="
  set "_ART_JH="
)
if defined JAVA_HOME (
  if not exist "%JAVA_HOME%\bin\jlink.exe" set "JAVA_HOME="
)
if not defined JAVA_HOME (
  if exist "%ProgramFiles%\Java\jdk-21.0.10\bin\jlink.exe" (
    set "JAVA_HOME=%ProgramFiles%\Java\jdk-21.0.10"
  )
)
if not defined JAVA_HOME (
  if exist "%ProgramFiles%\Java\jdk-26\bin\jlink.exe" (
    set "JAVA_HOME=%ProgramFiles%\Java\jdk-26"
  )
)
if not defined JAVA_HOME (
  if exist "%ProgramFiles%\Java\jdk-21\bin\jlink.exe" (
    set "JAVA_HOME=%ProgramFiles%\Java\jdk-21"
  )
)
if not defined JAVA_HOME (
  if exist "%ProgramFiles%\Eclipse Adoptium\" (
    for /d %%J in ("%ProgramFiles%\Eclipse Adoptium\jdk-*") do (
      if exist "%%J\bin\jlink.exe" (
        set "JAVA_HOME=%%J"
        goto __arteria_jdk_after_adoptium
      )
    )
  )
)
:__arteria_jdk_after_adoptium
if not defined JAVA_HOME (
  if exist "%ProgramFiles%\Microsoft\" (
    for /d %%J in ("%ProgramFiles%\Microsoft\jdk-*") do (
      if exist "%%J\bin\jlink.exe" (
        set "JAVA_HOME=%%J"
        goto __arteria_jdk_after_ms
      )
    )
  )
)
:__arteria_jdk_after_ms

@rem Set local scope for the variables, and ensure extensions are enabled
setlocal EnableExtensions

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Find java.exe — require a full JDK for this Android project (do not use PATH JRE).
if defined JAVA_HOME goto findJavaFromJavaHome

echo. 1>&2
echo ERROR: No full JDK with bin\jlink.exe found for Android Gradle builds. 1>&2
echo. 1>&2
echo Install Temurin JDK 21+ ^(or Oracle JDK^), or set JAVA_HOME to that JDK. 1>&2
echo Checked: %ProgramFiles%\Java\jdk-21.0.10, jdk-26, jdk-21, Eclipse Adoptium\jdk-*, 1>&2
echo Microsoft\jdk-*. Cursor/VS Code JAVA_HOME is ignored when it has no jlink. 1>&2

"%COMSPEC%" /c exit 1

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

"%COMSPEC%" /c exit 1

:execute
@rem Setup the command line



@rem Execute Gradle
@rem endlocal doesn't take effect until after the line is parsed and variables are expanded
@rem which allows us to clear the local environment before executing the java command
endlocal & "%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %* & call :exitWithErrorLevel

:exitWithErrorLevel
@rem Use "%COMSPEC%" /c exit to allow operators to work properly in scripts
"%COMSPEC%" /c exit %ERRORLEVEL%
