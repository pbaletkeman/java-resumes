@echo off
REM ============================================================================
REM Backend Production Build Script (Windows)
REM Build Spring Boot Fat JAR for production deployment
REM ============================================================================

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR:~0,-9%
set BUILD_DIR=%PROJECT_ROOT%build
set JAR_DIR=%BUILD_DIR%\libs

echo.
echo ======================================================================
echo Backend Production Build Script
echo ======================================================================
echo Project Root: %PROJECT_ROOT%
echo Build Directory: %BUILD_DIR%
echo JAR Output: %JAR_DIR%
echo.

REM Step 1: Check if Java is installed
where /Q java
if errorlevel 1 (
    echo.
    echo [X] ERROR: Java is not installed
    echo     Please install Java 25 LTS or later
    exit /b 1
)

for /f "tokens=*" %%A in ('java -version 2^>^&1 ^| findstr /R "version"') do set JAVA_VERSION=%%A
echo [OK] %JAVA_VERSION%
echo.

REM Step 2: Navigate to project root
cd /d "%PROJECT_ROOT%"
if errorlevel 1 (
    echo [X] ERROR: Failed to navigate to project root
    exit /b 1
)

REM Step 3: Clean previous builds
echo [*] Cleaning previous builds...
call gradlew.bat clean --no-daemon
if errorlevel 1 (
    echo [!] Warning: Clean step had issues, continuing...
)
echo.

REM Step 4: Run tests
echo [*] Running tests...
call gradlew.bat test --no-daemon -x checkstyleMain -x checkstyleTest
if errorlevel 1 (
    echo [!] Warning: Some tests failed
)
echo.

REM Step 5: Check code quality
echo [*] Running code quality checks...
call gradlew.bat checkstyleMain checkstyleTest --no-daemon
if errorlevel 1 (
    echo [!] Warning: Code quality checks had issues
)
echo.

REM Step 6: Build fat JAR
echo [*] Building production fat JAR...
call gradlew.bat build --no-daemon -x test -x checkstyleMain -x checkstyleTest
if errorlevel 1 (
    echo [X] ERROR: Build failed
    exit /b 1
)
echo.

REM Step 7: Verify JAR file
if exist "%JAR_DIR%" (
    for /f "delims=" %%A in ('dir /b "%JAR_DIR%\*.jar" 2^>nul ^| findstr ".jar$"') do (
        set JAR_FILE=%%A
        goto :jar_found
    )

    :jar_found
    if defined JAR_FILE (
        echo [OK] JAR file created successfully!
        echo      File: %JAR_DIR%\%JAR_FILE%
        for /f "tokens=*" %%A in ('dir /-l "%JAR_DIR%\%JAR_FILE%"') do echo      %%A
    ) else (
        echo [X] ERROR: No JAR file found in build\libs\
        exit /b 1
    )
) else (
    echo [X] ERROR: Build directory not created
    exit /b 1
)

echo.
echo ======================================================================
echo [OK] Backend build completed successfully!
echo ======================================================================
echo.
echo Next steps:
echo   1. Configure application.properties for production
echo   2. Deploy JAR file to production server
echo   3. Run: java -jar %JAR_FILE%
echo.

endlocal
