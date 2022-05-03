package com.alianza.comunicaciones.services;

import java.util.List;

import com.alianza.comunicaciones.exceptions.BusinessException;

/**
 * Interface for Message service
 * 
 * @author ksgor
 *
 */
public interface IMessageRecieverService {

	/**
	 * Implementation for method declared on IMessageRecieverService
	 * 
	 * @param list of message broadcasted by the satellites
	 * @return the complete message if it was possible to determine it
	 * @throws BusinessException if it was not possible to reconstruct the message
	 */
	public String getMessage(List<String[]> messages) throws BusinessException;

}
