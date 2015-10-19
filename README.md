# client-sdk-android

This section is meant for people who have existing android native/hybrid (Cordova) apps and would like to integrate Ezetap PoS solution through a native android SDK implementation.

######This documentation contains the instructions to integrate-
1. Ezetap Native Android SDK
2. Ezetap Cordova SDK

#1. Native Android Integration

If you have an Deploy-able Android Native Application this API will help you integrate Ezetap Services to your App. To integrate this API, you need to have a good grasp of Android app development, building APKs etc. The Ezetap integration part involves setting up a project, importing a library and then actual coding with just a few lines of code.

## Getting Started
1. Android development environment
2. Android phone that can connect to internet
3. This documentation
4. Ezetap app key or login credentials to Ezetap service
5. Ezetap device to test

##Sample App
There is a sample Android App inside the sample folder of the repository. You can use this project as a reference to integrate Ezetap SDK.

#####Follow the steps below to get the demo app working-
1. Import the project as an Android Project in Eclipse IDE.
2. Clean & Build the project.
3. Run the EzeNativeSampleActivity on your Smartphone.
4. EzeNativeSampleActivity.java will be your point of reference for Native Android SDK integration

>Note- The errors you may face while importing the project will most likely be for Android version mismatch which EclipseIDE would normally resolve itself. Changing the Android version or restarting the Eclipse can help u solve this problem

## Steps to follow-
* You can find EzeAPI jar file in the releases folder of this repository, Add the jar file in <a href="https://github.com/ezetap/client-sdk-android/tree/master/release">libs/</a> folder of your Native Android Project.
* In the manifest file of your Android Project add a new Activity, which looks like this-
```xml
		<activity android:name="com.eze.api.EzeAPIActivity"
	        android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale"
	        android:screenOrientation="portrait"
	        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" />
```
* Good to go, please refer Ezetap API portal for API usage

>Note- The EzeAPIActivity has to be configured with the same attributes as given above.

#2. Cordova Integration

If you have an Deploy-able Android Hybrid Application built on Cordova platform, this API will help you integrate Ezetap Services to your App. To integrate this API, you need to know how Cordova works & how to configure Cordova Plugin for Android. Ezetap provides a Cordova plugin which is embedded in the SDK which does all the hard work, all you need to do is to configure our plugin to your Android Cordova Project and write simple code snippet to invoke our Plugin.

## Getting Started
1. Android development environment
2. Cordova development setup
3. Android phone that can connect to internet
4. This documentation
5. Ezetap app key or login credentials to Ezetap service
6. Ezetap device to test

##Sample App
There is a sample Android App inside the sample folder of the repository. You can use this project as a reference to integrate Ezetap SDK.

#####Follow the steps below to get the demo app working-
1. Import the project as an Android Project in Eclipse IDE.
2. Clean & Build the project.
3. Run the EzeCordovaSampleActivity on your Smartphone.
4. Ezehelper.js will be your point of reference for Cordova Android SDK integration

>Note- The errors you may face while importing the project will most likely be for Android version mismatch which EclipseIDE would normally resolve itself. Changing the Android version or restarting the Eclipse can help u solve this problem

## Steps to follow-
* You can find EzeAPI jar file in the releases folder of this repository, Add the jar file in <a href="https://github.com/ezetap/client-sdk-android/tree/master/release">libs/</a> folder of your Cordova Android Project.
* In res>xml>config.xml add a new plugin, which looks like this-
```xml
	    <feature name="EzeAPIPlugin">
	        <param name="android-package" value="com.eze.api.EzeAPIPlugin" />
	    </feature>
```
* In the manifest file of your Cordova Android Project add a new Activity, which looks like this-
```xml
    <activity android:name="com.eze.api.EzeAPIActivity"
        android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale"
        android:screenOrientation="portrait"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" />
```
* Good to go, please refer Ezetap API portal for API usage

>Note- The Ezetap Cordova Plugin and the EzeAPIActivity has to be configured with the same attributes as given above.

