package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import config.DatabaseConnection;

public class AccountRepository {

    public static void saveAccount(String accountNumber, String pinCode,
                                   double accountBalance, boolean isLocked) {

        String sql = "INSERT INTO accounts " +
                     "(AccountNumber, PinCode, AccountBalance, isLocked) " +
                     "VALUES (?, ?, ?, ?)";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, accountNumber);
            ps.setString(2, pinCode);
            ps.setDouble(3, accountBalance);
            ps.setBoolean(4, isLocked);

            ps.executeUpdate();

            System.out.println("Account saved to database!");

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Failed to save account!");
            e.printStackTrace();
        }
    }
}