package app;

import java.util.Scanner;

public class ATM {

    Scanner sc = new Scanner(System.in);

    public void start() {

        while(true) {

            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:
                    System.out.println("Login Function");
                    break;

                case 2:
                    System.out.println("Good Bye");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}