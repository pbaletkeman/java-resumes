@echo off
REM Master batch file to run frontend and/or backend
REM Usage: run.bat [--frontend] [--backend] [--all]
REM   --frontend : Run frontend batch file
REM   --backend  : Run backend batch file
REM   --all      : Run both frontend and backend (in separate windows)
REM   --help     : Show this help message

setlocal enabledelayedexpansion

if "%1"=="" (
    echo.
    echo Usage: run.bat [OPTIONS]
    echo.
    echo Options:
    echo   --frontend      Start frontend development with npm install and dev server
    echo   --backend       Run backend with gradle clean build and Java app
    echo   --all           Run both frontend and backend in separate windows
    echo   --help          Show this help message
    echo.
    echo Examples:
    echo   run.bat --frontend
    echo   run.bat --backend
    echo   run.bat --all
    echo.
    goto :eof
)

set "runFrontend=0"
set "runBackend=0"

REM Parse command line arguments
:parseArgs
if "%1"=="" goto :startProcesses
if /i "%1"=="--frontend" (
    set "runFrontend=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--backend" (
    set "runBackend=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--all" (
    set "runFrontend=1"
    set "runBackend=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--help" (
    echo.
    echo Usage: run.bat [OPTIONS]
    echo.
    echo Options:
    echo   --frontend      Start frontend development with npm install and dev server
    echo   --backend       Run backend with gradle clean build and Java app
    echo   --all           Run both frontend and backend in separate windows
    echo   --help          Show this help message
    echo.
    goto :eof
)
shift
goto :parseArgs

:startProcesses
if !runFrontend! equ 0 if !runBackend! equ 0 (
    echo Error: Please specify at least one option: --frontend, --backend, or --all
    echo Use 'run.bat --help' for usage information
    goto :eof
)

if !runFrontend! equ 1 (
    echo.
    echo ========================================
    echo Starting Frontend in new window...
    echo ========================================
    start "Java Resumes - Frontend" cmd /k "cd /d "%~dp0" && frontend.bat --all"
    timeout /t 2 /nobreak
)

if !runBackend! equ 1 (
    echo.
    echo ========================================
    echo Starting Backend in new window...
    echo ========================================
    start "Java Resumes - Backend" cmd /k "cd /d "%~dp0" && backend.bat --all"
)

echo.
echo ========================================
echo Services launching in separate windows...
echo ========================================
echo.
echo Frontend window title: "Java Resumes - Frontend"
echo Backend window title: "Java Resumes - Backend"
echo.
echo Press Ctrl+C in either window to stop that service
echo.
