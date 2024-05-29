package it.polimi.ingsw.network.client.gui.controllers;
import it.polimi.ingsw.view.ClientView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import it.polimi.ingsw.network.client.gui.scene.LobbyScene;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChooseNumOfPlayersController {

    private Stage primaryStage;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    @FXML
    private ToggleGroup numOfPlayersGroup;
    @FXML
    public Label testNumbers;
    @FXML
    Button button;
    private int selectedNumOfPlayers;
    private ClientView clientView;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = cl;
    }

    @FXML
    public void initialize() {
        button.setDisable(true);
        numOfPlayersGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            checkFields();
        });
    }

    private void checkFields() {
        Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();
        button.setDisable(numOfPlayers == null);
    }

    @FXML
    private void goToLobbyClicked(ActionEvent event) throws IOException{
        Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();
        if (numOfPlayers == null) {
            System.out.println("Select number of players");
            return;
        }
        String selectedNumOfPlayersText = ((RadioButton) numOfPlayers).getText();
        switch (selectedNumOfPlayersText) {
            case "2 Players":
                selectedNumOfPlayers = 2;
                break;
            case "3 Players":
                selectedNumOfPlayers = 3;
                break;
            case "4 Players":
                selectedNumOfPlayers = 4;
                break;
            default:
                System.out.println("Not valid number of players");
                return;
        }
        testNumbers.setText("Number of players is: " + selectedNumOfPlayers);
        out.println(selectedNumOfPlayers);
        System.out.println(in.readLine()); //Players number correctly chosen
        System.out.println(in.readLine());
        System.out.println(in.readLine()); //You have to wait until all players are connected
        LobbyScene lobbySceneHandler = new LobbyScene();
        lobbySceneHandler.createLobbyScene(primaryStage, out, socket, in, clientView, null, 0);
    }
}

