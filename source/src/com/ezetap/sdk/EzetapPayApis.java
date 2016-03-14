/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/

package com.ezetap.sdk;

import static com.ezetap.sdk.AppConstants.BASE_PACKAGE;

import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.ezetap.sdk.EzeConstants.AppMode;
import com.ezetap.sdk.EzeConstants.CommunicationChannel;
import com.ezetap.sdk.EzeConstants.LoginAuthMode;

/**
 * Implementation of the Payment API
 */

public abstract class EzetapPayApis {

	private static final String APP_ID = "appId";
	private static final String APP_NAME = "appName";
	private static final String APP_VERSION = "appVersion";
	private static final String DISPLAY_VERSION = "displayVersion";
	private static final String HAS_VERSION_INFO = "hasVersionInfo";

	private EzetapPayApis() {

	}

	private static class EzetapPayApiImpl implements EzetapPayApi {
		public static final String EZETAP_PACKAGE_ACTION = ".EZESERV";
		private static final String DEBUG_TAG = "EzetapPayApiImpl";
		private String appKey = AppConstants.APP_KEY;
		private LoginAuthMode loginMode = AppConstants.AUTH_MODE;
		private String merchantName = AppConstants.MERCHANT_NAME;
		private String currencyCode = AppConstants.CURRENCY_CODE;
		private boolean captureSignature = false;
		private CommunicationChannel preferredCommChannel = CommunicationChannel.NONE;

		// default constructor
		private EzetapPayApiImpl() {

		}

		// additional constructor for replacing defaults if necessary
		private EzetapPayApiImpl(LoginAuthMode loginMode, String appKey, String merchantName, String currencyCode, AppMode mode, boolean captureSignature, CommunicationChannel defaultChannel) {
			if (loginMode != null)
				this.loginMode = loginMode;

			if (appKey != null)
				this.appKey = appKey;

			if (merchantName != null)
				this.merchantName = merchantName;

			if (currencyCode != null)
				this.currencyCode = currencyCode;
			// Commented as currently only demo is available

			if (mode != null && mode.equals(AppMode.EZETAP_DEMO)) {
				AppConstants.BASE_PACKAGE = AppConstants.DEMO_BASE_PACKAGE;
				AppConstants.APP_STATUS_URL = AppConstants.DEMO_APP_STATUS_URL;
			}
			if (mode != null && mode.equals(AppMode.EZETAP_PROD)) {
				AppConstants.BASE_PACKAGE = AppConstants.PROD_BASE_PACKAGE;
				AppConstants.APP_STATUS_URL = AppConstants.PROD_APP_STATUS_URL;
			}
			if (mode != null && mode.equals(AppMode.EZETAP_PREPROD)) {
				AppConstants.BASE_PACKAGE = AppConstants.PREPROD_BASE_PACKAGE;
				AppConstants.APP_STATUS_URL = AppConstants.PREPROD_APP_STATUS_URL;
			}			 
			this.captureSignature = captureSignature;
			this.preferredCommChannel = defaultChannel;
		}

		public void checkForIncompleteTransaction(Activity context, int reqCode, String username) {
			Log.v(DEBUG_TAG, "Ezetap SDK checking for incomplete transaction");

			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_NONCE_CHECK);
				intent.putExtra(EzeConstants.KEY_USERNAME, username);
				intent.putExtra(EzeConstants.KEY_APPKEY, appKey);
				intent.putExtra(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#checkForUpdate(android.app.Activity,
		 * int, java.lang.String)
		 */
		public void checkForUpdate(Activity context, int reqCode, String username) {
			Log.v(DEBUG_TAG, "Ezetap SDK checking for Service app update");

			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				intent.putExtra(EzeConstants.KEY_HAS_VERSION_INFO, true);
				intent.putExtra(EzeConstants.KEY_APP_ID, AppConstants.APP_ID);
				intent.putExtra(EzeConstants.KEY_APP_NAME, AppConstants.APP_NAME);
				intent.putExtra(EzeConstants.KEY_APP_VERSION, AppConstants.SDK_VERSION);
				intent.putExtra(EzeConstants.KEY_DISPLAY_VERSION, AppConstants.SDK_VERSION);
				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_CHECK_UPDATES);
				intent.putExtra(EzeConstants.KEY_USERNAME, username);
				intent.putExtra(EzeConstants.KEY_APPKEY, appKey);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.ezetap.sdk.EzetapPayApi#startCardPayment(android.app.Activity,
		 * int, java.lang.String, double, java.lang.String, double,
		 * java.lang.String, java.lang.String, java.lang.String,
		 * java.util.Hashtable)
		 */

		@Override
		public void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, String externalReference2, String externalReference3, Hashtable<String, Object> appData) {

			_startCardPayment(context, reqCode, username, amount, 0, orderNumber, tip, mobile, null, externalReference2, externalReference3, appData);
		}
		

		@Override
		public void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData) {
			_startCardPayment(context, reqCode, username, amount, 0, orderNumber, tip, mobile, emailId, externalReference2, externalReference3, appData);
		}

		// master
		private void _startCardPayment(Activity context, int reqCode, String username, double amount, double amountCashBack, String orderNumber, double tip, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData) {
			_startCardPayment(context, reqCode, username, amount, amountCashBack, orderNumber, tip, mobile, emailId, externalReference2, externalReference3, appData, null);
		}

		// master
		private void _startCardPayment(Activity context, int reqCode, String username, double amount, 
				double amountCashBack, String orderNumber, double tip, String mobile, String emailId, 
				String externalReference2, String externalReference3, 
				Hashtable<String, Object> appData, Hashtable<String, Object> additionalData) {
			Log.v(DEBUG_TAG, "amount=" + amount + " tip=" + tip + " mobile=" + mobile);
			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_PAYCARD);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				reqData.put(EzeConstants.KEY_AMOUNT_CASH_BACK, Double.valueOf(amountCashBack));
				if (tip > 0.0) {
					reqData.put(EzeConstants.KEY_TIP_AMOUNT, Double.valueOf(tip));
				}
				// ensure you pass the username AND your appkey
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, mobile);

				if (emailId != null) {
					reqData.put(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
					intent.putExtra(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
				}

				// reqData.put(EzeConstants.KEY_APPKEY, appKey);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				if (externalReference2 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_2, externalReference2);
				if (externalReference3 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_3, externalReference3);

				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;

							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}

							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
				}

				if (additionalData != null) {
					JSONObject addData = new JSONObject();
					Enumeration<String> keys = additionalData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = additionalData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;
							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							addData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							addData.put(aKey, aVal);
						}
						else if (aVal instanceof Boolean){
							addData.put(aKey, aVal);
						}
					}
					intent.putExtra(EzeConstants.KEY_ADDITIONAL_DATA, addData.toString());
				}
				
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#startCardPayment(double, double,
		 * java.lang.String, android.app.Activity, java.lang.String,
		 * java.lang.String, java.lang.String, int, java.util.Hashtable,
		 * boolean)
		 */
		@Override
		public void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, Hashtable<String, Object> appData) {
			Log.v(DEBUG_TAG, "amount=" + amount + " tip=" + tip + " mobile=" + mobile);

			_startCardPayment(context, reqCode, username, amount, 0, orderNumber, tip, mobile, null, null, null, appData);
		}
		
		@Override
		public void startCardPayment(Activity context, int reqCode,
				String username, double amount, double amountCashBack,
				String orderNumber, double tip, String mobile, String emailId,
				String externalReference2, String externalReference3,
				Hashtable<String, Object> appData) {
			_startCardPayment(context, reqCode, username, amount, amountCashBack, orderNumber, tip, mobile, emailId, externalReference2, externalReference3, appData);
		}
		
		@Override
		public void startPreAuth(Activity context, int reqCode, String username, double amount, 
			String orderNumber, String mobile, String emailId, String externalReference2, 
			String externalReference3, Hashtable<String, Object> appData){
			try {
				Intent intent = createIntent();
				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_AUTHORISE_CARD);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				// ensure you pass the username AND your appkey
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, mobile);

				if (emailId != null) {
					reqData.put(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
					intent.putExtra(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
				}

				// reqData.put(EzeConstants.KEY_APPKEY, appKey);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				if (externalReference2 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_2, externalReference2);
				if (externalReference3 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_3, externalReference3);

				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;
							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
				}
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				intent.putExtra(EzeConstants.ALLOW_SDK_DEBUGGING, false);
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void confirmPreAuth(Activity context, int reqCode, String username, double amount, 
			String orderNumber, String mobile, String emailId, String externalReference2, 
			String externalReference3, Hashtable<String, Object> appData){
			try {
				Intent intent = createIntent();
				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra(EzeConstants.KEY_ENABLE_CUSTOM_LOGIN,false);
				
				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_CONFIRM_PRE_AUTH);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, mobile);

				if (emailId != null) {
					reqData.put(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
					intent.putExtra(EzeConstants.KEY_CUSTOMER_EMAIL, emailId);
				}

				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				if (externalReference2 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_2, externalReference2);
				if (externalReference3 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_3, externalReference3);

				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;
							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
				}
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				
				intent.putExtra(EzeConstants.ALLOW_SDK_DEBUGGING, false);
				intent.putExtra("isCachingEnabled", false);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);		

			} catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void releasePreAuth(Activity context, int reqCode, String username, double amount, String orderNumber, Hashtable<String, Object> appData){
			try {
				Intent intent = createIntent();
				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				
				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra(EzeConstants.KEY_ENABLE_CUSTOM_LOGIN,false);
				intent.putExtra(EzeConstants.ALLOW_SDK_DEBUGGING, false);
				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_RELEASE_PRE_AUTH);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				
				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;
							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
				}
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("isCachingEnabled", false);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);		
			} catch(Exception e){
				e.printStackTrace();
			}			
			
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#startCashPayment(double, double,
		 * java.lang.String, android.app.Activity, java.lang.String,
		 * java.lang.String, java.lang.String, java.lang.String, int, boolean)
		 */
		@Override
		public void startCashPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, String customerName) {
			Log.v(DEBUG_TAG, "amount=" + amount + " tip=" + tip + " mobile=" + mobile);

			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_PAY_CASH);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				if (tip > 0.0) {
					reqData.put(EzeConstants.KEY_TIP_AMOUNT, Double.valueOf(tip));
				}
				reqData.put(EzeConstants.KEY_USERNAME, username);
				// reqData.put(EzeConstants.KEY_APPKEY, appKey);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_NAME, customerName);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, mobile);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);

				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#startVoidTransaction(java.lang.Long,
		 * android.app.Activity, java.lang.String, java.lang.String, int,
		 * boolean)
		 */

		@Override
		public void startVoidTransaction(Activity context, int reqCode, String username, String txnId) {
			Log.v(DEBUG_TAG, "txnID=" + txnId);

			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_VOID);

				reqData.put(EzeConstants.KEY_TRANSACTION_ID, txnId);
				reqData.put(EzeConstants.KEY_USERNAME, username);
				// reqData.put(EzeConstants.KEY_APPKEY, appKey);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#logout(android.app.Activity,
		 * java.lang.String, java.lang.String, int)
		 */
		public void logout(Activity context, int reqCode, String username) {
			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_LOGOUT);
				reqData.put(EzeConstants.KEY_USERNAME, username);

				// reqData.put(EzeConstants.KEY_APPKEY, appKey);

				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#attachSignature(java.lang.Long,
		 * android.app.Activity, java.lang.String, java.lang.String, int,
		 * boolean)
		 */

		@Override
		public void attachSignature(Activity context, int reqCode, String username, String txnId) {
			Log.v(DEBUG_TAG, "Attach Signature txnID=" + txnId);

			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_ATTACH_SIGNATURE);

				reqData.put(EzeConstants.KEY_TRANSACTION_ID, txnId);
				reqData.put(EzeConstants.KEY_USERNAME, username);
				// reqData.put(EzeConstants.KEY_APPKEY, appKey);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void attachSignature(Activity context, int reqCode, String username, String txnId, Bitmap bitmap, CompressFormat format) {
			attachSignature(context, reqCode, username, txnId, null, bitmap, format);
		}
		
		public void attachSignature(Activity context, int reqCode, String username, String txnId, String emiId, Bitmap bitmap, CompressFormat format) {
			Log.v(DEBUG_TAG, "Attach Signature txnID=" + txnId);

			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_ATTACH_SIGNATURE_EX);

				reqData.put(EzeConstants.KEY_TRANSACTION_ID, txnId);
				reqData.put(EzeConstants.KEY_USERNAME, username);
				if(emiId != null)
					reqData.put(EzeConstants.KEY_EMI_ID, emiId);
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				ByteArrayOutputStream s = new ByteArrayOutputStream();
				bitmap.compress(format, 80, s);
				s.flush();
				s.close();

				String signatureFormat = null;
				if (format.equals(CompressFormat.PNG)) {
					signatureFormat = "image/png";
				} else if (format.equals(CompressFormat.JPEG)) {
					signatureFormat = "image/jpeg";
				}
				reqData.put(EzeConstants.KEY_SIGNATURE_WIDTH, width);
				reqData.put(EzeConstants.KEY_SIGNATURE_HEIGHT, height);
				reqData.put(EzeConstants.KEY_SIGNATURE_FORMAT, signatureFormat);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				intent.putExtra(EzeConstants.KEY_SIGNATURE_DATA, s.toByteArray());
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra(EzeConstants.KEY_APPKEY, appKey);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#startLoginRequest(java.lang.String,
		 * java.lang.String, android.app.Activity, java.lang.String, int)
		 */
		public void startLoginRequest(Activity context, int reqCode, String username, String password) {

			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_LOGIN);
				reqData.put(EzeConstants.KEY_PASSWORD, password);
				reqData.put(EzeConstants.KEY_USERNAME, username);

				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());

				// intent.putExtra("DBG_HOST", "10.0.1.118:8080");
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.ezetap.sdk.EzetapPayApi#startChangePasswordRequest(java.lang.
		 * String, java.lang.String, java.lang.String, android.app.Activity,
		 * java.lang.String, int)
		 */
		public void startChangePasswordRequest(Activity context, int reqCode, String username, String oldPassword, String newPassword) {

			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_CHANGE_PWD);

				intent.putExtra(EzeConstants.KEY_PASSWORD, oldPassword);
				intent.putExtra(EzeConstants.KEY_NEW_PASSWORD, newPassword);

				intent.putExtra(EzeConstants.KEY_USERNAME, username);
				// intent.putExtra(EzeConstants.KEY_APPKEY, ezetapLicenceKey);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ezetap.sdk.EzetapPayApi#registerDongle(java.lang.String,
		 * android.app.Activity, java.lang.String, int, boolean)
		 */
		@Override
		public void registerDongle(Activity context, int reqCode, String username) {
			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				// put request parameters
				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_REGISTER_DONGLE);
				intent.putExtra(EzeConstants.KEY_USERNAME, username);
				// intent.putExtra(EzeConstants.KEY_APPKEY, ezetapLicenceKey);

				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.ezetap.sdk.EzetapPayApi#fetchTransactionHistory(java.lang.String,
		 * android.app.Activity, java.lang.String, int, boolean)
		 */

		@Override
		public void getTransactionHistory(Activity context, int reqCode, String username) {
			try {
				Intent intent = new Intent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				// put request parameters
				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_TXN_HISTORY);
				intent.putExtra(EzeConstants.KEY_USERNAME, username);
				// intent.putExtra(EzeConstants.KEY_APPKEY, ezetapLicenceKey);

				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void getCardInfo(Activity context, int reqCode, String username) {

			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				// put request parameters
				intent.putExtra(EzeConstants.KEY_ACTION, EzeConstants.ACTION_GET_CARD_INFO);
				intent.putExtra(EzeConstants.KEY_USERNAME, username);

				if (!checkAndSetLoginMode(loginMode, intent, username, null, appKey, merchantName, currencyCode))
					return;
				intent.putExtra(EzeConstants.KEY_APPKEY, appKey);
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String findTargetAppPackage(Intent intent, Activity context) {
			return new EzetapUtils().findTargetAppPackage(intent, context);
		}

		private AlertDialog showDownloadDialog(Activity context, String username) {
			return new EzetapUtils().showDownloadDialog(context, username, appKey);
		}
		
		private AlertDialog showDownloadDialog1(Activity context, String username) {
			return new EzetapUtils().showDownloadDialog1(context, username, appKey);
		}
		
		private boolean isServiceAppCompatible(Intent intent, Activity context) {
			return new EzetapUtils().isServiceAppCompatible(intent, context);
		}

		// returns false on error
		private boolean checkAndSetLoginMode(LoginAuthMode loginMode, Intent intent, String userName, JSONObject reqData, String appKey, String merchantName, String currencyCode) {
			if (userName == null || userName.length() == 0) {
				Log.v(DEBUG_TAG, ">> UserName is Mandatory for all login modes !!!");
				return false;
			}

			if (loginMode == LoginAuthMode.EZETAP_LOGIN_CUSTOM) {
				intent.putExtra(EzeConstants.KEY_ENABLE_CUSTOM_LOGIN, !(AppConstants.LOGIN_SESSION_TIME_OUT_AUTO_HANDLE)); // if
																															// auto
			} else if (loginMode == LoginAuthMode.EZETAP_LOGIN_PROMPT) {
				intent.putExtra(EzeConstants.KEY_ENABLE_CUSTOM_LOGIN, false);
			} else if (loginMode == LoginAuthMode.EZETAP_LOGIN_BYPASS) {
				if (appKey == null || appKey.length() == 0) {
					// appkey is mandatory ideally throw exception
					Log.v(DEBUG_TAG, ">> AppKey is Mandatory for EZETAP_LOGIN_BYPASS mode !!!");
					return false;
				}

				intent.putExtra(EzeConstants.KEY_IS_USER_VALIDATED_BY_MERCHANT, true);

				if (merchantName == null || merchantName.length() == 0) {
					// Merchant Name is mandatory ideally throw exception
					Log.v(DEBUG_TAG, ">> Merchant Name is Mandatory for EZETAP_LOGIN_BYPASS mode !!!");
					return false;
				}

				if (currencyCode == null || currencyCode.length() == 0) {
					// Currency code is mandatory ideally throw exception
					Log.v(DEBUG_TAG, ">> Currency code is Mandatory for EZETAP_LOGIN_BYPASS mode !!!");
					return false;
				}
				try {
					if (reqData != null) {
						reqData.put(EzeConstants.KEY_APPKEY, appKey);
						reqData.put(EzeConstants.KEY_MERCHANT_NAME, merchantName);
						reqData.put(EzeConstants.KEY_CURRENCY_CODE, currencyCode);
					} else {
						intent.putExtra(EzeConstants.KEY_APPKEY, appKey);
						intent.putExtra(EzeConstants.KEY_MERCHANT_NAME, merchantName);
						intent.putExtra(EzeConstants.KEY_CURRENCY_CODE, currencyCode);
					}

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			return true;
		}

		private void _initializeDevice(Activity context, int reqCode, String username, Hashtable<String, Object> appData) {
			Log.v(DEBUG_TAG, "Ezetap SDK checking for Service app update");

			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());
				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_INIT_DEVICE_SESSION);
				reqData.put(EzeConstants.KEY_USERNAME, username);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;

							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}

							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}

				}
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void initializeDevice(Activity context, int reqCode, String username, Hashtable<String, Object> appData) {
			_initializeDevice(context, reqCode, username, appData);
		}
		
		@Override
		public void initializeDevice(Activity context, int reqCode, String username) {
			_initializeDevice(context, reqCode, username, null);
		}

		@Override
		public void startCardPayment(Activity context, int reqCode, String username, double amount, 
				double amountCashback, String orderNumber, double tip, String mobile, String emailID, 
				Hashtable<String, Object> appData,
				Hashtable<String, Object> addData) {
			_startCardPayment(context, reqCode, username, amount, amountCashback, orderNumber, tip, mobile, emailID, null, null, appData, addData);
		}

		@Override
		public void startWalletPayment(Activity context, int reqCode, String username, double amount, 
			String orderNumber, String customerMobile, String customerName, String emailAddress, String[] labels) {
			Log.v(DEBUG_TAG, "amount=" + amount + " mobile=" + customerMobile);
			try {
				Intent intent = createIntent();
				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra("labels", labels);

				JSONObject reqData = new JSONObject();
				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_PAY_WALLET);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_NAME, customerName);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, customerMobile);
				reqData.put(EzeConstants.KEY_CUSTOMER_EMAIL, emailAddress);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);
			} catch (Exception e) {
			}
		}

		@Override
		public void startCNPPayment(Activity context, int reqCode, String cnpURL, 
				String username, double amount, 
				String orderNumber,  String mobile, String emailID,
				String externalReference2, String externalReference3,
				Hashtable<String, Object> appData,
				Hashtable<String, Object> additionalData) {

			Log.v(DEBUG_TAG, "amount=" + amount  + " mobile=" + mobile);
			try {
				Intent intent = createIntent();

				intent.setAction(BASE_PACKAGE + EZETAP_PACKAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);

				String targetAppPackage = findTargetAppPackage(intent, context);
				if (targetAppPackage == null) {
					Log.v(DEBUG_TAG, "Ezetap app not found.");
					showDownloadDialog(context, username);
					return;
				}
				if(!isServiceAppCompatible(intent, context)){
					Log.v(DEBUG_TAG, "Compatible Ezetap app not found.");
					showDownloadDialog1(context, username);
					return;
				}
				intent.setPackage(targetAppPackage);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				JSONObject reqData = new JSONObject();
				if(preferredCommChannel != CommunicationChannel.NONE)
					reqData.put(EzeConstants.KEY_PREFERRED_COMM, preferredCommChannel.toString());

				reqData.put(EzeConstants.KEY_ACTION, EzeConstants.ACTION_PAYCNP);

				if (!checkAndSetLoginMode(loginMode, intent, username, reqData, appKey, merchantName, currencyCode))
					return;

				reqData.put(EzeConstants.KEY_AMOUNT, Double.valueOf(amount));
				
				// ensure you pass the username AND your appkey
				reqData.put(EzeConstants.KEY_CNPURL, cnpURL);
				reqData.put(EzeConstants.KEY_USERNAME, username);
				reqData.put(EzeConstants.KEY_ORDERID, orderNumber);
				reqData.put(EzeConstants.KEY_CUSTOMER_MOBILE, mobile);

				if (emailID != null) {
					reqData.put(EzeConstants.KEY_CUSTOMER_EMAIL, emailID);
					intent.putExtra(EzeConstants.KEY_CUSTOMER_EMAIL, emailID);
				}

				// reqData.put(EzeConstants.KEY_APPKEY, appKey);
				reqData.put(EzeConstants.KEY_CAPTURE_SIGNATURE, captureSignature);
				if (externalReference2 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_2, externalReference2);
				if (externalReference3 != null)
					reqData.put(EzeConstants.KEY_EXTERNAL_REF_3, externalReference3);

				if (appData != null) {
					Enumeration<String> keys = appData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = appData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;

							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}

							reqData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							reqData.put(aKey, aVal);
						}
					}
				}

				if (additionalData != null) {
					JSONObject addData = new JSONObject();
					Enumeration<String> keys = additionalData.keys();
					while (keys.hasMoreElements()) {
						String aKey = (String) keys.nextElement();
						Object aVal = additionalData.get(aKey);
						if (aVal instanceof String[]) {
							JSONArray jsonArr = new JSONArray();
							String[] arr = (String[]) aVal;
							for (int index = 0; index < arr.length; index++) {
								jsonArr.put(arr[index]);
							}
							addData.put(aKey, jsonArr);
						} else if (aVal instanceof String) {
							addData.put(aKey, aVal);
						}
						else if (aVal instanceof Boolean){
							addData.put(aKey, aVal);
						}
					}
					intent.putExtra(EzeConstants.KEY_ADDITIONAL_DATA, addData.toString());
				}
				
				Log.v(DEBUG_TAG, "MAP>>" + appData);
				Log.v(DEBUG_TAG, ">>" + reqData.toString(0));
				intent.putExtra(EzeConstants.KEY_JSON_REQ_DATA, reqData.toString());
				intent.putExtra("removeConfirmTransaction", true);
				context.startActivityForResult(intent, reqCode);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Call to create a EzetapPayApi instance used to call an Ezetap Payment API
	 * using default config
	 * 
	 * @return - a reference to a new instance of EzetapPayApi
	 */
	public static EzetapPayApi create() {
		return new EzetapPayApiImpl();
	}

	/**
	 * Call to create a EzetapPayApi instance used to call an Ezetap Payment API
	 * using custom overrides to specific params
	 * 
	 * @see EzetapApiConfig
	 * @param config
	 *            - an EzetapApiConfig instance containing the overridden
	 *            params.
	 * @return - a reference to a new instance of EzetapPayApi initialized with
	 *         the overrides passed
	 */

	public static EzetapPayApi create(EzetapApiConfig config) {
		EzetapPayApiImpl api = new EzetapPayApiImpl(config.getAuthMode(), config.getAppKey(), config.getMerchantName(), config.getCurrencyCode(), config.getMode(), config.isCaptureSignature(), config.getPreferredChannel());
		return api;
	}

	private final static Intent createIntent() {
		Intent intent = new Intent();
		intent.putExtra(HAS_VERSION_INFO, true);
		intent.putExtra(APP_ID, AppConstants.APP_ID);
		intent.putExtra(APP_NAME, AppConstants.APP_NAME);
		intent.putExtra(APP_VERSION, AppConstants.SDK_VERSION);
		intent.putExtra(DISPLAY_VERSION, AppConstants.SDK_DISPLAY_VERSION);
		return intent;
	}
}
