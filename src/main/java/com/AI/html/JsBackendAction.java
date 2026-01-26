package com.AI.html;

import java.sql.ResultSet;

import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

// This class handles actions triggered from HTML
public class JsBackendAction {
    private Stage webStage;
    private WebEngine engine;

    public JsBackendAction (Stage stage, WebEngine webEngine) {
        this.webStage = stage;
        this.engine = webEngine;
    }

    // public void sayHello() {
    //     System.out.println("Button clicked in HTML! Logic running in Java.");
    // }

    // public void exitApp() {
    //     System.out.println("Exiting...");
    //     System.exit(0);
    // }

    // public void triggerAction(String actionName) {
    //     System.out.println("Java triggered action: " + actionName);
    // }

    // public void loadAnalytics() {
    //     System.out.println("Loading analytics module...");
    // }

    public void navigateTo(String template) {
        Demo demo = new Demo();
        demo.navigateTo(template, this.webStage, this.engine);
    }

    public int createJobs(String role, String exp) {
        try {
            var conn = Database.connectDB();
            var columns = "role,exp";
            var columnList = new java.util.ArrayList<String>();
            columnList.add(role);
            columnList.add(exp);
            int status = Database.create(conn, "jobs", columns, columnList);
            if (status > 0) {
                System.out.println("Job created: " + role + ", " + exp);
                return status; // Success
            } else {
                System.out.println("Job Failed: " + role + ", " + exp);
                return status; // Failure
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    public int updateJobs(String id, String role, String exp) {
        try {
            var conn = Database.connectDB();
            // var columns = "role=?, exp=?";
            // var columnList = new java.util.ArrayList<String>();
            // columnList.add(role);
            // columnList.add(exp);
            String condition = "id='" + id + "'";
            int status = Database.update(conn, id, role, exp);
            if (status > 0) {
                System.out.println("Job updated: " + id + ", " + role + ", " + exp);
                return status; // Success
            } else {
                System.out.println("Job Update Failed: " + id + ", " + role + ", " + exp);
                return status; // Failure
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    public String listJobs() {
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "jobs");
            StringBuilder sb = new StringBuilder();
            String role, exp, id, created;
            int index = 1;
            while (list.next()) {
                role = list.getString("role");
                exp = list.getString("exp");
                created = list.getString("createDate");
                // id = role +"-"+ exp;
                id = list.getString("id");
                sb.append("<tr>\n" + //
                        "    <td><strong>"+(index++)+"</strong></td>\n" + //
                        "    <td><strong>"+id+"</strong></td>\n" + //
                        "    <td>"+role+"</td>\n" + //
                        "    <td>"+exp+"</td>\n" + //
                        "    <td>"+created+"</td>\n" + //
                        "    <td class=\"text-center\">\n" + //
                        "        <button class=\"btn btn-outline-info btn-sm edit-nav-btn me-1 nav-btn\" data-id=\""+id+"\" data-href=\"edit-job\"><i\n" + //
                        "                class=\"bi bi-pencil\"></i></button>\n" + //
                        "        <button class=\"btn btn-outline-danger delete-btn btn-sm nav-btn\" data-id=\""+id+"\"><i class=\"bi bi-trash\"></i></button>\n" + //
                        "    </td>\n" + //
                        "</tr>");
                // sb.append("Role: ").append(list.getString("role")).append(", Experience: ").append(list.getString("exp")).append("\n");
            }

            if (sb.length() == 0) {
                return "<tr><td colspan='6' class='text-center'>No jobs found.</td></tr>";
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving jobs.";
        }
    }

    public String getJobOptions() {
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "jobs");
            StringBuilder sb = new StringBuilder();
            String role, exp, id, template;
            while (list.next()) {
                role = list.getString("role");
                exp = list.getString("exp");
                id = list.getString("id");
                template = "<option value=\"" + id + "\">" + id + " " + role + " - " + exp + "</option>\n";
                sb.append(template);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getJobById(String id) {
        try {
            var conn = Database.connectDB();
            String condition = "id='" + id + "'";
            ResultSet rs = Database.get(conn, "jobs", condition);
            if (rs.next()) {
                String role = rs.getString("role");
                String exp = rs.getString("exp");
                return role + ";;" + exp; // Using ';;' as a delimiter
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Boolean deleteJobs(String id) {
        try {
            var conn = Database.connectDB();
            boolean success = Database.delete(conn, id);
            if (success) {
                System.out.println("Jobs deleted: " + id);
            } else {
                System.out.println("Job Deletion Failed: " + id);
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loadHistory() {
        ResumeAnalyzer analyzer = new ResumeAnalyzer();
        return analyzer.getResults();
    }

    public String getHistoryDetails(String id) {
        ResumeAnalyzer analyzer = new ResumeAnalyzer();
        return analyzer.getHistoryDetails(id);
    }
}
