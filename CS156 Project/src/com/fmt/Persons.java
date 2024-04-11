package com.fmt;

import java.util.List;

public class Persons {

	private final String id;
	private final String lastName;
	private final String firstName;
	private final Address address;
	private final List<String> emails;
	
	protected Persons(String id, String lastName, String firstName, String street, String city, String state, String zip, String country, List<String> emails) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = new Address(street, city, state, zip, country);
		this.emails = emails;
	}
	
	public String getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getEmails() {
		return emails;
	}
	
	
}
