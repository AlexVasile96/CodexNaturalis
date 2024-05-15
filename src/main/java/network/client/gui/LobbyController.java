package network.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class LobbyController {
    //guarda in.readLine stampa e vedi cosa stampa allCLients logged
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;


    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in=in;
    }
    public void waitAllPlayers() throws IOException {
        System.out.println(in.readLine());
    }
}
