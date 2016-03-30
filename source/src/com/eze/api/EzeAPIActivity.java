/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * <p/>
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *******************************************************/
package com.eze.api;

import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.eze.api.EzeAPIConstants.EzetapErrors;
import com.ezetap.sdk.AppConstants;
import com.ezetap.sdk.EzeConstants;
import com.ezetap.sdk.EzeConstants.AppMode;
import com.ezetap.sdk.EzeConstants.CommunicationChannel;
import com.ezetap.sdk.EzeConstants.LoginAuthMode;
import com.ezetap.sdk.EzetapApiConfig;
import com.ezetap.sdk.EzetapPayApis;

public class EzeAPIActivity extends Activity {

	private JSONArray jsonArray = null;

	/**
	 * Variable used to save user config temporarily till initialize is
	 * successful
	 */
	private EzetapApiConfig tempConfig = null;
	/**
	 * Variable used to save user name temporarily till initialize is successful
	 */
	private String tempUserName = null;
	/**
	 * Variable used to temporarily save the user selected work flow
	 */
	private int WORKFLOW_SELECTED = 0;
	/**
	 * Variable used to temporarily save request parameter of user if service
	 * app is not install and after install execute single work flow.
	 */

	private String APIParams = null;

	/**
	 * {@inheritDoc Inherited method which is invoked when Activity is created}
	 **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getAction() != null) {
			if (getIntent().hasExtra("params"))
				invokeEzeAPIs(getIntent().getAction(), getIntent()
						.getStringExtra("params"));
			else
				invokeEzeAPIs(getIntent().getAction(), "");
		} else {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		}
	}

	/**
	 * Method to delegate the request to Ezetap service app
	 * 
	 * @param String
	 *            API - Ezetap API name as passed from JS
	 * @param String
	 *            APIParams - Arguments to the Ezetap API, as passed from JS
	 */
	private void invokeEzeAPIs(String API, String APIParams) {
		WORKFLOW_SELECTED = returnWorkflow(API);
		this.APIParams = APIParams;
		try {
			switch (WORKFLOW_SELECTED) {
			case EzeAPIConstants.WORKFLOW_INIT:
				initializeUser(new JSONArray(APIParams));
				break;
			case EzeAPIConstants.WORKFLOW_PREPARE_DEVICE:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						prepareDevice(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;
			case EzeAPIConstants.WORKFLOW_DO_WALLETTRANSACTION:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateWalletPayment(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;
			case EzeAPIConstants.WORKFLOW_DO_CHEQUETRANSACTION:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateChequePayment(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;
			case EzeAPIConstants.WORKFLOW_DO_CARDTRANSACTION:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateCardPayment(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;
			case EzeAPIConstants.WORKFLOW_DO_CASHTRANSACTION:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateCashPayment(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;

			case EzeAPIConstants.WORKFLOW_GET_TRANSACTIONHISTORY:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateGetTransactionHistory(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				}
				break;
			case EzeAPIConstants.WORKFLOW_VOID_TRANSACTION:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateVoidTransaction(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case EzeAPIConstants.WORKFLOW_ATTACH_SIGNATURE:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateSignatureAPI(new JSONArray(APIParams));
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case EzeAPIConstants.WORKFLOW_DO_UPDATE:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateUpdate();
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;
			case EzeAPIConstants.WORKFLOW_DO_LOGOUT:
				if (EzetapUserConfig.getEzeUserConfig() != null) {
					if (findTargetAppPackage(this) != null) {
						initiateLogout();
					} else {
						validateDeviceFirmware(new JSONArray(APIParams));
					}
				} else
					ezetapErrorCallBack(
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorCode(),
							EzetapErrors.ERROR_INIT_REQUIRED.getErrorMessage());
				break;

			case EzeAPIConstants.WORKFLOW_SEND_RECEIPT:
				ezetapErrorCallBack(
						EzetapErrors.ERROR_API_SENDRECEIPT.getErrorCode(),
						EzetapErrors.ERROR_API_SENDRECEIPT.getErrorMessage());
				break;
			case EzeAPIConstants.WORKFLOW_GET_TRANSACTIONDETAILS:
				ezetapErrorCallBack(
						EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL
								.getErrorCode(),
						EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL
								.getErrorMessage());
				break;
			case EzeAPIConstants.WORKFLOW_DO_LOGIN:
				initiateLogin(new JSONArray(APIParams));
				break;
			default:
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				break;
			}
		} catch (Exception e) {

			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		}
	}

	/**
	 * Method to return the Work flow
	 * 
	 * @param String
	 *            apiName - Accepts API name as an argument
	 */
	private int returnWorkflow(String apiName) {
		if (apiName.equalsIgnoreCase("initialize"))
			return EzeAPIConstants.WORKFLOW_INIT;
		if (apiName.equalsIgnoreCase("prepareDevice"))
			return EzeAPIConstants.WORKFLOW_PREPARE_DEVICE;
		if (apiName.equalsIgnoreCase("walletTransaction"))
			return EzeAPIConstants.WORKFLOW_DO_WALLETTRANSACTION;
		if (apiName.equalsIgnoreCase("chequeTransaction"))
			return EzeAPIConstants.WORKFLOW_DO_CHEQUETRANSACTION;
		if (apiName.equalsIgnoreCase("cardTransaction"))
			return EzeAPIConstants.WORKFLOW_DO_CARDTRANSACTION;
		if (apiName.equalsIgnoreCase("cashTransaction"))
			return EzeAPIConstants.WORKFLOW_DO_CASHTRANSACTION;
		if (apiName.equalsIgnoreCase("chequeTransaction"))
			return EzeAPIConstants.WORKFLOW_DO_CHEQUETRANSACTION;
		if (apiName.equalsIgnoreCase("searchTransaction"))
			return EzeAPIConstants.WORKFLOW_GET_TRANSACTIONHISTORY;
		if (apiName.equalsIgnoreCase("voidtransaction"))
			return EzeAPIConstants.WORKFLOW_VOID_TRANSACTION;
		if (apiName.equalsIgnoreCase("attachsignature"))
			return EzeAPIConstants.WORKFLOW_ATTACH_SIGNATURE;
		if (apiName.equalsIgnoreCase("update"))
			return EzeAPIConstants.WORKFLOW_DO_UPDATE;
		if (apiName.equalsIgnoreCase("close"))
			return EzeAPIConstants.WORKFLOW_DO_LOGOUT;

		if (apiName.equalsIgnoreCase("login"))
			return EzeAPIConstants.WORKFLOW_DO_LOGIN;
		if (apiName.equalsIgnoreCase("sendreceipt"))
			return EzeAPIConstants.WORKFLOW_SEND_RECEIPT;
		if (apiName.equalsIgnoreCase("getTransaction"))
			return EzeAPIConstants.WORKFLOW_GET_TRANSACTIONDETAILS;

		return 0;
	}

	/**
	 * Method to initialize the user
	 * 
	 * @param JSONArray
	 *            args - Arguments to the Ezetap API, as passed from JS
	 */
	private void initializeUser(JSONArray args) {
		LoginAuthMode authMode = null;
		boolean captureSignatureBool = false;
		String appKey = null, merchantName = null, userName = null, currencyCode = null, captureSignature = null, prodAppKey = null, prepareDevice = null;
		AppMode appMode = null;
		CommunicationChannel communicationChannel = null;
		JSONObject jsonConfigData = null;
		if (EzetapUserConfig.getEzeUserConfig() == null) {
			try {
				jsonConfigData = new JSONObject(args.getString(0));
			} catch (Exception e1) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				return;
			}
			authMode = getLoginAuthCode();

			if (jsonConfigData.has(EzeAPIConstants.DEMO_APP_KEY)) {
				try {
					appKey = jsonConfigData
							.getString(EzeAPIConstants.DEMO_APP_KEY);
					if ("".equalsIgnoreCase(appKey) || appKey == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.PROD_APP_KEY)) {
				try {
					prodAppKey = jsonConfigData
							.getString(EzeAPIConstants.PROD_APP_KEY);
					if ("".equalsIgnoreCase(prodAppKey) || prodAppKey == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_MERCHANT_NAME)) {
				try {
					merchantName = jsonConfigData
							.getString(EzeAPIConstants.KEY_MERCHANT_NAME);
					if ("".equalsIgnoreCase(merchantName)
							|| merchantName == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_USER_NAME)) {
				try {
					userName = jsonConfigData
							.getString(EzeAPIConstants.KEY_USER_NAME);
					if ("".equalsIgnoreCase(userName) || userName == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					} else {
						EzetapUserConfig.setUserName(userName);
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_CURRENCY_CODE)) {
				try {
					currencyCode = jsonConfigData
							.getString(EzeAPIConstants.KEY_CURRENCY_CODE);
					if ("".equalsIgnoreCase(currencyCode)
							|| currencyCode == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_APP_MODE)) {
				try {
					appMode = getAppMode(jsonConfigData
							.getString(EzeAPIConstants.KEY_APP_MODE));
					if (appMode == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					}
					if (appMode == AppMode.EZETAP_PROD) {
						if (jsonConfigData.has(EzeAPIConstants.PROD_APP_KEY)) {
							appKey = jsonConfigData
									.getString(EzeAPIConstants.PROD_APP_KEY);
							if ("".equalsIgnoreCase(appKey) || appKey == null) {
								ezetapErrorCallBack(
										EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
												.getErrorCode(),
										EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
												.getErrorMessage());
								return;
							}
						} else {
							ezetapErrorCallBack(
									EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
											.getErrorCode(),
									EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
											.getErrorMessage());
							return;
						}
					}

				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_CAPTURE_SIGNATURE)) {
				try {
					captureSignature = jsonConfigData
							.getString(EzeAPIConstants.KEY_CAPTURE_SIGNATURE);
					if ("".equalsIgnoreCase(captureSignature)
							|| captureSignature == null)
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
					else if ("true".equalsIgnoreCase(captureSignature))
						captureSignatureBool = true;
					else if ("false".equalsIgnoreCase(captureSignature))
						captureSignatureBool = false;
					else
						captureSignatureBool = false;

				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorCode(),
						EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
								.getErrorMessage());
				return;
			}

			if (jsonConfigData.has(EzeAPIConstants.KEY_COMMUNICATION_CHANNEL)) {
				try {
					communicationChannel = getCommunicationChannel(jsonConfigData
							.getString(EzeAPIConstants.KEY_COMMUNICATION_CHANNEL));
					if (communicationChannel == null) {
						communicationChannel = CommunicationChannel.NONE;
					}
				} catch (Exception e) {
					communicationChannel = CommunicationChannel.NONE;
				}
			} else {
				communicationChannel = CommunicationChannel.NONE;
			}

			if (jsonConfigData.has(EzeAPIConstants.PREPARE_DEVICE)) {
				try {
					prepareDevice = jsonConfigData
							.getString(EzeAPIConstants.PREPARE_DEVICE);
					if ("".equalsIgnoreCase(prepareDevice)
							|| prepareDevice == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorCode(),
								EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
										.getErrorMessage());
						return;
					} else if ("true".equalsIgnoreCase(prepareDevice)) {
						EzetapUserConfig.setPrepareDevice(true);
					} else if ("false".equalsIgnoreCase(prepareDevice)) {
						EzetapUserConfig.setPrepareDevice(false);
					} else {
						EzetapUserConfig.setPrepareDevice(false);
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					return;
				}
			} else {
				EzetapUserConfig.setPrepareDevice(true);
			}

			EzetapUserConfig.setEzeUserConfig(new EzetapApiConfig(authMode,
					appKey, merchantName, currencyCode, appMode,
					captureSignatureBool, communicationChannel));

			if (EzetapUserConfig.getPrepareDevice()) {
				if (findTargetAppPackage(this) != null) {
					initiateDevice(args);
				} else {
					validateDeviceFirmware(args);
				}
			} else {
				JSONObject result = null;
				try {
					EzetapUserConfig.setUserName(userName);
					result = new JSONObject(
							"{\"message\":\"initialize device successful.\"}");
					ezetapSuccessCallBack(result.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			ezetapSuccessCallBack("{\"message\":\"Device instantiated!\"}");
		}
	}

	/**
	 * Method to initialize device
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void prepareDevice(JSONArray jsonArray) {
		EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.initializeDevice(this, AppConstants.REQ_CODE_INIT_DEVICE,
						EzetapUserConfig.getUserName());
	}

	/**
	 * Method to initialize device
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateDevice(JSONArray pluginArgs) {
		try {
			if (EzetapUserConfig.getEzeUserConfig().getAuthMode() == LoginAuthMode.EZETAP_LOGIN_CUSTOM) {
				initiateLogin(pluginArgs);
			} else {
				if (findTargetAppPackage(this) != null) {
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
							.initializeDevice(this,
									AppConstants.REQ_CODE_INIT_DEVICE,
									EzetapUserConfig.getUserName());
					tempConfig = EzetapUserConfig.getEzeUserConfig();
					tempUserName = EzetapUserConfig.getUserName();
				} else {
					validateDeviceFirmware(pluginArgs);
				}

			}
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		}
	}

	/**
	 * Method to initiate wallet payment
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateWalletPayment(JSONArray pluginArgs) {
		JSONObject jsonObject = null;
		Double amount = null;
		try {
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			amount = getAmount(jsonObject);
			if (amount == null) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_MISSING_AMOUNT.getErrorCode(),
						EzetapErrors.ERROR_MISSING_AMOUNT.getErrorMessage());
				return;
			}
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		try {
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.startWalletPayment(this, AppConstants.REQ_CODE_PAY_WALLET,
							EzetapUserConfig.getUserName(), amount,
							getOrderNumber(jsonObject),
							getMobileNumber(jsonObject),
							getEmailID(jsonObject),
							getCustomerName(jsonObject),
							getExternalReference1(jsonObject),
							getExternalReference2(jsonObject),
							new Hashtable<String, Object>(),
							new Hashtable<String, Object>(), getLabels());
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_API_WALLETPAYMENT.getErrorCode(),
					EzetapErrors.ERROR_API_WALLETPAYMENT.getErrorMessage());
		}
	}

	/**
	 * Method to initiate card payment
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateCardPayment(JSONArray pluginArgs) {
		Double amount = null;
		String paymentMode = null;
		Double payBackAmount = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			amount = getAmount(jsonObject);
			paymentMode = getPaymentMode(jsonObject);
			payBackAmount = getAmountCashBack(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		if (isAmountInvalid(amount, payBackAmount, paymentMode)
				|| paymentMode == null) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		} else {
			if (EzeAPIConstants.KEY_PAYMENT_MODE_SALE
					.equalsIgnoreCase(paymentMode)) {
				payBackAmount = 0.0;
			} else if (EzeAPIConstants.KEY_PAYMENT_MODE_CASHBACK
					.equalsIgnoreCase(paymentMode)) {
				if (amount == null)
					amount = 0.0;
				if (payBackAmount == null)
					payBackAmount = 0.00;
			} else if (EzeAPIConstants.KEY_PAYMENT_MODE_CASH_AT_POS
					.equalsIgnoreCase(paymentMode)) {
				amount = 0.0;
			}

			try {
				if (EzetapUserConfig.getPrepareDevice()) {
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
							.startCardPayment(this,
									AppConstants.REQ_CODE_PAY_CARD,
									EzetapUserConfig.getUserName(), amount,
									payBackAmount, getOrderNumber(jsonObject),
									getTip(jsonObject),
									getMobileNumber(jsonObject),
									getEmailID(jsonObject),
									getExternalReference1(jsonObject),
									getExternalReference2(jsonObject),
									new Hashtable<String, Object>());
				} else {
					jsonArray = pluginArgs;
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
							.initializeDevice(this,
									AppConstants.REQ_CODE_INIT_DEVICE,
									EzetapUserConfig.getUserName());
					tempConfig = EzetapUserConfig.getEzeUserConfig();
					tempUserName = EzetapUserConfig.getUserName();
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
		}
	}

	/**
	 * Method to initiate cash payment
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateCashPayment(JSONArray pluginArgs) {
		Double amount = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			amount = getAmount(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.startCashPayment(
						this,
						AppConstants.REQ_CODE_PAY_CASH,
						EzetapUserConfig.getUserName(),
						amount,
						getOrderNumber(jsonObject),
						getMobileNumber(jsonObject),// optional customer mobile
						getCustomerName(jsonObject), getEmailID(jsonObject),
						getExternalReference1(jsonObject),
						getExternalReference2(jsonObject),
						new Hashtable<String, Object>(),// optional App Data
						new Hashtable<String, Object>());
	}

	/**
	 * Method to initiate card payment
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateChequePayment(JSONArray pluginArgs) {
		Double amount = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			amount = getAmount(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
				.startChequePayment(this, AppConstants.REQ_CODE_PAY_CHEQUE,
						EzetapUserConfig.getUserName(), amount,
						getOrderNumber(jsonObject),
						getMobileNumber(jsonObject), getEmailID(jsonObject),
						getCustomerName(jsonObject),
						getChequeNumber(jsonObject), getBankCode(jsonObject),
						getBankName(jsonObject), getBankAccNumber(jsonObject),
						getChequeDate(jsonObject),
						getExternalReference1(jsonObject),
						getExternalReference2(jsonObject),
						new Hashtable<String, Object>(),
						new Hashtable<String, Object>());
	}

	/**
	 * Method to initiate Get Transaction History
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateGetTransactionHistory(JSONArray pluginArgs) {
		String userName = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgs.get(0).toString());
			userName = getUserNameForTransHistory(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		if (userName == null) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		} else {
			try {
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
						.getTransactionHistory(this,
								AppConstants.REQ_CODE_GET_TXN_LIST, userName);
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TRANSHISTORY
								.getErrorMessage());
			}
		}
	}

	/**
	 * Method to initiate Void Transaction History
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateVoidTransaction(JSONArray pluginArgs) {
		String transID = null;
		try {
			transID = pluginArgs.get(0).toString();
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		if (transID != null) {
			try {
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
						.startVoidTransaction(this,
								AppConstants.REQ_CODE_VOID_TXN,
								EzetapUserConfig.getUserName(), transID);
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorMessage());
			}
		}
	}

	/**
	 * Method to initiate Login
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateLogin(JSONArray pluginArgs) {
		String strPwd = null;
		String strUserName = null;
		try {
			strUserName = new JSONObject(pluginArgs.get(0).toString())
					.getString(EzeAPIConstants.KEY_USER_NAME);
		} catch (Exception e1) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage()
							+ e1.getMessage());
		}
		try {
			strPwd = new JSONObject(pluginArgs.get(0).toString())
					.getString(EzeAPIConstants.KEY_PASSWORD);
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		}
		if (strPwd == null)
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		else if (strUserName == null)
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		else
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.startLoginRequest(this, AppConstants.REQ_CODE_LOGIN,
							strUserName, strPwd);
	}

	/**
	 * Method to initiate Attach Signature APIS
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private void initiateSignatureAPI(JSONArray pluginArgumemnts) {
		String transID = null;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(pluginArgumemnts.get(0).toString());
			transID = getTransactionID(jsonObject);
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			return;
		}
		if (transID != null) {
			switch (getAttachSignFlow(jsonObject)) {
			case EzeAPIConstants.WORKFLOW_ATTSIGN_WITHIMG:
				CompressFormat imgFormat = null;
				Bitmap imageBitmap = null;
				try {
					imageBitmap = getImageBitmap(jsonObject);
				} catch (Throwable e2) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage() + e2.getMessage());
					return;
				}
				try {
					imgFormat = getImageFormat(jsonObject);
				} catch (Throwable e1) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage() + e1.getMessage());
					return;
				}
				if (imgFormat == null) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
					return;
				} else if (imageBitmap == null) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
					return;
				} else
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
							.attachSignature(this,
									AppConstants.REQ_CODE_ATTACH_SIGN,
									EzetapUserConfig.getUserName(), transID,
									imageBitmap, imgFormat);

				break;
			case EzeAPIConstants.WORKFLOW_ATTSIGN_WITHEMI:
				String emiID = null;
				CompressFormat format = null;
				Bitmap imageBmp = null;
				try {
					format = getImageFormat(jsonObject);
				} catch (Throwable e1) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage() + e1.getMessage());
				}
				try {
					emiID = getEmiID(jsonObject);
				} catch (Throwable e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				}
				try {
					imageBmp = getImageBitmap(jsonObject);
				} catch (Throwable e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				}
				if (emiID == null)
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				else if (format == null)
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				else if (imageBmp == null)
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				else
					EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
							.attachSignature(this,
									AppConstants.REQ_CODE_ATTACH_SIGN,
									EzetapUserConfig.getUserName(), transID,
									emiID, imageBmp, format);
				break;
			case EzeAPIConstants.WORKFLOW_ATTSIGN_WITHOUTIMG:
				EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
						.attachSignature(this,
								AppConstants.REQ_CODE_ATTACH_SIGN,
								EzetapUserConfig.getUserName(), transID);
				break;
			case EzeAPIConstants.WORKFLOW_ATTSIGN_ERRIMG:
				break;
			}
		} else {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		}

	}

	/**
	 * Method to initiate Update API
	 */
	private void initiateUpdate() {
		try {
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.checkForUpdate(this, AppConstants.REQ_CODE_CHECK_UPDATE,
							EzetapUserConfig.getUserName());
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		}
	}

	/**
	 * Method to initiate Logout API
	 */
	private void initiateLogout() {
		try {
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig()).logout(
					this, AppConstants.REQ_CODE_LOGOUT,
					EzetapUserConfig.getUserName());
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage());
		}
	}

	/**
	 * Method which returns the cash back amount from the takePayment arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private Double getAmountCashBack(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has("amountCashback")) {
					Double amountCashback = Double.parseDouble(jsonOptions.get(
							"amountCashback").toString());
					if (amountCashback <= 0)
						return null;
					else
						return amountCashback;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method which returns getExternalReference2 from the initiatePayment
	 * arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private String getExternalReference2(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_REFERENCES)) {
					JSONObject jsonReferences = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_REFERENCES).toString());
					if (jsonReferences.has(EzeAPIConstants.KEY_REFERENCES3)) {
						String orderNum = jsonReferences.get(
								EzeAPIConstants.KEY_REFERENCES3).toString();
						if ("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Method which returns getExternalReference1 from the initiatePayment
	 * arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private String getExternalReference1(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_REFERENCES)) {
					JSONObject jsonReferences = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_REFERENCES).toString());
					if (jsonReferences.has(EzeAPIConstants.KEY_REFERENCES2)) {
						String orderNum = jsonReferences.get(
								EzeAPIConstants.KEY_REFERENCES2).toString();
						if ("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Method to return the payment mode from the takePayment arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getPaymentMode(JSONObject jsonParams) throws Exception {
		String mode = null;
		if (jsonParams.has("mode")) {
			mode = jsonParams.get("mode").toString();
			if ("".equalsIgnoreCase(mode))
				return null;
			else
				return mode;
		} else {
			return null;
		}
	}

	/**
	 * Method to return the amount from the takePayment arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private Double getAmount(JSONObject jsonParams) throws Exception {
		if (jsonParams.has(EzeAPIConstants.KEY_TOTAL_AMOUNT)) {
			Double amt = Double.parseDouble(jsonParams.get(
					EzeAPIConstants.KEY_TOTAL_AMOUNT).toString());
			if (amt <= 0)
				return null;
			else
				return amt;
		} else {
			return null;
		}
	}

	/**
	 * Method to return the user name from the getTransactionHistory arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getUserNameForTransHistory(JSONObject jsonParams)
			throws Exception {
		if (jsonParams.has("agentName")) {
			String username = jsonParams.get("agentName").toString();
			if ("".equalsIgnoreCase(username))
				return null;
			else
				return username;
		} else {
			return null;
		}
	}

	/**
	 * Method to return the order number from the takePayment arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getOrderNumber(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_REFERENCES)) {
					JSONObject jsonReferences = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_REFERENCES).toString());
					if (jsonReferences.has(EzeAPIConstants.KEY_REFERENCES1)) {
						String orderNum = jsonReferences.get(
								EzeAPIConstants.KEY_REFERENCES1).toString();
						if ("".equalsIgnoreCase(orderNum))
							return null;
						else
							return orderNum;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the cheque number from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getChequeNumber(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_CHEQUE)) {
				JSONObject jsonCheque = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_CHEQUE).toString());
				if (jsonCheque.has(EzeAPIConstants.KEY_CHEQUE_NUMBER)) {
					String chqNumber = jsonCheque.get(
							EzeAPIConstants.KEY_CHEQUE_NUMBER).toString();
					if ("".equalsIgnoreCase(chqNumber))
						return null;
					else
						return chqNumber;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the Bank Code from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getBankCode(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_CHEQUE)) {
				JSONObject jsonCheque = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_CHEQUE).toString());
				if (jsonCheque.has(EzeAPIConstants.KEY_BANK_CODE)) {
					String bankCode = jsonCheque.get(
							EzeAPIConstants.KEY_BANK_CODE).toString();
					if ("".equalsIgnoreCase(bankCode))
						return null;
					else
						return bankCode;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the Bank Name from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getBankName(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_CHEQUE)) {
				JSONObject jsonCheque = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_CHEQUE).toString());
				if (jsonCheque.has(EzeAPIConstants.KEY_BANK_NAME)) {
					String bankName = jsonCheque.get(
							EzeAPIConstants.KEY_BANK_NAME).toString();
					if ("".equalsIgnoreCase(bankName))
						return null;
					else
						return bankName;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the Bank Account Number from the takePayment API
	 * arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getBankAccNumber(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_CHEQUE)) {
				JSONObject jsonCheque = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_CHEQUE).toString());
				if (jsonCheque.has(EzeAPIConstants.KEY_BANK_ACCOUNT_NUMBER)) {
					String bankAccNum = jsonCheque.get(
							EzeAPIConstants.KEY_BANK_ACCOUNT_NUMBER).toString();
					if ("".equalsIgnoreCase(bankAccNum))
						return null;
					else
						return bankAccNum;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the Cheque Date from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getChequeDate(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_CHEQUE)) {
				JSONObject jsonCheque = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_CHEQUE).toString());
				if (jsonCheque.has(EzeAPIConstants.KEY_CHEQUE_DATE)) {
					String chqDate = jsonCheque.get(
							EzeAPIConstants.KEY_CHEQUE_DATE).toString();
					if ("".equalsIgnoreCase(chqDate))
						return null;
					else
						return chqDate;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the User Name from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getUserName(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_USER_NAME)) {
				String userName = jsonParams.get(EzeAPIConstants.KEY_USER_NAME)
						.toString();
				if ("".equalsIgnoreCase(userName))
					return null;
				else
					return userName;
			} else
				return null;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the customer name from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - The request object as passed from JS
	 */
	private String getCustomerName(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_CUSTOMER)) {
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_CUSTOMER).toString());
					if (jsonCustomer.has(EzeAPIConstants.KEY_NAME)) {
						String name = jsonCustomer
								.get(EzeAPIConstants.KEY_NAME).toString();
						if ("".equalsIgnoreCase(name))
							return null;
						else
							return name;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the labels as a String array for wallet payment from the
	 * User Config
	 */
	private String[] getLabels() {
		String[] labels = new String[] { "WALLET_USER_"
				+ EzetapUserConfig.getUserName() };
		return labels;
	}

	/**
	 * Method to return the customer mobile number from the takePayment API
	 * arguments
	 * 
	 * @param JSONArray
	 *            args - Arguments to the Ezetap API, as passed from JS
	 */
	private String getMobileNumber(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_CUSTOMER)) {
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_CUSTOMER).toString());
					if (jsonCustomer.has(EzeAPIConstants.KEY_MOBILENO)) {
						String mobileNum = jsonCustomer.get(
								EzeAPIConstants.KEY_MOBILENO).toString();
						if ("".equalsIgnoreCase(mobileNum))
							return null;
						else
							return mobileNum;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the Email ID from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private String getEmailID(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_CUSTOMER)) {
					JSONObject jsonCustomer = new JSONObject(jsonOptions.get(
							EzeAPIConstants.KEY_CUSTOMER).toString());
					if (jsonCustomer.has(EzeAPIConstants.KEY_EMAIL)) {
						String email = jsonCustomer.get(
								EzeAPIConstants.KEY_EMAIL).toString();
						if ("".equalsIgnoreCase(email))
							return null;
						else
							return email;
					} else
						return null;
				} else
					return null;
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to return the tip amount from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            args - The request object as passed from JS
	 */
	private double getTip(JSONObject jsonParams) {
		try {
			if (jsonParams.has(EzeAPIConstants.KEY_OPTIONS)) {
				JSONObject jsonOptions = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_OPTIONS).toString());
				if (jsonOptions.has(EzeAPIConstants.KEY_AMOUNT_TIP)) {
					Double tip = Double.parseDouble(jsonOptions.get(
							EzeAPIConstants.KEY_AMOUNT_TIP).toString());
					if (tip <= 0)
						return 0;
					else
						return tip;
				} else
					return 0;
			} else
				return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Method to return the transaction Id from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private String getTransactionID(JSONObject jsonParams) throws Exception {
		if (jsonParams.has(EzeAPIConstants.KEY_TRANSACTION_ID)) {
			String txnId = jsonParams.get(EzeAPIConstants.KEY_TRANSACTION_ID)
					.toString();
			if ("".equalsIgnoreCase(txnId))
				return null;
			else
				return txnId;
		} else {
			return null;
		}
	}

	/**
	 * Method to return the AttachSignature Workflow
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private int getAttachSignFlow(JSONObject jsonParams) {
		if (jsonParams.has(EzeAPIConstants.KEY_IMAGE)) {
			JSONObject imageObj;
			try {
				imageObj = new JSONObject(jsonParams.get(
						EzeAPIConstants.KEY_IMAGE).toString());
				if (imageObj.has(EzeAPIConstants.KEY_IMAGE_DATA)) {
					if (jsonParams.has(EzeAPIConstants.KEY_EMI_ID))
						return EzeAPIConstants.WORKFLOW_ATTSIGN_WITHEMI;
					else
						return EzeAPIConstants.WORKFLOW_ATTSIGN_WITHIMG;
				} else
					return EzeAPIConstants.WORKFLOW_ATTSIGN_ERRIMG;
			} catch (Exception e) {
				return EzeAPIConstants.WORKFLOW_ATTSIGN_ERRIMG;
			}
		} else {
			return EzeAPIConstants.WORKFLOW_ATTSIGN_WITHOUTIMG;
		}
	}

	/**
	 * Method to return the EMI Id from the takePayment API arguments
	 * 
	 * @param JSONObject
	 *            jsonParams - Arguments to the Ezetap API, as passed from JS
	 */
	private String getEmiID(JSONObject jsonParams) throws Exception {
		if (jsonParams.has(EzeAPIConstants.KEY_EMI_ID)) {
			if (jsonParams.get(EzeAPIConstants.KEY_EMI_ID).toString()
					.equalsIgnoreCase(""))
				return null;
			else
				return jsonParams.get(EzeAPIConstants.KEY_EMI_ID).toString();
		} else
			return null;
	}

	/**
	 * Method to return the image format from the Attach Signature API arguments
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private CompressFormat getImageFormat(JSONObject jsonParams)
			throws Exception {
		JSONObject imageObj;
		imageObj = new JSONObject(jsonParams.get(EzeAPIConstants.KEY_IMAGE)
				.toString());
		if (imageObj.has(EzeAPIConstants.KEY_IMAGE_TYPE)) {
			if (imageObj.get(EzeAPIConstants.KEY_IMAGE_TYPE).toString()
					.equalsIgnoreCase(""))
				return null;
			else {
				if (imageObj.get(EzeAPIConstants.KEY_IMAGE_TYPE).toString()
						.equalsIgnoreCase("jpeg"))
					return CompressFormat.JPEG;
				else if (imageObj.get(EzeAPIConstants.KEY_IMAGE_TYPE)
						.toString().equalsIgnoreCase("png"))
					return CompressFormat.PNG;
				else if (imageObj.get(EzeAPIConstants.KEY_IMAGE_TYPE)
						.toString().equalsIgnoreCase("webp"))
					return CompressFormat.WEBP;
				else
					return null;
			}
		} else
			return null;
	}

	/**
	 * Method to return the image format from the Attach Signature API arguments
	 * 
	 * @param JSONArray
	 *            pluginArgs - Arguments to the Ezetap API, as passed from JS
	 */
	private Bitmap getImageBitmap(JSONObject jsonParams) throws Exception {
		JSONObject imageObj;
		imageObj = new JSONObject(jsonParams.get(EzeAPIConstants.KEY_IMAGE)
				.toString());
		if (imageObj.has(EzeAPIConstants.KEY_IMAGE_DATA)) {
			byte[] decodedString = Base64.decode(
					imageObj.get(EzeAPIConstants.KEY_IMAGE_DATA).toString(),
					Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(decodedString, 0,
					decodedString.length);
		} else {
			return null;
		}
	}

	/**
	 * Util method to return the value of an attribute for the given key
	 * 
	 * @param JSONObject
	 *            object - The JSON object which has the key/value pair.
	 * @param String
	 *            key - Key for which value has to be fetched.
	 * @return Returns an empty array if key is not found
	 */
	private String getValueForKey(JSONObject object, String key) {
		try {
			if (object.has(key))
				return object.get(key).toString();
			else
				return "";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Method to validate communicationChannel and return the default values.
	 * 
	 * @param String
	 *            communicationChannel - Accepts CommunicationChannel which is
	 *            passed by the user in Initialize API.
	 */
	private CommunicationChannel getCommunicationChannel(
			String communicationChannel) {
		if (communicationChannel.equalsIgnoreCase(""))
			return null;
		else if (communicationChannel
				.equalsIgnoreCase(CommunicationChannel.MOCK.toString()))
			return CommunicationChannel.MOCK;
		else if (communicationChannel
				.equalsIgnoreCase(CommunicationChannel.NONE.toString()))
			return CommunicationChannel.NONE;
		else if (communicationChannel
				.equalsIgnoreCase(CommunicationChannel.USBA.toString()))
			return CommunicationChannel.USBA;
		return null;
	}

	/**
	 * Method to return the app mode.
	 * 
	 * @param String
	 *            appMode. - Accepts app mode which is passed by the user in
	 *            Initialize API.
	 */
	private AppMode getAppMode(String appMode) {
		if (appMode.equalsIgnoreCase(""))
			return null;
		else if (appMode.equalsIgnoreCase(EzeAPIConstants.KEY_APP_MODE_DEMO))
			return AppMode.EZETAP_DEMO;
		else if (appMode.equalsIgnoreCase(EzeAPIConstants.KEY_APP_MODE_PREPROD))
			return AppMode.EZETAP_PREPROD;
		else if (appMode.equalsIgnoreCase(EzeAPIConstants.KEY_APP_MODE_PROD))
			return AppMode.EZETAP_PROD;
		return null;
	}

	/**
	 * Method to return the auth code. EZETAP_LOGIN_BYPASS is the only option
	 * supported in this version of our API.
	 */
	private LoginAuthMode getLoginAuthCode() {
		return LoginAuthMode.EZETAP_LOGIN_BYPASS;
	}

	/**
	 * Method to validate amount for a Card Transaction
	 * 
	 * @param Double
	 *            amount - Sale amount
	 * @param Double
	 *            amount - Cashback amount amount
	 * @param String
	 *            paymentMode amount - Sale amount
	 */
	private boolean isAmountInvalid(Double amount, Double payBackAmount,
			String paymentMode) {
		if (EzeAPIConstants.KEY_PAYMENT_MODE_SALE.equalsIgnoreCase(paymentMode)) {
			return amount == null;
		} else if (EzeAPIConstants.KEY_PAYMENT_MODE_CASHBACK
				.equalsIgnoreCase(paymentMode)) {
			return amount == null && payBackAmount == null;
		} else if (EzeAPIConstants.KEY_PAYMENT_MODE_CASH_AT_POS
				.equalsIgnoreCase(paymentMode)) {
			return payBackAmount == null;
		} else
			return true;
	}

	/**
	 * {@inheritDoc Callback method from Ezetap service app}
	 **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		switch (requestCode) {
		case AppConstants.REQ_CODE_INIT_DEVICE:
			initDeviceCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_PAY_WALLET:
			takeWalletPaymentCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_PAY_CHEQUE:
			takeChequePaymentCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_PAY_CARD:
			takeCardPaymentCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_PAY_CASH:
			takeCashPaymentCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_GET_TXN_LIST:
			getTransactionsCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_VOID_TXN:
			voidTransactionCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_ATTACH_SIGN:
			attachSignatureCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_CHECK_UPDATE:
			checkUpdateCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_LOGOUT:
			logoutCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_LOGIN:
			loginCallBack(intent, resultCode);
			break;
		case AppConstants.REQ_CODE_INSTALL:
			installCallBack(intent, resultCode);
			break;

		default:
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for initialize
	 * API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void initDeviceCallBack(Intent data, int resultCode) {
		try {
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				if (tempConfig != null) {
					EzetapUserConfig.setEzeUserConfig(tempConfig);
					EzetapUserConfig.setUserName(tempUserName);
					tempUserName = null;
					tempConfig = null;
				}
				if (WORKFLOW_SELECTED == EzeAPIConstants.WORKFLOW_PREPARE_DEVICE) {
					JSONObject result = new JSONObject(
							"{\"message\":\"Prepare device successful.\"}");
					ezetapSuccessCallBack(result.toString());
				} else if (WORKFLOW_SELECTED == EzeAPIConstants.WORKFLOW_DO_CARDTRANSACTION) {
					EzetapUserConfig.setPrepareDevice(true);
					initiateCardPayment(jsonArray);
				} else {
					JSONObject result = new JSONObject(
							"{\"message\":\"Initialize and Prepare device successful.\"}");
					ezetapSuccessCallBack(result.toString());
				}
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				if (data != null) {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				}
				break;
			}
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * takeWalletPayment API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void takeWalletPaymentCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION,
								returnTransactionObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_WALLET,
								returnWalletObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_MERCHANT,
								returnMerchantObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CUSTOMER,
								returnCustomerObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION_RECEIPT,
								returnReceiptObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_REFERENCES,
								returnReferencesObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA) != null) {
					JSONObject errJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(
							errJson.getString(EzeConstants.KEY_ERROR_CODE),
							errJson.getString(EzeConstants.KEY_ERROR_MESSAGE));
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorCode(),
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * takeChequePayment API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void takeChequePaymentCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION,
								returnTransactionObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CHEQUE,
								returnChequeObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_MERCHANT,
								returnMerchantObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CUSTOMER,
								returnCustomerObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION_RECEIPT,
								returnReceiptObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_REFERENCES,
								returnReferencesObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA) != null) {
					JSONObject errJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(
							errJson.getString(EzeConstants.KEY_ERROR_CODE),
							errJson.getString(EzeConstants.KEY_ERROR_MESSAGE));
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorCode(),
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * takeCardPayment API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void takeCardPaymentCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION,
								returnTransactionObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_MERCHANT,
								returnMerchantObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CUSTOMER,
								returnCustomerObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION_RECEIPT,
								returnReceiptObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CARD_DETAILS,
								returnCardObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_REFERENCES,
								returnReferencesObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA) != null) {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(
							aJson.getString(EzeConstants.KEY_ERROR_CODE),
							aJson.getString(EzeConstants.KEY_ERROR_MESSAGE));
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorCode(),
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * takeCashPayment API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void takeCashPaymentCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION,
								returnTransactionObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_CUSTOMER,
								returnCustomerObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_TRANSACTION_RECEIPT,
								returnReceiptObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				jsonResult
						.put(EzeAPIConstants.KEY_REFERENCES,
								returnReferencesObject(data
										.getStringExtra(EzeConstants.KEY_RESPONSE_DATA)));
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA) != null) {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorCode(),
							EzetapErrors.ERROR_CANCEL_TAKEPAYMENT
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_TAKEPAYMENT
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * GetTransactionHistory API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void getTransactionsCallBack(Intent data, int resultCode) {
		try {
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				JSONObject jsonResult = new JSONObject();
				JSONArray transactionArray = new JSONArray();
				JSONObject jsonResposne = new JSONObject(
						data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
				if (jsonResposne.has(EzeAPIConstants.KEY_TRANSACTIONS)) {
					JSONArray temptransactionArray = new JSONArray(jsonResposne
							.get(EzeAPIConstants.KEY_TRANSACTIONS).toString());
					for (int i = 0; i < temptransactionArray.length(); i++) {
						JSONObject tempObj = new JSONObject();
						JSONObject tempTxn = new JSONObject(
								returnTransactionObject(
										temptransactionArray.get(i).toString())
										.toString());
						if (tempTxn.get(EzeAPIConstants.KEY_PAYMENT_MODE)
								.toString().equalsIgnoreCase("cash")) {
							tempObj.put("cheque", "");
							tempObj.put("card", "");
						} else {
							tempObj.put("cheque", "");
							tempObj.put("card",
									returnCardObject(temptransactionArray
											.get(i).toString()));
						}
						tempObj.put(EzeAPIConstants.KEY_TRANSACTION, tempTxn);
						tempObj.put(EzeAPIConstants.KEY_CUSTOMER,
								returnCustomerObject(temptransactionArray
										.get(i).toString()));
						tempObj.put(EzeAPIConstants.KEY_TRANSACTION_RECEIPT,
								returnReceiptObject(temptransactionArray.get(i)
										.toString()));
						tempObj.put(EzeAPIConstants.KEY_MERCHANT,
								returnMerchantObject(temptransactionArray
										.get(i).toString()));
						tempObj.put(EzeAPIConstants.KEY_REFERENCES,
								returnReferencesObject(temptransactionArray
										.get(i).toString()));
						transactionArray.put(i, tempObj);
					}
				}
				jsonResult.put("data", transactionArray);
				ezetapSuccessCallBack(jsonResult.toString());
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				try {
					if (data == null) {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_DEFAULT_TRANSHISTORY
										.getErrorCode(),
								EzetapErrors.ERROR_DEFAULT_TRANSHISTORY
										.getErrorMessage());
					} else {
						JSONObject aJson = new JSONObject(
								data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
						ezetapErrorCallBack(
								aJson.get(EzeConstants.KEY_ERROR_CODE)
										.toString(),
								aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
										.toString());
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_TRANSHISTORY
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_TRANSHISTORY
									.getErrorMessage());

				}
				break;
			}
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_TRANSHISTORY.getErrorMessage());
		}
	}

	/**
	 * Method to return the Transaction object back from the service app
	 * response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnReferencesObject(String transaction)
			throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject refObj = new JSONObject();
		if (jsonTxn.has(EzeAPIConstants.KEY_EXT_REF1))
			refObj.put(EzeAPIConstants.KEY_REF1,
					getValueForKey(jsonTxn, EzeAPIConstants.KEY_EXT_REF1));
		if (jsonTxn.has(EzeAPIConstants.KEY_EXT_REF2))
			refObj.put(EzeAPIConstants.KEY_REF2,
					getValueForKey(jsonTxn, EzeAPIConstants.KEY_EXT_REF2));
		if (jsonTxn.has(EzeAPIConstants.KEY_EXT_REF3))
			refObj.put(EzeAPIConstants.KEY_REF3,
					getValueForKey(jsonTxn, EzeAPIConstants.KEY_EXT_REF3));
		return refObj;
	}

	/**
	 * Method to return the response back from the service app for
	 * voidTransaction API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void voidTransactionCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				ezetapSuccessCallBack("{\"message\":\"Transaction voided.\"}");
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data != null) {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_VOIDTRANSACTION
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for
	 * attachSignature API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void attachSignatureCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			try {
				JSONObject jsonResult = new JSONObject();
				jsonResult.put("message", "Attach e-signature successful.");
				ezetapSuccessCallBack(jsonResult.toString());
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
								.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
								.getErrorMessage());
			}
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data != null) {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
									.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
								.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_ATTACHSIGNATURE
								.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for checkUpdate
	 * API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void checkUpdateCallBack(Intent data, int resultCode) {
		try {
			switch (resultCode) {
			case EzeConstants.RESULT_SUCCESS:
				JSONObject result = new JSONObject(
						"{\"message\":\"Update successful\"}");
				ezetapSuccessCallBack(result.toString());
				break;
			case EzeConstants.RESULT_FAILED:
			default:
				try {

					if (data != null) {
						JSONObject aJson = new JSONObject(
								data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
						ezetapErrorCallBack(
								aJson.get(EzeConstants.KEY_ERROR_CODE)
										.toString(),
								aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
										.toString());
					} else {
						ezetapErrorCallBack(
								EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
								EzetapErrors.ERROR_DEFAULT_INIT
										.getErrorMessage());
					}
				} catch (Exception e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				}
				break;
			}
		} catch (Exception e) {
			ezetapErrorCallBack(EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
					EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
		}
	}

	/**
	 * Method to return the response back from the service app for logout API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void logoutCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			EzetapUserConfig.setEzeUserConfig(null);
			EzetapUserConfig.setUserName(null);
			ezetapSuccessCallBack("{\"message\":\"User logged out successfully\"}");
			break;
		case EzeConstants.RESULT_FAILED:
		default:
			JSONObject aJson;
			try {
				if (data != null) {
					aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				} else {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_LOGOUT.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the response back from the service app for login API
	 * 
	 * @param int resultCode - The result code returned by the service app.
	 * @param Intent
	 *            intent - The result data returned by the service app
	 */
	private void loginCallBack(Intent data, int resultCode) {
		switch (resultCode) {
		case EzeConstants.RESULT_SUCCESS:
			EzetapPayApis.create(EzetapUserConfig.getEzeUserConfig())
					.initializeDevice(this, AppConstants.REQ_CODE_INIT_DEVICE,
							EzetapUserConfig.getUserName());
			break;

		case EzeConstants.RESULT_FAILED:
		default:
			try {
				if (data == null) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
							EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
				} else {
					JSONObject aJson = new JSONObject(
							data.getStringExtra(EzeConstants.KEY_RESPONSE_DATA));
					ezetapErrorCallBack(aJson.get(EzeConstants.KEY_ERROR_CODE)
							.toString(),
							aJson.get(EzeConstants.KEY_ERROR_MESSAGE)
									.toString());
				}
			} catch (Exception e) {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
			}
			break;
		}
	}

	/**
	 * Method to return the Transaction object back from the service app
	 * response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnTransactionObject(String transaction)
			throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject txnObject = new JSONObject();
		txnObject.put(EzeAPIConstants.KEY_TRANSACTION_ID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_TRANSACTION_ID));
		txnObject.put(EzeAPIConstants.KEY_TXN_DATE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_POSTING_DATE));
		txnObject.put(EzeAPIConstants.KEY_TOTAL_AMOUNT,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_TOTAL_AMOUNT));
		txnObject.put(EzeAPIConstants.KEY_CURRENCY_CODE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CURRENCY_CODE));
		txnObject.put(EzeAPIConstants.KEY_PAYMENT_MODE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_PAYMENT_MODE));
		txnObject.put(EzeAPIConstants.KEY_AUTH_CODE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_AUTH_CODE));
		txnObject.put(EzeAPIConstants.KEY_DEVICE_SERIAL,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_DEVICE_SERIAL));
		txnObject.put(EzeAPIConstants.KEY_MID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_MID));
		txnObject.put(EzeAPIConstants.KEY_TID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_TID));
		txnObject.put(EzeAPIConstants.KEY_EMI_ID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_EMI_ID));
		return txnObject;
	}

	/**
	 * Method to return the wallet transaction object back from the service app
	 * response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnWalletObject(String transaction) throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject txnWalletObject = new JSONObject();

		txnWalletObject.put(EzeAPIConstants.KEY_WALLET_PROVIDER,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_WALLET_PROVIDER));
		txnWalletObject
				.put(EzeAPIConstants.KEY_WALLET_CUSTOMER_ID,
						getValueForKey(jsonTxn,
								EzeAPIConstants.KEY_WALLET_CUSTOMER_ID));
		txnWalletObject.put(EzeAPIConstants.KEY_WALLET_CHANNEL_ID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_WALLET_CHANNEL_ID));
		txnWalletObject.put(EzeAPIConstants.KEY_WALLET_MID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_WALLET_MID));
		txnWalletObject.put(EzeAPIConstants.KEY_WALLET_ACQUIRER,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_WALLET_ACQUIRER));
		txnWalletObject.put(EzeAPIConstants.KEY_WALLET_REF_TXN_ID,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_WALLET_REF_TXN_ID));
		return txnWalletObject;
	}

	/**
	 * Method to return the cheque transaction object back from the service app
	 * response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnChequeObject(String transaction) throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject txnChequeObject = new JSONObject();
		txnChequeObject.put(EzeAPIConstants.KEY_BANK_CODE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_BANK_CODE));
		txnChequeObject.put(EzeAPIConstants.KEY_CHEQUE_NUMBER,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CHEQUE_NUMBER));
		txnChequeObject.put(EzeAPIConstants.KEY_CHEQUE_DATE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CHEQUE_DATE));
		txnChequeObject.put(EzeAPIConstants.KEY_BANK_NAME,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_BANK_NAME));
		txnChequeObject
				.put(EzeAPIConstants.KEY_BANK_ACCOUNT_NUMBER,
						getValueForKey(jsonTxn,
								EzeAPIConstants.KEY_BANK_ACCOUNT_NUMBER));
		return txnChequeObject;
	}

	/**
	 * Method to return the card object back from the service app response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnCardObject(String transaction) throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject cardObject = new JSONObject();
		cardObject.put(EzeAPIConstants.KEY_MASKED_CARD_NO,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_FORMATTED_PAN));
		cardObject.put(EzeAPIConstants.KEY_CARD_BRAND,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_TXN_CARD_BRAND));
		return cardObject;
	}

	/**
	 * Method to return the Merchant object back from the service app response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnMerchantObject(String transaction)
			throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject merchantObject = new JSONObject();
		merchantObject.put(EzeAPIConstants.KEY_MERCHANT_NAME,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_MERCHANT_NAME));
		merchantObject.put(EzeAPIConstants.KEY_MERCHANT_CODE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_MERCHANT_CODE));
		return merchantObject;
	}

	/**
	 * Method to return the Customer object back from the service app response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnCustomerObject(String transaction)
			throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject customerObject = new JSONObject();
		customerObject.put(EzeAPIConstants.KEY_EMAIL,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CUSTOMER_EMAIL));
		customerObject.put(EzeAPIConstants.KEY_MOBILENO,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CUSTOMER_MOBILE));
		customerObject.put(EzeAPIConstants.KEY_NAME,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CUSTOMER_NAME));
		return customerObject;
	}

	/**
	 * Method to return the Receipt object back from the service app response
	 * 
	 * @param String
	 *            transaction - The response object received from service app.
	 */
	private JSONObject returnReceiptObject(String transaction) throws Exception {
		JSONObject jsonTxn = new JSONObject(transaction);
		JSONObject receiptObject = new JSONObject();
		receiptObject.put(EzeAPIConstants.KEY_RECEIPT_DATE,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CHARGE_SLIP_DATE));
		receiptObject.put(EzeAPIConstants.KEY_RCPT_URL,
				getValueForKey(jsonTxn, EzeAPIConstants.KEY_CUST_RCPT_URL));
		return receiptObject;
	}

	/**
	 * Method to return the success result to the calling Activity
	 * 
	 * @param String
	 *            response - The response object which needs to be passed
	 */
	private void ezetapSuccessCallBack(String response) {
		JSONObject jsonResponse = new JSONObject();
		try {
			Intent intent = new Intent();
			jsonResponse.put("error", "");
			jsonResponse.put("status", "success");
			jsonResponse.put("result", new JSONObject(response));
			intent.putExtra("response", jsonResponse.toString());
			setResult(Activity.RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ezetapErrorCallBack(
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS.getErrorCode(),
					EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
							.getErrorMessage());
		}
	}

	/**
	 * Method to return the error result to the calling Activity
	 * 
	 * @param String
	 *            errorCode - The error code encountered
	 * @param String
	 *            errorMessage - The error message encountered
	 */
	private void ezetapErrorCallBack(String errorCode, String errorMessage) {
		Intent intent = new Intent();
		JSONObject jsonErrorObj = new JSONObject();
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonErrorObj.put("code", errorCode);
			jsonErrorObj.put("message", errorMessage);
			jsonResponse.put("error", jsonErrorObj);
			jsonResponse.put("status", "fail");
			jsonResponse.put("result", "");
			intent.putExtra("response", jsonResponse.toString());
		} catch (Exception ex) {
			intent.putExtra(
					"response",
					"{\"status\":\"fail\",\"result\":null,\"error\":{\"code\":\""
							+ EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode()
							+ "\",\"message\":\""
							+ EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage() + "\"}}");
		}
		setResult(Activity.RESULT_CANCELED, intent);
		finish();
	}

	private void validateDeviceFirmware(JSONArray args) {
		int targetSdkVersion = 0;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
		} catch (PackageManager.NameNotFoundException e) {
		}
		if (Build.VERSION.SDK_INT >= 23 && targetSdkVersion >= 23) {
			if (findTargetAppPackage(this) == null) {
				try {
					if (checkPermission(this)) {
						initiateUpdate();
					} else {
						requestPermission(this);
					}
				} catch (NoClassDefFoundError e) {
					ezetapErrorCallBack(
							EzetapErrors.ERROR_PERMISSION_EXCEPTION
									.getErrorCode(),
							EzetapErrors.ERROR_PERMISSION_EXCEPTION
									.getErrorMessage());
				}
			} else {
				initiateUpdate();
			}
		} else {
			initiateUpdate();
		}
	}

	protected String findTargetAppPackage(Activity context) {
		String BASE_PACKAGE = "";
		if (EzetapUserConfig.getEzeUserConfig().getMode() != null
				&& EzetapUserConfig.getEzeUserConfig().getMode()
						.equals(AppMode.EZETAP_PROD)) {
			BASE_PACKAGE = EzeAPIConstants.PROD_BASE_PACKAGE;
		}
		if (EzetapUserConfig.getEzeUserConfig().getMode() != null
				&& EzetapUserConfig.getEzeUserConfig().getMode()
						.equals(AppMode.EZETAP_PREPROD)) {
			BASE_PACKAGE = EzeAPIConstants.PREPROD_BASE_PACKAGE;
		}
		if (EzetapUserConfig.getEzeUserConfig().getMode() != null
				&& EzetapUserConfig.getEzeUserConfig().getMode()
						.equals(AppMode.EZETAP_DEMO)) {
			BASE_PACKAGE = EzeAPIConstants.DEMO_BASE_PACKAGE;
		}
		Intent intent = new Intent();
		intent.setAction(BASE_PACKAGE + EzeAPIConstants.EZETAP_PACKAGE_ACTION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> availableApps = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (availableApps != null) {
			for (ResolveInfo availableApp : availableApps) {
				String packageName = availableApp.activityInfo.packageName;
				if (BASE_PACKAGE.equals(packageName)) {
					return packageName;
				}
			}
		}
		return null;
	}

	private void requestPermission(Activity activity)
			throws NoClassDefFoundError {
		ActivityCompat.requestPermissions(activity,
				new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
				EzeAPIConstants.REQUESTCODE_WRITE_STORAGE);
	}

	private boolean checkPermission(Activity activity)
			throws NoClassDefFoundError {
		return (ContextCompat.checkSelfPermission(activity,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
	}

	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
		case EzeAPIConstants.REQUESTCODE_WRITE_STORAGE:
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				initiateUpdate();
			} else {
				ezetapErrorCallBack(
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorCode(),
						EzetapErrors.ERROR_DEFAULT_INIT.getErrorMessage());
			}
			break;
		}
	}

	private void installCallBack(Intent data, int resultCode) {
		if (findTargetAppPackage(this) != null) {
			try {
				switch (WORKFLOW_SELECTED) {
				case EzeAPIConstants.WORKFLOW_INIT:
					if (EzetapUserConfig.getPrepareDevice()) {
						EzetapUserConfig.setEzeUserConfig(null);
						EzetapUserConfig.setUserName(null);
						initializeUser(new JSONArray(APIParams));
					} else {
						initializeUser(new JSONArray(APIParams));
					}
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_PREPARE_DEVICE:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						prepareDevice(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_WALLETTRANSACTION:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateWalletPayment(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_CARDTRANSACTION:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateCardPayment(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_CASHTRANSACTION:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateCashPayment(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_CHEQUETRANSACTION:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateChequePayment(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_GET_TRANSACTIONHISTORY:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateGetTransactionHistory(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_VOID_TRANSACTION:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateVoidTransaction(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_ATTACH_SIGNATURE:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateSignatureAPI(new JSONArray(APIParams));
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_UPDATE:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateUpdate();
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;
				case EzeAPIConstants.WORKFLOW_DO_LOGOUT:
					if (EzetapUserConfig.getEzeUserConfig() != null)
						initiateLogout();
					else
						ezetapErrorCallBack(EzetapErrors.ERROR_INIT_REQUIRED
								.getErrorCode(),
								EzetapErrors.ERROR_INIT_REQUIRED
										.getErrorMessage());
					APIParams = null;
					break;

				case EzeAPIConstants.WORKFLOW_SEND_RECEIPT:
					APIParams = null;
					ezetapErrorCallBack(
							EzetapErrors.ERROR_API_SENDRECEIPT.getErrorCode(),
							EzetapErrors.ERROR_API_SENDRECEIPT
									.getErrorMessage());
					break;
				case EzeAPIConstants.WORKFLOW_GET_TRANSACTIONDETAILS:
					APIParams = null;
					ezetapErrorCallBack(
							EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL
									.getErrorCode(),
							EzetapErrors.ERROR_API_GETTRANSACTIONDETAIL
									.getErrorMessage());
					break;
				case EzeAPIConstants.WORKFLOW_DO_LOGIN:
					initiateLogin(new JSONArray(APIParams));
					APIParams = null;
					break;
				default:
					APIParams = null;
					ezetapErrorCallBack(
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorCode(),
							EzetapErrors.ERROR_MISSING_MANDATORYPARAMS
									.getErrorMessage());
					break;
				}
			} catch (Exception e) {
				APIParams = null;
				ezetapErrorCallBack(
						EzetapErrors.ERROR_INSTALL_FAILED.getErrorCode(),
						EzetapErrors.ERROR_INSTALL_FAILED.getErrorMessage());
			}
		} else {
			APIParams = null;
			ezetapErrorCallBack(
					EzetapErrors.ERROR_INSTALL_FAILED.getErrorCode(),
					EzetapErrors.ERROR_INSTALL_FAILED.getErrorMessage());
		}
	}
}
