package com.AI.html;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

// This class handles actions triggered from HTML
public class JsBackendAction {
    private static Stage webStage;
    private static WebEngine engine;

    private static volatile JsBackendAction instance;

    private JsBackendAction(Stage stage, WebEngine webEngine) {}

    public static JsBackendAction getInstance(Stage stage, WebEngine webEngine) {

        if (instance == null) { // First check
            synchronized (JsBackendAction.class) {
                if (instance == null) { // Second check
                    instance = new JsBackendAction(stage, webEngine);
                }
            }
        }
        JsBackendAction.webStage = stage;
        JsBackendAction.engine = webEngine;
        return instance;
    }

    public void navigateTo(String template) {
        ResumeAnalyzer analyzer = new ResumeAnalyzer();
        analyzer.navigateTo(template, JsBackendAction.webStage, JsBackendAction.engine);
    }

    public int createJobs(String role, String exp) {
        try {
            var conn = Database.connectDB();
            var columns = "role,exp";
            var columnList = new java.util.ArrayList<String>();
            columnList.add(role);
            columnList.add(exp);
            int status = Database.create(conn, "jobs", columns, columnList);
            conn.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    public int updateJobs(String id, String role, String exp) {
        try {
            var conn = Database.connectDB();
            int status = Database.update(conn, id, role, exp);
            conn.close();
            return status;
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
                        "    <td><strong>" + (index++) + "</strong></td>\n" + //
                        "    <td><strong>" + id + "</strong></td>\n" + //
                        "    <td>" + role + "</td>\n" + //
                        "    <td>" + exp + "</td>\n" + //
                        "    <td>" + created + "</td>\n" + //
                        "    <td class=\"text-center\">\n" + //
                        "        <button class=\"btn btn-outline-info btn-sm edit-nav-btn me-1 nav-btn\" data-id=\""
                        + id + "\" data-href=\"edit-job\"><i\n" + //
                        "                class=\"bi bi-pencil\"></i></button>\n" + //
                        "        <button class=\"btn btn-outline-danger delete-btn btn-sm nav-btn\" data-id=\"" + id
                        + "\" data-href=\"list-job\"><i class=\"bi bi-trash\"></i></button>\n" + //
                        "    </td>\n" + //
                        "</tr>");
            }
            conn.close();
            if (sb.length() == 0) {
                return "<tr><td colspan='6' class='text-center'>No jobs found.</td></tr>";
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<tr><td colspan='6' class='text-center'>No jobs found.</td></tr>";
        }
    }

    public String getJobOptions() {
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "jobs");
            StringBuilder sb = new StringBuilder();
            sb.append("<option value=\"\" selected disabled>Select a Job Description</option>\n");
            String role = "", exp = "", id = "", template = "";
            while (list.next()) {
                role = list.getString("role");
                exp = list.getString("exp");
                id = list.getString("id");
                template = "<option value=\"" + id + "\">" + id + " " + role + " - " + exp + "</option>\n";
                sb.append(template);
            }
            conn.close();
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
                conn.close();
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
            conn.close();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loadHistory() {
        return AiAnalyzer.getResults();
    }

    public String getHistoryDetails(String id) {
        return AiAnalyzer.getHistoryDetails(id);
    }

    public String processWithAI(String fileName, String base64Data, String role) {
        return AiAnalyzer.processWithAI(fileName, base64Data, role);
    }

    public String getRecentResults(int count) {
        return AiAnalyzer.getRecentResults(count);
    }

    public String getDashboardStats() {
        String resultString = "0;;0;;0";
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "resumedata");
            int totalResumes = 0, matchedResumes = 0, unmatchedResumes = 0;
            while (list.next()) {
                totalResumes++;
                String infoString = list.getString("info");
                String info[] = infoString.split(";;");

                if (info.length > 0 && info[1].equalsIgnoreCase("matched")) {
                    matchedResumes++;
                } else if (info.length > 0 && (info[1].equalsIgnoreCase("unmatched") || info[1].equalsIgnoreCase("not matched"))) {
                    unmatchedResumes++;
                }
            }
            conn.close();
            resultString = totalResumes + ";;" + matchedResumes + ";;" + unmatchedResumes; // Using ';;' as a delimiter
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public String getRoleExpOptions() {
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "jobs");
            String role, exp;
            List<String> roleList = new ArrayList<>();
            List<String> expList = new ArrayList<>();
            while (list.next()) {
                role = list.getString("role");
                exp = list.getString("exp");
                roleList.add(role);
                expList.add(exp);
            }
            conn.close();
            List<String> uniqueRoleList = roleList.stream().distinct().sorted(String.CASE_INSENSITIVE_ORDER).toList();
            List<String> uniqueExpList = expList.stream().distinct().sorted(String.CASE_INSENSITIVE_ORDER).toList();
            StringBuilder sb = new StringBuilder();
            sb.append("<option value=\"\" selected disabled>Select Role Type</option>\n");
            for (String r : uniqueRoleList) {
                sb.append("<option value=\"").append(r).append("\">").append(r).append("</option>\n");
            }
            sb.append(";;");
            sb.append("<option value=\"\" selected disabled>Select Experience Level</option>\n");
            for (String e : uniqueExpList) {
                sb.append("<option value=\"").append(e).append("\">").append(e).append("</option>\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<option value=\"\" disabled>Select Role Type</option>;;<option value=\"\" disabled>Select Experience Level</option>"; // Return empty options on error
        }
    }
}
