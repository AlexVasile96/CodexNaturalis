package network.client.gui;
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
public class InitCardScene {

    private int isFront=0; //The server need 1 to place the card on the back and 0 to place it on the front

    public void chooseInitCard(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) throws IOException {
        Pane root = new Pane();
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        root.setBackground(background);

        VBox vbox = new VBox();
        HBox hbox = new HBox();

        Text initText = new Text("YOUR INIT CARD");
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        String id = in.readLine();
        System.out.println(id);
        String pathInit = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
        Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathInit)));
        ImageView initCard = new ImageView(initImage);

        initCard.setFitWidth(165);
        initCard.setFitHeight(120);

        Button turnBack = new Button("Turn card to back");
        Button turnFront = new Button("Turn card to front");
        Button placeCard = new Button("Place the init card on the board");

        hbox.getChildren().addAll(turnFront, turnBack);

        vbox.getChildren().addAll(initText, initCard, hbox, placeCard);

        root.getChildren().add(vbox);

        Scene initCardScene = new Scene(root, 800, 600);
        primaryStage.setScene(initCardScene);

        turnBack.setOnMouseClicked(e->{
            int idToInt= Integer.parseInt(id);
            isFront=0;
            if(idToInt>=81 && idToInt<=86) //InitCard
            {
                String pathFlipped = "/ImmaginiCodex/CarteBack/Init/" + id + ".png";
                Image initImageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
                initCard.setImage(initImageBack);
                //setInitCard(initCard);
            }
        });

        turnFront.setOnMouseClicked(e->{
            int idToInt= Integer.parseInt(id);
            isFront=1;
            if(idToInt>=81 && idToInt<=86) //InitCard
            {
                String pathFlipped = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
                Image initImageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
                initCard.setImage(initImageFront);
                //setInitCard(initCard);
            }
        });

        placeCard.setOnMouseClicked(e->{
            out.println(isFront);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            update();
            game();
            Platform.runLater(() -> primaryStage.setScene(gameScene));
        });
    }
}
