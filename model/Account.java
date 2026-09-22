package model;

public abstract class Account {

    private String accountNumber;
    private String pin;
    private double balance;
    private boolean locked;
    private int failedAttempts;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.locked = false;
        this.failedAttempts = 0;
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
    }

    protected void deductBalance(double amount) {
        balance -= amount;
    }

    public abstract void withdraw(double amount) throws Exception;
}