package app;

import java.util.Scanner;

public class ATM {

    private Scanner scanner;

    public ATM() {
        scanner = new Scanner(System.in);
    }

    public void start() {

        while (true) {

            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("1. Check Balance");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. Transfer");
                    System.out.println("5. Transaction History");
                    System.out.println("6. Change PIN");
                    System.out.println("7. Logout");
                    break;

                case 2:
                    System.out.println(
                            "Thank you for using ATM.");
                    return;

                default:
                    System.out.println(
                            "Invalid choice.");
            }
        }
    }
}