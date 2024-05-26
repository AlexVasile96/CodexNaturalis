module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.logging;
    requires com.google.gson;
    requires javafx.media;
    requires java.desktop;

    opens network.client.gui to javafx.fxml;
    opens model to javafx.fxml;
    exports model;
    exports network.client.gui;
    opens network.server to com.google.gson;
    opens network.client to com.google.gson;
    opens controller to com.google.gson;
    opens network.client.cli to com.google.gson;
    exports network.client.gui.controllers;
    opens network.client.gui.controllers to javafx.fxml;
    exports network.client.gui.scene;
    opens network.client.gui.scene to javafx.fxml;
}