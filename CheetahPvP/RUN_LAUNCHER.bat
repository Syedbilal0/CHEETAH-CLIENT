@echo off
echo ========================================
echo  CheetahPvP Launcher
echo ========================================
echo.

cd /d "%~dp0"

echo Building and running launcher...
call gradlew.bat :launcher:run

pause
