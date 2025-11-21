package com.quickchat;

import java.util.Scanner;

public class LoginConsole {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public LoginConsole() {
    }

    public boolean hasCredentials() {
        return username != null && password != null;
    }

    public String register(Scanner sc) {
        System.out.println("--- Registration ---");
        System.out.print("Enter first name: ");
        this.firstName = sc.nextLine().trim();
        System.out.print("Enter last name: ");
        this.lastName = sc.nextLine().trim();
        System.out.print("Choose a username (must contain '_' and <=5 chars): ");
        String u = sc.nextLine().trim();
        if (!checkUserName(u)) {
            return "Username invalid. Must contain an underscore and be <= 5 characters.";
        }
        System.out.print("Choose a password (min 8 chars, 1 uppercase, 1 number, 1 special): ");
        String p = sc.nextLine();
        if (!checkPasswordComplexity(p)) {
            return "Password invalid. Must be 8+ chars, include uppercase, number and special char.";
        }
        System.out.print("Enter cell number with +27 code (e.g. +27123456789): ");
        String cell = sc.nextLine().trim();
        if (!checkCellPhoneNumber(cell)) {
            return "Cell number invalid. Must start with +27 followed by 9 digits.";
        }
        this.username = u;
        this.password = p;
        return "Registration successful. You can now login.";
    }

    public boolean login(Scanner sc) {
        if (!hasCredentials()) {
            System.out.println("No registered user. Please register first.");
            return false;
        }
        System.out.println("--- Login ---");
        System.out.print("Username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine();
        boolean ok = this.username.equals(u) && this.password.equals(p);
        if (ok) {
            System.out.println("Welcome " + firstName + " " + lastName + ".");
        } else {
            System.out.println("Username or password incorrect.");
        }
        return ok;
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\\:\\{}|<>].*");
        boolean longEnough = password.length() >= 8;
        return hasUpper && hasNumber && hasSpecial && longEnough;
    }

    public boolean checkCellPhoneNumber(String number) {
        return number.matches("\\+27[0-9]{9}");
    }

    // helpers for tests
    public void setCredentials(String username, String password, String fn, String ln) {
        this.username = username; this.password = password; this.firstName = fn; this.lastName = ln;
    }
}
