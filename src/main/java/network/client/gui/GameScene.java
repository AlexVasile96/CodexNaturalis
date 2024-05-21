package network.client.gui;

import controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private GameSceneController gameSceneController;
    private boolean isFirstClient;
    private static int totalPlayers=0;
    private static int loggedInPlayers = 0;  // Static field to keep track of logged-in players


    public GameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, String id, ClientView clientView, String currentPlayerNickname, boolean isFirstClient) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.initCardId = id;
        this.clientView = clientView;
        this.currentPlayerNickname = currentPlayerNickname;
        System.out.println("Current player in init data is:" + currentPlayerNickname);
        this.isFirstClient = isFirstClient;
        this.gameSceneController = new GameSceneController();
    }

    public void game(boolean isFirstClient) throws IOException {
        System.out.println("Initializing game data for client: " + clientView.getUserName());
        gameSceneController.initData(primaryStage, out, socket, in, clientView, currentPlayerNickname);
        out.println("updateLoggedPlayers");                                         //+1 dei logged players
        System.out.println("Server says: " + in.readLine());                                         //Update loggedPlayers
        out.println("howManyPlayers");
        loggedInPlayers= Integer.parseInt(in.readLine());
        System.out.println("Logged in players: " + loggedInPlayers);

        if (isFirstClient) {
            System.out.println("First client updating setup");
            gameSceneController.updateFirst();
            out.println("totPlayers");
            totalPlayers= Integer.parseInt(in.readLine());
            System.out.println("Total PLayers in the game: " + totalPlayers);
            out.println("SETUPFINISHED");
            System.out.println(in.readLine()); //stampo la setupfinished
            System.out.println("First Player in game is "+ in.readLine());
            System.out.println("Next player to setup is " + in.readLine());
        } else {
            System.out.println("Not First client updating setup");
            gameSceneController.updateFirst();
            out.println("totPlayers");
            totalPlayers= Integer.parseInt(in.readLine());
            System.out.println("Total PLayers in the game: " + totalPlayers);
            if (loggedInPlayers<totalPlayers) {
                out.println("SETUPFINISHED");
                System.out.println(in.readLine()); //stampo la setupfinished
                System.out.println("First Player in game is "+ in.readLine());
                System.out.println("Next player to setup is " + in.readLine());
           }
            if(loggedInPlayers==totalPlayers)
            {
                System.out.println("LAST PLAYER!");
                out.println("STARTGUI");
                String STARTGUI=in.readLine();
                System.out.println("All clients updated the data, Server says " + STARTGUI);
            }
        }

        System.out.println("Starting game for client: " + clientView.getUserName());
        gameSceneController.startGame(initCardId);
    }
}
