package network.client.gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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


    private int selectedNumOfPlayers;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket=socket;
        this.in = in;
    }

    @FXML
    private void goToLobbyClicked(ActionEvent event) throws IOException, InterruptedException {
        Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();
        if (numOfPlayers == null) {
            System.out.println("Select number of players");
            return;
        }
        String howManyPlayersAreThere = in.readLine();
        System.out.println(howManyPlayersAreThere); //At the moment there is
        if (howManyPlayersAreThere.equals("There's already someone online! You will be ")) {
            System.out.println(in.readLine()); //2
            System.out.println(in.readLine());//players
        } else {
            System.out.println(in.readLine()); //1
            System.out.println(in.readLine()); //player.ChooseHowManyPlayers
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
            System.out.println(selectedNumOfPlayersText);
        }
        System.out.println(in.readLine()); //You have to wait until all players are connected
        LobbyScene lobbySceneHandler = new LobbyScene();
        lobbySceneHandler.createLobbyScene(primaryStage);
    }
}
