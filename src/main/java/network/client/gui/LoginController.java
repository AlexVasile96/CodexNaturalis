package network.client.gui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    public ToggleGroup toggleGroup;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
    }

    @FXML
    public void loginButtonClicked(ActionEvent event) throws IOException {
        System.out.println(in.readLine());
        String username = usernameField.getText();
        if (username.isEmpty()) {
            System.out.println("Username necessary");
            return;
        }
        Toggle dot = toggleGroup.getSelectedToggle();
        if (dot == null) {
            System.out.println("Choose your dot color please");
            return;
        }

        //clientView.setUserName(username);
        //System.out.println(clientView.getUserName());
        out.println(username);
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        String realChosenDot = ((RadioButton) dot).getText();
        //clientView.setDot(Dot.valueOf(realChosenDot));
        out.println(realChosenDot);
        System.out.println(in.readLine());
        //IF->IN.READLINE SBORRATA ->>>>> ALLORA LABEL === COLOR CHOSEN NOT AVAILABLE ->>> RETURN
        //primaryStage.setScene(chooseNumOfPlayersScene);
        //inizializzazione controller
        ChooseNumOfPlayersScene chooseNumOfPlayersScene= new ChooseNumOfPlayersScene();
        chooseNumOfPlayersScene.createChooseNumOfPlayersScene(primaryStage,out,socket,in);
    }
}