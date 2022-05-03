package com.alianza.comunicaciones.services;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.ImperialShip;
import com.alianza.comunicaciones.models.Satellite;
import com.alianza.comunicaciones.models.SatelliteList;

/**
 * Interface of Communication service
 * 
 * @author ksgor
 *
 */
public interface ICommunicationService {

	/**
	 * Method to receive the satellite list and returns the space ship location and
	 * message
	 * 
	 * @param messages
	 * @return Imperial ship
	 * @throws SystemException,   if something goes wrong in the system while
	 *                            calculating position or reconstructing message
	 * @throws BusinessException, if there a violation of some rule of business
	 */
	public ImperialShip recieveMessage(final SatelliteList satelliteList) throws SystemException, BusinessException;

	/**
	 * Method to receive the individual satellite and returns the space ship
	 * location and message if there are previous messages received
	 * 
	 * @param messages
	 * @return Imperial ship
	 * @throws SystemException,   if something goes wrong in the system while
	 *                            calculating position or reconstructing message
	 * @throws BusinessException, if there a violation of some rule of business
	 */
	public ImperialShip recieveMessage(final Satellite satellite) throws SystemException, BusinessException;

}
