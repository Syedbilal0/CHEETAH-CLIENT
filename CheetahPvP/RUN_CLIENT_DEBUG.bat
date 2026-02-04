@echo off
echo ========================================
echo  CheetahPvP Client - Debug Mode
echo ========================================
echo.
echo This will launch Minecraft with the CheetahPvP mod in development mode.
echo.

cd /d "%~dp0"

echo Starting Forge client...
call gradlew.bat :client:runClient

pause
