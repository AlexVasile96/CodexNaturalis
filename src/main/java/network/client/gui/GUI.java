package network.client.gui;


import controller.GuiController;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.game.Dot;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Stack;

public class GUI extends Application {

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

    private double heightWellCards = 70*1.75;
    private double widthWellCards = 101*1.75;
    private static Stage window;
    public ImageView firstCardFromWell;
    public ImageView secondCardFromWell;
    public ImageView fourthCardFromWell;
    public ImageView thirdCardFromWell;
    public GridPane gridPane;
    private Scene startScene;
    private Scene loginScene;
    private static BufferedReader in;
    private int isFront=0; //The server need 1 to place the card on the back and 0 to place it on the front
    private Scene gameScene;
    private Scene chooseNumOfPlayersScene;
    private Scene lobbyScene;
    private Scene chooseSecretObjectiveScene;
    private Scene chooseInitCardScene;
    private int selectedNumOfPlayers;
    private static GuiController guiController = null;
    private String id;
    private ClientView clientView = new ClientView();
    private static Socket socket;
    private static PrintWriter out;
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;
    @FXML
    public ToggleGroup toggleGroup;
    @FXML
    public Label testDot;
    @FXML
    public ToggleGroup numOfPlayersGroup;
    @FXML
    public Label testNumbers;
    @FXML
    public ImageView obiettivo1;
    @FXML
    public ImageView obiettivo2;
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




    @FXML
    public HBox hboxGame;
    @FXML
    public VBox vboxGame;

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
        launch(args); //default

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        startMenuScene(primaryStage);
        guiController = new GuiController(0);

    }

    private void startMenuScene(Stage primaryStage) throws IOException {

        // Carica l'immagine di sfondo
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/mainMenu.fxml")));
        Image codexLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(codexLogo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        // Imposta lo sfondo del layout
        StackPane root = new StackPane();
        root.setBackground(background);
        root.getChildren().addAll(fxml); // Aggiungi il layout dei bottoni sopra all'immagine di sfondo
        // Crea la scena di avvio
        startScene = new Scene(root, 919, 743);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Codex");
        primaryStage.show();
    }

    public void startGameClicked(ActionEvent event) throws IOException {
        //Must send this string to the server in order to be able to login
        String firstMessage = "login";
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        out.println(firstMessage); //-> il client ha detto login
        //Creates the login scene
        loginScene();
        //thread to update GUI
        Platform.runLater(() -> {
            primaryStage.setScene(loginScene);
        });


    }




    private void loginScene() throws IOException {
        //Load the scene from fxml
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/loginScene.fxml")));
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);




        StackPane root = new StackPane();
        root.setBackground(background);
        root.getChildren().addAll(fxml);
        loginScene = new Scene(root, 800, 600);

    }

    public void loginButtonClicked(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            System.out.println("Username necessary");
            return;
        }

        Toggle dot = toggleGroup.getSelectedToggle();
        if (dot == null) {
            System.out.println("Choose your dot color please");
            return;
        }

        clientView.setUserName(username);
        out.println(username);

        String realChosenDot = ((RadioButton) dot).getText();
        clientView.setDot(Dot.valueOf(realChosenDot));
        out.println(realChosenDot);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseNumOfPlayers();
        Platform.runLater(() -> {
            primaryStage.setScene(chooseNumOfPlayersScene);
        });
    }

    private void chooseNumOfPlayers() throws IOException {
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/ChooseNumberOfPlayers.fxml")));
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        StackPane root = new StackPane();
        root.setBackground(background);
        root.getChildren().addAll(fxml);
        chooseNumOfPlayersScene = new Scene(root, 800, 600);
    }

    @FXML
    private void goToLobbyClicked(ActionEvent event) throws IOException {
        if (!guiController.isSizeSet()) {
            Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();
            guiController.setSizeSet(true);
            if (numOfPlayers == null) {
                System.out.println("Select number of players");
                return;
            }

            String selectedNumOfPlayersText = ((RadioButton) numOfPlayers).getText();
            switch (selectedNumOfPlayersText) {
                case "2 Players":
                    selectedNumOfPlayers = 1;
                    break;
                case "3 Players":
                    selectedNumOfPlayers = 3;
                    break;
                case "4 Players":
                    selectedNumOfPlayers = 4;
                    break;
                default:
                    System.out.println("Numero di giocatori non valido");
                    return;
            }

            testNumbers.setText("Il numero di giocatori è: " + selectedNumOfPlayers);
            out.println(selectedNumOfPlayers);
            guiController.setSizeSet(true);
        }

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseSecretObjective();
        Platform.runLater(() -> {
            primaryStage.setScene(chooseSecretObjectiveScene);
        });
    }

    @FXML
    private void lobby() throws IOException{
        Parent fxmlLobby = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/lobby.fxml")));

        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().addAll(fxmlLobby);

        lobbyScene = new Scene(root, 800, 600);
    }

    @FXML
    private void chooseSecretObjective() throws IOException{
        Parent fxmlGame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/SceltaObiettivoSegreto.fxml")));
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        Pane root = new Pane();
        root.setBackground(background);
        root.getChildren().addAll(fxmlGame);
        chooseSecretObjectiveScene = new Scene(root, 800, 600);
    }

    @FXML
    private void chooseSecretObjectiveClicked() throws IOException {

        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();

        String firstSecretCard = in.readLine();
        String secondSecretCard = in.readLine();

        String firstCardId = in.readLine();
        String secondCardId = in.readLine();

        String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + firstCardId + ".png";
        String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + secondCardId + ".png";

        Image objImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
        Image objImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
        obiettivo1.setImage(objImage1);
        obiettivo2.setImage(objImage2);
        System.out.println(firstSecretCard);
        System.out.println(secondSecretCard);
    }

    @FXML
    public void chosenObj1(MouseEvent event) throws IOException {
        out.println(1);
        chosenObj = obiettivo1;
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        Platform.runLater(() -> {
            primaryStage.setScene(chooseInitCardScene);
        });

    }

    @FXML
    public void chosenObj2(MouseEvent event) throws IOException {
        out.println(2);
        chosenObj = obiettivo2;
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        Platform.runLater(() -> {
            primaryStage.setScene(chooseInitCardScene);
        });

    }

    @FXML
    private void chooseInitCard() throws IOException {
        Parent fxmlInit = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/FlipInitCard.fxml")));
        Image loginBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoSchermataLogin.png")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(loginBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        Pane root = new Pane();
        root.setBackground(background);
        root.getChildren().addAll(fxmlInit);
        chooseInitCardScene = new Scene(root, 800, 600);
    }

    @FXML
    public void showInit() throws IOException {
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        id = in.readLine();
        String pathInit = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
        Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathInit)));
        initCard.setImage(initImage);
    }

    @FXML
    public int flipToBackCard() {
        int idToInt= Integer.parseInt(id);
        isFront=0;
        if(idToInt>=1 && idToInt <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(idToInt>40 && idToInt<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
        else if(idToInt>=81 && idToInt<=86) //InitCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Init/" + id + ".png";
            Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            initCard.setImage(initImage);
            setInitCard(initCard);
        }
        return isFront;
    }

    @FXML
    public int flipToFrontCard() {
        int idToInt= Integer.parseInt(id);
        isFront=1;
        if(idToInt>=1 && idToInt <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(idToInt>40 && idToInt<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
        else if(idToInt>=81 && idToInt<=86) //InitCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Init/" + id + ".png";
            Image initImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            initCard.setImage(initImage);
            setInitCard(initCard);
        }
        return isFront;
    }

    @FXML
    private void chooseInitCardFrontOrBack(ActionEvent event) throws IOException {

        out.println(isFront);
        //System.out.printf(""+isFront);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        update();
        game();
        Platform.runLater(() -> {
            primaryStage.setScene(gameScene);
        });
    }


    private void update() throws IOException {
        in.readLine();
        out.println("firstWellId");
        idCard1 = in.readLine();

        System.out.println(idCard1);

        out.println("secondWellId");
        idCard2 = in.readLine();

        System.out.println(idCard2);

        out.println("thirdWellId");
        idCard3 = in.readLine();

        System.out.println(idCard3);

        out.println("fourthWellId");
        idCard4 = in.readLine();
        System.out.println(idCard4);

        typeCard1 = checkType(idCard1);
        typeCard2 = checkType(idCard2);
        typeCard3 = checkType(idCard3);
        typeCard4 = checkType(idCard4);

        out.println("deckId");
        idHandCard1=in.readLine();
        System.out.println(idHandCard1);
        idHandCard2=in.readLine();
        System.out.println(idHandCard2);
        idHandCard3=in.readLine();
        System.out.println(idHandCard3);

        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);

        in.readLine(); //spazio


        out.println("firstCardResourceGui");
        idTopCardResourceDeck = in.readLine();
        System.out.println("la topCardResourceDeck is: "+idTopCardResourceDeck);

        out.println("firstCardGoldGui");
        idTopCardGoldDeck = in.readLine();
        System.out.println("la topCardGoldDeck is: "+idTopCardGoldDeck);
    }

    private int cardSelected;


    private void game() throws IOException {
        //patch for decks
        String pathResourceDeck = "/ImmaginiCodex/CarteBack/Resource/"+idTopCardResourceDeck+".png";
        String pathGoldDeck = "/ImmaginiCodex/CarteBack/Gold/"+idTopCardGoldDeck+".png";

        //decks images
        Image topCardResourceDeck = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathResourceDeck)));
        Image topCardGoldDeck = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathGoldDeck)));

        //decks imageViews
        ImageView topCardResourceDeckView = new ImageView(topCardResourceDeck);
        topCardResourceDeckView.setFitWidth(widthWellCards);
        topCardResourceDeckView.setFitHeight(heightWellCards);

        ImageView topCardGoldDeckView = new ImageView(topCardGoldDeck);
        topCardGoldDeckView.setFitWidth(widthWellCards);
        topCardGoldDeckView.setFitHeight(heightWellCards);

        //Creating well
        String pathCard1 = "/ImmaginiCodex/CarteFront/"+typeCard1+"/"+ idCard1 +".png";
        String pathCard2 = "/ImmaginiCodex/CarteFront/"+typeCard2+"/"+ idCard2 +".png";
        String pathCard3 = "/ImmaginiCodex/CarteFront/"+typeCard3+"/"+ idCard3 +".png";
        String pathCard4 = "/ImmaginiCodex/CarteFront/"+typeCard4+"/"+ idCard4 +".png";

        Image wellCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathCard1)));
        Image wellCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathCard2)));
        Image wellCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathCard3)));
        Image wellCard4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathCard4)));

        ImageView wellCard1View = new ImageView(wellCard1);
        wellCard1View.setFitHeight(heightWellCards);
        wellCard1View.setFitWidth(widthWellCards);

        ImageView wellCard2View = new ImageView(wellCard2);
        wellCard2View.setFitHeight(heightWellCards);
        wellCard2View.setFitWidth(widthWellCards);

        ImageView wellCard3View = new ImageView(wellCard3);
        wellCard3View.setFitHeight(heightWellCards);
        wellCard3View.setFitWidth(widthWellCards);

        ImageView wellCard4View = new ImageView(wellCard4);
        wellCard4View.setFitHeight(heightWellCards);
        wellCard4View.setFitWidth(widthWellCards);

        wellCard1View.setOnMouseClicked(event ->{
            System.out.println("Hai selezionato la carta id: "+idCard1);
        });
        wellCard2View.setOnMouseClicked(event ->{
            System.out.println("Hai selezionato la carta id: "+idCard2);
        });
        wellCard3View.setOnMouseClicked(event ->{
            System.out.println("Hai selezionato la carta id: "+idCard3);
        });
        wellCard4View.setOnMouseClicked(event ->{
            System.out.println("Hai selezionato la carta id: "+idCard4);
        });

        StackPane stackPaneInitCard = new StackPane();
        GridPane gridPaneInitCard = new GridPane();

        //end creation of well

        //Creating handCards
        String pathHandCard1 = "/ImmaginiCodex/CarteFront/"+typeHandCard1+"/"+idHandCard1+".png";
        String pathHandCard2 = "/ImmaginiCodex/CarteFront/"+typeHandCard2+"/"+idHandCard2+".png";
        String pathHandCard3 = "/ImmaginiCodex/CarteFront/"+typeHandCard3+"/"+idHandCard3+".png";

        Image handCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard1)));
        Image handCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard2)));
        Image handCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathHandCard3)));

        ImageView handCard1View = new ImageView(handCard1);
        handCard1View.setFitWidth(widthWellCards);
        handCard1View.setFitHeight(heightWellCards);

        ImageView handCard2View = new ImageView(handCard2);
        handCard2View.setFitWidth(widthWellCards);
        handCard2View.setFitHeight(heightWellCards);

        ImageView handCard3View = new ImageView(handCard3);
        handCard3View.setFitWidth(widthWellCards);
        handCard3View.setFitHeight(heightWellCards);

        //End creation of handCards


        //Createing suddivision of initCard
        StackPane.setAlignment(stackPaneInitCard, Pos.CENTER);
        StackPane.setMargin(stackPaneInitCard, new Insets(0, 0, 0, 0));

        Region regionTopLeft = new Region();
        regionTopLeft.setPrefSize(widthWellCards/2, heightWellCards/2);
        regionTopLeft.setOnMouseClicked(e->{
            System.out.println("hai cliccato il topLeft della initCard");
            out.println(0);
            out.println("TL");
        });

        Region regionBottomLeft = new Region();
        regionBottomLeft.setPrefSize(widthWellCards/2, heightWellCards/2);
        regionBottomLeft.setOnMouseClicked(e->{
            System.out.println("hai cliccato il bottomLeft della initCard");
            out.println(0);
            out.println("BL");
        });

        Region regionTopRight = new Region();
        regionTopRight.setPrefSize(widthWellCards/2, heightWellCards/2);
        regionTopRight.setOnMouseClicked(e->{
            System.out.println("hai cliccato il topRight della initCard");
            out.println(0);
            out.println("TR");
        });

        Region regionBottomRight = new Region();
        regionBottomRight.setPrefSize(widthWellCards/2, heightWellCards/2);
        regionBottomRight.setOnMouseClicked(e->{
            System.out.println("hai cliccato il bottomRight della initCard");
            out.println(0);
            out.println("BR");
        });

        gridPaneInitCard.add(regionTopLeft, 0, 0);
        gridPaneInitCard.add(regionTopRight, 1, 0);
        gridPaneInitCard.add(regionBottomLeft, 0, 1);
        gridPaneInitCard.add(regionBottomRight, 1, 1);

        stackPaneInitCard.getChildren().addAll(initCard, gridPaneInitCard);

        initCard.setOnMouseClicked(event -> {
            cardSelected = 0;
            out.println(cardSelected);
        });


// Creare bottone per playCard: salvataggio della carta da voler piazzare e carta su cui piazzare + angolo salvati in variabili, onClick pulsante piazza carta nel posto giusto ezzz
        handCard1View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard1);
            out.println("playCard");
            try {
                String actualBoard= in.readLine();
                do{
                    System.out.println(actualBoard);
                    actualBoard= in.readLine();
                }while (!actualBoard.equals("fine board"));
                out.println(0);
                //out.println(cardSelected);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        handCard2View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard2);
        });
        handCard3View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard3);
        });



        Pane root = new Pane();
        Image sfondoGame = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/sfondoGame.jpg")));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundGameImage = new BackgroundImage(sfondoGame, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundGameImage);
        root.setBackground(background);

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



        insideScrollPane.setBackground(background);
        HBox secondRow = new HBox();
        HBox firstColomnOfSecondRow = new HBox();
        //Pane paneForImages = new Pane();

        insideScrollPane.getChildren().add(stackPaneInitCard);
        GridPane gridPaneForWellCards = new GridPane();
        gridPaneForWellCards.add(wellCard1View, 0, 0);
        gridPaneForWellCards.add(wellCard2View, 0, 1);
        gridPaneForWellCards.add(wellCard3View, 1, 0);
        gridPaneForWellCards.add(wellCard4View, 1, 1);

        HBox decks = new HBox();
        decks.getChildren().addAll(topCardResourceDeckView, topCardGoldDeckView);


        //paneForImages.getChildren().addAll(wellCard1View, wellCard2View, wellCard3View, wellCard4View);
        vboxGame.getChildren().addAll(gridPaneForWellCards, decks);
        hboxGame.getChildren().addAll(gameBoard, vboxGame);
        firstColomnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColomnOfSecondRow);
        vboxContainer.getChildren().addAll(hboxGame, secondRow);
        anchorPane.getChildren().add(vboxContainer);

        gameScene = new Scene(root, 600, 400);

        window.widthProperty().addListener((observable, oldValue, newValue) -> {
            insideScrollPane.setPrefWidth(window.getWidth()*0.8);
        });
        window.heightProperty().addListener((observable, oldValue, newValue) -> {
            insideScrollPane.setPrefHeight(window.getHeight()*0.8);
        });
        /*window.widthProperty().addListener((observable, oldValue, newValue) -> {
            vboxGame.setPrefWidth(window.getWidth()*0.2);
        });
        window.heightProperty().addListener((observable, oldValue, newValue) -> {
            vboxGame.setPrefHeight(window.getHeight()*0.2);
        });*/

        //in.readLine();
    }

    private String checkType(String id){
        int intId = Integer.parseInt(id);
        String type;
        if(intId>0 && intId<=40){
            type = "Resource";
        }
        else{
            type = "Gold";
        }
        return type;
    }


    /*private void game() throws IOException {
        Parent fxmlGame = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/GameLayout.fxml")));
        Pane root = new Pane();
        Pane fxmlPane = (Pane) fxmlGame;
        hboxGame = (HBox) fxmlPane.lookup("#hboxGame");
        vboxGame = (VBox) fxmlPane.lookup("#vboxGame");
        gameBoard = (ScrollPane) fxmlPane.lookup("#gameBoard");
        CARTA = (ImageView) fxmlPane.lookup("#CARTA");
        if (gameBoard != null) {
            Pane contentPane = (Pane) gameBoard.getContent();
            if (contentPane != null) {
                contentPane.getChildren().add(initCard);
                root.getChildren().addAll(fxmlPane, gameBoard);
                gameScene = new Scene(root, 600, 400);
            } else {
                System.err.println("Content Pane inside gameBoard is null");
            }
        } else {
            System.err.println("Game Board is null");
        }

        //Avendo messo hbox e vbox devo definirle come è stato fatto per gameboard e mettere i pane al loro interno in modo da far capire a java che la CARTA si trova all'interno di vbox
        Image immagineCarta = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Gold/41.png")));
        CARTA.setImage(immagineCarta);
        //commento



        in.readLine();
    }*/

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

    private void addCard(){
        out.println("playcard");
        ImageView card = new ImageView();
    }

    @FXML
    public void exitClicked(ActionEvent event) throws IOException {
        try {
            closeConnection(socket);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void closeConnection(Socket socket) throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    //Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/3.png")));
    //            ImageView firstCardFromWell = new ImageView(image1);
    //            //gridPane.add(firstCardFromWell, 0, 0);
    //
    //            Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/10.png")));
    //            ImageView secondCardFromWell = new ImageView(image2);
    //
    //            //gridPane.add(secondCardFromWell, 1, 0);
    //
    //            Image image3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/25.png")));
    //            ImageView thirdCardFromWell = new ImageView(image3);
    //
    //            //gridPane.add(thirdCardFromWell, 0, 1);
    //
    //            Image image4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/31.png")));
    //            ImageView fourthCardFromWell = new ImageView(image4);
    //            fourthCardFromWell.autosize();
    //            gridPane.add(fourthCardFromWell, 1, 1);

    public void setInitCard(ImageView initCard) {
        this.initCard = initCard;
    }

    public void setObj1ImageView(ImageView obj1ImageView) {
        this.obj1ImageView = obj1ImageView;
    }

    public void setObj2ImageView(ImageView obj2ImageView) {
        this.obj2ImageView = obj2ImageView;
    }
}

/*gridPane = (GridPane) fxmlPane.lookup("#gridPane");
        gridPane.setPrefSize(300,300);
        if (gridPane != null) {
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/3.png")));
            Image image3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/3.png")));
            Image image4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/3.png")));
            Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/CarteFront/Resource/3.png")));
            firstCardFromWell = new ImageView(image1);
           secondCardFromWell= new ImageView(image2);
            thirdCardFromWell= new ImageView(image3);
            fourthCardFromWell = new ImageView(image4);

            // Imposta le dimensioni delle ImageView se necessario
            firstCardFromWell.setFitWidth(117);
            firstCardFromWell.setFitHeight(80);
            secondCardFromWell.setFitWidth(117);
            secondCardFromWell.setFitHeight(80);
            thirdCardFromWell.setFitWidth(117);
            thirdCardFromWell.setFitHeight(80);
            fourthCardFromWell.setFitWidth(117);
            fourthCardFromWell.setFitHeight(80);

            // Aggiungi le ImageView al GridPane nelle posizioni desiderate
            GridPane.setConstraints(firstCardFromWell, 0, 0);
            GridPane.setConstraints(secondCardFromWell, 1, 0);
            GridPane.setConstraints(thirdCardFromWell, 0, 1);
            GridPane.setConstraints(fourthCardFromWell, 1, 1);

            // Aggiungi le ImageView al GridPane
            gridPane.getChildren().addAll(fxmlGame, secondCardFromWell, thirdCardFromWell, fourthCardFromWell);
        }
        else {
            System.err.println("GridPane is null");
        }*/