package com.alianza.comunicaciones.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.Coordinates;
import com.alianza.comunicaciones.models.ImperialShip;
import com.alianza.comunicaciones.models.Satellite;
import com.alianza.comunicaciones.models.SatelliteList;
import com.alianza.comunicaciones.services.ICommunicationService;
import com.alianza.comunicaciones.services.ILocalizationService;
import com.alianza.comunicaciones.services.IMessageRecieverService;
import com.alianza.comunicaciones.singleton.SatelliteLocations;
import com.alianza.comunicaciones.utils.Constants;
import com.alianza.comunicaciones.utils.Utils;

/**
 * Implementation of ICommunicationService. Service to call message service and
 * localization service
 * 
 * @author ksgor
 *
 */
@Service
public class CommunicationServiceImp implements ICommunicationService {

	@Autowired
	ILocalizationService localizationService;

	@Autowired
	IMessageRecieverService messageService;

	/**
	 * Method to receive service request and get message and ship's position (if
	 * possible)
	 * 
	 * @throws SystemException,   if something goes wrong in the system while
	 *                            calculating position or reconstructing message
	 * @throws BusinessException, if there a violation of some rule of business
	 */
	@Override
	public ImperialShip recieveMessage(final SatelliteList satelliteList) throws SystemException, BusinessException {
		final Coordinates spaceShipLocation = localizationService.getLocation(satelliteList.getDistanceToSatellites(),
				satelliteList.getInitialPositions());
		final String spaceMessage = messageService.getMessage(satelliteList.getMessageList());
		final ImperialShip impShip = new ImperialShip(spaceShipLocation, spaceMessage);

		return impShip;
	}

	/**
	 * Method to receive service request call to a single satellite and get message
	 * and ship's position (if possible)
	 * 
	 * @throws SystemException,   if something goes wrong in the system while
	 *                            calculating position or reconstructing message
	 * @throws BusinessException, if there a violation of some rule of business
	 */
	@Override
	public ImperialShip recieveMessage(Satellite satellite) throws SystemException, BusinessException {
		final String fileName = Constants.DATA_FILE_FORMAT.replace("{satellite_name}", satellite.getName())
				.replace("{serial}", System.nanoTime() + "");

		Utils.saveObjectInFile(satellite, Constants.MESSAGE_OBJECTS_PATH, fileName);
		return recieveMessage(getListOfSatellites());
	}

	/**
	 * Method to get the list satellites based on the previous messages received by
	 * the service
	 * 
	 * @return The list of satellites
	 * @throws SystemException,   if is not possible to read or write the messages
	 *                            in disc
	 * @throws BusinessException, if a business rule is violated
	 */
	private SatelliteList getListOfSatellites() throws SystemException, BusinessException {
		final Set<String> satellitesNames = SatelliteLocations.getSatelliteLocations().keySet();
		final List<Satellite> satelliteList = new ArrayList<>();
		final List<String> listMsgFilesName = new ArrayList<>();
		for (final String satelliteName : satellitesNames) {
			listMsgFilesName.add(getMessageFileName(satelliteName, Utils.getSortedFiles("./Data_objects")));
		}
		for (final String msgFileName : listMsgFilesName) {
			satelliteList.add((Satellite) Utils.getObjectFromFile(Constants.MESSAGE_OBJECTS_PATH, msgFileName));
		}
		Utils.moveFiles(Constants.MESSAGE_OBJECTS_PATH, Constants.MESSAGE_PROCESSED_OBJECTS_PATH, listMsgFilesName);
		return new SatelliteList(satelliteList);
	}

	/**
	 * Method to get the previous satellite object (previous message) of the given
	 * satellite name. It takes the older message (object) first.
	 * 
	 * @param satelliteName
	 * @param dataObjects,  list of files (previous messages) in a given directory
	 * @return The older file name that matches with the satellite name
	 * @throws SystemException,   if is not possible to read the object
	 * @throws BusinessException, if no file was found in the directory starting
	 *                            with the satellite's name
	 */
	private final String getMessageFileName(final String satelliteName, final File[] dataObjects)
			throws SystemException, BusinessException {
		if (dataObjects != null && dataObjects.length > 0) {
			for (final File dataObject : dataObjects) {
				if (dataObject.getName().startsWith(satelliteName)) {
					return dataObject.getName();
				}
			}
		}
		throw new BusinessException(Constants.LACK_INFO_MESSAGES);
	}

}
