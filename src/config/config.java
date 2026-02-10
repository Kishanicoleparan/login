
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;



public class config {
    //Connection Method to SQLITE
public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:sample.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }



public void addRecord(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
public boolean authenticate(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return true;
            }
        }
    } catch (SQLException e) {
        System.out.println("Login Error: " + e.getMessage());
    }
    return false;
}

public void displayUser(String sql, javax.swing.JTable table) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        // This line automatically maps the Resultset to your JTable
        table.setModel(DbUtils.resultSetToTableModel(rs));
        
    } catch (SQLException e) {
        System.out.println("Error displaying data: " + e.getMessage());
    }
}
private String hashPassword(String password) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));

        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    } catch (Exception e) {
        return null;
    }
}
// =====================
    // INSERT / UPDATE / DELETE (GENERAL)
    // =====================
    public int executeUpdate(String sql, Object... values) {
        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }
            return pst.executeUpdate(); // returns affected rows

        } catch (SQLException e) {
            System.out.println("Execute Error: " + e.getMessage());
            return 0;
        }
    }
// ADD WITH TRANSACTION
    // (SQLite safe)
    // =========================
    public boolean executeTransaction(String[] sqls, Object[][] values) {

        try (Connection con = connectDB()) {
            con.setAutoCommit(false);

            for (int i = 0; i < sqls.length; i++) {
                PreparedStatement pst = con.prepareStatement(sqls[i]);

                for (int j = 0; j < values[i].length; j++) {
                    pst.setObject(j + 1, values[i][j]);
                }

                pst.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Transaction Error: " + e.getMessage());
            return false;
        }
    }
   
     // =========================
    // FETCH SINGLE RECORD
    // =========================
    public ResultSet fetch(String sql, Object... values) {

        try {
            Connection con = connectDB();
            PreparedStatement pst = con.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }

            return pst.executeQuery();

        } catch (SQLException e) {
            System.out.println("Fetch Error: " + e.getMessage());
            return null;
        }
    }
}





