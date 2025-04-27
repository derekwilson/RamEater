# RamEater
Android memory stress tool for developers and testers

## AndroidStudio

This project uses AndroidStudio and Gradle, the project is in a folder called RamEaterAndroidStudio

You will need to download AndroidStudio from here

http://developer.android.com/sdk/index.html

## Building

```gradlew assembleDebug```

Or use android studio

## Building for release

```build```

or

```build_set_jdk```

The release build needs the signing key and credentials if you don't have it you cannot build a release

This does a `assembleRelease` and a `clean` as well as copying the APK to the support folder

`gradle.properties` override `JAVA_HOME` to use the embedded Android Studio JDK, it needs to be 17+

Install the APK like this `adb install -r rameater-release-1.0.9-f538e11.apk`, the APK name will reflect the version and the commit.

## Prebuild APKs

There is a [release archive](AndroidSupport/releases/README.md) in the `AndroidSupport` folder in this repo.
