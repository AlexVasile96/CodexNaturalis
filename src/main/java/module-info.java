module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.logging;
    requires com.google.gson;

    opens network.client.gui to javafx.fxml;
    opens model to javafx.fxml;
    exports model;
    exports network.client.gui;
    opens server to com.google.gson;
}