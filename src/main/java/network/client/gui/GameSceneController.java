package network.client.gui;

import com.google.gson.*;
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
import javafx.stage.Stage;
import view.ClientView;

import java.io.*;
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
    private Button seeOtherPlayersBoards = new Button("See other players boards");
    private Button seeYourPoints = new Button("See your points");
    Button showObjective = new Button("Show Objective");
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
    private int nextCardIndex = 0; //
    private String pathHandCard1;
    private String pathHandCard2;
    private String pathHandCard3;
    private String pathChosen;
    private GridPane gridPaneForWellCards;
    String firstCommonId;
    String secondCommonId;
    ShowObjectiveScene objectiveScene;
    private boolean cardOnHerBack=false;
    private String pathFlipped;
    private Image initCardImage;
    private ImageView handCard1View;
    private ImageView handCard2View;
    private ImageView handCard3View;
    private Image handCard1;
    private Image handCard2;
    private Image handCard3;


    public void initData(Stage primaryStage, PrintWriter out, Socket socket, BufferedReader in, ClientView clientView, String currentPlayerNickname) throws IOException {
        this.primaryStage = primaryStage;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.in = in;
        this.currentPlayerNickname = currentPlayerNickname;
        System.out.println("Current player in initData is:" + currentPlayerNickname);
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

    public void startGame(String initCardId, int isFront) throws IOException {
        Image backGroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundImage backgroundImage = new BackgroundImage(backGroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
        pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
        pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";

        handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
        handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
        handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));

        handCard1View = new ImageView(handCard1);
        setWidthAndHeight(handCard1View);

        handCard2View = new ImageView(handCard2);
        handCard2View.setFitWidth(widthWellCards);
        handCard2View.setFitHeight(heightWellCards);

        handCard3View = new ImageView(handCard3);
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
        if(isFront==1)
        {
          initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Init/" + initCardId + ".png")));
        }
        else {
            initCardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteBack/Init/" + initCardId + ".png")));
        }

        GridPane initCardDividedInNineRegions = subnettingEachImage(initCardImage, initCardId);

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

        nextCardIndex++; //1 for initial card
        //All the index of the same card has the same value
        cardIndices.put(tlCardView, nextCardIndex);
        System.out.println("Added TL CardView with index: " + nextCardIndex);
        cardIndices.put(tcCardView, nextCardIndex);
        System.out.println("Added TC CardView with index: " + nextCardIndex);
        cardIndices.put(trCardView, nextCardIndex);
        System.out.println("Added TR CardView with index: " + nextCardIndex);
        cardIndices.put(lcCardView, nextCardIndex);
        System.out.println("Added LC CardView with index: " + nextCardIndex);
        cardIndices.put(ccCardView, nextCardIndex);
        System.out.println("Added CC CardView with index: " + nextCardIndex);
        cardIndices.put(rcCardView, nextCardIndex);
        System.out.println("Added RC CardView with index: " + nextCardIndex);
        cardIndices.put(blCardView, nextCardIndex);
        System.out.println("Added BL CardView with index: " + nextCardIndex);
        cardIndices.put(bcCardView, nextCardIndex);
        System.out.println("Added BC CardView with index: " + nextCardIndex);
        cardIndices.put(brCardView, nextCardIndex);
        System.out.println("Added BR CardView with index: " + nextCardIndex);


        tlImageView.setPickOnBounds(true);
        trImageView.setPickOnBounds(true);
        blImageView.setPickOnBounds(true);
        brImageView.setPickOnBounds(true);

        gameBoard.add(tlImageView, boardDimension / 3, boardDimension / 3);
        gameBoard.add(tcImageView, boardDimension / 3+1, boardDimension / 3);
        gameBoard.add(trImageView, boardDimension / 3+2, boardDimension / 3);
        gameBoard.add(lcImageView, boardDimension / 3, boardDimension / 3+1);
        gameBoard.add(ccImageView, boardDimension / 3+1, boardDimension / 3+1);
        gameBoard.add(rcImageView, boardDimension / 3+2, boardDimension / 3+1);
        gameBoard.add(blImageView, boardDimension / 3, boardDimension / 3+2);
        gameBoard.add(bcImageView, boardDimension / 3+1, boardDimension / 3+2);
        gameBoard.add(brImageView, boardDimension / 3+2, boardDimension / 3+2);

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

        buttonContainer.setHgap(2);
        buttonContainer.setVgap(2);
        creatingButtons();
        vboxGame.getChildren().addAll(gridPaneForWellCards, decks, decksText, buttonContainer);
        buttonContainer.setDisable(true);

        VBox secondColumnOfSecondRow = new VBox();

        chosenCardToPlace.setStyle("-fx-text-fill: white;");
        chosenCardToBePlacedOn.setStyle("-fx-text-fill: white;");
        chosenCorner.setStyle("-fx-text-fill: white;");
        chosenDeckOrWell.setStyle("-fx-text-fill: white;");

        secondColumnOfSecondRow.getChildren().addAll(chosenCardToPlace, chosenCardToBePlacedOn, chosenCorner, chosenDeckOrWell);
        chosenDeckOrWell.setText("Drawing from: " + wellOrDeck);

        layout.setRight(vboxGame);
        firstColumnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColumnOfSecondRow, secondColumnOfSecondRow);
        layout.setBottom(secondRow);
        root.getChildren().add(layout);
        Scene gameScene = new Scene(root, 430, 625);

        Platform.runLater(() -> primaryStage.setScene(gameScene));

        if (currentPlayerNickname.equals(clientView.getUserName())) {
            isCurrentPlayerTurn = true;
            waitUntilLastMessage();
            setupGameActions();
            buttonContainer.setDisable(false);
        } else {
            isCurrentPlayerTurn = false;
            waitForTurn(handCard1View, handCard2View, handCard3View);
        }

    }

    private void handleCardClick(CardView cardView) {
        if (isCurrentPlayerTurn) {
            setClickedCardView(cardView);
            cornerSelected = cardView.getPosition();
            Integer cardIndex = cardIndices.get(cardView);
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

    private void setupGameActions() throws IOException {
        playCard.setOnAction(e -> {
            if (isCurrentPlayerTurn) {
                if (haveToPlay) {
                    if(cornerSelected==null)
                    {
                        showAlert("Corner Missing","Please select a corner beforing playing a card");
                    }
                    if (!cornerSelected.equals("notSelected") && indexCardToPlace < 4) {
                        try {
                            String isTheCardFlipped=null;
                            String actualId=null;
                            switch (indexCardToPlace) {
                                case 0:

                                    handCard1View.setImage(null);
                                    actualId=idHandCard1;
                                    isTheCardFlipped=handCard1View.getId();
                                    break;
                                case 1:

                                    handCard2View.setImage(null);
                                    actualId=idHandCard2;
                                    isTheCardFlipped=handCard2View.getId();
                                    break;
                                case 2:

                                    handCard3View.setImage(null);
                                    actualId=idHandCard3;
                                    isTheCardFlipped=handCard3View.getId();
                                    break;
                                default:
                                    System.out.println("sono nullo");
                                    break;
                            }
                            System.out.println("Indice della carta che voglio piazzare è: " +indexCardToPlace);
                            controller.playCardClick(indexCardToBePlacedOn, indexCardToPlace, cornerSelected,isTheCardFlipped);
                            indexCardPlayedFromHand = indexCardToPlace;
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

                            haveToDraw = true;
                            handCard1View.setId("Front");
                            handCard2View.setId("Front");
                            handCard3View.setId("Front");
                            cornerSelected=null;
                            indexCardToPlace=100;
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

        endTurn.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                if(!haveToDraw && !haveToPlay){
                    try {
                        savePath();
                        String nextPlayerNickname = controller.endTurn();
                        updateTurnState(nextPlayerNickname.equals(clientView.getUserName()));
                        haveToPlay = true;
                        waitForTurn(handCard1View, handCard2View, handCard3View);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if(haveToDraw){
                    showAlert("HavetoDraw", "You have to draw a card first");
                }
                else{
                    showAlert("HavetoPlay", "You have to place a card first");
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
                            System.out.println("idHandCard" + (indexCardPlayedFromHand + 1) + " = " + idTopCard);

                            initializeWell(); //firtwellid,secondwellid,....
                            String newPath1= "/ImmaginiCodex/CarteFront/Resource/"+ SharedObjectsInGui.getIdCard1()+".png";
                            Image newImage1= new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath1)));
                            SharedObjectsInGui.getWellCard1View().setImage(newImage1);
                            String newPath2= "/ImmaginiCodex/CarteFront/Resource/"+ SharedObjectsInGui.getIdCard2()+".png";
                            Image newImage2= new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath2)));
                            SharedObjectsInGui.getWellCard2View().setImage(newImage2);
                            String newPath3= "/ImmaginiCodex/CarteFront/Gold/"+ SharedObjectsInGui.getIdCard3()+".png";
                            Image newImage3= new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath3)));
                            SharedObjectsInGui.getWellCard3View().setImage(newImage3);
                            String newPath4= "/ImmaginiCodex/CarteFront/Gold/"+ SharedObjectsInGui.getIdCard4()+".png";
                            Image newImage4= new Image(Objects.requireNonNull(getClass().getResourceAsStream(newPath4)));
                            SharedObjectsInGui.getWellCard4View().setImage(newImage4);
                            updateResourceDeckTopCard();
                            updatedGoldDeckTopCard();
                            haveToDraw = false;
                        } else {
                            showAlert("Action not allowed", "Yoh have to choose a card to draw");
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
                        handCard1View.setId("Back");
                        break;
                    case 1:
                        handCard2View.setImage(flipToBackCard(idHandCard2));
                        handCard2View.setId("Back");
                        break;
                    case 2:
                        handCard3View.setImage(flipToBackCard(idHandCard3));
                        handCard3View.setId("Back");
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
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard1View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 0;
                chosenCardToPlace.setText("First card of your hand");

                if(handCard1View.getId()==null|| handCard1View.getId().equals("Front"))
                {
                    pathChosen = pathHandCard1;
                    System.out.println("Front");
                }
                else{
                    pathChosen=pathFlipped;
                    System.out.println(pathChosen);
                    System.out.println("Back");
                }

            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard2View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 1;
                chosenCardToPlace.setText("Second card of your hand");
                pathChosen = pathHandCard2;
                if(handCard2View.getId()==null|| handCard2View.getId().equals("Front"))
                {
                    pathChosen = pathHandCard1;
                    System.out.println("Front");
                }
                else{
                    pathChosen=pathFlipped;
                    System.out.println(pathChosen);
                    System.out.println("Back");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        handCard3View.setOnMouseClicked(event -> {
            if (isCurrentPlayerTurn) {
                indexCardToPlace = 2;
                chosenCardToPlace.setText("Third card of your hand");
                pathChosen = pathHandCard3;
                if(handCard3View.getId()==null|| handCard3View.getId().equals("Front"))
                {
                    pathChosen = pathHandCard1;
                    System.out.println("Front");
                }
                else{
                    pathChosen=pathFlipped;
                    System.out.println(pathChosen);
                    System.out.println("Back");
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        seeYourSpecificSeeds.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    String yourSeeds = controller.showSpecificSeed();
                    showAlert("Your seeds at the moment", yourSeeds);
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
                    boardPointsScene = new BoardPointsScene(primaryStage, out, socket, in, clientView);
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

        showObjective.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                try {
                    objectiveScene = new ShowObjectiveScene(primaryStage, out, socket, in);
                    firstCommonId = controller.firstCommon();
                    secondCommonId = controller.secondCommon();
                    objectiveScene.popupObjectiveScene(firstCommonId, secondCommonId);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showAlert("Not your turn", "It's not your turn yet.");
            }
        });

        quit.setOnMouseClicked(e -> {
            if (isCurrentPlayerTurn) {
                if (haveToDraw) {
                    showAlert("Quit", "You can't quit right now, draw any card.");
                } else {
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
                        System.out.println("In Gamescenecontroller");
                        System.out.println(in.readLine()); //Fine turno
                        updateGUI();
                        setupGameActions();


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
            pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        } else if (id > 40 && id <= 80) {
            pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        }
        return null;
    }

    private Image flipCardToFront(String stringId) {
        int id = Integer.parseInt(stringId);
        if (id >= 1 && id <= 40) {
            pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        } else if (id > 40 && id <= 80) {
             pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
        }
        return null;
    }

    public GridPane subnettingEachImage(Image image, String cardId) {
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

        imageViewTL.setFitWidth(100);
        imageViewTL.setPreserveRatio(true);
        imageViewTC.setFitWidth(100);
        imageViewTC.setPreserveRatio(true);
        imageViewTR.setFitWidth(100);
        imageViewTR.setPreserveRatio(true);
        imageViewLC.setFitWidth(100);
        imageViewLC.setPreserveRatio(true);
        imageViewCC.setFitWidth(100);
        imageViewCC.setPreserveRatio(true);
        imageViewRC.setFitWidth(100);
        imageViewRC.setPreserveRatio(true);
        imageViewBL.setFitWidth(100);
        imageViewBL.setPreserveRatio(true);
        imageViewBC.setFitWidth(100);
        imageViewBC.setPreserveRatio(true);
        imageViewBR.setFitWidth(100);
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

        imageViewTL.setOnMouseClicked(event -> handleCardClick(tlCardView));
        imageViewTR.setOnMouseClicked(event -> handleCardClick(trCardView));
        imageViewBL.setOnMouseClicked(event -> handleCardClick(blCardView));
        imageViewBR.setOnMouseClicked(event -> handleCardClick(brCardView));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.add(imageViewTL, 0, 0);
        gridPane.add(imageViewTC, 1, 0);
        gridPane.add(imageViewTR, 2, 0);
        gridPane.add(imageViewLC, 0,1);
        gridPane.add(imageViewCC, 1,1);
        gridPane.add(imageViewRC, 2, 1);
        gridPane.add(imageViewBL, 0, 2);
        gridPane.add(imageViewBC, 1, 2);
        gridPane.add(imageViewBR, 2, 2);

        return gridPane;
    }

    public void placingBottomRightCard(ImageView cardOnTheBoard, GridPane board, int id) {
        int lastIndex=nextCardIndex;
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));

        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        board.add(TL, y, x);
        board.add(TC, (y + 1), x);
        board.add(TR, (y + 2), x);

        board.add(LC, y, (x + 1));
        board.add(CC, (y + 1), (x + 1));
        board.add(RC, (y + 2), (x + 1));

        board.add(BL, y, (x + 2));
        board.add(BC, (y + 1), (x + 2));
        board.add(BR, (y + 2), (x + 2));

        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);
        TL.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (cardIndices.get(cardView) == lastIndex && cardView.getPosition().equals("BR")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }

    }

    public void placingBottomLeftCard(ImageView cardOnTheBoard, GridPane board, int id) {
        int lastIndex=nextCardIndex;
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));

        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        board.add(TL, (y - 2), x);
        board.add(TC, (y - 1), x);
        board.add(TR, y, x);

        board.add(LC, (y - 2), (x + 1));
        board.add(CC, (y - 1), (x + 1));
        board.add(RC, y, (x + 1));

        board.add(BL, (y - 2), (x + 2));
        board.add(BC, (y - 1), (x + 2));
        board.add(BR, y, (x + 2));

        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);
        TR.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (cardIndices.get(cardView) == lastIndex && cardView.getPosition().equals("BL")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }

    public void placingTopLeftCard(ImageView cardOnTheBoard, GridPane board, int id) {
        int lastIndex=nextCardIndex;
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));

        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        board.add(TL, (y - 2), (x - 2));
        board.add(TC, (y - 1), (x - 2));
        board.add(TR, y, (x - 2));

        board.add(LC, (y - 2), (x - 1));
        board.add(CC, (y - 1), (x - 1));
        board.add(RC, y, (x - 1));

        board.add(BL, (y - 2), x);
        board.add(BC, (y - 1), x);
        board.add(BR, y, x);

        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);
        BR.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (cardIndices.get(cardView) == lastIndex && cardView.getPosition().equals("TL")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }

    public void placingTopRightCard(ImageView cardOnTheBoard, GridPane board, int id) {
        int lastIndex=nextCardIndex;
        nextCardIndex++;
        Image newImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathChosen)));
        int x = getX(cardOnTheBoard);
        int y = getY(cardOnTheBoard);
        GridPane gridPanePlacingOn = subnettingEachImage(newImage, String.valueOf(id));
        ImageView TL = (ImageView) gridPanePlacingOn.getChildren().get(0);
        ImageView TC = (ImageView) gridPanePlacingOn.getChildren().get(1);
        ImageView TR = (ImageView) gridPanePlacingOn.getChildren().get(2);
        ImageView LC = (ImageView) gridPanePlacingOn.getChildren().get(3);
        ImageView CC = (ImageView) gridPanePlacingOn.getChildren().get(4);
        ImageView RC = (ImageView) gridPanePlacingOn.getChildren().get(5);
        ImageView BL = (ImageView) gridPanePlacingOn.getChildren().get(6);
        ImageView BC = (ImageView) gridPanePlacingOn.getChildren().get(7);
        ImageView BR = (ImageView) gridPanePlacingOn.getChildren().get(8);

        TL.setPickOnBounds(true);
        TR.setPickOnBounds(true);
        BL.setPickOnBounds(true);
        BR.setPickOnBounds(true);

        board.add(TL, y, (x - 2));
        board.add(TC, (y + 1), (x - 2));
        board.add(TR, (y + 2), (x - 2));

        board.add(LC, y, (x - 1));
        board.add(CC, (y + 1), (x - 1));
        board.add(RC, (y + 2), (x - 1));

        board.add(BL, y, x);
        board.add(BC, (y + 1), x);
        board.add(BR, (y + 2), x);

        allCardViews.add(new CardView(TL, String.valueOf(id), "TL"));
        allCardViews.add(new CardView(TC, String.valueOf(id), "TC"));
        allCardViews.add(new CardView(TR, String.valueOf(id), "TR"));
        allCardViews.add(new CardView(LC, String.valueOf(id), "LC"));
        allCardViews.add(new CardView(CC, String.valueOf(id), "CC"));
        allCardViews.add(new CardView(RC, String.valueOf(id), "RC"));
        allCardViews.add(new CardView(BL, String.valueOf(id), "BL"));
        allCardViews.add(new CardView(BC, String.valueOf(id), "BC"));
        allCardViews.add(new CardView(BR, String.valueOf(id), "BR"));

        cardIndices.put(new CardView(TL, String.valueOf(id), "TL"), nextCardIndex);
        cardIndices.put(new CardView(TC, String.valueOf(id), "TC"), nextCardIndex);
        cardIndices.put(new CardView(TR, String.valueOf(id), "TR"), nextCardIndex);
        cardIndices.put(new CardView(LC, String.valueOf(id), "LC"), nextCardIndex);
        cardIndices.put(new CardView(CC, String.valueOf(id), "CC"), nextCardIndex);
        cardIndices.put(new CardView(RC, String.valueOf(id), "RC"), nextCardIndex);
        cardIndices.put(new CardView(BL, String.valueOf(id), "BL"), nextCardIndex);
        cardIndices.put(new CardView(BC, String.valueOf(id), "BC"), nextCardIndex);
        cardIndices.put(new CardView(BR, String.valueOf(id), "BR"), nextCardIndex);
        BL.setDisable(true);
        for (CardView cardView : allCardViews) {
            if (cardIndices.get(cardView) == lastIndex && cardView.getPosition().equals("TR")) {
                cardView.getImageView().setDisable(true);
                break;
            }
        }
    }

    public CardView getClickedImageView() {
        return clickedCardView;
    }

    private synchronized void firstWellCard() throws IOException {
        out.println("firstWellId");
        SharedObjectsInGui.setIdCard1(in.readLine());
    }

    private synchronized void secondWellCard() throws IOException {
        out.println("secondWellId");
        SharedObjectsInGui.setIdCard2(in.readLine());
    }

    private synchronized void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        SharedObjectsInGui.setIdCard3(in.readLine());
    }

    private synchronized void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        SharedObjectsInGui.setIdCard4(in.readLine());
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
        System.out.println("first card of deck:" + idHandCard1);
        idHandCard2 = in.readLine();
        System.out.println("Second card of deck:" + idHandCard2);
        idHandCard3 = in.readLine();
        System.out.println("Third card of deck:" + idHandCard3);
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

    }

    private synchronized void initializeWell() throws IOException {
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
    }

    private void updateResourceDeckTopCard() throws IOException {
        out.println("firstCardResourceGui");
        String newTopCardResourceDeckId = in.readLine();
        idTopCardResourceDeck=newTopCardResourceDeckId;
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
        idTopCardGoldDeck=newTopCardGoldDeckId;
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

    private void creatingButtons() {
        buttonContainer.add(playCard, 0, 0);
        buttonContainer.add(drawCard, 1, 0);
        buttonContainer.add(flipCardToFront, 0, 1);
        buttonContainer.add(flipCardToBack, 1, 1);
        buttonContainer.add(seeYourPoints, 0, 2);
        buttonContainer.add(seeYourSpecificSeeds, 1, 2);
        buttonContainer.add(showObjective, 0, 3);
        buttonContainer.add(endTurn, 1, 3);
        buttonContainer.add(quit, 0, 4);
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


    private void updateGUI() throws IOException {
        load();
        System.out.println(SharedObjectsInGui.getIdCard1());
        System.out.println(SharedObjectsInGui.getIdCard2());
        System.out.println(SharedObjectsInGui.getIdCard3());
        System.out.println(SharedObjectsInGui.getIdCard4());
        System.out.println("Carta 1 del deck" + idHandCard1);
        System.out.println("Carta 2 del deck" + idHandCard2);
        System.out.println("Carta 3 del deck" + idHandCard3);
        newPathAfterUpdate();
        updateResourceDeckTopCard();
        updatedGoldDeckTopCard();
        haveToDraw = true;
        handCard1View.setId("Front");
        handCard2View.setId("Front");
        handCard3View.setId("Front");
        cornerSelected=null;
        indexCardToPlace=100;
        playerDeck();
        checkTypePlayerDeck();
        pathHandCard1 = "/ImmaginiCodex/CarteFront/" + typeHandCard1 + "/" + idHandCard1 + ".png";
        System.out.println(pathHandCard1);
        pathHandCard2 = "/ImmaginiCodex/CarteFront/" + typeHandCard2 + "/" + idHandCard2 + ".png";
        System.out.println(pathHandCard2);
        pathHandCard3 = "/ImmaginiCodex/CarteFront/" + typeHandCard3 + "/" + idHandCard3 + ".png";
        System.out.println(pathHandCard3);
        handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
        handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
        handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));
        handCard1View.setImage(handCard1);
        handCard2View.setImage(handCard2);
        handCard3View.setImage(handCard3);
    }

    private void newPathAfterUpdate(){
        String Path1= "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard1() +".png";
        SharedObjectsInGui.setWellCard1(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path1))));
        SharedObjectsInGui.getWellCard1View().setImage(SharedObjectsInGui.getWellCard1());
        System.out.println(Path1);

        String Path2= "/ImmaginiCodex/CarteFront/Resource/" + SharedObjectsInGui.getIdCard2() +".png";
        SharedObjectsInGui.setWellCard2(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path2))));
        SharedObjectsInGui.getWellCard2View().setImage(SharedObjectsInGui.getWellCard2());
        System.out.println(Path2);

        String Path3= "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard3() +".png";
        SharedObjectsInGui.setWellCard3(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path3))));
        SharedObjectsInGui.getWellCard3View().setImage(SharedObjectsInGui.getWellCard3());

        String Path4= "/ImmaginiCodex/CarteFront/Gold/" + SharedObjectsInGui.getIdCard4() +".png";
        SharedObjectsInGui.setWellCard4(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Path4))));
        SharedObjectsInGui.getWellCard4View().setImage(SharedObjectsInGui.getWellCard4());
    }

    private void waitUntilLastMessage() throws IOException {
        String messageFromServer = in.readLine();
        while (!messageFromServer.equals("STARTGUI")) {
            System.out.println("Server says " + messageFromServer);
            messageFromServer = in.readLine();
        }
        System.out.println("Game finally starting!");
    }

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
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Method to load SharedObjectVariables from json file in order to correctly update attributes
    * */

    public void loadElementsFromGui(Path path) {
        try (FileReader reader = new FileReader(path.toFile())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            idTopCardResourceDeck = jsonObject.get("idTopResourceCardDeck").getAsString();
            idTopCardGoldDeck = jsonObject.get("idTopGoldCardDeck").getAsString();
            SharedObjectsInGui.setIdCard1(jsonObject.get("idCard1").getAsString());
            SharedObjectsInGui.setIdCard2(jsonObject.get("idCard2").getAsString());
            SharedObjectsInGui.setIdCard3(jsonObject.get("idCard3").getAsString());
            SharedObjectsInGui.setIdCard4(jsonObject.get("idCard4").getAsString());
            System.out.println("File loaded successfully.");
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
    private boolean checkGoldCardRequirements(String goldCardId) throws IOException {
        // Retrieve the requirements for the specific gold card
        out.println("getGoldCardRequirements");
        //out.println(goldCardId);
        String requirements = in.readLine(); // Assuming the server sends requirements as a JSON string

        JsonObject requirementsJson = JsonParser.parseString(requirements).getAsJsonObject();

        // Retrieve the current attributes from the board
        out.println("getCurrentAttributes");
        String currentAttributes = in.readLine(); // Assuming the server sends current attributes as a JSON string

        JsonObject currentAttributesJson = JsonParser.parseString(currentAttributes).getAsJsonObject();

        // Check if all required attributes are met
        for (Map.Entry<String, JsonElement> entry : requirementsJson.entrySet()) {
            String attribute = entry.getKey();
            int requiredValue = entry.getValue().getAsInt();
            int currentValue = currentAttributesJson.has(attribute) ? currentAttributesJson.get(attribute).getAsInt() : 0;

            if (currentValue < requiredValue) {
                return false;
            }
        }
        return true;
    }

}
