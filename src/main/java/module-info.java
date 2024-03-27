module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proj to javafx.fxml;
    exports com.example.proj;
}