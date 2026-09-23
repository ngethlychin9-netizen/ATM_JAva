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

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PIN = "admin123";

    private Bank bank;
    private AuthenticationService auth;
    private Account currentAccount;
    private JFrame frame;

    public ATM() {
        bank = new Bank();
        auth = new AuthenticationService();
        bank.addAccount(new SavingsAccount("1001", "1234", 500));
        bank.addAccount(new CheckingAccount("1002", "4321", 300));
        bank.addAccount(new CheckingAccount("1003", "5432", 200));
    }

    public void start() {
        SwingUtilities.invokeLater(this::showLogin);
    }

    private void showLogin() {
        String[] roles = {"User Login", "Admin Login", "Exit"};
        int role = JOptionPane.showOptionDialog(null,
                "Select how you want to sign in.", "ATM Login",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, roles, roles[0]);
        if (role == 0) {
            showUserLogin();
        } else if (role == 1) {
            showAdminLogin();
        }
    }

    private void showUserLogin() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField accountField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        panel.add(new JLabel("Account number:"));
        panel.add(accountField);
        panel.add(new JLabel("PIN:"));
        panel.add(pinField);

        int result = JOptionPane.showConfirmDialog(null, panel, "User Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            showLogin();
            return;
        }

        Account account = bank.findAccount(accountField.getText().trim());
        String pin = new String(pinField.getPassword());
        if (!auth.login(account, pin)) {
            showError("Invalid account number or PIN.");
            showUserLogin();
            return;
        }

        currentAccount = account;
        showMenu();
    }

    private void showAdminLogin() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        panel.add(new JLabel("Admin username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Admin PIN:"));
        panel.add(pinField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Admin Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            showLogin();
            return;
        }

        String username = usernameField.getText().trim();
        String pin = new String(pinField.getPassword());
        if (ADMIN_USERNAME.equals(username) && ADMIN_PIN.equals(pin)) {
            showAdminMenu();
        } else {
            showError("Invalid admin username or PIN.");
            showAdminLogin();
        }
    }

    private void showMenu() {
        frame = new JFrame("ATM - " + currentAccount.getAccountNumber());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 300);
        frame.setLocationRelativeTo(null);

        JLabel accountTypeLabel = new JLabel(
            "Account type: " + getAccountType(currentAccount),
            JLabel.CENTER);
        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        addButton(buttons, "Check balance", this::showBalance);
        addButton(buttons, "Deposit", this::deposit);
        addButton(buttons, "Withdraw", this::withdraw);
        addButton(buttons, "Transfer", this::transfer);
        addButton(buttons, "Transaction history", this::showHistory);
        addButton(buttons, "Change PIN", this::changePin);
        addButton(buttons, "Logout", this::logout);
        frame.add(accountTypeLabel, BorderLayout.NORTH);
        frame.add(buttons, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String getAccountType(Account account) {
        return account instanceof SavingsAccount ? "SAVINGS" : "CHECKING";
    }

    private void showAdminMenu() {
        frame = new JFrame("ATM Administration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 220);
        frame.setLocationRelativeTo(null);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        addButton(buttons, "Create user account", this::createAccount);
        addButton(buttons, "View all accounts", this::showAllAccounts);
        addButton(buttons, "Lock or unlock account", this::toggleAccountLock);
        addButton(buttons, "Admin logout", this::logout);
        frame.add(buttons, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createAccount() {
        String accountNumber = generateNextAccountNumber();
        JPasswordField pinField = new JPasswordField();
        JTextField balanceField = new JTextField();
        String[] accountTypes = {"Savings", "Checking"};
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Generated account number: " + accountNumber));
        panel.add(new JLabel("PIN:"));
        panel.add(pinField);
        panel.add(new JLabel("Starting balance:"));
        panel.add(balanceField);
        panel.add(new JLabel("Account type:"));
        javax.swing.JComboBox<String> typeField = new javax.swing.JComboBox<>(accountTypes);
        panel.add(typeField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Create User Account",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String pin = new String(pinField.getPassword());
        try {
            double balance = parseNonNegativeAmount(balanceField.getText());
            if (pin.isEmpty()) {
                throw new IllegalArgumentException("PIN is required.");
            }

            Account account = "Savings".equals(typeField.getSelectedItem())
                    ? new SavingsAccount(accountNumber, pin, balance)
                    : new CheckingAccount(accountNumber, pin, balance);
            bank.addAccount(account);
            showMessage("User account created successfully.");
        } catch (NumberFormatException exception) {
            showError("Starting balance must be a valid number.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private String generateNextAccountNumber() {
        int highestAccountNumber = 100;
        for (String accountNumber : bank.getAccounts().keySet()) {
            try {
                highestAccountNumber = Math.max(highestAccountNumber,
                        Integer.parseInt(accountNumber));
            } catch (NumberFormatException exception) {
            }
        }
        return String.valueOf(highestAccountNumber + 1);
    }

    private void showAllAccounts() {
        String accounts = bank.getAccounts().values().stream()
                .sorted((first, second) -> first.getAccountNumber()
                        .compareTo(second.getAccountNumber()))
                .map(account -> String.format("%s | %s | Balance: $%.2f | %s",
                        account.getAccountNumber(),
                    getAccountType(account),
                        account.getBalance(),
                        account.isLocked() ? "Locked" : "Active"))
                .collect(Collectors.joining("\n"));
        showMessage(accounts.isEmpty() ? "No user accounts found." : accounts);
    }

    private void toggleAccountLock() {
        String accountNumber = JOptionPane.showInputDialog(frame,
                "Enter the user account number:");
        if (accountNumber == null) {
            return;
        }

        Account account = bank.findAccount(accountNumber.trim());
        if (account == null) {
            showError("Account was not found.");
            return;
        }
        if (account.isLocked()) {
            account.unlockAccount();
            showMessage("Account unlocked successfully.");
        } else {
            account.lockAccount();
            showMessage("Account locked successfully.");
        }
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

    private double parseNonNegativeAmount(String input) {
        double amount = Double.parseDouble(input.trim());
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Starting balance cannot be negative.");
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