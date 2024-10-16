package it.polimi.ingsw.network.client.gui.scene;
import it.polimi.ingsw.network.client.gui.controllers.ChooseNumOfPlayersController;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;


public class ChooseNumOfPlayersScene {
    //Initializing scene
    public void createChooseNumOfPlayersScene(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/ChooseNumberOfPlayers.fxml"));
        Pane root = loader.load();
        ChooseNumOfPlayersController controller = loader.getController();
        controller.initData(primaryStage, out, socket, in, clientView);
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        root.setBackground(background);
        Scene chooseScene = new Scene(root, 800, 600);
        primaryStage.setScene(chooseScene);

    }
}

