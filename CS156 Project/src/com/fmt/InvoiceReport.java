package com.fmt;

import java.util.List;

/**
 * Runnable which creates instances of necessary data, serializes
 * that data into Json files, and generates reports
 * 
 * @author harrisonjohs
 * data: 2023-4-14
 */

public class InvoiceReport {

	public static final String personsJson = "data/Persons.json";
	public static final String storesJson = "data/Stores.json";
	public static final String itemsJson = "data/Items.json";

	
	public static void main(String args[]) {
		
		
		List<Persons> persons = CSVReader.personsReader();
		List<Stores> stores = CSVReader.storesReader();
		List<Items> items = CSVReader.itemsReader();
		List<Invoice> invoices = CSVReader.invoiceReader(items,persons);
		
		//Serialize lists to json files
		JsonSerializer.serialize(personsJson,persons);
		JsonSerializer.serialize(itemsJson,items);
		JsonSerializer.serialize(storesJson,stores);
		ReportGenerator reportGen = new ReportGenerator();
		
		//Generate necessary reports
		reportGen.salesReport(invoices,persons);
		reportGen.storeReport(invoices,stores,persons);
		reportGen.invoiceReport(invoices);

	}
}
