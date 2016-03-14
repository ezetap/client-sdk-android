/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/
package com.eze.api;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.eze.api.EzeAPIConstants.EzetapErrors;
import com.ezetap.sdk.AppConstants;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.widget.Toast;
import com.eze.api.EzeAPIConstants.EzetapErrors;

public class EzeAPIPlugin extends CordovaPlugin {

	/**
	 * Variable to save the context of the Cordova Plugin
	 * */
	private CallbackContext callbackCtxt;

	/**
	 * Request code used to communicate with EzeAPIActivity
	 * */
	private static final int EZE_REQUESTCODE=3230111;

	/**
	 * Method to initialize EzeAPIPlugin Cordova Plugin.
	 * */
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}


	/**
	 * Method invoked when EzeAPIPlugin Cordova Plugin is invoked
	 * */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		super.execute(action, args, callbackContext);
		callbackCtxt=callbackContext;
		delegateRequest(action,args.toString());
		return true;
	}

	/**
	 * Method to delegate the request to EzeAPIActivity
	 * @param String api
	 * 					- Ezetap API name as passed from JS
	 * @param String params
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void delegateRequest(String api,String params) {
		try {
			AppConstants.APP_ID = "SDK_AND_COR";
			AppConstants.APP_NAME = "Android Cordova SDK";
			Intent intent = new Intent();
			intent.setClass(cordova.getActivity(),EzeAPIActivity.class);
			intent.setAction(api);
			intent.putExtra("params", params);
			cordova.startActivityForResult(this, intent, EZE_REQUESTCODE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(cordova.getActivity(), EzetapErrors.ERROR_MANIFEST_ACTIVITY.getErrorMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Call back method from EzeAPIActivity
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode==EZE_REQUESTCODE){
			switch (resultCode) {
			case Activity.RESULT_OK:
				if(intent!=null && intent.hasExtra("response"))
					callbackCtxt.success(intent.getStringExtra("response"));
				else
					callbackCtxt.error("{\"status\":\"fail\",\"result\":null,\"error\":{\"ERROR_CODE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode()
					+ "\",\"ERROR_MESSAGE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()
					+ "\"}}");
				break;
			case Activity.RESULT_CANCELED:
				if(intent!=null && intent.hasExtra("response"))
					callbackCtxt.error(intent.getStringExtra("response"));
				else
					callbackCtxt.error("{\"status\":\"fail\",\"result\":null,\"error\":{\"ERROR_CODE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode()
					+ "\",\"ERROR_MESSAGE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()
					+ "\"}}");
				break;
			default:
				break;
			}
		}
	}
}