package it.polimi.ingsw.network.client.gui.scene;

import it.polimi.ingsw.network.client.gui.controllers.Controller;
import it.polimi.ingsw.view.ClientView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EndGameScene {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private ClientView clientView;
    private Controller controller;

    public EndGameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, Controller controller) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
        this.controller=controller;
    }

    public void endGame() throws IOException {
//        StackPane root = new StackPane();
//        Text winner = new Text("The winner is: " );
//        System.out.println("Sono entrato in ENDGAME");
//        //Image background = new Image("/ImmaginiCodex/SfondoWinner.jpg");
//
//
//        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
//        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//        root.setBackground(new Background(backgroundImage));
//
//        winner.setFill(Color.WHITE);
//        // Imposta la dimensione del carattere
//        winner.setFont(new Font(24));
//        // Aggiungi un'ombra al testo per migliorare la visibilità
//        winner.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");
//
//        root.getChildren().add(winner);
//
//        primaryStage.setScene(new Scene(root, 800, 533));
//        primaryStage.setTitle("Winner winner chicken dinner");
//        primaryStage.show();
//
//        String drumRollSound = getClass().getResource("/Sounds/DrumRollSound.mp3").toString();
//        Media drumSoundMedia = new Media(drumRollSound);
//        MediaPlayer drumRollPlayer = new MediaPlayer(drumSoundMedia);
//
//        String yeah = getClass().getResource("/Sounds/CSI.mp3").toString();
//        Media yeahMedia = new Media(yeah);
//        MediaPlayer yeahPlayer = new MediaPlayer(yeahMedia);
//
//
//        PauseTransition delay = new PauseTransition(Duration.seconds(4.25));
//
//        drumRollPlayer.play();
//        System.out.println(in.readLine());
//
//        drumRollPlayer.setOnEndOfMedia(()->{
//            drumRollPlayer.stop();
//            yeahPlayer.play();
//        });
//
//        delay.setOnFinished(event -> {
//            winner.setText("The winner is: Eli");
//        });
//        delay.play();
        String waitForCall = in.readLine();
        do{
            System.out.println(waitForCall);
            waitForCall = in.readLine();
        }while (!waitForCall.equals("exit"));
        //stampaggio deipunteggi del broder
        controller.quit(primaryStage);
    }
}
