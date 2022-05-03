package com.alianza.comunicaciones.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoordinatesTest {

	private Coordinates cord;
	
	@BeforeEach
	void setUp() throws Exception {
		cord = new Coordinates(null, null);
		cord.setX(20.0);
		cord.setY(30.0);
	}

	@Test
	void test() {
		cord.getX();
		cord.getY();
	}

}
