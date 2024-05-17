package network.client.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class GameSceneController {
    //POST ALEX
    private AnchorPane root=new AnchorPane();
    private static ImageView clickedImageView;
    private static int selectedCorner;
    private static volatile boolean isFirstThread = true;
    //PRE ALEX
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private int cardSelected;
    private String pathResourceDeck = null;
    private String pathGoldDeck = null;
    private ImageView topCardResourceDeckView = null;
    private ImageView topCardGoldDeckView = null;
    private Image topCardResourceDeck = null;
    private Image topCardGoldDeck = null;
    private String wellPathOne = null;
    private String wellPathSecond = null;
    private String wellPathThird = null;
    private String wellPathForth = null;
    private Image wellCard1 = null;
    private Image wellCard2 = null;
    private Image wellCard3 = null;
    private Image wellCard4 = null;
    private ImageView wellCard1View = null;
    private ImageView wellCard2View = null;
    private ImageView wellCard3View = null;
    private ImageView wellCard4View = null;

    private String idCard1 = null;
    private String idCard2 = null;
    private String idCard3 = null;
    private String idCard4 = null;
    private String typeCard1 = null;
    private String typeCard2 = null;
    private String typeCard3 = null;
    private String typeCard4 = null;

    private String typeHandCard1 = null;
    private String typeHandCard2 = null;
    private String typeHandCard3 = null;

    private String idHandCard1 = null;
    private String idHandCard2 = null;
    private String idHandCard3 = null;
    private String idTopCardResourceDeck = null;
    private String idTopCardGoldDeck = null;

    private Pane specificSeedsPane;
    private Text specificSeedsText;
    private Label specificSeedsLabel;

    private Button playCard = new Button("Play Card");
    private Button flipCardToBack = new Button("Flip Card to back");
    private Button flipCardToFront = new Button("Flip Card to front");
    private Button drawCard = new Button("Draw card");
    private Button seeYourSpecificSeeds = new Button("See your seeds");
    private Button seeOtherPlayersBoards = new Button("See other players boards");
    private Button seeYourPoints = new Button("See your points");
    private Button endTurn = new Button("End turn");

    private Boolean haveToDraw = false;
    private Boolean haveToPlay = true;

    private String chosenDeckForDrawingNewCard = null;
    private String wellOrDeck = "notSelected";

    private double heightWellCards = 80;
    private double widthWellCards = 110;
    private Integer indexCardToPlace = 100;
    private Integer indexCardToBePlacedOn = 100;
    private Integer indexCardPlayedFromHand = 9999999;
    private Integer indexCardFromWellSelected = 89989898;
    private String cornerSelected = "notSelected";
    private static Stage window;
    private Image wellCardSelected = null;
    private String idWellCardSelected = null;
    private Image cardFlipped = null;

    private Player currentPlayer = null;
    Controller controller = new Controller(in, out);
    private String currentPlayerNickname;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in = in;
        this.currentPlayerNickname = in.readLine();
    }

    public void updateFirst() throws IOException {
        //synchronized (GameSceneController.class) {
            //if (isFirstThread) {
                firstWellCard();
                secondWellCard();
                thirdWellCard();
                fourthWellCard();
                checkTypeWellCards();
                playerDeck();
                checkTypePlayerDeck();
                updatingResourceAndGoldDeck();
                isFirstThread = false;
            //}
       // }
    }

    public void startGame(String initCardId) {
//        creatingPathForGameMethod();
//        topCardResourceDeck = createNewPathForImages(pathResourceDeck); //Resource Deck Back Image
//        topCardGoldDeck = createNewPathForImages(pathGoldDeck);
//        creatingDeckAndGoldDeckView();
//        //Handling well creation
//        creatingWell();
//        creatingImagesForTheWell();
//        creatingImagesViewForTheWell();
//        settingWellOnMouseClickedEvent();
//        settingDecksOnMouseClickedEvent();

        //Setting background
        Image backGroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundImage backgroundImage = new BackgroundImage(backGroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

//        String pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
//        String pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
//        String pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";
//
//        Image handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
//        Image handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
//        Image handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));

//        ImageView handCard1View = new ImageView(handCard1);
//        setWidthAndHeight(handCard1View);
//
//        ImageView handCard2View = new ImageView(handCard2);
//        handCard2View.setFitWidth(widthWellCards);
//        handCard2View.setFitHeight(heightWellCards);
//
//        ImageView handCard3View = new ImageView(handCard3);
//        handCard3View.setFitWidth(widthWellCards);
//        handCard3View.setFitHeight(heightWellCards);
//        //Creo uno scroll pane che conterrà tutto
        ScrollPane cardsOntheBoardScrollPane = new ScrollPane();
        int boardDimension = 500;

        GridPane gameBoard = new GridPane(boardDimension, boardDimension);  //Creo il gridpane che conterrà uno StackPane e gli piazzo il background

        gameBoard.setBackground(background);
        gameBoard.setHgap(0);
        gameBoard.setVgap(0);
        gameBoard.setAlignment(Pos.CENTER); //init card in the middle

        double windowdLenght = root.getWidth(); //Salvo la dimensione della finestra (root) in due variabili
        double windowHight = root.getHeight();

        gameBoard.setPrefSize(windowdLenght * 0.8, windowHight * 0.8); //Imposto la grandezza del gridpane all'80% della dimensione (iniziale) della finestra

        //Creo immagine e ImageView della carta iniziale che dovrò piazzare al centro

        Image initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Init/" + initCardId + ".png")));
        GridPane initCardDividedINFourRegions = subnettingEachImage(initCardImage);


        //CARTA INIZIALE -> ogni cella del gridpane viene castata a ImageView
        ImageView tlImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(0); //Tl
        ImageView trImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(1);
        ImageView blImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(2);
        ImageView brImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(3);

        //Ogni imageView viene resa clickabile
        tlImageView.setPickOnBounds(true);
        trImageView.setPickOnBounds(true);
        blImageView.setPickOnBounds(true);
        brImageView.setPickOnBounds(true);

        //InitializeInitCard-> all

        //Adding each imageview to the gameBoard

        gameBoard.add(tlImageView, boardDimension / 2, boardDimension / 2);
        gameBoard.add(trImageView, boardDimension / 2 + 1, boardDimension / 2);
        gameBoard.add(blImageView, boardDimension / 2, boardDimension / 2 + 1);
        gameBoard.add(brImageView, boardDimension / 2 + 1, boardDimension / 2 + 1);

        tlImageView.setOnMouseClicked(event -> {
            setClickedImageView(tlImageView);
            System.out.println("YOu clicked tl");
            selectedCorner = 1;
        });
        trImageView.setOnMouseClicked(event -> {
            setClickedImageView(trImageView);
            System.out.println("YOu clicked tr");
            selectedCorner = 2;
        });
        blImageView.setOnMouseClicked(event -> {
            setClickedImageView(blImageView);
            System.out.println("YOu clicked bl");
            selectedCorner = 3;
        });
        brImageView.setOnMouseClicked(event -> {
            setClickedImageView(brImageView);
            System.out.println("YOu clicked br");
            selectedCorner = 4;
        });


        cardsOntheBoardScrollPane.setContent(gameBoard);
        BorderPane layout = new BorderPane();
        layout.setCenter(cardsOntheBoardScrollPane); //initcard in the middle

        Button playcard = new Button("Play Card");
        playcard.setOnAction(e -> {
            if (selectedCorner == 1) {
                piazzaCartaTL(getClickedImageView(), gameBoard, indexCardToPlace);
            }
            if (selectedCorner == 2) {
                piazzaCartaTR(getClickedImageView(), gameBoard, indexCardToPlace);
            }
            if (selectedCorner == 3) {
                piazzaCartaBL(getClickedImageView(), gameBoard, indexCardToPlace);
            } else if (selectedCorner == 4) {
                piazzaCartaBR(getClickedImageView(), gameBoard, indexCardToPlace);
            }
        });
        //Piazza il pulsante a sinistra
        layout.setRight(playcard);
        root.getChildren().add(layout);
        Scene gameScene = new Scene(root, 600, 400);
        primaryStage.setScene(gameScene);
    }

    //Prende un'immagine e crea un gridPane 2x2 che avrà in ogni cella un quarto dell'immagine
    public GridPane subnettingEachImage(Image image) {
        double larghezzaA = image.getWidth() / 2;
        double altezzaA = image.getHeight() / 2;
        int altezza = (int) altezzaA;
        int larghezza = (int) larghezzaA;
        Image image1 = new WritableImage(image.getPixelReader(), 0, 0, larghezza, altezza);
        Image image2 = new WritableImage(image.getPixelReader(), larghezza, 0, larghezza, altezza);
        Image image3 = new WritableImage(image.getPixelReader(), 0, altezza, larghezza, altezza);
        Image image4 = new WritableImage(image.getPixelReader(), larghezza, altezza, larghezza, altezza);
        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        imageView1.setFitWidth(100);
        imageView1.setPreserveRatio(true);
        imageView2.setFitWidth(100);
        imageView2.setPreserveRatio(true);
        imageView3.setFitWidth(100);
        imageView3.setPreserveRatio(true);
        imageView4.setFitWidth(100);
        imageView4.setPreserveRatio(true);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        gridPane.add(imageView1, 0, 0);
        gridPane.add(imageView2, 1, 0);
        gridPane.add(imageView3, 0, 1);
        gridPane.add(imageView4, 1, 1);

        imageView1.setPickOnBounds(true);
        imageView2.setPickOnBounds(true);
        imageView3.setPickOnBounds(true);
        imageView4.setPickOnBounds(true);

        //Rendo cliccabile + update del valore corrente che vorrò andare a piazzare sulla board
        imageView1.setOnMouseClicked(event -> {
            setClickedImageView(imageView1);
            selectedCorner = 1;
        });

        imageView2.setOnMouseClicked(event -> {
            setClickedImageView(imageView2);
            selectedCorner = 2;
        });

        imageView3.setOnMouseClicked(event -> {
            setClickedImageView(imageView3);
            selectedCorner = 3;
        });

        imageView4.setOnMouseClicked(event -> {
            setClickedImageView(imageView4);
            selectedCorner = 4;
        });
        return gridPane;
    }


    public int getX(ImageView img) {
        return GridPane.getRowIndex(img);
    }

    public int getY(ImageView img) {
        return GridPane.getColumnIndex(img);
    }

    public void piazzaCartaBR(ImageView cartaSuCuiPiazzo, GridPane board, int id) {

        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/" + id + ".png"));

        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);

        GridPane piazzare = subnettingEachImage(secondaImmagine);

        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
        ImageView im4 = (ImageView) piazzare.getChildren().get(3);

        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);

        board.add(im1, y, x);
        board.add(im2, (y + 1), x);
        board.add(im3, y, (x + 1));
        board.add(im4, (y + 1), (x + 1));
    }

    public void piazzaCartaBL(ImageView cartaSuCuiPiazzo, GridPane board, int id) {

        Image secondaImmagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCarte/" + id + ".png")));

        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);

        GridPane piazzare = subnettingEachImage(secondaImmagine);

        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
        ImageView im4 = (ImageView) piazzare.getChildren().get(3);

        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);

        board.add(im1, (y - 1), (x));
        board.add(im2, (y), (x));
        board.add(im3, (y - 1), (x + 1));
        board.add(im4, (y), (x + 1));
    }

    public void piazzaCartaTL(ImageView cartaSuCuiPiazzo, GridPane board, int id) {

        Image secondaImmagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCarte/" + id + ".png")));

        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);

        GridPane piazzare = subnettingEachImage(secondaImmagine);

        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
        ImageView im4 = (ImageView) piazzare.getChildren().get(3);

        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);

        board.add(im1, (y - 1), (x - 1));
        board.add(im2, (y), (x - 1));
        board.add(im3, (y - 1), (x));
        board.add(im4, (y), (x));
    }

    public void piazzaCartaTR(ImageView cartaSuCuiPiazzo, GridPane board, int id) {

        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/" + id + ".png"));

        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);

        GridPane piazzare = subnettingEachImage(secondaImmagine);

        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
        ImageView im4 = (ImageView) piazzare.getChildren().get(3);

        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);

        board.add(im1, (y), (x - 1));
        board.add(im2, (y + 1), (x - 1));
        board.add(im3, (y), (x));
        board.add(im4, (y + 1), (x));
    }

    public ImageView getClickedImageView() {
        return clickedImageView;
    }

    public void setClickedImageView(ImageView clickedImageView) {
        GameSceneController.clickedImageView = clickedImageView;
    }

    private void firstWellCard() throws IOException {

        out.println("firstWellId");
        idCard1 = in.readLine();
        //System.out.println(idCard1);
    }

    private void secondWellCard() throws IOException {
        out.println("secondWellId");
        idCard2 = in.readLine();
        //System.out.println(idCard2);
    }

    private void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        idCard3 = in.readLine();
        //System.out.println(idCard3);
    }

    private void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        idCard4 = in.readLine();
        //System.out.println(idCard4);
    }

    private void updatingResourceAndGoldDeck() throws IOException {
        out.println("firstCardResourceGui");
        idTopCardResourceDeck = in.readLine();
        System.out.println("la topCardResourceDeck is: " + idTopCardResourceDeck);
        out.println("firstCardGoldGui");
        idTopCardGoldDeck = in.readLine();
        System.out.println("la topCardGoldDeck is: " + idTopCardGoldDeck);
    }

    private void checkTypeWellCards() {
        typeCard1 = checkType(idCard1);
        typeCard2 = checkType(idCard2);
        typeCard3 = checkType(idCard3);
        typeCard4 = checkType(idCard4);
    }

    private void playerDeck() throws IOException {
        out.println("deckId");
        idHandCard1 = in.readLine();
        System.out.println(idHandCard1);
        idHandCard2 = in.readLine();
        System.out.println(idHandCard2);
        idHandCard3 = in.readLine();
        System.out.println(idHandCard3);
    }

    private void checkTypePlayerDeck() throws IOException {
        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);
        in.readLine(); //spazio
    }

    private String checkType(String id) {
        int intId = Integer.parseInt(id);
        String type;
        if (intId > 0 && intId <= 40) {
            type = "Resource";
        } else {
            type = "Gold";
        }
        return type;
    }

    private void creatingBackground(Pane root) {
        Image gameBackgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundGameImage = new BackgroundImage(gameBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundGameImage);
        root.setBackground(background);
    }

    private void creatingPathForGameMethod() {
        pathResourceDeck = createPathForBackCards(idTopCardResourceDeck);
        pathGoldDeck = createPathForBackCards(idTopCardGoldDeck);
    }

    private Image createNewPathForImages(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    private void setWidthAndHeight(ImageView imageView) {
        imageView.setFitWidth(widthWellCards);
        imageView.setFitHeight(heightWellCards);
    }

    private void creatingDeckAndGoldDeckView() {
        topCardResourceDeckView = new ImageView(topCardResourceDeck); //Resource deck imageview
        topCardGoldDeckView = new ImageView(topCardGoldDeck);         //Gold Deck imageview
        setWidthAndHeight(topCardResourceDeckView);
        setWidthAndHeight(topCardGoldDeckView);
    }

    private void settingDecksOnMouseClickedEvent() {
        topCardResourceDeckView.setOnMouseClicked(e -> {
            chosenDeckForDrawingNewCard = "resource";
            wellOrDeck = "deck";
            System.out.println("resourceDeck clicked");
            System.out.println(idTopCardResourceDeck);
        });
        topCardGoldDeckView.setOnMouseClicked(e -> {
            chosenDeckForDrawingNewCard = "gold";
            wellOrDeck = "deck";
            System.out.println("goldDeck clicked");
            System.out.println(idTopCardGoldDeck);
        });
    }

    private void creatingWell() {
        wellPathOne = "/ImmaginiCodex/CarteFront/" + typeCard1 + "/" + idCard1 + ".png";
        wellPathSecond = "/ImmaginiCodex/CarteFront/" + typeCard2 + "/" + idCard2 + ".png";
        wellPathThird = "/ImmaginiCodex/CarteFront/" + typeCard3 + "/" + idCard3 + ".png";
        wellPathForth = "/ImmaginiCodex/CarteFront/" + typeCard4 + "/" + idCard4 + ".png";
    }

    private void creatingImagesForTheWell() {
        wellCard1 = createNewPathForImages(wellPathOne);
        wellCard2 = createNewPathForImages(wellPathSecond);
        wellCard3 = createNewPathForImages(wellPathThird);
        wellCard4 = createNewPathForImages(wellPathForth);
    }

    private void settingWellOnMouseClickedEvent() {
        wellCard1View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + idCard1);
            wellOrDeck = "well";
            wellCardSelected = wellCard1;
            idWellCardSelected = idCard1;
            indexCardFromWellSelected = 0;
        });
        wellCard2View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + idCard2);
            wellOrDeck = "well";
            wellCardSelected = wellCard2;
            idWellCardSelected = idCard2;
            indexCardFromWellSelected = 1;
        });
        wellCard3View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + idCard3);
            wellOrDeck = "well";
            wellCardSelected = wellCard3;
            idWellCardSelected = idCard3;
            indexCardFromWellSelected = 2;
        });
        wellCard4View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + idCard4);
            wellOrDeck = "well";
            wellCardSelected = wellCard4;
            idWellCardSelected = idCard4;
            indexCardFromWellSelected = 3;
        });
    }

    private void creatingImagesViewForTheWell() {
        wellCard1View = new ImageView(wellCard1);
        setWidthAndHeight(wellCard1View);
        wellCard2View = new ImageView(wellCard2);
        setWidthAndHeight(wellCard2View);
        wellCard3View = new ImageView(wellCard3);
        setWidthAndHeight(wellCard3View);
        wellCard4View = new ImageView(wellCard4);
        setWidthAndHeight(wellCard4View);
    }

    private String createPathForBackCards(String cardId) {
        if (Integer.parseInt(cardId) < 41) {
            return pathForResourceCardsBack(cardId);
        } else {
            return pathForGoldCardBack(cardId);
        }
    }

    private String createPathForFrontCards(String cardId) {
        if (Integer.parseInt(cardId) < 41) {
            return pathForResourceCardFront(cardId);
        } else {
            return pathForGoldCardFront(cardId);
        }
    }

    private String pathForResourceCardFront(String cardId) {
        String correctPath = "/ImmaginiCodex/CarteFront/Resource/";
        return correctPath + cardId + ".png";
    }

    private String pathForGoldCardFront(String cardId) {
        String correctPath = "/ImmaginiCodex/CarteFront/Gold/";
        return correctPath + cardId + ".png";
    }


    private String pathForResourceCardsBack(String cardId) {
        String correctPath = "/ImmaginiCodex/CarteBack/Resource/";
        return correctPath + cardId + ".png";
    }

    private String pathForGoldCardBack(String cardId) {
        String correctPath = "/ImmaginiCodex/CarteBack/Gold/";
        return correctPath + cardId + ".png";
    }
}



