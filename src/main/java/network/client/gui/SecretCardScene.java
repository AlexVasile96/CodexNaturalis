package network.client.gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
public class SecretCardScene {
    private Scene chooseSecretObjectiveScene;

    @FXML
    public ImageView chosenObj;

    public void chooseSecretCard(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) {
        Platform.runLater(() -> {
            try {
                System.out.println(in.readLine()); //Printing first secret card
                System.out.println(in.readLine()); //printing second secret card
                System.out.println("Secret card printed");
                String firstCardId = in.readLine();
                String secondCardId = in.readLine();
                String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + firstCardId + ".png";
                String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + secondCardId + ".png";
                Image objImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
                Image objImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
                ImageView obiettivo1 = new ImageView(objImage1);
                ImageView obiettivo2 = new ImageView(objImage2);

                Pane root = new Pane();
                Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                Background background = new Background(backgroundImage);
                root.setBackground(background);

                VBox vBox = new VBox();
                vBox.setSpacing(20); // Aggiunge spazio tra gli elementi
                vBox.setAlignment(Pos.CENTER);

                Text text = new Text("Choose your secret objective card");
                text.setFill(Color.WHITE); // Cambia il colore del testo
                text.setFont(Font.font("Arial", FontWeight.BOLD, 17)); // Cambia il font e la dimensione del testo

                obiettivo1.setFitHeight(120);
                obiettivo2.setFitHeight(120);
                obiettivo1.setFitWidth(165);
                obiettivo2.setFitWidth(165);

                vBox.getChildren().addAll(text, obiettivo1, obiettivo2);
                root.getChildren().addAll(vBox);

                Scene chooseSecretObjectiveScene = new Scene(root, 800, 600);
                primaryStage.setScene(chooseSecretObjectiveScene);

                // Centra la radice
                vBox.layoutXProperty().bind(chooseSecretObjectiveScene.widthProperty().subtract(vBox.widthProperty()).divide(2));
                vBox.layoutYProperty().bind(chooseSecretObjectiveScene.heightProperty().subtract(vBox.heightProperty()).divide(2));

                /*primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                    root.setPrefWidth((double) newVal); // Ridimensiona il VBox orizzontalmente.
                });
                primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                    root.setPrefHeight((double) newVal); // Ridimensiona il VBox verticalmente.
                });*/

                obiettivo1.setOnMouseClicked(e -> {
                    try {
                        out.println(1);
                        chosenObj = obiettivo1;
                        System.out.println("Choose first card");
                        InitCardScene initCardSceneHandler = new InitCardScene();
                        //initCardSceneHandler.chooseInitCard(primaryStage, out, socket, in);
                        primaryStage.setScene(initCardSceneHandler.chooseInitCard(primaryStage, out, socket, in));

                    } catch (Exception action) {
                        action.printStackTrace();
                    }
                });
                obiettivo2.setOnMouseClicked(e -> {
                    try {
                        out.println(2);
                        chosenObj = obiettivo2;
                        System.out.println("Choose second card");
                        InitCardScene initCardSceneHandler = new InitCardScene();
                        //initCardSceneHandler.chooseInitCard(primaryStage, out, socket, in);
                        primaryStage.setScene(initCardSceneHandler.chooseInitCard(primaryStage, out, socket, in));
                    } catch (Exception action) {
                        action.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
