package com.alianza.comunicaciones.models;

import java.io.Serializable;

/**
 * Class that represents the coordinates information
 * 
 * @author ksgor
 *
 */
public class Coordinates implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3507053441240498058L;
	private Double x;
	private Double y;

	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 */
	public Coordinates(Double x, Double y) {
		super();
		this.x = x;
		this.y = y;
	}

	//Setters and getters
	
	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

}
