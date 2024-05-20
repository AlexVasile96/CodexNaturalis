package network.client.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.ClientView;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSceneController {
    private StackPane root = new StackPane();
    private static ImageView clickedImageView;
    private static int selectedCorner;
    private static final Object syncObject = new Object();
    private Stage primaryStage;
    private PrintWriter out;
    private Socket socket;
    private BufferedReader in;
    private String typeHandCard1 = null;
    private String typeHandCard2 = null;
    private String typeHandCard3 = null;
    private String idHandCard1 = null;
    private String idHandCard2 = null;
    private String idHandCard3 = null;
    private String idTopCardResourceDeck;
    private String idTopCardGoldDeck;
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
    private Button quit = new Button("Quit");
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
    private Image wellCardSelected = null;
    private String idWellCardSelected = null;
    private Controller controller = null;
    private String currentPlayerNickname;
    private ClientView clientView;
    private BoardPointsScene boardPointsScene;
    private boolean isCurrentPlayerTurn = false;
    private ScrollPane cardsOntheBoardScrollPane;
    private GridPane gameBoard;
    GridPane buttonContainer = new GridPane();
    Label chosenCardToPlace = new Label();
    Label chosenCardToBePlacedOn = new Label();
    Label chosenCorner = new Label();
    Label chosenDeckOrWell = new Label();
    private List<CardView> allCardViews = new ArrayList<>();
    private static CardView clickedCardView;
    private Map<CardView, Integer> cardIndices = new HashMap<>();
    private int nextCardIndex = 0; // Contatore per i prossimi indici delle carte
    private String pathHandCard1;
    private String pathHandCard2;
    private String pathHandCard3;
    private String pathChosen;
    private  GridPane gridPaneForWellCards;



    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, String currentPlayerNickname) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in = in;
        this.currentPlayerNickname = currentPlayerNickname;
        this.clientView = clientView;
        controller = new Controller(in, out);
        isCurrentPlayerTurn = clientView.getUserName().equals(currentPlayerNickname);

    }

    public synchronized void updateFirst() throws IOException {
        synchronized (syncObject) {
            initializeWell();
            updatingResourceAndGoldDeck();
            creatingPathForGameMethod();
            SharedObjectsInGui.setTopCardResourceDeck(createNewPathForImages(SharedObjectsInGui.getPathResourceDeck()));
            SharedObjectsInGui.setTopCardGoldDeck(createNewPathForImages(SharedObjectsInGui.getPathGoldDeck()));
            out.println("firstCardResourceGui");
            idTopCardResourceDeck = in.readLine();
            out.println("firstCardGoldGui");
            idTopCardGoldDeck = in.readLine();
            creatingDeckAndGoldDeckView();
            creatingWell();
            creatingImagesForTheWell();
            creatingImagesViewForTheWell();
            settingWellOnMouseClickedEvent();
            settingDecksOnMouseClickedEvent();
            playerDeck();
            checkTypePlayerDeck();
        }
    }

    public void startGame(String initCardId) throws IOException {
        Image backGroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundImage backgroundImage = new BackgroundImage(backGroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
        pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
        pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";

        Image handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
        Image handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
        Image handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));

        ImageView handCard1View = new ImageView(handCard1);
        setWidthAndHeight(handCard1View);

        ImageView handCard2View = new ImageView(handCard2);
        handCard2View.setFitWidth(widthWellCards);
        handCard2View.setFitHeight(heightWellCards);

        ImageView handCard3View = new ImageView(handCard3);
        handCard3View.setFitWidth(widthWellCards);
        handCard3View.setFitHeight(heightWellCards);

        cardsOntheBoardScrollPane = new ScrollPane();
        cardsOntheBoardScrollPane.setPrefSize(400.00, 400.00);
        int boardDimension = 500;

        gameBoard = new GridPane(boardDimension, boardDimension);
        gameBoard.setBackground(background);
        gameBoard.setHgap(0);
        gameBoard.setVgap(0);
        gameBoard.setAlignment(Pos.CENTER);

        double windowedLength = root.getWidth();
        double windowHeight = root.getHeight();

        gameBoard.setPrefSize(windowedLength * 0.8, windowHeight * 0.8);

        Image initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Init/" + initCardId + ".png")));
        GridPane initCardDividedINFourRegions = subnettingEachImage(initCardImage,initCardId);

        ImageView tlImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(0);
        ImageView trImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(1);
        ImageView blImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(2);
        ImageView brImageView = (ImageView) initCardDividedINFourRegions.getChildren().get(3);

        CardView tlCardView = new CardView(tlImageView, initCardId, "TL");
        CardView trCardView = new CardView(trImageView, initCardId, "TR");
        CardView blCardView = new CardView(blImageView, initCardId, "BL");
        CardView brCardView = new CardView(brImageView, initCardId, "BR");

        nextCardIndex++; //1 for initial card
        //All the index of the same card has the same value
        cardIndices.put(tlCardView, nextCardIndex);
        System.out.println("Added TL CardView with index: " + nextCardIndex);
        cardIndices.put(trCardView, nextCardIndex);
        System.out.println("Added TR CardView with index: " + nextCardIndex);
        cardIndices.put(blCardView, nextCardIndex);
        System.out.println("Added BL CardView with index: " + nextCardIndex);
        cardIndices.put(brCardView, nextCardIndex);
        System.out.println("Added BR CardView with index: " + nextCardIndex);


        tlImageView.setPickOnBounds(true);
        trImageView.setPickOnBounds(true);
        blImageView.setPickOnBounds(true);
        brImageView.setPickOnBounds(true);

        gameBoard.add(tlImageView, boardDimension / 2, boardDimension / 2);
        gameBoard.add(trImageView, boardDimension / 2 + 1, boardDimension / 2);
        gameBoard.add(blImageView, boardDimension / 2, boardDimension / 2 + 1);
        gameBoard.add(brImageView, boardDimension / 2 + 1, boardDimension / 2 + 1);

        cardsOntheBoardScrollPane.setContent(gameBoard);
        BorderPane layout = new BorderPane();
        layout.setCenter(cardsOntheBoardScrollPane);
        layout.setStyle("-fx-background-color: #212121;");



        VBox vboxGame = new VBox();
        vboxGame.setFillWidth(true);

        Insets padding = new Insets(10, 10, 10, 10);
        Insets paddingDecks = new Insets(100, 10, 10, 10);

        HBox secondRow = new HBox();
        HBox firstColumnOfSecondRow = new HBox();
        firstColumnOfSecondRow.setPadding(padding);
        firstColumnOfSecondRow.setSpacing(4);

        Label wellText = new Label("                    WELL");
        Label wellText2 = new Label("CARDS");
        wellText.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");
        wellText2.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");

        //Initializing well cards

        gridPaneForWellCards = new GridPane();
        gridPaneForWellCards.setVgap(4.0);
        gridPaneForWellCards.setHgap(4.0);
        gridPaneForWellCards.setPadding(padding);
        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard1View(), 0, 0);
        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard2View(), 0, 1);
        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard3View(), 1, 0);
        gridPaneForWellCards.add(SharedObjectsInGui.getWellCard4View(), 1, 1);
        gridPaneForWellCards.add(wellText, 0, 2);
        gridPaneForWellCards.add(wellText2, 1, 2);

        HBox decks = new HBox();
        Label decksText = new Label("      Resource Deck               Gold Deck");
        decksText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        decks.setSpacing(4.0);
        decks.setPadding(paddingDecks);
        decks.getChildren().addAll(SharedObjectsInGui.getTopCardResourceDeckView(), SharedObjectsInGui.getTopCardGoldDeckView());

        specificSeedsPane = new Pane();
        specificSeedsText = new Text("Your specific Seeds are: ");
        specificSeedsLabel = new Label();
        specificSeedsPane.getChildren().addAll(specificSeedsText, specificSeedsLabel);

        creatingButtons();
        buttonContainer.add(quit, 2,2);
        vboxGame.getChildren().addAll(gridPaneForWellCards, decks, decksText, specificSeedsPane, buttonContainer);
        //inizialmente disabled per tutti
        buttonContainer.setDisable(true);

        VBox secondColumnOfSecondRow = new VBox();

        secondColumnOfSecondRow.getChildren().addAll(chosenCardToPlace, chosenCardToBePlacedOn, chosenCorner, chosenDeckOrWell);
        chosenDeckOrWell.setText("Drawing from: "+wellOrDeck);

        layout.setRight(vboxGame);
        firstColumnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColumnOfSecondRow, secondColumnOfSecondRow);
        layout.setBottom(secondRow);
        root.getChildren().add(layout);
        Scene gameScene = new Scene(root, 1200, 900);

        /*layout.layoutXProperty().bind(gameScene.widthProperty().subtract(layout.widthProperty()).divide(2));
        layout.layoutYProperty().bind(gameScene.heightProperty().subtract(layout.heightProperty()).divide(2));*/

        primaryStage.setScene(gameScene);

        if (currentPlayerNickname.equals(clientView.getUserName())) {
            isCurrentPlayerTurn = true;
            setupGameActions(handCard1View, handCard2View, handCard3View);
            buttonContainer.setDisable(false);
            //enableOrDisableGameActions();
        } else {
            isCurrentPlayerTurn=false;
            waitForTurn(handCard1View, handCard2View, handCard3View);
        }

    }

    private void handleCardClick(CardView cardView) {
        if (isCurrentPlayerTurn) {
            setClickedCardView(cardView);
            cornerSelected = cardView.getPosition();
            Integer cardIndex = cardIndices.get(cardView);

            // Debug: stampa il CardView cliccato e la mappa
            System.out.println("Clicked CardView: " + cardView);
            System.out.println("Current cardIndices map:");
            for (Map.Entry<CardView, Integer> entry : cardIndices.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            if (cardIndex != null) {
                indexCardToBePlacedOn = cardIndex;
                System.out.println("Card Clicked: indexCardToBePlacedOn = " + indexCardToBePlacedOn + ", cornerSelected = " + cornerSelected);
            } else {
                System.out.println("Card Clicked: cardIndex not found, cornerSelected = " + cornerSelected);
            }
            chosenCorner.setText("Corner: " + cornerSelected);
            chosenCardToBePlacedOn.setText("Place your card on: " + indexCardToBePlacedOn);
        } else {
            showAlert("Not your turn", "It's not your turn yet.");
        }
    }

    private void setupGameActions(ImageView handCard1View, ImageView handCard2View, ImageView handCard3View) throws IOException {
        playCard.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                if (haveToPlay) {
                    try {
                        controller.playCardClick(indexCardToBePlacedOn, indexCardToPlace, cornerSelected);
                        switch(cornerSelected) {
                            case "TL":
                               piazzaCartaTL(getClickedImageView().getImageView(),gameBoard,indexCardToPlace);
                                break;
                            case "TR":
                                piazzaCartaTR(getClickedImageView().getImageView(),gameBoard,indexCardToPlace);
                                break;
                            case "BL":
                                piazzaCartaBL(getClickedImageView().getImageView(),gameBoard,indexCardToPlace);
                                break;
                            case "BR":
                                piazzaCartaBR(getClickedImageView().getImageView(),gameBoard,indexCardToPlace);
                                break;
                            default:

                                break;
                        }
                        indexCardPlayedFromHand = indexCardToPlace;
                        haveToDraw = true;
                        switch (indexCardPlayedFromHand) {
                            case 0:
                                handCard1View.setImage(null);
                                break;
                            case 1:
                                handCard2View.setImage(null);
                                break;
                            case 2:
                                handCard3View.setImage(null);
                                break;
                            default:
                                // Handle unexpected values if necessary
                                break;
                        }
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                    haveToPlay = false;
                } else {
                    showAlert("Action not allowed", "You have already played a card.");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        endTurn.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    String nextPlayerNickname = controller.endTurn();
                    updateTurnState(nextPlayerNickname.equals(clientView.getUserName()));
                    haveToPlay = true;
                    waitForTurn(handCard1View, handCard2View, handCard3View);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        drawCard.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                if (haveToDraw) {
                    try {
                        controller.drawCard(wellOrDeck, chosenDeckForDrawingNewCard, indexCardFromWellSelected);
                        Image drawnCardImage = null;
                        String idTopCard = null;

                        if (wellOrDeck.equals("deck")) {
                            System.out.println(wellOrDeck);
                            if (chosenDeckForDrawingNewCard.equals("resource")) {
                                drawnCardImage = createNewPathForImages(pathForResourceCardFront(idTopCardResourceDeck));
                                idTopCard = idTopCardResourceDeck;
                            } else if (chosenDeckForDrawingNewCard.equals("gold")) {
                                drawnCardImage = createNewPathForImages(pathForGoldCardFront(idTopCardGoldDeck));
                                idTopCard = idTopCardGoldDeck;
                            }
                        } else if (wellOrDeck.equals("well")) {
                            System.out.println(wellOrDeck);
                            drawnCardImage = wellCardSelected;
                            idTopCard = idWellCardSelected;
                        }
                        System.out.println(idTopCard);
                        if (drawnCardImage != null && idTopCard != null) {
                            switch (indexCardPlayedFromHand) {
                                case 0:
                                    handCard1View.setImage(drawnCardImage);
                                    pathHandCard1= "/ImmaginiCodex/CarteFront/" +checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard1 = idTopCard;
                                    break;
                                case 1:
                                    handCard2View.setImage(drawnCardImage);
                                    pathHandCard2= "/ImmaginiCodex/CarteFront/" +checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard2 = idTopCard;
                                    break;
                                case 2:
                                    handCard3View.setImage(drawnCardImage);
                                    pathHandCard3= "/ImmaginiCodex/CarteFront/" +checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard3 = idTopCard;
                                    break;
                            }
                            System.out.println("idHandCard" + (indexCardPlayedFromHand + 1) + " = " + idTopCard);
                            String pathResource = "/ImmaginiCodex/CarteFront/Resource/" + idTopCardResourceDeck + ".png";
                            System.out.println("Idtopcardresourcedeck:" + idTopCardResourceDeck);
                            System.out.println("path: " +  pathResource);
                            Image newWellResourceCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathResource)));

                            String pathGold = "/ImmaginiCodex/CarteFront/Gold/" + idTopCardGoldDeck + ".png";
                            System.out.println(idTopCardGoldDeck);
                            System.out.println(pathGold);
                            Image newWellGoldCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathGold)));

                            if(indexCardFromWellSelected == 0){
                                SharedObjectsInGui.getWellCard1View().setImage(newWellResourceCardImage);
                                updateResourceDeckTopCard();
                            }
                            else if(indexCardFromWellSelected == 1){
                                SharedObjectsInGui.getWellCard2View().setImage(newWellResourceCardImage);
                                updateResourceDeckTopCard();

                            }
                            else if(indexCardFromWellSelected == 2){
                                SharedObjectsInGui.getWellCard3View().setImage(newWellGoldCardImage);
                                updatedGoldDeckTopCard();
                            }
                            else if(indexCardFromWellSelected == 3) {
                                SharedObjectsInGui.getWellCard4View().setImage(newWellGoldCardImage);
                                updatedGoldDeckTopCard();
                            }
                            System.out.println("Well card updated");
                            haveToDraw = false;
                        } else {
                            System.out.println("You have to choose a card to draw");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    showAlert("Action not allowed", "You can't draw any card now.");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        flipCardToBack.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                switch (indexCardToPlace) {
                    case 0:
                        handCard1View.setImage(flipToBackCard(idHandCard1));
                        break;
                    case 1:
                        handCard2View.setImage(flipToBackCard(idHandCard2));
                        break;
                    case 2:
                        handCard3View.setImage(flipToBackCard(idHandCard3));
                        break;
                    default:
                        showAlert("Invalid action", "You chose an unflippable card.");
                        break;
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        flipCardToFront.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                switch (indexCardToPlace) {
                    case 0:
                        handCard1View.setImage(flipCardToFront(idHandCard1));
                        break;
                    case 1:
                        handCard2View.setImage(flipCardToFront(idHandCard2));
                        break;
                    case 2:
                        handCard3View.setImage(flipCardToFront(idHandCard3));
                        break;
                    default:
                        showAlert("Invalid action", "You chose an unflippable card.");
                        break;
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard1View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 0;
                chosenCardToPlace.setText("First card of your hand");
                pathChosen=pathHandCard1;

            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard2View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 1;
                chosenCardToPlace.setText("Second card of your hand");
                pathChosen=pathHandCard2;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard3View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 2;
                chosenCardToPlace.setText("Third card of your hand");
                pathChosen=pathHandCard3;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        seeYourSpecificSeeds.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    String yourSeeds = controller.showSpecificSeed();
                    specificSeedsLabel.setText(yourSeeds);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        seeYourPoints.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {

                    boardPointsScene = new BoardPointsScene(primaryStage, out, socket, in);
                    boardPointsScene.popupBoardPoints();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        seeOtherPlayersBoards.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                showAlert("Action unavailable", "This action is currently not implemented.");
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        quit.setOnMouseClicked(e->{
            if (isCurrentPlayerTurn) {
                if(haveToDraw){
                    showAlert("Quit", "You can't quit right now, draw any card.");
                }
                else {
                    String nextPlayerNickname;
                    try {
                        nextPlayerNickname = controller.endTurn();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    updateTurnState(nextPlayerNickname.equals(clientView.getUserName()));
                    haveToPlay = true;
                    controller.quit(primaryStage);
                }
            } else {
                controller.quit(primaryStage);
            }
        });

        enableOrDisableGameActions();
    }

    private void waitForTurn(ImageView handCard1View, ImageView handCard2View, ImageView handCard3View) {
        new Thread(() -> {
            try {
                controller.waitForTurn(clientView.getUserName());
                Platform.runLater(() -> {
                    updateTurnState(true);
                    try {

                        setupGameActions(handCard1View, handCard2View, handCard3View);
                        System.out.println(in.readLine()); //Fine turno
                        updateGUI();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void enableOrDisableGameActions() {
        playCard.setDisable(!isCurrentPlayerTurn);
        drawCard.setDisable(!isCurrentPlayerTurn);
        seeYourSpecificSeeds.setDisable(!isCurrentPlayerTurn);
        seeOtherPlayersBoards.setDisable(!isCurrentPlayerTurn);
        seeYourPoints.setDisable(!isCurrentPlayerTurn);
        endTurn.setDisable(!isCurrentPlayerTurn);
        buttonContainer.setDisable(false);

    }

    private void updateTurnState(boolean isCurrentPlayerTurn) {
        this.isCurrentPlayerTurn = isCurrentPlayerTurn;
        enableOrDisableGameActions();
    }

    private Image flipToBackCard(String stringId) {
        int id = Integer.parseInt(stringId);
        if (id >= 1 && id <= 40) {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        } else if (id > 40 && id <= 80) {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        }
        return null;
    }

    private Image flipCardToFront(String stringId) {
        int id = Integer.parseInt(stringId);
        if (id >= 1 && id <= 40) {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        } else if (id > 40 && id <= 80) {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        }
        return null;
    }

    public GridPane subnettingEachImage(Image image, String cardId) {
        double width = image.getWidth() / 2;
        double height = image.getHeight() / 2;
        int heightInt = (int) height;
        int widthInt = (int) width;
        Image image1 = new WritableImage(image.getPixelReader(), 0, 0, widthInt, heightInt);
        Image image2 = new WritableImage(image.getPixelReader(), widthInt, 0, widthInt, heightInt);
        Image image3 = new WritableImage(image.getPixelReader(), 0, heightInt, widthInt, heightInt);
        Image image4 = new WritableImage(image.getPixelReader(), widthInt, heightInt, widthInt, heightInt);
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

        CardView cardView1 = new CardView(imageView1, cardId, "TL");
        CardView cardView2 = new CardView(imageView2, cardId, "TR");
        CardView cardView3 = new CardView(imageView3, cardId, "BL");
        CardView cardView4 = new CardView(imageView4, cardId, "BR");

        allCardViews.add(cardView1);
        allCardViews.add(cardView2);
        allCardViews.add(cardView3);
        allCardViews.add(cardView4);

        imageView1.setOnMouseClicked(event -> handleCardClick(cardView1));
        imageView2.setOnMouseClicked(event -> handleCardClick(cardView2));
        imageView3.setOnMouseClicked(event -> handleCardClick(cardView3));
        imageView4.setOnMouseClicked(event -> handleCardClick(cardView4));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.add(imageView1, 0, 0);
        gridPane.add(imageView2, 1, 0);
        gridPane.add(imageView3, 0, 1);
        gridPane.add(imageView4, 1, 1);

        return gridPane;
    }

    public void piazzaCartaBR(ImageView cartaSuCuiPiazzo, GridPane board, int id) {
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage,String.valueOf(id));
        ImageView im1 = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView im2 = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView im3 = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView im4 = (ImageView) gridPanePlacingOn.getChildren().get(3);
        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);
        board.add(im1,y, x);
        board.add(im2,(y + 1), x);
        board.add(im3,y, (x + 1));
        board.add(im4,(y + 1), (x + 1));
        allCardViews.add(new CardView(im1, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(im2, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(im3, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(im4, String.valueOf(id), "BR"));
        cardIndices.put(new CardView(im1, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(im2, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(im3, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(im4, String.valueOf(id), "BR"), nextCardIndex);
        im1.setDisable(true);

    }

    public void piazzaCartaBL(ImageView cartaSuCuiPiazzo, GridPane board, int id) {
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));
        ImageView im1 = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView im2 = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView im3 = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView im4 = (ImageView) gridPanePlacingOn.getChildren().get(3);
        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);
        board.add(im1, (y - 1), (x));
        board.add(im2, (y), (x));
        board.add(im3, (y - 1), (x + 1));
        board.add(im4, (y), (x + 1));
        allCardViews.add(new CardView(im1, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(im2, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(im3, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(im4, String.valueOf(id), "BR"));
        cardIndices.put(new CardView(im1, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(im2, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(im3, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(im4, String.valueOf(id), "BR"), nextCardIndex);
    }

    public void piazzaCartaTL(ImageView cartaSuCuiPiazzo, GridPane board, int id) {
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));
        ImageView im1 = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView im2 = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView im3 = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView im4 = (ImageView) gridPanePlacingOn.getChildren().get(3);
        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);
        board.add(im1,(y - 1), (x - 1));
        board.add(im2,(y), (x - 1));
        board.add(im3,(y - 1),(x));
        board.add(im4,(y),(x));
        allCardViews.add(new CardView(im1, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(im2, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(im3, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(im4, String.valueOf(id), "BR"));
        cardIndices.put(new CardView(im1, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(im2, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(im3, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(im4, String.valueOf(id), "BR"), nextCardIndex);
    }

    public void piazzaCartaTR(ImageView cartaSuCuiPiazzo, GridPane board, int id) {
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cartaSuCuiPiazzo);
        int y = getY(cartaSuCuiPiazzo);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));
        ImageView im1 = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView im2 = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView im3 = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView im4 = (ImageView) gridPanePlacingOn.getChildren().get(3);
        im1.setPickOnBounds(true);
        im2.setPickOnBounds(true);
        im3.setPickOnBounds(true);
        im4.setPickOnBounds(true);
        board.add(im1, (y), (x - 1));
        board.add(im2, (y + 1), (x - 1));
        board.add(im3, (y), (x));
        board.add(im4, (y + 1), (x));
        allCardViews.add(new CardView(im1, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(im2, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(im3, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(im4, String.valueOf(id), "BR"));
        cardIndices.put(new CardView(im1, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(im2, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(im3, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(im4, String.valueOf(id), "BR"), nextCardIndex);
    }

    public CardView getClickedImageView() {
        return clickedCardView;
    }

    private synchronized void firstWellCard() throws IOException {
        out.println("firstWellId");
        SharedObjectsInGui.setIdCard1(in.readLine());
        System.out.println("First well card id: " + SharedObjectsInGui.getIdCard1());
    }

    private synchronized void secondWellCard() throws IOException {
        out.println("secondWellId");
        SharedObjectsInGui.setIdCard2(in.readLine());
        System.out.println("Second well card id: " + SharedObjectsInGui.getIdCard2());
    }

    private synchronized void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        SharedObjectsInGui.setIdCard3(in.readLine());
        System.out.println("Third well card id: " + SharedObjectsInGui.getIdCard3());
    }

    private synchronized void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        SharedObjectsInGui.setIdCard4(in.readLine());
        System.out.println("Fourth well card id: " + SharedObjectsInGui.getIdCard4());
    }

    private synchronized void updatingResourceAndGoldDeck() throws IOException {
        out.println("firstCardResourceGui");
        SharedObjectsInGui.setPathResourceDeck(in.readLine());
        out.println("firstCardGoldGui");
        SharedObjectsInGui.setPathGoldDeck(in.readLine());
    }

    private void checkTypeWellCards() {
        SharedObjectsInGui.setWellPathOne(createPathForFrontCards(SharedObjectsInGui.getIdCard1()));
        SharedObjectsInGui.setWellPathSecond(createPathForFrontCards(SharedObjectsInGui.getIdCard2()));
        SharedObjectsInGui.setWellPathThird(createPathForFrontCards(SharedObjectsInGui.getIdCard3()));
        SharedObjectsInGui.setWellPathForth(createPathForFrontCards(SharedObjectsInGui.getIdCard4()));
    }

    private synchronized void playerDeck() throws IOException {
        out.println("deckId");
        idHandCard1 = in.readLine();
        idHandCard2 = in.readLine();
        idHandCard3 = in.readLine();
    }

    private synchronized void checkTypePlayerDeck() throws IOException {
        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);
        in.readLine();
    }

    private String checkType(String id) {
        int intId = Integer.parseInt(id);
        return intId > 0 && intId <= 40 ? "Resource" : "Gold";
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
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
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
            if (isCurrentPlayerTurn) {
                chosenDeckForDrawingNewCard = "resource";
                wellOrDeck = "deck";
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
        SharedObjectsInGui.getTopCardGoldDeckView().setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                chosenDeckForDrawingNewCard = "gold";
                wellOrDeck = "deck";
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
    }

    private void creatingImagesForTheWell() {
        SharedObjectsInGui.setWellCard1(createNewPathForImages(SharedObjectsInGui.getWellPathOne()));
        SharedObjectsInGui.setWellCard2(createNewPathForImages(SharedObjectsInGui.getWellPathSecond()));
        SharedObjectsInGui.setWellCard3(createNewPathForImages(SharedObjectsInGui.getWellPathThird()));
        SharedObjectsInGui.setWellCard4(createNewPathForImages(SharedObjectsInGui.getWellPathForth()));
    }

   private void creatingImagesViewForTheWell() {
       ImageView wellCard1View = new ImageView(SharedObjectsInGui.getWellCard1());
       ImageView wellCard2View = new ImageView(SharedObjectsInGui.getWellCard2());
       ImageView wellCard3View = new ImageView(SharedObjectsInGui.getWellCard3());
       ImageView wellCard4View = new ImageView(SharedObjectsInGui.getWellCard4());
       setWidthAndHeight(wellCard1View);
       setWidthAndHeight(wellCard2View);
       setWidthAndHeight(wellCard3View);
       setWidthAndHeight(wellCard4View);
       SharedObjectsInGui.setWellCard1View(wellCard1View);
       SharedObjectsInGui.setWellCard2View(wellCard2View);
       SharedObjectsInGui.setWellCard3View(wellCard3View);
       SharedObjectsInGui.setWellCard4View(wellCard4View);
   }


    private void settingWellOnMouseClickedEvent() {
        SharedObjectsInGui.getWellCard1View().setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                wellOrDeck = "well";
                wellCardSelected = SharedObjectsInGui.getWellCard1();
                idWellCardSelected = SharedObjectsInGui.getIdCard1();
                indexCardFromWellSelected = 0;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
        SharedObjectsInGui.getWellCard2View().setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                wellOrDeck = "well";
                wellCardSelected = SharedObjectsInGui.getWellCard2();
                idWellCardSelected = SharedObjectsInGui.getIdCard2();
                indexCardFromWellSelected = 1;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
        SharedObjectsInGui.getWellCard3View().setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                wellOrDeck = "well";
                wellCardSelected = SharedObjectsInGui.getWellCard3();
                idWellCardSelected = SharedObjectsInGui.getIdCard3();
                indexCardFromWellSelected = 2;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
        SharedObjectsInGui.getWellCard4View().setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                wellOrDeck = "well";
                wellCardSelected = SharedObjectsInGui.getWellCard4();
                idWellCardSelected = SharedObjectsInGui.getIdCard4();
                indexCardFromWellSelected = 3;
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });
    }

    private String createPathForBackCards(String cardId) {
        return Integer.parseInt(cardId) < 41 ? pathForResourceCardsBack(cardId) : pathForGoldCardBack(cardId);
    }

    private String createPathForFrontCards(String cardId) {
        return Integer.parseInt(cardId) < 41 ? pathForResourceCardFront(cardId) : pathForGoldCardFront(cardId);
    }

    private String pathForResourceCardFront(String cardId) {
        return "/ImmaginiCodex/CarteFront/Resource/" + cardId + ".png";
    }

    private String pathForGoldCardFront(String cardId) {
        return "/ImmaginiCodex/CarteFront/Gold/" + cardId + ".png";
    }

    private String pathForResourceCardsBack(String cardId) {
        return "/ImmaginiCodex/CarteBack/Resource/" + cardId + ".png";
    }

    private String pathForGoldCardBack(String cardId) {
        return "/ImmaginiCodex/CarteBack/Gold/" + cardId + ".png";
    }

    private void creatingWell() {
        SharedObjectsInGui.setWellPathOne(createPathForFrontCards(SharedObjectsInGui.getIdCard1()));
        SharedObjectsInGui.setWellPathSecond(createPathForFrontCards(SharedObjectsInGui.getIdCard2()));
        SharedObjectsInGui.setWellPathThird(createPathForFrontCards(SharedObjectsInGui.getIdCard3()));
        SharedObjectsInGui.setWellPathForth(createPathForFrontCards(SharedObjectsInGui.getIdCard4()));
        //SharedObjectsInGui.getWellCard1View().setImage(newWellResourceCardImage);
    }

    private void initializeWell() throws IOException {
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
    }
    private void updateResourceDeckTopCard() throws IOException {
        out.println("firstCardResourceGui");
        String newTopCardResourceDeckId = in.readLine();
        System.out.println(newTopCardResourceDeckId);
        String newPathResource = "/ImmaginiCodex/CarteBack/Resource/" + newTopCardResourceDeckId + ".png";
        System.out.println(newPathResource);
        Image newTopCardResourceDeckImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPathResource)));
        SharedObjectsInGui.getTopCardResourceDeckView().setImage(newTopCardResourceDeckImage);
        SharedObjectsInGui.setPathResourceDeck(newPathResource);
        SharedObjectsInGui.setTopCardResourceDeck(newTopCardResourceDeckImage);
    }
    private void updatedGoldDeckTopCard() throws IOException {
        out.println("firstCardGoldGui");
        String newTopCardGoldDeckId = in.readLine();
        System.out.println(newTopCardGoldDeckId);
        String newPathGold = "/ImmaginiCodex/CarteBack/Gold/" + newTopCardGoldDeckId + ".png";
        Image newTopCardGoldImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPathGold)));
        SharedObjectsInGui.getTopCardGoldDeckView().setImage(newTopCardGoldImage);
        SharedObjectsInGui.setPathGoldDeck(newPathGold);
        SharedObjectsInGui.setTopCardGoldDeck(newTopCardGoldImage);
    }


    public int getX(ImageView img) {
        return GridPane.getRowIndex(img);
    }

    public int getY(ImageView img) {
        return GridPane.getColumnIndex(img);
    }
    private void creatingButtons(){
        buttonContainer.add(playCard, 0, 0);
        buttonContainer.add(drawCard, 1, 0);
        buttonContainer.add(seeYourSpecificSeeds, 2, 0);
        buttonContainer.add(flipCardToBack, 0, 1);
        buttonContainer.add(flipCardToFront, 0, 2);
        buttonContainer.add(seeYourPoints, 0, 3);
        buttonContainer.add(endTurn, 2, 1);
        buttonContainer.add(seeOtherPlayersBoards, 1, 1);
    }

        public void setClickedCardView(CardView cardView) {
            GameSceneController.clickedCardView = cardView;
        }
    private void updateWell() throws IOException {
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();

        Image newWellCard1Image = createNewPathForImages(SharedObjectsInGui.getWellPathOne());
        Image newWellCard2Image = createNewPathForImages(SharedObjectsInGui.getWellPathSecond());
        Image newWellCard3Image = createNewPathForImages(SharedObjectsInGui.getWellPathThird());
        Image newWellCard4Image = createNewPathForImages(SharedObjectsInGui.getWellPathForth());

        SharedObjectsInGui.getWellCard1View().setImage(newWellCard1Image);
        SharedObjectsInGui.getWellCard2View().setImage(newWellCard2Image);
        SharedObjectsInGui.getWellCard3View().setImage(newWellCard3Image);
        SharedObjectsInGui.getWellCard4View().setImage(newWellCard4Image);
    }

    private void updateGUI() throws IOException {
        Platform.runLater(() -> {
            try {
                updateResourceDeckTopCard();
                updatedGoldDeckTopCard();
                updateWell();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    }

