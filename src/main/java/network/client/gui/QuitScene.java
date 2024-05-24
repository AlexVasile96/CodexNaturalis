package network.client.gui;

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
                Label message = new Label("You are quitting the game...");
                StackPane root = new StackPane();
                root.getChildren().add(message);
                primaryStage.setScene(new Scene(root, 800, 600));
                primaryStage.setTitle("Quitting");
                primaryStage.show();

                PauseTransition delay = new PauseTransition(Duration.seconds(4));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
//                primaryStage.close();
            });
        }).start();
    }
}
