package app;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Account;
import model.CheckingAccount;
import model.SavingsAccount;
import service.AuthenticationService;
import service.Bank;

public class ATM {

    private Bank bank;
    private AuthenticationService auth;
    private Account currentAccount;
    private JFrame frame;

    public ATM() {
        bank = new Bank();
        auth = new AuthenticationService();
        bank.addAccount(new SavingsAccount("1001", "1234", 5000));
        bank.addAccount(new CheckingAccount("1002", "4321", 3000));
    }

    public void start() {
        SwingUtilities.invokeLater(this::showLogin);
    }

    private void showLogin() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField accountField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        panel.add(new JLabel("Account number:"));
        panel.add(accountField);
        panel.add(new JLabel("PIN:"));
        panel.add(pinField);

        int result = JOptionPane.showConfirmDialog(null, panel, "ATM Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Account account = bank.findAccount(accountField.getText().trim());
        String pin = new String(pinField.getPassword());
        if (!auth.login(account, pin)) {
            showError("Invalid account number or PIN.");
            showLogin();
            return;
        }

        currentAccount = account;
        showMenu();
    }

    private void showMenu() {
        frame = new JFrame("ATM - " + currentAccount.getAccountNumber());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 300);
        frame.setLocationRelativeTo(null);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        addButton(buttons, "Check balance", this::showBalance);
        addButton(buttons, "Deposit", this::deposit);
        addButton(buttons, "Withdraw", this::withdraw);
        addButton(buttons, "Transfer", this::transfer);
        addButton(buttons, "Transaction history", this::showHistory);
        addButton(buttons, "Change PIN", this::changePin);
        addButton(buttons, "Logout", this::logout);
        frame.add(buttons, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(event -> action.run());
        panel.add(button);
    }

    private void showBalance() {
        showMessage(String.format("Current balance: $%.2f", currentAccount.getBalance()));
    }

    private void deposit() {
        Double amount = readAmount("Deposit amount:");
        if (amount != null) {
            currentAccount.deposit(amount);
            showMessage(String.format("Deposit successful. Balance: $%.2f", currentAccount.getBalance()));
        }
    }

    private void withdraw() {
        Double amount = readAmount("Withdrawal amount:");
        if (amount == null) {
            return;
        }
        try {
            currentAccount.withdraw(amount);
            showMessage(String.format("Withdrawal successful. Balance: $%.2f", currentAccount.getBalance()));
        } catch (Exception exception) {
            showError(exception.getMessage());
        }
    }

    private void transfer() {
        JTextField accountField = new JTextField();
        JTextField amountField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Destination account:"));
        panel.add(accountField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        int result = JOptionPane.showConfirmDialog(frame, panel, "Transfer",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            double amount = parsePositiveAmount(amountField.getText());
            String destination = accountField.getText().trim();
            if (destination.equals(currentAccount.getAccountNumber())) {
                throw new IllegalArgumentException("Destination must be another account.");
            }
            if (!bank.transfer(currentAccount.getAccountNumber(), destination, amount)) {
                throw new IllegalArgumentException("Destination account was not found.");
            }
            showMessage(String.format("Transfer successful. Balance: $%.2f", currentAccount.getBalance()));
        } catch (Exception exception) {
            showError(exception.getMessage());
        }
    }

    private void showHistory() {
        String history = currentAccount.getTransactions().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        showMessage(history.isEmpty() ? "No transactions yet." : history);
    }

    private void changePin() {
        JPasswordField oldPin = new JPasswordField();
        JPasswordField newPin = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Current PIN:"));
        panel.add(oldPin);
        panel.add(new JLabel("New PIN:"));
        panel.add(newPin);
        int result = JOptionPane.showConfirmDialog(frame, panel, "Change PIN",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (auth.changePin(currentAccount, new String(oldPin.getPassword()),
                    new String(newPin.getPassword()))) {
                showMessage("PIN changed successfully.");
            } else {
                showError("Current PIN is incorrect.");
            }
        }
    }

    private void logout() {
        currentAccount = null;
        frame.dispose();
        showLogin();
    }

    private Double readAmount(String prompt) {
        String input = JOptionPane.showInputDialog(frame, prompt);
        if (input == null) {
            return null;
        }
        try {
            return parsePositiveAmount(input);
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
            return null;
        }
    }

    private double parsePositiveAmount(String input) {
        double amount = Double.parseDouble(input.trim());
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        return amount;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "ATM", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "ATM Error", JOptionPane.ERROR_MESSAGE);
    }
}