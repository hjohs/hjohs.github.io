	package com.fmt;

/**
 * Subclass of Items which stores Product data
 * 
 * @author harrisonjohs
 *
 */
public class Products extends Items {

	private final String name;
	private final String unit;
	private final double unitPrice;


	protected Products(String code, String name, String unit, double unitPrice) {
		super(code);
		this.name = name;
		this.unit = unit;
		this.unitPrice = unitPrice;
	}
	
	public String getName() {
		return name;
	}

	public String getUnit() {
		return unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}	
	
	public double getHourlyRate() {
		return 0;
	}
}
