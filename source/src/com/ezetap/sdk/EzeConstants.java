/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/

package com.ezetap.sdk;

/**
 * Constants used by the SDK and the Application
 * 
 */

public class EzeConstants {

	/**
	 * Keys for application request.
	 */
	public static final String KEY_ACTION = "action";
	public static final String KEY_JSON_REQ_DATA = "jsonReqData";
	public static final String KEY_ADDITIONAL_DATA = "additionalData";
	public static final String KEY_CNPURL = "cnpurl";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_TIP_ENABLED = "tipEnabled";
	public static final String KEY_EMI_NOTELIGIBLE = "markEMINotEligible";

	public static final String KEY_PASSWORD = "password";
	public static final String KEY_NEW_PASSWORD = "newPassword";

	public static final String KEY_AUTH_TOKEN = "authToken";
	public static final String KEY_AUTH_CODE = "authCode";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_TIP_AMOUNT = "amountAdditional";
	public static final String KEY_AMOUNT_CASH_BACK = "amountCashBack";
	public static final String KEY_TOTAL_AMOUNT = "amount";
	public static final String KEY_TRANSACTION_LIST = "txns";
	public static final String KEY_CARD_LAST_4_DIGITS = "cardLastFourDigit";
	
	public static final String ALLOW_SDK_DEBUGGING = "allowSDKDebugging";

	public static final String KEY_ORDERID = "externalRefNumber";
	public static final String KEY_CUSTOMER_MOBILE = "customerMobileNumber";
	public static final String KEY_CUSTOMER_EMAIL = "customerEmail";
	public static final String KEY_CUSTOMER_MOBILE_RESP = "customerMobile";
	public static final String KEY_CUSTOMER_NAME = "customerName";
	public static final String KEY_CAPTURE_SIGNATURE = "captureSignature";
	public static final String KEY_TRANSACTION_ID = "txnId";
	public static final String KEY_ERROR_CODE = "errorCode";
	public static final String KEY_ERROR_MESSAGE = "errorMessage";
	public static final String KEY_ENABLE_CUSTOM_LOGIN = "enableCustomELogin";
	public static final String KEY_PAYMENT_MODE = "paymentMode";
	public static final String KEY_CHARGE_SLIP_DATE = "chargeSlipDate";
	public static final String KEY_TXN_EXT_REF_NUM = "externalRefNumber";
	public static final String KEY_CUST_RCPT_URL = "customerReceiptUrl";
	public static final String KEY_TXN_RRN = "rrNumber";
	public static final String KEY_TXN_BATCH = "batchNumber";
	public static final String KEY_TXN_INVOICE = "invoiceNumber";
	public static final String KEY_TXN_STATUS = "status";
	public static final String KEY_TXN_CARD_TYPE = "cardType";
	public static final String KEY_MERCHANT_NAME = "merchantName";
	public static final String KEY_CURRENCY_CODE = "currencyCode";
	public static final String KEY_APPS = "apps";
	public static final String KEY_APPLICATION_ID = "applicationId";
	public static final String KEY_VERSION_CODE = "versionCode";
	
	public static final String KEY_BANK_NAME = "bankName";
	public static final String KEY_BANK_CODE = "bankCode";
	public static final String KEY_BANK_ACCOUNT = "bankAccountNo";
	public static final String KEY_CHEQUE_DATE = "chequeDate";
	public static final String KEY_CHEQUE_NUMBER = "chequeNumber";
	
	public static final String KEY_COMM_DEVICE_ID = "communicationDeviceId";
	public static final String KEY_PREFERRED_COMM = "preferredCommunication";
	
	public static final String KEY_PRE_SELECTED_TERMINAL_LABEL = "preSelectedTerminalLabel";
	public static final String KEY_LABLES = "labels";
	
	public static final String KEY_PROCESS_DATA_MAP			= "processDataMap";
	public static final String ACTION_SAVE_DATA				= "saveData";
	public static final String ACTION_AUTHORISE_CARD		= "authorisecard";
	public static final String ACTION_RELEASE_PRE_AUTH		= "releasePreAuth";
	public static final String ACTION_CONFIRM_PRE_AUTH		= "confirmPreAuth";

	public static final String KEY_EXTERNAL_REF_2 = "externalRefNumber2";
	public static final String KEY_EXTERNAL_REF_3 = "externalRefNumber3";
	public static final String KEY_EXTERNAL_REF_4 = "externalRefNumber4";
	public static final String KEY_EXTERNAL_REF_5 = "externalRefNumber5";

	public static final String KEY_SIGNATURE_WIDTH = "signatureWidth";
	public static final String KEY_SIGNATURE_HEIGHT = "signatureHeight";
	public static final String KEY_SIGNATURE_FORMAT = "signatureFormat";
	public static final String KEY_SIGNATURE_DATA = "signatureData";

	public static final String KEY_APP_ID = "appId";
	public static final String KEY_APP_NAME = "appName";
	public static final String KEY_APP_VERSION = "appVersion";
	public static final String KEY_DISPLAY_VERSION = "displayVersion";
	public static final String KEY_HAS_VERSION_INFO = "hasVersionInfo";

	public static final String KEY_ON_SUCCESS_ACTION			= "onSuccessAction";
	public static final String KEY_ON_SUCCESS_ACTION_JSON_DATA	= "onSuccessActionJsonData";
	
	public static final String KEY_SHARED_ID = "ezCollectedForMerchant";
	/**
	 * Keys for values to be set in case AppKey takes precedence.
	 */
	// Value ofr key "precedeAppKey" is boolean and should be set to true if
	// appkey precedence is required
	// appkey precedence also makes username mandatory. Please see EzetapUtils
	// for sample
	public static final String KEY_IS_USER_VALIDATED_BY_MERCHANT = "isUserValidatedByMerchant";
	// your organization's app keyas generated on ezetap's portal
	public static final String KEY_APPKEY = "appKey";
	/**
	 * Keys through which application will receive response from Ezetap
	 * Application.
	 */

	/**
	 * JSON formated transaction response for Ezetap application.
	 */
	public static final String KEY_RESPONSE_DATA = "responseData";

	/**
	 * Key contains transaction status.
	 */
	public static final String KEY_IS_SUCCESS = "isSuccess";

	// public static final String KEY_NAME_ON_CARD = "nameOnCard";
	public static final String KEY_NAME_ON_CARD = "payerName";

	/**
	 * Key contains signature status.
	 */
	public static final String KEY_IS_SIGNATURE_ATTACHED = "isSignatueAttached";

	/**
	 * Action code to logout the session from Ezetap Application.
	 */
	public static final String ACTION_LOGOUT = "logout";

	/**
	 * Action code to start a card transaction.
	 */
	public static final String ACTION_PAYCARD = "paycard";

	/**
	 * Action code to start a CNP transaction.
	 */
	public static final String ACTION_PAYCNP = "payCNP";
	
	public static final String ACTION_PAY_CASH = "paycash";
	
	public static final String ACTION_PAY_WALLET = "paywallet";
	
	public static final String ACTION_PAY_CHEQUE = "paycheque";
	

	/**
	 * Action code to register Ezetap Device.
	 */
	public static final String ACTION_REGISTER_DONGLE = "registerDongle";

	/**
	 * Action code to initiate a void transaction.
	 */
	public static final String ACTION_VOID = "void";

	/**
	 * Action code to request for login.
	 */
	public static final String ACTION_LOGIN = "login";

	/**
	 * Action code to change user password.
	 */
	public static final String ACTION_CHANGE_PWD = "change-password";

	/**
	 * Action code to attach the signature with an existing transaction.
	 */
	public static final String ACTION_ATTACH_SIGNATURE = "attachSignature";

	/**
	 * Action code to get the transaction history.
	 */
	public static final String ACTION_TXN_HISTORY = "txnhistory";

	/**
	 * Action code to get the card info.
	 */
	public static final String ACTION_GET_CARD_INFO = "getCardInfo";

	/**
	 * Action code to attach a collected signature to an existing transaction
	 */
	public static final String ACTION_ATTACH_SIGNATURE_EX = "attachSignatureEx";

	/**
	 * Action code to check for app updates (applicable only to service app for
	 * SDK users)
	 */
	public static final String ACTION_CHECK_UPDATES = "checkUpdates";

	/**
	 * Action code to check for incomplete transaction
	 */
	public static final String ACTION_NONCE_CHECK = "nonceCheck";

	/**
	 * Action code to initialize device
	 */
	public static final String ACTION_INIT_DEVICE_SESSION = "initDeviceSession";

	// result from Ezetap service application
	public static final int RESULT_SUCCESS 	= 2001;
	public static final int RESULT_FAILED 	= 3001;
	
	public static final int RESULT_DOWNLOAD_FAILURE = 4001;
	public static final int RESULT_INSTALL_CANCELLED = 4002;
	public static final int RESULT_DOWNLOAD_SUCCESS = 4003;
	public static final int RESULT_DOWNLOAD_ABORTED = 4004;
	
	/**
	 * Ezetap result codes
	 */
	// public static final String ERROR_CODE_SESSION_TIMEDOUT =
	// "SESSION_TIMEDOUT";

	public static final String ERROR_CODE_SESSION_TIMED_OUT = "SESSION_TIMED_OUT";
	public static final String ERROR_CODE_ACCESS_DENIED = "ACCESS_DENIED";

	public static final String KEY_SIGNATURE_ATTACHED = "signatureAttached";

	public static final String KEY_IS_VOIDABLE = "voidable";
	
	public static final String KEY_EMI_ID = "emiId";

	public static final String KEY_SIGNATURE_REQD = "signReqd";
	/**
	 * Login Authentication Modes
	 */

	public enum LoginAuthMode {
		/**
		 * User Authentication credentials input screen and validation handled
		 * by Ezetap
		 */
		EZETAP_LOGIN_PROMPT,
		/**
		 * User Authentication credentials input screen handled by App and
		 * validation by Ezetap
		 */
		EZETAP_LOGIN_CUSTOM,
		/**
		 * User Authentication is completely done by the App and notified to
		 * Ezetap (needs AppKey)
		 */
		EZETAP_LOGIN_BYPASS
	}

	public enum AppMode {
		EZETAP_DEMO, EZETAP_PROD, EZETAP_PREPROD
	}

	public enum CommunicationChannel {
		BT, USBA, MOCK, NONE
	}
	
	public interface CLIENT_ERROR_CODES {
		public static final String TXN_STATUS_CHECK_SKIPPED = "TXN_STATUS_CHECK_SKIPPED"; // Txn (nonce) Status check is skipped
		public static final String BT_CONN_ERROR_01 = "DEVICE_CONN_ERROR_11"; // Could not connect to specified bluetooth enabled V2 device id
		public static final String BT_CONN_ERROR_02 = "DEVICE_CONN_ERROR_12"; // Connection between bluetooth enabled v2 device and application is broken
	}
}
