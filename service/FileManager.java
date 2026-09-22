package service;

import model.Account;
import model.SavingsAccount;
import model.CheckingAccount;

import java.io.*;

public class FileManager {

    private static final String FILE_NAME = "accounts.txt";

    // Save one account
    public void saveAccount(Account account) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_NAME, true))) {

            String type = account instanceof SavingsAccount
                    ? "Savings"
                    : "Checking";

            writer.write(
                    account.getAccountNumber()
                            + ","
                            + account.getPin()
                            + ","
                            + account.getBalance()
                            + ","
                            + type
            );

            writer.newLine();

            System.out.println("Account saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving account.");
        }
    }

    // Load accounts into Bank
    public void loadAccounts(Bank bank) {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                String accountNumber = data[0];
                String pin = data[1];
                double balance =
                        Double.parseDouble(data[2]);
                String type = data[3];

                Account account;

                if (type.equals("Savings")) {

                    account = new SavingsAccount(
                            accountNumber,
                            pin,
                            balance
                    );

                } else {

                    account = new CheckingAccount(
                            accountNumber,
                            pin,
                            balance
                    );
                }

                bank.addAccount(account);
            }

            System.out.println("Accounts loaded successfully.");

        } catch (Exception e) {
            System.out.println("Error loading accounts.");
        }
    }

    // Rewrite all accounts
    public void saveAllAccounts(Bank bank) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_NAME))) {

            // Add a getter in Bank.java:
            // public HashMap<String, Account> getAccounts()

            for (Account account :
                    bank.getAccounts().values()) {

                String type =
                        account instanceof SavingsAccount
                                ? "Savings"
                                : "Checking";

                writer.write(
                        account.getAccountNumber()
                                + ","
                                + account.getPin()
                                + ","
                                + account.getBalance()
                                + ","
                                + type
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }
}