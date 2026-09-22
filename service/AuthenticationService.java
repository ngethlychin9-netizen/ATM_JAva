package service;

import model.Account;

public class AuthenticationService {

    public boolean login(Account account,
                         String enteredPin) {

        if (account == null) {
            return false;
        }

        return account.getPin().equals(enteredPin);
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