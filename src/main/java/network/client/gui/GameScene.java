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

    private AnchorPane root = new AnchorPane();
    private String typeHandCard1 = null;
    private String typeHandCard2 = null;
    private String typeHandCard3 = null;
    private String idHandCard1 = null;
    private String idHandCard2 = null;
    private String idHandCard3 = null;
    private String idTopCardResourceDeck;
    private String idTopCardGoldDeck;
    private Pane specificSeedsPane;
    private Text specificSeedsText;
    private Label specificSeedsLabel;
    private Button playCard = new Button("Play Card");
    private Button flipCardToBack = new Button("Flip Card to back");
    private Button flipCardToFront = new Button("Flip Card to front");
    private Button drawCard = new Button("Draw card");
    private Button seeYourSpecificSeeds = new Button("See your seeds");
    private Button seeOtherPlayersBoards = new Button("See other players boards");
    private Button seeYourPoints = new Button("See your points");
    private Button endTurn = new Button("End turn");
    private String wellOrDeck = "notSelected";
    private double heightWellCards = 80;
    private double widthWellCards = 110;
    GridPane buttonContainer = new GridPane();
    Label chosenCardToPlace = new Label();
    Label chosenCardToBePlacedOn = new Label();
    Label chosenCorner = new Label();
    Label chosenDeckOrWell = new Label();

    private boolean isCurrentPlayerTurn = false;

    public GameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, String id, ClientView clientView, String currentPlayerNickname, boolean isFirstClient) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.initCardId = id;
        this.clientView = clientView;
        this.currentPlayerNickname = currentPlayerNickname;
        this.isFirstClient = isFirstClient;
        this.gameSceneController = new GameSceneController();
    }

    public void game(boolean isFirstClient) throws IOException {
        System.out.println("Initializing game data for client: " + clientView.getUserName());
        gameSceneController.initData(primaryStage, out, socket, in, clientView, currentPlayerNickname);
        if (isFirstClient) {
            System.out.println("First client updating setup");
            gameSceneController.updateFirst();
            out.println("SETUPFINISHED");
        } else {
            //System.out.println("Second client updating setup");
            in.readLine();
            gameSceneController.updateFirst();
            out.println("SETUPFINISHED");
        }
        while (true) {
            String message = in.readLine();
            if (message.equals("SETUPFINISHED")) {
                System.out.println("Current player " + in.readLine());
                break;
            }
        }

        System.out.println("Starting game for client: " + clientView.getUserName());
        gameSceneController.startGame(initCardId);
    }
}
