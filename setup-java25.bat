@echo off
REM Setup Java 25 environment for this terminal session
REM This script sets JAVA_HOME and PATH to use Java 25 Corretto

setlocal enabledelayedexpansion

set "JAVA_HOME=C:\Users\Pete\java\corretto25"
set "PATH=!JAVA_HOME!\bin;%PATH%"

echo.
echo ========================================
echo Java 25 Environment Setup
echo ========================================
echo JAVA_HOME set to: %JAVA_HOME%
java -version
echo.
echo You can now use: gradlew.bat clean build
echo ========================================
echo.

cmd /k
