package com.ezetap.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Convenient bean representation of JSON data received.
 * 
 * @author vivek
 * 
 */
public class EzetapAppUpdateResponse {
	public static final int MANDATORY_UPDATE = 0;
	public static final int OPTIONAL_UPDATE = 1;

	private boolean success = false;
	private String errorCode;
	private String errorMessage;

	private boolean updateAvailable;
	private long filesize;
	private String ezetapAppId;
	private String downloadUrl;
	private int downloadSeverity;
	private String versionName;
	private String updateNotes;

	private String jsonRepresentation;

	public EzetapAppUpdateResponse() {

	}

	/**
	 * Creates a new instance based on the json data passed as String
	 * 
	 * @param json
	 * @throws Exception
	 */
	public EzetapAppUpdateResponse(String json) throws Exception {
		this(new JSONObject(json));
	}

	/**
	 * Creates a new instance based on the JSON Object passed
	 * 
	 * @param j
	 * @throws Exception
	 */
	public EzetapAppUpdateResponse(JSONObject j) throws Exception {
		if (j == null)
			throw new IllegalArgumentException("Invalid update response");
		if (!j.has("success"))
			throw new IllegalArgumentException("Invalid update response. Success flag not found");

		if (!j.has("apps"))
			throw new IllegalArgumentException("Invalid update response. App details not found");

		if (j.has("success"))
			this.success = j.getBoolean("success");

		JSONArray appArray = j.getJSONArray("apps");

		if (appArray.length() != 0) {
			JSONObject json = j.getJSONArray("apps").getJSONObject(0);
			if (json.has("upgrade"))
				this.updateAvailable = json.getBoolean("upgrade");
			if (json.has("fileSize"))
				this.filesize = json.getLong("fileSize");
			if (json.has("applicationId"))
				this.ezetapAppId = json.getString("applicationId");
			if (json.has("downloadUrl"))
				this.downloadUrl = json.getString("downloadUrl");
			if (json.has("severity"))
				this.downloadSeverity = json.getInt("severity");
			if (json.has("versionName"))
				this.versionName = json.getString("versionName");
			if (json.has("notes"))
				this.updateNotes = json.getString("notes");

			this.jsonRepresentation = json.toString();
		}

	}

	public boolean isUpdateAvailable() {
		return updateAvailable;
	}

	public void setUpdateAvailable(boolean updatevailable) {
		this.updateAvailable = updatevailable;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getEzetapAppId() {
		return ezetapAppId;
	}

	public void setEzetapAppId(String ezetapAppId) {
		this.ezetapAppId = ezetapAppId;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getDownloadSeverity() {
		return downloadSeverity;
	}

	public void setDownloadSeverity(int downloadSeverity) {
		this.downloadSeverity = downloadSeverity;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdateNotes() {
		return updateNotes;
	}

	public void setUpdateNotes(String updateNotes) {
		this.updateNotes = updateNotes;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getJsonRepresentation() {
		return jsonRepresentation;
	}

	public void setJsonRepresentation(String jsonRepresentation) {
		this.jsonRepresentation = jsonRepresentation;
	}

}
