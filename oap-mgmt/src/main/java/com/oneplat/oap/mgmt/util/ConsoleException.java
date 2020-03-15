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

/**
 * 
 * ConsoleException class
 *
 * @author Hyun-Seock Kim, Bluedigm
 * @date 2012. 7. 3. 오후 4:54:30
 * @version $Id: ConsoleException.java 5149 2012-07-03 09:14:09Z lenycer $
 */
public class ConsoleException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8218743589043426932L;
	
	public ConsoleException() {
		super();
	}
	
	public ConsoleException(Exception e) {
		super(e);
	}
	
	public ConsoleException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
