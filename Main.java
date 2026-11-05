import java.util.*;
import repository.AccountRepository;
import java.time.LocalDateTime;
import model.Account;
import java.math.BigDecimal;

class Main {

    static Scanner sc = new Scanner(System.in);

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

            if (AccountRepository.accountExists(accountNumber)) {

                System.out.println("Account Found!");
                Account account = AccountRepository.getAccount(accountNumber, sc);

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

            if (AccountRepository.accountExists(accountNumber)){
                System.out.println("Account already exists!");
                System.out.println("Please try again with a different account number.");
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


        BigDecimal accountBalance;

        while (true) {

            System.out.print("Enter Initial Balance: ");

            try {

                accountBalance = new BigDecimal(sc.nextLine());

                if (accountBalance.compareTo(new BigDecimal("2000"))<0) {

                    System.out.println("Initial balance should be greater than or equal to 2000.");

                    continue;
                }

                break;

            } catch (InputMismatchException e) {

                System.out.println("Enter a valid amount.");
                sc.nextLine();
            }
        }


        if (AccountRepository.saveAccount(
            accountNumber, pinCode, accountBalance, false)) {
            System.out.println("Account created successfully!");
            } 
        else {
                System.out.println("Account creation failed!");
            }
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
            if (account.isAccountLocked()) {
                System.out.println("Your account has been locked.");
                System.out.println("Exiting ATM...");
                return;
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