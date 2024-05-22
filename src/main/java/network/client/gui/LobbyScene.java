package network.client.gui;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class LobbyScene {
    private Scene lobbyScene;

    public void createLobbyScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, String initCard) {
        Platform.runLater(() -> {
            try {
                System.out.println();
                Parent fxmlLobby = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/lobby.fxml")));
                LobbyController controller = new LobbyController();
                controller.initData(primaryStage, out, socket,in, clientView,initCard);
                Pane root = new Pane();
                root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                root.getChildren().addAll(fxmlLobby);
                lobbyScene = new Scene(root, 800, 600);
                primaryStage.setScene(lobbyScene);
                Platform.runLater(controller::waitAllPlayers);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
