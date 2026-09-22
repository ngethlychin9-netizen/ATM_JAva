package service;

import model.Account;

public class AuthenticationService {

    public boolean login(Account account, String enteredPin) {

        if (account == null) {
            return false;
        }

        if (account.isLocked()) {
            System.out.println("Account is locked.");
            return false;
        }

        if (account.getPin().equals(enteredPin)) {
            account.setFailedAttempts(0);
            return true;
        }

        account.setFailedAttempts(
                account.getFailedAttempts() + 1);

        if (account.getFailedAttempts() >= 3) {
            account.lockAccount();
            System.out.println("Account locked!");
        }

        return false;
    }

    public boolean changePin(Account account,
                             String oldPin,
                             String newPin) {

        if (account.getPin().equals(oldPin)) {
            account.setPin(newPin);
            return true;
        }

        return false;
    }
}