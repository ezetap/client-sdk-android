/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/
package com.eze.api;

public class EzeAPIConstants {
	public enum EzetapErrors {		
		ERROR_INIT_REQUIRED("EZECLI_323020","Device not initialized, call INITIALIZE API."),
		ERROR_API_CHEQUE("EZECLI_323021","Cheque payment API is not supported in this version of SDK."),
		ERROR_API_SENDRECEIPT("EZECLI_323022","Send Receipt is not supported in this version of SDK."),
		ERROR_MISSING_MANDATORYPARAMS("EZECLI_323023","Mandatory parameters are missing."),
		ERROR_DEFAULT_INIT("EZECLI_323024","Error occured during Initialize device."),
		ERROR_DEFAULT_SENDRECEIPT("EZECLI_323025","Error occurred during SEND RECEIPT."),
		ERROR_DEFAULT_TAKEPAYMENT("EZECLI_323026","Error occurred during TAKE PAYMENT."),
		ERROR_DEFAULT_TRANSHISTORY("EZECLI_323027","Error occurred in TRANSACTION HISTORY."),
		ERROR_DEFAULT_TRANSACTIONDETAIL("EZECLI_323028","Error occurred in TRANACTION DETAIL."),
		ERROR_DEFAULT_ATTACHSIGNATURE("EZECLI_323029","Error occurred in ATTACH SIGNATURE."),
		ERROR_DEFAULT_VOIDTRANSACTION("EZECLI_323030","Error occurred in VOID TRANSACTION."),
		ERROR_DEFAULT_LOGOUT("EZECLI_323031","Error occurred in LOGOUT."),
		ERROR_API_GETTRANSACTIONDETAIL("EZECLI_323032","GetTransactionDetail API is not supported in this version of SDK."),
		ERROR_DEFAULT_GETTRANSACTIONDETAIL("EZECLI_323033","Error occurred in GET TRANSACTIONDETAIL."),
		ERROR_API_WALLETPAYMENT("EZECLI_323034","Wallet payment is not supported in this version of SDK."),
		ERROR_DEFAULT_WALLETPAYMENT("EZECLI_323035","Error occurred in WALLET PAYMENT API."),
		ERROR_CONNECTION_FAILED("EZECLI_323036","Error occured while connecting to Ezetap Server. Please try again after sometime."),
		ERROR_CANCEL_TAKEPAYMENT("EZECLI_323037","Transaction cancelled.");
		
		private final String ERRORCODE,ERRORMESSAGE;
		EzetapErrors(String mERRORCODE,String mERRORMESSAGE) { 
			this.ERRORCODE = mERRORCODE;
			this.ERRORMESSAGE = mERRORMESSAGE;
		}
		public String getErrorCode(){return ERRORCODE;}
		public String getErrorMessage(){return ERRORMESSAGE;}		
	};
	//Payment object key names
	public static final String KEY_TRANSACTION_ID = "txnId";
	public static final String KEY_TOTAL_AMOUNT = "amount";
	public static final String KEY_AMOUNT_CASH_BACK = "amountCashBack";
	public static final String KEY_CURRENCY_CODE = "currencyCode";
	public static final String KEY_CUSTOMER_EMAIL = "customerEmail";
	public static final String KEY_CUSTOMER_NAME = "customerName";
	public static final String KEY_CUSTOMER_MOBILE = "customerMobile";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_NAME = "name";
	public static final String KEY_MOBILENO = "mobileNo";
	public static final String KEY_CUST_RCPT_URL = "customerReceiptUrl";
	public static final String KEY_RCPT_URL ="receiptUrl";
	public static final String KEY_MERCHANT_NAME = "merchantName";
	public static final String KEY_MERCHANT_CODE = "merchantCode";
	public static final String KEY_PAYMENT_MODE = "paymentMode";
	public static final String KEY_CHARGE_SLIP_DATE = "chargeSlipDate";
	public static final String KEY_RECEIPT_DATE = "receiptDate";
	public static final String KEY_TXN_DATE = "txnDate";
	public static final String KEY_POSTING_DATE = "postingDate";
	public static final String KEY_SIGNATURE_ID = "signatureId";


	//Card object key names
	public static final String KEY_AUTH_CODE = "authCode";
	public static final String KEY_FORMATTED_PAN = "formattedPan";
	public static final String KEY_DEVICE_SERIAL = "deviceSerial";
	public static final String KEY_MASKED_CARD_NO = "maskedCardNo";
	public static final String KEY_MASKED_CARD_NUMBER = "maskedCardNumber";
	public static final String KEY_MID = "mid";
	public static final String KEY_TID = "tid";
	public static final String KEY_TXN_CARD_TYPE = "paymentCardBrand";
	public static final String KEY_TXN_CARD_BRAND = "paymentCardBrand";
	public static final String KEY_CARD_BRAND = "cardBrand";

	//Check object key names
	public static final String KEY_CHEQUE_TYPE = "chequeType";
	public static final String KEY_CHEQUE_NUMBER = "chequeNumber";
	public static final String KEY_CHEQUE_DATE = "chequeDate";
	public static final String KEY_CHEQUE_BANK = "chequeBank";
	
}
