/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/

package com.ezetap.sdk;

import static com.ezetap.sdk.AppConstants.BASE_PACKAGE;

import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ezetap.sdk.EzeConstants.LoginAuthMode;

/**
 * Utils used by the SDK
 */

public class EzetapUtils {

	public static final String EZETAP_PACKAGE_ACTION = ".EZESERV";
	private static final String DEBUG_TAG = "EzeUtils";

	private static Handler eventHandler;
	
	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param amount
	 *            - Transaction amount
	 * @param tip
	 *            - Tip for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of customer (optional)
	 * @param context
	 *            - Calling activity's context
	 * @param appKey
	 *            - Ezetap Licence key for your organization
	 * @param username
	 *            - Ezetap username
	 * @param reqCode
	 *            - Request code for handling response
	 * @deprecated
	 */
	public void startCardPayment(double amount, double tip, String mobile, Activity context, String appKey, String username, String orderNumber, int reqCode, Hashtable<String, Object> appData, boolean enableCustomLogin) {
		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().startCardPayment(context, reqCode, username, amount, orderNumber, tip, mobile, appData);
		}
	}

	/**
	 * To initiate a cash payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param amount
	 *            - Transaction amount
	 * @param tip
	 *            - Tip for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of customer (optional)
	 * @param context
	 *            - Calling activity's context
	 * @param appKey
	 *            - Ezetap Licence key for your organization
	 * @param username
	 *            - Ezetap username
	 * @param reqCode
	 *            - Request code for handling response
	 * @deprecated
	 */
	public void startCashPayment(double amount, double tip, String mobile, Activity context, String appKey, String username, String orderNumber, String customerName, int reqCode, boolean enableCustomLogin) {
		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().startCashPayment(context, reqCode, username, amount, orderNumber, tip, mobile, customerName);
		}
	}

	/**
	 * To initiate a void transaction request, application should call this
	 * method with following parameters:
	 * 
	 * @param txnID
	 *            - Transaction ID to void
	 * @param context
	 *            - Calling activity's context
	 * @param appKey
	 *            - Ezetap Licence key for your organization
	 * @param username
	 *            - Ezetap username
	 * @param reqCode
	 *            - Request code for handling response
	 */
	public void startVoidTransaction(String txnID, Activity context, String appKey, String username, int reqCode, boolean enableCustomLogin) {
		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().startVoidTransaction(context, reqCode, username, txnID);
		}
	}

	/**
	 * To logout current sesssion associated with AppKey , application should
	 * call this method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param appKey
	 *            - Ezetap Licence key for your organization
	 * @param reqCode
	 *            - Request code for handling response
	 * @deprecated
	 */
	public void logout(Activity context, String appKey, String username, int reqCode) {

		EzetapPayApis.create().logout(context, reqCode, username);
	}

	/**
	 * Attach signature to existing transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param txnID
	 *            - Transaction ID to attach signature
	 * @param context
	 *            - Calling activity's context
	 * @param appKey
	 *            - Ezetap Licence key for your organization
	 * @param username
	 *            - Ezetap username
	 * @param reqCode
	 *            - Request code for handling response
	 */
	public void attachSignature(String txnID, Activity context, String appKey, String username, int reqCode, boolean enableCustomLogin) {
		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().attachSignature(context, reqCode, username, txnID);
		}
	}

	protected static String findTargetAppPackage(Intent intent, Activity context) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> availableApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (availableApps != null) {
			for (ResolveInfo availableApp : availableApps) {
				String packageName = availableApp.activityInfo.packageName;
				if (BASE_PACKAGE.equals(packageName)) {
					return packageName;
				}
			}
		}
		return null;
	}
	
	protected boolean isServiceAppCompatible(Intent intent, Activity context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if (BASE_PACKAGE.equals(p.packageName)) {
				if(p.versionCode < AppConstants.COMPATIBLE_SERVICE_APP_VERSION_CODE)
					return false;
				else 
					return true;
			}
		}
		return false;
	}

	protected AlertDialog showDownloadDialog(final Activity context, final String username, final String appKey) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(context);
		downloadDialog.setTitle("Install Ezetap Service Application");
		downloadDialog.setCancelable(false);
		downloadDialog.setMessage("This application requires Ezetap Service Application. Would you like to install it?");
		downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				try {
					NetworkUtils utils = new NetworkUtils(context, username, appKey);
					utils.getAppStatus();
				} catch (ActivityNotFoundException anfe) {
					Log.v(DEBUG_TAG, "Could not install Ezetap Service Application.");
				}
			}
		});
		downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				context.setResult(EzeConstants.RESULT_INSTALL_CANCELLED);
				if(eventHandler != null) {
					Message msg = eventHandler.obtainMessage();
					msg.what = EzeConstants.RESULT_INSTALL_CANCELLED;
					eventHandler.sendMessage(msg);
				}
			}
		});
		return downloadDialog.show();
	}
	
	protected AlertDialog showDownloadDialog1(final Activity context, final String username, final String appKey) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(context);
		downloadDialog.setTitle("Install Compatible Ezetap Service Application");
		downloadDialog.setCancelable(false);
		downloadDialog.setMessage("This SDK requires new Ezetap Service Application which supports new features added in SDK. Would you like to install it?");
		downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				try {
					NetworkUtils utils = new NetworkUtils(context, username, appKey);
					utils.getAppStatus();
				} catch (ActivityNotFoundException anfe) {
					Log.v(DEBUG_TAG, "Could not install Ezetap Service Application.");
				}
			}
		});
		downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				if(eventHandler != null) {
					Message msg = eventHandler.obtainMessage();
					msg.what = EzeConstants.RESULT_INSTALL_CANCELLED;
					eventHandler.sendMessage(msg);
				}
			}
		});
		return downloadDialog.show();
	}
	
	public static void setHandler(Handler handler) {
		eventHandler = handler;
	}
	
	public static Handler getHandler() {
		return eventHandler;
	}

	/**
	 * @param username
	 * @param password
	 * @param Activity
	 * @param ezetapLicenceKey
	 * @param reqCode
	 * @deprecated
	 */
	public void startLoginRequest(String username, String password, Activity context, String ezetapLicenceKey, int reqCode) {
		EzetapPayApis.create().startLoginRequest(context, reqCode, username, password);
	}

	/**
	 * @param username
	 * @param password
	 * @param Activity
	 * @param ezetapLicenceKey
	 * @param reqCode
	 * @deprecated
	 */
	public void startChangePasswordRequest(String username, String oldPassword, String newPassword, Activity context, String ezetapLicenceKey, int reqCode) {
		EzetapPayApis.create().startChangePasswordRequest(context, reqCode, username, oldPassword, newPassword);
	}

	/**
	 * registerDongle : To register or change the ownership of Ezetap Device
	 * 
	 * @param username
	 * @param Activity
	 * @param ezetapLicenceKey
	 * @param reqCode
	 * @param enableCustomLogin
	 * @deprecated
	 */
	public void registerDongle(String username, Activity context, String appKey, int reqCode, boolean enableCustomLogin) {
		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().registerDongle(context, reqCode, username);
		}
	}

	/**
	 * @param username
	 * @param Activity
	 * @param ezetapLicenceKey
	 * @param reqCode
	 * @param enableCustomLogin
	 * @deprecated
	 */
	public void fetchTransactionHistory(String username, Activity context, String appKey, int reqCode, boolean enableCustomLogin) {

		if ((enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_CUSTOM) || (!enableCustomLogin && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_PROMPT) || ((appKey != null) && AppConstants.AUTH_MODE == LoginAuthMode.EZETAP_LOGIN_BYPASS)) {
			EzetapPayApis.create().getTransactionHistory(context, reqCode, username);
		}

	}

	public static boolean isEzetapApplicationInstalled(Activity activity) {
		Intent intent = new Intent();
		intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);

		String targetAppPackage = findTargetAppPackage(intent, activity);
		return !(targetAppPackage == null);
	}
}
