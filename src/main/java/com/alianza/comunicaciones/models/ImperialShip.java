package com.alianza.comunicaciones.models;

import java.io.Serializable;

/**
 * Class that represents the spaceship information
 * @author ksgor
 *
 */
public class ImperialShip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3587337204544031877L;
	private Coordinates position;
	private String message;
	
	/**
	 * Default constructor
	 */
	public ImperialShip() {
	}

	/**
	 * Extended constructor (overloaded)
	 * @param position
	 * @param message
	 */
	public ImperialShip(Coordinates position, String message) {
		super();
		this.position = position;
		this.message = message;
	}
	
	//Setters and getters
	
	public Coordinates getPosition() {
		return position;
	}
	public void setPosition(Coordinates position) {
		this.position = position;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
