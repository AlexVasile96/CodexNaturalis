package network.client.gui.scene;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
        VBox vbox = new VBox();
        Text winner = new Text("The winner is: " );
        vbox.getChildren().add(winner);

        root.getChildren().add(vbox);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("The winner is...");
        primaryStage.show();

        String drumRollSound = getClass().getResource("/Sounds/DrumRollSound.mp3").toString();
        Media drumSoundMedia = new Media(drumRollSound);
        MediaPlayer drumRollPlayer = new MediaPlayer(drumSoundMedia);

        drumRollPlayer.play();
        System.out.println(in.readLine());

        PauseTransition delay = new PauseTransition(drumSoundMedia.getDuration());

        delay.setOnFinished(event -> {
            winner.setText("The winner is: Paolo");
        });
        delay.play();

    }
}
