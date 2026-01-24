package com.AI.html;

// import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

public class Database {
    static final String URL = "jdbc:mysql://localhost:3306/demo";
    static final String USER = "root";
    static final String PASS = "";

    public static boolean addUser(Connection conn, String role, String exp) {
        if (conn == null) {
            System.out.println("Connection is null. Cannot add user.");
            return false;
        }
        try (Connection connection = conn) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO jobs(role,exp) VALUES (?,?)");
            ps.setString(1, role);
            ps.setString(2, exp);
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " record(s) added.");
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Role already exists: " + role);
            return false; // Role already exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection createConn() {
        final String URL = "jdbc:sqlite:app.db";
        final String USER = "user1";
        final String PASS = "testpass";
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection established successfully.");
            // conn.close();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateConn(Connection conn, int id, String role, String exp) {
        try (Connection connection = conn) {
            PreparedStatement ps = conn.prepareStatement("UPDATE jobs SET role = ?, exp=? WHERE id = ?");
            ps.setString(1, role);
            ps.setString(2, exp);
            ps.setInt(3, id);
            ps.executeUpdate();
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                // Display values
                System.out.print("Id:" + rs.getString("id")+"\n");
                System.out.print("Role:" + rs.getString("role") + "\n");
                System.out.println("exp:" + rs.getString("exp") + "\n");
            }
            return true;
            
            
        } 
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Role already exists: " + role);
            // return false; // Role already exists
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
        return true;
    }

    /*
     * public static boolean validateUser(String email, String password) {
     * try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
     * PreparedStatement ps =
     * conn.prepareStatement("SELECT * FROM user WHERE email=? AND password=?");
     * ps.setString(1, email);
     * ps.setString(2, password);
     * ResultSet rs = ps.executeQuery();
     * return rs.next();
     * } catch (Exception e) {
     * e.printStackTrace();
     * return false;
     * }
     * }
     */

    public static void main(String[] args) {
        Connection conn = Database.createConn();
        // Database.addUser(conn, "Senior developer", "6 years");
        Database.updateConn(conn, 5, "Super Senior developer", "19 years");
    }
}
