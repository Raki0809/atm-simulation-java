package repository;

import java.util.*;
import model.Account;
import java.sql.*;
import java.time.LocalDateTime;
import config.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;


public class AccountRepository {

    public static boolean saveAccount(String accountNumber, String pinCode,
                                   double accountBalance, boolean isLocked) throws SQLException{

        String sql = "INSERT INTO accounts " +
                     "(AccountNumber, PinCode, AccountBalance, isLocked) " +
                     "VALUES (?, ?, ?, ?)";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            String hashedPin=BCrypt.hashpw(pinCode,BCrypt.gensalt());

            ps.setString(1, accountNumber);
            ps.setString(2, hashedPin);
            ps.setDouble(3, accountBalance);
            ps.setBoolean(4, isLocked);
            int rows=ps.executeUpdate();

            return rows>0;

        }
        catch (Exception e) {
            System.out.println("Failed to save account!");
            e.printStackTrace();
            return false;
        }
    }
    //=================to check whether the account exist in the database ========================
    public static boolean accountExists(String accountNumber){
        String sql="Select AccountNumber from accounts where AccountNumber=?";
        
        try(Connection con =DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            
            ps.setString(1,accountNumber);//1 represents the first parameter

            var rs=ps.executeQuery();// var adopts to the datatype
            boolean exists=rs.next();//next checks if the result has any row r not ,if exists it returns true

            
            return exists;

        }
        catch(Exception e){
            System.out.println("Failed to check account!");
            e.printStackTrace();//prints the error so that we can debug ourself
            return false;

        }
    }

    //==================to get the account details from the database if exist=============

    public static Account getAccount(String accountNumber,Scanner sc){

        String sql="select AccountNumber ,PinCode,AccountBalance, isLocked,lockedUntil from accounts where AccountNumber=?";

        try(Connection con=DatabaseConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
        
        ps.setString(1,accountNumber);

        var rs=ps.executeQuery();
        
            if(rs.next()){
                String accountNo=rs.getString("AccountNumber");
                String pin=rs.getString("PinCode");
                double balance=rs.getDouble("AccountBalance");
                boolean locked=rs.getBoolean("isLocked");
                Timestamp lockedUntilTimestamp=rs.getTimestamp("lockedUntil");
                LocalDateTime lockedUntil=null;

                if(lockedUntilTimestamp!=null){
                    lockedUntil=lockedUntilTimestamp.toLocalDateTime();
                }

                return new Account(accountNo,pin ,balance,locked,lockedUntil,sc);

            }
            
        }
        catch(Exception e){
            System.out.println("Failed to Retrieve data");
            e.printStackTrace();
        }
        return null;
    }

    public static void lockAccount(String accountNumber,LocalDateTime lockedUntil){
        
        String sql="update accounts set isLocked=1,lockedUntil=? where AccountNumber=?";
        
        try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            
            ps.setTimestamp(1,Timestamp.valueOf(lockedUntil));
            ps.setString(2,accountNumber);

            ps.executeUpdate();

        }

        catch(Exception e){

            System.out.println("Failed to lock account!");
            e.printStackTrace();
        }
    }


    public static void unlockAccount(String accountNumber){

        String sql="update accounts set isLocked=0 ,lockedUntil=null where AccountNumber=?";

        try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            
            ps.setString(1,accountNumber);
            ps.executeUpdate();
        
        }
        catch(Exception e){
            System.out.println("Failed to unlock Account");
            e.printStackTrace();
        }

    }

    public static boolean deposit(String accountNumber,double amount){
        String sql="update accounts set AccountBalance=AccountBalance + ? where AccountNumber = ?";
        
        try(Connection con=DatabaseConnection.getConnection()){
            con.setAutoCommit(false);

            try{
                try(PreparedStatement ps=con.prepareStatement(sql)){

                    ps.setDouble(1,amount);
                    ps.setString(2,accountNumber);

                    int rows=ps.executeUpdate();
                    if(rows==0){
                        con.rollback();
                        return false;
                    }
                }
                saveTransaction(con,accountNumber,"Deposit",amount);
                con.commit();
                return true;
            }
            catch(Exception e){
                con.rollback();
                throw e;
            }

        }
        catch(Exception e){
            System.out.println("Failed to Deposit");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean withdraw(String accountNumber,double amount){

        String sql="Update accounts set AccountBalance=AccountBalance- ? where AccountNumber=? and  AccountBalance>= ?";

        try (Connection con=DatabaseConnection.getConnection()){
            con.setAutoCommit(false);
            try{
            try(PreparedStatement ps=con.prepareStatement(sql)){
                
                ps.setDouble(1,amount);
                ps.setString(2,accountNumber);
                ps.setDouble(3,amount);

                int rows=ps.executeUpdate();
                if(rows==0){
                    con.rollback();
                    return false;
                }
            }
            saveTransaction(con,accountNumber,"Withdraw",amount);
            con.commit();
            return true;
            }
            
            catch(Exception e){
            con.rollback();
            throw e;
        }
    }

        catch(Exception e){
             System.out.println("Failed to withdraw amount");
             e.printStackTrace();
             return false;
        }
    }


    //=========================Transacation table updating ===========================


    public static void saveTransaction(Connection con ,String accountNumber ,String TransactionType,double amount){

        String sql="insert into transactions (AccountNumber ,TransactionType ,Amount) values (?,?,?)";

        
            try(PreparedStatement ps=con.prepareStatement(sql)){

            ps.setString(1,accountNumber);
            ps.setString(2,TransactionType);
            ps.setDouble(3,amount);

            ps.executeUpdate();

        }

        catch(Exception e){
            System.out.println("Failed to save Transaction:) ");
            e.printStackTrace();
        }

    }

    public static void getTransactionHistory(String accountNumber){

        String sql="select TransactionID, TransactionType,Amount,TransactionTime from transactions where AccountNumber= ? order by TransactionTime desc";

        try(Connection con=DatabaseConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            
            
            ps.setString(1,accountNumber);

            var rs=ps.executeQuery();
            if(!rs.isBeforeFirst()){
                System.out.println("No transaction Available");
            }
            else{
                System.out.println("================Transaction History==================");
                int count=1;
                while(rs.next()){

                    String type=rs.getString("TransactionType");
                    double amount=rs.getDouble("Amount");
                    Timestamp time=rs.getTimestamp("TransactionTime");

                    System.out.println(count+" | "+type+" |Rs. "+amount+" | "+time);
                    count++;
                }
            }

        }
        catch(Exception e ){
            System.out.println("Failed to retrieve transaction history");
            e.printStackTrace();
        }
    }


}