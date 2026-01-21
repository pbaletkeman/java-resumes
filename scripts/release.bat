@echo off
REM ============================================================================
REM Release Script - Frontend and Backend Versioning & Git Push (Windows)
REM Manages version bumping, tagging, and GitHub push for both packages
REM ============================================================================

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR:~0,-9%

echo.
echo ======================================================================
echo Release Management Script
echo ======================================================================
echo.

REM Check if git is installed
where /Q git
if errorlevel 1 (
    echo [X] ERROR: Git is not installed or not in PATH
    exit /b 1
)

REM Navigate to project root
cd /d "%PROJECT_ROOT%"
if errorlevel 1 (
    echo [X] ERROR: Failed to navigate to project root
    exit /b 1
)

REM Check if git repository exists
if not exist ".git" (
    echo [X] ERROR: Git repository not initialized
    exit /b 1
)

REM Get current versions
for /f "tokens=3" %%A in ('findstr /R "\"version\"" "%PROJECT_ROOT%frontend\package.json" ^| findstr /V "//"') do (
    set FRONTEND_VERSION=%%A
    set FRONTEND_VERSION=!FRONTEND_VERSION:"=!
    set FRONTEND_VERSION=!FRONTEND_VERSION:,=!
    goto :frontend_done
)
:frontend_done

for /f "tokens=2,3" %%A in ('findstr /R "version = " "%PROJECT_ROOT%build.gradle"') do (
    set BACKEND_VERSION=%%B
    set BACKEND_VERSION=!BACKEND_VERSION:'=!
    goto :backend_done
)
:backend_done

echo [*] Current versions:
echo     Frontend: v%FRONTEND_VERSION%
echo     Backend:  v%BACKEND_VERSION%
echo.

REM Check for uncommitted changes
git diff-index --quiet HEAD --
if errorlevel 1 (
    echo [!] Uncommitted changes detected:
    git status --short
    echo.
    echo Please commit changes first.
    exit /b 1
)

echo Release Options:
echo   1. Bump frontend patch version
echo   2. Bump frontend minor version
echo   3. Bump frontend major version
echo   4. Bump backend patch version
echo   5. Bump backend minor version
echo   6. Bump backend major version
echo   7. Bump both (patch)
echo   0. Exit
echo.

set /p option="Select option (0-7): "

if "%option%"=="0" (
    echo [*] Exiting...
    exit /b 0
)

if "%option%"=="1" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%FRONTEND_VERSION%' | ForEach-Object { $_.major, $_.minor, $_.build + 1 -join '.' }"') do set NEW_FRONTEND=%%A

    REM Update package.json
    powershell -Command "(Get-Content '%PROJECT_ROOT%frontend\package.json') -replace '\"version\": \"[^\"]*\"', '\"version\": \"%NEW_FRONTEND%\"' | Set-Content '%PROJECT_ROOT%frontend\package.json'"
    echo [OK] Updated frontend version to %NEW_FRONTEND%
    set TAG=frontend/v%NEW_FRONTEND%
)

if "%option%"=="2" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%FRONTEND_VERSION%' | ForEach-Object { $_.major, ($_.minor + 1), 0 -join '.' }"') do set NEW_FRONTEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%frontend\package.json') -replace '\"version\": \"[^\"]*\"', '\"version\": \"%NEW_FRONTEND%\"' | Set-Content '%PROJECT_ROOT%frontend\package.json'"
    echo [OK] Updated frontend version to %NEW_FRONTEND%
    set TAG=frontend/v%NEW_FRONTEND%
)

if "%option%"=="3" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%FRONTEND_VERSION%' | ForEach-Object { ($_.major + 1), 0, 0 -join '.' }"') do set NEW_FRONTEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%frontend\package.json') -replace '\"version\": \"[^\"]*\"', '\"version\": \"%NEW_FRONTEND%\"' | Set-Content '%PROJECT_ROOT%frontend\package.json'"
    echo [OK] Updated frontend version to %NEW_FRONTEND%
    set TAG=frontend/v%NEW_FRONTEND%
)

if "%option%"=="4" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%BACKEND_VERSION%' | ForEach-Object { $_.major, $_.minor, $_.build + 1 -join '.' }"') do set NEW_BACKEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%build.gradle') -replace \"version = '[^']*'\", \"version = '%NEW_BACKEND%'\" | Set-Content '%PROJECT_ROOT%build.gradle'"
    echo [OK] Updated backend version to %NEW_BACKEND%
    set TAG=backend/v%NEW_BACKEND%
)

if "%option%"=="5" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%BACKEND_VERSION%' | ForEach-Object { $_.major, ($_.minor + 1), 0 -join '.' }"') do set NEW_BACKEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%build.gradle') -replace \"version = '[^']*'\", \"version = '%NEW_BACKEND%'\" | Set-Content '%PROJECT_ROOT%build.gradle'"
    echo [OK] Updated backend version to %NEW_BACKEND%
    set TAG=backend/v%NEW_BACKEND%
)

if "%option%"=="6" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%BACKEND_VERSION%' | ForEach-Object { ($_.major + 1), 0, 0 -join '.' }"') do set NEW_BACKEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%build.gradle') -replace \"version = '[^']*'\", \"version = '%NEW_BACKEND%'\" | Set-Content '%PROJECT_ROOT%build.gradle'"
    echo [OK] Updated backend version to %NEW_BACKEND%
    set TAG=backend/v%NEW_BACKEND%
)

if "%option%"=="7" (
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%FRONTEND_VERSION%' | ForEach-Object { $_.major, $_.minor, $_.build + 1 -join '.' }"') do set NEW_FRONTEND=%%A
    for /f "tokens=*" %%A in ('powershell -Command "[version]'%BACKEND_VERSION%' | ForEach-Object { $_.major, $_.minor, $_.build + 1 -join '.' }"') do set NEW_BACKEND=%%A

    powershell -Command "(Get-Content '%PROJECT_ROOT%frontend\package.json') -replace '\"version\": \"[^\"]*\"', '\"version\": \"%NEW_FRONTEND%\"' | Set-Content '%PROJECT_ROOT%frontend\package.json'"
    powershell -Command "(Get-Content '%PROJECT_ROOT%build.gradle') -replace \"version = '[^']*'\", \"version = '%NEW_BACKEND%'\" | Set-Content '%PROJECT_ROOT%build.gradle'"
    echo [OK] Updated frontend version to %NEW_FRONTEND%
    echo [OK] Updated backend version to %NEW_BACKEND%
    set TAG=v%NEW_FRONTEND%-%NEW_BACKEND%
)

echo.
echo [*] Preparing release...
echo.

REM Stage version files
git add frontend\package.json build.gradle

REM Create commit
set /p commit_msg="Enter commit message [Release %TAG%]: "
if "!commit_msg!"=="" set commit_msg=Release %TAG%

git commit -m "!commit_msg!"
echo [OK] Committed changes

REM Create tag
git tag -a "%TAG%" -m "Release %TAG%"
echo [OK] Created tag: %TAG%

echo.
echo ======================================================================
echo Ready to push to GitHub
echo ======================================================================
echo.
echo Changes to push:
echo   Tag: %TAG%
echo.

set /p push_confirm="Push to GitHub? (y/n): "

if /i "!push_confirm!"=="y" (
    echo [*] Pushing to GitHub...

    git push origin master
    echo [OK] Pushed commits

    git push origin "%TAG%"
    echo [OK] Pushed tag: %TAG%

    echo.
    echo [OK] Release complete!
    echo.
    echo GitHub URLs:
    echo   Commits: https://github.com/pbaletkeman/java-resumes/commits/master
    echo   Release: https://github.com/pbaletkeman/java-resumes/releases/tag/%TAG%
) else (
    echo [!] Push cancelled. Local changes remain.
    echo.
    echo To push later, run:
    echo   git push origin master
    echo   git push origin %TAG%
)

echo.
endlocal
