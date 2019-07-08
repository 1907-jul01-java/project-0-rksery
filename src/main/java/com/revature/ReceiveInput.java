package com.revature;

import java.util.Scanner;

public class ReceiveInput {
    public static String scannerSet() {
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        sc.close();
        return (username);
    }
}