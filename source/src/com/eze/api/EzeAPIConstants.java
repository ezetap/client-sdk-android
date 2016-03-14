/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * <p/>
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *******************************************************/
package com.eze.api;

public class EzeAPIConstants {

    public static final String PREPARE_DEVICE = "prepareDevice";

    //Payment object key names
    public static final String KEY_TRANSACTION_ID = "txnId";
    public static final String KEY_TRANSACTION = "txn";
    public static final String KEY_TRANSACTIONS = "txns";
    public static final String KEY_TRANSACTION_RECEIPT = "receipt";
    public static final String KEY_MERCHANT = "merchant";
    public static final String KEY_CARD_DETAILS = "cardDetails";

    public static final String KEY_TOTAL_AMOUNT = "amount";
    public static final String KEY_AMOUNT_CASH_BACK = "amountCashBack";
    public static final String KEY_AMOUNT_TIP = "amountTip";
    public static final String KEY_CURRENCY_CODE = "currencyCode";
    public static final String KEY_CUSTOMER_EMAIL = "customerEmail";
    public static final String KEY_CUSTOMER_NAME = "customerName";
    public static final String KEY_CUSTOMER_MOBILE = "customerMobile";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_MOBILENO = "mobileNo";
    public static final String KEY_CUST_RCPT_URL = "customerReceiptUrl";
    public static final String KEY_RCPT_URL = "receiptUrl";
    public static final String KEY_MERCHANT_NAME = "merchantName";
    public static final String KEY_MERCHANT_CODE = "merchantCode";
    public static final String KEY_PAYMENT_MODE = "paymentMode";
    public static final String KEY_CHARGE_SLIP_DATE = "chargeSlipDate";
    public static final String KEY_RECEIPT_DATE = "receiptDate";
    public static final String KEY_TXN_DATE = "txnDate";
    public static final String KEY_POSTING_DATE = "postingDate";
    public static final String KEY_SIGNATURE_ID = "signatureId";
    public static final String DEMO_APP_KEY = "demoAppKey";
    public static final String PROD_APP_KEY = "prodAppKey";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_APP_MODE = "appMode";
    public static final String KEY_CAPTURE_SIGNATURE = "captureSignature";
    public static final String KEY_COMMUNICATION_CHANNEL = "communicationChannel";
    public static final String KEY_APP_MODE_DEMO = "DEMO";
    public static final String KEY_APP_MODE_PREPROD = "PREPROD";
    public static final String KEY_APP_MODE_PROD = "PROD";
    public static final String KEY_PAYMENT_MODE_CASHBACK = "CASHBACK";
    public static final String KEY_PAYMENT_MODE_SALE = "SALE";
    public static final String KEY_PAYMENT_MODE_CASH_AT_POS = "CASH@POS";
    public static final String KEY_PRE_SELECTED_TERMINAL_LABEL = "terminalLabel";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_REFERENCES = "references";
    public static final String KEY_REFERENCES1 = "reference1";
    public static final String KEY_REFERENCES2 = "reference2";
    public static final String KEY_REFERENCES3 = "reference3";

    //Signature Image Key Names
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IMAGE_DATA = "imageData";
    public static final String KEY_EMI_ID = "emiID";
    public static final String KEY_IMAGE_TYPE = "imageType";


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


    /**
     * Variable used when user selects LOGIN API.
     */
    public static final int WORKFLOW_DO_LOGIN = 2;
    /**
     * Variable used when user selects ATTACH SIGNATURE API.
     */
    public static final int WORKFLOW_ATTACH_SIGNATURE = 3;
    /**
     * Variable used when user selects GET TRANSACTION HISTORY API.
     */
    public static final int WORKFLOW_GET_TRANSACTIONHISTORY = 4;
    /**
     * Variable used when user selects VOID TRANSACTION API.
     */
    public static final int WORKFLOW_VOID_TRANSACTION = 5;
    /**
     * Variable used when user selects LOGOUT API.
     */
    public static final int WORKFLOW_DO_LOGOUT = 6;
    /**
     * Variable used when user selects INITIALIZE API.
     */
    public static final int WORKFLOW_INIT = 8;
    /**
     * Variable used when user wants to capture the signature.
     */
    public static final int WORKFLOW_ATTSIGN_WITHOUTIMG = 9;
    /**
     * Variable used when user has captured the signature & invoke ATTACH SIGNATURE API.
     */
    public static final int WORKFLOW_ATTSIGN_WITHIMG = 10;
    /**
     * Variable used when user invoke ATTACH SIGNATURE API which has an EMI ID.
     */
    public static final int WORKFLOW_ATTSIGN_WITHEMI = 11;
    /**
     * Variable used when error occurred in processing image in ATTACH SIGNATURE API.
     */
    public static final int WORKFLOW_ATTSIGN_ERRIMG = 12;
    /**
     * Variable used when user selects SEND RECEIPT API.
     */
    public static final int WORKFLOW_SEND_RECEIPT = 13;
    /**
     * Variable used when user selects GET TRANSACTION DETAILS API.
     */
    public static final int WORKFLOW_GET_TRANSACTIONDETAILS = 14;
    /**
     * Variable used when user selects CARD PAYMENT API.
     */
    public static final int WORKFLOW_DO_CARDTRANSACTION = 15;
    /**
     * Variable used when user selects CASH PAYMENT API.
     */
    public static final int WORKFLOW_DO_CASHTRANSACTION = 16;
    /**
     * Variable used when user selects CHEQUE PAYMENT API.
     */
    public static final int WORKFLOW_DO_CHEQUETRANSACTION = 17;
    /**
     * Variable used when user selects CHEQUE PAYMENT API.
     */
    public static final int WORKFLOW_DO_WALLETTRANSACTION = 18;
    /**
     * Variable used when user selects CHEQUE PAYMENT API.
     */
    public static final int WORKFLOW_PREPARE_DEVICE = 19;

    /**
     * Variable used when user selects Card not present PAYMENT API.
     */
    public static final int WORKFLOW_DO_CNPTRANSACTION= 20;

    /**
     * Variable used when user selects Update API.
     */
    public static final int WORKFLOW_DO_UPDATE = 22;


    public enum EzetapErrors {
        ERROR_MANIFEST_ACTIVITY("EZECLI_323019", "Looks like you have not defined EzeAPIActivity in your Manifest file."),
        ERROR_INIT_REQUIRED("EZECLI_323020", "Device not initialized, call INITIALIZE API."),
        ERROR_API_CHEQUE("EZECLI_323021", "Cheque payment API is not supported in this version of SDK."),
        ERROR_API_SENDRECEIPT("EZECLI_323022", "Send Receipt is not supported in this version of SDK."),
        ERROR_MISSING_MANDATORYPARAMS("EZECLI_323023", "Mandatory parameters are missing."),
        ERROR_DEFAULT_INIT("EZECLI_323024", "Error occured during Initialize device."),
        ERROR_DEFAULT_SENDRECEIPT("EZECLI_323025", "Error occurred during SEND RECEIPT."),
        ERROR_DEFAULT_TAKEPAYMENT("EZECLI_323026", "Error occurred during TAKE PAYMENT."),
        ERROR_DEFAULT_TRANSHISTORY("EZECLI_323027", "Error occurred in TRANSACTION HISTORY."),
        ERROR_DEFAULT_TRANSACTIONDETAIL("EZECLI_323028", "Error occurred in TRANACTION DETAIL."),
        ERROR_DEFAULT_ATTACHSIGNATURE("EZECLI_323029", "Error occurred in ATTACH SIGNATURE."),
        ERROR_DEFAULT_VOIDTRANSACTION("EZECLI_323030", "Error occurred in VOID TRANSACTION."),
        ERROR_DEFAULT_LOGOUT("EZECLI_323031", "Error occurred in LOGOUT."),
        ERROR_API_GETTRANSACTIONDETAIL("EZECLI_323032", "GetTransactionDetail API is not supported in this version of SDK."),
        ERROR_DEFAULT_GETTRANSACTIONDETAIL("EZECLI_323033", "Error occurred in GET TRANSACTIONDETAIL."),
        ERROR_API_WALLETPAYMENT("EZECLI_323034", "Wallet payment is not supported in this version of SDK."),
        ERROR_DEFAULT_WALLETPAYMENT("EZECLI_323035", "Error occurred in WALLET PAYMENT API."),
        ERROR_CONNECTION_FAILED("EZECLI_323036", "Error occured while connecting to Ezetap Server. Please try again after sometime."),
        ERROR_CANCEL_TAKEPAYMENT("EZECLI_323037", "Transaction cancelled."),
        ERROR_MISSING_AMOUNT("EZECLI_323028", "please fill the amount"),
        ERROR_API_CNPPAYMENT("EZECLI_323038", "CNP payment is not supported in this version of SDK."),
        ERROR_DEFAULT_CNPPAYMENT("EZECLI_323039", "Error occurred in CNP PAYMENT API.");
        private final String ERRORCODE, ERRORMESSAGE;


        EzetapErrors(String mERRORCODE, String mERRORMESSAGE) {
            this.ERRORCODE = mERRORCODE;
            this.ERRORMESSAGE = mERRORMESSAGE;
        }

        public String getErrorCode() {
            return ERRORCODE;
        }

        public String getErrorMessage() {
            return ERRORMESSAGE;
        }
    }

}
