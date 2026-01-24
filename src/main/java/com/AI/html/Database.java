package com.AI.html;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Database {
    static final String URL = "jdbc:mysql://localhost:3306/demo";
    static final String USER = "root";
    static final String PASS = "";

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

    public static boolean updateConn(Connection conn, int id, String role, String exp) {
        try (Connection connection = conn) {
            PreparedStatement ps = conn.prepareStatement("UPDATE jobs SET role = ?, exp=? WHERE id = ?");
            ps.setString(1, role);
            ps.setString(2, exp);
            ps.setInt(3, id);
            int rowsaffect = ps.executeUpdate();

            System.out.println(rowsaffect + " Updated");

        } catch (SQLException e) {
            System.out.println("Role already exists: ");
            return false; // Role already existst
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    public static boolean validateUser(Connection conn, String role, String exp) {
        try (Connection connection = conn) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM jobs WHERE role=? AND exp=?");
            ps.setString(1, role);
            ps.setString(2, exp);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteConn(Connection conn, int id) {
        try (Connection connection = conn) {
            PreparedStatement ps = conn.prepareStatement("Delete from jobs where id=?");
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Delete");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Connection conn = Database.createConn();
        // Database.addUser(conn, "Senior developer", "6 years");
        // Database.updateConn(conn, 17, "Super Senior developer", "190 years");
        Database.deleteConn(conn, 1);
    }
}
