import java.util.*;
class Simulation{
    public static void main(String[]args){
        Scanner sc=new Scanner(System.in);
        
        //-----------------Asking for accnum-----------------------------
        String accountNumber;
        while(true){
            System.out.print("Enter the AccountNumber: ");
            accountNumber=sc.nextLine();
            
            if(accountNumber.matches("\\d{11}")){
                break;
            }
            System.out.println("Enter a valid 11 digit Account Number.");
        }
        
        
        
        //----------------------asking for pincode;-------------------------
        String pinCode;
        while(true){
            System.out.println("Set the pinCode: ");
            pinCode=sc.nextLine();
            
            if(pinCode.matches("\\d{6}")){
                break;
            }
            System.out.println("Enter a Valid 6 Digit Pin Code");
        }
        
        //---------------------intilizeing the accountBalance-----------------------
        System.out.println("Enter the Initial Balance: ");
        double accountBalance;
        while(true){
            try{
                accountBalance=sc.nextDouble();
                sc.nextLine();
                if(accountBalance>0)
                break;
                System.out.println("Account Balance Sholud be Greater than 0");
            }
        
            catch(InputMismatchException e){
                System.out.println("Enter the Invalid Input.");
                accountBalance=0.00;
                sc.nextLine();
            
            }
        }
        Account account=new Account(accountNumber,pinCode,accountBalance,sc);
        
        
        //-------------------Atm Menu--------------------------
        int choice;
        do{
            System.out.println("=======Menu=======");
            System.out.println("press 1 to Deposit ");
            System.out.println("press 2 to WithDraw ");
            System.out.println("press 3 to CheckBalance ");
            System.out.println("Press 4 to exit ATM");
            
            try{
                choice=sc.nextInt();
                sc.nextLine();
                
            }
            catch(InputMismatchException e){
                System.out.println("Invalid Input Please Enter a Number");
                sc.nextLine();
                choice =0;
                continue;
            }
            
            switch(choice){
                case 1:
                    account.deposit();
                    break;
                case 2:
                    account.withDraw();
                    break;
                case 3:
                    account.checkBalance();
                    break;
                case 4:
                    System.out.println("Thank You for Using Atm.");
                    break;
                default:
                    System.out.println("Invalid Input");
            }
            
        }while(choice !=4);
    }
}

//------------------class of Accounts---------------------------------------

class Account{
    private String accountNumber;
    private String pinCode;
    private double accountBalance;
    private Scanner sc;
    private boolean isLocked=false;
    
    public Account(String accountNumber,String pinCode,double accountBalance,Scanner sc){
        this.accountNumber=accountNumber;
        this.pinCode=pinCode;
        this.accountBalance=accountBalance;
        this.sc=sc;
    }
    //--------------------checking the pin Code--------------------------------
    public boolean pinCheck(){
        
        if(isLocked){
            System.out.println("Your Account has been Locked Due to Multiple attempts");
            return false;
        }
        
        
        int n=3;
        while(n>0){
            System.out.println("Enter the PinCode: ");
            String pin;
            while(true){
                pin=sc.nextLine();
                if(pin.matches("\\d{6}"))break;
                System.out.println("Enter a valid 6 digit pin.");
            }
            
            if(pin.equals(pinCode)){
                return true;
            }
            
            n--;
            if(n!=0)
            System.out.println("Incorrect Pin. "+n+" chance left");
        }
        System.out.println("You Have Exceeded the Limit");
        isLocked=true;
        return false;
        
    }
    //---------------------deposit--------------------------
    public void deposit(){
        
        
        boolean flag=pinCheck();
        
        if(flag){
            System.out.println("Enter the Deposit value: ");
            double value;
            while(true){
                try{
                    value=sc.nextDouble();
                    sc.nextLine();
                    if(value>0)
                    break;
                    System.out.println("Amount must be greater than 0");
                }
                catch(InputMismatchException e){
                    System.out.println("Please Enter Valid Amount");
                    sc.nextLine();
                    value=0;
                }
                
            }
            if(value>0){
                accountBalance+=value;
                System.out.println(value+" has been Deposited Successfully");
                System.out.println("The Available balance: "+accountBalance);
                System.out.println();
                
            
            }
            
        }
        
        
    }
    //----------------------Withdrawing--------------------------------
    public void withDraw(){
        
        boolean flag=pinCheck();
        
        if(flag){
            System.out.println("Enter the amount that u need to withdraw");
            double value;
            while(true){
                try{
                    value=sc.nextDouble();
                    sc.nextLine();
                    if(value>0)
                    break;
                    System.out.println("Amount must Greater than 0");
                }
                catch(InputMismatchException e){
                    System.out.println("Please Enter a Valid Amount.");
                    sc.nextLine();
                    value=0;
                }
            }
            
            if(value>0 && value<=accountBalance){
                accountBalance-=value;
                System.out.println(value+" withdrawn successfully");
                System.out.print("The Available Balance: "+accountBalance);
                System.out.println();
            }
            
            else if(value>accountBalance) System.out.println("Insufficient balance");
            
            
        }
    }
    //-------------------------------checking balance--------------------------
    public void checkBalance(){
        boolean flag=pinCheck();
        
        if(flag){
            System.out.println("Available Balance: "+accountBalance);
            System.out.println();
        }
        
    }
}