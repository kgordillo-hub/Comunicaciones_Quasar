package com.alianza.comunicaciones.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.singleton.SatelliteLocations;
import com.alianza.comunicaciones.utils.Constants;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Wrapper class which contains a list of Satellite objects
 * 
 * @author ksgor
 *
 */
public class SatelliteList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1065493993471489750L;
	/**
	 * List of satellites
	 */
	@Valid
	@NotNull
	private List<Satellite> satellites;

	/**
	 * Default constructor
	 */
	public SatelliteList() {
	}

	/**
	 * Extended constructor
	 */
	public SatelliteList(List<Satellite> satellites) {
		this.satellites = satellites;
	}

	/**
	 * Getter
	 * 
	 * @return list of satellites
	 */
	public List<Satellite> getSatellites() {
		return satellites;
	}

	/**
	 * Setter
	 * 
	 * @param satellites
	 */
	public void setSatellites(List<Satellite> satellites) {
		this.satellites = satellites;
	}

	/**
	 * Method to get a list of all distances from the spaceship to the satellites
	 * 
	 * @return an array of doubles with the distances from the spaceship to the
	 *         satellites
	 */
	@Hidden
	public double[] getDistanceToSatellites() {
		double satellitePos[] = new double[satellites.size()];
		for (int i = 0; i < satellites.size(); i++) {
			satellitePos[i] = satellites.get(i).getDistance();
		}
		return satellitePos;
	}

	/**
	 * Gets the initial positions from the Satellites based on the Singleton
	 * configuration
	 * 
	 * @return matrix with the positions of the satellites
	 * @throws SystemException   if something goes wrong while getting the positions
	 * @throws BusinessException if some business rule is violated
	 */
	@Hidden
	public double[][] getInitialPositions() throws SystemException, BusinessException {
		final Map<String, double[]> satellitePositions = SatelliteLocations.getSatelliteLocations();
		final double[][] initialPositions = new double[satellitePositions.size()][];
		for (int i = 0; i < satellites.size(); i++) {
			final String satelliteName = satellites.get(i).getName();
			if (satelliteName != null) {
				initialPositions[i] = satellitePositions.get(satelliteName);
				if (initialPositions[i] == null) {
					throw new BusinessException(Constants.SATELLITE_NOT_FOUND.replace("{satellite}", satelliteName));
				}
			} else {
				throw new BusinessException(Constants.SATELLITE_NULL);
			}
		}
		return initialPositions;
	}

	/**
	 * Method to get the list of all messages arrays
	 * 
	 * @return list of all messages
	 */
	@Hidden
	public List<String[]> getMessageList() throws BusinessException {
		final List<String[]> messageList = new ArrayList<>();
		for (final Satellite satellite : satellites) {
				messageList.add(satellite.getMessage());
		}
		return messageList;
	}
	
}
