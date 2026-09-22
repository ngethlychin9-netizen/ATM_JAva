package model;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private String accountNumber;
    private String pin;
    private double balance;
    private boolean locked;
    private int failedAttempts;
    private List<Transaction> transactions;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.locked = false;
        this.failedAttempts = 0;
        transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return locked;
    }

    public void lockAccount() {
        locked = true;
    }

    public void deposit(double amount) {

        balance += amount;

        addTransaction(
            new Transaction(
                "Deposit",
                amount,
                balance
            )
        );
    }

    public int getFailedAttempts() {
    return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public void unlockAccount() {
        locked = false;
        failedAttempts = 0;
    }

    protected void deductBalance(double amount) {
        balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public abstract void withdraw(double amount) throws Exception;


}