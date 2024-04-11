package com.fmt;

/**
 * Basic class to store address of persons and stores
 * 
 * @author harrisonjohs
 */
public class Address {
	
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String county;
	
	protected Address(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.county = country;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCounty() {
		return county;
	}
	
}
