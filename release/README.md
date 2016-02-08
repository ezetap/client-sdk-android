# Releases

## Version 1.0.1

Initial release version of Cordova Android SDK.

## Version 1.0.2

This version of the SDK fixes the "external reference" issue. In the earlier version, if references::reference[1,2,3] fields were passed from the SDK, it was not reaching the server. This release fixes that issue. If you are using the earlier version of the SDK, please upgrade to the new version to have this issue resolved.


## Version 1.0.2

This version of the SDK adds the below features-
	1. Android 6.0 Marshmallow Support.
	2. Added the references object in all the payment responses. 
	

### How to upgrade:
IMPORTANT- If your project's targetSdkVersion is higher or equal to 23(Android 6.0 Marshmallow) please add Android support library v4 to your Android project from <a href="http://developer.android.com/tools/support-library/setup.html">here.</a> The Android support libraries are not required if your project's targetSdkVersion is lesser than 23.

Replace the "EzetapAndroidSDK-Cordova1.0.3.JAR" file with the Version 1.0.2 JAR file.
