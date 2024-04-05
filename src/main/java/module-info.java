module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.logging;


    opens model to javafx.fxml;
    exports model;
}