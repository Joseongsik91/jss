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

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * TrustedRestTemplate SSL self trusted
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 6. 27. 오후 5:48:47
 * @version $Id: TrustedRestTemplate.java 12755 2012-11-02 08:33:23Z lenycer $
 */
public class TrustedRestTemplate extends RestTemplate {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		if(url.toLowerCase().startsWith("https")) {
			try {
				SSLContext ctx = SSLContext.getInstance("TLS");
				X509TrustManager tm = new FakeX509TrustManager();
				ctx.init(null, new TrustManager[] { tm }, null);
				SSLContext.setDefault(ctx);
			} 
			catch (NoSuchAlgorithmException e) {
				log.error(e.toString()); 
			}
			catch (KeyManagementException e) {
				log.error(e.toString()); 
			}
		}
		return super.exchange(url, method, requestEntity, responseType, uriVariables);
	}
	
	/**
	 * exchangeForConsole for ConsoleException
	 * 
	 * @param url
	 * @param method
	 * @param requestEntity
	 * @param responseType
	 * @param uriVariables
	 * @return
	 * @throws ConsoleException
	 */
	public <T> ResponseEntity<T> exchangeForConsole(URI url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Map<String, String> urlVariables) throws ConsoleException {
		ResponseEntity<T> responseEntity = null;
		
		try {
			responseEntity = exchange(url, method, requestEntity, responseType);
		} catch (RestClientException e) {
			throw new ConsoleException(e);
		} catch (IllegalArgumentException e) {
			throw new ConsoleException(e);
		}
		return responseEntity;
	}
}
