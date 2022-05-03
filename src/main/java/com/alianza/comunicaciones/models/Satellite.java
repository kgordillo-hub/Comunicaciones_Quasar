package com.alianza.comunicaciones.models;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.alianza.comunicaciones.exceptions.BusinessException;
import com.alianza.comunicaciones.utils.Constants;

/**
 * Class that represents the satellite information
 * 
 * @author ksgor
 *
 */
public class Satellite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -784944460028578966L;
	/**
	 * 
	 */
	private String name;
	@NotNull
	private Double distance;
	@NotNull
	private String[] message;

	/**
	 * Default constructor
	 */
	public Satellite() {
	}

	/**
	 * Extended constructor
	 */

	// Setters and getters

	public String getName() {
		return name;
	}

	public Satellite(@NotNull Double distance, @NotNull String[] message) {
		this.distance = distance;
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) throws BusinessException {
		this.message = checkNullWords(message);
	}

	/**
	 * Method to check if there are null words in the message
	 * @param wordList
	 * @return if no null values, it will return the same list
	 * @throws BusinessException, if there is a null word in the message
	 */
	private String[] checkNullWords(final String[] wordList) throws BusinessException {
		for (final String word : wordList) {
			if (word == null) {
				throw new BusinessException(Constants.NULL_WORDS_ERROR);
			}
		}
		return wordList;
	}

}
