package com.alianza.comunicaciones.exceptions;

/**
 * Custom exception to handle the violation to business rules
 * @author ksgor
 *
 */
public class BusinessException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2219131656577701646L;
	
	public BusinessException(final String errorMessage) {
		super(errorMessage);
	}
}
