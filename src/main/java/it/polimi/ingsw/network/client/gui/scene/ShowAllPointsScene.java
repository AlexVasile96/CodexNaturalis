package it.polimi.ingsw.network.client.gui.scene;

import it.polimi.ingsw.view.ClientView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ShowAllPointsScene {

    private Stage primaryStage;
    private ClientView clientView;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String waitForCall;
    Text playerPoints = new Text();

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
        popupStage.setResizable(true);
        popupStage.initModality(Modality.WINDOW_MODAL); //prevents the interactions with the primary window while the popup is running

        StackPane root = new StackPane();
        VBox vbox = new VBox(10);
        showAllPts();
        vbox.getChildren().addAll(playerPoints);
        root.getChildren().add(vbox);
        Scene scene = new Scene(root, 400,400);
        popupStage.setScene(scene);
        popupStage.show();

    }

    private void showAllPts() throws IOException {
        out.println("showAllPoints");
        waitForCall = in.readLine();
        do {
            playerPoints.setText(playerPoints.getText() + "\n" + waitForCall);
            waitForCall = in.readLine();
        }while(!waitForCall.equals("exit"));
    }

}
