@echo off
echo ========================================
echo  CheetahPvP - Build Release
echo ========================================
echo.

cd /d "%~dp0"

echo Building launcher JAR...
call gradlew.bat :launcher:build

echo.
echo Building client mod JAR...
call gradlew.bat :client:build

echo.
echo ========================================
echo  Build complete!
echo ========================================
echo.
echo Launcher JAR: launcher\build\libs\launcher-1.0.0.jar
echo Client MOD:   client\build\libs\client-1.0.0.jar
echo.
pause
