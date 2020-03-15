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

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * 
 * FakeX509HostnameVerifier
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 6. 28. 오후 6:01:51
 * @version $Id: FakeX509HostnameVerifier.java 4739 2012-06-28 09:15:35Z lenycer $
 */
public class FakeX509HostnameVerifier implements X509HostnameVerifier {

	@Override
	public boolean verify(String arg0, SSLSession arg1) {
		return true;
	}

	@Override
	public void verify(String arg0, SSLSocket arg1) throws IOException {
	}

	@Override
	public void verify(String arg0, X509Certificate arg1) throws SSLException {
	}

	@Override
	public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
	}
}
