package it.polimi.ingsw.network.client.gui.scene;

import it.polimi.ingsw.network.client.gui.controllers.Controller;
import it.polimi.ingsw.view.ClientView;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import javafx.application.Platform;

public class EndGameScene {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private ClientView clientView;
    private Controller controller;
    private String winnerPlayer;
    private String playerOnePoints;
    private String playerTwoPoints;
    private String playerThreePoints;
    private String playerFourPoints;
    Text playerOne = new Text(null);
    Text playerTwo = new Text(null);
    Text playerThree = new Text(null);
    Text playerFour = new Text(null);
    String waitForCall;

    public EndGameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, Controller controller) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
        this.controller = controller;
    }

    public void endGame() throws IOException {
        waitForCall = in.readLine();
        do {
            System.out.println(waitForCall);
            waitForCall = in.readLine();
            if(waitForCall.equals("Suspance...")) {
                winnerPlayer = in.readLine();
                in.readLine(); //Points:
                playerOnePoints = in.readLine() + " ";
                playerTwoPoints = in.readLine() + " ";
                playerThreePoints = in.readLine() + " ";
                if(playerThreePoints.equals("exit ")) {
                    playerThreePoints = null;
                    waitForCall = "exit";
                    break;
                }
                playerFourPoints = in.readLine() + " ";
                if(playerFourPoints.equals("exit ")) {
                    playerFourPoints = null;
                    waitForCall = "exit";
                    break;
                }
            }
        } while (!waitForCall.equals("exit"));
        Platform.runLater(() -> {
            try {
                StackPane root = new StackPane();
                Text winner = new Text("The winner is: ");
                Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/SfondoWinner.jpg")));

                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
                BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                root.setBackground(new Background(backgroundImage));

                winner.setFill(Color.WHITE);
                winner.setFont(new Font(24));
                winner.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");
                playerOne.setFill(Color.WHITE);
                playerTwo.setFill(Color.WHITE);
                playerThree.setFill(Color.WHITE);
                playerFour.setFill(Color.WHITE);
                playerOne.setFont(new Font(15));
                playerTwo.setFont(new Font(15));
                playerThree.setFont(new Font(15));
                playerFour.setFont(new Font(15));
                playerOne.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");
                playerTwo.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");
                playerThree.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");
                playerFour.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");

                VBox vbox = new VBox();
                HBox hbox = new HBox();
                StackPane.setAlignment(vbox, Pos.CENTER);

                hbox.getChildren().addAll(playerOne, playerTwo, playerThree, playerFour);

                vbox.getChildren().addAll(winner, hbox);
                root.getChildren().add(vbox);

                primaryStage.setScene(new Scene(root, 800, 533));
                primaryStage.setTitle("Winner");
                primaryStage.show();

                String drumRollSound = getClass().getResource("/Sounds/DrumRollSound.mp3").toString();
                Media drumSoundMedia = new Media(drumRollSound);
                MediaPlayer drumRollPlayer = new MediaPlayer(drumSoundMedia);

                String yeah = getClass().getResource("/Sounds/CSI.mp3").toString();
                Media yeahMedia = new Media(yeah);
                MediaPlayer yeahPlayer = new MediaPlayer(yeahMedia);

                PauseTransition delay = new PauseTransition(Duration.seconds(4.25));

                drumRollPlayer.play();
                drumRollPlayer.setOnEndOfMedia(() -> {
                    drumRollPlayer.stop();
                    yeahPlayer.play();
                });

                delay.setOnFinished(event -> {
                    winner.setText("The winner is: "+ winnerPlayer);
                    playerOne.setText(playerOnePoints);
                    playerTwo.setText(playerTwoPoints);
                    playerThree.setText(playerThreePoints);
                    playerFour.setText(playerFourPoints);
                });
                delay.play();

                PauseTransition quitDelay = new PauseTransition(Duration.seconds(9.25));
                quitDelay.setOnFinished(event -> {
                    if(clientView.getUserName().equals(winnerPlayer)){
                        try {
                            controller.quit(primaryStage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        try {
                            in.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                quitDelay.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
