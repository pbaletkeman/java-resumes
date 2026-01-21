@echo off
REM ============================================================================
REM Frontend Production Build Script (Windows)
REM Build ReactJS for production deployment
REM ============================================================================

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR:~0,-9%
set FRONTEND_DIR=%PROJECT_ROOT%frontend
set BUILD_DIR=%FRONTEND_DIR%\dist

echo.
echo ======================================================================
echo Frontend Production Build Script
echo ======================================================================
echo Project Root: %PROJECT_ROOT%
echo Frontend Directory: %FRONTEND_DIR%
echo Build Output: %BUILD_DIR%
echo.

REM Step 1: Check if Node.js is installed
where /Q node
if errorlevel 1 (
    echo.
    echo [X] ERROR: Node.js is not installed
    echo     Please install Node.js v20 or later
    exit /b 1
)

for /f "tokens=*" %%A in ('node --version') do set NODE_VERSION=%%A
for /f "tokens=*" %%A in ('npm --version') do set NPM_VERSION=%%A

echo [OK] Node.js version: %NODE_VERSION%
echo [OK] npm version: %NPM_VERSION%
echo.

REM Step 2: Navigate to frontend directory
cd /d "%FRONTEND_DIR%"
if errorlevel 1 (
    echo [X] ERROR: Failed to navigate to frontend directory
    exit /b 1
)

REM Step 3: Install dependencies
echo [*] Installing dependencies...
call npm install
if errorlevel 1 (
    echo [X] ERROR: npm install failed
    exit /b 1
)
echo.

REM Step 4: Run tests
echo [*] Running tests...
call npm test -- --run 2>&1 | findstr /I "Test Files"
if errorlevel 1 (
    echo [!] Warning: Tests did not complete successfully
)
echo.

REM Step 5: Build for production
echo [*] Building production bundle...
call npm run build
if errorlevel 1 (
    echo [X] ERROR: Build failed
    exit /b 1
)
echo.

REM Step 6: Verify build output
if exist "%BUILD_DIR%" (
    echo [OK] Build successful!
    echo      Output directory: %BUILD_DIR%
    for /f "usebackq tokens=*" %%A in (`dir /-s /-b "%BUILD_DIR%" ^| find /c /v ""`) do (
        echo      Files created: %%A
    )
) else (
    echo [X] ERROR: Build failed - Output directory not created
    exit /b 1
)

echo.
echo ======================================================================
echo [OK] Frontend build completed successfully!
echo ======================================================================
echo.
echo Next steps:
echo   1. Review the dist\ directory for production files
echo   2. Deploy dist\ contents to your web server
echo   3. Use scripts\deploy-frontend.bat for automated deployment
echo.

endlocal
