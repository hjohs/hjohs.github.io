package com.fmt;

import java.util.List;

public class ReportGenerator {
	
	//Generates report for every sale
	public void salesReport(List<Invoice> invoices, List<Persons> persons) {
		System.out.printf("%22s %n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n","Sales Summary Report");
		System.out.printf("%s %15s %10s %10s %15s %n","Invoice ID","Customer ID","Item","Tax","Total");
		for (Invoice invoice : invoices) {
			for (InvoiceItems item : invoice.getInvoiceItems()) {
				System.out.printf("%s %20s %10s %10.2f %15.2f %n",invoice.getInvoiceCode(),
						invoice.getCustomerName(invoice.getCustomerCode()),item.getType(),item.getTax(),item.getTotal());
			}
		}
		System.out.printf("%n%n");
	}
	//Generates report for every store
	public void storeReport(List<Invoice> invoices, List<Stores> stores, List<Persons> persons) {
		System.out.printf("%22s %n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n","Store Summary Report");
		System.out.printf("%s %16s %13s %13s %n","Store","Manager","# Sales","Grand Total");
		for (Stores store : stores) {
			for (Invoice invoice : invoices) {
				if (invoice.getStoreCode().equals(store.getStoreCode())) {
					System.out.printf("%s %22s %9d %15.2f %n",invoice.getStoreCode(),
							invoice.getManagerName(store.getManagerCode()),store.getNumSales(invoices),store.getGrandTotal(invoices));
				}	
			}	
		}
		System.out.println();
	}
	//Generates report for every invoice
	public void invoiceReport(List<Invoice> invoices) {
		System.out.printf("%n%22s %n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n","Invoice Summary Report");
		for (Invoice inv : invoices) {
			System.out.printf("Invoice: %s %28s%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
					+ "~~~~~~~~~~~~~~~~~~~~%n",inv.getInvoiceCode(),"Total");
			for(InvoiceItems item : inv.getInvoiceItems()) {
				System.out.printf("%s%n %5s (%5s) %8s %15.2f%n%s%n","Item:",item.getItemCode(),
						item.getType(),item.getItem().getName(),item.getTotal(),inv.getInvoiceDate());
			}
			System.out.printf("%35s %.2f%n%n%n","Grand Total:",inv.getTotal());		
		}
	}
	
	public void sortedSalesReport(List<Invoice> invoices, List<Stores> stores, List<Persons> persons) {
		System.out.printf("%10s %s %n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n","Sales by",invoices);
		System.out.printf("%s %7s %13s %21s %11s %n","Sale:","Store:","Customer:","SalesPerson:","Total:");
		for (Invoice invoice : invoices) {
			String code = "";
			for (Stores store : stores) {
				if (invoice.getStoreCode().equals(store.getStoreCode())) {
					code = store.getManagerCode();
				}
			}
			System.out.printf("%s %-10s %-18s %-17s %.2f %n",invoice.getInvoiceCode(),invoice.getStoreCode(),
					invoice.getCustomerName(invoice.getCustomerCode()),invoice.getManagerName(code),invoice.getTotal());
		}
		System.out.println();
		System.out.println();
	}
}
