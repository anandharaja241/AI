package com.AI.html;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class Demo extends Application {

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load your HTML file
        // String pageUrl = getClass().getResource("/UI/dashboard.html").toExternalForm();
        String pageUrl = getClass().getResource("/UI/create-job.html").toExternalForm();
        // String pageUrl = getClass().getResource("/UI/edit-job.html").toExternalForm();
        // String pageUrl = getClass().getResource("/UI/list-job.html").toExternalForm();
        webEngine.load(pageUrl);

        // Bridge: Allow JavaScript to call Java methods
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBackend", new JsBackendAction());
            }
        });

        Scene scene = new Scene(webView, 800, 600);
        stage.setScene(scene);
        // Set to maximized
        stage.setMaximized(true);

        stage.setTitle("AI Resume Analyzer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}