package com.alianza.comunicaciones.models;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.utils.Constants;

@SpringBootTest
class SatelliteListTest {

	private SatelliteList satelliteList;
	
	@BeforeEach
	void setUp() throws Exception {
		satelliteList = new SatelliteList();
		final List<Satellite> satList = new ArrayList<>();
		satList.add(new Satellite(null, null));
		satelliteList.setSatellites(satList);
		satelliteList.getSatellites();
	}

	@Test
	void testInitialPositionsNull() {
		BusinessException be = Assertions.assertThrows(BusinessException.class,
				() -> satelliteList.getInitialPositions());
		Assertions.assertEquals(Constants.SATELLITE_NULL, be.getMessage());
	}

}
