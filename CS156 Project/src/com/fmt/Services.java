package com.fmt;

/**
 * Subclass of Items which stores services data
 * 
 * @author harrisonjohs
 *
 */
public class Services extends Items {

	private final String name;
	private final double hourlyRate;
	
	protected Services(String code, String name, double hourlyRate) {
		super(code);
		this.name = name;
		this.hourlyRate = hourlyRate;
	}

	public String getName() {
		return name;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}
	
	public double getUnitPrice() {
		return 0;
	}
}
