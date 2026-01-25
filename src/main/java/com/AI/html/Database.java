package com.AI.html;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class Database {
    static final String URL = "jdbc:mysql://localhost:3306/demo";
    static final String USER = "root";
    static final String PASS = "";

    public static Connection connectDB() {
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

    public static int create(Connection conn, String table, String columns, List<String> columnList) {
        if (conn == null || table == null || table.isEmpty()) {
            System.out.println("Connection is null or table name is invalid.");
            return 0;
        }
        try (Connection connection = conn) {
            String placeHolders = "";
            for (int i = 1; i <= columnList.size(); i++) {
                // if (i == columnList.size()) {
                //     placeHolders += "?";
                //     break;
                // }
                placeHolders += "?,";
            }
            PreparedStatement ps = conn
                    .prepareStatement("INSERT INTO " + table + " (" + columns + ", createDate) VALUES (" + placeHolders + "?)");
            for (int i = 0; i < columnList.size(); i++) {
                ps.setString(i + 1, columnList.get(i));
            }
            ps.setString(columnList.size() + 1, getIndianDate());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " record(s) added.");

            return 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Record already exists: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean update(Connection conn, int id, String role, String exp) {
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

    public static boolean delete(Connection conn, String ids) {
        try (Connection connection = conn) {
            String sql = "DELETE FROM jobs WHERE id IN (" + ids + ")"; 
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Delete");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getAll(Connection conn, String table) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table);
            // PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE
            // email=? AND password=?");
            rs = ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet get(Connection conn, String table, String where) {
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM " + table + " WHERE " + where;
            PreparedStatement ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void main(String[] args) {
        Connection conn = Database.connectDB();
        String table = "jobs";
        List<String> columnList = new ArrayList<>();
        columnList.add("Senior developer2");
        columnList.add("10 years");
        /*
         * Create a new record in the database
         */
        // Database.create(conn, table, "role,exp", columnList);5

        // conn = Database.connectDB();
        // Database.update(conn, 22, "Super Senior developer", "190 years");

        // conn = Database.connectDB();
        /*
         * Retrieve all records from the table
         */
        ResultSet list = Database.getAll(conn, table);

        /*
         * Retrieve records from the table based on condition
         */
        // String condition = "id='2'";
        // String condition = "id IN (4,5)";
        // ResultSet list = Database.get(conn, table, condition);
        // try {
        //     while (list.next()) {
        //         System.out.println("ID: " + list.getInt("id") + ", Role: " + list.getString("role") + ", Exp: "
        //                 + list.getString("exp"));
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // Database.delete(conn, "3,4,5,6,7,8,9,21,22,25,26,27,28.29");
    }

    public static String getIndianDate() {
        ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a");
        String formattedTime = istTime.format(formatter);
        return formattedTime;
    }
}
