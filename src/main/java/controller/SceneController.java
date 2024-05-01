package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SceneController {

    private Button start;
    private Button returnToDesktop;
    private Button returnToMainMenu;
    private Stage window;
    private Scene startScene;
    private Scene loginScene;
    private Scene gameScene;
    private Socket socket;
    private ClientView clientView = new ClientView();

    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;


    public SceneController() {}

    public void startMenuScene(Stage primaryStage) throws IOException {

    }
}