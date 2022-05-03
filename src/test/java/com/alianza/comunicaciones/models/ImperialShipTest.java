package com.alianza.comunicaciones.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImperialShipTest {
	
	private ImperialShip impShip;

	@BeforeEach
	void setUp() throws Exception {
		impShip = new ImperialShip();
		impShip.setPosition(new Coordinates(null, null));
		impShip.setMessage("any message");
	}

	@Test
	void test() {
		impShip.getMessage();
		impShip.getPosition();
	}

}
