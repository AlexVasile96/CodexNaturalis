package network.client.gui.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import network.client.gui.scene.SecretCardScene;
import network.client.gui.scene.GameScene;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
    private String initCardId=null;
    private int isFront;
    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl, String initCardId, int isFront) throws SocketException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.socket.setSoTimeout(60000);
        this.in = in;
        this.executor = Executors.newSingleThreadExecutor();
        this.clientview = cl;
        this.initCardId=initCardId;
        this.isFront=isFront;
    }

    public void waitAllPlayers() {
        executor.execute(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("All clients connected")) {
                        Platform.runLater(() -> {
                            System.out.println("All clients are connected, choosing secret cards...");
                            //Da fare qua il controllo sul numero di giocarori
                            String whatIsYourIndex=null;
                            int gameSize=0;
                            try {
                                gameSize= Integer.parseInt(in.readLine());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                whatIsYourIndex=in.readLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if(Integer.parseInt(whatIsYourIndex)<=gameSize) {
                                SecretCardScene secretCardSceneHandler = new SecretCardScene();
                                secretCardSceneHandler.chooseSecretCard(primaryStage, out, socket, in, clientview);
                            }
                            else{
                                System.out.println("Number of player is already full");
                                handleDisconnection();
                            }

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
                               waitAllPlayers();
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
                handleDisconnection();
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
                        gameScene = new GameScene(primaryStage, out, socket, in, initCardId, clientview, currentPlayerNickname, true,isFront);
                        primaryStage.setTitle("Codex");
                    }
                }
                gameScene.game(true);
            } else {
                synchronized (LobbyController.class) {
                    if (gameScene == null) {
                        System.out.println("Initializing GameScene for other clients.");
                        //gameScene = new GameScene(primaryStage, out, socket, in, initCardId, clientview, currentPlayerNickname, false,isFront);
                        primaryStage.setTitle("Codex");
                    }
                }
                System.out.println("Waiting for game to start...");
                waitAllPlayers();
            }
        } catch (IOException e) {
            handleDisconnection();
        }
    }

    private void handleSetupFinished(String currentPlayerNickname) {
        try {

            System.out.println("Sono qua");
            synchronized (LobbyController.class) {
                if (gameScene == null) {
                    System.out.println("Initializing GameScene in setup finished.");
                    gameScene = new GameScene(primaryStage, out, socket, in, initCardId, clientview, currentPlayerNickname, false,isFront);
                }
            }
            System.out.println("Ready to start");
            gameScene.game(false);
            //executor.shutdownNow();

        } catch (IOException e) {
            handleDisconnection();
        }
    }
    private void handleDisconnection() {
        Platform.runLater(() -> {
            // Show an alert indicating the disconnection
            showAlert("Disconnection", "Lobby is full.");
            try {
                // Close resources
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                // Exit the application
                Platform.exit();
                System.exit(0);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // Throw a runtime exception if an IOException occurs
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * Shows an alert with the given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
