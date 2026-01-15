package com.AI.html;

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

    public void sayHello() {
        System.out.println("Button clicked in HTML! Logic running in Java.");
    }

    public void exitApp() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void triggerAction(String actionName) {
        System.out.println("Java triggered action: " + actionName);
    }

    public void loadAnalytics() {
        System.out.println("Loading analytics module...");
    }

    public void navigateTo(String template) {
        Demo demo = new Demo();
        demo.navigateTo(template, this.webStage, this.engine);
    }
}
