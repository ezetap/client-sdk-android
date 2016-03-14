package com.ezetap.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtils {
	
	Activity activity;
	ExternalServiceFetcher serviceFetcher;
	String username = null;
	String appKey = null;
	String serviceAppId = "ezetap_android_service";
	ProgressDialog progressDialog;
	
	public NetworkUtils(Activity pActivity, String username, String appKey) {
		if(serviceFetcher == null) {
			serviceFetcher = new ServerTimeFetcher();
		}
		this.activity = pActivity;
		this.username = username;
		this.appKey = appKey;
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("Fetching");
		progressDialog.setMessage("Fetching Ezetap Service Application.\nPlease wait...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				progressDialog.dismiss();
				progressDialog = null;
				activity = null;
				Handler handler = EzetapUtils.getHandler();
				if(handler != null) {
					Message msg = handler.obtainMessage();
					msg.what = EzeConstants.RESULT_DOWNLOAD_ABORTED;
					handler.sendMessage(msg);
				}				
			}
		});
	}

	public void getAppStatus() {
		progressDialog.show();
		serviceFetcher.fetch();	
	}
	
	private interface ExternalServiceFetcher {
		public void fetch();
	}
	
	private class ServerTimeFetcher implements ExternalServiceFetcher {
		
		public ServerTimeFetcher(){
		}
		
		@Override
		public void fetch() {
			try {
				HttpClientUtils customerDetailsCall = new HttpClientUtils();
				JSONObject json = new JSONObject();
				json.put(EzeConstants.KEY_APPKEY, appKey);
				json.put(EzeConstants.KEY_USERNAME, username);
				JSONObject apps = new JSONObject();
				apps.put(EzeConstants.KEY_APPLICATION_ID, serviceAppId);
				apps.put(EzeConstants.KEY_VERSION_CODE, AppConstants.COMPATIBLE_SERVICE_APP_VERSION_CODE);
				json.put(EzeConstants.KEY_APPS, new JSONArray().put(apps));
				customerDetailsCall.process(EzetapServiceBaseConstants.VAL_POST, AppConstants.APP_STATUS_URL, json, 1001, new EzetapExternalServiceResponseHandler());
			} catch (JSONException e) { }
		}
	}
	
	
	private class EzetapExternalServiceResponseHandler implements HttpResponseHandler {
		@Override
		public void handleResponse(JSONObject response, int requestCode) {
			if(progressDialog != null)
				progressDialog.cancel();
			if(activity == null)
				return;
			String errorCode = "Ezetap Error";
			String errorMessage = "Please try again later or contact Ezetap support";				
			try {
				if(response != null && response.has("success")) {
					if(response.getBoolean("success")) {
						if(response.has(EzeConstants.KEY_APPS) && response.getJSONArray(EzeConstants.KEY_APPS).length() > 0) {
							JSONArray apps = response.getJSONArray(EzeConstants.KEY_APPS);
							boolean urlFound = false;
							for(int i = 0; i < apps.length(); i++) {
								JSONObject appData = apps.getJSONObject(i);
								if(appData.has(EzeConstants.KEY_APPLICATION_ID) && 
									appData.getString(EzeConstants.KEY_APPLICATION_ID).equalsIgnoreCase(serviceAppId)) {
									if(appData.has("downloadUrl")) {
										urlFound = true;
										final String downloadUrl = appData.getString("downloadUrl");
										activity.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												EzetapDownloadUtils utils = new EzetapDownloadUtils(downloadUrl, activity, serviceAppId);
												utils.start();
											}
										});
										return;
									}
								}
							}
							if(!urlFound) {
								errorMessage = "Download url not found. Please try again or contact Ezetap support";
								showError(errorMessage, errorCode);
							}
						} else {
							errorMessage = "App data not found. Please try again or contact Ezetap support";
							showError(errorMessage, errorCode);
						}
					} else {
						if(response.has(EzeConstants.KEY_ERROR_CODE))
							errorCode = response.getString(EzeConstants.KEY_ERROR_CODE);
						if(response.has(EzeConstants.KEY_ERROR_MESSAGE))
							errorMessage = response.getString(EzeConstants.KEY_ERROR_MESSAGE);
						showError(errorMessage, errorCode);
					}
				} else {
					showError(errorMessage, errorCode);
				}
			} catch(JSONException e) {
				showError(errorMessage, errorCode);
			}
		}

		@Override
		public void handleError(Exception e, String msg, int requestCode) {
			if(progressDialog != null)
				progressDialog.cancel();
			if(activity == null)
				return;
			showError(msg, "Ezetap Error");
		}
		
		private void showError(final String errorMessage, final String errorCode) {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					final AlertDialog builder = new AlertDialog.Builder(activity).create();
					builder.setCancelable(false);
					builder.setCanceledOnTouchOutside(false);
					builder.setTitle(errorCode);
					builder.setMessage(errorMessage);
					builder.setButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							builder.dismiss();
							Handler handler = EzetapUtils.getHandler();
							if(handler != null) {
								Message msg = handler.obtainMessage();
								msg.what = EzeConstants.RESULT_DOWNLOAD_FAILURE;
								handler.sendMessage(msg);
							}
						}
					});
					builder.show();
				}
			});
		}
	}
}