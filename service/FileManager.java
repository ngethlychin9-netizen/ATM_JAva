package service;

import java.io.FileWriter;
import java.io.IOException;
import model.Account;

public class FileManager {

    public void saveAccount(Account account) {

        try {

            FileWriter writer =
                    new FileWriter("accounts.txt", true);

            writer.write(
                    account.getAccountNumber()
                    + ","
                    + account.getBalance()
                    + "\n"
            );

            writer.close();

            System.out.println(
                    "Account saved successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving account."
            );
        }
    }
}
