package it.polimi.ingsw.network.client.gui.controllers;
import it.polimi.ingsw.model.game.Dot;
import it.polimi.ingsw.network.client.gui.scene.ChooseNumOfPlayersScene;
import it.polimi.ingsw.network.client.gui.scene.LobbyScene;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LoginController {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;

    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;
    @FXML
    public Label loginLabel;
    @FXML
    public ToggleGroup toggleGroup;
    private ClientView clientView= new ClientView();

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) throws SocketException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
        this.clientView=cl;
        this.socket.setSoTimeout(60000);

    }

    @FXML
    public void initialize() {
        loginButton.setDisable(true); //disabling login button unless client has inserted his username
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());        //Listener to see if the field has been completed
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private void checkFields() {
        // Checking both fields
        String username = usernameField.getText();
        Toggle dot = toggleGroup.getSelectedToggle();
        loginButton.setDisable(username.isEmpty() || dot == null);
    }

    @FXML
    public void loginButtonClicked() {
        new Thread(() -> {
            try {

                String username = usernameField.getText();
                Toggle dot = toggleGroup.getSelectedToggle();

                if (username.isEmpty() || dot == null) {
                    Platform.runLater(() -> {
                        if (username.isEmpty()) {
                            loginLabel.setText("Write your username");
                        }

                        if (dot == null) {
                            loginLabel.setText("Choose a dot color");
                            loginLabel.setStyle("-fx-font-size: 20px;" +
                                    "-fx-font-family: Arial;" +
                                    "-fx-text-fill: #EF8156;" +
                                    "-fx-padding: 10px;" +
                                    "-fx-border-color: #EF8156;" +
                                    "-fx-border-width: 1px;" +
                                    "-fx-border-radius: 5px;");
                        }
                    });
                    return;
                }

                String chosenName= usernameField.getText();

                out.println(chosenName); // Sending username to the server
                String whatServerSays= null;
                try {
                    whatServerSays = in.readLine();
                } catch (IOException e) {
                    handleDisconnection();
                }
                assert whatServerSays != null;
                if (whatServerSays.equals("Username already taken. Please choose another username:")) {
                    Platform.runLater(() -> {
                        loginLabel.setText("Username already taken. Please choose another username:");
                        loginLabel.setStyle("-fx-font-size: 20px;" +
                                "-fx-font-family: Arial;" +
                                "-fx-text-fill: #EF8156;" +
                                "-fx-padding: 10px;" +
                                "-fx-border-color: #EF8156;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-radius: 5px;");
                        usernameField.clear();
                    });
                    return;
                }

                clientView.setUserName(username);

                in.readLine(); //Choose the color of
                String realChosenDot = ((RadioButton) dot).getText();
                clientView.setDot(Dot.valueOf(realChosenDot));
                out.println(realChosenDot);

                if (in.readLine().equals("Chosen color not available!")) {
                    Platform.runLater(() -> {
                        loginLabel.setText("Chosen color not available! Choose another color:");
                        loginLabel.setStyle("-fx-font-size: 20px;" +
                                "-fx-font-family: Arial;" +
                                "-fx-text-fill: #EF8156;" +
                                "-fx-padding: 10px;" +
                                "-fx-border-color: #EF8156;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-radius: 5px;");
                    });
                    return;
                }
                String mexFromS= in.readLine();
                if(mexFromS.equals("Choose the number of players(2-4): ")) {
                    Platform.runLater(() -> {
                        ChooseNumOfPlayersScene chooseNumOfPlayersScene = new ChooseNumOfPlayersScene();
                        try {
                            chooseNumOfPlayersScene.createChooseNumOfPlayersScene(primaryStage, out, socket, in, clientView);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else {
                    Platform.runLater(()-> showAlert("Someone is online!","Please wait until the first client chooses the number of players!"));
                    in.readLine();
                    LobbyScene lobbySceneHandler = new LobbyScene();
                    in.readLine();
                    lobbySceneHandler.createLobbyScene(primaryStage, out, socket, in, clientView, null, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

}