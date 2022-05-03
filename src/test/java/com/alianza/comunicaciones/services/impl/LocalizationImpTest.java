package com.alianza.comunicaciones.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.services.ILocalizationService;
import com.alianza.comunicaciones.utils.Constants;

@SpringBootTest
class LocalizationImpTest {

	@Autowired
	ILocalizationService localizationService;

	@BeforeEach
	void setUp() throws Exception {
	}

	@DisplayName("Testing regular exception when arrays are do not have the same size")
	@Test
	void testInvalidDistancesSize() {
		
		
		final BusinessException be = Assertions.assertThrows(BusinessException.class,
				() -> localizationService.getLocation(new double[] { 10.0, 20.0 },
						new double[][] { { 50.0, 30.1 }, { 50.0, 20 }, { 30.0, 10.0 } }));
		Assertions.assertEquals(Constants.SHIP_POSITION_NULL, be.getMessage());

	}

	
}
