package com.AI.html;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

public class Database {
    static final String URL = "jdbc:mysql://localhost:3306/demo";
    static final String USER = "root";
    static final String PASS = "";
    
    public static boolean addUser(String email, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            
            // Class.forName("com.mysql.cj.jdbc.Driver");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user(email,password) VALUES (?,?)");
            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // Email already exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateUser(String email, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
