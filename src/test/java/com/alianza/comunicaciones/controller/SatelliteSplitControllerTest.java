package com.alianza.comunicaciones.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.alianza.comunicaciones.models.Satellite;
import com.alianza.comunicaciones.utils.Constants;

@SpringBootTest
@AutoConfigureMockMvc
class SatelliteSplitControllerTest {

	@Autowired
	SatelliteSplitController satelliteSplitController;

	@InjectMocks
	private Satellite satellite;

	private String satelliteName;

	@BeforeEach
	void setUp() throws Exception {
		satellite.setDistance(125.50);
		satellite.setMessage(new String[] { "pablito", "", "", "clavito" });
		satelliteName = "sato";
	}

	@DisplayName("Testing regular flow, using POST per satellite. Will trhough a ResponseStatusException if not enough information recieved to other satellites")
	@Test
	void testRecieveMessageFromSingleSatellitePost() {
		try {
			assertNotNull(satelliteSplitController.getSecretMessagePost(satellite, satelliteName));
		} catch (final ResponseStatusException e) {
			assertEquals(Constants.LACK_INFO_MESSAGES, e.getReason());
		}
	}

	@DisplayName("Will through an exception if satellite is not in the config through POST")
	@Test
	void testSatelliteNameNotValid() {
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteSplitController.getSecretMessagePost(satellite, "SuperSatellite123"));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.SATELLITE_NOT_FOUND.replace("{satellite}", "SuperSatellite123"),
				res.getReason());

	}

	@DisplayName("Will through an exception if satellite is not in the config through POST")
	@Test
	void testNullSatelliteName() {
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteSplitController.getSecretMessagePost(satellite, null));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.SATELLITE_NOT_FOUND.replace("{satellite}", "null"), res.getReason());

	}
	
	@DisplayName("Setting name to the satellite in the POST request. It should through a ResponseStatusException, as the request should not caontain any name")
	@Test
	void testNullSatelliteNameInRequest() {
		satellite.setName("superS123");
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteSplitController.getSecretMessagePost(satellite, "kenobi"));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.BAD_SPLIT_SATELLITE_REQUEST.replace("{satellite}", "kenobi"), res.getReason());

	}

	@DisplayName("Testing regular flow, using GET per satellite. Will trhough a ResponseStatusException if not enough information recieved to other satellites")
	@Test
	void testRecieveMessageFromSingleSatelliteGetNull(){
		try {
			assertNotNull(satelliteSplitController.getSecretMessageGet("kenobi", 125.00, ""));
		} catch (final ResponseStatusException e) {
			assertEquals(Constants.INCORRECT_MESSAGE_FORMAT_GET, e.getReason());
		}
	}
	
	@DisplayName("Testing regular flow, using GET per satellite. Will trhough a ResponseStatusException if not enough information recieved to other satellites")
	@Test
	void testRecieveMessageFromSingleSatelliteGet() {
		try {
			assertNotNull(satelliteSplitController.getSecretMessageGet("kenobi", 125.00, ",clavo,,clavito"));
		} catch (final ResponseStatusException e) {
			assertEquals(Constants.LACK_INFO_MESSAGES, e.getReason());
		}

		try {
			assertNotNull(satelliteSplitController.getSecretMessageGet("sato", 105.00, "pablito,clavo,,clavito"));
		} catch (final ResponseStatusException e) {
			assertEquals(Constants.LACK_INFO_MESSAGES, e.getReason());
		}

		try {
			Assertions.assertEquals(
					satelliteSplitController.getSecretMessageGet("skywalker", 145.00, ",,un,").getMessage(),
					"pablito clavo un clavito");
		} catch (final ResponseStatusException e) {
			assertEquals(Constants.LACK_INFO_MESSAGES, e.getReason());
		}
	}

	
}
