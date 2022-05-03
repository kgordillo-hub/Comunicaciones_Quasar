package com.alianza.comunicaciones.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.ImperialShip;
import com.alianza.comunicaciones.models.Satellite;
import com.alianza.comunicaciones.services.ICommunicationService;
import com.alianza.comunicaciones.singleton.SatelliteLocations;
import com.alianza.comunicaciones.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to receive the splitted request and call the communication service
 * 
 * @author ksgor
 *
 */
@RestController
@RequestMapping
@Validated
public class SatelliteSplitController {

	/**
	 * Getting log error
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger("error-log");

	@Autowired
	private ICommunicationService communicationService;

	/**
	 * Method to handle the incoming request per satellite
	 * 
	 * @param Single    satellite in JSON format
	 * @param satellite name in path
	 * @return The representation in JSON format of the Imperial ship
	 * @throws ResponseStatusException, if something goes wrong
	 */
	@Operation(summary = "Reconstruct menssage and get spaceship's position using the requests sent to each satellite")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "If it was possible to reconstruct the message and get the spaceship's position", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ImperialShip.class)) }),
			@ApiResponse(responseCode = "404", description = "If there is not enough information to reconstruct the message or, if some business rule is violeted", content = @Content),
			@ApiResponse(responseCode = "500", description = "If something fails internally", content = @Content) })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, value = "${alliance.satellite.split.controller.path}/{satelliteName}")
	public ImperialShip getSecretMessagePost(@Valid @RequestBody Satellite satellite,
			@PathVariable String satelliteName) {
		return getSpaceShipMessage(satellite, satelliteName);
	}

	/**
	 * Method to handle the incoming GET request per satellite
	 * 
	 * @param satellite name in path
	 * @param distance  from spaceship as double
	 * @param message   - comma separated
	 * @return The representation in JSON format of the Imperial ship
	 * @throws ResponseStatusException, if something goes wrong
	 */
	@Operation(summary = "Reconstruct menssage and get spaceship's position using the requests sent to each satellite. Received the information through URL parameters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "If it was possible to reconstruct the message and get the spaceship's position", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ImperialShip.class)) }),
			@ApiResponse(responseCode = "404", description = "If there is not enough information to reconstruct the message or, if some business rule is violeted", content = @Content),
			@ApiResponse(responseCode = "500", description = "If something fails internally", content = @Content) })
	@GetMapping(produces = {
			MediaType.APPLICATION_JSON_VALUE }, value = "${alliance.satellite.split.controller.path}/{satelliteName}")
	public ImperialShip getSecretMessageGet(@PathVariable String satelliteName,
			@RequestParam(required = true) Double distance, @RequestParam(required = true) String message) {
		final String[] words = message.split(Constants.MESSAGE_SPLITTER, -1);
		if (words != null && words.length > 1) {
			final Satellite satellite = new Satellite(distance, words);
			return getSpaceShipMessage(satellite, satelliteName);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.INCORRECT_MESSAGE_FORMAT_GET);
		}
	}

	/**
	 * Method that calls the services to reconstruct the message and get the
	 * spaceship's position
	 * 
	 * @param satellite
	 * @param satelliteName
	 * @return Imperial ship object with the message and coordinates
	 */
	private ImperialShip getSpaceShipMessage(Satellite satellite, String satelliteName) {
		try {
			if (satelliteName != null && SatelliteLocations.getSatelliteLocations().get(satelliteName) != null) {
				if (satellite.getName() == null) {
					satellite.setName(satelliteName);
					return communicationService.recieveMessage(satellite);
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND,
							Constants.BAD_SPLIT_SATELLITE_REQUEST.replace("{satellite}", satelliteName));
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.SATELLITE_NOT_FOUND
						.replace("{satellite}", satelliteName != null ? satelliteName : "null"));
			}
		} catch (final SystemException se) {
			LOGGER.error(se.getMessage(), se);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"System error, please contact the administrator");
		} catch (final BusinessException be) {
			LOGGER.info(be.getMessage(), be);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, be.getMessage());
		}
	}
}
