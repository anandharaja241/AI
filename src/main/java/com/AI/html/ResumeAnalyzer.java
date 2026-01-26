package com.AI.html;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.scene.chart.PieChart.Data;

public class ResumeAnalyzer {

      private static final String API_KEY = "";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    public static void main(String[] args) {
        String prompt = "You are an expert HR Recruiter. " +
                "Analyze the following resume text against the Job Role: 'Software Developer'. " +
                "Return ONLY a JSON object with this format: " +
                "{\"score\": 85, \"reason\": \"Short explanation\", \"skills\": [\"skill1\", \"skill2\"]} " +
                "Resume Text: Experienced Java Developer with Spring Boot and SQL skills";

        getAiResponse(prompt);
        System.out.println(getResults());
    }

    public static String getAiResponse(String prompt) {
        String outputString = "";
        try {
            // String response = sendAiRequest(prompt);

            String response = "{\"score\": 90, \"reason\": \"Relevant Java and Spring Boot experience\", \"skills\": [\"Java\", \"Spring Boot\", \"SQL\"]}";
            // System.out.println("AI Result: " + response);
            JsonObject output = JsonParser.parseString(response).getAsJsonObject();
            outputString = output.get("score").getAsString() + ";;" + output.get("reason").getAsString() + ";;"
                    + output.get("skills").toString();
            System.out.println(outputString);
            insertData(outputString, "42");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputString;
    }

    public static String sendAiRequest(String userPrompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        // Construct the request body using Gson JsonObject
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "llama-3.3-70b-versatile"); // or your preferred model

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", userPrompt);

        // Add message to an array
        com.google.gson.JsonArray messages = new com.google.gson.JsonArray();
        messages.add(message);
        requestBody.add("messages", messages);

        // String jsonBody = "{\"contents\": [{\"parts\":[{\"text\": \"" + userPrompt +
        // "\"}]}]}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                // .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("API procssing...");

        if (response.statusCode() == 200) {
            System.out.println("API success 1");
            // Parse the response using JsonParser
            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(JsonParser.parseString(response.body()).getAsJsonObject());
            System.out.println("API success 2");
            return root.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .get("message").getAsJsonObject() // Note: path is message -> content
                    .get("content").getAsString();
        } else {
            throw new RuntimeException("API Request Failed: " + response.body());
        }
    }

    public static void insertData(String info, String jobid) {
        Database db = new Database();
        var conn = db.connectDB();

        var columns = "info, jobid";
        var columnList = new java.util.ArrayList<String>();
        columnList.add(info);
        columnList.add(jobid);
        int status = Database.create(conn, "resumedata", columns, columnList);
    }

    public static String getResults() {
        String results = "";
        try {
            Database db = new Database();
            var conn = db.connectDB();
            ResultSet list = Database.getAll(conn, "resumedata");
            String info, jobid, created, id;
            while (list.next()) {
                info = list.getString("info");
                created = list.getString("createDate");
                jobid = list.getString("jobid");
                id = list.getString("id");
                if (jobid == null || jobid.isEmpty()) {
                    continue;
                }
                String jobTitle = getJobById(jobid);
                String score = info.split(";;")[0];
                // results += info + " - " + jobTitle + " - " + created + "\n";
                results += "<div class=\"history-item\" data-id=\"" + id + "\">\n" +
                           "    <small class=\"text-muted\">" + created + "</small>\n" +
                           "    <div class=\"fw-bold\">" + jobTitle + "</div>\n" +
                           "    <small>Score: " + score + "%</small>\n" +
                           "</div>\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static String getJobById(String id) {
        try {
            var conn = Database.connectDB();
            String condition = "id='" + id + "'";
            ResultSet rs = Database.get(conn, "jobs", condition);
            if (rs.next()) {
                String jobId = rs.getString("id");
                String role = rs.getString("role");
                String exp = rs.getString("exp");
                return jobId + " " + role + " - " + exp; // Using ';;' as a delimiter
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHistoryDetails(String id) {
        String result = "";
        try {
            Database db = new Database();
            var conn = db.connectDB();
            String condition = "id='" + id + "'";
            ResultSet rs = Database.get(conn, "resumedata", condition);
            if (rs.next()) {
                String info = rs.getString("info");
                String jobid = rs.getString("jobid");
                String created = rs.getString("createDate");
                String jobTitle = getJobById(jobid);
                String score = info.split(";;")[0];
                String reason = info.split(";;")[1];
                String skills = info.split(";;")[2];
                result += "<h5>Job: " + jobTitle + "</h5>\n" +
                          "<p><strong>Score:</strong> " + score + "%</p>\n" +
                          "<p><strong>Reason:</strong> " + reason + "</p>\n" +
                          "<p><strong>Skills:</strong> " + skills + "</p>\n" +
                          "<small class=\"text-muted\">Analyzed on: " + created + "</small>\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}