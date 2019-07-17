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
		System.out.println("Welcome to THE BANK!\n\n" + "Please make a selection:\n" + "1. Log in\n"
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
		System.out.println("\nPlease enter your user name:");
		username = scnr.nextLine();
		System.out.println("\nPlease enter your password:");
		pw = scnr.nextLine();

		switch (customerDao.authenticateUser(username, pw)) {
		case 1:
			System.out.println("\nWelcome, " + username + "!");
			// System.out.println("Please type the account number of the account you wish to
			// change:");
			// customerDao.getAccountList(username);
			// int an = scnr.nextInt();
			if (customerDao.checkAccountStatus(customerDao.readAccountNumbers(username)) == 2) {
				customerMenu(scnr);
				break;
			} else {
				System.out.println(
						"\nI'm sorry, your account is currently inactive. Please contact a BANK employee for more information.\n");
				mainMenu(scnr);
			}

		case 2:
			System.out.println("\nWelcome, " + username + "!");
			employeeMenu(scnr);
			break;
		case 3:
			System.out.println("\nWelcome, " + username + "!");
			adminMenu(scnr);
			break;
		default:
			System.out.println("\nI'm sorry, that user name and password does not match our records.\n");
			break;
		}

		mainMenu(scnr);
	}

	private static void customerMenu(Scanner scnr) {
		System.out.println("\nPlease make a selection:\n" + "1. Withdraw\n" + "2. Deposit\n" + "3. Transfer funds\n");
		switch (scnr.nextInt()) {
		case 1:
			System.out.println("\nPlease enter the desired withdrawal amount:");
			String newamt = customerDao.withdrawBalance(username, (scnr.nextBigDecimal()).abs());
			System.out.println();
			System.out.println(newamt);
			System.out.println();
			break;
		case 2:
			System.out.println("\nPlease enter the desired deposit amount:");
			newamt = customerDao.depositBalance(username, (scnr.nextBigDecimal()).abs());
			System.out.println();
			System.out.println(newamt);
			System.out.println();

			break;
		case 3:
			System.out.println("\nPlease enter the account number you would like to transfer funds to:");
			int an = scnr.nextInt();
			System.out.println("\nPlease enter the desired transfer amount:");
			BigDecimal amt = (scnr.nextBigDecimal()).abs();
			System.out.println();
			System.out.println(customerDao.transferBalance(username, an, amt));
			System.out.println();
			break;
		}
		customerMenu(scnr);
	}

	private static void employeeMenu(Scanner scnr) {
		System.out
				.println("\nPlease make a selection:\n" + "1. View Account Details\n" + "2. View Open Applications\n");
		switch (scnr.nextInt()) {
		case 1:
			// accounts information, balances, personal information
			System.out.println("Please enter the customer's account number:");
			int an = scnr.nextInt();
			System.out.println();
			System.out.println(customerDao.getAccountInfo(an));
			System.out.println();
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
			System.out.println("\nPlease enter the customer's account number:");
			an = scnr.nextInt();
			System.out.println();
			System.out.println(customerDao.getAccountInfo(an));
			System.out.println();
			customerDao.getAccountInfo(an);
			break;
		case 2:
			customerDao.approveDenyApplications(scnr);
			System.out.println();
			break;
		case 3:
			scnr.nextLine();
			System.out.println("\n\nPlease enter the user name of the account you wish to withdraw from:");
			username = scnr.nextLine();
			System.out.println("\nPlease enter the desired withdrawal amount:");
			String newamt = customerDao.withdrawBalance(username, (scnr.nextBigDecimal()).abs());
			System.out.println();
			System.out.println(newamt);
			System.out.println();
			break;
		case 4:
			scnr.nextLine();
			System.out.println("\nPlease enter the user name of the account you wish to deposit into:");
			username = scnr.nextLine();
			System.out.println("\nPlease enter the desired deposit amount:");
			newamt = customerDao.depositBalance(username, (scnr.nextBigDecimal()).abs());
			System.out.println();
			System.out.println(newamt);
			System.out.println();
			break;
		case 5:
			scnr.nextLine();
			System.out.println("\nPlease enter the account number you wish to transfer funds FROM:");
			int an1 = scnr.nextInt();
			System.out.println("\nPlease enter the account number you would like to transfer funds TO:");
			int an2 = scnr.nextInt();
			System.out.println("\nPlease enter the desired transfer amount:");
			BigDecimal amt = (scnr.nextBigDecimal()).abs());
			System.out.println();
			System.out.println(customerDao.transferBalance(an1, an2, amt));
			System.out.println();
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
		scnr.nextLine();
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
		// System.out.println("Please enter an initial deposit and press enter.");
		balance = (BigDecimal.ZERO);
		customerDao.insert(new Customer(title, username, pw, firstname, middlename, lastname, balance));
		System.out.println("Would you like to create another user for this account?\n1. Yes\n2. No\n");
		if (scnr.nextInt() == 1) {
			scnr.nextLine();
			System.out.println("Please enter a username and press enter.");
			String un = scnr.nextLine();
			// System.out.println(username);
			System.out.println("Please enter a password and press enter.");
			pw = scnr.nextLine();
			System.out.println("Please enter your first name and press enter.");
			firstname = scnr.nextLine();
			System.out.println("Please enter your middle name and press enter.");
			middlename = scnr.nextLine();
			System.out.println("Please enter your last name and press enter.");
			lastname = scnr.nextLine();
			// System.out.println("Please enter an initial deposit and press enter.");
			balance = (BigDecimal.ZERO);
			int accountnumber = customerDao.readAccountNumbers(username);
			System.out.println("Using account number: " + accountnumber);
			customerDao.insert(new Customer(title, accountnumber, un, pw, firstname, middlename, lastname, balance));
		}
		mainMenu(scnr);
	}
}