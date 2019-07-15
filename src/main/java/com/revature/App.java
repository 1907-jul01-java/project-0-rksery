package com.revature;

import java.util.Scanner;

import com.revature.customers.Customer;
import com.revature.customers.CustomerDao;

/**
 * Banking app
 *
 */
public class App {
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		String title = "Customer";
		String username = "undefineduser";
		String pw = "undefinedpass";
		String firstname = "undefinedfirst";
		String middlename = "Not Applicable";
		String lastname = "undefinedlast";
		float balance = 0;

		ConnectionUtil connectionUtil = new ConnectionUtil();
		CustomerDao customerDao = new CustomerDao(connectionUtil.getConnection());
		System.out.println("Please enter a username and press enter.");
		username = scnr.nextLine();
		// System.out.println(username);
		System.out.println("Please enter a password and press enter.");
		pw = scnr.nextLine();
		System.out.println("Please enter your first name and press enter.");
		firstname = scnr.nextLine();
		System.out.println("Please enter your middle name and press enter.");
		middlename = scnr.nextLine();
		System.out.println("Please enter your last name and press enter.");
		lastname = scnr.nextLine();
		System.out.println("Please enter an initial deposit and press enter.");
		balance = Float.parseFloat(scnr.nextLine());

		customerDao.insert(new Customer(title, username, pw, firstname, middlename, lastname, balance));
		// System.out.println(customerDao.getAll());
		// System.out.println("Kevin Bacon id:" + customerDao.getActorIdByName("Kevin
		// Bacon"));
		scnr.close();
		connectionUtil.close();

		// NewConnection.connect();
		// System.out.println("Enter your username: ");
		// String username = ReceiveInput.scannerSet();
		// System.out.println("Your username is " + username);
	}
}