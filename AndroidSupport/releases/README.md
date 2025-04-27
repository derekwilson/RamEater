# RamEater Release Archive
This is the release archive for RamEater. If you cannot get an APK from google you can side-load it from here. 

It is also available from the [Amazon App Store](https://www.amazon.com/Derek-Wilson-RamEater/dp/B0B1LBJYY1/) and for [sideloading using Obtainium](https://derekwilson.net/static/obtainium.html)

## APKs

The APKs support different SDK levels due to restrictions of publishing via the google play store.

| Version | Date        | MinSDK          | TargetSDK
| ------- | ----------- | --------------- | ---------------
| 1.0.9   | 28 Apr 2025 | 14 (Android 4)  | 35 (Android 15)
| 1.0.8   | 28 May 2024 | 14 (Android 4)  | 33 (Android 13)
| 1.0.7   | 28 Sep 2023 | 14 (Android 4)  | 33 (Android 13)
| 1.0.6   | 11 Apr 2021 | 14 (Android 4)  | 30 (Android 11)
| 1.0.5   | 23 May 2019 | 14 (Android 4)  | 26 (Android 8)
| 1.0.4   | 27 Sep 2018 | 14 (Android 4)  | 26 (Android 8)
| 1.0.3   | 17 Jul 2017 | 7 (Android 2.1) | 21 (Android 5)
| 1.0.2   | 3 Apr 2016  | 7 (Android 2.1) | 21 (Android 5)
| 1.0.1   | 13 May 2015 | 7 (Android 2.1) | 21 (Android 5)
| 1.0.0   | 25 Mar 2015 | 11 (Android 3)  | 21 (Android 5)

## Installation

To install use ADB like this, or email/copy the APK into the device and install in the device

```
adb install -r rameater-release-1.0.7-4abdc86.apk
```

## Notes

Major changes for each version

### v1.0.9 - 28 Apr 2025

- 30 Services
- Recompiled for Android 15
- Updated help text

### v1.0.8 - 28 May 2024

- 20 Services
- Ask for Notification permission on Android 13+
- Updated help text

### v1.0.7 - 28 Sep 2023

- 20 Services
- Recompiled for Android 13
- Minor fix for services list UI (thanks Oliver)

### v1.0.6 - 11 Apr 2021

- 20 Services
- Added Start All menu item
- Added services property required by SDK 30, named services

### v1.0.5 - 23 May 2019

- 16 Services
- Fixed issue with toolbar when scrolling
- Updated help

### v1.0.4 - 27 Sep 2018

- 10 Services
- Updated support libraries to fix render issue on toolbar

### v1.0.3 - 17 Jul 2017

- 10 Services

### v1.0.2 - 3 Apr 2016

- 6 Services
- Added burger menu for settings and help
- Changed navigate to running apps to be navigate to developer options for Android 6+ (Android no longer supports the intent to goto running apps)

### v1.0.1 - 13 May 2015

- 6 Services
- Added menu bar for settings, help and goto running apps
- Settings to constrain the maximum amount of memory to allocate
- Added help
- Added navigate to running apps page in system settings

### v1.0.0 - 25 Mar 2015

- 5 Services
- Initial version






