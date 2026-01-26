package com.AI.html;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResumeAnalyzer {

    private static final String API_KEY = "";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        String prompt = "You are an expert HR Recruiter. " +
                "Analyze the following resume text against the Job Role: 'Software Developer'. " +
                "Return ONLY a JSON object with this format: " +
                "{\"score\": 85, \"reason\": \"Short explanation\", \"skills\": [\"skill1\", \"skill2\"]} " +
                "Resume Text: Experienced Java Developer with Spring Boot and SQL skills";

        try {
            String response = sendAiRequest(prompt);
            System.out.println("AI Result: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendAiRequest(String userPrompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        // Construct the request body using Gson JsonObject
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o"); // or your preferred model

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

        if (response.statusCode() == 200) {
            // Parse the response using JsonParser
            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            return root.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    // .get(message).getAsJsonObject() // Note: path is message -> content
                    .get("content").getAsString();
        } else {
            throw new RuntimeException("API Request Failed: " + response.body());
        }
    }
}