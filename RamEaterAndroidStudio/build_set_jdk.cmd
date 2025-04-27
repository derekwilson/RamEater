del .\app\build\outputs\apk\release\rameater-*.apk
echo %JAVA_HOME%
rem  override the machine specified JAVA_HOME as we need to use the embedded JDK
rem  this will only work if the embedded JDK is 17+
call gradlew clean assembleRelease "-Dorg.gradle.java.home=C:\Program Files\Android\Android Studio\jbr"
pause
copy .\app\build\outputs\apk\release\rameater-release*.apk ..\AndroidSupport\releases
pause
