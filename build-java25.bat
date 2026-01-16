@echo off
set JAVA_HOME=C:\Users\Pete\java\corretto25
set PATH=%JAVA_HOME%\bin;%PATH%
echo Java Home: %JAVA_HOME%
echo.
"%JAVA_HOME%\bin\java.exe" -version
echo.
echo Running Gradle build...
call gradlew.bat clean build --no-daemon
