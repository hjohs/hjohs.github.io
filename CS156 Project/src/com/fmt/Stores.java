package com.fmt;

import java.util.List;

public class Stores {

	private final String storeCode;
	private final String managerCode;
	private final Address address;
	
	protected Stores(String storeCode, String managerCode, String street, String city, String state, String zip, String country) {
		super();
		this.storeCode = storeCode;
		this.managerCode = managerCode;
		this.address = new Address(street, city, state, zip, country);
	}

	public String getStoreCode() {
		return storeCode;
	}

	public String getManagerCode() {
		return managerCode;
	}

	public Address getAddress() {
		return address;
	}
	
	//Calculates total sales for entire store
	public double getGrandTotal(List<Invoice> invoices) {
		double grandTotal = 0;
		for (Invoice inv : invoices) {
			if (inv.getStoreCode().equals(storeCode)) {
				grandTotal += inv.getTotal();
			}
		}
		return grandTotal;
	}
	
	//Calculate number of sales at this location
	public int getNumSales(List<Invoice> invoices) {
		int numSales = 0;
		for (Invoice inv : invoices) {
			if (inv.getStoreCode().equals(storeCode)) {
				numSales += inv.getInvoiceItems().size();
			}
		}
		return numSales;
	}
}
