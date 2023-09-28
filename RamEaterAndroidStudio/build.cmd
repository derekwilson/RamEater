echo %JAVA_HOME%
pause
call gradlew clean assembleRelease
pause
copy .\app\build\outputs\apk\release\rameater-release*.apk ..\AndroidSupport\releases
pause
