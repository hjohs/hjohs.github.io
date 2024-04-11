package com.fmt;

/**
 * Subclass of Items which stores equipment data
 * 
 * @author harrisonjohs
 *
 */
public class Equipment extends Items {
	
	private final String name;
	private final String model;
	
	protected Equipment(String code, String name, String model) {
		super(code);
		this.name = name;
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public String getModel() {
		return model;
	}
	
	public double getHourlyRate() {
		return 0;
	}
	public double getUnitPrice() {
		return 0;
	}
}
