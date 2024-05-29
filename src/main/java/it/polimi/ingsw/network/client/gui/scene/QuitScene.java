package it.polimi.ingsw.network.client.gui.scene;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuitScene {

    public void quit(Stage primaryStage){

        new Thread(() -> {
            Platform.runLater(() -> {
                primaryStage.close();
            });
        }).start();
    }
}
