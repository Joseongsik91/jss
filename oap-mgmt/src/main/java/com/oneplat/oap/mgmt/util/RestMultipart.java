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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * RestMultipart
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 6. 26. 오후 3:33:27
 * @version $Id: RestMultipart.java 10448 2012-09-07 08:11:52Z lenycer $
 */
public class RestMultipart {

	private static final int TIMEOUT = 60000;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HttpResponse sendMultipart(String url, Map<String, String> headers,
			  Map<String, String> formParam, List<MultipartFile> files, HttpServletRequest request) throws ConsoleException {
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient = this.wrapClient(url, httpClient);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
			HttpPost httpPost = new HttpPost(url);
			this.setHeader(httpPost, headers);
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName(HTTP.UTF_8));
			if (formParam != null) {
				for (Map.Entry<String, String> entry : formParam.entrySet()) {
					entity.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName(HTTP.UTF_8)));
				}
			}

//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			for(int i=0; i<files.size(); i++){
				String key = files.get(i).getName();
				ByteArrayBody body = new ByteArrayBody(files.get(i).getBytes(), "application/octet-stream", files.get(i).getOriginalFilename());
				entity.addPart(key, body);
			}
//			Iterator<String> fileNameIter = files.getFileNames();
//			while (fileNameIter.hasNext()) {
//				String key = (String) fileNameIter.next();
//				MultipartFile mfile = multipartRequest.getFile(key);
//				ByteArrayBody body = new ByteArrayBody(mfile.getBytes(), "application/octet-stream", mfile.getOriginalFilename());
//				entity.addPart(key, body);
//			}
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);

		} catch(ClientProtocolException e) {
			throw new ConsoleException(e);
		} catch(SocketTimeoutException e) {
			throw new ConsoleException(e);
		} catch(UnsupportedEncodingException e) {
			throw new ConsoleException(e);
		} catch(IOException e) {
			throw new ConsoleException(e);
		}
		return response;
	}
	
	/**
	 * setHeader
	 * 
	 * @param httpPost
	 * @param headers
	 */
	private void setHeader(HttpPost httpPost, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
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
