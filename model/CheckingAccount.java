package model;

import exception.InsufficientFundsException;

public class CheckingAccount extends Account {

    private final double DAILY_LIMIT = 1000;

    public CheckingAccount(String accountNumber,
                           String pin,
                           double balance) {
        super(accountNumber, pin, balance);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException {

        if(amount > DAILY_LIMIT) {
            throw new InsufficientFundsException(
                    "Daily limit exceeded");
        }

        if(amount > getBalance()) {
            throw new InsufficientFundsException(
                    "Insufficient Balance");
        }

        deductBalance(amount);
    }
}
