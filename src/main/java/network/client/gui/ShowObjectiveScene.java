package network.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ShowObjectiveScene {
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;

    public ShowObjectiveScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
    }

    public void popupObjectiveScene(String firstId, String secondId) throws IOException {
        Stage popupStage = new Stage();

        StackPane popupRoot = new StackPane();

        popupStage.setTitle("Common objective cards");
        popupStage.setResizable(false);
        popupStage.initModality(Modality.WINDOW_MODAL); //prevents the interactions with the primary window while the popup is running

        String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + firstId + ".png";
        String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + secondId + ".png";

        System.out.println("Il path della carta 1: "+ pathObj1);
        System.out.println("Il path della carta 2: "+ pathObj2);

        Image objImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
        Image objImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
        ImageView obiettivo1 = new ImageView(objImage1);
        ImageView obiettivo2 = new ImageView(objImage2);

        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        popupRoot.setBackground(background);

        HBox hBox = new HBox();
        hBox.setSpacing(20); // Aggiunge spazio tra gli elementi
        hBox.setAlignment(Pos.CENTER);

        Text text = new Text("Theese are the common objective cards");
        text.setFill(Color.WHITE); // Cambia il colore del testo
        text.setFont(Font.font("Arial", FontWeight.BOLD, 17)); // Cambia il font e la dimensione del testo

        obiettivo1.setFitHeight(120);
        obiettivo2.setFitHeight(120);
        obiettivo1.setFitWidth(165);
        obiettivo2.setFitWidth(165);

        hBox.getChildren().addAll(text, obiettivo1, obiettivo2);
        popupRoot.getChildren().addAll(hBox);

        Scene popupScene = new Scene(popupRoot, 800, 600);

        popupStage.setScene(popupScene);
        popupStage.setTitle("Board Points");
        popupStage.show();
    }

}
