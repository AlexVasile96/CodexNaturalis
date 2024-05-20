package network.client.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import view.ClientView;

import java.io.*;
import java.net.Socket;

public class GUI extends Application {
    private static BufferedReader in;
    private static Socket socket;
    private static PrintWriter out;
    private ClientView clientview = new ClientView();

    @FXML
    public Button closeButton;
    @FXML
    public ScrollPane gameBoard;
    @FXML
    public ImageView card;
    @FXML
    public ImageView initCard;

    public static void main(String[] args) throws IOException {
        ConnectionWithServer connectionWithServer = new ConnectionWithServer(); //creazione classe
        socket = connectionWithServer.connectToServer();
        out = new PrintWriter(socket.getOutputStream(), true); //to write
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/mainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.initData(primaryStage, out, socket, in, clientview);
        Scene scene = new Scene(root, 919, 743);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
        controller.startMenuScene(primaryStage);
    }
}


//TO DO LIST IN GUI

//SINCRONIZZARE BENE LE CARTE DEL DECK
//DOT SULLA BOARDPOINTS CHE PRENDA I PUNTI DEL PLAYER E CI FICCHI SOPRA IL CAZZAFà
//FARE IN MODO CHE VENGA ANCHE AGGIORNATA LA TOP CARD DEI DUE DECK -> DA FARE
//AGGIORNARE LA GAMESCENE DELL'ALTRO PLAYER CON LA ENDTURN
//BOTTONE QUIT-> quando un player quitta, passa il turno al client successivo