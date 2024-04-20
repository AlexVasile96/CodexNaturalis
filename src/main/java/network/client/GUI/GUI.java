package network.client.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

public class GUI extends Application {

    Button start;
    Button returnToDesktop;
    Stage window;
    Scene startScene;
    Scene gameScene;
    BorderPane borderPane = new BorderPane();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        start=new Button("Start new game");
        start.setOnAction(e -> window.setScene(gameScene));

        Region spacer = new Region();
        spacer.setPrefHeight(100);

        //Layout StartScene
        VBox startLayout = new VBox();
        startLayout.getChildren().addAll(spacer, start);
        startLayout.setAlignment(Pos.CENTER);
        StackPane.setAlignment(startLayout, Pos.CENTER);

        startLayout.setPrefHeight(100);

        StackPane root = new StackPane(startLayout);
        startScene= new Scene(root, 400, 300);

        //Layout GameScene
        VBox gameLayout=new VBox();
        gameLayout.setSpacing(10);
        gameLayout.setPadding(new javafx.geometry.Insets(10,10,10,10));
        gameLayout.getChildren().add(new Label("Game"));

        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }

}

