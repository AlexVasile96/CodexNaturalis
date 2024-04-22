package network.client.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class GUI extends Application {

    Button start;
    Button returnToDesktop;
    Button returnToMainMenu;
    Stage window;
    Scene startScene;
    Scene gameScene;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        StackPane rootGame = new StackPane();


        // Carica l'immagine
        Image codexLogo = new Image(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png"));

        // Crea un'immagine di sfondo adattabile alla dimensione della finestra
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        // Crea uno StackPane per sovrapporre l'immagine di sfondo con altri nodi
        StackPane root = new StackPane();

        // Imposta lo sfondo del layout
        root.setBackground(background);

        // Aggiungi il pulsante "Start new game"
        start = new Button("Start new game");
        start.setOnAction(e -> {
            gameScene.setRoot(rootGame);
            window.setScene(gameScene);
            primaryStage.setWidth(gameScene.getWidth());
            gameScene.getWindow().setHeight(primaryStage.getHeight());

        });

        // Layout dei bottoni
        VBox buttonLayout = new VBox(20); // Spaziatura tra i bottoni
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().add(start);

        // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        root.getChildren().add(buttonLayout);

        // Crea le scene
        startScene = new Scene(root, 919, 743);




        // Aggiungi il pulsante "Start new game"
        Label gameLabel = new Label("Game Scene");
        returnToMainMenu = new Button("Back to main menu");
        returnToMainMenu.setOnAction(e -> window.setScene(startScene));

        VBox gameLayout = new VBox(20); // Spaziatura tra i nodi
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.getChildren().addAll(gameLabel, returnToMainMenu);
        gameLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        rootGame.getChildren().addAll(gameLayout);
        gameScene= new Scene(rootGame, 800, 600);


        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();





        /*window=primaryStage;
        start=new Button("Start new game");
        start.setOnAction(e -> window.setScene(gameScene));
        returnToMainMenu = new Button("Return to main menu");
        returnToMainMenu.setOnAction(e -> window.setScene(startScene));

        //Layout GameScene
        VBox gameLayout=new VBox();
        gameLayout.setAlignment(Pos.CENTER);
        StackPane.setAlignment(gameLayout, Pos.CENTER);
        StackPane root2 = new StackPane(gameLayout);
        gameLayout.getChildren().addAll(new Label("Game"), returnToMainMenu);
        gameScene= new Scene(root2, 800, 600);

        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();*/
    }

}

