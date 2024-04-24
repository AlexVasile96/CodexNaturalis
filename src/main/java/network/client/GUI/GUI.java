package network.client.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import server.HandlingPlayerInputsThread;
import server.ServerLobby;
import view.ClientView;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
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
    private ClientView clientView;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        clientView = new ClientView();
        startMenuScene(primaryStage);
        loginScene();


    }

    private void startMenuScene(Stage primaryStage) {

        // Carica l'immagine di sfondo
        Image codexLogo = new Image(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        // Imposta lo sfondo del layout
        StackPane root = new StackPane();
        root.setBackground(background);

        // Aggiungi il pulsante "Start new game"
        start = new Button("Start new game");
        start.setOnAction(e -> {
            connectToServer();
            window.setScene(loginScene);
        });

        // Layout dei bottoni
        VBox buttonLayout = new VBox(20); // Spaziatura tra i bottoni
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().add(start);

        // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        root.getChildren().add(buttonLayout);

        // Crea la scena di avvio
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }




    private void loginScene() {

        Button loginButton = new Button("Login");
        Label test = new Label();

        StackPane rootGame = new StackPane();
        Label loginLabel = new Label("Write your username");
        TextField usernameField = new TextField();

        loginButton.setOnAction(e->{
            clientView.setUserName(usernameField.getText());
        });
        if(clientView.getUserName() != null) {
           test = new Label("Il tuo username è: "+ clientView.getUserName());
        }



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
