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
    private String currentPlayerNickname=null;

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
                            System.out.println("All clients are connected, choosing secret cards...");
                            SecretCardScene secretCardSceneHandler = new SecretCardScene();
                            secretCardSceneHandler.chooseSecretCard(primaryStage, out, socket, in, clientview);
                        });
                        break;
                    } else if (message.equals("All clients chose the init Card")) {
                        Platform.runLater(() -> {
                            System.out.println("All clients chose the initial card, starting game...");
                            handleInitCardChoice();
                        });
                        break;
                    } else if (message.equals("SETUPFINISHED")) {
                        System.out.println(message);
                        currentPlayerNickname = in.readLine();
                        System.out.println("Current Player:" +currentPlayerNickname);
                        String nextPlayer = in.readLine();
                        System.out.println("Next PLayer is:" + nextPlayer);

                            if (nextPlayer.equals(clientview.getUserName())) {
                                System.out.println("Setup finished, starting game...");
                                handleSetupFinished(currentPlayerNickname);
                            } else {
                                System.out.println("Not your turn, waiting for setup...");
                               waitForSetupCompletion();
                            }
                            break;
                    }
                    else if(message.equals("STARTGUI"))
                    {
                        System.out.println("All clients logged!");
                        executor.shutdown();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleInitCardChoice() {
        try {
            currentPlayerNickname= in.readLine();
            System.out.println("CurrentPlayerNickname is: " + currentPlayerNickname);
            if (in.readLine().equals("You are the first client")) {
                synchronized (LobbyController.class) {
                    if (gameScene == null) {
                        System.out.println("Initializing GameScene for the first client.");
                        gameScene = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname, true);
                    }
                }
                gameScene.game(true);
            } else {
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

    private void handleSetupFinished(String currentPlayerNickname) {
        try {

            System.out.println("Sono qua");
            synchronized (LobbyController.class) {
                if (gameScene == null) {
                    System.out.println("Initializing GameScene in setup finished.");
                    gameScene = new GameScene(primaryStage, out, socket, in, "82", clientview, currentPlayerNickname, false);
                }
            }
            System.out.println("Ready to start");
            gameScene.game(false);
            //executor.shutdownNow();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitForSetupCompletion() {
        try {
            String message = in.readLine();
            if (message.equals("SETUPFINISHED")) {
                synchronized (this) {
                    notifyAll();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
