package network.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.game.Game;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyController {
    //guarda in.readLine stampa e vedi cosa stampa allCLients logged
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private ExecutorService executor;
    private ClientView clientview;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
        this.executor = Executors.newSingleThreadExecutor();
        this.clientview=cl;
    }
    public void waitAllPlayers() {
        executor.execute(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    if (message.equals("All clients connected")) {
                        Platform.runLater(() -> {
                            System.out.println("ciao");
                            SecretCardScene secretCardSceneHandler = new SecretCardScene();
                            secretCardSceneHandler.chooseSecretCard(primaryStage, out, socket, in, clientview);
                                 });
                        break;
                    }
                    if (message.equals("All clients chose the init Card")) {
                        Platform.runLater(() -> {
                            System.out.println("seconda lobby");
                            try {
                                String currentPlayerNickname = in.readLine();
                                System.out.println("CurrentPlayerNickname is: "+currentPlayerNickname);
                                GameScene gameSceneHandler = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname);
                                gameSceneHandler.game();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
