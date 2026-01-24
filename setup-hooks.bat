@echo off
REM Setup git hooks for the project on Windows
REM This script installs git hooks from .githooks directory

setlocal enabledelayedexpansion

REM Get repository root
for /f "tokens=*" %%i in ('git rev-parse --show-toplevel') do set "REPO_ROOT=%%i"

set "HOOKS_SOURCE_DIR=%REPO_ROOT%\.githooks"
set "HOOKS_TARGET_DIR=%REPO_ROOT%\.git\hooks"

echo.
echo ============================================
echo       Setting Up Git Hooks (Windows)
echo ============================================
echo.

REM Check if .githooks directory exists
if not exist "%HOOKS_SOURCE_DIR%" (
    echo [ERROR] .githooks directory not found!
    echo Expected: %HOOKS_SOURCE_DIR%
    exit /b 1
)

REM Create .git/hooks directory if it doesn't exist
if not exist "%HOOKS_TARGET_DIR%" mkdir "%HOOKS_TARGET_DIR%"

REM Install hooks
set "FAILED=0"

for %%H in (pre-commit pre-push) do (
    set "SOURCE=%HOOKS_SOURCE_DIR%\%%H"
    set "TARGET=%HOOKS_TARGET_DIR%\%%H"

    if not exist "!SOURCE!" (
        echo [ERROR] Hook not found: !SOURCE!
        set "FAILED=1"
    ) else (
        copy /Y "!SOURCE!" "!TARGET!" >nul
        echo [OK] Installed: %%H
    )
)

echo.

if %FAILED% equ 0 (
    echo ============================================
    echo      Git hooks installed successfully!
    echo ============================================
    echo.
    echo Installed hooks:
    echo   - pre-commit
    echo   - pre-push
    echo.
    echo To skip hooks temporarily:
    echo   git commit --no-verify
    echo   git push --no-verify
    echo.
    exit /b 0
) else (
    echo [ERROR] Failed to install some hooks
    exit /b 1
)
