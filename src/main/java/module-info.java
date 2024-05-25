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
}