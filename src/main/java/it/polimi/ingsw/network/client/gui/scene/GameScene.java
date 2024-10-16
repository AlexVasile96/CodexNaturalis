package it.polimi.ingsw.network.client.gui.scene;

import it.polimi.ingsw.view.ClientView;
import javafx.stage.Stage;
import it.polimi.ingsw.network.client.gui.controllers.GameSceneController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameScene {
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private String initCardId;
    private int isFront;
    private ClientView clientView;
    private String currentPlayerNickname;
    private GameSceneController gameSceneController;
    private boolean isFirstClient;
    private static int totalPlayers=0;
    private static int loggedInPlayers = 0;  // Static field to keep track of logged-in players


    public GameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, String id, ClientView clientView, String currentPlayerNickname, boolean isFirstClient, int isFirst) throws IOException {
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
        this.isFront=isFirst;
    }

    public void game(boolean isFirstClient) throws IOException {
        System.out.println("Initializing game data for client: " + clientView.getUserName());
        gameSceneController.initData(primaryStage, out, socket, in, clientView, currentPlayerNickname);
        out.println("updateLoggedPlayers");
        System.out.println("Server says: " + in.readLine());
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
        gameSceneController.startGame(initCardId, isFront);
    }
}
