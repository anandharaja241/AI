package com.AI.html;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class ResumeAnalyzer extends Application {

    private static String template = "/UI/dashboard.html";

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load your HTML file
        String pageUrl = getClass().getResource(ResumeAnalyzer.template).toExternalForm();
        webEngine.load(pageUrl);

        // Attach listener BEFORE loading
        attachJavaScriptBridge(webEngine, stage);

        Scene scene = new Scene(webView, 800, 600);
        stage.setScene(scene);
        // Set to maximized
        stage.setMaximized(true);

        stage.setTitle("AI Smart Resume Analyzer");
        stage.show();
    }

    public void navigateTo(String template, Stage stage, WebEngine webEngine) {
        String pagePath = "/UI/" + template + ".html";
        if (getClass().getResource(pagePath) == null) {
            System.out.println("[ERROR] Requested Page not found: " + pagePath);
            return;
        }

        String url = getClass().getResource(pagePath).toExternalForm();
        webEngine.load(url);

        // Attach listener BEFORE loading to catch the state change
        attachJavaScriptBridge(webEngine, stage);
    }

    private static void attachJavaScriptBridge(WebEngine webEngine, Stage stage) {
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBackend", JsBackendAction.getInstance(stage, webEngine));
            }
        });
    }

    public static void main(String[] args) {
        try {
            Database.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}