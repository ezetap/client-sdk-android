/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * <p/>
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *******************************************************/
package com.eze.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.eze.api.EzeAPIConstants.EzetapErrors;

import org.json.JSONArray;
import org.json.JSONObject;

public class EzeAPI {

	/**
	 * Method to initialize the Ezetap transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void initialize(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "initialize",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to close prepare device before any transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 */
	public static void prepareDevice(Context context, int requestCode) {
		sendRequestIntent(context, "prepareDevice", returnRequestParams(""),
				requestCode);
	}

	/**
	 * Method to start a wallet transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void walletTransaction(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "walletTransaction",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to start a card transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void cardTransaction(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "cardTransaction",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to start a cash transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void cashTransaction(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "cashTransaction",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to search transaction(s)
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void searchTransaction(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "searchTransaction",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to void transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param String
	 *            transactionID - The ID of the transaction which is to be
	 *            voided
	 */
	public static void voidTransaction(Context context, int requestCode,
			String transactionID) {
		sendRequestIntent(context, "voidTransaction",
				returnRequestParams(transactionID), requestCode);
	}

	/**
	 * Method to attach Signature for a transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 * @param JSONObject
	 *            requestObject - The request object as defined in the API
	 *            documentation
	 */
	public static void attachSignature(Context context, int requestCode,
			JSONObject requestObject) {
		sendRequestIntent(context, "attachSignature",
				returnRequestParams(requestObject), requestCode);
	}

	/**
	 * Method to update ezetap Device
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 */
	public static void updateDevice(Context context, int requestCode) {
		sendRequestIntent(context, "update", returnRequestParams(""),
				requestCode);
	}

	/**
	 * Method to close Ezetap sessions
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 */
	public static void close(Context context, int requestCode) {
		sendRequestIntent(context, "close", returnRequestParams(""),
				requestCode);
	}

	/**
	 * Method to return request params
	 * 
	 * @param Object
	 *            obj - The request object which should be bundled inside an
	 *            array.
	 */
	private static String returnRequestParams(Object obj) {
		JSONArray array = new JSONArray();
		try {
			array.put(0, obj);
		} catch (Exception e) {
		}
		return array.toString();
	}

	/**
	 * Method to attach Signature for a transaction
	 * 
	 * @param Context
	 *            context - Context of the calling Activity, the result will be
	 *            supplied to the onActivityResult method of the calling
	 *            Activity.
	 * @param String
	 *            apiName - Name of the API
	 * @param String
	 *            params - The request object as defined in the API
	 *            documentation
	 * @param int requestCode - Request code you wish to handle for the result
	 *        in onActivityResult method.
	 */
	private static void sendRequestIntent(Context context, String apiName,
			String params, int requestCode) {
		try {
			Intent intent = new Intent(context, EzeAPIActivity.class);
			intent.setAction(apiName);
			intent.putExtra("params", params);
			((Activity) context).startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context,
					EzetapErrors.ERROR_MANIFEST_ACTIVITY.getErrorMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
