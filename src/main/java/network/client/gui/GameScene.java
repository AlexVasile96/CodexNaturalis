package network.client.gui;

import controller.GameController;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameScene {
    private Stage primaryStage;
    private static PrintWriter out;
    private static Socket socket;
    private static BufferedReader in;
    private String initCardId;

    public GameScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, String id) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in=in;
        this.initCardId=id;
        System.out.println(initCardId); //debugging
    }

    public void game() throws IOException {

        GameSceneController controller = new GameSceneController();
        controller.initData(primaryStage, out, socket,in);
        controller.updateFirst();
        controller.startGame(initCardId);

    }


}
