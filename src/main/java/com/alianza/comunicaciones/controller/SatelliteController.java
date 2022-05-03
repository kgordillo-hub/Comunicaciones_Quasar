package com.alianza.comunicaciones.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.ImperialShip;
import com.alianza.comunicaciones.models.SatelliteList;
import com.alianza.comunicaciones.services.ICommunicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to receive the application request and call the communication
 * service
 * 
 * @author ksgor
 *
 */
@RestController
@RequestMapping("${alliance.satellite.controller.path}")
@Validated
public class SatelliteController {

	/**
	 * Getting log error
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger("error-log");

	@Autowired
	private ICommunicationService communicationService;

	/**
	 * Method to handle the incoming request
	 * 
	 * @param satellites, in JSON format
	 * @return The representation in JSON format of the Imperial ship
	 * @throws ResponseStatusException, if something goes wrong
	 */
	@Operation(summary = "Reconstruct menssage and get spaceship's position, must provide all satellite's information in the request")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "If it was possible to reconstruct the message and get the spaceship's position", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ImperialShip.class)) }),
			@ApiResponse(responseCode = "404", description = "If there is not enough information to reconstruct the message or, if some business rule is violeted", content = @Content),
			@ApiResponse(responseCode = "500", description = "If something fails internally", content = @Content) })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ImperialShip getSecretMessage(@Valid @RequestBody SatelliteList satellites) {

		try {
			return communicationService.recieveMessage(satellites);

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
