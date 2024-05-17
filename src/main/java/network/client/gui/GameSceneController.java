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
import view.ClientView;

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


    private static  String typeCard1 ;
    private static  String typeCard2 ;
    private static  String typeCard3 ;
    private  static String typeCard4;

    private String typeHandCard1 = null;
    private String typeHandCard2 = null;
    private String typeHandCard3 = null;

    private String idHandCard1 = null;
    private String idHandCard2 = null;
    private String idHandCard3 = null;
    private String idTopCardResourceDeck ;
    private String idTopCardGoldDeck ;

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
    private ClientView clientView;

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, String currentPlayerNickname) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in = in;
        this.currentPlayerNickname = currentPlayerNickname;
        System.out.println("Current Player nickname " + currentPlayerNickname); //salvato il nome del current player
        this.clientView=clientView;

    }

    // currentplayer-> initializeGame-> inizializzato il well, inizializzato deck risorsa e deck gold.
    //ogni giocatore->pesca le 3 carte


    public void updateFirst() throws IOException {
        synchronized (GameSceneController.class) {
            if (!SharedObjectsInGui.isInitialized()) {
                if (clientView.getUserName().equals(currentPlayerNickname)) {
                    initializeWell(); // Inizializza il pozzo
                    updatingResourceAndGoldDeck(); // Inizializza i deck
                    creatingPathForGameMethod();

                    SharedObjectsInGui.setTopCardResourceDeck(createNewPathForImages(SharedObjectsInGui.getPathResourceDeck()));
                    SharedObjectsInGui.setTopCardGoldDeck(createNewPathForImages(SharedObjectsInGui.getPathGoldDeck()));

                    creatingDeckAndGoldDeckView();
                    creatingWell();
                    creatingImagesForTheWell();
                    creatingImagesViewForTheWell();
                    settingWellOnMouseClickedEvent();
                    settingDecksOnMouseClickedEvent();
                    SharedObjectsInGui.setInitialized(true);
                    System.out.println("Setup finished");
                }
            }
        }
    }
    private void creatingWell() {
        System.out.println("Creating paths for well cards...");
        SharedObjectsInGui.setWellPathOne(createPathForFrontCards(SharedObjectsInGui.getIdCard1()));
        SharedObjectsInGui.setWellPathSecond(createPathForFrontCards(SharedObjectsInGui.getIdCard2()));
        SharedObjectsInGui.setWellPathThird(createPathForFrontCards(SharedObjectsInGui.getIdCard3()));
        SharedObjectsInGui.setWellPathForth(createPathForFrontCards(SharedObjectsInGui.getIdCard4()));

        // Aggiungiamo stampe di debug per verificare i percorsi
        System.out.println("Well Path One: " + SharedObjectsInGui.getWellPathOne());
        System.out.println("Well Path Second: " + SharedObjectsInGui.getWellPathSecond());
        System.out.println("Well Path Third: " + SharedObjectsInGui.getWellPathThird());
        System.out.println("Well Path Forth: " + SharedObjectsInGui.getWellPathForth());
    }

//    playerDeck(); //pesca 2 carte r e 1 carta g dal server(id)
//    checkTypePlayerDeck(); //serve x creare i path correttamente
    public void startGame(String initCardId) {
//

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
        cardsOntheBoardScrollPane.setPrefSize(400.00,400.00);
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


        VBox vboxContainer = new VBox();
        HBox hboxGame = new HBox();
        VBox vboxGame = new VBox();
        vboxGame.setFillWidth(true);

        Insets padding = new Insets(10, 10, 10, 10);
        Insets paddingDecks = new Insets(100, 10, 10, 10);

        HBox secondRow = new HBox();
        HBox firstColomnOfSecondRow = new HBox();
        firstColomnOfSecondRow.setPadding(padding);
        firstColomnOfSecondRow.setSpacing(4);

        Label wellText = new Label("                    WELL");
        Label wellText2 = new Label("CARDS");
        wellText.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");
        wellText2.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");


        GridPane gridPaneForWellCards = new GridPane();
        gridPaneForWellCards.setVgap(4.0);
        gridPaneForWellCards.setHgap(4.0);
        gridPaneForWellCards.setPadding(padding);
//        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard1View(), 0, 0);
//        System.out.println(SharedObjectsInGui.getWellCard1View());
//        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard2View(), 0, 1);
//        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard3View(), 1, 0);
//        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard4View(), 1, 1);
//        gridPaneForWellCards.add(wellText, 0, 2);
//        gridPaneForWellCards.add(wellText2, 1, 2);


        HBox decks = new HBox();

        Label decksText = new Label("      Resource Deck               Gold Deck");
        decksText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        decks.setSpacing(4.0);
        decks.setPadding(paddingDecks);
        //decks.getChildren().addAll(SharedObjectsInGui.getTopCardResourceDeckView(), SharedObjectsInGui.getTopCardGoldDeckView());

        specificSeedsPane = new Pane();
        specificSeedsText = new Text("Your specific Seeds are: ");
        specificSeedsLabel = new Label();

        specificSeedsPane.getChildren().addAll(specificSeedsText, specificSeedsLabel);

        GridPane buttonContainer = new GridPane();

        buttonContainer.add(playCard,0, 0);
        buttonContainer.add(drawCard, 1, 0);
        buttonContainer.add(seeYourSpecificSeeds, 2, 0);
        buttonContainer.add(flipCardToBack, 0,1);
        buttonContainer.add(flipCardToFront, 0,2);
        buttonContainer.add(seeYourPoints, 0, 3);
        buttonContainer.add(endTurn, 2, 1);
        buttonContainer.add(seeOtherPlayersBoards, 1, 1);

        vboxGame.getChildren().addAll(gridPaneForWellCards, decks, decksText, specificSeedsPane, buttonContainer);
        layout.setRight(vboxGame);
        /*firstColomnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColomnOfSecondRow);
        layout.setBottom(secondRow);*/

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
        SharedObjectsInGui.setIdCard1(in.readLine());
    }

    private void secondWellCard() throws IOException {
        out.println("secondWellId");
        SharedObjectsInGui.setIdCard2(in.readLine());
    }

    private void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        SharedObjectsInGui.setIdCard3(in.readLine());
    }

    private void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        SharedObjectsInGui.setIdCard4(in.readLine());
    }

    private void updatingResourceAndGoldDeck() throws IOException {
        out.println("firstCardResourceGui");
        SharedObjectsInGui.setPathResourceDeck(in.readLine());
        System.out.println("la topCardResourceDeck is: " + SharedObjectsInGui.getPathResourceDeck());

        out.println("firstCardGoldGui");
        SharedObjectsInGui.setPathGoldDeck(in.readLine());
        System.out.println("la topCardGoldDeck is: " + SharedObjectsInGui.getPathGoldDeck());
    }

    private void checkTypeWellCards() {
        SharedObjectsInGui.setWellPathOne(createPathForFrontCards(SharedObjectsInGui.getIdCard1()));
        SharedObjectsInGui.setWellPathSecond(createPathForFrontCards(SharedObjectsInGui.getIdCard2()));
        SharedObjectsInGui.setWellPathThird(createPathForFrontCards(SharedObjectsInGui.getIdCard3()));
        SharedObjectsInGui.setWellPathForth(createPathForFrontCards(SharedObjectsInGui.getIdCard4()));
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
        SharedObjectsInGui.setPathResourceDeck(createPathForBackCards(SharedObjectsInGui.getPathResourceDeck()));
        SharedObjectsInGui.setPathGoldDeck(createPathForBackCards(SharedObjectsInGui.getPathGoldDeck()));
    }

    private Image createNewPathForImages(String path) {
        System.out.println("Trying to load image from path: " + path);
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            System.err.println("Error: Unable to load image from path: " + path);
            return null;
        }
    }

    private void setWidthAndHeight(ImageView imageView) {
        imageView.setFitWidth(widthWellCards);
        imageView.setFitHeight(heightWellCards);
    }

    private void creatingDeckAndGoldDeckView() {
        SharedObjectsInGui.setTopCardResourceDeckView(new ImageView(SharedObjectsInGui.getTopCardResourceDeck()));
        SharedObjectsInGui.setTopCardGoldDeckView(new ImageView(SharedObjectsInGui.getTopCardGoldDeck()));
        setWidthAndHeight(SharedObjectsInGui.getTopCardResourceDeckView());
        setWidthAndHeight(SharedObjectsInGui.getTopCardGoldDeckView());
    }



    private void settingDecksOnMouseClickedEvent() {
        SharedObjectsInGui.getTopCardResourceDeckView().setOnMouseClicked(e -> {
            chosenDeckForDrawingNewCard = "resource";
            wellOrDeck = "deck";
            System.out.println("resourceDeck clicked");
            System.out.println(idTopCardResourceDeck);
        });
        SharedObjectsInGui.getTopCardGoldDeckView().setOnMouseClicked(e -> {
            chosenDeckForDrawingNewCard = "gold";
            wellOrDeck = "deck";
            System.out.println("goldDeck clicked");
            System.out.println(idTopCardGoldDeck);
        });
    }


    private void creatingImagesForTheWell() {
        SharedObjectsInGui.setWellCard1(createNewPathForImages(SharedObjectsInGui.getWellPathOne()));
        SharedObjectsInGui.setWellCard2(createNewPathForImages(SharedObjectsInGui.getWellPathSecond()));
        SharedObjectsInGui.setWellCard3(createNewPathForImages(SharedObjectsInGui.getWellPathThird()));
        SharedObjectsInGui.setWellCard4(createNewPathForImages(SharedObjectsInGui.getWellPathForth()));
    }

    private void creatingImagesViewForTheWell() {
        SharedObjectsInGui.setWellCard1View(new ImageView(SharedObjectsInGui.getWellCard1()));
        SharedObjectsInGui.setWellCard2View(new ImageView(SharedObjectsInGui.getWellCard2()));
        SharedObjectsInGui.setWellCard3View(new ImageView(SharedObjectsInGui.getWellCard3()));
        SharedObjectsInGui.setWellCard4View(new ImageView(SharedObjectsInGui.getWellCard4()));
        setWidthAndHeight(SharedObjectsInGui.getWellCard1View());
        setWidthAndHeight(SharedObjectsInGui.getWellCard2View());
        setWidthAndHeight(SharedObjectsInGui.getWellCard3View());
        setWidthAndHeight(SharedObjectsInGui.getWellCard4View());
    }

    private void settingWellOnMouseClickedEvent() {
        SharedObjectsInGui.getWellCard1View().setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + SharedObjectsInGui.getIdCard1());
            wellOrDeck = "well";
            wellCardSelected = SharedObjectsInGui.getWellCard1();
            idWellCardSelected = SharedObjectsInGui.getIdCard1();
            indexCardFromWellSelected = 0;
        });
        SharedObjectsInGui.getWellCard2View().setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + SharedObjectsInGui.getIdCard2());
            wellOrDeck = "well";
            wellCardSelected = SharedObjectsInGui.getWellCard2();
            idWellCardSelected = SharedObjectsInGui.getIdCard2();
            indexCardFromWellSelected = 1;
        });
        SharedObjectsInGui.getWellCard3View().setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + SharedObjectsInGui.getIdCard3());
            wellOrDeck = "well";
            wellCardSelected = SharedObjectsInGui.getWellCard3();
            idWellCardSelected = SharedObjectsInGui.getIdCard3();
            indexCardFromWellSelected = 2;
        });
        SharedObjectsInGui.getWellCard4View().setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: " + SharedObjectsInGui.getIdCard4());
            wellOrDeck = "well";
            wellCardSelected = SharedObjectsInGui.getWellCard4();
            idWellCardSelected = SharedObjectsInGui.getIdCard4();
            indexCardFromWellSelected = 3;
        });
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

    private void initializeWell() throws IOException {
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
    }
}



