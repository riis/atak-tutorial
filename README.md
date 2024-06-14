(Plugin Name)


_________________________________________________________________
PURPOSE AND CAPABILITIES

A Basic Example App that can be taken and refactored into any kind of ATAK project.
This project was created as part of a series on YouTube called Starting an ATAK Project


_________________________________________________________________
STATUS

Released

_________________________________________________________________
POINT OF CONTACTS

If you would like to [contact RIIS](https://www.riis.com/contact) about potentially starting an ATAK project contact Godfrey (below).
If you want to report something that's broken open an issue or if you want to fix something open a PR.

RIIS
- Godfrey Nolan (godfrey@riis.com)
- Zain Raza (zraza@riis.com)

_________________________________________________________________
PORTS REQUIRED

n/a

_________________________________________________________________
EQUIPMENT REQUIRED

_________________________________________________________________
EQUIPMENT SUPPORTED

_________________________________________________________________
COMPILATION

We recommend you watch the YouTube series if you're newer and then look at these instructions to better understand what we're trying to accomplish

1. If you haven’t already to the [release page of ATAK-CIV](https://github.com/deptofdefense/AndroidTacticalAssaultKit-CIV/releases/latest)
2. Download `atak-civ-sdk-4.6.x.x.zip`
3. extract the zip wherever
4. Go inside the `atak-civ-sdk-4.6.x.x` directory
5. make a directory called `plugins`
6. Go into `atak-civ-sdk-4.6.x.x/plugins`
7. Clone this repo into `atak-civ-sdk-4.6.x.x/plugins`
8. Change directory into the “plugintemplate” open a terminal and run the following:
`keytool -genkeypair -dname "CN=Android Debug,O=Android,C=US" -validity 9999 -keystore debug.keystore -alias androiddebugkey -keypass android -storepass android -keyalg RSA`
`keytool -genkeypair -dname "CN=Android Release,O=Android,C=US" -validity 9999 -keystore release.keystore -alias androidreleasekey -keypass android -storepass android -keyalg RSA`
9. Open Android Studio and enter the following in your `local.properties` and fill out the paths of the keystores you just generated:
```
# the sdk.dir should be automatically assigned to the path of your Android Studio SDK
# for the paths, if you are on mac or linux please use / instead of \\
sdk.dir=<ANDROID_SDK_PATH>
takDebugKeyFile=<ABSOLUTE_PLUGIN_PATH>\\debug.keystore
takDebugKeyFilePassword=android
takDebugKeyAlias=androiddebugkey
takDebugKeyPassword=android

takReleaseKeyFile=<ABSOLUTE_PLUGIN_PATH>\\release.keystore
takReleaseKeyFilePassword=android
takReleaseKeyAlias=androidreleasekey
takReleaseKeyPassword=android
```
10. NOTE: Do not upgrade gradle. In “File > Project Structure” on the “Project” tab, make sure the Gradle Plugin Version is "4.2.2" and then Gradle Version is "6.9.1" or other errors will arise
11. Go to "File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK" and make sure you are using Java 11, if you don't have it, install it.
12. Resync and rebuild, should work
13. Open the Run Configurations dropdown menu and select "Edit Configurations".
14. Set "Launch Options > Launch dropdown" selector to the value "Nothing" and press the Apply button
15. Set “Name” to “plugin new install”
16. In the top left of the “Run/Debug Configurations” Window, click the “Copy Configuration” icon or press “CTRL/CMD+D”
17. Go into that new config and set “Name” to “plugin reinstall”
18. Go to “Before Launch > Add > Run External Tool”
19. In the “External Tools” Window click “Add”
20. Set Name to “reinstall (whatever plugin name is) plugin”
21. Set “Tool Settings > Program” to `adb`
22. Set “Tool Settings > Arguments: `uninstall com.atakmap.android.plugintemplate.plugin` **NOTE YOU NEED TO CHANGE THIS IF YOU REFACTOR YOUR PACKAGE NAME**
23. Set “Tool Settings > Working Directory” to your project root directory. For me it was `/home/zain/Documents/Tutorial/atak-civ-sdk-4.6.0.5/atak-civ/plugins/plugintemplate`
24. Untick “Advanced Options > Synchronize files after execution”
25. Save, apply and exit out of all of the config menus
26. NOTE - The “app reinstall” configuration will be useful for debugging so you don’t need to constantly reinstall the app to see changes made on your plugin, you will be using it every time after the first time run
27. Towards the bottom left, click on the "Build Variants" tab and change the "Active Build Variant" from "milDebug" to “civDebug".
28. Build and rerun, test with the ATAK app in the ATAK release you cloned. check the "Plugins" menu in the side drawer and the plugin name should be listed.

_________________________________________________________________
DEVELOPER NOTES
