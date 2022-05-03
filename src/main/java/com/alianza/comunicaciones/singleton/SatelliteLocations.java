package com.alianza.comunicaciones.singleton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.utils.Constants;
import com.alianza.comunicaciones.utils.Utils;

/**
 * Singleton class to initialize and have a single instance of the satellites positions
 * @author ksgor
 *
 */
public class SatelliteLocations {

	/**
	 * Map to hold the satellites positions. Key: satellite's name. Value: satellite's position (x,y)
	 */
	private static Map<String, double[]> satellitePositions;

	/**
	 *  Prevent clients from using the constructor
	 */
	private SatelliteLocations() {
	}

	/**
	 * Method to get the unique instance of the map with the satellites positions
	 * @return the Map of satellites positions
	 * @throws SystemException if something goes wrong while creating the instance
	 */
	public static Map<String, double[]> getSatelliteLocations() throws SystemException {
		if (satellitePositions == null) {
			initMap();
		}
		return satellitePositions;
	}

	/**
	 * Method to initialize the map with the positions. It takes the information from 'satellite_config.properties'
	 * @throws SystemException if something goes wrong
	 */
	private static final void initMap() throws SystemException {
		satellitePositions = new HashMap<>();
		final String satelliteNames = Utils.readProperty(Constants.SATELLITES_CONFIG_PROPERTIES, "satellite.names");
		if (satelliteNames != null && !satelliteNames.isEmpty()) {
			final String[] satellites = satelliteNames.split(",");
			for (final String satelliteName : satellites) {
				final String[] coordinatesStr = Utils
						.readProperty(Constants.SATELLITES_CONFIG_PROPERTIES, "satellite." + satelliteName + ".position").split(",");
				final double[] coordinates = Arrays.stream(coordinatesStr).mapToDouble(Double::parseDouble).toArray();
				satellitePositions.put(satelliteName, coordinates);
			}
		}
	}

}
