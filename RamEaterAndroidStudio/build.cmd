del .\app\build\outputs\apk\release\rameater-release*.apk
call gradlew clean assembleRelease
copy .\app\build\outputs\apk\release\rameater-release*.apk ..\AndroidSupport\Deploy
pause
