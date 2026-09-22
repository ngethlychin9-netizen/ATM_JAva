package service;

import java.util.HashMap;
import model.Account;

public class Bank {

    private HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void displayAccounts() {
        for (Account account : accounts.values()) {
            System.out.println("Account: " +
                    account.getAccountNumber());
        }
    }
}
