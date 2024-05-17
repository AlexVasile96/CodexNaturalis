package network.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.game.Game;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyController {
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private ExecutorService executor;
    private ClientView clientview;
    private static GameScene gameScene;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.executor = Executors.newSingleThreadExecutor();
        this.clientview = cl;
    }

    public void waitAllPlayers() {
        executor.execute(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("All clients connected")) {
                        Platform.runLater(() -> {
                            System.out.println("ciao");
                            SecretCardScene secretCardSceneHandler = new SecretCardScene();
                            secretCardSceneHandler.chooseSecretCard(primaryStage, out, socket, in, clientview);
                        });
                        break;
                    } else if (message.equals("All clients chose the init Card")) {
                        System.out.println("Received message: " + message); // Debug per gli altri if
                        Platform.runLater(() -> {
                            System.out.println("seconda lobby");
                            handleInitCardChoice();
                        });
                        break;
                    } else if (message.equals("SETUPFINISHED")) {
                        System.out.println("Received message: " + message); // Debug per gli altri if
                        Platform.runLater(() -> {
                            System.out.println("Setup finished, starting game...");
                            handleSetupFinished();
                        });
                        break;
                    } else {
                        // Se il messaggio non è riconosciuto, continua ad aspettare
                        System.out.println("Unknown message, waiting for correct message..."); // Debug per gli altri if
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleInitCardChoice() {
        try {
            String currentPlayerNickname = in.readLine();
            System.out.println("CurrentPlayerNickname is: " + currentPlayerNickname);
            if (in.readLine().equals("You are the first client")) {
                // Inizializza GameScene solo se non è già stato inizializzato
                synchronized (LobbyController.class) {
                    if (gameScene == null) {
                        System.out.println("Initializing GameScene for the first client.");
                        gameScene = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname, true);
                    }
                }
                gameScene.game(true);
            } else {
                // Altri client aspettano il messaggio di setup finito
                synchronized (LobbyController.class) {
                    if (gameScene == null) {
                        System.out.println("Initializing GameScene for other clients.");
                        gameScene = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname, false);
                    }
                }
                System.out.println("Waiting for game to start...");
                waitAllPlayers();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSetupFinished() {
        try {
            String currentPlayerNickname = in.readLine();
            System.out.println("CurrentPlayerNickname in setup finished: " + currentPlayerNickname);
            // Verifica che gameScene sia inizializzato
            System.out.println("Sono qua");
            synchronized (LobbyController.class) {
                if (gameScene == null) {
                    System.out.println("Initializing GameScene in setup finished.");
                    gameScene = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname, false);
                }
            }
            System.out.println("Ready to start");
            gameScene.game(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
