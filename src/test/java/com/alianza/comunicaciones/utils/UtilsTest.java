package com.alianza.comunicaciones.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alianza.comunicaciones.exceptions.SystemException;
import com.alianza.comunicaciones.models.Satellite;

class UtilsTest {

	@Test
	void testPropertiesReader() {
		Assertions.assertThrows(SystemException.class,
				() -> Utils.readProperty("property123", "randomFile.properties"));

		Assertions.assertThrows(SystemException.class,
				() -> Utils.readProperty("property123", "exception_messages.properties"));

		SystemException se = Assertions.assertThrows(SystemException.class,
				() -> Utils.readProperty("exception_messages.properties", "property123"));

		Assertions.assertEquals(Constants.PROPERTY_NOT_FOUND.replace("{p_name}", "property123").replace("{f_name}",
				"exception_messages.properties"), se.getMessage());
	}

	@Test
	void testObjectWritterFileNotFound() {
		Assertions.assertThrows(SystemException.class,
				() -> Utils.saveObjectInFile(new Satellite(), Constants.MESSAGE_OBJECTS_PATH, "/other/anyFile.txt"));
	}

	@Test
	void testObjectReaderFileNotFound() {
		Assertions.assertThrows(SystemException.class,
				() -> Utils.getObjectFromFile(Constants.MESSAGE_OBJECTS_PATH, "/other/anyFile.txt"));
	}

	@Test
	void testObjectReaderNoValidObject() {
		Assertions.assertThrows(SystemException.class,
				() -> Utils.getObjectFromFile(Constants.MESSAGE_OBJECTS_PATH, "/anyFile.txt"));
	}

	@Test
	void testMoveFiles() {
		List<String> files = new ArrayList<String>();
		files.add("/file/other.txt");
		Assertions.assertThrows(SystemException.class, () -> Utils
				.moveFiles(Constants.MESSAGE_OBJECTS_PATH, Constants.MESSAGE_OBJECTS_PATH + "/processed", files));
	}

}
