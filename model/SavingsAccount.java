package model;

import exception.InsufficientFundsException;
import model.Transaction;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber,
                          String pin,
                          double balance) {
        super(accountNumber, pin, balance);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException {

        if(amount > getBalance()) {
            throw new InsufficientFundsException(
                    "Insufficient Balance");
        }

        deductBalance(amount);

        addTransaction(
        new Transaction(
            "Withdraw",
            amount,
            getBalance()
        )
    );
    }
}