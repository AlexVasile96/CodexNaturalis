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
    public Label loginLabel;
    @FXML
    public ToggleGroup toggleGroup;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
    }

    @FXML
    public void initialize() {
        // Disabilita il pulsante di login all'avvio
        loginButton.setDisable(true);

        // Aggiungi un listener per verificare quando uno dei campi viene compilato
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkFields();
        });

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            checkFields();
        });
    }

    private void checkFields() {
        // Controlla se entrambi i campi sono compilati
        String username = usernameField.getText();
        Toggle dot = toggleGroup.getSelectedToggle();

        // Abilita il pulsante di login solo se entrambi i campi sono compilati
        loginButton.setDisable(username.isEmpty() || dot == null);
    }

    @FXML
    public void loginButtonClicked(ActionEvent event) throws IOException {

        System.out.println(in.readLine());
        String username = usernameField.getText();
        Toggle dot = toggleGroup.getSelectedToggle();

        if(username.isEmpty() || dot == null){
            if (username.isEmpty()) {
                System.out.println("Username necessary");
                loginLabel.setText("Write your username");
            }

            if (dot == null) {
                loginLabel.setText("choose a dot color");
                loginLabel.setStyle("-fx-font-size: 20px;" + // Dimensione del font
                        "-fx-font-family: Arial;" + // Famiglia del font
                        "-fx-text-fill: #EF8156;" + // Colore del testo
                        "-fx-padding: 10px;" + // Spaziatura interna
                        "-fx-border-color: #EF8156;" + // Colore del bordo
                        "-fx-border-width: 1px;" + // Spessore del bordo
                        "-fx-border-radius: 5px;" );// Arrotondamento del bordo
                System.out.println("Choose your dot color please");
            }
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
        if(in.readLine().equals("Chosen color not available!")){
            return;
        }


        ChooseNumOfPlayersScene chooseNumOfPlayersScene= new ChooseNumOfPlayersScene();
        chooseNumOfPlayersScene.createChooseNumOfPlayersScene(primaryStage,out,socket,in);
    }
}