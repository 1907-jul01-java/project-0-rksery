package com.revature;

/**
 * Banking app
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Enter your username: ");
		String username = ReceiveInput.scannerSet();
		System.out.println("Your username is " + username);
	}
}
