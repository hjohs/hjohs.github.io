package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Class which has carries conditional values 
 * depending on what the type is
 * 
 * @author harrisonjohs
 *
 */
public class InvoiceItems {

	private final String invoiceCode;
	private final String itemCode;
	private final Items item;
	private Double monthFee = null;
	private LocalDate startDate = null;
	private LocalDate endDate = null;
	private Double purchasePrice = null;
	private Double hours = null;
	private Double unitAmount = null;
	private final String type;
	
	
	protected InvoiceItems(String invoiceCode, String itemCode, Items item, String type) {
		super();
		this.invoiceCode = invoiceCode;
		this.itemCode = itemCode;
		this.item = item;
		this.type = type;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public Items getItem() {
		return item;
	}

	public String getType() {
		return type;
	}

	public Double getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(Double monthFee) {
		this.monthFee = monthFee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getHours() {
		return hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public Double getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(Double unitAmount) {
		this.unitAmount = unitAmount;
	}
	
	public double getTotal() {
		
		double total = 0;
		
		// Switch cases to return total based on different item types
		if (type.equals("Purchased")) {
		        total = purchasePrice;
		}
		else if (type.equals("Leased")) {
				long duration = startDate.until(endDate, ChronoUnit.DAYS);
				total = monthFee * (duration/30);
		}
		else if (type.equals("Product")) {
		        total = (unitAmount * item.getUnitPrice());
		}
		else if (type.equals( "Service")) {
		        total = (hours * item.getHourlyRate());
		}
		return total;
	}
	
	// Calculates total tax
	public double getTax() {
		double tax = 0;
		
		// Switch cases to return tax based on different item types
		if (type.equals("Purchased")) {
		        tax = 0;
		}
		else if (type.equals("Leased")) {
 				long duration = startDate.until(endDate, ChronoUnit.DAYS);
				double price = monthFee * (duration/30);
				if (price < 10000) {
					tax = 0;
				}
				else if (price < 100000) {
					tax = 500;
				}
				else {
					tax = 1500;
				}
		}
		else if (type.equals("Product")) {
		        tax = (unitAmount * item.getUnitPrice()) * 0.0715;
		}
		else if (type.equals("Service")) {
		        tax = (hours * item.getHourlyRate()) * 0.0345;
		}
		return tax;
	}
	
}
