/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/
package com.eze.api;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eze.api.EzeAPIConstants.EzetapErrors;
import com.ezetap.sdk.AppConstants;
import com.ezetap.sdk.EzeConstants;
import com.ezetap.sdk.EzeConstants.AppMode;
import com.ezetap.sdk.EzeConstants.CommunicationChannel;
import com.ezetap.sdk.EzeConstants.LoginAuthMode;
import com.ezetap.sdk.EzetapApiConfig;
import com.ezetap.sdk.EzetapPayApis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

public class EzeAPIActivity extends Activity{
	/**
	 * Variable used when user selects LOGIN API.
	 * */
	private static final int WORKFLOW_DO_LOGIN=2;
	/**
	 * Variable used when user selects ATTACH SIGNATURE API.
	 * */
	private static final int WORKFLOW_ATTACH_SIGNATURE=3;
	/**
	 * Variable used when user selects GET TRANSACTION HISTORY API.
	 * */
	private static final int WORKFLOW_GET_TRANSACTIONHISTORY=4;
	/**
	 * Variable used when user selects VOID TRANSACTION API.
	 * */
	private static final int WORKFLOW_VOID_TRANSACTION=5;
	/**
	 * Variable used when user selects LOGOUT API.
	 * */
	private static final int WORKFLOW_DO_LOGOUT=6;
	/**
	 * Variable used when user selects INITIALIZE API.
	 * */
	private static final int WORKFLOW_INIT=8;
	/**
	 * Variable used when user wants to capture the signature.
	 * */
	private static final int WORKFLOW_ATTSIGN_WITHOUTIMG=9;
	/**
	 * Variable used when user has captured the signature & invoke ATTACH SIGNATURE API.
	 * */
	private static final int WORKFLOW_ATTSIGN_WITHIMG=10;
	/**
	 * Variable used when user invoke ATTACH SIGNATURE API which has an EMI ID.
	 * */
	private static final int WORKFLOW_ATTSIGN_WITHEMI=11;
	/**
	 * Variable used when error occurred in processing image in ATTACH SIGNATURE API.
	 * */
	private static final int WORKFLOW_ATTSIGN_ERRIMG=12;
	/**
	 * Variable used when user selects SEND RECEIPT API.
	 * */
	private static final int WORKFLOW_SEND_RECEIPT=13;
	/**
	 * Variable used when user selects GET TRANSACTION DETAILS API.
	 * */
	private static final int WORKFLOW_GET_TRANSACTIONDETAILS=14;
	/**
	 * Variable used when user selects CARD PAYMENT API.
	 * */
	private static final int WORKFLOW_DO_CARDTRANSACTION=15;
	/**
	 * Variable used when user selects CASH PAYMENT API.
	 * */
	private static final int WORKFLOW_DO_CASHTRANSACTION=16;
	/**
	 * Variable used when user selects CHEQUE PAYMENT API.
	 * */
	private static final int WORKFLOW_DO_CHEQUETRANSACTION=17;
	/**
	 * Variable used when user selects CHEQUE PAYMENT API.
	 * */
	private static final int WORKFLOW_DO_WALLETTRANSACTION=18;
	/**
	 * Variable used when user selects CHEQUE PAYMENT API.
	 * */
	private static final int WORKFLOW_PREPARE_DEVICE=19;
	/**
	 * Variable used to save user config temporarily till initialize is successful
	 * */
	private EzetapApiConfig tempConfig=null;
	/**
	 * Variable used to save user name temporarily till initialize is successful
	 * */
	private String tempUserName=null;
	/**
	 * Variable used to temporarily save the user selected workflow
	 * */
	private int WORKFLOW_SELECTED=0;

	/**
	 * {@inheritDoc Inherited method which is invoked when Activity is created} 
	 **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getAction()!=null){
			if(getIntent().hasExtra("params"))
				invokeEzeAPIs(getIntent().getAction(),getIntent().getStringExtra("params"));
			else
				invokeEzeAPIs(getIntent().getAction(),"");
		}else
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(), EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
	}

	/**
	 * Method to delegate the request to Ezetap service app
	 * @param String API
	 * 					- Ezetap API name as passed from JS
	 * @param String APIParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void invokeEzeAPIs(String API, String APIParams) {
		WORKFLOW_SELECTED = returnWorkflow(API);
		try{
			switch (WORKFLOW_SELECTED) {
			case WORKFLOW_INIT:
				initializeUser(new JSONArray(APIParams));
				break;				
			case WORKFLOW_ATTACH_SIGNATURE:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateSignatureAPI(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_DO_LOGIN:
				initiateLogin(new JSONArray(APIParams));
				break;
			case WORKFLOW_DO_LOGOUT:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateLogout();
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_GET_TRANSACTIONHISTORY:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateGetTransactionHistory(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_DO_CARDTRANSACTION:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateCardPayment(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_DO_CASHTRANSACTION:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateCashPayment(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_DO_CHEQUETRANSACTION:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateChequePayment(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_DO_WALLETTRANSACTION:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateWalletPayment(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_VOID_TRANSACTION:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					initiateVoidTransaction(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case WORKFLOW_SEND_RECEIPT:
				ezetapErrorCallBack(EzetapErrors.ERROR_API_SENDRECEIPT.getErrorCode(),EzetapErrors.ERROR_API_SENDRECEIPT.getErrorMessage());
				break;
			case WORKFLOW_GET_TRANSACTIONDETAILS:
				ezetapErrorCallBack(EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL.getErrorCode(),EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL.getErrorMessage());
				break;
			case WORKFLOW_PREPARE_DEVICE:
				if(EzetapUserConfig.getEzeUserConfig()!=null)
					prepareDevice(new JSONArray(APIParams));
				else
					ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(), EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			default:
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				break;
			}
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
		}
	}

	/**
	 * {@inheritDoc Callback method from Ezetap service app} 
	 **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
		case AppConstants.REQ_CODE_PAY_CARD:
			takePaymentCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_INIT_DEVICE:
			initDeviceCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_CHECK_UPDATE:
			checkUpdateCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_ATTACH_SIGN:
			attachSignatureCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_LOGIN:
			loginCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_LOGOUT:
			logoutCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_PAY_CASH:
			takeCashPaymentCallBack(intent,resultCode);
			break;
		case AppConstants.REQ_CODE_GET_TXN_LIST:
			getTransactionsCallBack(resultCode,intent);
			break;
		case AppConstants.REQ_CODE_VOID_TXN:
			voidTransactionCallBack(resultCode,intent);
			break;
		default:
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
			break;
		}
	}

	/**
	 * Method to initialize the user
	 * @param JSONArray args
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initializeUser(JSONArray args){
		LoginAuthMode authMode = null;
		boolean captureSignatureBool = false;
		String appKey=null,merchantName=null,currencyCode=null,ezetapUserName=null,captureSignature=null;
		AppMode appMode = null;
		CommunicationChannel communicationChannel = null;
		JSONObject jsonConfigData = null;
		if(EzetapUserConfig.getEzeUserConfig()==null){
			try {
				jsonConfigData = new JSONObject(args.getString(0));
			} catch (Exception e1) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				return;
			}
			authMode = getLoginAuthCode();

			if(jsonConfigData.has("demoAppKey")){
				try {
					appKey = jsonConfigData.getString("demoAppKey");
					if("".equalsIgnoreCase(appKey) || appKey==null){
						ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					return;
				}
			}else{
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				return;
			}

			if(jsonConfigData.has("merchantName")){
				try {
					merchantName = jsonConfigData.getString("merchantName");
					if("".equalsIgnoreCase(merchantName) || merchantName==null){
						ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					return;
				}
			}else{
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				return;
			}

			if(jsonConfigData.has("currencyCode")){
				try {
					currencyCode = jsonConfigData.getString("currencyCode");
					if("".equalsIgnoreCase(currencyCode) || currencyCode==null){
						ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					return;
				}
			}else{
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				return;
			}

			if(jsonConfigData.has("appMode")){
				try {
					appMode = getAppMode(jsonConfigData.getString("appMode"));
					if(appMode==null){
						ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
						return;
					}
					if(appMode==AppMode.EZETAP_PROD){
						if(jsonConfigData.has("prodAppKey")){
							appKey = jsonConfigData.getString("prodAppKey");
							if("".equalsIgnoreCase(appKey) || appKey==null){
								ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
								return;
							}
						}else{
							ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
							return;							
						}
					}

				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					return;
				}
			}else{
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				return;
			}

			if(jsonConfigData.has("captureSignature")){
				try {
					captureSignature = jsonConfigData.getString("captureSignature");
					if("".equalsIgnoreCase(captureSignature) || captureSignature==null)
						ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					else if("true".equalsIgnoreCase(captureSignature))
						captureSignatureBool=true;
					else if("false".equalsIgnoreCase(captureSignature))
						captureSignatureBool=false;
					else
						captureSignatureBool=false;

				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
					return;
				}
			}else{
				ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
				return;
			}

			if(jsonConfigData.has("communicationChannel")){
				try {
					communicationChannel = getCommunicationChannel(jsonConfigData.getString("communicationChannel"));
					if(communicationChannel==null){
						communicationChannel = CommunicationChannel.NONE;
					}
				} catch (Exception e) {
					communicationChannel = CommunicationChannel.NONE;
				}
			}else{
				communicationChannel = CommunicationChannel.NONE;
			}

			ezetapUserName=EzetapUserConfig.getUserName();

			EzetapUserConfig.setEzeUserConfig(new EzetapApiConfig(authMode, appKey, merchantName, currencyCode, appMode, captureSignatureBool, communicationChannel));
			EzetapUserConfig.setUserName(ezetapUserName);
			initiateDevice(args);
		}else{
			ezetapSuccessCallBack("{\"message\":\"Device instantiated!\"}");
		}
	}

	/**
	 * Method to validate communicationChannel and return the default values.
	 * @param String communicationChannel
	 * 					- Accepts CommunicationChannel which is passed by the user in Initialize API.
	 * */
	private CommunicationChannel getCommunicationChannel(String communicationChannel) {
		if(communicationChannel.equalsIgnoreCase(""))
			return null;
		else if(communicationChannel.equalsIgnoreCase(CommunicationChannel.MOCK.toString()))
			return CommunicationChannel.MOCK;
		else if(communicationChannel.equalsIgnoreCase(CommunicationChannel.NONE.toString()))
			return CommunicationChannel.NONE;
		else if(communicationChannel.equalsIgnoreCase(CommunicationChannel.USBA.toString()))
			return CommunicationChannel.USBA;
		return null;
	}

	/**
	 * Method to return the app mode.
	 * @param String appMode.
	 * 					- Accepts app mode which is passed by the user in Initialize API.
	 * */
	private AppMode getAppMode(String appMode) {
		if(appMode.equalsIgnoreCase(""))
			return null;
		else if(appMode.equalsIgnoreCase("DEMO"))
			return AppMode.EZETAP_DEMO;
		else if(appMode.equalsIgnoreCase("PREPROD"))
			return AppMode.EZETAP_PREPROD;
		else if(appMode.equalsIgnoreCase("PROD"))
			return AppMode.EZETAP_PROD;
		return null;
	}

	/**
	 * Method to return the auth code. EZETAP_LOGIN_BYPASS is the only option supported in this version
	 * of our API.
	 * */
	private LoginAuthMode getLoginAuthCode(){
		return LoginAuthMode.EZETAP_LOGIN_BYPASS;
	}

	/**
	 * Method to initiate cash payment
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateCashPayment(JSONArray pluginArgs){
		String userName = null;
		Double amount = null;
		JSONObject jsonObject = null;
		try{
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			userName = getUserName(jsonObject);
			amount = getAmount(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()+e.getMessage());
			return;
		}
		EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
		.startCashPayment(
				this,
				AppConstants.REQ_CODE_PAY_CASH,
				userName,
				amount,
				getOrderNumber(jsonObject),
				0.0,//Tip
				getMobileNumber(jsonObject),//optional customer mobile number
				getCustomerName(jsonObject));//optional App Data
	}
	/**
	 * Method to initiate card payment
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateChequePayment(JSONArray pluginArgs){
		ezetapErrorCallBack(EzetapErrors.ERROR_API_CHEQUE.getErrorCode(),EzetapErrors.ERROR_API_CHEQUE.getErrorMessage());
	}
	/**
	 * Method to initiate card payment
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateWalletPayment(JSONArray pluginArgs){
		ezetapErrorCallBack(EzetapErrors.ERROR_API_WALLETPAYMENT.getErrorCode(),EzetapErrors.ERROR_API_WALLETPAYMENT.getErrorMessage());
	}
	/**
	 * Method to initiate card payment
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateCardPayment(JSONArray pluginArgs){
		String userName = null;
		Double amount = null;
		String paymentMode = null;
		Double payBackAmount = null;
		JSONObject jsonObject = null;
		try{
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			userName = getUserName(jsonObject);
			amount = getAmount(jsonObject);
			paymentMode = getPaymentMode(jsonObject);
			payBackAmount = getAmountCashBack(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()+e.getMessage());
			return;
		}
		if(userName==null || isAmountInvalid(amount,payBackAmount,paymentMode) || paymentMode==null){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
		}else{
			if("SALE".equalsIgnoreCase(paymentMode)){
				payBackAmount=0.0;
			}else if("CASHBACK".equalsIgnoreCase(paymentMode)){
				if(amount==null)
					amount=0.0;
				if(payBackAmount==null)
					payBackAmount=0.00;
			}else if("CASH@POS".equalsIgnoreCase(paymentMode)){
				amount = 0.0;
			}		
			try{	
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.startCardPayment(
						this,
						AppConstants.REQ_CODE_PAY_CARD,
						userName, 
						amount,
						payBackAmount,
						getOrderNumber(jsonObject),
						getTip(jsonObject),
						getMobileNumber(jsonObject),
						getEmailID(jsonObject), 
						getExternalReference1(jsonObject),
						getExternalReference2(jsonObject),
						new Hashtable<String, Object>());
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage()+e.getMessage());
			}
		}
	}

	/**
	 * Method to validate amount for a Card Transaction
	 * @param Double amount
	 * 					- Sale amount
	 * @param Double amount
	 * 					- Cashback amount amount
	 * @param String paymentMode amount
	 * 					- Sale amount
	 * 
	 * */
	private boolean isAmountInvalid(Double amount, Double payBackAmount, String paymentMode) {
		if("SALE".equalsIgnoreCase(paymentMode)){
			if(amount==null)
				return true;
			else
				return false;
		}else if("CASHBACK".equalsIgnoreCase(paymentMode)){
			if(amount==null && payBackAmount == null)
				return true;
			else
				return false;
		}else if("CASH@POS".equalsIgnoreCase(paymentMode)){
			if(payBackAmount == null)
				return true;
			else
				return false;
		}else
			return true;
	}

	/**
	 * Method to initiate Login
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateLogin(JSONArray pluginArgs){
		String strPwd = null;
		String strUserName = null;
		try {
			strUserName = new JSONObject(pluginArgs.get(0).toString()).getString("userName");
		} catch (Exception e1) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(), EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e1.getMessage());
		}
		try {
			strPwd = new JSONObject(pluginArgs.get(0).toString()).getString("password");
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(), EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
		}
		if(strPwd==null)
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(), EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		else if(strUserName==null)
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(), EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		else
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
			.startLoginRequest(
					this, 
					AppConstants.REQ_CODE_LOGIN,
					strUserName,
					strPwd);
	}

	/**
	 * Method to initiate Attach Signature APIS
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateSignatureAPI(JSONArray pluginArgumemnts){
		String transID =null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgumemnts.get(0).toString());
			transID = getTransactionID(jsonObject);
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(), EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()+e.getMessage());
			return;
		}
		if(transID!=null){
			switch(getAttachSignFlow(jsonObject)){
			case WORKFLOW_ATTSIGN_WITHIMG:
				CompressFormat imgFormat = null;
				Bitmap imageBitmap = null;
				try {
					imageBitmap = getImageBitmap(jsonObject);
				} catch (Throwable e2) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e2.getMessage());
					return;
				}
				try {
					imgFormat = getImageFormat(jsonObject);
				} catch (Throwable e1) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e1.getMessage());
					return;
				}
				if(imgFormat==null){
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
					return;
				}else if(imageBitmap==null){
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
					return;
				}else
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.attachSignature(
							this,
							AppConstants.REQ_CODE_ATTACH_SIGN,
							EzetapUserConfig.getUserName(),
							transID,
							imageBitmap,
							imgFormat);

				break;
			case WORKFLOW_ATTSIGN_WITHEMI:
				String emiID = null;
				CompressFormat format = null;
				Bitmap imageBmp = null;
				try {
					format = getImageFormat(jsonObject);
				} catch (Throwable e1) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e1.getMessage());
				}
				try {
					emiID = getEmiID(jsonObject);
				} catch (Throwable e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e.getMessage());
				}
				try {
					imageBmp = getImageBitmap(jsonObject);
				} catch (Throwable e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e.getMessage());
				}
				if(emiID==null)
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
				else if(format==null)
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
				else if(imageBmp==null)
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(), EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
				else
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.attachSignature(
							this,
							AppConstants.REQ_CODE_ATTACH_SIGN,
							EzetapUserConfig.getUserName(),
							transID,
							emiID,
							imageBmp,
							format);
				break;
			case WORKFLOW_ATTSIGN_WITHOUTIMG:
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.attachSignature(
						this, 
						AppConstants.REQ_CODE_ATTACH_SIGN, 
						EzetapUserConfig.getUserName(),
						transID);
				break;
			case WORKFLOW_ATTSIGN_ERRIMG:
				break;
			}
		}else{
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(), EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
		}

	}

	/**
	 * Method to initiate Get Transaction History
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateGetTransactionHistory(JSONArray pluginArgs){
		String userName = null;
		JSONObject jsonObject = null;
		try{
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			userName = getUserNameForTransHistory(jsonObject);
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()+e.getMessage());
			return;			
		}
		if(userName==null){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
		}else{
			try {
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.getTransactionHistory(
						this, 
						AppConstants.REQ_CODE_GET_TXN_LIST, 
						userName);
			} catch(Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorMessage());
			}
		}
	}

	/**
	 * Method to initiate Void Transaction History
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateVoidTransaction(JSONArray pluginArgs){
		String transID =null;
		try {
			transID = pluginArgs.get(0).toString();
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(), EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()+e.getMessage());
			return;
		}
		if(transID!=null){
			try {
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.startVoidTransaction(
						this, 
						AppConstants.REQ_CODE_VOID_TXN, 
						EzetapUserConfig.getUserName(),
						transID);
			}
			catch(Exception e){
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorCode(), EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorMessage()+e.getMessage());
			}
		}
	}

	/**
	 * Method to initiate Logout API
	 * */
	private void initiateLogout(){
		try {
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
			.logout(
					this, 
					AppConstants.REQ_CODE_LOGOUT, 
					EzetapUserConfig.getUserName());
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage()+e.getMessage());
		}
	}

	/**
	 * Method to initialize device
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void prepareDevice(JSONArray jsonArray) {
		EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
		.initializeDevice(
				this, 
				AppConstants.REQ_CODE_INIT_DEVICE, 
				EzetapUserConfig.getUserName());
	}

	/**
	 * Method to initialize device
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private void initiateDevice(JSONArray pluginArgs){
		try {
			if(EzetapUserConfig.getEzeUserConfig().getAuthMode()==LoginAuthMode.EZETAP_LOGIN_CUSTOM){
				initiateLogin(pluginArgs);
			}else{
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.initializeDevice(
						this, 
						AppConstants.REQ_CODE_INIT_DEVICE, 
						EzetapUserConfig.getUserName());
				tempConfig = EzetapUserConfig.getEzeUserConfig();
				tempUserName = EzetapUserConfig.getUserName();
				EzetapUserConfig.setEzeUserConfig(null);
				EzetapUserConfig.setUserName(null);
			}
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
		}
	}

	/**
	 * Method which returns the cash back amount from the takePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private Double getAmountCashBack(JSONObject jsonParams) {
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("amountCashback")){
					Double amountCashback = Double.parseDouble(jsonOptions.get("amountCashback").toString());
					if(amountCashback <= 0)
						return null;
					else
						return amountCashback;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}
	}

	/**
	 * Method which returns getExternalReference2 from the initiatePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getExternalReference2(JSONObject jsonParams) {
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("references")){
					JSONObject jsonReferences = new JSONObject(jsonParams.get("references").toString());
					if(jsonReferences.has("reference3")){
						String orderNum = jsonReferences.get("reference3").toString();
						if("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}

	}

	/**
	 * Method which returns getExternalReference1 from the initiatePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getExternalReference1(JSONObject jsonParams) {
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("references")){
					JSONObject jsonReferences = new JSONObject(jsonParams.get("references").toString());
					if(jsonReferences.has("reference2")){
						String orderNum = jsonReferences.get("reference2").toString();
						if("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}

	}

	/**
	 * Method to return the user name from the takePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private String getUserName(JSONObject jsonParams) throws Exception{
		String uname = null;
		if(jsonParams.has("agentName")){
			uname = jsonParams.get("agentName").toString();
			if("".equalsIgnoreCase(uname))
				return null;
			else
				return uname;
		}else{
			return null;
		}
	}

	/**
	 * Method to return the payment mode from the takePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private String getPaymentMode(JSONObject jsonParams) throws Exception{
		String mode = null;
		if(jsonParams.has("mode")){
			mode = jsonParams.get("mode").toString();
			if("".equalsIgnoreCase(mode))
				return null;
			else
				return mode;
		}else{
			return null;
		}
	}

	/**
	 * Method to return the amount from the takePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private Double getAmount(JSONObject jsonParams) throws Exception{
		if(jsonParams.has("amount")){
			Double amt = Double.parseDouble(jsonParams.get("amount").toString());
			if(amt<=0)
				return null;
			else				
				return amt;
		}else{
			return null;
		}
	}

	/**
	 * Method to return the user name from the getTransactionHistory arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private String getUserNameForTransHistory(JSONObject jsonParams)throws Exception{
		if(jsonParams.has("agentName")){
			String username = jsonParams.get("agentName").toString();
			if("".equalsIgnoreCase(username))
				return null;
			else				
				return username;
		}else{
			return null;
		}
	}

	/**
	 * Method to return the order number from the takePayment arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private String getOrderNumber(JSONObject jsonParams){
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("references")){
					JSONObject jsonReferences = new JSONObject(jsonParams.get("references").toString());
					if(jsonReferences.has("reference1")){
						String orderNum = jsonReferences.get("reference1").toString();
						if("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}
	}

	/**
	 * Method to return the customer name from the takePayment API arguments 
	 * @param JSONObject jsonParams
	 * 					- The request object as passed from JS
	 * */
	private String getCustomerName(JSONObject jsonParams) {
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("customer")){
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get("customer").toString());
					if(jsonCustomer.has("name")){
						String name = jsonCustomer.get("name").toString();
						if("".equalsIgnoreCase(name))
							return null;
						else
							return name;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}
	}

	/**
	 * Method to return the customer mobile number from the takePayment API arguments 
	 * @param JSONArray args
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getMobileNumber(JSONObject jsonParams){
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("customer")){
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get("customer").toString());
					if(jsonCustomer.has("mobileNo")){
						String mobileNum = jsonCustomer.get("mobileNo").toString();
						if("".equalsIgnoreCase(mobileNum))
							return null;
						else
							return mobileNum;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}
	}

	/**
	 * Method to return the Email ID from the takePayment API arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getEmailID(JSONObject jsonParams){
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("customer")){
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get("customer").toString());
					if(jsonCustomer.has("mobileNo")){
						String email = jsonCustomer.get("email").toString();
						if("".equalsIgnoreCase(email))
							return null;
						else
							return email;
					}else
						return null;
				}else
					return null;
			}else
				return null;
		} catch (Exception e) {
			return null;				
		}
	}

	/**
	 * Method to return the tip amount from the takePayment API arguments 
	 * @param JSONObject args
	 * 					-  The request object as passed from JS
	 * */
	private double getTip(JSONObject jsonParams){
		try {
			if(jsonParams.has("options")){
				JSONObject jsonOptions = new JSONObject(jsonParams.get("options").toString());
				if(jsonOptions.has("amountTip")){
					Double tip = Double.parseDouble(jsonOptions.get("amountTip").toString());
					if(tip <= 0)
						return 0;
					else
						return tip;
				}else
					return 0;
			}else
				return 0;
		} catch (Exception e) {
			return 0;				
		}
	}

	/**
	 * Method to return the transaction Id from the takePayment API arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getTransactionID(JSONObject jsonParams) throws Exception {
		if(jsonParams.has("txnId")){
			String txnId = jsonParams.get("txnId").toString();
			if("".equalsIgnoreCase(txnId))
				return null;
			else				
				return txnId;
		}else{
			return null;
		}
	}

	/**
	 * Method to return the AttachSignature Workflow
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private int getAttachSignFlow(JSONObject jsonParams) {
		if(jsonParams.has("image")){
			JSONObject imageObj;
			try {
				imageObj = new JSONObject(jsonParams.get("image").toString());
				if(imageObj.has("imageData")){
					if(jsonParams.has("emiID"))
						return WORKFLOW_ATTSIGN_WITHEMI;
					else
						return WORKFLOW_ATTSIGN_WITHIMG;
				}else
					return WORKFLOW_ATTSIGN_ERRIMG;
			}catch (Exception e) {				
				return WORKFLOW_ATTSIGN_ERRIMG;
			}
		}else{
			return WORKFLOW_ATTSIGN_WITHOUTIMG;
		}
	}

	/**
	 * Method to return the EMI Id from the takePayment API arguments 
	 * @param JSONObject jsonParams
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private String getEmiID(JSONObject jsonParams) throws Exception {
		if(jsonParams.has("emiID")){
			if(jsonParams.get("emiID").toString().equalsIgnoreCase(""))
				return null;
			else
				return jsonParams.get("emiID").toString();
		}else
			return null;
	}

	/**
	 * Method to return the image format from the Attach Signature API arguments 
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */	
	private CompressFormat getImageFormat(JSONObject jsonParams) throws Exception {
		JSONObject imageObj;
		imageObj = new JSONObject(jsonParams.get("image").toString());
		if(imageObj.has("imageType")){
			if(imageObj.get("imageType").toString().equalsIgnoreCase(""))
				return null;
			else{
				if(imageObj.get("imageType").toString().equalsIgnoreCase("jpeg"))
					return CompressFormat.JPEG;
				else if(imageObj.get("imageType").toString().equalsIgnoreCase("png"))
					return CompressFormat.PNG;
				else if(imageObj.get("imageType").toString().equalsIgnoreCase("webp"))
					return CompressFormat.WEBP;
				else
					return null;
			}
		}else
			return null;
	}

	/**
	 * Method to return the image format from the Attach Signature API arguments 
	 * @param JSONArray pluginArgs
	 * 					- Arguments to the Ezetap API, as passed from JS
	 * */
	private Bitmap getImageBitmap(JSONObject jsonParams) throws Exception {
		JSONObject imageObj;
		imageObj = new JSONObject(jsonParams.get("image").toString());
		if(imageObj.has("imageData")){
			byte[] decodedString = Base64.decode(imageObj.get("imageData").toString(), Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		}else{
			return null;
		}
	}

	/**
	 * Method to return the Work flow
	 * @param String apiName
	 * 					- Accepts API name as an argument
	 * */
	private int returnWorkflow(String apiName){
		if(apiName.equalsIgnoreCase("login"))
			return WORKFLOW_DO_LOGIN;
		if(apiName.equalsIgnoreCase("cardTransaction"))
			return WORKFLOW_DO_CARDTRANSACTION;
		if(apiName.equalsIgnoreCase("cashTransaction"))
			return WORKFLOW_DO_CASHTRANSACTION;
		if(apiName.equalsIgnoreCase("chequeTransaction"))
			return WORKFLOW_DO_CHEQUETRANSACTION;
		if(apiName.equalsIgnoreCase("walletTransaction"))
			return WORKFLOW_DO_WALLETTRANSACTION;
		if(apiName.equalsIgnoreCase("attachsignature"))
			return WORKFLOW_ATTACH_SIGNATURE;
		if(apiName.equalsIgnoreCase("searchTransaction"))
			return WORKFLOW_GET_TRANSACTIONHISTORY;
		if(apiName.equalsIgnoreCase("voidtransaction"))
			return WORKFLOW_VOID_TRANSACTION;
		if(apiName.equalsIgnoreCase("close"))
			return WORKFLOW_DO_LOGOUT;
		if(apiName.equalsIgnoreCase("initialize"))
			return WORKFLOW_INIT;
		if(apiName.equalsIgnoreCase("sendreceipt"))
			return WORKFLOW_SEND_RECEIPT;
		if(apiName.equalsIgnoreCase("getTransaction"))
			return WORKFLOW_GET_TRANSACTIONDETAILS;
		if(apiName.equalsIgnoreCase("prepareDevice"))
			return WORKFLOW_PREPARE_DEVICE;		
		return 0;		
	}

	/**
	 * Method to return the response back from the service app for GetTransactionHistory API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void getTransactionsCallBack(int resultCode, Intent intent) {
		try{
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				JSONObject jsonResult = new JSONObject();
				JSONArray transactionArray = new JSONArray();
				JSONObject jsonResposne  = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
				if(jsonResposne.has("txns")){
					JSONArray temptransactionArray = new JSONArray(jsonResposne.get("txns").toString());
					for (int i = 0; i < temptransactionArray.length(); i++) {
						JSONObject tempObj = new JSONObject();
						JSONObject tempTxn = new JSONObject(returnTransactionObject(temptransactionArray.get(i).toString()).toString());
						if(tempTxn.get(EzeAPIConstants.KEY_PAYMENT_MODE).toString().equalsIgnoreCase("cash")){
							tempObj.put("cheque","");
							tempObj.put("card","");
						}else{
							tempObj.put("cheque","");
							tempObj.put("card",returnCardObject(temptransactionArray.get(i).toString()));
						}
						tempObj.put("txn",tempTxn);
						tempObj.put("customer",returnCustomerObject(temptransactionArray.get(i).toString()));
						tempObj.put("receipt",returnReceiptObject(temptransactionArray.get(i).toString()));
						tempObj.put("merchant", returnMerchantObject(temptransactionArray.get(i).toString()));
						transactionArray.put(i, tempObj);
					}
				}
				jsonResult.put("data",transactionArray);
				ezetapSuccessCallBack(jsonResult.toString());
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				try {
					if(intent==null){
						ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorMessage());
					}else{
						JSONObject aJson = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
						ezetapErrorCallBack(
								aJson.get(EzeConstants.KEY_ERROR_CODE).toString(),
								aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
					}
				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorMessage()+e.getMessage());

				}
				break;
			}
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorMessage()+e.getMessage());
		}
	}

	/**
	 * Method to return the response back from the service app for attachSignature API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void attachSignatureCallBack(int resultCode, Intent data) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult.put("message", "Attach e-signature successful.");
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(),EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e.getMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if(data!=null){
					JSONObject aJson = new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(),EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorCode(),EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE.getErrorMessage()+e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for logout API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void logoutCallBack(int resultCode, Intent intent) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			EzetapUserConfig.setEzeUserConfig(null);
			EzetapUserConfig.setUserName(null);
			ezetapSuccessCallBack("{\"message\":\"User logged out successfully\"}");
			break;
		case EzeConstants.RESULT_FAILED:
		default : 
			JSONObject aJson;
			try {
				if(intent!=null){
					aJson = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage()+e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for login API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void loginCallBack(int resultCode, Intent data) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
			.initializeDevice(
					this, 
					AppConstants.REQ_CODE_INIT_DEVICE, 
					EzetapUserConfig.getUserName());
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				EzetapUserConfig.setEzeUserConfig(null);
				EzetapUserConfig.setUserName(null);
				if(data==null){
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				}else{
					JSONObject aJson = new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for voidTransaction API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void voidTransactionCallBack(int resultCode, Intent intent) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				ezetapSuccessCallBack("{\"message\":\"Transaction voided.\"}");
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorCode(),EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorMessage()+e.getMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if(intent!=null){
					JSONObject aJson = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorCode(),EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorCode(),EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION.getErrorMessage()+e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for checkUpdate API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void checkUpdateCallBack(int resultCode, Intent intent) {
		try{
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				JSONObject result = new JSONObject("{\"message\":\"Initialization successful\"}");
				ezetapSuccessCallBack(result.toString());
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				try {
					EzetapUserConfig.setEzeUserConfig(null);
					EzetapUserConfig.setUserName(null);
					if(intent!=null){
						JSONObject aJson = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
						ezetapErrorCallBack(
								aJson.get(EzeConstants.KEY_ERROR_CODE).toString(),
								aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
					}else{
						ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
					}
				} catch (Exception e) {
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
				}
				break;
			}
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
		}
	}

	/**
	 * Method to return the response back from the service app for initialize API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void initDeviceCallBack(int resultCode, Intent intent){
		try{
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				if(tempConfig!=null){
					EzetapUserConfig.setEzeUserConfig(tempConfig);
					EzetapUserConfig.setUserName(tempUserName);
					tempUserName = null;
					tempConfig = null;
				}
				if(WORKFLOW_SELECTED == WORKFLOW_PREPARE_DEVICE){
					JSONObject result = new JSONObject("{\"message\":\"Prepare device successful.\"}");
					ezetapSuccessCallBack(result.toString());
				}else{
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig()).
					checkForUpdate(
							this, 
							AppConstants.REQ_CODE_CHECK_UPDATE, 
							EzetapUserConfig.getUserName());					
				}
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				EzetapUserConfig.setEzeUserConfig(null);
				EzetapUserConfig.setUserName(null);
				if(intent!=null){
					JSONObject aJson = new JSONObject(intent.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				}
				break;
			}
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()+e.getMessage());
		}
	}

	/**
	 * Method to return the response back from the service app for takeCardPayment API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void takePaymentCallBack(Intent data,int resultCode){
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult.put("txn", returnTransactionObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("merchant", returnMerchantObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("customer", returnCustomerObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("receipt", returnReceiptObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("cardDetails", returnCardObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage()+e.getMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if(data!=null){
					JSONObject aJson = new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.getString(EzeConstants.KEY_ERROR_CODE), aJson.getString(EzeConstants.KEY_ERROR_MESSAGE));
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage()+e.getMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for takeCashPayment API
	 * @param int resultCode
	 * 					- The result code returned by the service app.
	 * @param Intent intent
	 * 					- The result data returned by the service app
	 * */
	private void takeCashPaymentCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult.put("txn", returnTransactionObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("customer", returnCustomerObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult.put("receipt", returnReceiptObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage()+e.getMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if(data!=null){
					JSONObject aJson = new JSONObject(data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE).toString(), aJson.get(EzeConstants.KEY_ERROR_MESSAGE).toString());
				}else{
					ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the Transaction object back from the service app response
	 * @param String transaction
	 * 					- The response object received from service app.
	 * */
	private JSONObject returnTransactionObject(String transaction) throws Exception{
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject txnObject = new JSONObject();
		txnObject.put(EzeAPIConstants.KEY_TRANSACTION_ID,getValueForKey(jsonTxn,EzeAPIConstants.KEY_TRANSACTION_ID));
		txnObject.put(EzeAPIConstants.KEY_TXN_DATE,getValueForKey(jsonTxn,EzeAPIConstants.KEY_POSTING_DATE));
		txnObject.put(EzeAPIConstants.KEY_TOTAL_AMOUNT,getValueForKey(jsonTxn,EzeAPIConstants.KEY_TOTAL_AMOUNT));
		txnObject.put(EzeAPIConstants.KEY_CURRENCY_CODE,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CURRENCY_CODE));
		txnObject.put(EzeAPIConstants.KEY_PAYMENT_MODE,getValueForKey(jsonTxn,EzeAPIConstants.KEY_PAYMENT_MODE));
		txnObject.put(EzeAPIConstants.KEY_AUTH_CODE ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_AUTH_CODE));
		txnObject.put(EzeAPIConstants.KEY_DEVICE_SERIAL ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_DEVICE_SERIAL));
		txnObject.put(EzeAPIConstants.KEY_MID ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_MID));
		txnObject.put(EzeAPIConstants.KEY_TID ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_TID));
		return txnObject;
	}

	/**
	 * Method to return the card object back from the service app response
	 * @param String transaction
	 * 					- The response object received from service app.
	 * */
	private JSONObject returnCardObject(String transaction) throws Exception{
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject cardObject = new JSONObject();
		cardObject.put(EzeAPIConstants.KEY_MASKED_CARD_NO ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_FORMATTED_PAN));
		cardObject.put(EzeAPIConstants.KEY_CARD_BRAND ,getValueForKey(jsonTxn,EzeAPIConstants.KEY_TXN_CARD_BRAND));
		return cardObject;
	}
	/**
	 * Method to return the Merchant object back from the service app response
	 * @param String transaction
	 * 					- The response object received from service app.
	 * */
	private JSONObject returnMerchantObject(String transaction) throws Exception{
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject merchantObject = new JSONObject();
		merchantObject.put(EzeAPIConstants.KEY_MERCHANT_NAME,getValueForKey(jsonTxn,EzeAPIConstants.KEY_MERCHANT_NAME));
		merchantObject.put(EzeAPIConstants.KEY_MERCHANT_CODE,getValueForKey(jsonTxn,EzeAPIConstants.KEY_MERCHANT_CODE));
		return merchantObject;
	}
	/**
	 * Method to return the Customer object back from the service app response
	 * @param String transaction
	 * 					- The response object received from service app.
	 * */
	private JSONObject returnCustomerObject(String transaction) throws Exception{
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject customerObject = new JSONObject();
		customerObject.put(EzeAPIConstants.KEY_EMAIL,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CUSTOMER_EMAIL));
		customerObject.put(EzeAPIConstants.KEY_MOBILENO,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CUSTOMER_MOBILE));
		customerObject.put(EzeAPIConstants.KEY_NAME,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CUSTOMER_NAME));
		return customerObject;
	}
	/**
	 * Method to return the Receipt object back from the service app response
	 * @param String transaction
	 * 					- The response object received from service app.
	 * */
	private JSONObject returnReceiptObject(String transaction) throws Exception{
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject receiptObject = new JSONObject();
		receiptObject.put(EzeAPIConstants.KEY_RECEIPT_DATE,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CHARGE_SLIP_DATE));
		receiptObject.put(EzeAPIConstants.KEY_RCPT_URL,getValueForKey(jsonTxn,EzeAPIConstants.KEY_CUST_RCPT_URL));
		return receiptObject;
	}

	/**
	 * Util method to return the value of an attribute for the given key
	 * @param JSONObject object
	 * 					- The JSON object which has the key/value pair.
	 * @param String key
	 * 					- Key for which value has to be fetched.
	 * 
	 * @return Returns an empty array if key is not found
	 * */
	private String getValueForKey(JSONObject object,String key){
		try {
			if(object.has(key))
				return object.get(key).toString();
			else
				return "";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Method to return the success result to the calling Activity 
	 * @param String response
	 * 					- The response object which needs to be passed
	 * */
	private void ezetapSuccessCallBack(String response){		
		JSONObject jsonResponse = new JSONObject();
		try{
			Intent intent = new Intent();
			jsonResponse.put("error", "");
			jsonResponse.put("status", "success");
			jsonResponse.put("result", new JSONObject(response));
			intent.putExtra("response", jsonResponse.toString());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}catch(Exception e){
			ezetapErrorCallBack(EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage());
		}
	}

	/**
	 * Method to return the error result to the calling Activity 
	 * @param String errorCode
	 * 					- The error code encountered
	 *  @param String errorMessage
	 * 					- The error message encountered
	 * */
	private void ezetapErrorCallBack(String errorCode,String errorMessage){
		Intent intent = new Intent();
		JSONObject jsonErrorObj = new JSONObject();
		JSONObject jsonResponse = new JSONObject();
		try{
			jsonErrorObj.put("ERROR_CODE", errorCode);
			jsonErrorObj.put("ERROR_MESSAGE", errorMessage);
			jsonResponse.put("error", jsonErrorObj);
			jsonResponse.put("status", "fail");
			jsonResponse.put("result", "");
			intent.putExtra("response", jsonResponse.toString());
		}catch(Exception ex){
			intent.putExtra("response","{\"status\":\"fail\",\"result\":null,\"error\":{\"ERROR_CODE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode()
			+ "\",\"ERROR_MESSAGE\":\""+EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorMessage()
			+ "\"}}");
		}
		setResult(Activity.RESULT_CANCELED, intent);
		finish();
	}
}
