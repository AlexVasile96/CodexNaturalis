package network.client.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class GUI extends Application {

    private Button start;
    private Button returnToDesktop;
    private Button returnToMainMenu;
    private Stage window;
    private Scene startScene;
    private Scene gameScene;
    private Socket socket;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        // Inizializza la scena di gioco
        initializeGameScene();

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
            window.setScene(gameScene);
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

    private void initializeGameScene() {
        StackPane rootGame = new StackPane();

        // Aggiungi il pulsante "Back to main menu"
        Label gameLabel = new Label("Game Scene");
        returnToMainMenu = new Button("Back to main menu");
        returnToMainMenu.setOnAction(e -> {
            window.setScene(startScene);
            try {
                closeConnection();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        VBox gameLayout = new VBox(20); // Spaziatura tra i nodi
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.getChildren().addAll(gameLabel, returnToMainMenu);
        gameLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        rootGame.getChildren().addAll(gameLayout);

        // Inizializza la scena di gioco
        gameScene = new Scene(rootGame, 800, 600);
    }

    private Socket connectToServer() {
        Socket socket = null;
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

            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            System.err.println("Connection failed\n");
        }
        return socket;
    }
    private void closeConnection() throws IOException {
        Socket socket = connectToServer();
        if (socket != null) {
            socket.close();
        }
    }
}
