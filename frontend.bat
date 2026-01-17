@echo off
REM Frontend batch file for npm management and dev server
REM Usage: frontend.bat [OPTIONS]
REM   --clean      : Clear node_modules directory
REM   --install    : Install npm modules
REM   --start      : Start the frontend dev server
REM   --all        : Run clean, install, and start
REM   --help       : Show this help message

setlocal enabledelayedexpansion

if "%1"=="" (
    echo.
    echo Usage: frontend.bat [OPTIONS]
    echo.
    echo Options:
    echo   --clean         Clear node_modules directory
    echo   --install       Install npm modules
    echo   --start         Start the frontend dev server
    echo   --all           Run clean, install, and start
    echo   --help          Show this help message
    echo.
    echo Examples:
    echo   frontend.bat --clean
    echo   frontend.bat --install
    echo   frontend.bat --start
    echo   frontend.bat --all
    echo.
    goto :eof
)

set "runClean=0"
set "runInstall=0"
set "runStart=0"

REM Parse command line arguments
:parseArgs
if "%1"=="" goto :processFrontend
if /i "%1"=="--clean" (
    set "runClean=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--install" (
    set "runInstall=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--start" (
    set "runStart=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--all" (
    set "runClean=1"
    set "runInstall=1"
    set "runStart=1"
    shift
    goto :parseArgs
)
if /i "%1"=="--help" (
    echo.
    echo Usage: frontend.bat [OPTIONS]
    echo.
    echo Options:
    echo   --clean         Clear node_modules directory
    echo   --install       Install npm modules
    echo   --start         Start the frontend dev server
    echo   --all           Run clean, install, and start
    echo   --help          Show this help message
    echo.
    goto :eof
)
shift
goto :parseArgs

:processFrontend
if !runClean! equ 0 if !runInstall! equ 0 if !runStart! equ 0 (
    echo Error: Please specify at least one option: --clean, --install, --start, or --all
    echo Use 'frontend.bat --help' for usage information
    goto :eof
)

if !runClean! equ 1 (
    echo.
    echo ========================================
    echo Clearing npm modules...
    echo ========================================
    cd /d "%~dp0frontend"
    if exist "node_modules" (
        echo Removing node_modules directory...
        rmdir /s /q node_modules
        if !errorlevel! equ 0 (
            echo ✓ npm modules cleared successfully
        ) else (
            echo ✗ Failed to clear npm modules
            goto :eof
        )
    ) else (
        echo node_modules directory not found, skipping...
    )
)

if !runInstall! equ 1 (
    echo.
    echo ========================================
    echo Installing npm modules...
    echo ========================================
    cd /d "%~dp0frontend"
    call npm install
    if !errorlevel! neq 0 (
        echo ✗ npm install failed
        goto :eof
    )
    echo ✓ npm modules installed successfully
)

if !runStart! equ 1 (
    echo.
    echo ========================================
    echo Starting frontend dev server...
    echo ========================================
    cd /d "%~dp0frontend"
    call npm run dev
    if !errorlevel! neq 0 (
        echo ✗ Frontend dev server failed to start
        goto :eof
    )
)

echo.
echo ========================================
echo Frontend tasks completed!
echo ========================================
