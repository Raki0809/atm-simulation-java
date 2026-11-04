package model;

import java.util.*;
import java.time.LocalDateTime;
import repository.AccountRepository;
import org.mindrot.jbcrypt.BCrypt;

public class Account{

    private String accountNumber;
    private String pinCode;
    private double accountBalance;
    private boolean isLocked = false;
    private LocalDateTime lockedUntil;
    private Scanner sc;

    public Account(String accountNumber, String pinCode, double accountBalance,boolean isLocked,LocalDateTime lockedUntil,Scanner sc) {
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.accountBalance = accountBalance;
        this.isLocked=isLocked;
        this.lockedUntil=lockedUntil;
        this.sc = sc;
    }
    //=============Transaction History==========================================
    public void viewTransactionHistory() {
        if(!pinCheck()){
            return;
        }
        AccountRepository.getTransactionHistory(accountNumber);
    }
    // ========================= PIN CHECK =========================

    public boolean pinCheck() {

        if (isLocked) {
            if(lockedUntil !=null && LocalDateTime.now().isAfter(lockedUntil)){
                isLocked=false;
                lockedUntil=null;
                AccountRepository.unlockAccount(accountNumber);
                System.out.println("Your Account has been automatically unlocked");
            }
            else{
                System.out.println("Your account is locked");
                System.out.println("Try again after: "+lockedUntil);
                return false;
            }
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

            if (BCrypt.checkpw(enteredPin,pinCode)) {
                System.out.println("PIN verified successfully.");
                return true;
            }

            attempts--;
            System.out.println("Incorrect PIN.");
            System.out.println("Attempts left: " + attempts);
        }

        isLocked = true;
        lockedUntil=LocalDateTime.now().plusHours(24);

        AccountRepository.lockAccount(accountNumber,lockedUntil);

        System.out.println("Your account has been locked for 24 hours");
        System.out.println("Account will be unlocked after: "+lockedUntil);

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

            if (value >= 500) {
                break;
            }

            System.out.println("Amount should be greater than or equal to 500.");

        } catch (InputMismatchException e) {

            System.out.println("Enter a valid amount.");
            sc.nextLine();
        }
    }

    if (!pinCheck()) {
        return;  
    }
    if(AccountRepository.deposit(accountNumber,value)){
        accountBalance += value;

        System.out.println(value + " deposited successfully.");
        System.out.println("Available Balance: " + accountBalance);


    }
    else{
        System.out.println("Deposit Failed");

    }
}

    public boolean isAccountLocked() {
        return isLocked;
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
    if(AccountRepository.withdraw(accountNumber,value)){
    
        accountBalance -= value;

        System.out.println(value + " withdrawn successfully.");
        System.out.println("Available Balance: " + accountBalance);
    }
    else{
        System.out.println("Failed to WithDraw!");
    }
    
}

    // ========================= CHECK BALANCE =========================

    public void checkBalance() {

        if (!pinCheck()) {
            return;
        }

        System.out.println("Available Balance: " + accountBalance);
    }
}