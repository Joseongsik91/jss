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

import com.oneplat.oap.mgmt.common.model.ConsoleConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 
 * RestResponseErrorHandler
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 6. 26. 오후 3:33:09
 * @version $Id: RestResponseErrorHandler.java 9019 2012-08-22 08:18:36Z lenycer $
 */
public class RestResponseErrorHandler implements ResponseErrorHandler {

	/** the response error message */
	private String responseMessage;

	/** the response error flag */
	private boolean isRespnoseError = false;
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return hasError(response.getStatusCode());
	}

	/**
	 * error 여부 
	 * @param statusCode
	 * @return 
	 */
	protected boolean hasError(HttpStatus statusCode) {
		responseMessage = statusCode + ConsoleConstant.EMPTY_SPACE + statusCode.getReasonPhrase();
		return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR || statusCode.series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		responseMessage = statusCode + ConsoleConstant.EMPTY_SPACE + response.getStatusText();
		isRespnoseError = true;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @return the isRespnoseError
	 */
	public boolean isRespnoseError() {
		return isRespnoseError;
	}

}
