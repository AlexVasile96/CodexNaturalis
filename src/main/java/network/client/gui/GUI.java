package network.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.game.Player;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class GUI extends Application {
    private int cardSelected;
    private Background background=null;
    private String pathResourceDeck=null;
    private String pathGoldDeck=null;
    private ImageView topCardResourceDeckView=null;
    private ImageView topCardGoldDeckView=null;
    private Image topCardResourceDeck=null;
    private Image topCardGoldDeck=null;
    private String wellPathOne =null;
    private String wellPathSecond = null;
    private String wellPathThird = null;
    private String wellPathForth = null;
    private Image wellCard1=null;
    private Image wellCard2=null;
    private Image wellCard3=null;
    private Image wellCard4=null;
    private ImageView wellCard1View=null;
    private ImageView wellCard2View=null;
    private ImageView wellCard3View=null;
    private ImageView wellCard4View=null;
    private Image handCard1=null;
    private Image handCard2=null;
    private Image handCard3=null;

    private ImageView handCard1View=null;
    private ImageView handCard2View=null;
    private ImageView handCard3View=null;
    private String currentPlayerNickname=null;
    private String idCard1=null;
    private String idCard2=null;
    private String idCard3=null;
    private String idCard4=null;
    private String typeCard1=null;
    private String typeCard2=null;
    private String typeCard3=null;
    private String typeCard4=null;

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

    private static BufferedReader in;

    private Scene gameScene;
    private Scene chooseInitCardScene;

    private String id;
    private ClientView clientView = new ClientView();
    private static Socket socket;
    private static PrintWriter out;
    private Image wellCardSelected = null;
    private String idWellCardSelected = null;
    private Image cardFlipped = null;
    private ClientView clientview= new ClientView();
    private Player currentPlayer=null;
    Controller controller = new Controller(in, out);



    @FXML
    public ImageView chosenObj;
    @FXML
    public ImageView obj1ImageView;
    @FXML
    public ImageView obj2ImageView;
    @FXML
    public Button closeButton;
    @FXML
    public ScrollPane gameBoard;

    private Image codexLogo = null;

    @FXML
    public ImageView CARTA;
    @FXML
    public ImageView initCard;
    @FXML
    public ImageView resourceCard;
    @FXML
    public ImageView goldCard;

    public static void main(String[] args) throws IOException {
        ConnectionWithServer connectionWithServer= new ConnectionWithServer(); //creazione classe
        socket= connectionWithServer.connectToServer();
        out=new PrintWriter(socket.getOutputStream(), true); //to write
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        launch(args);
    }

   @Override
   public void start(Stage primaryStage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/mainMenu.fxml"));
       Parent root = loader.load();
       MainMenuController controller = loader.getController();
       controller.initData(primaryStage, out, socket,in, clientview);
       Scene scene = new Scene(root, 919, 743);
       primaryStage.setScene(scene);
       primaryStage.setTitle("Codex");
       primaryStage.show();
       controller.startMenuScene(primaryStage);
   }


    private Image flipToBackCard(String stringId){
        int id = Integer.parseInt(stringId);
        if(id>=1 && id <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            cardFlipped= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            //resourceCard.setImage(cardFlipped);
        }
        else if(id>40 && id<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            cardFlipped= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            //goldCard.setImage(cardFlipped);
        }
        return cardFlipped;
    }
    private Image flipCardToFront(String stringId){
        int id = Integer.parseInt(stringId);
        if(id>=1 && id <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            cardFlipped= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            //resourceCard.setImage(resourceImage);
        }
        else if(id>40 && id<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            cardFlipped= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            //goldCard.setImage(goldImage);
        }
        return cardFlipped;
    }

   /* @FXML
    private void chooseInitCardFrontOrBack(ActionEvent event) throws IOException {

        out.println(isFront);
        //System.out.printf(""+isFront);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        update();
        game();
        Platform.runLater(() -> primaryStage.setScene(gameScene));
    }*/


  /*  private void update() throws IOException {
        currentPlayerNickname= in.readLine();
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
        playerDeck();
        checkTypePlayerDeck();
        updatingResourceAndGoldDeck();
        //System.out.println(in.readLine());

    }*/

    private int counter = 0;
    private void creaRegionPerNuovaCarta(ImageView cartaSelezionata){
        Region regionTopLeft = new Region();
        regionTopLeft.setPrefSize(widthWellCards, heightWellCards);
        regionTopLeft.setStyle("-fx-background-color: red;");
        regionTopLeft.setOnMouseClicked(e->{
            try {

                indexCardToBePlacedOn = (Integer) cartaSelezionata.getUserData();
                cartaSelezionata.setUserData(counter);

                cornerSelected = "TL";
                System.out.println("hai cliccato il " + cornerSelected + " della carta numero " + indexCardToBePlacedOn);
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });

        Region regionBottomLeft = new Region();
        regionBottomLeft.setPrefSize(widthWellCards, heightWellCards);
        regionBottomLeft.setStyle("-fx-background-color: yellow;");
        regionBottomLeft.setOnMouseClicked(e->{
            cartaSelezionata.setUserData(counter);

            indexCardToBePlacedOn = (Integer) cartaSelezionata.getUserData();
            cornerSelected = "BL";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionTopRight = new Region();
        regionTopRight.setPrefSize(widthWellCards, heightWellCards);
        regionTopRight.setStyle("-fx-background-color: green;");
        regionTopRight.setOnMouseClicked(e->{
            cartaSelezionata.setUserData(counter);
            indexCardToBePlacedOn = (Integer) cartaSelezionata.getUserData();
            cornerSelected = "TR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionBottomRight = new Region();
        regionBottomRight.setPrefSize(widthWellCards, heightWellCards);
        regionBottomRight.setStyle("-fx-background-color: blue;");
        regionBottomRight.setOnMouseClicked(e->{
            cartaSelezionata.setUserData(counter);
            indexCardToBePlacedOn = (Integer) cartaSelezionata.getUserData();
            cornerSelected = "BR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });
    }

    private void game() {

        creatingPathForGameMethod();

        topCardResourceDeck = createNewPathForImages(pathResourceDeck); //Resource Deck Back Image
        topCardGoldDeck = createNewPathForImages(pathGoldDeck);

        creatingDeckAndGoldDeckView();

        //Handling well creation
        creatingWell();
        creatingImagesForTheWell();
        creatingImagesViewForTheWell();
        settingWellOnMouseClickedEvent();
        settingDecksOnMouseClickedEvent();

        StackPane stackPaneInitCard = new StackPane();
        GridPane gridPaneInitCard = new GridPane();

        //end creation of well

        //Creating handCards
        String pathHandCard1 = "/ImmaginiCodex/CarteFront/"+typeHandCard1+"/"+idHandCard1+".png";
        String pathHandCard2 = "/ImmaginiCodex/CarteFront/"+typeHandCard2+"/"+idHandCard2+".png";
        String pathHandCard3 = "/ImmaginiCodex/CarteFront/"+typeHandCard3+"/"+idHandCard3+".png";

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

        //End creation of handCards


        //Creating subdivision of initCard
        StackPane.setAlignment(stackPaneInitCard, Pos.CENTER);
        StackPane.setMargin(stackPaneInitCard, new Insets(0, 0, 0, 0));




        Region regionTopLeft = new Region();
        regionTopLeft.setPrefSize(widthWellCards, heightWellCards);
        regionTopLeft.setStyle("-fx-background-color: red;");
        regionTopLeft.setOnMouseClicked(e->{
            try {
                out.println("WE che carta sono?");
                in.readLine();
                indexCardToBePlacedOn = (Integer) initCard.getUserData();
                initCard.setUserData(counter);

                cornerSelected = "TL";
                System.out.println("hai cliccato il " + cornerSelected + " della carta numero " + indexCardToBePlacedOn);
            }catch (Exception suca){
                suca.printStackTrace();
            }
        });

        Region regionBottomLeft = new Region();
        regionBottomLeft.setPrefSize(widthWellCards, heightWellCards);
        regionBottomLeft.setStyle("-fx-background-color: yellow;");
        regionBottomLeft.setOnMouseClicked(e->{
            initCard.setUserData(counter);

            indexCardToBePlacedOn = (Integer) initCard.getUserData();
            cornerSelected = "BL";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionTopRight = new Region();
        regionTopRight.setPrefSize(widthWellCards, heightWellCards);
        regionTopRight.setStyle("-fx-background-color: green;");
        regionTopRight.setOnMouseClicked(e->{
            initCard.setUserData(counter);
            indexCardToBePlacedOn = (Integer) initCard.getUserData();
            cornerSelected = "TR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionBottomRight = new Region();
        regionBottomRight.setPrefSize(widthWellCards, heightWellCards);
        regionBottomRight.setStyle("-fx-background-color: blue;");
        regionBottomRight.setOnMouseClicked(e->{
            initCard.setUserData(counter);
            cornerSelected = "BR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        gridPaneInitCard.add(regionTopLeft, 0, 0);
        gridPaneInitCard.add(regionTopRight, 1, 0);
        gridPaneInitCard.add(regionBottomLeft, 0, 1);
        gridPaneInitCard.add(regionBottomRight, 1, 1);

        stackPaneInitCard.getChildren().addAll(initCard, gridPaneInitCard);


        //if(clientView.getUserName().equals(currentPlayerNickname)){
        playCard.setOnAction(event -> {
            if(haveToPlay){
                try {
                    controller.playCardClick(indexCardToBePlacedOn, indexCardToPlace, cornerSelected);
                    indexCardPlayedFromHand = indexCardToPlace;
                    haveToDraw = true;
                    if(indexCardPlayedFromHand == 0){
                        handCard1View.setImage(codexLogo);
                    }
                    if(indexCardPlayedFromHand == 1){
                        handCard2View.setImage(codexLogo);
                    }
                    if(indexCardPlayedFromHand == 2){
                        handCard3View.setImage(codexLogo);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                haveToPlay = false;
            }
            else System.out.println("You already placed a card");
    });//}
        /*else{
            System.out.println("non è il tuo turno");
            return;
        }*/

        drawCard.setOnAction(e -> {
            if(haveToDraw){
                try {
                    controller.drawCard(wellOrDeck, chosenDeckForDrawingNewCard, indexCardFromWellSelected);

                    if(wellOrDeck.equals("deck")) {
                        if(chosenDeckForDrawingNewCard.equals("resource")) {
                            Image drownCardImage = createNewPathForImages(pathForResourceCardFront(idTopCardResourceDeck));
                            if (indexCardPlayedFromHand == 0) {
                                handCard1View.setImage(drownCardImage);
                                idHandCard1 = idTopCardResourceDeck;
                                System.out.println("idHandCard1 = " + idHandCard1);
                            }
                            if (indexCardPlayedFromHand == 1) {
                                handCard2View.setImage(drownCardImage);
                                idHandCard2 = idTopCardResourceDeck;
                                System.out.println("idHandCard2 = " + idHandCard2);
                            }
                            if (indexCardPlayedFromHand == 2) {
                                handCard3View.setImage(drownCardImage);
                                idHandCard3 = idTopCardResourceDeck;
                                System.out.println("idHandCard3 = " + idHandCard3);
                            }
                            haveToDraw =false;
                        }
                        if(chosenDeckForDrawingNewCard.equals("gold")) {
                            Image drownCardImage = createNewPathForImages(pathForGoldCardFront(idTopCardGoldDeck));
                            if (indexCardPlayedFromHand == 0) {
                                handCard1View.setImage(drownCardImage);
                                idHandCard1 = idTopCardGoldDeck;
                                System.out.println("idHandCard1 = " + idHandCard1);
                            }
                            if (indexCardPlayedFromHand == 1) {
                                handCard2View.setImage(drownCardImage);
                                idHandCard2 = idTopCardGoldDeck;
                                System.out.println("idHandCard2 = " + idHandCard2);
                            }
                            if (indexCardPlayedFromHand == 2) {
                                handCard3View.setImage(drownCardImage);
                                idHandCard3 = idTopCardGoldDeck;
                                System.out.println("idHandCard3 = " + idHandCard3);
                            }
                            haveToDraw =false;
                        }
                    }
                    if(wellOrDeck.equals("well")){
                        Image drownCardImage = wellCardSelected;
                        if (indexCardPlayedFromHand == 0) {
                            handCard1View.setImage(drownCardImage);
                            idHandCard1 = idWellCardSelected;
                            System.out.println("idHandCard1 = "+idHandCard1);
                        }
                        if (indexCardPlayedFromHand == 1) {
                            handCard2View.setImage(drownCardImage);
                            idHandCard2 = idWellCardSelected;
                            System.out.println("idHandCard2 = "+idHandCard2);
                        }
                        if (indexCardPlayedFromHand == 2) {
                            handCard3View.setImage(drownCardImage);
                            idHandCard3 = idWellCardSelected;
                            System.out.println("idHandCard3: "+idHandCard3);
                        }
                        haveToDraw =false;
                    }
                    else if(wellOrDeck.equals("notSelected")){
                        System.out.println("devi selezionare una carta da pescare");
                    }

                    //updateDecks(); //implementare metodo per aggiornare i deck una volta pescata la carta
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else System.out.println("You can't draw any card now");
        });

        flipCardToBack.setOnAction(e->{
            if(indexCardToPlace == 0){
                handCard1View.setImage(flipToBackCard(idHandCard1));
            }
            if(indexCardToPlace == 1){
                handCard2View.setImage(flipToBackCard(idHandCard2));
            }
            if(indexCardToPlace == 2){
                handCard3View.setImage(flipToBackCard(idHandCard3));
            }
            else{
                System.out.println("You chose an unflippable card");
            }
        });

        flipCardToFront.setOnAction(e->{
            if(indexCardToPlace == 0){
                handCard1View.setImage(flipCardToFront(idHandCard1));
            }
            if(indexCardToPlace == 1){
                handCard2View.setImage(flipCardToFront(idHandCard2));
            }
            if(indexCardToPlace == 2){
                handCard3View.setImage(flipCardToFront(idHandCard3));
            }
            else{
                System.out.println("You chose an unflippable card");
            }
        });


        handCard1View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard1);
            indexCardToPlace = 0;
            System.out.println("indexCardPlayedFromHand = "+indexCardToPlace);
        });
        handCard2View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard2);
            indexCardToPlace = 1;
            System.out.println("indexCardPlayedFromHand = "+indexCardToPlace);
        });
        handCard3View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard3);
            indexCardToPlace = 2;
            System.out.println("indexCardPlayedFromHand = "+indexCardToPlace);
        });

        seeYourSpecificSeeds.setOnMouseClicked(e->{
            try {
                String yourSeeds = controller.showSpecificSeed();
                System.out.println(yourSeeds);
                specificSeedsLabel.setText(yourSeeds);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        seeYourPoints.setOnMouseClicked(e->{
            try {
                String yourPoints = controller.showPoints();
                System.out.println("your points are: "+yourPoints);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        seeOtherPlayersBoards.setOnMouseClicked(e->{
            System.out.println("Per ora guardati la tua board");
        });

        endTurn.setOnMouseClicked(e->{
            try {
                controller.endTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        Pane root = new Pane();
        creatingBackground(root); //Creating backGround for root


        AnchorPane anchorPane = new AnchorPane();
        root.getChildren().add(anchorPane);

        VBox vboxContainer = new VBox();
        HBox hboxGame = new HBox();
        VBox vboxGame = new VBox();
        vboxGame.setFillWidth(true);



        //all'interno della scrollPane gameBoard c'è insideScrollPane che è un Pane
        //gameBoard è in una Hbox che è in una Vbox che è contenuto in Root
        ScrollPane gameBoard = new ScrollPane();                //Creating my scrollPane
        Pane insideScrollPane = new Pane();                     //Creating a pane i'll set inside my scrollpane
        insideScrollPane.setPrefWidth(window.getWidth()*0.8);   //Setting pane default width and height
        insideScrollPane.setPrefHeight(window.getHeight()*0.8);
        gameBoard.setPannable(true);
        gameBoard.setFitToWidth(true);
        gameBoard.setFitToHeight(true);
        gameBoard.setContent(insideScrollPane);
        gameBoard.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        gameBoard.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        gameBoard.pannableProperty().set(true);


        //padding between elements
        Insets padding = new Insets(10, 10, 10, 10);
        Insets paddingDecks = new Insets(100, 10, 10, 10);

        insideScrollPane.setBackground(background);
        HBox secondRow = new HBox();
        HBox firstColomnOfSecondRow = new HBox();
        firstColomnOfSecondRow.setPadding(padding);
        firstColomnOfSecondRow.setSpacing(4);
        //Pane paneForImages = new Pane();

        Label wellText = new Label("                    WELL");
        Label wellText2 = new Label("CARDS");
        wellText.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");
        wellText2.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14");

        insideScrollPane.getChildren().add(stackPaneInitCard);
        GridPane gridPaneForWellCards = new GridPane();
        gridPaneForWellCards.setVgap(4.0);
        gridPaneForWellCards.setHgap(4.0);
        gridPaneForWellCards.setPadding(padding);
        gridPaneForWellCards.add(wellCard1View, 0, 0);
        gridPaneForWellCards.add(wellCard2View, 0, 1);
        gridPaneForWellCards.add(wellCard3View, 1, 0);
        gridPaneForWellCards.add(wellCard4View, 1, 1);
        gridPaneForWellCards.add(wellText, 0,2);
        gridPaneForWellCards.add(wellText2, 1,2);



        HBox decks = new HBox();

        Label decksText = new Label("      Resource Deck               Gold Deck");
        decksText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        decks.setSpacing(4.0);
        decks.setPadding(paddingDecks);
        decks.getChildren().addAll(topCardResourceDeckView, topCardGoldDeckView);

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



        vboxGame.getChildren().addAll(gridPaneForWellCards, decks,decksText, specificSeedsPane, buttonContainer);
        hboxGame.getChildren().addAll(gameBoard, vboxGame);
        firstColomnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColomnOfSecondRow);
        vboxContainer.getChildren().addAll(hboxGame, secondRow);
        anchorPane.getChildren().add(vboxContainer);
        gameScene = new Scene(root, 600, 400);

        window.widthProperty().addListener((observable, oldValue, newValue) -> insideScrollPane.setPrefWidth(window.getWidth()*0.8));
        window.heightProperty().addListener((observable, oldValue, newValue) -> insideScrollPane.setPrefHeight(window.getHeight()*0.8));

    }




    @FXML
    private void showObjCardsClicked() throws IOException {
        // Create an HBox to hold the images of the objective cards
        HBox root = new HBox(20);

        // Request the first common objective card from the server
        out.println("firstCommon");
        int idObj1 = Integer.parseInt(in.readLine()); // Read the ID of the first common objective card
        System.out.println(idObj1); // Print the ID of the first common objective card

        // Request the second common objective card from the server
        out.println("secondCommon");
        int idObj2 = Integer.parseInt(in.readLine()); // Read the ID of the second common objective card
        System.out.println(idObj2); // Print the ID of the second common objective card

        // Construct the file paths for the images of the objective cards
        String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + idObj1 + ".png";
        Image obj1Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
        obj1ImageView = new ImageView(obj1Image); // Create an ImageView for the first objective card
        obj1ImageView.setFitWidth(162.6); // Set the width of the ImageView
        obj1ImageView.setFitHeight(112.8); // Set the height of the ImageView

        String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + idObj2 + ".png";
        Image obj2Image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
        obj2ImageView = new ImageView(obj2Image); // Create an ImageView for the second objective card
        obj2ImageView.setFitWidth(162.6); // Set the width of the ImageView
        obj2ImageView.setFitHeight(112.8); // Set the height of the ImageView

        closeButton = new Button("Close");
        // Define the action to be performed when the Close button is clicked
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());

        VBox vbox = new VBox(50); // Create a VBox to hold the ImageView and Close button
        // Add the objective card ImageViews to the HBox
        root.getChildren().addAll(obj1ImageView, obj2ImageView);
        vbox.setAlignment(Pos.CENTER); // Set the alignment of the VBox
        // Add the objective card ImageViews and Close button to the VBox
        vbox.getChildren().addAll(obj1ImageView, obj2ImageView, closeButton);

        // Create a popup stage to display the objective cards
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Set the modality of the stage
        popupStage.setTitle("Objective cards"); // Set the title of the stage
        popupStage.setScene(new Scene(vbox)); // Set the scene of the stage
        popupStage.setWidth(500); // Set the width of the stage
        popupStage.setHeight(350); // Set the height of the stage
        popupStage.showAndWait(); // Display the stage and wait for it to be closed
    }


    private String createPathForBackCards(String cardId) {
        if (Integer.parseInt(cardId)<41 ) {
            return pathForResourceCardsBack(cardId);
        } else {
            return pathForGoldCardBack(cardId);
        }
    }
    private String createPathForFrontCards(String cardId)
    {
        if (Integer.parseInt(cardId)<41 ) {
            return pathForResourceCardFront(cardId);
        } else {
            return pathForGoldCardFront(cardId);
        }
    }

    private String pathForResourceCardFront(String cardId)
    {
        String correctPath= "/ImmaginiCodex/CarteFront/Resource/";
        return correctPath + cardId + ".png";
    }
    private String pathForGoldCardFront(String cardId)
    {
        String correctPath= "/ImmaginiCodex/CarteFront/Gold/";
        return correctPath + cardId + ".png";
    }


    private String pathForResourceCardsBack(String cardId)
    {
        String correctPath= "/ImmaginiCodex/CarteBack/Resource/";
        return correctPath + cardId + ".png";
    }
    private String pathForGoldCardBack(String cardId)
    {
        String correctPath= "/ImmaginiCodex/CarteBack/Gold/";
        return correctPath + cardId + ".png";
    }


    public void setInitCard(ImageView initCard) {
        this.initCard = initCard;
    }

    public void setObj1ImageView(ImageView obj1ImageView) {
        this.obj1ImageView = obj1ImageView;
    }

    public void setObj2ImageView(ImageView obj2ImageView) {
        this.obj2ImageView = obj2ImageView;
    }

    private void creatingBackground(Pane root){
        Image gameBackgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundGameImage = new BackgroundImage(gameBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        background = new Background(backgroundGameImage);
        root.setBackground(background);
    }
    private void creatingPathForGameMethod(){
        pathResourceDeck = createPathForBackCards(idTopCardResourceDeck);
        pathGoldDeck = createPathForBackCards(idTopCardGoldDeck);
    }
    private Image createNewPathForImages(String path)
    {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    private void setWidthAndHeight(ImageView imageView)
    {
        imageView.setFitWidth(widthWellCards);
        imageView.setFitHeight(heightWellCards);
    }
    private void creatingDeckAndGoldDeckView(){
        topCardResourceDeckView = new ImageView(topCardResourceDeck); //Resource deck imageview
        topCardGoldDeckView = new ImageView(topCardGoldDeck);         //Gold Deck imageview
        setWidthAndHeight(topCardResourceDeckView);
        setWidthAndHeight(topCardGoldDeckView);
    }

    private void settingDecksOnMouseClickedEvent(){
        topCardResourceDeckView.setOnMouseClicked(e-> {
            chosenDeckForDrawingNewCard = "resource";
            wellOrDeck = "deck";
            System.out.println("resourceDeck clicked");
            System.out.println(idTopCardResourceDeck);
        });
        topCardGoldDeckView.setOnMouseClicked(e-> {
            chosenDeckForDrawingNewCard = "gold";
            wellOrDeck = "deck";
            System.out.println("goldDeck clicked");
            System.out.println(idTopCardGoldDeck);
        });
    }
    private void creatingWell()
    {
        wellPathOne = "/ImmaginiCodex/CarteFront/"+typeCard1+"/"+ idCard1 +".png";
        wellPathSecond = "/ImmaginiCodex/CarteFront/"+typeCard2+"/"+ idCard2 +".png";
        wellPathThird = "/ImmaginiCodex/CarteFront/"+typeCard3+"/"+ idCard3 +".png";
        wellPathForth = "/ImmaginiCodex/CarteFront/"+typeCard4+"/"+ idCard4 +".png";
    }
    private void creatingImagesForTheWell(){
        wellCard1 = createNewPathForImages(wellPathOne);
        wellCard2 = createNewPathForImages(wellPathSecond);
        wellCard3 = createNewPathForImages(wellPathThird);
        wellCard4 = createNewPathForImages(wellPathForth);
    }

    private void settingWellOnMouseClickedEvent(){
        wellCard1View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: "+idCard1);
            wellOrDeck="well";
            wellCardSelected = wellCard1;
            idWellCardSelected = idCard1;
            indexCardFromWellSelected=0;
        });
        wellCard2View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: "+idCard2);
            wellOrDeck="well";
            wellCardSelected = wellCard2;
            idWellCardSelected = idCard2;
            indexCardFromWellSelected=1;
        });
        wellCard3View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: "+idCard3);
            wellOrDeck="well";
            wellCardSelected = wellCard3;
            idWellCardSelected = idCard3;
            indexCardFromWellSelected=2;
        });
        wellCard4View.setOnMouseClicked(event -> {
            System.out.println("You chose from well card number: "+idCard4);
            wellOrDeck="well";
            wellCardSelected = wellCard4;
            idWellCardSelected = idCard4;
            indexCardFromWellSelected=3;
        });
    }
    private void creatingImagesViewForTheWell(){
        wellCard1View = new ImageView(wellCard1);
        setWidthAndHeight(wellCard1View);
        wellCard2View = new ImageView(wellCard2);
        setWidthAndHeight(wellCard2View);
        wellCard3View = new ImageView(wellCard3);
        setWidthAndHeight(wellCard3View);
        wellCard4View = new ImageView(wellCard4);
        setWidthAndHeight(wellCard4View);
    }



    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
//controller-> metodi x reagire agli eventi-> button
//initialize implements initializable
//changescene


//
//@SuppressWarnings("DataFlowIssue")
//public class HelloApplication extends Application {
//    private AnchorPane root;
//    private static ImageView clickedImageView;
//    private static int angoloSelezionato;
//
//    @Override
//    public void start(Stage stage) {
//        root = new AnchorPane();
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//
//        gameboardScene(stage);
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//    public void gameboardScene(Stage stage) {
//
//
//        Image immagineBack = new Image(getClass().getResourceAsStream("/ImmaginiCarte/990796-neutral-backgrounds-3000x1661-for-desktop-2880789718.jpg"));
//        BackgroundImage backgroundImage = new BackgroundImage(immagineBack,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT);
//        Background background = new Background(backgroundImage);
//
//        //Creo uno scroll pane che conterrà tutto
//        ScrollPane perScrollare = new ScrollPane();
//        int dimensioneBoard = 500;
//
//        //Creo il TilePane che conterrà uno StackPane e gli piazzo il background
//        GridPane gameboard = new GridPane(dimensioneBoard,dimensioneBoard);
//
//
//
//        gameboard.setBackground(background);
//        gameboard.setHgap(0);
//        gameboard.setVgap(0);
//        gameboard.setAlignment(Pos.CENTER);
//
//
//
//
//        //Salvo la dimensione della finestra (root) in due variabili
//        double larghezzaFinestra = root.getWidth();
//        double altezzaFinestra = root.getHeight();
//
//        //Imposto la grandezza del TilePane all'80% della dimensione (iniziale) della finestra
//        gameboard.setPrefSize(larghezzaFinestra * 0.8, altezzaFinestra * 0.8);
//
//        //Creo immagine e ImageView della carta iniziale che dovrò piazzare al centro
//
//        Image cartaIniziale = new Image(getClass().getResourceAsStream("/ImmaginiCarte/15.png"));
//        GridPane immagineDivisa = dividiImmagineEpiazzalaInUnGridPane(cartaIniziale);
//
//
//        //CARTA INIZIALE -> ogni cella del gridpane viene castata a ImageView
//        ImageView img1 = (ImageView) immagineDivisa.getChildren().get(0);
//        ImageView img2 = (ImageView) immagineDivisa.getChildren().get(1);
//        ImageView img3 = (ImageView) immagineDivisa.getChildren().get(2);
//        ImageView img4 = (ImageView) immagineDivisa.getChildren().get(3);
//
//        //Ogni imageView viene resa clickabile
//        img1.setPickOnBounds(true);
//        img2.setPickOnBounds(true);
//        img3.setPickOnBounds(true);
//        img4.setPickOnBounds(true);
//
//
//
//        //AGGIUNTA CARTA INIZIALE ogni imageView sopra creata viene aggiunta alla gameboard
//        gameboard.add(img1,dimensioneBoard/2,dimensioneBoard/2);
//        gameboard.add(img2,dimensioneBoard/2+1,dimensioneBoard/2);
//        gameboard.add(img3,dimensioneBoard/2,dimensioneBoard/2+1);
//        gameboard.add(img4,dimensioneBoard/2+1,dimensioneBoard/2+1);
//
//        img1.setOnMouseClicked(event -> {
//            setClickedImageView(img1);
//            angoloSelezionato = 1;
//        });
//        img2.setOnMouseClicked(event -> {
//            setClickedImageView(img2);
//            angoloSelezionato = 2;
//        });
//        img3.setOnMouseClicked(event -> {
//            setClickedImageView(img3);
//            angoloSelezionato = 3;
//        });
//        img4.setOnMouseClicked(event -> {
//            setClickedImageView(img4);
//            angoloSelezionato = 4;
//        });
//
////        Print di debugging, non servono ai fini del funzionamento
////        System.out.println("Coordinata x dell'immagine 1 TL: "+getX(img1));
////        System.out.println("Coordinata y dell'immagine 1 TL: "+getY(img1));
////        System.out.println("Coordinata x dell'immagine 2 TR: "+getX(img2));
////        System.out.println("Coordinata y dell'immagine 2 TR: "+getY(img2));
////        System.out.println("Coordinata x dell'immagine 3 BL: "+getX(img3));
////        System.out.println("Coordinata y dell'immagine 3 BL: "+getY(img3));
////        System.out.println("Coordinata x dell'immagine 4 BR: "+getX(img4));
////        System.out.println("Coordinata y dell'immagine 4 BR: "+getY(img4));
//
//        perScrollare.setContent(gameboard);
//        BorderPane layout = new BorderPane();
//        layout.setCenter(perScrollare);
//
//        Button playcard = new Button("Play Card");
//        playcard.setOnAction(e->{
//            if( angoloSelezionato == 3 ){
//                piazzaCartaBL(getClickedImageView(), gameboard, 32);}
//            else if( angoloSelezionato == 4 ){
//                piazzaCartaBR(getClickedImageView(), gameboard, 32);
//            }
//            else if( angoloSelezionato == 1 ){
//                piazzaCartaTL(getClickedImageView(), gameboard, 32);
//            }
//            else if( angoloSelezionato == 2 ){
//                piazzaCartaTR(getClickedImageView(), gameboard, 32);
//            }
//        });
//
//        //Piazza il pulsante a sinistra
//        layout.setRight(playcard);
//        root.getChildren().add(layout);
//    }
//
//    //Prende un'immagine e crea un gridPane 2x2 che avrà in ogni cella un quarto dell'immagine
//    public GridPane dividiImmagineEpiazzalaInUnGridPane(Image image){
//        double larghezzaA = image.getWidth()/2;
//        double altezzaA = image.getHeight()/2;
//
//        int altezza = (int) altezzaA;
//        int larghezza = (int) larghezzaA;
//
//        Image image1 = new WritableImage(image.getPixelReader(),0,0,larghezza,altezza);
//        Image image2 = new WritableImage(image.getPixelReader(),larghezza,0,larghezza,altezza);
//        Image image3 = new WritableImage(image.getPixelReader(),0,altezza,larghezza,altezza);
//        Image image4 = new WritableImage(image.getPixelReader(),larghezza,altezza,larghezza,altezza);
//
//        ImageView imageView1 = new ImageView(image1);
//        ImageView imageView2 = new ImageView(image2);
//        ImageView imageView3 = new ImageView(image3);
//        ImageView imageView4 = new ImageView(image4);
//
//        imageView1.setFitWidth(100);
//        imageView1.setPreserveRatio(true);
//        imageView2.setFitWidth(100);
//        imageView2.setPreserveRatio(true);
//        imageView3.setFitWidth(100);
//        imageView3.setPreserveRatio(true);
//        imageView4.setFitWidth(100);
//        imageView4.setPreserveRatio(true);
//
//        GridPane gridPane = new GridPane();
//        gridPane.setPadding(new Insets(0));
//        gridPane.setHgap(0);
//        gridPane.setVgap(0);
//
//        gridPane.add(imageView1, 0, 0);
//        gridPane.add(imageView2, 1, 0);
//        gridPane.add(imageView3, 0, 1);
//        gridPane.add(imageView4, 1, 1);
//
//        imageView1.setPickOnBounds(true);
//        imageView2.setPickOnBounds(true);
//        imageView3.setPickOnBounds(true);
//        imageView4.setPickOnBounds(true);
//
//        imageView1.setOnMouseClicked(event -> {
//            setClickedImageView(imageView1);
//            angoloSelezionato = 1;
//        });
//
//        imageView2.setOnMouseClicked(event -> {
//            setClickedImageView(imageView2);
//            angoloSelezionato = 2;
//        });
//
//        imageView3.setOnMouseClicked(event -> {
//            setClickedImageView(imageView3);
//            angoloSelezionato = 3;
//        });
//
//        imageView4.setOnMouseClicked(event -> {
//            setClickedImageView(imageView4);
//            angoloSelezionato = 4;
//        });
//
//        return gridPane;
//    }
//
//
//    public int getX (ImageView img){
//        return GridPane.getRowIndex(img);
//    }
//
//    public int getY (ImageView img){
//        return GridPane.getColumnIndex(img);
//    }
//
//    public void piazzaCartaBR(ImageView cartaSuCuiPiazzo, GridPane board, int id){
//
//        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/"+id+".png"));
//
//        int x = getX(cartaSuCuiPiazzo);
//        int y = getY(cartaSuCuiPiazzo);
//
//        GridPane piazzare = dividiImmagineEpiazzalaInUnGridPane(secondaImmagine);
//
//        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
//        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
//        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
//        ImageView im4 = (ImageView) piazzare.getChildren().get(3);
//
//        im1.setPickOnBounds(true);
//        im2.setPickOnBounds(true);
//        im3.setPickOnBounds(true);
//        im4.setPickOnBounds(true);
//
//        board.add(im1,y,x);
//        board.add(im2,(y+1),x);
//        board.add(im3,y,(x+1));
//        board.add(im4,(y+1),(x+1));
//    }
//
//    public void piazzaCartaBL(ImageView cartaSuCuiPiazzo, GridPane board, int id){
//
//        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/"+id+".png"));
//
//        int x = getX(cartaSuCuiPiazzo);
//        int y = getY(cartaSuCuiPiazzo);
//
//        GridPane piazzare = dividiImmagineEpiazzalaInUnGridPane(secondaImmagine);
//
//        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
//        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
//        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
//        ImageView im4 = (ImageView) piazzare.getChildren().get(3);
//
//        im1.setPickOnBounds(true);
//        im2.setPickOnBounds(true);
//        im3.setPickOnBounds(true);
//        im4.setPickOnBounds(true);
//
//        board.add(im1,(y-1),(x));
//        board.add(im2,(y),(x));
//        board.add(im3,(y-1),(x+1));
//        board.add(im4,(y),(x+1));
//    }
//
//    public void piazzaCartaTL(ImageView cartaSuCuiPiazzo, GridPane board, int id){
//
//        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/"+id+".png"));
//
//        int x = getX(cartaSuCuiPiazzo);
//        int y = getY(cartaSuCuiPiazzo);
//
//        GridPane piazzare = dividiImmagineEpiazzalaInUnGridPane(secondaImmagine);
//
//        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
//        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
//        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
//        ImageView im4 = (ImageView) piazzare.getChildren().get(3);
//
//        im1.setPickOnBounds(true);
//        im2.setPickOnBounds(true);
//        im3.setPickOnBounds(true);
//        im4.setPickOnBounds(true);
//
//        board.add(im1,(y-1),(x-1));
//        board.add(im2,(y),(x-1));
//        board.add(im3,(y-1),(x));
//        board.add(im4,(y),(x));
//    }
//
//    public void piazzaCartaTR(ImageView cartaSuCuiPiazzo, GridPane board, int id){
//
//        Image secondaImmagine = new Image(getClass().getResourceAsStream("/ImmaginiCarte/"+id+".png"));
//
//        int x = getX(cartaSuCuiPiazzo);
//        int y = getY(cartaSuCuiPiazzo);
//
//        GridPane piazzare = dividiImmagineEpiazzalaInUnGridPane(secondaImmagine);
//
//        ImageView im1 = (ImageView) piazzare.getChildren().get(0);
//        ImageView im2 = (ImageView) piazzare.getChildren().get(1);
//        ImageView im3 = (ImageView) piazzare.getChildren().get(2);
//        ImageView im4 = (ImageView) piazzare.getChildren().get(3);
//
//        im1.setPickOnBounds(true);
//        im2.setPickOnBounds(true);
//        im3.setPickOnBounds(true);
//        im4.setPickOnBounds(true);
//
//        board.add(im1,(y),(x-1));
//        board.add(im2,(y+1),(x-1));
//        board.add(im3,(y),(x));
//        board.add(im4,(y+1),(x));
//    }
//
//    public ImageView getClickedImageView() {
//        return clickedImageView;
//    }
//
//    public void setClickedImageView(ImageView clickedImageView) {
//        this.clickedImageView = clickedImageView;
//    }


