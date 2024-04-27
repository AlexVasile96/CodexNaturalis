package network.client.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    private Socket socket;
    private ClientView clientView = new ClientView();

    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;



    public static void main(String[] args) {
        launch(args);
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
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        // Imposta lo sfondo del layout
        StackPane root = new StackPane();
        root.setBackground(background);

        // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        root.getChildren().addAll(fxml);

        // Crea la scena di avvio
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }

    public void startGameClicked(ActionEvent event) throws IOException {
        String firstMessage = "login";
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        connectToServer();
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        printWriter.println(firstMessage);
        loginScene();
        primaryStage.setScene(loginScene);
    }




    private void loginScene() throws IOException {

        /*Button loginButton = new Button("Login");
        Label test = new Label();
        StackPane rootGame = new StackPane();
        Label loginLabel = new Label("Write your username");
        TextField usernameField = new TextField();
        loginButton.setOnAction(e->{
            String username = usernameField.getText();
          if(!username.isEmpty()){
              clientView.setUserName(username);
              setUsername(username);
              test.setText("Il tuo username è: " + username);
          }else{
              System.out.println("Username necessario");
          }
        });
        returnToMainMenu = new Button("Back to main menu");
        returnToMainMenu.setOnAction(e -> {
            window.setScene(startScene);
            try {
                closeConnection(socket);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        VBox loginLayout = new VBox(20); // Spaziatura tra i nodi
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(loginLabel, usernameField, loginButton, test, returnToMainMenu);
        loginLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        rootGame.getChildren().addAll(loginLayout);

        // Inizializza la scena di gioco
        loginScene = new Scene(rootGame, 800, 600);*/

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
            setUsername(username);
            test.setText("Il tuo username è: " + username);
        }else{
            System.out.println("Username necessario");
        }
    }



    private  void setUsername(String username){
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println(username);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private Socket connectToServer() {
        try {

            FileReader reader = new FileReader("src/main/resources/HostAndPort.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");

            String hostName = null;
            int portNumber = 0;
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                hostName = hostAndPort.getString("hostName");
                portNumber = hostAndPort.getInt("portNumber");
            }

            this.socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            System.err.println("Connection failed\n");
        }
        return socket;
    }

    private void closeConnection(Socket socket) throws IOException {
        socket.close();
    }
}
