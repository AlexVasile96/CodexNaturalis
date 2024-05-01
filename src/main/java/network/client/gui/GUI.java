package network.client.gui;

import controller.GameController;
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
import view.ClientView;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GUI extends Application {

    private Button start;
    private Button returnToDesktop;
    private Button returnToMainMenu;
    private Stage window;
    private Scene startScene;
    private Scene loginScene;
    private Scene gameScene;
    private Scene chooseNumOfPlayersScene;
    private int selectedNumOfPlayers;
    private boolean isFirstPlayer=false;

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

    public GUI() throws IOException {
    }


    public static void main(String[] args) throws IOException {

        ConnectionWithServer connectionWithServer= new ConnectionWithServer(); //creazione classe
        socket= connectionWithServer.connectToServer();
        out=new PrintWriter(socket.getOutputStream(), true); //to write

        launch(args); //default
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        startMenuScene(primaryStage);
    }

    private void startMenuScene(Stage primaryStage) throws IOException {

        // Carica l'immagine di sfondo
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

    public void startGameClicked(ActionEvent event) throws IOException {
        String firstMessage = "login";
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        out.println(firstMessage); //-> il client ha detto login
        loginScene();
        primaryStage.setScene(loginScene);

    }




    private void loginScene() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/model/loginScene.fxml"));
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




        /*if(seiIlPrimoPlayer){
            chooseNumOfPlayers();
        }*/
        /*chooseNumOfPlayers();
        primaryStage.setScene(chooseNumOfPlayers());*/

        //COLORE PECIOTTO
        //SE FIRST CLIENT-> SCEGLI NUMERO DI GIOCATORI DA 2 A 4
        //-> clients until #clients==gamecontroller.getsize-> changescene(lobby)
        //-> si cambia la scena e si arriva al game scene
    }

    private void chooseNumOfPlayers() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/model/ChooseNumberOfPlayers.fxml"));
        StackPane root = new StackPane();
        root.getChildren().addAll(fxml);
        chooseNumOfPlayersScene = new Scene(root, 800, 600);
    }

    @FXML
    private void goToLobbyClicked(ActionEvent event) throws IOException {
        if(isFirstPlayer) {
            Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();

            if (numOfPlayers.toString().equals("RadioButton[id=2, styleClass=radio-button]'2 Giocatori'")) {
                selectedNumOfPlayers = 2;
            }
            if (numOfPlayers.toString().equals("RadioButton[id=3, styleClass=radio-button]'3 Giocatori'")) {
                selectedNumOfPlayers = 3;
            }
            if (numOfPlayers.toString().equals("RadioButton[id=4, styleClass=radio-button]'4 Giocatori'")) {
                selectedNumOfPlayers = 4;
            }
            Label testNumbers = new Label();
            testNumbers.setText("Il numero di giocatori è: " + selectedNumOfPlayers);
            out.println(selectedNumOfPlayers);
        }
        else{

        }
    }




    public void closeConnection(Socket socket) throws IOException {
        socket.close();
    }
}