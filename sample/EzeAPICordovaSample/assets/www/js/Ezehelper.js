var initBtn = document.getElementById('btnInitialize');
var msgDiv = document.getElementById('messageDesc');
var payBtn = document.getElementById('btnPaySale');
var payBtnCashback = document.getElementById('btnPayCashback');
var payBtnCashAtPOS = document.getElementById('btnPayCashAtPOS');
var payBtnCash = document.getElementById('btnPayByCash');
var payBtnTransHistory = document.getElementById('btnTransactionHistory');
var btnVoidTransaction = document.getElementById('btnVoidTransaction');
var btnAttachSignature = document.getElementById('btnAttachSignature');
var btnLogout = document.getElementById('btnLogout');
var btnPrepDevice = document.getElementById('btnPrepDevice');
var transactionID = "";


initBtn.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	var EzetapConfig = {
			"demoAppKey":"Enter your demo app key here",
			"prodAppKey":"Enter your prod app key here",	
			"merchantName":"Merchant name",//The name of your organization
			"userName":"User name",//Name of the user or agent
			"currencyCode":"INR",//Defaulted to INR. Set to appropriate currency code your application uses.
			"appMode":"DEMO",//Accepts the value DEMO, PROD and PREPROD
			"captureSignature":"false",// Set it to TRUE if you wish Ezetap to capture signature			
	};
	cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","initialize",
			[EzetapConfig]);
}

btnPrepDevice.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","prepareDevice",[]);
}

msgDiv.onclick = function(){
	$("#formData").show();
	$("#messageDiv").hide();
	$("#referenceNumber").val("");
	$("#amount").val("");
	$("#comments").val("");
	$("#amountCashBack").val("");
}
payBtnCashAtPOS.onclick = function(){
	var refNum = $("#referenceNumber").val();
	var amount = $("#amountCashBack").val();
	if(refNum!="" && amount!=""){
		var ezeTapSuccessCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction successful");
			transactionID = JSON.parse(response).result.txn.txnId;
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
		};
		var ezeTapFailureCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction failed");
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
		};

		var Request = {
				"amount": 0.00,
				"mode": "CASH@POS",
				"options": {
					"amountCashback": amount,
					"amountTip": 0.0,
					"references": {
						"reference1":refNum
					},
					"customer": {
						"name":$("#name").val(),
						"mobileNo":$("#mobile").val(),
						"email":$("#email").val()
					}
				},
		};
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","cardTransaction",[Request]);
	}else{
		alert("Reference number and Cash Back Amount are the mandatory fields for Cash@POS transaction.");
	}
}
payBtnCashback.onclick = function(){
	var refNum = $("#referenceNumber").val();
	var amount = $("#amount").val();
	var amountCashback = $("#amountCashBack").val();
	if(refNum!="" && amount!="" && amountCashback!=""){
		var ezeTapSuccessCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction successful");
			transactionID = JSON.parse(response).result.txn.txnId;
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
		};
		var ezeTapFailureCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction failed");
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
		};

		var Request = {
				"amount": amount,
				"mode": "CASHBACK",
				"options": {
					"amountCashback": amountCashback,
					"amountTip": 0.0,
					"references": {
						"reference1":refNum
					},
					"customer": {
						"name":$("#name").val(),
						"mobileNo":$("#mobile").val(),
						"email":$("#email").val()
					}
				},
		};
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","cardTransaction",[Request]);
	}else{
		alert("Please fill up mandatory fields.");
	}
}

payBtn.onclick = function(){
	var refNum = $("#referenceNumber").val();
	var amount = $("#amount").val();
	if(refNum!="" && amount!=""){
		var ezeTapSuccessCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction successful");
			transactionID = JSON.parse(response).result.txn.txnId;
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
		};
		var ezeTapFailureCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction failed");
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
		};

		var Request = {
				"amount": amount,
				"mode": "SALE",
				"options": {
					"amountCashback": 0.0,
					"amountTip": 0.0,
					"references": {
						"reference1":refNum
					},
					"customer": {
						"name":$("#name").val(),
						"mobileNo":$("#mobile").val(),
						"email":$("#email").val()
					}
				},
		};
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","cardTransaction",[Request]);
	}else{
		alert("Please fill up mandatory fields.");
	}
}

payBtnCash.onclick = function(){
	var refNum = $("#referenceNumber").val();
	var amount = $("#amount").val();
	if(refNum!="" && amount!=""){
		var ezeTapSuccessCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction successful");
			transactionID = JSON.parse(response).result.txn.txnId;
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
		};
		var ezeTapFailureCallBack = function(response){
			$("#formData").hide();
			$("#messageDiv").show();
			$("#messageTag").text("Transaction failed");
			$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
		};

		var Request = {
				"amount": amount,
				"options": {
					"references": {
						"reference1":"PS123"
					},
					"customer": {
						"name":$("#name").val(),
						"mobileNo":$("#mobile").val(),
						"email":$("#email").val()
					}
				},
		};
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","cashTransaction",[Request]);
	}else{
		alert("Please fill up mandatory fields.");
	}
}

payBtnTransHistory.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	var Request= {
			"agentName": "Demo User",
			"startDate": "1/1/2015",
			"endDate": "12/31/2015",
			"txnType": "cash",
			"txnStatus": "void"		
	};
	cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","searchTransaction",[Request]);
}

btnVoidTransaction.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	if(transactionID == "" || transactionID == undefined || transactionID == null)
		alert("Unable to find transaction ID, please make a transaction.");
	else
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","voidTransaction",
				[transactionID]);
}
btnAttachSignature.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	var imgElem = document.getElementById('sampleImg');
	var Request= {
			"txnId": transactionID,
			//"tipAmount": 0.00,
			//"emiID":"852134799",
			/*"image": {
				"imageData": getBase64Image(imgElem),
				"imageType": "PNG",
				"height": "",
				"weight": ""
			}*/
	};
	if(transactionID == "" || transactionID == undefined || transactionID == null)
		alert("Unable to find transaction ID, please make a transaction.");
	else{
		cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","attachSignature",[Request]);
	}
}
var getBase64Image = function(imgElem) {
	var canvas = document.createElement("canvas");
	canvas.width = imgElem.clientWidth;
	canvas.height = imgElem.clientHeight;
	var ctx = canvas.getContext("2d");
	ctx.drawImage(imgElem, 0, 0);
	var dataURL = canvas.toDataURL("image/png");
	return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
}
btnLogout.onclick = function(){
	var ezeTapSuccessCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction successful");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));
	};
	var ezeTapFailureCallBack = function(response){
		$("#formData").hide();
		$("#messageDiv").show();
		$("#messageTag").text("Transaction failed");
		$("#messageDesc").text("Tap here to do another transaction.\n\n"+JSON.stringify(response));			
	};
	cordova.exec(ezeTapSuccessCallBack,ezeTapFailureCallBack,"EzeAPIPlugin","close",[]);
}