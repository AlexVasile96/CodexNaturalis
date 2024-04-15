module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.logging;
    requires com.google.gson;

    opens network.message to com.google.gson;
    opens model to javafx.fxml;
    exports model;
}