package network.client.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import server.ServerConnection;
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
        connectToServer();




    }

    /*private void startMenuScene(Stage primaryStage) throws IOException {

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
        //pronto.println(firstMessage);
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
        loginScene = new Scene(rootGame, 800, 600);

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
            PrintWriter newPrintWriter = new PrintWriter(socket.getOutputStream(),true);
            newPrintWriter.println(username);
        }else{
            System.out.println("Username necessario");
        }
    }*/

    @FXML
    public void connectToServer() throws IOException {
        try {
            FileReader reader = new FileReader("src/main/resources/HostAndPort.json"); // Reading JSON file to get host name and port number
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
            String hostName = null;                                                                   //INITIALIZING
            int portNumber = 0;                                                                       //INITIALIZING
            for (int i = 0; i < hostAndPortArray.length(); i++) {                                   // Iterating through the JSONArray to extract host names and port numbers.
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);                         // Get the JSONObject representing a host and port combination.
                hostName = hostAndPort.getString("hostName");                                   // Extract the host name from the JSONObject.
                portNumber = hostAndPort.getInt("portNumber");                                  // Extract the port number from the JSONObject.
                System.out.println("HostName: " + hostName);
                System.out.println("PortNumber: " + portNumber);
            }
            Socket socket = new Socket(hostName, portNumber);                                 // Creating the socket and connecting to the server
            System.out.println("Client connected!");                                        //Client successfully connected
            ClientView clientView = new ClientView();                                        //Giving the client a personal view
            GuiHandlerThread guiHandlerThread = new GuiHandlerThread(socket, clientView);     //Creating a new thread that will handle clients interactions
            guiHandlerThread.run();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void closeConnection(Socket socket) throws IOException {
        socket.close();
    }
}
