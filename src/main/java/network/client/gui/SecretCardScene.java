package network.client.gui;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
public class SecretCardScene {
    private Scene chooseSecretObjectiveScene;

    public void chooseSecretCard(Stage primaryStage) {
        Platform.runLater(() -> {
            try {
                Parent fxmlGame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/SceltaObiettivoSegreto.fxml")));
                Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                Background background = new Background(backgroundImage);
                Pane root = new Pane();
                root.setBackground(background);
                root.getChildren().addAll(fxmlGame);
                chooseSecretObjectiveScene = new Scene(root, 800, 600);
                primaryStage.setScene(chooseSecretObjectiveScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
