package com.AI.html;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Base64;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import io.github.cdimascio.dotenv.Dotenv;

public class AiAnalyzer {
    private static Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("AI_API_KEY");
    private static final String API_URL = dotenv.get("AI_API_URL");

    public static String getAiResponse(String prompt, String jobId, String fileName, String role) {
        String htmlString = "", dbString = "";
        try {
            System.out.println(prompt);
            // String response = sendAiRequest(prompt);
            String score = "", result = "", reason = "", skills = "";

            String response = "{\"score\": 20, \"result\": \"Not Matched\", \"reason\": \"No Relevant Java and Spring Boot experience\", \"skills\": \"No relevant skills found\"}";
            System.out.println("AI Result: " + response);
            JsonObject output = JsonParser.parseString(response).getAsJsonObject();
            score = output.get("score").getAsString();
            result = output.get("result").getAsString();
            reason = output.get("reason").getAsString();
            skills = output.get("skills").getAsString();
            dbString = score + ";;" + result + ";;" + reason + ";;" + skills + ";;" + fileName;
            System.out.println(dbString);
            String lastCreatedDate = insertData(dbString, jobId);
            String lastCreatedDateString = lastCreatedDate.equalsIgnoreCase("") ? "NOT OK" : "Created: " + lastCreatedDate;

            String color = result.equalsIgnoreCase("Matched") ? "text-success" : "text-danger";

            htmlString = "<div class=\"card resume-card shadow-sm mb-3\">\n" + //
                                "    <div class=\"card-body d-flex justify-content-between align-items-center\">\n" + //
                                "        <div>\n" + //
                                "            <h5 class=\"mb-1 text-primary\">"+fileName+"</h5>\n" + //
                                "            <p class=\"mb-0 text-muted\"><i class=\"bi bi-briefcase me-1\"></i> Match for: "+role+"</p>\n" + //
                                "            <small class=\"text-secondary\">Extracted Skills: "+skills+"</small>\n" + //
                                "            <p class=\"text-secondary m-0\">Reason: " + reason + "</p>\n" + //
                                "            <p class=\"text-secondary m-0\">" + lastCreatedDateString + "</p>\n" + //
                                "        </div>\n" + //
                                "        <div class=\"text-center\">\n" + //
                                "            <div class=\""+color+" score-badge\">"+score+"%</div>\n" + //
                                "            <small class=\""+color+" text-uppercase fw-bold\">"+result+"</small>\n" + //
                                "        </div>\n" + //
                                "    </div>\n" + //
                                "</div>";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlString;
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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
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

    public static String insertData(String info, String jobid) {
        var conn = Database.connectDB();

        var columns = "info, jobid";
        var columnList = new java.util.ArrayList<String>();
        columnList.add(info);
        columnList.add(jobid);
        String createdDate = Database.createAndReturnDate(conn, "resumedata", columns, columnList);
        return createdDate;
    }

    public static String getResults() {
        String results = "";
        try {
            var conn = Database.connectDB();
            ResultSet list = Database.getAll(conn, "resumedata");
            String info, jobid, created, id;
            int index = 0;
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
                String result = info.split(";;")[1];
                String activeClass = index == 0 ? "active" : "";
                String color = result.equalsIgnoreCase("Matched") ? "text-success" : "text-danger";
                results += "<div class=\"history-item " + activeClass + "\" data-id=\"" + id + "\">\n" +
                        "    <small class=\"text-muted\">" + created + "</small>\n" +
                        "    <div class=\"fw-bold\">" + jobTitle + "</div>\n" +
                        "    <small class=\""+color+" fw-bold\">Score: " + score + "%, "+result+"</small>\n" +
                        "</div>\n";
                index++;
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

    public static String getJobTitleById(String id) {
        try {
            var conn = Database.connectDB();
            String condition = "id='" + id + "'";
            ResultSet rs = Database.get(conn, "jobs", condition);
            if (rs.next()) {
                // String jobId = rs.getString("id");
                String role = rs.getString("role");
                String exp = rs.getString("exp");
                return role + " - " + exp; // Using '-' as a delimiter
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
            var conn = Database.connectDB();
            String condition = "id='" + id + "'";
            ResultSet rs = Database.get(conn, "resumedata", condition);
            if (rs.next()) {
                String info = rs.getString("info");
                String jobid = rs.getString("jobid");
                String created = rs.getString("createDate");
                String jobTitle = getJobById(jobid);
                String score = info.split(";;")[0];
                String resultValue = info.split(";;")[1];
                String textColor = resultValue.equalsIgnoreCase("Matched") ? "text-success" : "text-danger";
                String reason = info.split(";;")[2];
                String skills = info.split(";;")[3];
                String fileName = info.split(";;")[4];
                result += "<div class=\"card resume-card shadow-sm mb-3\">\n" + //
                        "    <div class=\"card-body d-flex justify-content-between align-items-center\">\n" + //
                        "        <div>\n" + //
                        "            <h5 class=\"mb-1 text-primary\">" + fileName + "</h5>\n" + //
                        "            <p class=\"mb-0 text-muted\"><i class=\"bi bi-briefcase me-1\"></i> Match for: "
                        + jobTitle + "</p>\n" + //
                        "            <small class=\"text-secondary\">Skills: " + skills + "</small>\n" + //
                        "            <p class=\"text-secondary m-0\">Reason: " + reason + "</p>\n" + //
                        "            <p class=\"text-secondary m-0\">Created: " + created + "</p>\n" + //
                        "        </div>\n" + //
                        "        <div class=\"text-center\">\n" + //
                        "            <div class=\"" + textColor + " score-badge\">" + score + "%</div>\n" + //
                        "            <small class=\"" + textColor + " text-uppercase fw-bold\">" + resultValue
                        + "</small>\n" + //
                        "        </div>\n" + //
                        "    </div>\n" + //
                        "</div>\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String processWithAI(String fileName, String base64Data, String jobId) {
        System.out.println("Received Base64 Length: " + base64Data.length());
        String resultHtml = "";
        try {
            String filePath = saveBase64ToFile(base64Data, "uploads/" + fileName);
            String resumeText = extractTextFromFile(filePath);

            String roleString = getJobTitleById(jobId);

            // 2. Prepare AI Prompt
            String prompt = "Analyze this resume text for the role of '" + roleString +
                    "'. Provide a match score (0-100), result(Matched or Not Matched), 1 line explanation for the score as reason and 3 key skills if exists, otherwise return 'No relevant skills found'. " +
                    "Return ONLY a JSON object with this sample format: " +
                    "{\"score\": 85, \"result\": \"Matched or Not Matched\", \"reason\": \"Short explanation for the score\", \"skills\": \"skill1, skill2, skill3\"} " +
                    "Resume text: " + resumeText;

            System.out.println(prompt);
            // 3. Call AI API (Example using an HTTP client)
            resultHtml = getAiResponse(prompt, jobId, fileName, roleString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHtml;
    }

public static String saveBase64ToFile(String base64Content, String outputFilePath) throws IOException {
        // 2. Decode the Base64 string into a byte array
        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

        // 3. Define the destination path
        Path destinationPath = Paths.get(outputFilePath);

        // 4. Write the bytes to the file
        Files.write(destinationPath, decodedBytes);

        return destinationPath.toAbsolutePath().toString();
    }

    public static String extractTextFromFile(String filePath) {
        Boolean isPdf = filePath.toLowerCase().endsWith(".pdf");
        Boolean isDocx = filePath.toLowerCase().endsWith(".docx");
        Boolean isDoc = filePath.toLowerCase().endsWith(".doc");
        Boolean isOctDoc = filePath.toLowerCase().endsWith(".oct");
        String extractedText = "";
        try {
            if (isPdf) {
                File file = new File(filePath);
                try (PDDocument document = Loader.loadPDF(file)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    stripper.setSortByPosition(true);
                    document.setAllSecurityToBeRemoved(true);
                    extractedText = stripper.getText(document);
                }
            } else if (isDocx || isDoc || isOctDoc) {
                try (InputStream in = Files.newInputStream(Paths.get(filePath));
                    XWPFDocument doc = new XWPFDocument(in);
                    XWPFWordExtractor ext = new XWPFWordExtractor(doc)) {
                    extractedText = ext.getText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedText;
    }
}