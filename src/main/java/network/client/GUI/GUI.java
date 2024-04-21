package network.client.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    Button returnToMainMenu;
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
        returnToMainMenu = new Button("Return to main menu");
        returnToMainMenu.setOnAction(e -> window.setScene(startScene));

        ImageView codexLogo = new ImageView(new Image(getClass().getResourceAsStream("/ImmaginiCodex/CodexLogo.png")));

        codexLogo.fitHeightProperty().bind(primaryStage.heightProperty());
        codexLogo.fitWidthProperty().bind(primaryStage.widthProperty());
        codexLogo.setPreserveRatio(true);
        codexLogo.setOpacity(20);

        Region spacer = new Region();
        spacer.setPrefHeight(100);

        //Layout StartScene
        VBox startLayout = new VBox();
        startLayout.getChildren().addAll(codexLogo, spacer, start);
        startLayout.setAlignment(Pos.CENTER);
        //StackPane.setAlignment(startLayout, Pos.CENTER);

        startLayout.setPrefHeight(100);

        StackPane root = new StackPane(startLayout);
        startScene= new Scene(root, 800, 600);

        //Layout GameScene
        VBox gameLayout=new VBox();
        gameLayout.setAlignment(Pos.CENTER);
        StackPane.setAlignment(gameLayout, Pos.CENTER);
        StackPane root2 = new StackPane(gameLayout);
        gameLayout.getChildren().addAll(new Label("Game"), returnToMainMenu);
        gameScene= new Scene(root2, 800, 600);

        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }

}

