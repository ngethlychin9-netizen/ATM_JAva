package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime dateTime;

    public Transaction(String type,
                       double amount,
                       double balanceAfter) {

        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
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

    @Override
    public String toString() {

        return type + " | " +
               amount + " | " +
               dateTime + " | " +
               balanceAfter;
    }
}