package com.oneplat.oap.mgmt.util;

public class UrlPrefixUtil {

	private static final String PREFIX = "api";
	
	public static String addPrefix(String url){
		if(url != null && !"".equals(url.trim())){
			return PREFIX + url;
		}
		return url;
	}
	
	public static String removePrefix(String url){
		if(url != null && !"".equals(url.trim()) && url.startsWith(PREFIX)){
			return url.substring(PREFIX.length());
		}
		return url;
	}
}
