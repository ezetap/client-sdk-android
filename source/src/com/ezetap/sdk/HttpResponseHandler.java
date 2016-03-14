package com.ezetap.sdk;

import org.json.JSONObject;

public interface HttpResponseHandler {

	public void handleResponse(JSONObject response, int requestCode);
	public void handleError(Exception e, String msg, int requestCode);
}
