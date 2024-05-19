package network.client.gui;

import controller.GameController;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.HandlingPlayerInputsThread;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class GameScene {
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private String initCardId;
    private ClientView clientView;
    private String currentPlayerNickname;
    private GameSceneController GameSceneController;
    private boolean isFirstClient;

    public GameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, String id, ClientView clientView, String currentPlayerNickname, boolean isFirstClient) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.initCardId = id;
        this.clientView = clientView;
        this.currentPlayerNickname = currentPlayerNickname;
        this.isFirstClient = isFirstClient;
        this.GameSceneController = new GameSceneController();
    }

    public void game(boolean isFirstClient) throws IOException {
        System.out.println("Initializing game data for client: " + clientView.getUserName());
        GameSceneController.initData(primaryStage, out, socket, in, clientView, currentPlayerNickname);
        if (isFirstClient) {
            System.out.println("First client updating setup");
            GameSceneController.updateFirst();
            out.println("SETUPFINISHED");
        } else {
            //System.out.println("Second client updating setup");
            in.readLine();
            GameSceneController.updateFirst();
            out.println("SETUPFINISHED");
        }
        while (true) {
            String message = in.readLine();
            if (message.equals("SETUPFINISHED")) {
                break;
            }
        }
        System.out.println("Starting game for client: " + clientView.getUserName());
        GameSceneController.startGame(initCardId);
    }
}
