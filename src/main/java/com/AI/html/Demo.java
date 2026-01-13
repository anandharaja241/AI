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
        String url = getClass().getResource("index.html").toExternalForm();
        webEngine.load(url);

        // Bridge: Allow JavaScript to call Java methods
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", new JavaBridge());
            }
        });

        Scene scene = new Scene(webView, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Java HTML UI");
        stage.show();
    }

    // This class handles actions triggered from HTML
    public class JavaBridge {
        public void sayHello() {
            System.out.println("Button clicked in HTML! Logic running in Java.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}