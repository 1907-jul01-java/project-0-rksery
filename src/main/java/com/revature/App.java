package com.revature;

import java.math.BigDecimal;
import java.util.Scanner;

import com.revature.customers.Customer;
import com.revature.customers.CustomerDao;

/**
 * Banking app
 *
 */
public class App {
	private static String title = "Customer";
	private static String username = "undefineduser";
	private static String pw = "undefinedpass";
	private static String firstname = "undefinedfirst";
	private static String middlename = "Not Applicable";
	private static String lastname = "undefinedlast";
	private static BigDecimal balance = new BigDecimal("0");
	private static ConnectionUtil connectionUtil = new ConnectionUtil();
	private static CustomerDao customerDao = new CustomerDao(connectionUtil.getConnection());

	public static void main(String[] args) {
		mainMenu();
		// System.out.println(customerDao.getAll());
		// System.out.println("Kevin Bacon id:" + customerDao.getActorIdByName("Kevin
		// Bacon"));
		// NewConnection.connect();
		// System.out.println("Enter your username: ");
		// String username = ReceiveInput.scannerSet();
		// System.out.println("Your username is " + username);
		connectionUtil.close();
	}

	private static void mainMenu() {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Welcome to THE BANK!\n" + "Please make a selection:\n" + "1. Log in\n"
				+ "2. Register a user account\n");

		switch (scnr.nextInt()) {
		case 1:
			logIn();
			break;
		case 2:
			registerUser();
			break;
		}
		scnr.close();
		return;
	}

	private static void logIn() {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Please enter your user name:");
		username = scnr.nextLine();
		System.out.println("Please enter your password:");
		pw = scnr.nextLine();

		switch (customerDao.authenticateUser(username, pw)) {
		case 1:
			System.out.println("Welcome, valued customer!");
			customerMenu();
			break;
		case 2:
			System.out.println("Welcome, valued employee!");
			employeeMenu();
			break;
		case 3:
			System.out.println("Welcome, valued administrator!");
			adminMenu();
			break;
		default:
			System.out.println("I'm sorry, that user name and password does not match our records.");
			break;
		}

		scnr.close();
		return;
	}

	private static void customerMenu() {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Please make a selection:\n" + "1. Withdraw\n" + "2. Deposit\n" + "3. Transfer funds\n");
		switch (scnr.nextInt()) {
		case 1:
			System.out.println("Please enter the desired withdrawal amount:");
			String newamt = customerDao.withdrawBalance(username, scnr.nextBigDecimal());
			System.out.println(newamt);
			break;
		case 2:
			System.out.println("Please enter the desired deposit amount:");
			newamt = customerDao.depositBalance(username, scnr.nextBigDecimal());
			System.out.println(newamt);
			break;
		case 3:
			System.out.println("Please enter the account number you would like to transfer funds to:");
			int an = scnr.nextInt();
			System.out.println("Please enter the desired transfer amount:");
			BigDecimal amt = scnr.nextBigDecimal();
			System.out.println(customerDao.transferBalance(username, an, amt));
			break;
		}
		scnr.close();
	}

	private static void employeeMenu() {

	}

	private static void adminMenu() {

	}

	private static void registerUser() {
		Scanner scnr = new Scanner(System.in);
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
		balance = (scnr.nextBigDecimal());
		customerDao.insert(new Customer(title, username, pw, firstname, middlename, lastname, balance));
		scnr.close();
		mainMenu();
	}
}