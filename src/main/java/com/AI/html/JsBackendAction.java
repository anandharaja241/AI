package com.AI.html;

// This class handles actions triggered from HTML
public class JsBackendAction {
    public void sayHello() {
        System.out.println("Button clicked in HTML! Logic running in Java.");
    }

    public void exitApp() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public void triggerAction(String actionName) {
        System.out.println("Java triggered action: " + actionName);
        // System.exit(0);
        // You could update a SQL database or start a file download here
    }

    public void loadAnalytics() {
        System.out.println("Loading analytics module...");
    }
}
