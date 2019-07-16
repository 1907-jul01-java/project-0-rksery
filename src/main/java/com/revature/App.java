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
		Scanner scnr = new Scanner(System.in);
		mainMenu(scnr);
		// System.out.println(customerDao.getAll());
		// System.out.println("Kevin Bacon id:" + customerDao.getActorIdByName("Kevin
		// Bacon"));
		// NewConnection.connect();
		// System.out.println("Enter your username: ");
		// String username = ReceiveInput.scannerSet();
		// System.out.println("Your username is " + username);
		scnr.close();
		connectionUtil.close();
	}

	private static void mainMenu(Scanner scnr) {
		System.out.println("Welcome to THE BANK!\n" + "Please make a selection:\n" + "1. Log in\n"
				+ "2. Register a user account\n");
		switch (scnr.nextInt()) {
		case 1:
			logIn(scnr);
			break;
		case 2:
			registerUser(scnr);
			break;
		}
		mainMenu(scnr);
	}

	private static void logIn(Scanner scnr) {
		scnr.nextLine();
		System.out.println("Please enter your user name:");
		username = scnr.nextLine();
		System.out.println("Please enter your password:");
		pw = scnr.nextLine();

		switch (customerDao.authenticateUser(username, pw)) {
		case 1:
			System.out.println("\nWelcome, valued customer!");
			customerMenu(scnr);
			break;
		case 2:
			System.out.println("\nWelcome, valued employee!");
			employeeMenu(scnr);
			break;
		case 3:
			System.out.println("\nWelcome, valued administrator!");
			adminMenu(scnr);
			break;
		default:
			System.out.println("\nI'm sorry, that user name and password does not match our records.");
			break;
		}

		mainMenu(scnr);
	}

	private static void customerMenu(Scanner scnr) {
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
		customerMenu(scnr);
	}

	private static void employeeMenu(Scanner scnr) {
		System.out.println("Please make a selection:\n" + "1. View Account Details\n" + "2. View Open Applications\n");
		switch (scnr.nextInt()) {
		case 1:
			// accounts information, balances, personal information
			System.out.println("Please enter the customer's account number:");
			int an = scnr.nextInt();
			System.out.println(customerDao.getAccountInfo(an));
			customerDao.getAccountInfo(an);
			break;
		case 2:
			// approve/deny customer applications
			customerDao.approveDenyApplications(scnr);
			System.out.println();
			break;
		default:
			System.out.println("I'm sorry, that was an invalid option.\n");
			break;
		}
		employeeMenu(scnr);
	}

	private static void adminMenu(Scanner scnr) {
		int an;
		System.out.println("Please make a selection:\n" + "1. View Account Details\n" + "2. View Open Applications\n"
				+ "3. Withdraw\n" + "4. Deposit\n" + "5. Transfer\n" + "6. Cancel Account");
		// accounts information, balances, personal information
		switch (scnr.nextInt()) {
		case 1:
			System.out.println("Please enter the customer's account number:");
			an = scnr.nextInt();
			System.out.println(customerDao.getAccountInfo(an));
			customerDao.getAccountInfo(an);
			break;
		case 2:
			customerDao.approveDenyApplications(scnr);
			System.out.println();
			break;
		case 3:
			scnr.nextLine();
			System.out.println("Please enter the user name of the account you wish to withdraw from:");
			username = scnr.nextLine();
			System.out.println("Please enter the desired withdrawal amount:");
			String newamt = customerDao.withdrawBalance(username, scnr.nextBigDecimal());
			System.out.println(newamt);
			break;
		case 4:
			scnr.nextLine();
			System.out.println("Please enter the user name of the account you wish to deposit into:");
			username = scnr.nextLine();
			System.out.println("Please enter the desired deposit amount:");
			newamt = customerDao.depositBalance(username, scnr.nextBigDecimal());
			System.out.println(newamt);
			break;
		case 5:
			scnr.nextLine();
			System.out.println("Please enter the account you wish to transfer funds FROM:");
			username = scnr.nextLine();
			System.out.println("Please enter the account number you would like to transfer funds TO:");
			an = scnr.nextInt();
			System.out.println("Please enter the desired transfer amount:");
			BigDecimal amt = scnr.nextBigDecimal();
			System.out.println(customerDao.transferBalance(username, an, amt));
			break;
		case 6:
			customerDao.cancelAccount(scnr);
			break;
		default:
			System.out.println("I'm sorry, that was an invalid option.");
			break;
		}
		// approve/deny customer applications
		// withdrawing, depositing, transferring from all accounts
		// cancel accounts
		adminMenu(scnr);
	}

	// separate account & customer registration
	private static void registerUser(Scanner scnr) {
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
		mainMenu(scnr);
	}
}