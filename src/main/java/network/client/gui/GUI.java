package network.client.gui;


import controller.GuiController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.game.Dot;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class GUI extends Application {

    private static Stage window;
    private Scene startScene;
    private Scene loginScene;
    private static BufferedReader in;
    private int isFront=2; //The server need 1 to place the card on the back and 2 to place it on the front

    private Button start;
    private Button returnToDesktop;
    private Button returnToMainMenu;

    private Scene gameScene;
    private Scene chooseNumOfPlayersScene;
    private Scene lobbyScene;
    private Scene chooseSecretObjectiveScene;
    private Scene chooseInitCardScene;
    private int selectedNumOfPlayers;
    private static GuiController guiController = null;
    private String id;
    private ClientView clientView = new ClientView();
    private static Socket socket;
    private static PrintWriter out;
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;
    @FXML
    public ToggleGroup toggleGroup;
    @FXML
    public Label testDot;
    @FXML
    public ToggleGroup numOfPlayersGroup;
    @FXML
    public Label testNumbers;
    @FXML
    public ImageView obiettivo1;
    @FXML
    public ImageView obiettivo2;
    @FXML
    public Button chooseObj;
    @FXML
    public ImageView initCard;
    @FXML
    public ImageView resourceCard;
    @FXML
    public ImageView goldCard;
    @FXML
    public ScrollPane gameBoard;


    /*public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnectionWithServer connectionWithServer= new ConnectionWithServer();
        this.socket= connectionWithServer.connectToServer();
        out=new PrintWriter(socket.getOutputStream(), true); //to write
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        window = primaryStage;
        startMenuScene(primaryStage);
        window.setScene(startScene);
        window.setTitle("Codex");
        window.show();
    }

    private void startMenuScene(Stage primaryStage) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/model/mainMenu.fxml"));
        Image codexLogo = new Image(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        // Imposta lo sfondo del layout
        StackPane root = new StackPane();
        root.setBackground(background);
        root.getChildren().addAll(fxml); // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        // Crea la scena di avvio
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();

    }

    @FXML
    private void startGameClicked(ActionEvent event) {
        Thread loginThread = new Thread(this::login);
        loginThread.start();

        //String firstMessage = "login";
        //        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //        out.println(firstMessage); //-> il client ha detto login
        //        loginScene();
        //        primaryStage.setScene(loginScene);
    }

    private void login() {
        try {
            String firstMessage = "login";
            //Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //VADO A REINIZIALIZZARE PRIMARY STAGE
            out.println(firstMessage);
            String response = in.readLine();
            if (response != null ) {
                Platform.runLater(() -> {
                    try {
                        loginScene();
                        window.setScene(loginScene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("Login failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/model/loginScene.fxml"));
        loginScene = new Scene(root, 800, 600);


        //   Parent fxml = FXMLLoader.load(getClass().getResource("/model/loginScene.fxml"));
        //        StackPane root = new StackPane();
        //        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        //        root.getChildren().addAll(fxml);
        //        loginScene = new Scene(root, 800, 600);
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {
        String username = usernameField.getText();
        if (!username.isEmpty()) {
            test.setText("Il tuo username è: " + username);
            out.println(username);
        } else {
            System.out.println("Username necessario");
        }
        //peciotti + parte un altro thread
    }

    //@FXML
    /*private void initialize() {
        try {
            ConnectionWithServer connectionWithServer = new ConnectionWithServer();
            socket = connectionWithServer.connectToServer();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*@Override
    public void stop() throws Exception {
        super.stop();
        if (socket != null) {
            socket.close();
        }
    }
}*/


/*import controller.GameController;
import controller.GuiController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.game.Dot;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import server.ServerConnection;
import view.ClientView;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI extends Application {

    private Button start;
    private Button returnToDesktop;
    private Button returnToMainMenu;
    private Stage window;
    private Scene startScene;
    private Scene loginScene;
    private Scene gameScene;
    private Scene chooseNumOfPlayersScene;
    private Scene lobbyScene;
    private int selectedNumOfPlayers;
    private static GuiController guiController = null;

    private ClientView clientView = new ClientView();
    private static Socket socket;
    private static PrintWriter out;
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;
    @FXML
    public ToggleGroup toggleGroup;
    @FXML
    public Label testDot;
    @FXML
    public ToggleGroup numOfPlayersGroup;
    @FXML
    public Label testNumbers;*/


    public static void main(String[] args) throws IOException {

        ConnectionWithServer connectionWithServer= new ConnectionWithServer(); //creazione classe
        socket= connectionWithServer.connectToServer();
        out=new PrintWriter(socket.getOutputStream(), true); //to write
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        launch(args); //default

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        startMenuScene(primaryStage);
        guiController = new GuiController(0);

    }

    private void startMenuScene(Stage primaryStage) throws IOException {

        // Carica l'immagine di sfondo
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/mainMenu.fxml")));
        Image codexLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        // Imposta lo sfondo del layout
        StackPane root = new StackPane();
        root.setBackground(background);
        root.getChildren().addAll(fxml); // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        // Crea la scena di avvio
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }

    public void startGameClicked(ActionEvent event) throws IOException {
        String firstMessage = "login";
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        out.println(firstMessage); //-> il client ha detto login
        loginScene();
        primaryStage.setScene(loginScene);

    }




    private void loginScene() throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/loginScene.fxml")));
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(fxml);
        loginScene = new Scene(root, 800, 600);
    }

    public void loginButtonClicked(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        if(!username.isEmpty()){
            clientView.setUserName(username);
            test.setText("Il tuo username è: " + clientView.getUserName());
            out.println(username);
        }else{
            System.out.println("Username necessario");
        }
        Toggle dot = toggleGroup.getSelectedToggle();

        String realChosenDot=null;
        if(dot.toString().equals("RadioButton[id=reddot, styleClass=radio-button]'RED'")){
            realChosenDot="RED";
        }
        if(dot.toString().equals("RadioButton[id=yellowdot, styleClass=radio-button]'YELLOW'")){
            realChosenDot="YELLOW";
        }
        if(dot.toString().equals("RadioButton[id=bluedot, styleClass=radio-button]'BLUE'")){
            realChosenDot="BLUE";
        }
        if(dot.toString().equals("RadioButton[id=greendot, styleClass=radio-button]'GREEN'")){
            realChosenDot="GREEN";
        }

        testDot.setText("Il colore scelto è: " + realChosenDot);
        out.println(realChosenDot);
        clientView.setDot(Dot.valueOf(realChosenDot));

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        chooseNumOfPlayers();
        primaryStage.setScene(chooseNumOfPlayersScene);


    }

    private void chooseNumOfPlayers() throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/ChooseNumberOfPlayers.fxml")));
        StackPane root = new StackPane();
        root.getChildren().addAll(fxml);
        chooseNumOfPlayersScene = new Scene(root, 800, 600);
    }

    @FXML
    private void goToLobbyClicked(ActionEvent event) throws IOException {
        if(!guiController.isSizeSet()) {
            Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();

            if (numOfPlayers.toString().equals("RadioButton[id=2, styleClass=radio-button]'2 Giocatori'")) {
                selectedNumOfPlayers = 1;
            }
            if (numOfPlayers.toString().equals("RadioButton[id=3, styleClass=radio-button]'3 Giocatori'")) {
                selectedNumOfPlayers = 3;
            }
            if (numOfPlayers.toString().equals("RadioButton[id=4, styleClass=radio-button]'4 Giocatori'")) {
                selectedNumOfPlayers = 4;
            }
            testNumbers.setText("Il numero di giocatori è: " + selectedNumOfPlayers);
            out.println(selectedNumOfPlayers);
            guiController.setSizeSet(true);

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //lobby();
            //primaryStage.setScene(lobbyScene);

            chooseSecretObjective();

            primaryStage.setScene(chooseSecretObjectiveScene);
        }
        else{
            testNumbers.setText("Quello che selezioni non conta niente, SCEMO, il numero di giocatori è: " + selectedNumOfPlayers);
        }
    }

    @FXML
    private void lobby() throws IOException{
        Parent fxmlLobby = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/lobby.fxml")));

        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().addAll(fxmlLobby);

        lobbyScene = new Scene(root, 800, 600);
    }

    @FXML
    private void chooseSecretObjective() throws IOException{
        Parent fxmlGame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/SceltaObiettivoSegreto.fxml")));
        Pane root = new Pane();
        root.getChildren().addAll(fxmlGame);
        chooseSecretObjectiveScene = new Scene(root, 800, 600);
    }

    @FXML
    private void chooseSecretObjectiveClicked() throws IOException {
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        String firstSecretCard = in.readLine();
        String secondSecretCard = in.readLine();

        String firstCardId = in.readLine();
        String secondCardId = in.readLine();

        String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + firstCardId + ".png";
        String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + secondCardId + ".png";

        Image objImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
        Image objImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
        obiettivo1.setImage(objImage1);
        obiettivo2.setImage(objImage2);
        System.out.println(firstSecretCard);
        System.out.println(secondSecretCard);
    }

    @FXML
    public void chosenObj1(MouseEvent event) throws IOException {
        out.println(1);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        primaryStage.setScene(chooseInitCardScene);
    }

    @FXML
    public void chosenObj2(MouseEvent event) throws IOException {
        out.println(2);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        primaryStage.setScene(chooseInitCardScene);
    }

    @FXML
    private void chooseInitCard() throws IOException {
        Parent fxmlInit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/FlipInitCard.fxml")));
        Pane root = new Pane();
        root.getChildren().addAll(fxmlInit);
        chooseInitCardScene = new Scene(root, 800, 600);
    }

    @FXML
    public void showInit() throws IOException {
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        id = in.readLine();
        String pathInit = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
        Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathInit)));
        initCard.setImage(initImage);
    }

    @FXML
    public void flipToBackCard() {
        int idToInt= Integer.parseInt(id);
        isFront=1;
        if(idToInt>=1 && idToInt <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(idToInt>40 && idToInt<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
        else if(idToInt>=81 && idToInt<=86) //InitCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Init/" + id + ".png";
            Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            initCard.setImage(initImage);
        }

    }

    @FXML
    public void flipToFrontCard() {
        int idToInt= Integer.parseInt(id);
        isFront=2;
        if(idToInt>=1 && idToInt <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(idToInt>40 && idToInt<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
        else if(idToInt>=81 && idToInt<=86) //InitCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
            Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            initCard.setImage(initImage);
        }
    }

    @FXML
    private void chooseInitCardFrontOrBack(ActionEvent event) throws IOException {
        in.readLine();
        out.println(isFront);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        game();
        primaryStage.setScene(gameScene);
    }

    private void game() throws IOException {
        Parent fxmlGame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/gameLayout.fxml")));
        Pane root = new Pane();
        gameBoard.setContent(initCard);
        root.getChildren().addAll(fxmlGame, gameBoard);

    }


    private void addCard(){
        out.println("playcard");
        ImageView card = new ImageView();
    }

    @FXML
    public void exitClicked(ActionEvent event) throws IOException {
        try {
            closeConnection(socket);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Socket socket) throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}