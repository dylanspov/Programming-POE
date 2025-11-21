package com.quickchat;

import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LoginConsole login = new LoginConsole();

        while (true) {
            System.out.println("\n--- QuickChat Part 1: Register / Login ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String choice = sc.nextLine().trim();
            if (choice.equals("1")) {
                String res = login.register(sc);
                System.out.println(res);
            } else if (choice.equals("2")) {
                boolean ok = login.login(sc);
                if (ok) {
                    // move to GUI (Parts 2 & 3)
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            QuickChatGUI qc = new QuickChatGUI();
                            qc.loadStoredMessages();
                            qc.showMainMenu();
                            System.exit(0);
                        }
                    });
                    break;
                }
            } else if (choice.equals("0")) {
                System.out.println("Goodbye."); break;
            } else {
                System.out.println("Invalid choice."); 
            }
        }

        sc.close();
    }
}
