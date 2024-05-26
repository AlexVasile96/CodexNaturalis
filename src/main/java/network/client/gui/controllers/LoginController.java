package network.client.gui.controllers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.game.Dot;
import network.client.gui.scene.ChooseNumOfPlayersScene;
import network.client.gui.scene.LobbyScene;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

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

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
        this.clientView=cl;

    }

    @FXML
    public void initialize() {
        loginButton.setDisable(true); //disabling login button unless client has insert his username
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
    public void loginButtonClicked(ActionEvent event) throws IOException {
        new Thread(() -> {
            try {

                String username = usernameField.getText();
                Toggle dot = toggleGroup.getSelectedToggle();

                if (username.isEmpty() || dot == null) {
                    Platform.runLater(() -> {
                        if (username.isEmpty()) {
                            System.out.println("Username necessary");
                            loginLabel.setText("Write your username");
                        }

                        if (dot == null) {
                            loginLabel.setText("Choose a dot color");
                            loginLabel.setStyle("-fx-font-size: 20px;" + // Dimensione del font
                                    "-fx-font-family: Arial;" + // Famiglia del font
                                    "-fx-text-fill: #EF8156;" + // Colore del testo
                                    "-fx-padding: 10px;" + // Spaziatura interna
                                    "-fx-border-color: #EF8156;" + // Colore del bordo
                                    "-fx-border-width: 1px;" + // Spessore del bordo
                                    "-fx-border-radius: 5px;"); // Arrotondamento del bordo
                            System.out.println("Choose your dot color please");
                        }
                    });
                    return;
                }

                String chosenName= usernameField.getText();

                out.println(chosenName); // Sending username to the server
                String whatServerSays= in.readLine();
                System.out.println(whatServerSays);
                if (whatServerSays.equals("Username already taken. Please choose another username:")) {
                    Platform.runLater(() -> {
                        loginLabel.setText("Username already taken. Please choose another username:");
                        loginLabel.setStyle("-fx-font-size: 20px;" + // Dimensione del font
                                "-fx-font-family: Arial;" + // Famiglia del font
                                "-fx-text-fill: #EF8156;" + // Colore del testo
                                "-fx-padding: 10px;" + // Spaziatura interna
                                "-fx-border-color: #EF8156;" + // Colore del bordo
                                "-fx-border-width: 1px;" + // Spessore del bordo
                                "-fx-border-radius: 5px;"); // Arrotondamento del bordo
                        usernameField.clear();
                    });
                    return;
                }

                clientView.setUserName(username);
                System.out.println("This clientview username is: " + clientView.getUserName());
                System.out.println(in.readLine()); //Choose the color of
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
                System.out.println(mexFromS);
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
                    System.out.println(in.readLine());
                    LobbyScene lobbySceneHandler = new LobbyScene();
                    System.out.println(in.readLine());
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
}