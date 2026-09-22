package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime dateTime;
    private List<Transaction> transactions;

    public Transaction(String type,
                       double amount,
                       double balanceAfter) {

        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
        transactions = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    private void showHistory(Account account) {

        for(Transaction transaction :
                account.getTransactions()) {

            System.out.println(transaction);
        }
    }

    @Override
    public String toString() {

        return type + " | " +
               amount + " | " +
               dateTime + " | " +
               balanceAfter;
    }
}