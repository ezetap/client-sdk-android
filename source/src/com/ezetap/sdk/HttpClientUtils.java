package com.ezetap.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClientUtils {
	
	private static final int CONN_TIMEOUT = 30*1000; // 30 seconds
	private String sessionKey;
	
	public void process(String op, String url, JSONObject data, int requestCode, HttpResponseHandler handler) {
		Thread dispatcher = new Thread(new DataDispatcher(url, op, data, requestCode,handler));
		dispatcher.start();
	}
	
	public void processInSameThread(String op, String url, JSONObject data, int requestCode, HttpResponseHandler handler) throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException {
		DataDispatcher dispatcher = new DataDispatcher(url, op, data, requestCode,handler);
		dispatcher.dispatch();
	}
	
	public void post(String url, JSONObject data, int requestCode, HttpResponseHandler handler) {
		String op = "POST";
		Thread dispatcher = new Thread(new DataDispatcher(url, op, data, requestCode,handler));
		dispatcher.start();
	}
	
	public void get(String url, JSONObject data, int requestCode, HttpResponseHandler handler) {
		String op = "GET";
		Thread dispatcher = new Thread(new DataDispatcher(url, op, data, requestCode, handler));
		dispatcher.start();		
	}
	
	public void put(String url, JSONObject data, int requestCode, HttpResponseHandler handler) {
		String op = "PUT";
		Thread dispatcher = new Thread(new DataDispatcher(url, op, data, requestCode, handler));
		dispatcher.start();		
	}
	
	public void delete(String url, JSONObject data, int requestCode, HttpResponseHandler handler) {
		String op = "DELETE";
		Thread dispatcher = new Thread(new DataDispatcher(url, op, data, requestCode, handler));
		dispatcher.start();		
	}
	
	
	private class DataDispatcher implements Runnable{
		private String op;
		private String url;
		private JSONObject data;
		private int requestCode;
		private HttpResponseHandler handler;
		
		public DataDispatcher(String url, String op, JSONObject data, int requestCode, HttpResponseHandler handler) {
			this.op = op;
			this.url = url;
			this.data = data;
			this.handler = handler;
			this.requestCode = requestCode;
		}
		
		private void dispatch() throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException {
			DefaultHttpClient httpClient = getNewHttpClient();
			httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0,false));
			StringEntity se = null;
			if(data!=null)
				se = new StringEntity(data.toString());
			
			HttpResponse httpResponse = null;
			
			if(op.equals("POST")) {
				HttpPost httpPost = new HttpPost(this.url);
				if(se != null){
					httpPost.setEntity(se);
				}
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				if(sessionKey != null) {
					httpPost.setHeader("Cookie", "jsessionid="+ sessionKey);
				}
				
				httpResponse = httpClient.execute(httpPost);

			} else if(op.equals("GET")) {
				HttpGet httpGet = new HttpGet(this.url);
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-type", "application/json");
				if(sessionKey != null) {
					httpGet.setHeader("Cookie", "jsessionid="+ sessionKey);
				}
				httpResponse = httpClient.execute(httpGet);
			} else if(op.equals("PUT")) {
				HttpPut httpPut = new HttpPut(this.url);
				if(se != null){
					httpPut.setEntity(se);
					httpPut.setHeader("Content-type", "application/json");
				}
				httpPut.setHeader("Accept", "application/json");
				httpResponse = httpClient.execute(httpPut);
				
			} else if(op.equals("DELETE")) {
				HttpDelete httpDelete = new HttpDelete(this.url);
				httpDelete.setHeader("Accept", "application/json");
				//httpDelete.setHeader("Content-type", "application/json");
				httpResponse = httpClient.execute(httpDelete);
			} else if(op.equals(EzetapServiceBaseConstants.VAL_HTTP_PLAIN_GET)) { // non JSON
				HttpGet httpGet = new HttpGet(this.url);
				httpResponse = httpClient.execute(httpGet);
			} else {
				handler.handleError(new UnsupportedOperationException(), "Unsupported operation "+this.op, requestCode);
			}
			
			if(!(op.equals(EzetapServiceBaseConstants.VAL_HTTP_PLAIN_GET))) { // Do this for JSON response only
			  JSONObject response = null;
				if (httpResponse != null) {
					StatusLine status = httpResponse.getStatusLine();
					if (status.getStatusCode() == HttpStatus.SC_OK) {
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String strResponse = responseHandler.handleResponse(httpResponse);
						try {
							response = new JSONObject(strResponse);
						} catch(Exception e) {
							response = new JSONObject();
							response.put("response", strResponse);
						}
					} else if (status.getStatusCode() == HttpStatus.SC_NOT_MODIFIED){
						response = new JSONObject();
						response.put("success", true);
						if(httpResponse.getHeaders("ETag") != null)
							response.put("ETag", httpResponse.getHeaders("ETag"));
					} else{
						handler.handleError(null, "Error, could not reach server", requestCode);
					}
				}
			  handler.handleResponse(response, requestCode);
			} else {
			  handler.handleResponse(null, requestCode);
			}
		}

		@Override
		public void run() {
			if((!op.equals(EzetapServiceBaseConstants.VAL_GET) && !op.equals(EzetapServiceBaseConstants.VAL_DELETE)) && !op.equals(EzetapServiceBaseConstants.VAL_HTTP_PLAIN_GET) && data == null)
			{
				handler.handleError(new NullPointerException(),"JSON Data is null", requestCode);
			} else {
				try {
					dispatch();
				} catch (UnsupportedEncodingException e) {
					handler.handleError(e, "Error while attempting to encode input JSON data", requestCode);
				} catch (ClientProtocolException e) {
					handler.handleError(e, "Error while posting data", requestCode);
				} catch (IOException e) {
					handler.handleError(e, "Error while posting data", requestCode);
				} catch (JSONException e) {
					handler.handleError(e, "Error while parsing response data into JSON", requestCode);
				}
			}
			
		}
	}
	
	private DefaultHttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	        HttpConnectionParams.setSoTimeout(params, CONN_TIMEOUT);
	        HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
	        
	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	        DefaultHttpClient c = new DefaultHttpClient(ccm, params);
	        return c;
	        
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

}
