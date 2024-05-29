package it.polimi.ingsw.network.client.gui.scene;

import it.polimi.ingsw.view.ClientView;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Stack;

public class ShowAllPointsScene {

    private Stage primaryStage;
    private ClientView clientView;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    Label playerOneLabel = new Label("Player One");
    Label playerTwoLabel = new Label("Player Two");
    Label playerThreeLabel = new Label("Player Three");
    Label playerFourLabel = new Label("Player Four");
    int counter=0;

    public ShowAllPointsScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
    }

    public void showAllPointsPopup() throws IOException {
        Stage popupStage = new Stage();
        popupStage.setTitle("Board Points");
        popupStage.setResizable(false);
        popupStage.initModality(Modality.WINDOW_MODAL); //prevents the interactions with the primary window while the popup is running

        StackPane root = new StackPane();
        VBox vbox = new VBox(10);
        sucasuca();
        vbox.getChildren().addAll(playerOneLabel, playerTwoLabel, playerThreeLabel, playerFourLabel);
        root.getChildren().add(vbox);

    }

    private void sucasuca() throws IOException {
        out.println("showAllPoints");
        String string = in.readLine();

        while (!string.equals("exit")) {

            if(counter==0){
                playerOneLabel.setText(string);
            }
            if(counter==1){
                playerTwoLabel.setText(string);
            }
            if(counter==2 && !string.equals("exit")){
                playerThreeLabel.setText(string);
            }
            if(counter==3 && !string.equals("exit")){
                playerFourLabel.setText(string);
            }
            else{
                System.out.println("suca");
            }
            counter++;
            string = in.readLine();
        }
        System.out.println("sucasuca");
    }
}
