package network.client.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardPointsScene {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private String initCardId;
    List<Circle> checkpoints = new ArrayList<>();

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

        createCheckpoints();
        positionCheckpoints();

        /*Circle checkpoint0 = createCheckpoint(131,862,35);
        Circle checkpoint1 = createCheckpoint(238.5,862,35);
        Circle checkpoint2 = createCheckpoint(346,862,35);*/

        paneForButton.getChildren().addAll(closeButton);

        vBoxContainer.getChildren().addAll(boardImageView, paneForButton);
        Pane popupRoot = new Pane();
        popupRoot.setStyle("-fx-background-color: #A49E22;");
        popupRoot.getChildren().add(vBoxContainer);
        popupRoot.getChildren().addAll(checkpoints);

        Scene popupScene = new Scene(popupRoot, 479, 980);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Board Points");
        popupStage.show();

    }
    private void createCheckpoints(){
        for(int i = 0;i<30;i++){
            Circle circle = new Circle();
            circle.setFill(Color.RED);
            circle.setRadius(34.5);
            checkpoints.add(circle);
        }
    }
    private void positionCheckpoints(){
        double[][] positions = {
                {131, 862}, {238.5, 862}, {346, 862}, //caselle 0,1,2
                {399.5, 764}, {292, 764}, {184.5, 764}, {77, 764}, //caselle 3,4,5,6
                {77, 666}, {184.5, 666}, {292, 666}, {399.5, 666}, //caselle 7,8,9,10
                {399.5, 568}, {292, 568}, {184.5, 568}, {77, 568}, //caselle 11,12,13,14
                {77, 470}, {184.5, 470}, {292, 470}, {399.5, 470}, //caselle 15,16,17,18
                {399.5, 371}, {238.5, 323}, {77.5, 371}, //caselle 19,20,21
                {77.5, 272.5}, //casella 22
                {77.5, 174.5}, //casella 23
                {140, 94.5}, //casella 24
                {238.5, 76.5}, //casella 25
                {337, 94.5}, //casella 26
                {399.5, 174.5}, //casella 27
                {399.5, 272.5}, //casella 28
                {238.5, 196} //casella 29


        };
        for(int i = 0;i<30;i++){
            if (i < positions.length) {
                checkpoints.get(i).setCenterX(positions[i][0]);
                checkpoints.get(i).setCenterY(positions[i][1]);
            }
        }
    }
}
