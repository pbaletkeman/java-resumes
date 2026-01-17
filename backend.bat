@echo off
REM Backend batch file for gradle build and Java application
REM Usage: backend.bat [OPTIONS]
REM   --clean      : Run gradle clean
REM   --build      : Run gradle clean build
REM   --run        : Run the Java application
REM   --all        : Run clean build and Java app
REM   --help       : Show this help message

setlocal enabledelayedexpansion

if "%1"=="" (
    echo.
    echo Usage: backend.bat [OPTIONS]
    echo.
    echo Options:
    echo   --clean         Run gradle clean
    echo   --build         Run gradle clean build
    echo   --run           Run the Java application
    echo   --all           Run clean build and Java app
    echo   --help          Show this help message
    echo.
    echo Examples:
    echo   backend.bat --clean
    echo   backend.bat --build
    echo   backend.bat --run
    echo   backend.bat --all
    echo.
    goto :eof
)

set "runClean=0"
set "runBuild=0"
set "runApp=0"

REM Parse command line arguments
:parseArgs
if "%1"=="" goto :processBackend
if /i "%1"=="--clean" (
    set "runClean=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--build" (
    set "runClean=1"
    set "runBuild=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--run" (
    set "runApp=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--all" (
    set "runClean=1"
    set "runBuild=1"
    set "runApp=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--help" (
    echo.
    echo Usage: backend.bat [OPTIONS]
    echo.
    echo Options:
    echo   --clean         Run gradle clean
    echo   --build         Run gradle clean build
    echo   --run           Run the Java application
    echo   --all           Run clean build and Java app
    echo   --help          Show this help message
    echo.
    goto :eof
)
shift
goto :parseArgs

:processBackend
if !runClean! equ 0 if !runBuild! equ 0 if !runApp! equ 0 (
    echo Error: Please specify at least one option: --clean, --build, --run, or --all
    echo Use 'backend.bat --help' for usage information
    goto :eof
)

cd /d "%~dp0"

if !runClean! equ 1 (
    echo.
    echo ========================================
    echo Running gradle clean...
    echo ========================================
    call gradlew clean
    if !errorlevel! neq 0 (
        echo ✗ Gradle clean failed
        goto :eof
    )
    echo ✓ Gradle clean completed successfully
)

if !runBuild! equ 1 (
    echo.
    echo ========================================
    echo Running gradle clean build...
    echo ========================================
    call gradlew clean build
    if !errorlevel! neq 0 (
        echo ✗ Gradle build failed
        goto :eof
    )
    echo ✓ Gradle build completed successfully
)

if !runApp! equ 1 (
    echo.
    echo ========================================
    echo Starting Java application...
    echo ========================================
    call gradlew bootRun
    if !errorlevel! neq 0 (
        echo ✗ Java application failed to start
        goto :eof
    )
)

echo.
echo ========================================
echo Backend tasks completed!
echo ========================================
