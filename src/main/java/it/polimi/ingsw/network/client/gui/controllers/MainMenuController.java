package it.polimi.ingsw.network.client.gui.controllers;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import it.polimi.ingsw.network.client.gui.scene.LoginScene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class MainMenuController {
    private Stage primaryStage;
    private static PrintWriter out;
    private static Socket socket;
    private static BufferedReader in;
    private ClientView clientview=new ClientView();

    private Image codexLogo = null;
    private Scene startScene;
    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView cl) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in=in;
        this.clientview=cl;

    }

    public void startMenuScene(Stage primaryStage) throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/mainMenu.fxml")));
        //codexLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png")));
//        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
//        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//        Background background = new Background(backgroundImage);
        StackPane root = new StackPane();
        //root.setBackground(background);
        root.getChildren().addAll(fxml); // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }


    @FXML
    private void startGameClicked(ActionEvent event) throws IOException {
        String firstMessage = "login";
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        out=new PrintWriter(socket.getOutputStream(), true);
        out.println(firstMessage);
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/loginScene.fxml"));
                Pane root = loader.load();
                LoginController controller = loader.getController();
                controller.initData(primaryStage, out, socket,in, clientview);
                Scene loginScene = new Scene(root, 800, 600);
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("Login");
                String serverResponse = in.readLine(); //HELLO YOU HAVE TO LOG IN PLEASEY INSERT YOUR USERNAME
                System.out.println(serverResponse);
                LoginScene loginSceneHandler = new LoginScene();
                loginSceneHandler.showLoginScene(primaryStage, out, socket, in, clientview);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void exitClicked(ActionEvent event) {
        try {
            closeConnection(socket);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeConnection(Socket socket) throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
