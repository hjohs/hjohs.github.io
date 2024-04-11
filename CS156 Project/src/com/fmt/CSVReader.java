package com.fmt;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

	public static final String personsFile = "data/Persons.csv";
	public static final String storesFile = "data/Stores.csv";
	public static final String itemsFile = "data/Items.csv";
	public static final String invoiceFile = "data/Invoices.csv";
	public static final String invoiceItemsFile = "data/InvoiceItems.csv";
	
	public static List<Persons> personsReader() {
		
		//Create array of persons
		List<Persons> persons = new ArrayList<Persons>();
		
		//Scanner to create instances of persons
		try (Scanner p = new Scanner(new File(personsFile))) {
			int numPersons = Integer.parseInt(p.nextLine().replace(",",""));
			for (int i = 0; i < numPersons; i++) {
				Persons newP = null;
				List<String> emails = new ArrayList<String>();
 				String line = p.nextLine();
				String tokens[] = line.split(",");
				String id = tokens[0];
				String lastName = tokens[1];
				String firstName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				String country = tokens[7];
				for (int j = 8; j < tokens.length; j++) {
					if (tokens[j] != "") {
						emails.add(tokens[j]);
					}
				}
				newP = new Persons(id,lastName,firstName,street,city,state,zip,country,emails);
				persons.add(newP);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return persons;
	}
	
	public static List<Stores> storesReader() {
		//Create array of stores
		List<Stores> stores = new ArrayList<Stores>();
		
		//Scanner to create instances of stores
		try (Scanner s = new Scanner(new File(storesFile))) {
			int numStores = Integer.parseInt(s.nextLine().replace(",",""));
			for (int i = 0; i < numStores; i++) {
				Stores newS = null;
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String storeCode = tokens[0];
				String managerCode = tokens[1];
				String street = tokens[2];
				String city = tokens[3];
				String state = tokens[4];
				String zip = tokens[5];
				String country = tokens[6];
				newS = new Stores(storeCode,managerCode,street,city,state,zip,country);
				stores.add(newS);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return stores;
	}
	
	public static List<Items> itemsReader() {
		
		//Create array of items
		List<Items> items = new ArrayList<Items>();
		
		//Scanner to create instances of items
		try (Scanner x = new Scanner(new File(itemsFile))) {
			int numItems = Integer.parseInt(x.nextLine().replace(",",""));
			for (int i = 0; i < numItems; i++) {
				Items newI = null;
				String line = x.nextLine();
				String tokens[] = line.split(",");
				String code = tokens[0];
				if (tokens[1].equals("E")) {
					newI = new Equipment(code,tokens[2],tokens[3]);
				} else if (tokens[1].equals("P")) {
					newI = new Products(code,tokens[2],tokens[3],Double.valueOf(tokens[4]));
				} else if (tokens[1].equals("S")) {
					newI = new Services(code,tokens[2],Double.valueOf(tokens[3]));
				}
				items.add(newI);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}
	
	
	public static List<Invoice> invoiceReader(List<Items> items, List<Persons> persons) {
	
		//Create empty list for invoices
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		//Scanner to create invoice instances
		try (Scanner s = new Scanner(new File(invoiceFile))) {
			int numInvoices = Integer.parseInt(s.nextLine().replace(",",""));
			for (int i = 0; i < numInvoices; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				LocalDate date = LocalDate.parse(tokens[4]);
				Invoice inv = new Invoice(tokens[0],tokens[1],tokens[2],tokens[3],date);
				inv.setPersons(persons);
				invoices.add(inv);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//Scanner to assign invoice items to respective invoices
		try (Scanner s = new Scanner(new File(invoiceItemsFile))) {
			int numItems = Integer.parseInt(s.nextLine().replace(",",""));
			for (int i = 0; i < numItems; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				int invIndex = 0;
				int itemIndex = 0;
				for (int j = 0; j < invoices.size();j++) {
					if (invoices.get(j).getInvoiceCode().equals(tokens[0])) {
						invIndex = j;
						break;
					}
				}
				for (int k = 0; k < items.size();k++) {
					if (items.get(k).getCode().equals(tokens[1])) {
						itemIndex = k;
						break;
					} 
				}
				//Creates invoice item instances based on type of item
				if (tokens[2].equals("P")) {
					InvoiceItems temp = new InvoiceItems(tokens[0], tokens[1], items.get(itemIndex),"Purchased");
					temp.setPurchasePrice(Double.valueOf(tokens[3]));
					invoices.get(invIndex).invoiceItems.add(temp);
					continue;
				} 
				else if (tokens[2].equals("L")) {
					InvoiceItems temp = new InvoiceItems(tokens[0], tokens[1], items.get(itemIndex),"Leased");
					LocalDate start = LocalDate.parse(tokens[4]);
					LocalDate end = LocalDate.parse(tokens[5]);
					temp.setEndDate(end);
					temp.setStartDate(start);
					temp.setMonthFee(Double.valueOf(tokens[3]));
					invoices.get(invIndex).invoiceItems.add(temp);
					continue;
				}
				else if (items.get(itemIndex).getClass().getSimpleName().equals("Services")) {
					InvoiceItems temp = new InvoiceItems(tokens[0], tokens[1], items.get(itemIndex),"Service");
					temp.setHours(Double.valueOf(tokens[2]));
					invoices.get(invIndex).invoiceItems.add(temp);
					continue;
				}
				else if (items.get(itemIndex).getClass().getSimpleName().equals("Products")) {
					InvoiceItems temp = new InvoiceItems(tokens[0], tokens[1], items.get(itemIndex),"Product");
					temp.setUnitAmount(Double.valueOf(tokens[2]));
					invoices.get(invIndex).invoiceItems.add(temp);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return invoices;
	}
}
