package network.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class BoardPointsScene {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private String initCardId;

    public BoardPointsScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
    }

    public void popupBoardPoints(){
        Stage popupStage = new Stage();

        popupStage.setTitle("Board Points");
        popupStage.setResizable(false);
        popupStage.initModality(Modality.WINDOW_MODAL); //prevents the interactions with the primary window while the popup is running

        VBox vBoxContainer = new VBox(10);
        StackPane paneForButton = new StackPane();

        Image boardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/Board_points.png")));
        ImageView boardImageView = new ImageView(boardImage);
        boardImageView.setFitHeight(938);
        boardImageView.setFitWidth(479);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            popupStage.close();
        });

        closeButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;"); // Stile CSS per il pulsante

        paneForButton.getChildren().addAll(closeButton);

        vBoxContainer.getChildren().addAll(boardImageView, paneForButton);
        StackPane popupRoot = new StackPane();
        popupRoot.setStyle("-fx-background-color: #A49E22;");
        popupRoot.getChildren().addAll(vBoxContainer);

        Scene popupScene = new Scene(popupRoot, 479, 980);
        popupStage.setScene(popupScene);
        popupStage.show();

    }
}
