/**
 * 
 */
package com.ezetap.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vivek
 *
 */
public class EzetapURL {

	private static final String DEMO_BASE_URL = "http://d.eze.cc";
	private static final String PRE_PROD_BASE_URL = "http://pp.eze.cc";
	private static final String PROD_BASE_URL = "https://www.ezetap.com";
	private static final String OTHER_BASE_URL = "http://d.eze.cc";
	
	private static final String DEMO_API_SUFFIX = "api";
	private static final String PRE_PROD_API_SUFFIX = "api";
	private static final String PROD_API_SUFFIX = "api";
	private static final String OTHER_API_SUFFIX = "api";
	
	private static Map<String, String> apis = new HashMap<String, String>();
	private static Map<String, String> urls = new HashMap<String, String>();
	
	static {
		apis.put("fetchDetails", "1.0/customer/details");
		apis.put("fetchCatalog", "1.0/catalog/list");
		
		urls.put("DEMO", DEMO_BASE_URL+"/"+DEMO_API_SUFFIX+"/");
		urls.put("PRE_PROD", PRE_PROD_BASE_URL+"/"+PRE_PROD_API_SUFFIX+"/");
		urls.put("PROD", PROD_BASE_URL+"/"+PROD_API_SUFFIX+"/");
		urls.put("OTHER", OTHER_BASE_URL+"/"+OTHER_API_SUFFIX+"/");
		urls.put("MOCK", DEMO_BASE_URL+"/"+DEMO_API_SUFFIX+"/");
	}
	
	public static String url(String mode, String apiName) {
		return urls.get(mode.toUpperCase()) + apis.get(apiName);
	}
}
