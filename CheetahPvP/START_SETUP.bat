@echo off
echo ========================================
echo  CheetahPvP - First Time Setup
echo ========================================
echo.

cd /d "%~dp0"

echo Downloading Gradle wrapper...
if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo Creating gradle wrapper directory...
    mkdir gradle\wrapper 2>nul
    
    echo Downloading gradle-wrapper.jar...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.5.0/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"
)

echo.
echo Setting up Forge workspace...
call gradlew.bat setupDecompWorkspace --refresh-dependencies

echo.
echo Building project...
call gradlew.bat build

echo.
echo ========================================
echo  Setup complete!
echo ========================================
echo.
echo Run RUN_LAUNCHER.bat to start the launcher
echo Run RUN_CLIENT_DEBUG.bat to test the mod in dev mode
echo.
pause
