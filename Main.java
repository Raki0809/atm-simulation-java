import java.util.*;
import java.time.LocalDateTime;

class Account {

    private String accountNumber;
    private String pinCode;
    private double accountBalance;
    private boolean isLocked = false;
    private Scanner sc;
    
    private ArrayList<String> transactionHistory = new ArrayList<>();

    public Account(String accountNumber, String pinCode, double accountBalance, Scanner sc) {
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.accountBalance = accountBalance;
        this.sc = sc;
    }
    //=============Transaction History==========================================
    public void viewTransactionHistory() {

    if (transactionHistory.isEmpty()) {
        System.out.println("No transactions available.");
        return;
    }

    System.out.println("====== Transaction History ======");

    for (String transaction : transactionHistory) {
        System.out.println(transaction);
    }
}
    // ========================= PIN CHECK =========================

    public boolean pinCheck() {

        if (isLocked) {
            System.out.println("Your account is locked.");
            return false;
        }

        int attempts = 3;

        while (attempts > 0) {

            System.out.print("Enter PIN: ");
            String enteredPin = sc.nextLine();

            if (!enteredPin.matches("\\d{6}")) {
                attempts--;
                System.out.println("PIN must contain 6 digits.");
                System.out.println("Attempts left: " + attempts);
                continue;
            }

            if (enteredPin.equals(pinCode)) {
                System.out.println("PIN verified successfully.");
                return true;
            }

            attempts--;
            System.out.println("Incorrect PIN.");
            System.out.println("Attempts left: " + attempts);
        }

        isLocked = true;
        System.out.println("Your account has been locked due to multiple incorrect attempts.");

        return false;
    }

    // ========================= DEPOSIT =========================

    public void deposit() {

    System.out.print("Enter amount to deposit: ");

    double value;

    while (true) {

        try {
            value = sc.nextDouble();
            sc.nextLine();

            if (value >= 2000) {
                break;
            }

            System.out.println("Amount should be greater than or equal to 2000.");

        } catch (InputMismatchException e) {

            System.out.println("Enter a valid amount.");
            sc.nextLine();
        }
    }

    if (!pinCheck()) {
        return;   // amount discarded, nothing recorded
    }

    accountBalance += value;

    System.out.println(value + " deposited successfully.");
    System.out.println("Available Balance: " + accountBalance);

    transactionHistory.add("Deposited: " + value + " | Balance: " + accountBalance + " | Time: " + LocalDateTime.now());
}

    // ========================= WITHDRAW =========================

    public void withdraw() {

    System.out.print("Enter amount to withdraw: ");

    double value;

    while (true) {

        try {
            value = sc.nextDouble();
            sc.nextLine();

            if (value < 500) {
                System.out.println("Minimum withdrawal amount is 500.");
                continue;
            }

            if (value > accountBalance) {
                System.out.println("Insufficient balance.");
                continue;
            }

            break;

        } catch (InputMismatchException e) {

            System.out.println("Enter a valid amount.");
            sc.nextLine();
        }
    }

    if (!pinCheck()) {
        return;
    }

    accountBalance -= value;

    System.out.println(value + " withdrawn successfully.");
    System.out.println("Available Balance: " + accountBalance);

    transactionHistory.add("Withdrawn: " + value + " | Balance: " + accountBalance + " | Time: " + LocalDateTime.now());
}

    // ========================= CHECK BALANCE =========================

    public void checkBalance() {

        if (!pinCheck()) {
            return;
        }

        System.out.println("Available Balance: " + accountBalance);
    }
}


class Main {

    static Scanner sc = new Scanner(System.in);

    static HashMap<String, Account> accounts = new HashMap<>();

    // ========================= EXISTING ACCOUNT =========================

    static void existingAccount() {

        int attempts = 5;

        while (attempts > 0) {

            System.out.print("Enter Account Number: ");
            String accountNumber = sc.nextLine();

            if (!accountNumber.matches("\\d{11}")) {

                attempts--;

                System.out.println("Account Number must contain 11 digits.");
                System.out.println("Attempts left: " + attempts);

                continue;
            }

            if (accounts.containsKey(accountNumber)) {

                System.out.println("Account Found!");
                Account account = accounts.get(accountNumber);
                atmMenu(account);

                return;
            }

            attempts--;

            System.out.println("Account does not exist.");
            System.out.println("Attempts left: " + attempts);
        }

        System.out.println("You have exceeded the Account Number attempts.");
    }


    // ========================= CREATE ACCOUNT =========================

    static void createAccount() {

        String accountNumber;

        while (true) {

            System.out.print("Create Account Number: ");
            accountNumber = sc.nextLine();

            if (!accountNumber.matches("\\d{11}")) {

                System.out.println("Account Number must contain 11 digits.");
                continue;
            }

            if (accounts.containsKey(accountNumber)) {

                System.out.println("Account already exists.");
                System.out.println("Please enter another Account Number.");
                continue;
            }

            break;
        }


        String pinCode;

        while (true) {

            System.out.print("Create 6-digit PIN: ");
            pinCode = sc.nextLine();

            if (!pinCode.matches("\\d{6}")) {

                System.out.println("PIN must contain exactly 6 digits.");
                continue;
            }

            break;
        }


        double accountBalance;

        while (true) {

            System.out.print("Enter Initial Balance: ");

            try {

                accountBalance = sc.nextDouble();
                sc.nextLine();

                if (accountBalance < 2000) {

                    System.out.println("Initial balance should be greater than or equal to 2000.");

                    continue;
                }

                break;

            } catch (InputMismatchException e) {

                System.out.println("Enter a valid amount.");
                sc.nextLine();
            }
        }


        Account account =
            new Account(accountNumber, pinCode, accountBalance, sc);

        accounts.put(accountNumber, account);

        System.out.println("Account created successfully!");
    }


    // ========================= ATM MENU =========================

    static void atmMenu(Account account) {

        int choice;

        do {

            System.out.println();
            System.out.println("========== ATM MENU ==========");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");

            try {

                choice = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {

                System.out.println("Enter a valid choice.");
                sc.nextLine();
                choice = 0;
            }


            switch (choice) {

                case 1:
                    account.deposit();
                    break;

                case 2:
                    account.withdraw();
                    break;

                case 3:
                    account.checkBalance();
                    break;
                case 4:
                    account.viewTransactionHistory();
                    break;
                    
                case 5:
                    System.out.println("Returning to main menu.");
                    break;

                default:
                    System.out.println("Please enter a valid choice.");
            }

        } while (choice != 5);
    }


    // ========================= MAIN =========================

    public static void main(String[] args) {

        int choice;

        do {

            System.out.println();
            System.out.println("========== ATM ==========");
            System.out.println("1. Existing Account");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");

            try {

                choice = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {

                System.out.println("Enter a valid choice.");
                sc.nextLine();
                choice = 0;
            }


            switch (choice) {

                case 1:
                    existingAccount();
                    break;

                case 2:
                    createAccount();
                    break;

                case 3:
                    System.out.println("Thank you for using the ATM.");
                    break;

                default:
                    System.out.println("Please enter a valid choice.");
            }

        } while (choice != 3);
    }
}