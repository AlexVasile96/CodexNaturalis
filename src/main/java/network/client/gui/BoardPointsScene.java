package network.client.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private int playerScore;
    private ClientView clientView;
    String dot;
    List<Circle> checkpoints = new ArrayList<>();
    Color dotColor;

    //Initializing scene
    public BoardPointsScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
    }

    //managing player's selection
    private void getDotColor(){
        dot = clientView.getDot().toString();
        System.out.println("Il dot è: "+dot);
        if(dot.contains("RED")){
            dot = "RED";
        }
        if(dot.contains("GREEN")){
            dot = "GREEN";
        }
        if(dot.contains("BLUE")){
            dot = "BLUE";
        }
        if(dot.contains("YELLOW")){
            dot = "YELLOW";
        }
        dotColor = Color.web(dot);
    }

    private int getScore() throws IOException {
        out.println("showPoints");
        String stringa = in.readLine();
        if(stringa.equals("Unknown command.")) {
            return 25;
        }
        System.out.println(stringa);
        return Integer.parseInt(stringa);
    }

    //creating the popup that shows the player's points
    public void popupBoardPoints() throws IOException {
        Stage popupStage = new Stage();

        playerScore = getScore();

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

        getDotColor();
        createCheckpoints();
        positionCheckpoints();
        updateCheckpoint();
        System.out.println(playerScore);

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
            circle.setRadius(34.5);
            checkpoints.add(circle);
        }
    }
    private void positionCheckpoints(){
        double[][] positions = {
                {131, 862}, {238.5, 862}, {346, 862}, //box 0,1,2
                {399.5, 764}, {292, 764}, {184.5, 764}, {77, 764}, //boxes 3,4,5,6
                {77, 666}, {184.5, 666}, {292, 666}, {399.5, 666}, //boxes 7,8,9,10
                {399.5, 568}, {292, 568}, {184.5, 568}, {77, 568}, //boxes 11,12,13,14
                {77, 470}, {184.5, 470}, {292, 470}, {399.5, 470}, //boxes 15,16,17,18
                {399.5, 371}, {238.5, 323}, {77.5, 371}, //boxes 19,20,21
                {77.5, 272.5}, //box 22
                {77.5, 174.5}, //box 23
                {140, 94.5}, //box 24
                {238.5, 76.5}, //box 25
                {337, 94.5}, //box 26
                {399.5, 174.5}, //box 27
                {399.5, 272.5}, //box 28
                {238.5, 196} //box 29


        };
        for(int i = 0;i<30;i++){
            if (i < positions.length) {
                checkpoints.get(i).setCenterX(positions[i][0]);
                checkpoints.get(i).setCenterY(positions[i][1]);
            }
        }
    }
    private void updateCheckpoint(){
        for(Circle circle : checkpoints){
            circle.setVisible(false);
        }
        if(playerScore >= 0 && playerScore < checkpoints.size()){
            checkpoints.get(playerScore).setVisible(true);
            checkpoints.get(playerScore).setFill(dotColor);
        }
    }
}
