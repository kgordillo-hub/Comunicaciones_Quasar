package com.alianza.comunicaciones.services;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.Coordinates;

/**
 * Interface for localization service
 * @author ksgor
 *
 */
public interface ILocalizationService {

	/**
	 * Method to get the spaceship position
	 * 
	 * @param the array of distances to the satellite
	 * @param the matrix of initial positions (x,y) of the satellites
	 */
	public Coordinates getLocation(final double[] distancesToSatellite, final double[][] initialPositions)
			throws SystemException, BusinessException;
}
