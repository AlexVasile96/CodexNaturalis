package it.polimi.ingsw.network.client.gui.controllers;

import it.polimi.ingsw.view.ClientView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PointsSceneController {

    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private ClientView clientView;
    String dot;
    Color dotColor;

    public PointsSceneController(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) throws IOException {
        this.primaryStage = primaryStage;
        this.out = out;
        this.socket = socket;
        this.in = in;
        this.clientView = clientView;
    }

    //managing player's selection
    public void getDotColor(){
        dot = clientView.getDot().toString();
        System.out.println("Il dot è: "+dot);
        if(dot.contains("RED")){
            dot = "RED";
        }
        if(dot.contains("GREEN")){
            dot = "GREEN";
        }
        if(dot.contains("BLUE")){
            dot = "BLUE";
        }
        if(dot.contains("YELLOW")){
            dot = "YELLOW";
        }
        dotColor = Color.web(dot);
    }

    public int getScore() throws IOException {
        out.println("showPoints");
        String stringa = in.readLine();
        if(stringa.equals("Unknown command.")) {
            return 25;
        }
        System.out.println(stringa);

        return Integer.parseInt(stringa);
    }
    public String[] getAllScores() throws IOException {
        String[] allScores = new String[16];
        out.println("showAllPoints");
        for(int i = 0; i < 15;i++){
            allScores[i] = in.readLine();
            if (allScores[i].equals("exit")) {
                return allScores;
            }
        }
        return allScores;
    }
}
