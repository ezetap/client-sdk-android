/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/

package com.ezetap.sdk;

import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

/**
 * Ezetap Pay API interface
 * 
 */

public interface EzetapPayApi {

	/**
	 * To check if a previous card transaction is pending. A card transaction
	 * could be pending if no response was received after the transaction was
	 * initiated or if there was an app crash after the transaction was
	 * initiated. The Ezetap SDK checks for pending transactions at the very
	 * next Ezetap API call, it is recommeded that calling applications call
	 * this API at the appropriate time for eg. - upon login or upon calling pay
	 * cash where the handling is not done by the Ezetap system.
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling the response
	 * @param username
	 *            - Ezetap username
	 */
	
	void checkForIncompleteTransaction(Activity context, int reqCode, String username);

	/**
	 * To check if there is an Ezetap Service app update available call this
	 * method. Upon receiving a response, use
	 * <code>EzetapAppUpdateResponse</code> to check if there are updates, if so
	 * what the severity is and the download URL.
	 * <code>EzetapAppUpdateResponse</code> contains the following info : </br>
	 * </br> <code>updateAvailable</code> - <code>true</code> indicates there is
	 * an update available </br> <code>filesize</code> - size of the update in
	 * bytes </br> <code>ezetapAppId</code> -
	 * <code>ezetap_android_service</code>, the app id of the Ezetap Service app
	 * </br> <code>downloadUrl</code> - the update download URL. You may use
	 * <code>EzetapDownloadUtils</code> to download and install the update.
	 * </br> <code>downloadSeverity</code> - an integer describing the severity
	 * of the available update </br>
	 * <code>EzetapAppUpdateResponse.MANDATORY_UPDATE</code> = 0 </br>
	 * <code>EzetapAppUpdateResponse.OPTIONAL_UPDATE</code> = 1 </br>
	 * <code>versionName</code> - the version name of the update (eg. 2.2.23
	 * etc) </br> <code>updateNotes</code> - a cumulative list of all available
	 * updates between the current version and the available version </br> </br>
	 * All updates are cumulative in nature - for eg. if you are on version
	 * 2.2.23 and the available is version 2.2.26, it will contain all updates
	 * in 2.2.24, 2,2,25 and 2.2.26 </br>
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling the response
	 * @param username
	 *            - Ezetap username
	 */

	void checkForUpdate(Activity context, int reqCode, String username);

	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of customer (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */
	void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, Hashtable<String, Object> appData);
	
	
	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param amountCashback
	 *            - Transaction Cashback Amount	 
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of customer (optional)
	 * @param emailID
	 *            - Email Address of customer (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 * @param addData
	 *            - Additional App specific data (optional)
	 *            
	 */
	void startCardPayment(Activity context, int reqCode, String username, double amount, double amountCashback, String orderNumber, double tip, String mobile, String emailID, Hashtable<String, Object> appData, Hashtable<String, Object> addData);

	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip (if applicable) for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of the customer (optional)
	 * @param externalReference1
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param externalReference2
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */
	void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, String externalReference2, String externalReference3, Hashtable<String, Object> appData);

	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip (if applicable) for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of the customer (optional)
	 * @param emailId
	 *            - Email Address of the customer (optional)
	 * @param externalReference1
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param externalReference2
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */

	void startCardPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData);
	
	/**
	 * To initiate a card payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param amountCashBack
	 *            - CashBack amount/Cash withdrawal amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip (if applicable) for this transaction (can be zero)
	 * @param mobile
	 *            - Mobile number of the customer (optional)
	 * @param emailId
	 *            - Email Address of the customer (optional)
	 * @param externalReference1
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param externalReference2
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */

	void startCardPayment(Activity context, int reqCode, String username, double amount, double amountCashBack, String orderNumber, double tip, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData);

	/**
	 * To initiate a cash payment transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param tip
	 *            - Tip for this transaction (can be zero)
	 * @param customerMobile
	 *            - Mobile number of customer (optional)
	 * @param customerName
	 *            - Name of customer (optional)
	 */
	void startCashPayment(Activity context, int reqCode, String username, double amount, String orderNumber, double tip, String customerMobile, String customerName);

	/**
	 * To initiate a void transaction request, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param txnID
	 *            - Transaction ID to void
	 */
	void startVoidTransaction(Activity context, int reqCode, String username, String txnId);

	/**
	 * To logout current sesssion associated with AppKey , application should
	 * call this method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 */
	void logout(Activity context, int reqCode, String username);

	/**
	 * Attach signature to existing transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param txnID
	 *            - Transaction ID to attach signature
	 */
	void attachSignature(Activity context, int reqCode, String username, String txnId);

	/**
	 * Attach signature to an existing transaction. This method is to be called
	 * when signature is captured by the calling application, with the following
	 * parameters :
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param txnId
	 *            - Transaction ID to attach signature
	 * @param bitmap
	 *            - Bitmap containing the signature
	 * @param format
	 *            - Compression format (supported format - CompressFormat.PNG)
	 */
	void attachSignature(Activity context, int reqCode, String username, String txnId, Bitmap bitmap, CompressFormat format);

	
	/**
	 * Attach signature to an existing transaction. This method is to be called
	 * to convert a transaction to EMI when signature is captured by the calling application, 
	 * with the following parameters :
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param txnId
	 *            - Transaction ID to attach signature
	 * @param emiId
	 *            - EMI ID to convert this txn to EMI. Pass null if you don't want to convert this transaction to EMI.
	 *            - EMI ID can be retrieved from the Transaction Details of startCardPayment response.            
	 * @param bitmap
	 *            - Bitmap containing the signature. Mandatory for EMI Transactions
	 * @param format
	 *            - Compression format (supported format - CompressFormat.PNG)
	 */
	void attachSignature(Activity context, int reqCode, String username, String txnId, String emiId, Bitmap bitmap, CompressFormat format);
	
	/**
	 * Initiate login request, application should call this mode if login auth
	 * mode is set to custom with following parameters:
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param password
	 *            - Ezetap password
	 */
	void startLoginRequest(Activity context, int reqCode, String username, String password);

	/**
	 * To change the password of the user
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param oldPassword
	 *            - Ezetap Old Password
	 * @param newPassword
	 *            - Ezetap New Password
	 */

	void startChangePasswordRequest(Activity context, int reqCode, String username, String oldPassword, String newPassword);

	/**
	 * To register or change the ownership of Ezetap Device
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 */

	void registerDongle(Activity context, int reqCode, String username);

	/**
	 * To retrieve the transaction history of a particular user
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 */
	void getTransactionHistory(Activity context, int reqCode, String username);

	/**
	 * To get card info from a swiped card (protected API - Contact Ezetap for
	 * usage)
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 */
	void getCardInfo(Activity context, int reqCode, String username);

	/**
	 * To initialize Ezetap device
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 */
	void initializeDevice(Activity context, int reqCode, String username);
	/**
	 * To initialize Ezetap device
	 * 
	 * @param context
	 *            - Calling activity
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */
	void initializeDevice(Activity context, int reqCode, String username, Hashtable<String, Object> appData);
	
	/**
	 * To initiate a Pre-Auth transaction, application should call 
	 * this method with following parameters
	 * 
	 * @param context
	 * 				- Calling activity
	 * @param reqCode
	 *  			- Request code for handling response
	 * @param username
	 * 				- Username
	 * * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param mobile
	 *            - Mobile number of the customer (optional)
	 * @param emailId
	 *            - Email Address of the customer (optional)
	 * @param externalReference1
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param externalReference2
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */
	void startPreAuth(Activity context, int reqCode, String username, double amount, String orderNumber, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData);
	
	/**
	 * To confirm a pre-authorized transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 * @param mobile
	 *            - Mobile number of the customer (optional)
	 * @param emailId
	 *            - Email Address of the customer (optional)
	 * @param externalReference1
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param externalReference2
	 *            - Additional External Reference associated with the
	 *            transaction (optional)
	 * @param appData
	 *            - Opaque App specific data (optional)
	 */
	void confirmPreAuth(Activity context, int reqCode, String username, double amount, String orderNumber, String mobile, String emailId, String externalReference2, String externalReference3, Hashtable<String, Object> appData);
	
	/**
	 * To release a pre-authorized transaction, application should call this
	 * method with following parameters:
	 * 
	 * @param context
	 *            - Calling activity's context
	 * @param reqCode
	 *            - Request code for handling response
	 * @param username
	 *            - Ezetap username
	 * @param amount
	 *            - Transaction amount
	 * @param orderNumber
	 *            - External Reference associated with the transaction
	 */
	void releasePreAuth(Activity context, int reqCode, String username, double amount, String orderNumber, Hashtable<String, Object> appData);
}