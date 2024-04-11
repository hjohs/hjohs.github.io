package com.fmt;

/**
 * Superclass which contains code for equipment, products, and services
 * 
 * @author harrisonjohs
 *
 */
public abstract class Items {

	private final String code;

	protected Items(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}	
	public abstract double getHourlyRate();
	public abstract double getUnitPrice();
	public abstract String getName();
}
