# ATM Java Application

A desktop ATM application built with Java and Swing. The project demonstrates object-oriented design with account models, authentication, banking operations, custom exceptions, and a graphical user interface.

## Features

- Account login with account number and PIN
- Balance inquiry
- Deposit money
- Withdraw money
- Transfer money between accounts
- Transaction history
- Change PIN
- Logout
- Account locking after three failed login attempts
- Different withdrawal rules for savings and checking accounts
- Admin account creation and account management

## Requirements

- Java Development Kit (JDK) 8 or newer
- Windows, macOS, or Linux

## Project Structure

```text
app/
  ATM.java
  Main.java
exception/
  AccountNotFoundException.java
  InsufficientFundsException.java
  InvalidPinException.java
model/
  Account.java
  Card.java
  CheckingAccount.java
  SavingsAccount.java
  Transaction.java
  User.java
service/
  AuthenticationService.java
  Bank.java
  FileManager.java
```

## Run the Application

Open a terminal in the project root, the folder containing `app`, `model`, `service`, and `exception`, then compile all source files:

### PowerShell

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp out app.Main
```

### macOS or Linux

```bash
mkdir -p out
javac -d out $(find . -name "*.java")
java -cp out app.Main
```

Do not run `javac Main.java` from inside the `app` folder because the application uses Java packages and imports classes from the other folders.

When the application starts, choose **User Login** to access a customer account or **Admin Login** to open the administration menu.

## Demo Login Accounts

These accounts are included in the application for demonstration purposes:

| Account number | PIN  | Account type | Starting balance |
|---|---|---|---:|
| `1001` | `1234` | Savings | 500 |
| `1002` | `4321` | Checking | 300 |
| `1003` | `5432` | Checking | 200 |

The account number acts as the username. These credentials are for local demonstration only and should not be used in a production system.

## Admin Login

Use the following demo credentials to open the administration menu:

- Username: `admin`
- PIN: `admin123`

The admin can:

1. Create new savings or checking accounts with a starting balance. Account numbers are generated automatically in sequence.
2. View all accounts, balances, account types, and lock status.
3. Lock or unlock a user account.

Account changes are held in memory while the application is running.

## Account Rules

- Deposit and withdrawal amounts must be greater than zero.
- Savings accounts cannot withdraw more than their available balance.
- Checking accounts have a withdrawal limit of `1000` per transaction.
- Transfers require a valid destination account and cannot be made to the same account.
- Wrong account numbers and PINs count as failed user login attempts.
- After three failed user login attempts, the ATM displays a message and exits.
- An account is locked after three incorrect PIN attempts for that account.

## Technologies

- Java
- Java Swing
- Object-oriented programming
- Custom checked exceptions
- Java collections and streams
