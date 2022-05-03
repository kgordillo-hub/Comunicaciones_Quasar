package com.alianza.comunicaciones.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.services.IMessageRecieverService;
import com.alianza.comunicaciones.utils.Constants;

/**
 * Implementation of IMessageRecieverService to reconstruct the message
 * 
 * @author ksgor
 *
 */
@Service
public class MessageRecieverServiceImp implements IMessageRecieverService {

	/**
	 * Implementation for method declared on IMessageRecieverService
	 * 
	 * @param list of message broadcasted by the satellites
	 * @return the complete message if it was possible to determine it
	 * @throws BusinessException if it was not possible to reconstruct the message
	 */
	@Override
	public String getMessage(final List<String[]> messages) throws BusinessException {
		if (validateMessageSize(messages)) {
			final String completeMessage = Stream.of(getAllWords(messages)).collect(Collectors.joining(" "));
			return completeMessage;
		} else {
			throw new BusinessException(Constants.MESSAGE_LENGTH_ERROR);
		}
	}

	/**
	 * Method to check if the arrays length are the same
	 * 
	 * @param list of messages
	 * @return true if size of all arrays is the same. False if not.
	 */
	private boolean validateMessageSize(final List<String[]> messages) {
		for (int i = 0; i < messages.size() - 1; i++) {
			if (messages.get(i).length != messages.get(i + 1).length) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This message basically, takes the list of messages and check two things: 1.
	 * If there is no a different word in the same position of all arrays 2. If
	 * there no empty word in the same array position for all arrays
	 * 
	 * @param list of messages
	 * @return returns the array of all words in the messages
	 * @throws BusinessException, if: 1. The word is empty at the same position in
	 *                            all the arrays. 2. If the word is different at the
	 *                            same position in all the arrays.
	 */
	private String[] getAllWords(final List<String[]> messages) throws BusinessException {
		// As all arrays have the same length we can take the first occurrence
		final int allArraySize = messages.get(0).length;
		String[] completeMessage = new String[allArraySize];

		for (int i = 0; i < allArraySize; i++) {
			for (int j = 0; j < messages.size(); j++) {
				if (!messages.get(j)[i].isEmpty()) {
					if (completeMessage[i] != null && !completeMessage[i].equals(messages.get(j)[i])) {
						throw new BusinessException(Constants.MESSAGE_POSITION_ERROR.replace("{pos}", (i + 1) + ""));
					} else {
						completeMessage[i] = messages.get(j)[i];
					}
				}
			}
			if (completeMessage[i] == null) {
				throw new BusinessException(Constants.MESSAGE_EMPTY_WORD_ERROR.replace("{pos}", (i + 1) + ""));
			}
		}
		return completeMessage;
	}
}
