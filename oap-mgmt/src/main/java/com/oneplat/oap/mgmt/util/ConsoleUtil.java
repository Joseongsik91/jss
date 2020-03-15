/*
 * Copyright (c) 2011 SK Telcom.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK Telcom.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK Telcom.
 */
package com.oneplat.oap.mgmt.util;

import com.oneplat.oap.mgmt.common.model.ConsoleConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * ConsoleUtil for Exception handling
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 7. 3. 오후 5:40:19
 * @version $Id: ConsoleUtil.java 13238 2012-11-15 03:47:20Z lenycer $
 */
@Component
public final class ConsoleUtil {

	protected final static Logger log = LoggerFactory.getLogger(ConsoleUtil.class);
	
	private final static String HTTPS = "https";
	/**
	 * private construct
	 *
	 */
	private ConsoleUtil() {
		
	}
	
	/**
	 * replaced url (queryString이 key/value가 일치하지 않을 수 있다고 해서 미리 replace함)
	 * 
	 * @param url
	 * @param queryString
	 * @param pathParam
	 * @param encodingType
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI getReplacedUrl(String url, Map<String, String> queryString, Map<String, String> pathParam,
			String encodingType) throws ConsoleException, URISyntaxException {
		String[] urls = StringUtils.split(url, "?");
		String convertUrl = (urls != null ? urls[0] : "");
		try {
			for (Entry<String, String> entry : pathParam.entrySet()) {
				String value = entry.getValue();
				if (!"".equals(StringUtil.null2string(encodingType, ""))) {
					value = URLEncoder.encode(value, encodingType);
				}
				convertUrl = StringUtils.replace(convertUrl, "{" + entry.getKey() + "}", value);
			}
			StringBuffer query = new StringBuffer();
			for (Entry<String, String> entry : queryString.entrySet()) {
				String value = entry.getValue();
				if (!"".equals(StringUtil.null2string(encodingType, ""))) {
					value = URLEncoder.encode(value, encodingType);
				}
				if (query.length() > 0) {
					query.append('&');
				}
				query.append(entry.getKey()).append('=').append(value);
			}
			if (query.length() > 0) {
				convertUrl += '?' + query.toString();
			}
		} catch(UnsupportedEncodingException e) {
			throw new ConsoleException(e);
		}

		URI returnUrl = new URI(convertUrl);
		return returnUrl;
	}
	
	/**
	 * 
	 * @param url
	 * @param pathParam
	 * @return
	 * @throws ConsoleException
	 * @throws URISyntaxException
	 */
	public static String getReplacedHttpClientUrl(String url, Map<String, String> pathParam) throws ConsoleException, URISyntaxException {
		String[] urls = StringUtils.split(url, "?");
		String convertUrl = (urls != null ? urls[0] : "");
		for (Entry<String, String> entry : pathParam.entrySet()) {
			String value = entry.getValue();
			convertUrl = StringUtils.replace(convertUrl, "{" + entry.getKey() + "}", value);
		}

		return convertUrl;
	}
	
	/**
	 * 
	 * @param queryString
	 * @param pathParam
	 * @param encodingType
	 * @return
	 * @throws ConsoleException
	 * @throws URISyntaxException
	 */
	public static String getReplacedMultipartUrl(String url, Map<String, String> queryString, Map<String, String> pathParam,
			String encodingType) throws ConsoleException, URISyntaxException {
		String[] urls = StringUtils.split(url, "?");
		String convertUrl = (urls != null ? urls[0] : "");
		try {
			for (Entry<String, String> entry : pathParam.entrySet()) {
				String value = entry.getValue();
				if (!"".equals(StringUtil.null2string(encodingType, ""))) {
					value = URLEncoder.encode(value, encodingType);
				}
				convertUrl = StringUtils.replace(convertUrl, "{" + entry.getKey() + "}", value);
			}
			StringBuffer query = new StringBuffer();
			for (Entry<String, String> entry : queryString.entrySet()) {
				String value = entry.getValue();
				if (!"".equals(StringUtil.null2string(encodingType, ""))) {
					value = URLEncoder.encode(value, encodingType);
				}
				if (query.length() > 0) {
					query.append('&');
				}
				query.append(entry.getKey()).append('=').append(value);
			}
			if (query.length() > 0) {
				convertUrl += '?' + query.toString();
			}
		} catch(UnsupportedEncodingException e) {
			throw new ConsoleException(e);
		}

		return convertUrl;
	}

	/**
	 * formatted data
	 * 
	 * @param response
	 * @param type
	 * @return
	 * @throws ConsoleException
	 */
	public static String getEntity(HttpResponse response, String type) {
		String entity = "";
		try {
			entity = EntityUtils.toString(response.getEntity());
			if (entity != null && !"".equals(entity)) {
				entity = getFormattedString(entity, type);
			}
		} catch(IOException e) {
			log.error("IOException : " + e.getMessage());
		}
		return entity;
	}
	
	/**
	 * formatted data
	 * 
	 * @param data
	 * @param type
	 * @return
	 */
	public static String getFormattedString(String data, String type) {
		String formattedString = data;
		try {
			if(ConsoleConstant.JSON.equals(type)) {
				formattedString = FormatUtil.getPrettyJson(data);
			} else if(ConsoleConstant.XML.equals(type)) {
				formattedString = FormatUtil.getPrettyXml(data);
			} else {
				formattedString = data;
			}

		} catch (TransformerException e) {
			log.error("TransformerException : " + e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error("ParserConfigurationException : " + e.getMessage());
		} catch (SAXException e) {
			log.error("SAXException : " + e.getMessage());
		} catch (IOException e) {
			log.error("IOException : " + e.getMessage());
		}
		return formattedString;
	}
	
	/**
	 * get error handler log
	 * 
	 * @param errorHandler
	 * @return
	 */
	public static String getErrorHandlerLog(RestResponseErrorHandler errorHandler, HttpServletRequest request) {
		StringBuffer resultData = new StringBuffer();
		if (errorHandler.isRespnoseError() || "ERROR_HANDLER".equals(request.getParameter("JUNIT_EXCEPTION"))) {
			/** rest template error 발생 시 */
			resultData.append(ConsoleConstant.NEW_LINE).append(errorHandler.getResponseMessage());
		}
		return resultData.toString();
	}
	
	/**
	 * getRestTemplateInstance (for fake SSL, verify)
	 * @param timeout
	 * @return
	 */
	public static TrustedRestTemplate getRestTemplateInstance(String url, int timeout) {
		TrustedRestTemplate rc = new TrustedRestTemplate();
//		DevCenterSimpleClientHttpRequestFactory requestFactory = new DevCenterSimpleClientHttpRequestFactory(
//				new NullHostnameVerifier());
		HttpClient httpClient = new DefaultHttpClient();
		if (StringUtil.null2string(url, "").toLowerCase().startsWith(HTTPS)) {
			WrapClient wrap = new WrapClient();
			httpClient = wrap.wrapClient(httpClient);
		}
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectTimeout(timeout);
		requestFactory.setReadTimeout(timeout);
		rc.setRequestFactory(requestFactory);
		return rc;
	}
	

    /**
     * Create HTTP Request Base
     * 
     * @param uri URI
     * @param httpMethod HTTP Method
     * @return HttpRequestBase
     */
    public static HttpRequestBase createHttpRequestBase(String uri, String httpMethod) {
    	HttpMethod method = HttpMethod.valueOf(httpMethod);
    	
    	if (method == HttpMethod.POST) {
    		return new HttpPost(uri);
    	} else if (method == HttpMethod.PUT) {
    		return new HttpPut(uri);
    	} else if (method == HttpMethod.DELETE) {
    		return new HttpDelete(uri);
    	} else if (method == HttpMethod.GET) {
    		return new HttpGet(uri);
    	} else if (method == HttpMethod.HEAD) {
    		return new HttpHead(uri);
    	} else {
    		throw new IllegalArgumentException("Not Supported HTTP Method: " + method);
    	}
    }
	
    /**
 	 * 입력으로 들어온 url과 encodeType 타입으로 Map 인코딩 해서 URI 생성 
 	 * 
 	 * @param url URL
 	 * @param map Map
 	 * @param encodeType Encoding Type
 	 * @return URI
 	 */
 	public static String createURI(String url, Map<String, String> map, String encodeType) {
 		String uri = url;
 		
 		if (map != null && map.size() > 0) {
 			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
 			Set<Entry<String,String>> entrySet = map.entrySet();
 			
 			for (Entry<String,String> entry : entrySet) {
 				qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
 			}
 			
 			String encodedQueryString = URLEncodedUtils.format(qparams, encodeType);
 			uri = url + "?" + encodedQueryString;
 		}
 		
 		return uri;
 	}
 
 	

 	 /**
 	 * 입력으로 들어온 url과 encodeType 타입으로 Map 인코딩 해서 URI 생성 
 	 * 
 	 * @param url URL
 	 * @param requestParameters Map
 	 * @return URI
 	 */
 	public static String createURIForNoEncoded(String url, Map<String, String> requestParameters) {
 		String uri = url;
 		if (requestParameters != null && requestParameters.size() > 0) {
 			Set<Entry<String,String>> entrySet = requestParameters.entrySet();
 			StringBuffer sb = new StringBuffer();
 			for (Entry<String,String> entry : entrySet) {
 				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
 			}
 			if (sb.length() > 0) {
 				sb.setLength(sb.length()-1);
 			}
 			uri = url + "?" + sb.toString();
 		}
 		return uri;
 	}

 	/*
	String uri = createURI(url, requestParameters, encodingType);
	String uri = createURIForEncoded(url, requestParameters, encodingType);
 	 */
 	
 	
	public HttpResponse executeProtocolHandler(String uri, Map<String,String> headerParameters, String requestMethod, String mimeType,  String inputPayload, String encodingType) {

		HttpClient httpClient = new DefaultHttpClient();
		httpClient = this.wrapClient(uri,httpClient);
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
		
		HttpRequestBase httpRequestBase = createHttpRequestBase(uri, requestMethod);
		setHeaderParamInfo(httpRequestBase, headerParameters);
		HttpMethod httpMethod = HttpMethod.valueOf(requestMethod);
		if (httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.POST) {
			putPostOrPutEntityInfo((HttpEntityEnclosingRequestBase) httpRequestBase, mimeType,  inputPayload,  encodingType);
		}
		
		//--최종 호출 직전에 값을 확인---------------------------------------------
		HttpResponse httpResponse = null;
		try {
	    	httpResponse = httpClient.execute(httpRequestBase);
		} catch(Exception e1) {
			log.error("console error: ", e1.getLocalizedMessage());
	    } finally {
			// Dispose Request
			if (httpRequestBase != null) {
				httpRequestBase.releaseConnection();
			}
	    }
		return httpResponse;
	}
	
	/*
	 * Set Parameter Information
	 */
	private void setHeaderParamInfo(HttpRequestBase httpRequestBase, Map<String, String> headerParameters) {
		
		//header에 추가적인 정보 세팅
		if (headerParameters == null) {
			return;
		}
		
		Iterator<String> iterator = headerParameters.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			String value = headerParameters.get(key);
			httpRequestBase.addHeader(key, value);			
		}
	}
	
	/**
	 * Put PostOrPut Entity Info
	 * 
	 * @param httpRequestBase HttpEntityEnclosingRequestBase
	 */
	protected void putPostOrPutEntityInfo(HttpEntityEnclosingRequestBase httpRequestBase,String mimeType, String inputPayload, String encodingType) {
		if (inputPayload != null && inputPayload.length()>0) {
			ContentType contentType = ContentType.create(mimeType, encodingType);
			StringEntity entity = new StringEntity(inputPayload, contentType);
			entity.setChunked(true);
			httpRequestBase.setEntity(entity);
		}
	}
	
	private static final int TIMEOUT = 60000;

	
	/**
	 * wrap httpClient
	 * 
	 * @param url
	 * @param httpClient
	 * @return
	 */
	private HttpClient wrapClient(String url, HttpClient httpClient) {
		HttpClient wrapHttpClient = httpClient;
		if (StringUtil.null2string(url, "").toLowerCase().startsWith("https")) {
			WrapClient wrap = new WrapClient();
			wrapHttpClient = wrap.wrapClient(httpClient);
		}
		return wrapHttpClient;
	}
}
