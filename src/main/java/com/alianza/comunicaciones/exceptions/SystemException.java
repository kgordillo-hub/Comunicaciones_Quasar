package com.alianza.comunicaciones.exceptions;
/**
 * Custom exception to handle the system failures
 * @author ksgor
 *
 */
public class SystemException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4250774029583332415L;
	
	public SystemException(final String errorMessage){
		super(errorMessage);
	}
	
	public SystemException(final String errorMessage, Throwable cause){
		super(errorMessage, cause);
	}
}
