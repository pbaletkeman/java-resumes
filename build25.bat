@echo off
REM Build script for Java 25
REM This script ensures Java 25 Corretto is used for the build

setlocal enabledelayedexpansion

REM Set Java Home to Java 25 Corretto
set "JAVA_HOME=C:\Users\Pete\java\corretto25"

REM Verify Java Home is valid
if not exist "!JAVA_HOME!\bin\java.exe" (
    echo Error: Java 25 not found at !JAVA_HOME!
    exit /b 1
)

echo Using Java:
"!JAVA_HOME!\bin\java.exe" -version

echo.
echo Running Gradle build...
call gradlew.bat %*

endlocal
