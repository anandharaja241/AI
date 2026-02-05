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
        String pageUrl = getClass().getResource(template).toExternalForm();
        webEngine.load(pageUrl);

        // Bridge: Allow JavaScript to call Java methods
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            // System.out.println("1) START: oldState: " + oldState + ", newState: " + newState);
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // System.out.println("2) START: oldState: " + oldState + ", newState: " + newState);
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBackend", new JsBackendAction(stage, webEngine));
            }
        });

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
            System.out.println("Requested Page not found: " + pagePath);
            return;
        }

        ResumeAnalyzer.template = template;
        this.start(stage);

        // String url = getClass().getResource(pagePath).toExternalForm();
        // System.out.println("template: "+template +", url: "+ url);
        // webEngine.load(url);

        // // This must be present to catch EVERY page load, not just the first one
        // webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
        //     System.out.println("1) navigateTo: oldState: " + oldState + ", newState: " + newState);
        //     if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        //         System.out.println("2) navigateTo: oldState: " + oldState + ", newState: " + newState);
        //         JSObject window = (JSObject) webEngine.executeScript("window");
        //         window.setMember("javaBackend", new JsBackendAction(stage, webEngine));
        //     }
        // });
    }

    public static void main(String[] args) {
        launch(args);
    }
}