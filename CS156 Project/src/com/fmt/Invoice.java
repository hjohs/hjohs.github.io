package com.fmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

	private final String invoiceCode;
	private final String storeCode;
	private final String customerCode;
	private final String salesPersonCode;
	private final LocalDate invoiceDate;
	private List<Persons> persons = null;
	public List<InvoiceItems> invoiceItems = new ArrayList<InvoiceItems>();
	
	protected Invoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			LocalDate invoiceDate) {
		this.invoiceCode = invoiceCode;
		this.storeCode = storeCode;
		this.customerCode = customerCode;
		this.salesPersonCode = salesPersonCode;
		this.invoiceDate = invoiceDate;
	}
	
	public void setPersons(List<Persons >persons) {
		this.persons = persons;
	}
	
	public String getInvoiceCode() {
		return invoiceCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public String getSalesPersonCode() {
		return salesPersonCode;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public List<InvoiceItems> getInvoiceItems() {
		return invoiceItems;
	}
	
	//Returns formatted last name first name string for a speicific customer
	public String getCustomerName(String id) {
		String name ="";
		for (Persons p : persons) {
			if (p.getId().equals(id)) {
				name = p.getLastName() +"," +p.getFirstName();
			}
		}
		return name;
	}
	
	//Returns formatted last name first name string for a speicific manager
	public String getManagerName(String code) {
		String name ="";
		for (Persons p : persons) {
			if (p.getId().equals(code)) {
				name = p.getLastName() +"," +p.getFirstName();
			}
		}
		return name;
	}
	//Calculates total of all sales in this invoice
	public double getTotal() {
		double total = 0;
		for (InvoiceItems item : invoiceItems) {
			total += item.getTotal() + item.getTax();
		}
		return total;
	}

	
}
