module com.example.proj{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.logging;
    requires com.google.gson;
    requires javafx.media;
    requires java.desktop;

    opens it.polimi.ingsw.network.client.gui to javafx.fxml;
    opens it.polimi.ingsw.model to javafx.fxml;
    exports it.polimi.ingsw.network.client.gui;
    opens it.polimi.ingsw.network.server to com.google.gson;
    opens it.polimi.ingsw.network.client to com.google.gson;
    opens it.polimi.ingsw.controller to com.google.gson;
    opens it.polimi.ingsw.network.client.cli to com.google.gson;
    exports it.polimi.ingsw.network.client.gui.controllers;
    opens it.polimi.ingsw.network.client.gui.controllers to javafx.fxml;
    exports it.polimi.ingsw.network.client.gui.scene;
    opens it.polimi.ingsw.network.client.gui.scene to javafx.fxml;

}