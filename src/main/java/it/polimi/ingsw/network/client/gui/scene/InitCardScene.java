package it.polimi.ingsw.network.client.gui.scene;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.net.SocketTimeoutException;
import java.util.Objects;
public class InitCardScene {

    private int isFront=1; //The server need 1 to place the card on the back and 0 to place it on the front
        //DEFAULT->NOBACK
    private String id;

    public Scene chooseInitCard(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) throws IOException {
        Pane root = new Pane();
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        VBox vbox = new VBox();
        vbox.setSpacing(20); // Aggiunge spazio tra gli elementi
        vbox.setAlignment(Pos.CENTER);

        Text initText = new Text("YOUR INIT CARD");
        initText.setFill(Color.WHITE); // Cambia il colore del testo
        initText.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Cambia il font e la dimensione del testo


        in.readLine(); //FIRST PART OF THE INIT CARD
        in.readLine(); //Second part of the init card
        in.readLine(); //Third part of the init card
        id = in.readLine();
        String pathInit = "/ImmaginiCodex/CarteFront/Init/" +id + ".png";
        Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathInit)));
        ImageView initCard = new ImageView(initImage);

        initCard.setFitWidth(165);
        initCard.setFitHeight(120);

        Button turnBack = new Button("Turn card to back");
        turnBack.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;"); // Stile CSS per il pulsante
        Button turnFront = new Button("Turn card to front");
        turnFront.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;"); // Stile CSS per il pulsante
        Button placeCard = new Button("Place the init card on the board");
        placeCard.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;"); // Stile CSS per il pulsante

        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(turnFront, turnBack);

        vbox.getChildren().addAll(initText, initCard, hbox, placeCard);

        root.getChildren().add(vbox);

        Scene initCardScene = new Scene(root, 800, 600);
        primaryStage.setScene(initCardScene);

        // Centra la radice
        vbox.layoutXProperty().bind(initCardScene.widthProperty().subtract(vbox.widthProperty()).divide(2));
        vbox.layoutYProperty().bind(initCardScene.heightProperty().subtract(vbox.heightProperty()).divide(2));

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            root.setPrefWidth((double) newVal); // Ridimensiona il VBox orizzontalmente.
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            root.setPrefHeight((double) newVal); // Ridimensiona il VBox verticalmente.
        });

        turnBack.setOnMouseClicked(e -> {
            int idToInt = Integer.parseInt(id);
            isFront = 0;
            if (idToInt >= 81 && idToInt <= 86) //InitCard
            {
                String pathFlipped = "/ImmaginiCodex/CarteBack/Init/" + id + ".png";
                Image initImageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
                initCard.setImage(initImageBack);
            }
        });

        turnFront.setOnMouseClicked(e -> {
            int idToInt = Integer.parseInt(id);
            isFront = 1;
            if (idToInt >= 81 && idToInt <= 86) //InitCard
            {
                String pathFlipped = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
                Image initImageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
                initCard.setImage(initImageFront);
            }
        });

        placeCard.setOnMouseClicked(e -> {
            try {
                out.println(isFront);
                LobbyScene lobbySceneHandler = new LobbyScene();
                lobbySceneHandler.createLobbyScene(primaryStage, out, socket, in, clientView,id,isFront);
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    // Show an alert indicating the disconnection
                    showAlert();
                    try {
                        // Close resources
                        in.close();
                        if (out != null) out.close();
                        if (socket != null) socket.close();
                        // Exit the application
                        Platform.exit();
                        System.exit(0);
                    } catch (SocketTimeoutException es) {
                        es.printStackTrace();
                    } catch (IOException exe) {
                        // Throw a runtime exception if an IOException occurs
                        throw new RuntimeException(exe);
                    }
                });
            }

        });
        return initCardScene;
    }
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Disconnection");
        alert.setHeaderText(null);
        alert.setContentText("Lobby is full.");
        alert.showAndWait();
    }

}
