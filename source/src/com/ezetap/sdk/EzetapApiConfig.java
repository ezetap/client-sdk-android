package com.ezetap.sdk;

import com.ezetap.sdk.EzeConstants.AppMode;
import com.ezetap.sdk.EzeConstants.CommunicationChannel;
import com.ezetap.sdk.EzeConstants.LoginAuthMode;

/**
 * Ezetap API Config object. Use a populated instance of this class and call
 * EzetapPayApis.create(EzetapApiConfig config) to initialize/override default
 * parameters (set in com.ezetap.sdk.AppConstants)
 * 
 * @author vivek
 * 
 */
public class EzetapApiConfig {

	private LoginAuthMode authMode;
	private String appKey;
	private String merchantName;
	private String currencyCode;
	private AppMode mode;
	private boolean captureSignature = false;
	private CommunicationChannel preferredChannel =  CommunicationChannel.NONE;

	/**
	 * Creates an instance of EzetapApiConfig with desired mode. Default/null
	 * will set mode to DEMO. Use this constructor to create an instance of
	 * <code>EzetapApiConfig</code> in case you want to override the default
	 * setting of <code>captureSignature = false</code> and set it to
	 * <code>true</code>. Setting it to <code>true</code> will enable the Ezetap
	 * signature capture UI to be shown during a payment transaction.
	 * 
	 * @param authMode
	 *            - Mode of Authentication as defined in enum LoginAuthMode
	 *            </br> EZETAP_LOGIN_PROMPT - User Authentication credentials
	 *            input screen and validation handled by Ezetap</br>
	 *            EZETAP_LOGIN_CUSTOM - User Authentication credentials input
	 *            screen handled by App and validation by Ezetap</br>
	 *            EZETAP_LOGIN_BYPASS - User Authentication is completely done
	 *            by the App and notified to Ezetap (needs AppKey)</br> By
	 *            default, signature capture is disabled. To enable, call
	 *            <code>setCaptureSignature(true)</code>
	 * @param appKey
	 *            - App Key (if applicable) provided by Ezetap for the merchant
	 * @param merchantName
	 *            - Name of the merchant
	 * @param currencyCode
	 *            - Currency code for the merchant (Defaulted to INR if null is
	 *            passed)
	 * @param captureSignature
	 *            - flag defaulted to <code>false</code>. Set this to true if
	 *            you require Ezetap to display the signature UI and capture
	 *            signature as a <code>Bitmap</code>
	 * @param mode
	 *            - App mode as defined in enum AppMode </br> EZETAP_DEMO -
	 *            Application is in DEMO mode connecting to Ezetap's DEMO
	 *            instance </br> EZETAP_PROD - Application is in PROD mode
	 *            connecting to Ezetap's PROD instance. </br> <b>Contact Ezetap
	 *            prior to connecting to the PROD instance</b>
	 * @param preferredChannel
	 * 			  - Specify which CommunicationChannel you wish set as default
	 */

	public EzetapApiConfig(LoginAuthMode authMode, String appKey, String merchantName, String currencyCode, AppMode mode, boolean captureSignature, CommunicationChannel preferredChannel) {

		this(authMode, appKey, merchantName, currencyCode, mode);
		this.captureSignature = captureSignature;
		this.preferredChannel = preferredChannel;
	}
	
	/**
	 * Creates an instance of EzetapApiConfig with desired mode. Default/null
	 * will set mode to DEMO. Use this constructor to create an instance of
	 * <code>EzetapApiConfig</code> in case you want to override the default
	 * setting of <code>captureSignature = false</code> and set it to
	 * <code>true</code>. Setting it to <code>true</code> will enable the Ezetap
	 * signature capture UI to be shown during a payment transaction.
	 * 
	 * @param authMode
	 *            - Mode of Authentication as defined in enum LoginAuthMode
	 *            </br> EZETAP_LOGIN_PROMPT - User Authentication credentials
	 *            input screen and validation handled by Ezetap</br>
	 *            EZETAP_LOGIN_CUSTOM - User Authentication credentials input
	 *            screen handled by App and validation by Ezetap</br>
	 *            EZETAP_LOGIN_BYPASS - User Authentication is completely done
	 *            by the App and notified to Ezetap (needs AppKey)</br> By
	 *            default, signature capture is disabled. To enable, call
	 *            <code>setCaptureSignature(true)</code>
	 * @param appKey
	 *            - App Key (if applicable) provided by Ezetap for the merchant
	 * @param merchantName
	 *            - Name of the merchant
	 * @param currencyCode
	 *            - Currency code for the merchant (Defaulted to INR if null is
	 *            passed)
	 * @param captureSignature
	 *            - flag defaulted to <code>false</code>. Set this to true if
	 *            you require Ezetap to display the signature UI and capture
	 *            signature as a <code>Bitmap</code>
	 * @param mode
	 *            - App mode as defined in enum AppMode </br> EZETAP_DEMO -
	 *            Application is in DEMO mode connecting to Ezetap's DEMO
	 *            instance </br> EZETAP_PROD - Application is in PROD mode
	 *            connecting to Ezetap's PROD instance. </br> <b>Contact Ezetap
	 *            prior to connecting to the PROD instance</b>
	 */

	public EzetapApiConfig(LoginAuthMode authMode, String appKey, String merchantName, String currencyCode, AppMode mode, boolean captureSignature) {

		this(authMode, appKey, merchantName, currencyCode, mode);
		this.captureSignature = captureSignature;
	}

	public CommunicationChannel getPreferredChannel() {
		return preferredChannel;
	}

	public void setPreferredChannel(CommunicationChannel preferredChannel) {
		this.preferredChannel = preferredChannel;
	}

	/**
	 * Creates an instance of EzetapApiConfig with desired mode. Default/null
	 * will set mode to DEMO
	 * 
	 * @param authMode
	 *            - Mode of Authentication as defined in enum LoginAuthMode
	 *            </br> EZETAP_LOGIN_PROMPT - User Authentication credentials
	 *            input screen and validation handled by Ezetap</br>
	 *            EZETAP_LOGIN_CUSTOM - User Authentication credentials input
	 *            screen handled by App and validation by Ezetap</br>
	 *            EZETAP_LOGIN_BYPASS - User Authentication is completely done
	 *            by the App and notified to Ezetap (needs AppKey)</br>
	 * 
	 *            By default, signature capture is disabled. To enable, call
	 *            <code>setCaptureSignature(true)</code> or use constructor
	 *            <code>public EzetapApiConfig(LoginAuthMode authMode, String appKey,String merchantName, String currencyCode, AppMode mode, boolean captureSignature)</code>
	 *            with the argument <code>captureSignature</code> set to true
	 *            when you create the <code>EzetapApiConfig</code> instance
	 * 
	 * @param appKey
	 *            - App Key (if applicable) provided by Ezetap for the merchant
	 * @param merchantName
	 *            - Name of the merchant
	 * @param currencyCode
	 *            - Currency code for the merchant (Defaulted to INR if null is
	 *            passed)
	 * @param mode
	 *            - App mode as defined in enum AppMode </br> EZETAP_DEMO -
	 *            Application is in DEMO mode connecting to Ezetap's DEMO
	 *            instance </br> EZETAP_PROD - Application is in PROD mode
	 *            connecting to Ezetap's PROD instance. </br> <b>Contact Ezetap
	 *            prior to connecting to the PROD instance</b>
	 */
	public EzetapApiConfig(LoginAuthMode authMode, String appKey, String merchantName, String currencyCode, AppMode mode) {
		super();
		this.authMode = authMode;
		this.appKey = appKey;
		this.merchantName = merchantName;
		this.currencyCode = currencyCode;
		this.setMode(mode);
	}

	/**
	 * Creates an instance of EzetapApiConfig with mode being set to default -
	 * DEMO
	 * 
	 * @param authMode
	 *            - Mode of Authentication as defined in enum LoginAuthMode
	 *            </br> EZETAP_LOGIN_PROMPT - User Authentication credentials
	 *            input screen and validation handled by Ezetap</br>
	 *            EZETAP_LOGIN_CUSTOM - User Authentication credentials input
	 *            screen handled by App and validation by Ezetap</br>
	 *            EZETAP_LOGIN_BYPASS - User Authentication is completely done
	 *            by the App and notified to Ezetap (needs AppKey)</br>
	 * 
	 * @param appKey
	 *            - App Key (if applicable) provided by Ezetap for the merchant
	 * @param merchantName
	 *            - Name of the merchant
	 * @param currencyCode
	 *            - Currency code for the merchant (Defaulted to INR if null is
	 *            passed)
	 */

	public EzetapApiConfig(LoginAuthMode authMode, String appKey, String merchantName, String currencyCode) {
		super();
		this.authMode = authMode;
		this.appKey = appKey;
		this.merchantName = merchantName;
		this.currencyCode = currencyCode;
	}

	public LoginAuthMode getAuthMode() {
		return authMode;
	}

	public void setAuthMode(LoginAuthMode authMode) {
		this.authMode = authMode;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public AppMode getMode() {
		return mode;
	}

	public void setMode(AppMode mode) {
		this.mode = mode;
	}

	public boolean isCaptureSignature() {
		return captureSignature;
	}

	public void setCaptureSignature(boolean captureSignature) {
		this.captureSignature = captureSignature;
	}

}
