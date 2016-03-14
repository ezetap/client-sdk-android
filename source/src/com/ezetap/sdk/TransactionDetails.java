/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * 
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *
 *******************************************************/

package com.ezetap.sdk;

import org.json.JSONObject;


/**
 * @author JAYESH
 *
 */

/**
 * Transaction Details which can be queried by the APP
 */

public class TransactionDetails {

	private final String transactionId;
	private String authCode;
	private boolean signatureAttached;
	private boolean isVoidable;
	private double amount;
	private double totalAmount;
	private String merchantName;

	private String customerMobile;
	private String timeStamp;// formatted timestamp
	private String lastFourDigits;// if this transaction is a card transaction
	private String nameOnCard;// if this transaction is a card transaction
	private String paymentMode;// card / cash / cheque

	private String externalRefNum;
	private String amountFormatted;
	private String customerReceiptUrl;
	private String reverseReferenceNumber;
	private String batchNumber;
	private String invoiceNumber;
	private String status;
	private String cardType;
	private String emiId;
	private boolean signReqd;

	public TransactionDetails(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @param nameOnCard
	 *            the nameOnCard to set
	 */
	protected void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	/**
	 * @return the nameOnCard
	 */
	public String getNameOnCard() {
		return nameOnCard;
	}

	/**
	 * @param merchantName
	 *            the merchantName to set
	 */
	protected void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * @param customerMobile
	 *            the customerMobile to set
	 */
	protected void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	/**
	 * @return the customerMobile
	 */
	public String getCustomerMobile() {
		return customerMobile;
	}

	/**
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	protected void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param isVoidable
	 *            the isVoidable to set
	 */
	protected void setVoidable(boolean isVoidable) {
		this.isVoidable = isVoidable;
	}

	/**
	 * @return the isVoidable
	 */
	public boolean isVoidable() {
		return isVoidable;
	}
	
	/**
	 * 
	 * @param emiId
	 */
	public void setEMIId(String emiId) {
		this.emiId = emiId;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEMIId(){
		return emiId;
	}
	
	
	public void setSignReqd(boolean signReqd) {
		this.signReqd = signReqd;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSignReqd() {
		return signReqd;
	}

	/**
	 * @param paymentMode
	 *            the paymentMode to set
	 */
	protected void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}

	/**
	 * @param lastFourDigits
	 *            the lastFourDigits to set
	 */
	protected void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	/**
	 * @return the lastFourDigits
	 */
	public String getLastFourDigits() {
		return lastFourDigits;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	protected void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	protected void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	public String getExternalRefNum() {
		return externalRefNum;
	}

	public String getAmountFormatted() {
		return amountFormatted;
	}

	public String getCustomerReceiptUrl() {
		return customerReceiptUrl;
	}

	public String getReverseReferenceNumber() {
		return reverseReferenceNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getStatus() {
		return status;
	}

	public String getCardType() {
		return cardType;
	}

	/**
	 * @param authCode
	 *            the authCode to set
	 */
	protected void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	public static TransactionDetails getTransactionDetails(JSONObject jsonTxn) {

		try {

			TransactionDetails aTxn = new TransactionDetails(jsonTxn.getString(EzeConstants.KEY_TRANSACTION_ID));
			if (jsonTxn.has(EzeConstants.KEY_AUTH_CODE))
				aTxn.setAuthCode(jsonTxn.getString(EzeConstants.KEY_AUTH_CODE));

			if (jsonTxn.has(EzeConstants.KEY_AMOUNT)) {
				aTxn.setAmount(jsonTxn.getDouble(EzeConstants.KEY_AMOUNT));
			}

			if (jsonTxn.has(EzeConstants.KEY_TOTAL_AMOUNT)) {
				aTxn.setTotalAmount(jsonTxn.getDouble(EzeConstants.KEY_TOTAL_AMOUNT));
			}

			if (jsonTxn.has(EzeConstants.KEY_PAYMENT_MODE)) {
				aTxn.setPaymentMode(jsonTxn.getString(EzeConstants.KEY_PAYMENT_MODE));
			}
			
			if (jsonTxn.has(EzeConstants.KEY_PAYMENT_MODE)) {
				aTxn.setPaymentMode(jsonTxn.getString(EzeConstants.KEY_PAYMENT_MODE));
			}

			if (jsonTxn.has(EzeConstants.KEY_CUSTOMER_MOBILE_RESP)) {
				aTxn.setCustomerMobile(jsonTxn.getString(EzeConstants.KEY_CUSTOMER_MOBILE_RESP));
			}

			if (jsonTxn.has(EzeConstants.KEY_CHARGE_SLIP_DATE)) {
				aTxn.setTimeStamp(jsonTxn.getString(EzeConstants.KEY_CHARGE_SLIP_DATE));
			}

			if (jsonTxn.has(EzeConstants.KEY_CARD_LAST_4_DIGITS)) {
				aTxn.setLastFourDigits(jsonTxn.getString(EzeConstants.KEY_CARD_LAST_4_DIGITS));
			}

			if (jsonTxn.has(EzeConstants.KEY_NAME_ON_CARD)) {
				aTxn.setNameOnCard(jsonTxn.getString(EzeConstants.KEY_NAME_ON_CARD));
			}

			if (jsonTxn.has(EzeConstants.KEY_MERCHANT_NAME)) {
				aTxn.setMerchantName(jsonTxn.getString(EzeConstants.KEY_MERCHANT_NAME));
			}

			// TODO: jayesh need to remove KEY_SIGNATURE_ATTACHED as we are
			// going to use KEY_IS_SIGNATURE_ATTACHED
			if (jsonTxn.has(EzeConstants.KEY_SIGNATURE_ATTACHED)) {
				aTxn.setSignatureAttached(jsonTxn.getBoolean(EzeConstants.KEY_SIGNATURE_ATTACHED));
			}

			if (jsonTxn.has(EzeConstants.KEY_IS_SIGNATURE_ATTACHED)) {
				aTxn.setSignatureAttached(jsonTxn.getBoolean(EzeConstants.KEY_IS_SIGNATURE_ATTACHED));
			}

			if (jsonTxn.has(EzeConstants.KEY_IS_VOIDABLE)) {
				aTxn.setVoidable(jsonTxn.getBoolean(EzeConstants.KEY_IS_VOIDABLE));
			}

			if (jsonTxn.has(EzeConstants.KEY_TXN_EXT_REF_NUM))
				aTxn.externalRefNum = jsonTxn.getString(EzeConstants.KEY_TXN_EXT_REF_NUM);

			if (jsonTxn.has(EzeConstants.KEY_AMOUNT))
				aTxn.amountFormatted = jsonTxn.getString(EzeConstants.KEY_AMOUNT);

			if (jsonTxn.has(EzeConstants.KEY_CUST_RCPT_URL))
				aTxn.customerReceiptUrl = jsonTxn.getString(EzeConstants.KEY_CUST_RCPT_URL);

			if (jsonTxn.has(EzeConstants.KEY_TXN_RRN))
				aTxn.reverseReferenceNumber = jsonTxn.getString(EzeConstants.KEY_TXN_RRN);

			if (jsonTxn.has(EzeConstants.KEY_TXN_BATCH))
				aTxn.batchNumber = jsonTxn.getString(EzeConstants.KEY_TXN_BATCH);

			if (jsonTxn.has(EzeConstants.KEY_TXN_INVOICE))
				aTxn.invoiceNumber = jsonTxn.getString(EzeConstants.KEY_TXN_INVOICE);

			if (jsonTxn.has(EzeConstants.KEY_TXN_STATUS))
				aTxn.status = jsonTxn.getString(EzeConstants.KEY_TXN_STATUS);

			if (jsonTxn.has(EzeConstants.KEY_TXN_CARD_TYPE))
				aTxn.cardType = jsonTxn.getString(EzeConstants.KEY_TXN_CARD_TYPE);

			if (jsonTxn.has(EzeConstants.KEY_IS_VOIDABLE)) {
				aTxn.setVoidable(jsonTxn.getBoolean(EzeConstants.KEY_IS_VOIDABLE));
			}

			if(jsonTxn.has(EzeConstants.KEY_EMI_ID)) {
				aTxn.setEMIId(jsonTxn.getString(EzeConstants.KEY_EMI_ID));
			}
			
			if (jsonTxn.has(EzeConstants.KEY_SIGNATURE_REQD)) {
				aTxn.setSignReqd(jsonTxn.getBoolean(EzeConstants.KEY_SIGNATURE_REQD));
			}
			
			return aTxn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param signatureAttached
	 *            the signatureAttached to set
	 */
	protected void setSignatureAttached(boolean signatureAttached) {
		this.signatureAttached = signatureAttached;
	}

	/**
	 * @return the signatureAttached
	 */
	public boolean isSignatureAttached() {
		return signatureAttached;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

}
