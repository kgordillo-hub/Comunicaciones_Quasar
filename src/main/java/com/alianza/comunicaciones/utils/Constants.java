package com.alianza.comunicaciones.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alianza.comunicaciones.exceptions.SystemException;

/**
 * Class with the values of all constant and message used in the main program
 * 
 * @author ksgor
 *
 */
public final class Constants {

	public static String SATELLITES_CONFIG_PROPERTIES = "satellite_config.properties";
	public static String EXCEPTION_MESSAGES_PROPERTIES = "exception_messages.properties";

	public static String PROPERTY_NOT_FOUND;
	public static String ERROR_READING_PROPERTIES;
	public static String MESSAGE_LENGTH_ERROR;
	public static String MESSAGE_POSITION_ERROR;
	public static String MESSAGE_EMPTY_WORD_ERROR;
	public static String SHIP_POSITION_ERROR;
	public static String SHIP_POSITION_NULL;
	public static String SATELLITE_NOT_FOUND;
	public static String SATELLITE_NULL;
	public static String BAD_SPLIT_SATELLITE_REQUEST;
	public static String INCORRECT_MESSAGE_FORMAT_GET;
	public static String NULL_WORDS_ERROR;
	public static String LACK_INFO_MESSAGES;
	public static String FILE_NOT_FOUND;
	public static String IO_EXCEPTION;

	public static String DATA_FILE_FORMAT;
	public static String MESSAGE_OBJECTS_PATH;
	public static String MESSAGE_PROCESSED_OBJECTS_PATH;
	public static String MESSAGE_SPLITTER;

	private static final Logger LOGGER = LoggerFactory.getLogger("error-log");

	static {
		try {
			PROPERTY_NOT_FOUND = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.property.not_found");
			
			ERROR_READING_PROPERTIES = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.properties.reading");
			
			MESSAGE_LENGTH_ERROR = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES, "exception.message.list.size");
			
			MESSAGE_POSITION_ERROR = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.mismatch.position");
			
			MESSAGE_EMPTY_WORD_ERROR = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.mismatch.empty");
			
			SHIP_POSITION_ERROR = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.position.calculate");
			
			SHIP_POSITION_NULL = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES, "exception.message.position.null");
			
			SATELLITE_NOT_FOUND = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.satellite.not_found");
			
			SATELLITE_NULL = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES, "exception.message.satellite.null");

			BAD_SPLIT_SATELLITE_REQUEST = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.satellite.split.bad_request");

			INCORRECT_MESSAGE_FORMAT_GET = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.message.format");
			
			FILE_NOT_FOUND = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.file.not_found");
			
			IO_EXCEPTION = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES,
					"exception.message.io.exception");
			
			NULL_WORDS_ERROR = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES, "exception.message.words.not_null");
			
			LACK_INFO_MESSAGES = Utils.readProperty(EXCEPTION_MESSAGES_PROPERTIES, "exception.message.lack.info");

			DATA_FILE_FORMAT = Utils.readProperty(SATELLITES_CONFIG_PROPERTIES, "satellite.data_file_format");
			
			MESSAGE_OBJECTS_PATH = Utils.readProperty(SATELLITES_CONFIG_PROPERTIES, "satellite.data.objects.path");
			
			MESSAGE_PROCESSED_OBJECTS_PATH = Utils.readProperty(SATELLITES_CONFIG_PROPERTIES,
					"satellite.data.objects.path.processed");
			
			MESSAGE_SPLITTER = Utils.readProperty(SATELLITES_CONFIG_PROPERTIES,
					"satellite.message.splitter");
			
		} catch (final SystemException e) {
			LOGGER.error("Error while loading constant values", e);
		}
	}
}
