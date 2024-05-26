package network.client.gui.scene;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.ClientView;
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

    public EndGameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
    }

    public void endGame() throws IOException {
        StackPane root = new StackPane();
        Text winner = new Text("The winner is: " );

        Image background = new Image("/SfondoWinner.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(backgroundImage));

        winner.setFill(Color.WHITE);
        // Imposta la dimensione del carattere
        winner.setFont(new Font(24));
        // Aggiungi un'ombra al testo per migliorare la visibilità
        winner.setStyle("-fx-effect: dropshadow(one-pass-box, black, 8, 0, 0, 0);");

        root.getChildren().add(winner);

        primaryStage.setScene(new Scene(root, 800, 533));
        primaryStage.setTitle("Winner winner chicken dinner");
        primaryStage.show();

        String drumRollSound = getClass().getResource("/Sounds/DrumRollSound.mp3").toString();
        Media drumSoundMedia = new Media(drumRollSound);
        MediaPlayer drumRollPlayer = new MediaPlayer(drumSoundMedia);

        String yeah = getClass().getResource("/Sounds/CSI.mp3").toString();
        Media yeahMedia = new Media(yeah);
        MediaPlayer yeahPlayer = new MediaPlayer(yeahMedia);


        PauseTransition delay = new PauseTransition(Duration.seconds(4.25));

        drumRollPlayer.play();
        System.out.println(in.readLine());

        drumRollPlayer.setOnEndOfMedia(()->{
            drumRollPlayer.stop();
            yeahPlayer.play();
        });

        delay.setOnFinished(event -> {
            winner.setText("The winner is: Eli");
        });
        delay.play();
    }
}
