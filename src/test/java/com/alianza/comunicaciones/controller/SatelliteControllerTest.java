package com.alianza.comunicaciones.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.models.Satellite;
import com.alianza.comunicaciones.models.SatelliteList;
import com.alianza.comunicaciones.utils.Constants;

@SpringBootTest
class SatelliteControllerTest {

	@Autowired
	SatelliteController satelliteController;

	@InjectMocks
	private SatelliteList satelliteList;

	private Satellite s1;
	private Satellite s2;
	private Satellite s3;

	@BeforeEach
	void setUp() {
		final List<Satellite> satellites = new ArrayList<>();
		s1 = new Satellite(142.7, new String[] { "este", "", "un", "", "" });
		s2 = new Satellite(115.5, new String[] { "", "es", "", "", "" });
		s3 = new Satellite(100.0, new String[] { "este", "", "", "mensaje", "secreto" });

		s1.setName("sato");
		s2.setName("skywalker");
		s3.setName("kenobi");

		satellites.add(s1);
		satellites.add(s2);
		satellites.add(s3);

		satelliteList.setSatellites(satellites);

	}

	@DisplayName("Testing regular flow, happy path for satellite list")
	@Test
	void testRecieveMessageSatelliteListNotNull() {
		try {
			assertNotNull(satelliteController.getSecretMessage(satelliteList));
		} catch (final ResponseStatusException e) {
			e.printStackTrace();
		}
	}

	@DisplayName("Should throws a ResponseStatusException if satellite name is not in the configuration")
	@Test
	void testBusinessExceptionInvalidSatellite() {
		s1.setName("Satellite123");
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteController.getSecretMessage(satelliteList));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.SATELLITE_NOT_FOUND.replace("{satellite}", "Satellite123"), res.getReason());
	}

	@DisplayName("Should throws a ResponseStatusException if all satellites have empty word in the same position of their messages")
	@Test
	void testBusinessExceptionInvalidEmptyMessages() {
		s1.setName("sato");
		s1.getMessage()[2] = "";
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteController.getSecretMessage(satelliteList));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.MESSAGE_EMPTY_WORD_ERROR.replace("{pos}", "3"), res.getReason());
	}

	
	@DisplayName("Should throws a ResponseStatusException if at least two satellites have a different word in the same position of their messages")
	@Test
	void testBusinessExceptionInvalidDifferentMessages() {
		s1.setName("sato");
		s1.getMessage()[0] = "hello";
		s1.getMessage()[2] = "un";
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteController.getSecretMessage(satelliteList));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.MESSAGE_POSITION_ERROR.replace("{pos}", "1"), res.getReason());
	}
	
	@DisplayName("Should throws a ResponseStatusException if at least two satellites have a different word in the same position of their messages")
	@Test
	void testBusinessExceptionInvalidSizes() {
		final List<Satellite> new_satellites = new ArrayList<>();
		s1 = new Satellite(142.7, new String[] { "este", "", "un", "", "", "more", "words" });
		s2 = new Satellite(115.5, new String[] { "", "es", "", "", "" });
		s3 = new Satellite(100.0, new String[] { "este", "", "", "mensaje", "secreto" });

		s1.setName("sato");
		s2.setName("skywalker");
		s3.setName("kenobi");

		new_satellites.add(s1);
		new_satellites.add(s2);
		new_satellites.add(s3);
		
		satelliteList.setSatellites(new_satellites);
		ResponseStatusException res = Assertions.assertThrows(ResponseStatusException.class,
				() -> satelliteController.getSecretMessage(satelliteList));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
		Assertions.assertEquals(Constants.MESSAGE_LENGTH_ERROR, res.getReason());
	}
	
	@DisplayName("If required values are null should throws a  ConstraintViolationException or BusinessException")
	@Test
	void testBusinessExceptionNullDistances() {
		s1.setName("sato");
		s1.getMessage()[2] = "un";
		s1.setDistance(null);
		Assertions.assertThrows(ConstraintViolationException.class,
				() -> satelliteController.getSecretMessage(satelliteList));

		s1.setDistance(142.7);
		String[] messageTmp = s1.getMessage();
		messageTmp[0] = null;

		BusinessException be = Assertions.assertThrows(BusinessException.class, () -> s1.setMessage(messageTmp));
		Assertions.assertEquals(Constants.NULL_WORDS_ERROR, be.getMessage());
	}
	
}
