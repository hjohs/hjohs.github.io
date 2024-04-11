package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnector {

	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static final String USERNAME = "hjohs";
	public static final String PASSWORD = "3xx:eV";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;
	
	
	public static Connection openConnection() {
		
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	// Returns list of persons instances from database
	public List<Persons> personsReader() {
		
		List<Persons> persons = new ArrayList<Persons>();
		
		Connection conn = openConnection();
		
		String query = "select personId,personCode,lastName,firstName,street,city,state,zip,county from Person join Address on Person.addressId = Address.addressId";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Persons p = new Persons(rs.getString("personCode"),rs.getString("lastName"),rs.getString("firstName"),rs.getString("street"),
						rs.getString("city"),rs.getString("state"),rs.getString("zip"),rs.getString("county"),new ArrayList<String>());
				persons.add(p);
			}
			
			//Query to select emails for each person
			query = "select emailName,Person.personCode from Email join Person on Email.personCode = Person.personCode";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			// Adds emails to corresponding person
			while (rs.next()) {
				for (Persons p : persons) {
					if (p.getId().equals(rs.getString("personCode"))) {
						p.getEmails().add(rs.getString("emailName"));
					}
				}
			}
			rs.close();
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return persons;
	}
	
	// Returns list of stores instances from database
	public List<Stores> storesReader() {
		
		List<Stores> stores = new ArrayList<Stores>();
		
		Connection conn = openConnection();
		
		String query = "select storeCode,managerCode,street,city,state,zip,county from Store join Address on Store.addressId = Address.addressId";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			//Creates instances of stores
			while (rs.next()) {
				Stores s = new Stores(rs.getString("storeCode"),rs.getString("managerCode"),rs.getString("street"),
						rs.getString("city"),rs.getString("state"),rs.getString("zip"),rs.getString("county"));
				stores.add(s);
			}
			rs.close();
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return stores;
	}
	
	// Returns list of item instances from database
	public List<Items> itemsReader() {
		
		List<Items> items = new ArrayList<Items>();
		
		Connection conn = openConnection();
		
		//Query for service info
		String query = "select itemCode,name,hourlyRate from Item join Service on Item.itemId = Service.itemId";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Services s = new Services(rs.getString("itemCode"),rs.getString("name"),rs.getDouble("hourlyRate"));
				items.add(s);
			}
			
			//Query for product info
			query = "select itemCode,name,unit,unitPrice from Item join Product on Item.itemId = Product.itemId";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Products p = new Products(rs.getString("itemCode"),rs.getString("name"),
						rs.getString("unit"),rs.getDouble("unitPrice"));
				items.add(p);
			}
			
			//Query for equipment info
			query = "select itemCode,name,model from Item join Equipment on Item.itemId = Equipment.itemId";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Equipment e = new Equipment(rs.getString("itemCode"),rs.getString("name"),rs.getString("model"));
				items.add(e);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return items;
	}
	
	public InvoiceList<Invoice> invoiceReader(List<Items> items, List<Persons> persons) {
		
		InvoiceList<Invoice> invoices = new InvoiceList<Invoice>();
		
		Connection conn = openConnection();
		
		String query = "select invoiceCode,Invoice.storeCode,Invoice.salesPersonCode,invoiceDate "
				+ "from Invoice join Store on Invoice.storeCode = Store.storeCode";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			//Creates instances of invoices and adds to list
			while (rs.next()) {
				Invoice i = new Invoice(rs.getString("invoiceCode"),rs.getString("storeCode"),
						rs.getString("salesPersonCode"),rs.getString("salesPersonCode"),
						LocalDate.parse(rs.getString("invoiceDate")));
						i.setPersons(persons);
				invoices.add(i);
			}
			
			query = "select invoiceCode,type,itemCode,monthFee,startDate,endDate,purchasePrice,hours,unitAmount from InvoiceItem";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				// Finds corresponding item based on itemId
				Items item = null;
				for (Items x : items) {
					if (x.getCode().equals(rs.getString("itemCode"))) {
						item = x;
					}
				}
				
				// Instantiate an invoice item
				InvoiceItems i = new InvoiceItems(rs.getString("invoiceCode"),
						rs.getString("itemCode"),item,rs.getString("type"));
				
				// Checks type of item and sets corresponding values needed for the type
				if (rs.getString("type").equals("Purchased")) {
					i.setPurchasePrice(rs.getDouble("purchasePrice"));
				}
				else if (rs.getString("type").equals("Leased")) {
					i.setEndDate(LocalDate.parse(rs.getString("endDate")));
					i.setStartDate(LocalDate.parse(rs.getString("startDate")));
					i.setMonthFee(rs.getDouble("monthFee"));
				}
				else if (rs.getString("type").equals("Service")) {
					i.setHours(rs.getDouble("hours"));
				}
				else if (rs.getString("type").equals("Product")) {
					i.setUnitAmount(rs.getDouble("unitAmount"));
				}
				
				for (Invoice inv : invoices) {
					if (inv.getInvoiceCode().equals(i.getInvoiceCode())) {
						inv.getInvoiceItems().add(i);
					}
				}
			}
			rs.close();
			ps.close();
			conn.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		
		return invoices;
	}
	
}
