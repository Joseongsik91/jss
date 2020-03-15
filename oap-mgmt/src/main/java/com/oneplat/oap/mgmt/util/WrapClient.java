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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * WrapClient for Https
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 6. 28. 오후 5:07:07
 * @version $Id: WrapClient.java 11766 2012-10-10 04:16:38Z lenycer $
 */
public class WrapClient {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String HTTPS = "https";
	
	/**
	 * wrap httpclient
	 * 
	 * @param base
	 * @return
	 */
	public HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new FakeX509TrustManager();
			X509HostnameVerifier verifier = new FakeX509HostnameVerifier();
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(verifier);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme(HTTPS, ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} 
		catch (NoSuchAlgorithmException e) {
			log.error(e.toString()); 
			return null;
		}
		catch (KeyManagementException e) {
			log.error(e.toString()); 
			return null;
		}
	}
}
