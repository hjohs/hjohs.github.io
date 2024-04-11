package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		
		Connection conn = DatabaseConnector.openConnection();
		
		// Uses delete command to clear contents of all tables
		String query = "delete from Equipment";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Product";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
				
			query = "delete from Service";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from InvoiceItem";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Item";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Invoice";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Email";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Customer";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Manager";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Store";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Person";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			query = "delete from Address";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street,
			String city, String state, String zip, String country) {
        
		if (personCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (!isNumeric(zip)) {
			System.out.println("Entry must be numeric");
			throw new RuntimeException();
		}
		if (firstName.isEmpty()) {
			throw new RuntimeException();
		}
		if (lastName.isEmpty()) {
			throw new RuntimeException();
		}
		if (street.isEmpty()) {
			throw new RuntimeException();
		}
		if (city.isEmpty()) {
			throw new RuntimeException();
		}
		if (state.isEmpty()) {
			throw new RuntimeException();
		}
		if (country.isEmpty()) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		//Inserts new address first
		String query = "insert into Address (street,city,state,zip,county) values (?,?,?,?,?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1,street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setInt(4, Integer.valueOf(zip));
			ps.setString(5, country);
			ps.executeUpdate();
			
			// Gets generated key from address that was just created
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			int key = keys.getInt(1);
			
			//Checks if person with given person code already exists
			ResultSet rs = null;
			query = "select personCode from Person where personCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			
			if (rs.next() == false) {
				// Inserts new person
				query = "insert into Person (personCode,lastName,firstName,addressId) values (?,?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1,personCode);
				ps.setInt(4,key);
				ps.setString(2,lastName);
				ps.setString(3, firstName);
				ps.executeUpdate();
			}	else {
				System.out.println("Person already exists with given person code");
				throw new RuntimeException();
			}
			
			
			ps.close();
			rs.close();
			keys.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		
		if (personCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (email.isEmpty()) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "insert into Email (emailName,personCode) values (?,?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2,personCode);
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {
        
		if (storeCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (managerCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (!isNumeric(zip)) {
			throw new RuntimeException();
		}
		if (street.isEmpty()) {
			throw new RuntimeException();
		}
		if (city.isEmpty()) {
			throw new RuntimeException();
		}
		if (state.isEmpty()) {
			throw new RuntimeException();
		}
		if (country.isEmpty()) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		//Inserts new address first
		String query = "insert into Address (street,city,state,zip,county) values (?,?,?,?,?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1,street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setInt(4, Integer.valueOf(zip));
			ps.setString(5, country);
			ps.executeUpdate();
			
			// Gets generated key from address that was just created
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			int key = keys.getInt(1);
			
			//Query to check if store already exists with the given store code
			ResultSet rs = null;
			query = "select storeCode from Store where storeCode = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			rs = ps.executeQuery();
			
			if (rs.next() == false) {
				// Inserts new store
				query = "insert into Store (storeCode,addressId,managerCode) values (?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, storeCode);
				ps.setInt(2,key);
				ps.setString(3, managerCode);
				ps.executeUpdate();
			}	else {
				System.out.println("Store already exists with given store code");
				throw new RuntimeException();
			}
			
			
			ps.close();
			rs.close();
			keys.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Adds a product record to the database with the given <code>code</code>, <code>name</code> and
	 * <code>unit</code> and <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addProduct(String code, String name, String unit, double pricePerUnit) {
		
		if (code.isEmpty()) {
			throw new RuntimeException();
		}
		if (name.isEmpty()) {
			throw new RuntimeException();
		}
		if (unit.isEmpty()) {
			throw new RuntimeException();
		}
		if (pricePerUnit < 0) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "select itemCode from Item where itemCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,code);
			rs = ps.executeQuery();
			
			//Checks if item with given code already exists
			if (rs.next() == false) {
				// Inserts new Item
				query = "insert into Item (itemCode,name) values (?,?)";
				ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.executeUpdate();
				
				// Gets generated key from address that was just created
				ResultSet keys = ps.getGeneratedKeys();
				keys.next();
				int key = keys.getInt(1);
				
				// Inserts new Product
				query = "insert into Product (unit,unitPrice,itemId) values (?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, unit);
				ps.setDouble(2,pricePerUnit);
				ps.setInt(3, key);
				ps.executeUpdate();
				
				
			}	else {
				System.out.println("Product already exists with given code");
				throw new RuntimeException();
			}
			
			ps.close();
			rs.close();
			conn.close();
					
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Adds an equipment record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addEquipment(String code, String name, String modelNumber) {
    
		if (code.isEmpty()) {
			throw new RuntimeException();
		}
		if (name.isEmpty()) {
			throw new RuntimeException();
		}
		if (modelNumber.isEmpty()) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "select itemCode from Item where itemCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,code);
			rs = ps.executeQuery();
			
			//Checks if item with given code already exists
			if (rs.next() == false) {
				// Inserts new Item
				query = "insert into Item (itemCode,name) values (?,?)";
				ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.executeUpdate();
				
				// Gets generated key from address that was just created
				ResultSet keys = ps.getGeneratedKeys();
				keys.next();
				int key = keys.getInt(1);
				
				// Inserts new Equipment
				query = "insert into Equipment (model,itemId) values (?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, modelNumber);
				ps.setInt(2, key);
				ps.executeUpdate();
				
			}	else {
				System.out.println("Equipment already exists with given code");
				throw new RuntimeException();
			}
			
			ps.close();
			rs.close();
			conn.close();
					
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a service record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addService(String code, String name, double costPerHour) {
    
		if (code.isEmpty()) {
			throw new RuntimeException();
		}
		if (name.isEmpty()) {
			throw new RuntimeException();
		}
		if (costPerHour < 0) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "select itemCode from Item where itemCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,code);
			rs = ps.executeQuery();
			
			
			//Checks if item with given code already exists
			if (rs.next() == false) {
				// Inserts new Item
				query = "insert into Item (name,itemCode) values (?,?)";
				ps = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				ps.setString(2, code);
				ps.setString(1, name);
				ps.executeUpdate();
				
				// Gets generated key from address that was just created
				ResultSet keys = ps.getGeneratedKeys();
				keys.next();
				int key = keys.getInt(1);
				
				// Inserts new Service
				query = "insert into Service (hourlyRate,itemId) values (?,?)";
				ps = conn.prepareStatement(query);
				ps.setDouble(1, costPerHour);
				ps.setInt(2, key);
				ps.executeUpdate();
				
			}	else {
				System.out.println("Service already exists with given code");
				throw new RuntimeException();
			}
			
			ps.close();
			rs.close();
			conn.close();
					
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
   * @param invoiceDate
	 */
	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode, String invoiceDate) {
	
		if (invoiceCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (storeCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (customerCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (salesPersonCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (!isDate(invoiceDate)) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "select invoiceCode from Invoice where invoiceCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1,invoiceCode);
			rs = ps.executeQuery();
			
			
			//Checks if invoice with given code already exists
			if (rs.next() == false) {
				// Inserts new invoice
				query = "insert into Invoice (invoiceCode,customerCode,storeCode,invoiceDate,salesPersonCode) values (?,?,?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1,invoiceCode);
				ps.setString(2, customerCode);
				ps.setString(3, storeCode);
				ps.setString(4, invoiceDate);
				ps.setString(5, salesPersonCode);
				ps.executeUpdate();
			}
			
			ps.close();
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
		
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>)
	 * to a particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */
	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
    
		if (invoiceCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (itemCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (quantity < 0) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "insert into InvoiceItem (invoiceCode,itemCode,type,unitAmount) values (?,?,\"Product\",?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, itemCode);
			ps.setDouble(3,Double.valueOf(quantity));
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular equipment <i>purchase</i> (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
    
		if (invoiceCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (itemCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (purchasePrice < 0) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "insert into InvoiceItem (invoiceCode,itemCode,type,purchasePrice) values (?,?,\"Purchased\",?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, itemCode);
			ps.setDouble(3,Double.valueOf(purchasePrice));
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular equipment <i>lease</i> (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the given 30-day
	 * <code>periodFee</code> and <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate, String endDate) {
    
		if (invoiceCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (itemCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (periodFee < 0) {
			throw new RuntimeException();
		}
		if (!isDate(beginDate)) {
			throw new RuntimeException();
		}
		if (!isDate(endDate)) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "insert into InvoiceItem (invoiceCode,itemCode,type,monthFee,startDate,endDate) values (?,?,\"Leased\",?,?,?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, itemCode);
			ps.setDouble(3, periodFee);
			ps.setString(4, beginDate);
			ps.setString(5, endDate);
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
   
		if (invoiceCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (itemCode.isEmpty()) {
			throw new RuntimeException();
		}
		if (billedHours < 0) {
			throw new RuntimeException();
		}
		
		Connection conn = DatabaseConnector.openConnection();
		
		String query = "insert into InvoiceItem (invoiceCode,itemCode,type,hours) values (?,?,\"Service\",?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.setString(2, itemCode);
			ps.setDouble(3,Double.valueOf(billedHours));
			ps.executeUpdate();
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}


	}

	/**
	 * Helper method to determine if string is valid integer
	 * 
	 * @param num
	 * 
	 */
	public static boolean isNumeric(String num) { 
		  try {  
		    Integer.parseInt(num);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	/**
	 * Helper method to determine if string is valid LocalDate
	 * 
	 * @param date
	 * 
	 */
	public static boolean isDate(String date) { 
		  try {  
		    LocalDate.parse(date);
		    return true;
		  } catch(DateTimeParseException e){  
		    return false;  
		  }  
		}
}
