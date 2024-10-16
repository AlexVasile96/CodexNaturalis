package it.polimi.ingsw.network.client.gui.controllers;

import com.google.gson.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.client.gui.scene.*;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSceneController {
    private StackPane root = new StackPane();
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
    private Button playCard = new Button("Play Card");
    private Button flipCardToBack = new Button("Flip Card to back");
    private Button flipCardToFront = new Button("Flip Card to front");
    private Button drawCard = new Button("Draw card");
    private Button seeYourSpecificSeeds = new Button("See your seeds");
    private Button seeYourPoints = new Button("See your points");
    private Button chat = new Button("Chat");
    Button showObjective = new Button("Show Objective");
    private Button endTurn = new Button("End turn");
    private Button showAllPoints = new Button("See all point");
    private Button quit = new Button("Quit");
    private Button showAllBoards = new Button("Show all boards");
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
    private String cornerSelected = null;
    private Image wellCardSelected = null;
    private String idWellCardSelected = null;
    private static Controller controller;
    private String currentPlayerNickname;
    private ClientView clientView;
    private BoardPointsScene boardPointsScene;
    private ShowAllBoardsScene showAllBoardsScene;
    private boolean isCurrentPlayerTurn = false;
    private ScrollPane cardsOntheBoardScrollPane;
    private GridPane gameBoard;
    GridPane buttonContainer = new GridPane();
    Label chosenCardToPlace = new Label();
    Label chosenCardToBePlacedOn = new Label();
    Label chosenCorner = new Label();
    private List<CardView> allCardViews = new ArrayList<>();
    private static CardView clickedCardView;
    private Map<CardView, Integer> cardIndices = new HashMap<>();
    private int nextCardIndex = 0; //
    private String pathHandCard1;
    private String pathHandCard2;
    private String pathHandCard3;
    private String pathChosen;
    private GridPane gridPaneForWellCards;
    String firstCommonId;
    String secondCommonId;
    String secretId;
    ShowObjectiveScene objectiveScene;
    private String pathFlipped;
    private Image initCardImage;
    private ImageView handCard1View;
    private ImageView handCard2View;
    private ImageView handCard3View;
    private Image handCard1;
    private Image handCard2;
    private Image handCard3;
    private String indexForGold=null;
    private final Color targetColor = Color.web("#DDD7A0");
    private boolean isInit = true;
    private String isTheCardFlipped = null;
    private boolean isFlipped = false;
    private boolean areYouTheWinner=false;
    private boolean isNotToBePlacedon =false;
    private ChatScene chatScene;
    private ChatController chatController;


    /**
     * Initializes the game data and sets up the initial stage, socket connections, and view components.
     *
     * @param primaryStage The primary stage of the application.
     * @param out The PrintWriter for sending data to the server.
     * @param socket The client socket for communication.
     * @param in The BufferedReader for receiving data from the server.
     * @param clientView The client's view component.
     * @param currentPlayerNickname The nickname of the current player.
     * @throws IOException If an I/O error occurs.
     */

    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, String currentPlayerNickname) throws IOException {
        // Initialize the main stage, output stream, socket, input stream, and client view
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in = in;
        this.currentPlayerNickname = currentPlayerNickname;
        this.clientView = clientView;

        // Initialize the controller with the input stream, output stream, and socket
        if(controller==null){
        controller = new Controller(in, out, socket,clientView);}

        // Determine if it's the current player's turn based on the nickname
        isCurrentPlayerTurn = clientView.getUserName().equals(currentPlayerNickname);

        // Set a socket timeout of 2 minutes
        socket.setSoTimeout(120000);
    }

    /**
     * Updates the game state for the first time, initializing resources and UI elements.
     *
     * @throws IOException If an I/O error occurs.
     */

    public synchronized void updateFirst() throws IOException {
        synchronized (syncObject) {
            // Initialize resources and UI elements for the game
            initializeWell();
            updatingResourceAndGoldDeck();
            creatingPathForGameMethod();

            // Set the top card images for resource and gold decks
            SharedObjectsInGui.setTopCardResourceDeck(createNewPathForImages(SharedObjectsInGui.getPathResourceDeck()));
            SharedObjectsInGui.setTopCardGoldDeck(createNewPathForImages(SharedObjectsInGui.getPathGoldDeck()));

            // Send requests to the server to get the top card IDs
            out.println("firstCardResourceGui");
            idTopCardResourceDeck = in.readLine();
            out.println("firstCardGoldGui");
            idTopCardGoldDeck = in.readLine();

            // Creating views for decks and well
            creatingDeckAndGoldDeckView();
            creatingWell();
            creatingImagesForTheWell();
            creatingImagesViewForTheWell();

            // Set event handlers for clicking on the well and decks
            settingWellOnMouseClickedEvent();
            settingDecksOnMouseClickedEvent();

            // Initializing player deck and check its type
            playerDeck();
            checkTypePlayerDeck();
        }
    }

    /**
     * Starts the game by setting up the game board and displaying the initial card.
     *
     * @param initCardId The ID of the initial card.
     * @param isFront Indicates whether the card is front (1) or back (0).
     * @throws IOException If an I/O error occurs.
     */

    public void startGame(String initCardId, int isFront) throws IOException {
        // Load and set the background image for the game board
        try {
            Image backGroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
            BackgroundImage backgroundImage = new BackgroundImage(backGroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);

            // Set the paths for the player's hand cards
            pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
            pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
            pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";

            // Load the hand card images
            handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
            handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
            handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));

            // Create image views for the hand cards and set their dimensions
            handCard1View = new ImageView(handCard1);
            setWidthAndHeight(handCard1View);

            handCard2View = new ImageView(handCard2);
            handCard2View.setFitWidth(widthWellCards);
            handCard2View.setFitHeight(heightWellCards);

            handCard3View = new ImageView(handCard3);
            handCard3View.setFitWidth(widthWellCards);
            handCard3View.setFitHeight(heightWellCards);

            // Initialize the scroll pane for the game board
            cardsOntheBoardScrollPane = new ScrollPane();
            cardsOntheBoardScrollPane.setPrefSize(400.00, 400.00);
            int boardDimension = 500;

            // Create and configure the game board
            gameBoard = new GridPane();
            gameBoard.setBackground(background);
            gameBoard.setHgap(0);
            gameBoard.setVgap(0);
            gameBoard.setAlignment(Pos.CENTER);

            double windowedLength = root.getWidth();
            double windowHeight = root.getHeight();

            gameBoard.setPrefSize(windowedLength * 0.8, windowHeight * 0.8);

            // Load the initial card image based on whether it is front or back
            if (isFront == 1) {
                initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Init/" + initCardId + ".png")));
            } else {
                initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteBack/Init/" + initCardId + ".png")));
            }


            // Divide the initial card image into a 3x3 grid
            GridPane initCardDividedInNineRegions = submittingEachImage(initCardImage, initCardId);

            // Create CardView objects for each region of the initial card
            ImageView tlImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(0);
            ImageView tcImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(1);
            ImageView trImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(2);
            ImageView lcImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(3);
            ImageView ccImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(4);
            ImageView rcImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(5);
            ImageView blImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(6);
            ImageView bcImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(7);
            ImageView brImageView = (ImageView) initCardDividedInNineRegions.getChildren().get(8);

            CardView tlCardView = new CardView(tlImageView, initCardId, "TL");
            CardView tcCardView = new CardView(tcImageView, initCardId, "TC");
            CardView trCardView = new CardView(trImageView, initCardId, "TR");
            CardView lcCardView = new CardView(lcImageView, initCardId, "LC");
            CardView ccCardView = new CardView(ccImageView, initCardId, "CC");
            CardView rcCardView = new CardView(rcImageView, initCardId, "RC");
            CardView blCardView = new CardView(blImageView, initCardId, "BL");
            CardView bcCardView = new CardView(bcImageView, initCardId, "BC");
            CardView brCardView = new CardView(brImageView, initCardId, "BR");

            // Increment the card index for the initial card and add it to the card indices map
            nextCardIndex++; // 1 for initial card
            cardIndices.put(tlCardView, nextCardIndex);
            cardIndices.put(tcCardView, nextCardIndex);
            cardIndices.put(trCardView, nextCardIndex);
            cardIndices.put(lcCardView, nextCardIndex);
            cardIndices.put(ccCardView, nextCardIndex);
            cardIndices.put(rcCardView, nextCardIndex);
            cardIndices.put(blCardView, nextCardIndex);
            cardIndices.put(bcCardView, nextCardIndex);
            cardIndices.put(brCardView, nextCardIndex);

            // Enable interaction with some card regions
            tlImageView.setPickOnBounds(true);
            trImageView.setPickOnBounds(true);
            blImageView.setPickOnBounds(true);
            brImageView.setPickOnBounds(true);

            // Add the initial card regions to the game board
            gameBoard.add(tlImageView, boardDimension / 3, boardDimension / 3);
            gameBoard.add(tcImageView, boardDimension / 3 + 1, boardDimension / 3);
            gameBoard.add(trImageView, boardDimension / 3 + 2, boardDimension / 3);
            gameBoard.add(lcImageView, boardDimension / 3, boardDimension / 3 + 1);
            gameBoard.add(ccImageView, boardDimension / 3 + 1, boardDimension / 3 + 1);
            gameBoard.add(rcImageView, boardDimension / 3 + 2, boardDimension / 3 + 1);
            gameBoard.add(blImageView, boardDimension / 3, boardDimension / 3 + 2);
            gameBoard.add(bcImageView, boardDimension / 3 + 1, boardDimension / 3 + 2);
            gameBoard.add(brImageView, boardDimension / 3 + 2, boardDimension / 3 + 2);

            // Set the scroll pane content to the game board
            cardsOntheBoardScrollPane.setContent(gameBoard);

            // Create the main layout and set its background color
            BorderPane layout = new BorderPane();
            layout.setCenter(cardsOntheBoardScrollPane);
            layout.setStyle("-fx-background-color: #212121;");

            // Create a VBox for the game UI elements
            VBox vboxGame = new VBox(0);
            vboxGame.setFillWidth(true);

            Insets padding = new Insets(10, 10, 10, 10);
            Insets paddingDecks = new Insets(100, 10, 10, 10);

            // Create the second row HBox and the first column HBox
            HBox secondRow = new HBox();
            HBox firstColumnOfSecondRow = new HBox();
            firstColumnOfSecondRow.setPadding(padding);
            firstColumnOfSecondRow.setSpacing(4);

            // Add labels for the well cards
            Label wellText = new Label("                    WELL");
            Label wellText2 = new Label("CARDS");
            wellText.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");
            wellText2.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");

            // Initialize the grid pane for well cards
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

            // Create an HBox for the decks
            HBox decks = new HBox();
            Label decksText = new Label("      Resource Deck               Gold Deck");
            decksText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

            decks.setSpacing(4.0);
            decks.setPadding(paddingDecks);
            decks.getChildren().addAll(SharedObjectsInGui.getTopCardResourceDeckView(), SharedObjectsInGui.getTopCardGoldDeckView());

            // Initialize and disable the button container
            buttonContainer.setHgap(2);
            buttonContainer.setVgap(2);
            creatingButtons();
            vboxGame.getChildren().addAll(gridPaneForWellCards, decks, decksText, buttonContainer);
            buttonContainer.setDisable(true);

            // Create the second column VBox
            VBox secondColumnOfSecondRow = new VBox();

            // Set styles for chosen card labels
            chosenCardToPlace.setStyle("-fx-text-fill: white;");
            chosenCardToBePlacedOn.setStyle("-fx-text-fill: white;");
            chosenCorner.setStyle("-fx-text-fill: white;");

            // Add labels to the second column
            secondColumnOfSecondRow.getChildren().addAll(chosenCardToPlace, chosenCardToBePlacedOn, chosenCorner);

            // Add UI elements to the layout
            layout.setRight(vboxGame);
            firstColumnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
            secondRow.getChildren().addAll(firstColumnOfSecondRow, secondColumnOfSecondRow);
            layout.setBottom(secondRow);
            root.getChildren().add(layout);

            // Set up the game scene
            Scene gameScene = new Scene(root, 430, 625);
            Platform.runLater(() -> primaryStage.setScene(gameScene));

            // Determine if it's the current player's turn and set up actions accordingly
            if (currentPlayerNickname.equals(clientView.getUserName())) {
                isCurrentPlayerTurn = true;
                waitUntilLastMessage();
                setupGameActions();
                buttonContainer.setDisable(false);
            } else {
                isCurrentPlayerTurn = false;
                waitForTurn();
            }
        } catch(SocketTimeoutException | SocketException e)
        {
            handleDisconnection();
        }
    }

    /**
     * Handles the click event on a card view.
     *
     * @param cardView The card view that was clicked.
     */

    private void handleCardClick(CardView cardView) {
        // Check if it's the current player's turn
        if (isCurrentPlayerTurn) {
            // Set the clicked card view and selected corner
            setClickedCardView(cardView);
            cornerSelected = cardView.getPosition();
            Integer cardIndex = cardIndices.get(cardView);

            if (cardIndex != null) {
                indexCardToBePlacedOn = cardIndex;
            }

            // Update UI labels with selected corner and index
            chosenCorner.setText("Corner: " + cornerSelected);
            chosenCardToBePlacedOn.setText("Place your card on: " + indexCardToBePlacedOn);
        } else {
            // If it's not the current player's turn, show a message
            showAlert("Not your turn", "It's not your turn yet.");
        }
    }

    /**
     * Sets up the actions for various game events.
     *
     * @throws IOException if an I/O error occurs.
     */

    private void setupGameActions() throws IOException {
        // Action for playing a card
        playCard.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                String canIPlaceTheGoldCard = null;
                if (haveToPlay) {
                    // Check if a corner is selected before playing a card
                    if (cornerSelected == null) {
                        showAlert("Corner Missing", "Please select a corner before playing a card");
                        return;
                    }

                    // Verifies if the selected card is a gold card
                    if(isNotToBePlacedon)
                    {
                        Platform.runLater(()->showAlert("Can't place the card here","You can't place your card here!"));
                        isNotToBePlacedon = false;
                        return;
                    }
                    if (indexForGold != null && Integer.parseInt(indexForGold) > 40) {
                        // Check if the card is flipped to "front"
                        if (!isFlipped) {
                            out.println("goldGui" + indexCardToPlace.toString());
                            try {
                                canIPlaceTheGoldCard = in.readLine();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (canIPlaceTheGoldCard.equals("NO")) {
                                showAlert("Gold Card Not Placeable", "You can't place the gold card because you don't have enough specific seeds");
                                indexForGold = null;
                                indexCardToPlace = 100;
                                haveToDraw = false;
                                return;
                            }
                        }
                    }

                    // Check if a corner is selected and indexCardToPlace is valid
                    if (cornerSelected != null && indexCardToPlace < 4) {
                        try {
                            // Play the selected card
                            switch (indexCardToPlace) {
                                case 0:
                                    handCard1View.setImage(null);
                                    isTheCardFlipped = handCard1View.getId();
                                    break;
                                case 1:
                                    handCard2View.setImage(null);
                                    isTheCardFlipped = handCard2View.getId();
                                    break;
                                case 2:
                                    handCard3View.setImage(null);
                                    isTheCardFlipped = handCard3View.getId();
                                    break;
                                default:
                                    break;
                            }

                            controller.playCardClick(indexCardToBePlacedOn, indexCardToPlace, cornerSelected, isTheCardFlipped);
                            indexCardPlayedFromHand = indexCardToPlace;

                            // Place the played card on the game board
                            switch (cornerSelected) {
                                case "TL":
                                    placingTopLeftCard(getClickedImageView().getImageView(), gameBoard, indexCardPlayedFromHand);
                                    break;
                                case "TR":
                                    placingTopRightCard(getClickedImageView().getImageView(), gameBoard, indexCardPlayedFromHand);
                                    break;
                                case "BL":
                                    placingBottomLeftCard(getClickedImageView().getImageView(), gameBoard, indexCardPlayedFromHand);
                                    break;
                                case "BR":
                                    placingBottomRightCard(getClickedImageView().getImageView(), gameBoard, indexCardPlayedFromHand);
                                    break;
                                default:
                                    break;
                            }

                            // Update flags and UI elements
                            haveToDraw = true;
                            handCard1View.setId("Front");
                            handCard2View.setId("Front");
                            handCard3View.setId("Front");
                            cornerSelected = null;
                            indexCardToPlace = 100;
                            check20Points();
                        } catch (IOException exception) {
                            throw new RuntimeException(exception);
                        }
                        haveToPlay = false;
                    } else {
                        showAlert("Action not allowed", "Choose the right cards.");
                    }
                } else {
                    showAlert("Action not allowed", "You have already played a card.");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        showAllBoards.setOnMouseClicked(e ->{
            if(isCurrentPlayerTurn){
                try {
                    ShowAllBoardsScene allBoardsScene = new ShowAllBoardsScene(primaryStage, out, socket, in, clientView);
                    allBoardsScene.showAllBoards();
                }catch (SocketException | SocketTimeoutException exe){
                    handleDisconnection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        // Action for ending the turn
        endTurn.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                if (!haveToDraw && !haveToPlay) {
                    try {
                        savePath();
                        String nextPlayerNickname = controller.endTurn();
                        updateTurnState(nextPlayerNickname.equals(clientView.getUserName()));
                        haveToPlay = true;
                        waitForTurn();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (haveToDraw) {
                    showAlert("HaveToDraw", "You have to draw a card first");
                } else {
                    showAlert("HaveToPlay", "You have to place a card first");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        drawCard.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                // Check if the player can draw a card
                if (haveToDraw) {
                    try {
                        // Draw a card from the specified deck
                        controller.drawCard(wellOrDeck, chosenDeckForDrawingNewCard, indexCardFromWellSelected);
                        Image drawnCardImage = null;
                        String idTopCard = null;

                        // Determine the image and ID of the drawn card based on the deck
                        if (wellOrDeck.equals("deck")) {
                            if (chosenDeckForDrawingNewCard.equals("resource")) {
                                drawnCardImage = createNewPathForImages(pathForResourceCardFront(idTopCardResourceDeck));
                                idTopCard = idTopCardResourceDeck;
                            } else if (chosenDeckForDrawingNewCard.equals("gold")) {
                                drawnCardImage = createNewPathForImages(pathForGoldCardFront(idTopCardGoldDeck));
                                idTopCard = idTopCardGoldDeck;
                            }
                        } else if (wellOrDeck.equals("well")) {
                            drawnCardImage = wellCardSelected;
                            idTopCard = idWellCardSelected;
                        }

                        // Set the image of the drawn card to the appropriate hand card view
                        if (drawnCardImage != null && idTopCard != null) {
                            switch (indexCardPlayedFromHand) {
                                case 0:
                                    handCard1View.setImage(drawnCardImage);
                                    pathHandCard1 = "/ImmaginiCodex/CarteFront/" + checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard1 = idTopCard;
                                    break;
                                case 1:
                                    handCard2View.setImage(drawnCardImage);
                                    pathHandCard2 = "/ImmaginiCodex/CarteFront/" + checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard2 = idTopCard;
                                    break;
                                case 2:
                                    handCard3View.setImage(drawnCardImage);
                                    pathHandCard3 = "/ImmaginiCodex/CarteFront/" + checkType(idTopCard) + "/" + idTopCard + ".png";
                                    idHandCard3 = idTopCard;
                                    break;
                            }
                            // Update the images of the well cards and decks

                            if(clientView.getPlayerScore()>=20&& !Controller.isEndgame()){ //One player reached 20 points, so all clients have one last turn and then the game will end
                                    firstWellCard();
                                    in.readLine(); //you smashed 20 points
                                    in.readLine(); //next player
                                    currentPlayerNickname=controller.finalEnd();
                                    updateTurnState(currentPlayerNickname.equals(clientView.getUserName()));
                                    haveToPlay = true;
                                    waitForTurn();
                                    haveToDraw = false;
                                    areYouTheWinner=true;
                                    clientView.setAreYouTheWinner(true);
                                    Controller.setEndgame(true);
                            }
                            else {
                                initializeWell();
                                recreatingPath();
                                updateResourceDeckTopCard();
                                updatedGoldDeckTopCard();
                                haveToDraw = false;
                            }
                        } else {
                            showAlert("Action not allowed", "You have to choose a card to draw");
                        }
                    } catch (SocketTimeoutException | SocketException exx) {
                        handleDisconnection(); }
                    catch (IOException ex) {
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
                // Set the card to be flipped to the back
                isFlipped = true;
                // Switch statement to handle flipping each card
                switch (indexCardToPlace) {
                    case 0:
                        handCard1View.setImage(flipToBackCard(idHandCard1));
                        handCard1View.setId("Back");
                        if (handCard1View.getId() == null || handCard1View.getId().equals("Front")) {
                            pathChosen = pathHandCard1;
                        } else {
                            pathChosen = pathFlipped;
                        }
                        break;
                    case 1:
                        handCard2View.setImage(flipToBackCard(idHandCard2));
                        handCard2View.setId("Back");
                        if (handCard2View.getId() == null || handCard2View.getId().equals("Front")) {
                            pathChosen = pathHandCard1;
                        } else {
                            pathChosen = pathFlipped;
                        }
                        break;
                    case 2:
                        handCard3View.setImage(flipToBackCard(idHandCard3));
                        handCard3View.setId("Back");
                        handCard2View.setId("Back");
                        if (handCard3View.getId() == null || handCard3View.getId().equals("Front")) {
                            pathChosen = pathHandCard1;
                        } else {
                            pathChosen = pathFlipped;
                        }
                        break;
                    default:
                        showAlert("Invalid action", "You chose an unflippable card.");
                        break;
                }
            } else {
                flipCardToBack.setDisable(true);
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        /*
         * Sets the action for flipping the selected card to the front.
         */
        flipCardToFront.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                // Set the card to be flipped to the front
                isFlipped = false;
                // Switch statement to handle flipping each card
                switch (indexCardToPlace) {
                    case 0:
                        handCard1View.setImage(flipCardToFront(idHandCard1));
                        handCard1View.setId("Front");
                        break;
                    case 1:
                        handCard2View.setImage(flipCardToFront(idHandCard2));
                        handCard2View.setId("Front");
                        break;
                    case 2:
                        handCard3View.setImage(flipCardToFront(idHandCard3));
                        handCard3View.setId("Front");
                        break;
                    default:
                        showAlert("Invalid action", "You chose an unflippable card.");
                        break;
                }
            } else {
                flipCardToFront.setDisable(true);
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        handCard1View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                // Set the index of the card to be placed
                indexCardToPlace = 0;
                chosenCardToPlace.setText("First card of your hand");

                // Check if the card is flipped or not and set the path accordingly
                if (handCard1View.getId() == null || handCard1View.getId().equals("Front")) {
                    pathChosen = pathHandCard1;
                } else {
                    pathChosen = pathFlipped;
                }

                // Determine if the selected card is a ResourceCard or a GoldCard
                if (Integer.parseInt(idHandCard1) < 41) {
                } else {
                    indexForGold = idHandCard1;
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        handCard2View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                // Set the index of the card to be placed
                indexCardToPlace = 1;
                chosenCardToPlace.setText("Second card of your hand");
                pathChosen = pathHandCard2;

                // Check if the card is flipped or not and set the path accordingly
                if (handCard2View.getId() == null || handCard2View.getId().equals("Front")) {
                    pathChosen = pathHandCard2;
                } else {
                    pathChosen = pathFlipped;
                }

                // Determine if the selected card is a ResourceCard or a GoldCard
                if (Integer.parseInt(idHandCard2) < 41) {
                } else {
                    indexForGold = idHandCard2;
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard3View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                // Set the index of the card to be placed
                indexCardToPlace = 2;
                chosenCardToPlace.setText("Third card of your hand");
                pathChosen = pathHandCard3;

                // Check if the card is flipped or not and set the path accordingly
                if (handCard3View.getId() == null || handCard3View.getId().equals("Front")) {
                    pathChosen = pathHandCard3;
                } else {
                    pathChosen = pathFlipped;
                }

                // Determine if the selected card is a ResourceCard or a GoldCard
                if (Integer.parseInt(idHandCard3) < 41) {
                } else {
                    indexForGold = idHandCard3;
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        seeYourSpecificSeeds.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    // Retrieve and display the specific seeds of the current player
                    String yourSeeds = controller.showSpecificSeed();
                    showAlert("Your seeds at the moment", yourSeeds);
                }catch (SocketTimeoutException | SocketException exx) {
                    handleDisconnection(); }
                    catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                // Alert if it's not the current player's turn
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        seeYourPoints.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    // Display the points of the current player
                    boardPointsScene = new BoardPointsScene(primaryStage, out, socket, in, clientView);
                    boardPointsScene.popupBoardPoints();
                }catch (SocketTimeoutException | SocketException exx) {
                    handleDisconnection(); }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                // Disable the button and alert if it's not the current player's turn
                seeYourPoints.setDisable(true);
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });


        showAllPoints.setOnMouseClicked(e -> {
            if(isCurrentPlayerTurn) {
                try {
                    ShowAllPointsScene allPointsScene = new ShowAllPointsScene(primaryStage, out, socket, in, clientView);
                    allPointsScene.showAllPointsPopup();

                }catch (SocketTimeoutException | SocketException exx) {
                    handleDisconnection(); }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else {
                // Alert if it's not the current player's turn
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        showObjective.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    // Display the objectives of the current player
                    objectiveScene = new ShowObjectiveScene(primaryStage, out, socket, in);
                    firstCommonId = controller.firstCommon();
                    secondCommonId = controller.secondCommon();
                    secretId = controller.secretCard();
                    objectiveScene.popupObjectiveScene(firstCommonId, secondCommonId, secretId);

                } catch (SocketTimeoutException | SocketException exx) {
                    handleDisconnection(); }catch (IOException ex ) {
                    throw new RuntimeException(ex);
                }
            } else {
                // Disable the button and alert if it's not the current player's turn
                showObjective.setDisable(true);
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        chat.setOnMouseClicked(e->{
            if(chatScene == null){
                chatScene = new ChatScene(clientView.getUserName());
                chatController = new ChatController(chatScene, clientView.getUserName());
                chatController.start();
            }
            chatScene.show();
        });


        quit.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try{
                    // Call the quit method from the controller to handle the quitting process
                    controller.quit(primaryStage);

                    // Close resources
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();

                    // Exit the application
                    Platform.exit();
                    System.exit(0);
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * Waits for the player's turn to begin and updates the GUI accordingly.
     *
     */
    private void waitForTurn() {
        new Thread(() -> {
            try {
                // Wait for the player's turn to begin
                controller.waitForTurn(clientView.getUserName(), primaryStage);
                if(clientView.getPlayerScore()>=20)
                {
                    EndGameScene endGameScene= new EndGameScene(primaryStage,out,socket,in,clientView, controller);
                    try {
                        in.readLine(); //end of turn
                        in.readLine(); //ENDGAME?
                        endGameScene.endGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    // Update the turn state
                    updateTurnState(true);
                    try {
                        // Output a message indicating that the turn has ended
                        in.readLine(); // Fine turno

                        // Update the GUI
                        updateGUI();
                        // Setup game actions
                        setupGameActions();
                    }
                    catch (SocketTimeoutException | SocketException e) {
                        handleDisconnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SocketTimeoutException | SocketException e) {
                handleDisconnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Handles the disconnection of a player.
     */
    private void handleDisconnection() {
        Platform.runLater(() -> {
            // Show an alert indicating the disconnection
            showAlert("Disconnection", "A player has disconnected.");
            try {
                // Save game progress
                savePath();
                System.out.println("Someone quit, thank you for playing codex");
                // Close resources
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                // Exit the application
                Platform.exit();
                System.exit(0);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // Throw a runtime exception if an IOException occurs
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Shows an alert with the given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Enables or disables game actions based on whether it's the current player's turn.
     */
    private void enableOrDisableGameActions() {
        buttonContainer.setDisable(!isCurrentPlayerTurn);
    }

    /**
     * Updates the turn state of the game and enables or disables game actions accordingly.
     *
     * @param isCurrentPlayerTurn true if it's the current player's turn, false otherwise
     */
    private void updateTurnState(boolean isCurrentPlayerTurn) {
        this.isCurrentPlayerTurn = isCurrentPlayerTurn;
        enableOrDisableGameActions();
    }

    /**
     * Flips the given card to its back side based on its ID.
     *
     * @param stringId The ID of the card to flip
     * @return The image of the card's back side
     */
    private Image flipToBackCard(String stringId) {
        int id = Integer.parseInt(stringId);
        if (id >= 1 && id <= 40) {
            pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
        } else if (id > 40 && id <= 80) {
            pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
        }
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
    }

    /**
     * Flips the given card to its front side based on its ID.
     *
     * @param stringId The ID of the card to flip
     * @return The image of the card's front side
     */
    private Image flipCardToFront(String stringId) {
        int id = Integer.parseInt(stringId);
        if (id >= 1 && id <= 40) {
            pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
        } else if (id > 40 && id <= 80) {
            pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
        }
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
    }

    /**
     * Checks if the given image contains the specified color.
     *
     * @param cornerImage The image to check
     * @param targetColor The color to search for
     * @return true if the color is found in the image, false otherwise
     */
    private boolean containsColor(Image cornerImage, Color targetColor) {
        PixelReader pixelReader = cornerImage.getPixelReader();
        if (pixelReader == null) {
            return false;
        }
        int width = (int) cornerImage.getWidth();
        int height = (int) cornerImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                if (color.equals(targetColor)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Splits the given image into a grid of nine equal parts and creates a GridPane with ImageView for each part.
     *
     * @param image The image to be split
     * @param cardId The ID of the card associated with the image
     * @return The GridPane containing ImageView for each part of the image
     */
    public GridPane submittingEachImage(Image image, String cardId) {
        double width = image.getWidth() / 3;
        double height = image.getHeight() / 3;
        int heightInt = (int) height;
        int widthInt = (int) width;
        Image imageTL = new WritableImage(image.getPixelReader(), 0, 0, widthInt, heightInt);
        Image imageTC = new WritableImage(image.getPixelReader(), widthInt, 0, widthInt, heightInt);
        Image imageTR = new WritableImage(image.getPixelReader(), 2 * widthInt, 0, widthInt, heightInt);
        Image imageLC = new WritableImage(image.getPixelReader(), 0, heightInt, widthInt, heightInt);
        Image imageCC = new WritableImage(image.getPixelReader(), widthInt, heightInt, widthInt, heightInt);
        Image imageRC = new WritableImage(image.getPixelReader(), 2 * widthInt, heightInt, widthInt, heightInt);
        Image imageBL = new WritableImage(image.getPixelReader(), 0, 2 * heightInt, widthInt, heightInt);
        Image imageBC = new WritableImage(image.getPixelReader(), widthInt, 2 * heightInt, widthInt, heightInt);
        Image imageBR = new WritableImage(image.getPixelReader(), 2 * widthInt, 2 * heightInt, widthInt, heightInt);

        ImageView imageViewTL = new ImageView(imageTL);
        ImageView imageViewTC = new ImageView(imageTC);
        ImageView imageViewTR = new ImageView(imageTR);
        ImageView imageViewLC = new ImageView(imageLC);
        ImageView imageViewCC = new ImageView(imageCC);
        ImageView imageViewRC = new ImageView(imageRC);
        ImageView imageViewBL = new ImageView(imageBL);
        ImageView imageViewBC = new ImageView(imageBC);
        ImageView imageViewBR = new ImageView(imageBR);

        imageViewTL.setFitWidth(50);
        imageViewTL.setPreserveRatio(true);
        imageViewTC.setFitWidth(50);
        imageViewTC.setPreserveRatio(true);
        imageViewTR.setFitWidth(50);
        imageViewTR.setPreserveRatio(true);
        imageViewLC.setFitWidth(50);
        imageViewLC.setPreserveRatio(true);
        imageViewCC.setFitWidth(50);
        imageViewCC.setPreserveRatio(true);
        imageViewRC.setFitWidth(50);
        imageViewRC.setPreserveRatio(true);
        imageViewBL.setFitWidth(50);
        imageViewBL.setPreserveRatio(true);
        imageViewBC.setFitWidth(50);
        imageViewBC.setPreserveRatio(true);
        imageViewBR.setFitWidth(50);
        imageViewBR.setPreserveRatio(true);

        CardView tlCardView = new CardView(imageViewTL, cardId, "TL");
        CardView tcCardView = new CardView(imageViewTC, cardId, "TC");
        CardView trCardView = new CardView(imageViewTR, cardId, "TR");
        CardView lcCardView = new CardView(imageViewLC, cardId, "LC");
        CardView ccCardView = new CardView(imageViewCC, cardId, "CC");
        CardView rcCardView = new CardView(imageViewRC, cardId, "RC");
        CardView blCardView = new CardView(imageViewBL, cardId, "BL");
        CardView bcCardView = new CardView(imageViewBC, cardId, "BC");
        CardView brCardView = new CardView(imageViewBR, cardId, "BR");

        allCardViews.add(tlCardView);
        allCardViews.add(tcCardView);
        allCardViews.add(trCardView);
        allCardViews.add(lcCardView);
        allCardViews.add(ccCardView);
        allCardViews.add(rcCardView);
        allCardViews.add(blCardView);
        allCardViews.add(bcCardView);
        allCardViews.add(brCardView);

        imageViewTL.setOnMouseClicked(event ->{
            handleCardClick(tlCardView);
            canIPlaceTheCardOrIsThereANotToBePlacedOn(imageViewTL);
                });
        imageViewTR.setOnMouseClicked(event -> {
            handleCardClick(trCardView);
            canIPlaceTheCardOrIsThereANotToBePlacedOn(imageViewTR);
        });
        imageViewBL.setOnMouseClicked(event -> {
            handleCardClick(blCardView);
            canIPlaceTheCardOrIsThereANotToBePlacedOn(imageViewBL);
        });
        imageViewBR.setOnMouseClicked(event -> {
            handleCardClick(brCardView);
            canIPlaceTheCardOrIsThereANotToBePlacedOn(imageViewBR);
        });

        Color targetInit = Color.web("#351F16");

        if (isInit) {
            if (!containsColor(imageTL, targetInit)) {
                imageViewTL.setDisable(true);
            }
            if (!containsColor(imageTR, targetInit)) {
                imageViewTR.setDisable(true);
            }
            if (!containsColor(imageBL, targetInit)) {
                imageViewBL.setDisable(true);
            }
            if (!containsColor(imageBR, targetInit)) {
                imageViewBR.setDisable(true);
            }
        } else {
            if (!containsColor(imageTL, targetColor)) {
                imageViewTL.setDisable(true);
            }
            if (!containsColor(imageTR, targetColor)) {
                imageViewTR.setDisable(true);
            }
            if (!containsColor(imageBL, targetColor)) {
                imageViewBL.setDisable(true);
            }
            if (!containsColor(imageBR, targetColor)) {
                imageViewBR.setDisable(true);
            }
        }

        isInit = false;

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(100); // Sets the preferred width of the first column
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150); // Sets the preferred width of the second column
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(200); // Sets the preferred width of the third column

        // Add column constraints to the GridPane
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        // Define row dimensions
        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(100); // Sets the preferred height of the first row
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(150); // Sets the preferred height of the second row
        RowConstraints row3 = new RowConstraints();
        row3.setPrefHeight(200); // Sets the preferred height of the third row

        // Add row constraints to the GridPane
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        gridPane.add(imageViewTL, 0, 0);
        gridPane.add(imageViewTC, 1, 0);
        gridPane.add(imageViewTR, 2, 0);
        gridPane.add(imageViewLC, 0, 1);
        gridPane.add(imageViewCC, 1, 1);
        gridPane.add(imageViewRC, 2, 1);
        gridPane.add(imageViewBL, 0, 2);
        gridPane.add(imageViewBC, 1, 2);
        gridPane.add(imageViewBR, 2, 2);

        return gridPane;
    }

    /**
     * This method places the card represented by the ImageView cardOnTheBoard in the bottom-right corner of the board.
     *
     * @param cardOnTheBoard The ImageView of the card to be placed on the board.
     * @param board The GridPane representing the board where the card will be placed.
     * @param id The identifier of the card.
     */
    public void placingBottomRightCard(ImageView cardOnTheBoard, GridPane board, int id) {

        // Incrementing the index for the next card

        nextCardIndex++;

        // Loading the image for the card
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));

        // Getting the coordinates for placing the card on the board
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);

        // Creating a grid pane for the new card and its surrounding images
        GridPane gridPanePlacingOn = submittingEachImage(newImage, String.valueOf(id));

        // Extracting ImageView objects for each position on the grid pane
        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        // Allowing picking on the corners for user interaction
        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        Node targetNode;

        targetNode = searchForNode(x, y+2); //check TR
        if (targetNode != null) {
            targetNode.setDisable(true);
            TR.setDisable(true);
        }
        targetNode = searchForNode(x+2, y);//check BL
        if (targetNode != null) {
            targetNode.setDisable(true);
            BL.setDisable(true);
        }
        targetNode = searchForNode(x+2, y+2);//check BR
        if (targetNode != null) {
            targetNode.setDisable(true);
            BR.setDisable(true);
        }

        // Adding the card and surrounding images to the board grid pane
        board.add(TL, y, x);
        board.add(TC, (y + 1), x);
        board.add(TR, (y + 2), x);
        board.add(LC, y, (x + 1));
        board.add(CC, (y + 1), (x + 1));
        board.add(RC, (y + 2), (x + 1));
        board.add(BL, y, (x + 2));
        board.add(BC, (y + 1), (x + 2));
        board.add(BR, (y + 2), (x + 2));

        // Creating CardView objects for each image and adding them to a list
        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        // Setting the index for each card in the cardIndices map
        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);

        // Disabling the bottom-right corner for the previously placed card
        TL.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (Objects.equals(cardIndices.get(cardView), indexCardToBePlacedOn) && cardView.getPosition().equals("BR")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }


    /**
     * This method places the card represented by the ImageView cardOnTheBoard in the bottom-left corner of the board.
     *
     * @param cardOnTheBoard The ImageView of the card to be placed on the board.
     * @param board The GridPane representing the board where the card will be placed.
     * @param id The identifier of the card.
     */
    public void placingBottomLeftCard(ImageView cardOnTheBoard, GridPane board, int id) {
        // Incrementing the index for the next card
        nextCardIndex++;

        // Loading the image for the card
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);

        // Creating a grid pane for the new card and its surrounding images
        GridPane gridPanePlacingOn = submittingEachImage(newImage, String.valueOf(id));

        // Extracting ImageView objects for each position on the grid pane
        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        // Allowing picking on the corners for user interaction
        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        Node targetNode;

        targetNode = searchForNode(x, y-2); //check TL
        if (targetNode != null) {
            targetNode.setDisable(true);
            TL.setDisable(true);
        }
        targetNode = searchForNode(x+2, y-2);//check BL
        if (targetNode != null) {
            targetNode.setDisable(true);
            BL.setDisable(true);
        }
        targetNode = searchForNode(x+2, y);//check BR
        if (targetNode != null) {
            targetNode.setDisable(true);
            BR.setDisable(true);
        }

        // Adding the card and surrounding images to the board grid pane
        board.add(TL, (y - 2), x);
        board.add(TC, (y - 1), x);
        board.add(TR, y, x);
        board.add(LC, (y - 2), (x + 1));
        board.add(CC, (y - 1), (x + 1));
        board.add(RC, y, (x + 1));
        board.add(BL, (y - 2), (x + 2));
        board.add(BC, (y - 1), (x + 2));
        board.add(BR, y, (x + 2));

        // Creating CardView objects for each image and adding them to a list
        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        // Setting the index for each card in the cardIndices map
        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);

        // Disabling the bottom-right corner for the previously placed card
        TR.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (Objects.equals(cardIndices.get(cardView), indexCardToBePlacedOn) && cardView.getPosition().equals("BL")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }

    /**
     * This method places the card represented by the ImageView cardOnTheBoard in the top-left corner of the board.
     *
     * @param cardOnTheBoard The ImageView of the card to be placed on the board.
     * @param board The GridPane representing the board where the card will be placed.
     * @param id The identifier of the card.
     */
    public void placingTopLeftCard(ImageView cardOnTheBoard, GridPane board, int id) {
        // Incrementing the index for the next card
        nextCardIndex++;

        // Loading the image for the card
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);

        // Creating a grid pane for the new card and its surrounding images
        GridPane gridPanePlacingOn = submittingEachImage(newImage, String.valueOf(id));

        // Extracting ImageView objects for each position on the grid pane
        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        // Allowing picking on the corners for user interaction
        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        // Adding the card and surrounding images to the board grid pane

        Node targetNode;

        targetNode = searchForNode(x-2, y); //check TR
        if (targetNode != null) {
            targetNode.setDisable(true);
            TR.setDisable(true);
        }
        targetNode = searchForNode(x-2, y-2);//check TL
        if (targetNode != null) {
            targetNode.setDisable(true);
            TL.setDisable(true);
        }
        targetNode = searchForNode(x, y-2);//check BL
        if (targetNode != null) {
            targetNode.setDisable(true);
            BL.setDisable(true);
        }

        board.add(TL, (y - 2), (x - 2));
        board.add(TC, (y - 1), (x - 2));
        board.add(TR, y, (x - 2));
        board.add(LC, (y - 2), (x - 1));
        board.add(CC, (y - 1), (x - 1));
        board.add(RC, y, (x - 1));
        board.add(BL, (y - 2), x);
        board.add(BC, (y - 1), x);
        board.add(BR, y, x);

        // Creating CardView objects for each image and adding them to a list
        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        // Setting the index for each card in the cardIndices map
        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);

        // Disabling the bottom-right corner for the previously placed card
        BR.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (Objects.equals(cardIndices.get(cardView), indexCardToBePlacedOn) && cardView.getPosition().equals("TL")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }

    /**
     * This method places the card represented by the ImageView cardOnTheBoard in the top-right corner of the board.
     *
     * @param cardOnTheBoard The ImageView of the card to be placed on the board.
     * @param board The GridPane representing the board where the card will be placed.
     * @param id The identifier of the card.
     */
    public void placingTopRightCard(ImageView cardOnTheBoard, GridPane board, int id) {
        // Incrementing the index for the next card
        nextCardIndex++;

        // Loading the image for the card
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);

        // Creating a grid pane for the new card and its surrounding images
        GridPane gridPanePlacingOn = submittingEachImage(newImage, String.valueOf(id));

        // Extracting ImageView objects for each position on the grid pane
        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        // Allowing picking on the corners for user interaction
        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        Node targetNode;

        targetNode = searchForNode(x-2, y); //check TL
        if (targetNode != null) {
            targetNode.setDisable(true);
            TL.setDisable(true);
        }
        targetNode = searchForNode(x-2, y+2);//check TR
        if (targetNode != null) {
            targetNode.setDisable(true);
            TR.setDisable(true);
        }
        targetNode = searchForNode(x, y+2);//check BR
        if (targetNode != null) {
            targetNode.setDisable(true);
            BR.setDisable(true);
        }

        // Adding the card and surrounding images to the board grid pane
        board.add(TL, y, (x - 2));
        board.add(TC, (y + 1), (x - 2));
        board.add(TR, (y + 2), (x - 2));
        board.add(LC, y, (x - 1));
        board.add(CC, (y + 1), (x - 1));
        board.add(RC, (y + 2), (x - 1));
        board.add(BL, y, x);
        board.add(BC, (y + 1), x);
        board.add(BR, (y + 2), x);


        // Creating CardView objects for each image and adding them to a list
        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        // Setting the index for each card in the cardIndices map
        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);

        // Disabling the bottom-left corner for the previously placed card
        BL.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (Objects.equals(cardIndices.get(cardView), indexCardToBePlacedOn) && cardView.getPosition().equals("TR")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }


    public CardView getClickedImageView() {
        return clickedCardView;
    }

    /**
     * Retrieves the identifier of the first well card from the server and updates it in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void firstWellCard() throws IOException {
        out.println("firstWellId");
        SharedObjectsInGui.setIdCard1(in.readLine());
    }

    /**
     * Retrieves the identifier of the second well card from the server and updates it in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void secondWellCard() throws IOException {
        out.println("secondWellId");
        SharedObjectsInGui.setIdCard2(in.readLine());
    }

    /**
     * Retrieves the identifier of the third well card from the server and updates it in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        SharedObjectsInGui.setIdCard3(in.readLine());
    }

    /**
     * Retrieves the identifier of the fourth well card from the server and updates it in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        SharedObjectsInGui.setIdCard4(in.readLine());
    }

    /**
     * Retrieves the paths for the first resource and gold cards from the server and updates them in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void updatingResourceAndGoldDeck() throws IOException {
        out.println("firstCardResourceGui");
        SharedObjectsInGui.setPathResourceDeck(in.readLine());
        out.println("firstCardGoldGui");
        SharedObjectsInGui.setPathGoldDeck(in.readLine());
    }

    /**
     * Sets the paths for each well card in the GUI.
     */
    private void checkTypeWellCards() {
        SharedObjectsInGui.setWellPathOne(createPathForFrontCards(SharedObjectsInGui.getIdCard1()));
        SharedObjectsInGui.setWellPathSecond(createPathForFrontCards(SharedObjectsInGui.getIdCard2()));
        SharedObjectsInGui.setWellPathThird(createPathForFrontCards(SharedObjectsInGui.getIdCard3()));
        SharedObjectsInGui.setWellPathForth(createPathForFrontCards(SharedObjectsInGui.getIdCard4()));
    }



    /**
     * Retrieves the identifiers of the player's deck cards from the server and updates them in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void playerDeck() throws IOException {
        out.println("deckId");
        idHandCard1 = in.readLine();
        idHandCard2 = in.readLine();
        idHandCard3 = in.readLine();

    }

    /**
     * Retrieves the type (Resource or Gold) of each player's deck card from the server and updates them in the GUI.
     * Synchronized to ensure thread safety.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void checkTypePlayerDeck() throws IOException {
        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);
        in.readLine();
    }

    /**
     * Determines the type (Resource or Gold) of a card based on its identifier.
     *
     * @param id The identifier of the card.
     * @return The type of the card.
     */
    private String checkType(String id) {
        int intId = Integer.parseInt(id);
        return intId > 0 && intId <= 40 ? "Resource" : "Gold";
    }

    /**
     * Updates the paths for the resource and gold deck cards in the GUI to display the card backs.
     */
    private void creatingPathForGameMethod() {
        SharedObjectsInGui.setPathResourceDeck(createPathForBackCards(SharedObjectsInGui.getPathResourceDeck()));
        SharedObjectsInGui.setPathGoldDeck(createPathForBackCards(SharedObjectsInGui.getPathGoldDeck()));
    }

    /**
     * Creates a new Image object from the provided path.
     *
     * @param path The path of the image.
     * @return The Image object.
     */
    private Image createNewPathForImages(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Sets the fit width and height for the provided ImageView.
     *
     * @param imageView The ImageView to set width and height.
     */
    private void setWidthAndHeight(ImageView imageView) {
        imageView.setFitWidth(widthWellCards);
        imageView.setFitHeight(heightWellCards);
    }

    /**
     * Creates ImageView objects for the top resource and gold cards in the deck, sets their dimensions, and updates them in the GUI.
     */
    private void creatingDeckAndGoldDeckView() {
        SharedObjectsInGui.setTopCardResourceDeckView(new ImageView(SharedObjectsInGui.getTopCardResourceDeck()));
        SharedObjectsInGui.setTopCardGoldDeckView(new ImageView(SharedObjectsInGui.getTopCardGoldDeck()));
        setWidthAndHeight(SharedObjectsInGui.getTopCardResourceDeckView());
        setWidthAndHeight(SharedObjectsInGui.getTopCardGoldDeckView());
    }

    /**
     * Sets the onMouseClicked event handlers for the top resource and gold deck cards.
     * Displays an alert if it's not the current player's turn.
     */
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

    /**
     * Creates Image objects for the well cards and updates them in the GUI.
     */
    private void creatingImagesForTheWell() {
        SharedObjectsInGui.setWellCard1(createNewPathForImages(SharedObjectsInGui.getWellPathOne()));
        SharedObjectsInGui.setWellCard2(createNewPathForImages(SharedObjectsInGui.getWellPathSecond()));
        SharedObjectsInGui.setWellCard3(createNewPathForImages(SharedObjectsInGui.getWellPathThird()));
        SharedObjectsInGui.setWellCard4(createNewPathForImages(SharedObjectsInGui.getWellPathForth()));
    }

    /**
     * Creates ImageView objects for the well cards, sets their dimensions, and updates them in the GUI.
     */
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

    /**
     * Sets the onMouseClicked event handlers for the well cards.
     * Displays an alert if it's not the current player's turn.
     */
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

    }

    /**
     * Initializes the well by obtaining cards for each well position and checking their types.
     *
     * @throws IOException If an I/O error occurs.
     */
    private synchronized void initializeWell() throws IOException {
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
    }

    /**
     * Updates the top card of the resource deck with a new card obtained from the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void updateResourceDeckTopCard() throws IOException {
        out.println("firstCardResourceGui");
        String newTopCardResourceDeckId = in.readLine();
        idTopCardResourceDeck = newTopCardResourceDeckId;
        String newPathResource = "/ImmaginiCodex/CarteBack/Resource/" + newTopCardResourceDeckId + ".png";
        Image newTopCardResourceDeckImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPathResource)));
        SharedObjectsInGui.getTopCardResourceDeckView().setImage(newTopCardResourceDeckImage);
        SharedObjectsInGui.setPathResourceDeck(newPathResource);
        SharedObjectsInGui.setTopCardResourceDeck(newTopCardResourceDeckImage);
    }

    /**
     * Updates the top card of the gold deck with a new card obtained from the server.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void updatedGoldDeckTopCard() throws IOException {
        out.println("firstCardGoldGui");
        String newTopCardGoldDeckId = in.readLine();
        idTopCardGoldDeck = newTopCardGoldDeckId;
        String newPathGold = "/ImmaginiCodex/CarteBack/Gold/" + newTopCardGoldDeckId + ".png";
        Image newTopCardGoldImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPathGold)));
        SharedObjectsInGui.getTopCardGoldDeckView().setImage(newTopCardGoldImage);
        SharedObjectsInGui.setPathGoldDeck(newPathGold);
        SharedObjectsInGui.setTopCardGoldDeck(newTopCardGoldImage);
    }

    private void check20Points() throws IOException {
        clientView.setPlayerScore(controller.getPoints());
        int playerScore = clientView.getPlayerScore();
        System.out.println("player score: " + playerScore);
    }


    public int getX(ImageView img) {
        return GridPane.getRowIndex(img);
    }

    public int getY(ImageView img) {
        return GridPane.getColumnIndex(img);
    }

    /**
     * Creates buttons and adds them to the button container.
     * Sets styles and preferred width for each button.
     */
    private void creatingButtons() {
        buttonContainer.add(playCard, 0, 0);
        buttonContainer.add(drawCard, 1, 0);
        buttonContainer.add(flipCardToFront, 0, 1);
        buttonContainer.add(flipCardToBack, 1, 1);
        buttonContainer.add(seeYourPoints, 0, 2);
        buttonContainer.add(showAllPoints, 1, 2);
        buttonContainer.add(showObjective, 0, 3);
        buttonContainer.add(seeYourSpecificSeeds, 1, 3);
        buttonContainer.add(showAllBoards, 0, 4);
        buttonContainer.add(chat, 1, 4);
        buttonContainer.add(quit, 0, 5);
        buttonContainer.add(endTurn, 1, 5);
        double buttonsWidth = 120.00;
        for (var node : buttonContainer.getChildren()) {
            if (node instanceof Button) {
                node.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;");
                ((Button) node).setPrefWidth(buttonsWidth);
            }
        }
    }

    public void setClickedCardView(CardView cardView) {
        GameSceneController.clickedCardView = cardView;
    }


    /**
     * Updates the GUI by loading data, updating card IDs and paths, and refreshing the views.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void updateGUI() throws IOException {
        load();
        initializeWell();
        newPathAfterUpdate();
        updateResourceDeckTopCard();
        updatedGoldDeckTopCard();
        haveToDraw = true;
        handCard1View.setId("Front");
        handCard2View.setId("Front");
        handCard3View.setId("Front");
        cornerSelected = null;
        indexCardToPlace = 100;
        playerDeck();
        checkTypePlayerDeck();
        pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
        pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
        pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";
        handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
        handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
        handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));
        handCard1View.setImage(handCard1);
        handCard2View.setImage(handCard2);
        handCard3View.setImage(handCard3);
    }

    /**
     * Updates the paths and images for the well cards after data update.
     */
    private void newPathAfterUpdate() {
        String Path1 = "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard1() + ".png";
        SharedObjectsInGui.setWellCard1(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path1))));
        SharedObjectsInGui.getWellCard1View().setImage(SharedObjectsInGui.getWellCard1());

        String Path2 = "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard2() + ".png";
        SharedObjectsInGui.setWellCard2(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path2))));
        SharedObjectsInGui.getWellCard2View().setImage(SharedObjectsInGui.getWellCard2());

        String Path3 = "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard3() + ".png";
        SharedObjectsInGui.setWellCard3(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path3))));
        SharedObjectsInGui.getWellCard3View().setImage(SharedObjectsInGui.getWellCard3());

        String Path4 = "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard4() + ".png";
        SharedObjectsInGui.setWellCard4(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path4))));
        SharedObjectsInGui.getWellCard4View().setImage(SharedObjectsInGui.getWellCard4());
    }

    /**
     * Waits until the last message from the server before starting the GUI.
     * If a quit message is received, it initiates the quit process.
     *
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    private void waitUntilLastMessage() throws IOException {
        String messageFromServer = in.readLine();
        while (!messageFromServer.equals("STARTGUI")) {
            messageFromServer = in.readLine();
            if (messageFromServer.equals("One client decided to quit, so the game will end for every player.")) {
                try {
                    controller.quit(primaryStage);
                    socket.close();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Saves the elements in the GUI to a JSON file.
     *
     * @param path The path where the JSON file will be saved.
     */
    public void saveElementsInGui(Path path) {
        JsonObject sharedElementsObject = new JsonObject();
        sharedElementsObject.add("idTopResourceCardDeck", new JsonPrimitive(idTopCardResourceDeck));
        sharedElementsObject.add("idTopGoldCardDeck", new JsonPrimitive(idTopCardGoldDeck));
        sharedElementsObject.add("idCard1", new JsonPrimitive(SharedObjectsInGui.getIdCard1()));
        sharedElementsObject.add("idCard2", new JsonPrimitive(SharedObjectsInGui.getIdCard2()));
        sharedElementsObject.add("idCard3", new JsonPrimitive(SharedObjectsInGui.getIdCard3()));
        sharedElementsObject.add("idCard4", new JsonPrimitive(SharedObjectsInGui.getIdCard4()));
        try (FileWriter file = new FileWriter(path.toFile())) {
            Gson gson = new Gson();
            gson.toJson(sharedElementsObject, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the elements from the GUI from a JSON file.
     *
     * @param path The path of the JSON file to load from.
     */
    public void loadElementsFromGui(Path path) {
        try (FileReader reader = new FileReader(path.toFile())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            idTopCardResourceDeck = jsonObject.get("idTopResourceCardDeck").getAsString();
            idTopCardGoldDeck = jsonObject.get("idTopGoldCardDeck").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        loadElementsFromGui(getDefaultGuiPath());
    }
    public void savePath(){
        saveElementsInGui(getDefaultGuiPath());
    }
    private Path getDefaultGuiPath() {
        String home = ("src/main/resources/sharedElementsInGui.json");
        return Paths.get(home);
    }
    private void canIPlaceTheCardOrIsThereANotToBePlacedOn(ImageView cardOnTheBoard) {
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);

        if (cornerSelected!=null &&cornerSelected.equals("BR")) {
            int newX = x; //TR
            int newY = y+2;
            Node targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x+2; //BR
            newY=y+2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x+2; //BL
            newY=y;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
        }
        if(cornerSelected!=null && cornerSelected.equals("TR")){
            int newX = x - 2; //TL
            int newY = y;
            Node targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x-2; //TR
            newY=y+2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x; //BR
            newY=y+2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
        }
        if(cornerSelected!=null &&cornerSelected.equals("TL")){
            int newX = x - 2; //TR
            int newY = y;
            Node targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x-2; //TL
            newY=y-2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
            newX=x; //BL
            newY=y-2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            if(isNotToBePlacedon) return;
        }
        if(cornerSelected!=null && cornerSelected.equals("BL")){
            int newX = x; //TL
            int newY = y-2;
            Node targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            newX=x+2; //BL
            newY=y-2;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
            newX=x+2; //BR
            newY=y;
            targetNode = searchForNode(newX, newY);
            checkIfTheNodeIsPlaceable(targetNode);
        }
    }

    private Node searchForNode(int x,int y)
    {
        Node targetNode = null;
        for (Node node : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                targetNode = node;
                break;
            }
        }
        return  targetNode;
    }
    private void  checkIfTheNodeIsPlaceable(Node targetNode){
        if (targetNode == null) {
            isNotToBePlacedon =false;
        } else {
            if (targetNode instanceof ImageView targetImageView) {
                Image targetImage = targetImageView.getImage();
                isNotToBePlacedon = targetImage != null && !containsColor(targetImage, targetColor);

            }
        }
    }
    private void recreatingPath(){
        String newPath1 = "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard1() + ".png";
        Image newImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath1)));
        SharedObjectsInGui.getWellCard1View().setImage(newImage1);
        String newPath2 = "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard2() + ".png";
        Image newImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath2)));
        SharedObjectsInGui.getWellCard2View().setImage(newImage2);
        String newPath3 = "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard3() + ".png";
        Image newImage3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath3)));
        SharedObjectsInGui.getWellCard3View().setImage(newImage3);
        String newPath4 = "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard4() + ".png";
        Image newImage4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath4)));
        SharedObjectsInGui.getWellCard4View().setImage(newImage4);
    }

}
