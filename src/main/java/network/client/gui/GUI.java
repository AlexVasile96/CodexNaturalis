package network.client.gui;


import controller.GameController;
import controller.GuiController;
import exceptions.ParametersNotValidException;
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
import model.game.Player;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class GUI extends Application {
    private static GuiController guiController;
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
    private Player player;
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

    private Button playCard = new Button("Play Card");
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
    private String cornerSelected = null;
    private static Stage window;
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

    private String id;
    private ClientView clientView = new ClientView();
    private static Socket socket;
    private static PrintWriter out;
    private Image wellCardSelected = null;
    private String idWellCardSelected = null;
    private Player currentPlayer=null;
    Controller controller = new Controller(in, out);

    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label test;
    @FXML
    public ToggleGroup toggleGroup;
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
        //GuiController guiController = GuiController.getInstance(); // Ottieni un'istanza di GuiController
        ConnectionWithServer connectionWithServer= new ConnectionWithServer(); //creazione classe
        socket= connectionWithServer.connectToServer();
        out=new PrintWriter(socket.getOutputStream(), true); //to write
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        startMenuScene(primaryStage);
        this.clientView=clientView;

    }

    private void startMenuScene(Stage primaryStage) throws IOException {

        // Carica l'immagine di sfondo
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/model/mainMenu.fxml")));
        codexLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ImmaginiCodex/codexLogo.png")));
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
        Platform.runLater(() -> primaryStage.setScene(loginScene));
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
        System.out.println( in.readLine()); //Welcome-> you have to log in, please type your username
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
        System.out.println(clientView.getUserName());
        out.println(username);
        System.out.println( in.readLine());//Login succesfully done
        System.out.println(in.readLine());//Choose your dot color
        String realChosenDot = ((RadioButton) dot).getText();
        clientView.setDot(Dot.valueOf(realChosenDot));
        out.println(realChosenDot); //Sending dot color
        System.out.println(in.readLine());//Color correctly chosen
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseNumOfPlayers();
        Platform.runLater(() -> primaryStage.setScene(chooseNumOfPlayersScene));
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
    private void goToLobbyClicked(ActionEvent event) throws IOException, InterruptedException {
            Toggle numOfPlayers = numOfPlayersGroup.getSelectedToggle();
            if (numOfPlayers == null) {
                System.out.println("Select number of players");
                return;
            }
            String howManyPlayersAreThere = in.readLine();
            System.out.println(howManyPlayersAreThere); //At the moment there is
            if (howManyPlayersAreThere.equals("There's already someone online! You will be ")) {
                System.out.println(in.readLine()); //2
                System.out.println(in.readLine());//players
                System.out.println(in.readLine()); //You have to wait until all clients are connected;
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                chooseSecretObjective();
                Platform.runLater(() -> primaryStage.setScene(chooseSecretObjectiveScene));
            }
            else {

                System.out.println(in.readLine()); //1
                System.out.println(in.readLine()); //player.ChooseHowManyPlayers
                String selectedNumOfPlayersText = ((RadioButton) numOfPlayers).getText();
                switch (selectedNumOfPlayersText) {
                    case "2 Players":
                        selectedNumOfPlayers = 2;
                        break;
                    case "3 Players":
                        selectedNumOfPlayers = 3;
                        break;
                    case "4 Players":
                        selectedNumOfPlayers = 4;
                        break;
                    default:
                        System.out.println("Not valid number of players");
                        return;
                }
                testNumbers.setText("Number of players is: " + selectedNumOfPlayers);
                out.println(selectedNumOfPlayers);
                System.out.println(in.readLine()); //Players number correctly chosen
                GuiController.getInstance().setSizeSet(true);
                GuiController.getInstance().setNumOfPlayersLogged(1);
                System.out.println(GuiController.getInstance().getNumOfPlayersLogged()); //ok-> 1
                GuiController.getInstance().setGameSize(selectedNumOfPlayers); //2
                System.out.println(GuiController.getInstance().getGameSize());
                System.out.println(selectedNumOfPlayersText);

                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                lobby();
                Platform.runLater(() -> primaryStage.setScene(lobbyScene));
                Platform.runLater(() -> primaryStage.setScene(chooseSecretObjectiveScene));
            }
    }

    @FXML
    private void lobby() throws IOException, InterruptedException {
        System.out.println(in.readLine()); //You have to wait all players
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
        System.out.println(in.readLine()); //Printing first secret card
        System.out.println(in.readLine()); //printing second secret card
        System.out.println("Secret card printed");
        String firstCardId = in.readLine();
        String secondCardId = in.readLine();
        String pathObj1 = "/ImmaginiCodex/CarteFront/Objective/" + firstCardId + ".png";
        String pathObj2 = "/ImmaginiCodex/CarteFront/Objective/" + secondCardId + ".png";
        Image objImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj1)));
        Image objImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathObj2)));
        obiettivo1.setImage(objImage1);
        obiettivo2.setImage(objImage2);
    }

    @FXML
    public void chosenObj1(MouseEvent event) throws IOException {
        out.println(1);
        chosenObj = obiettivo1;
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        Platform.runLater(() -> primaryStage.setScene(chooseInitCardScene));
    }

    @FXML
    public void chosenObj2(MouseEvent event) throws IOException {
        out.println(2);
        chosenObj = obiettivo2;
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chooseInitCard();
        Platform.runLater(() -> primaryStage.setScene(chooseInitCardScene));

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
    public int flipToBackInitCard() {
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
    public int flipToFrontInitCard() {
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

    private void flipCardToBack(int id){
        if(id>=1 && id <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(id>40 && id<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteBack/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
    }
    private void flipCardToFront(int id){
        if(id>=1 && id <=40) //Resource Card
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Resource/" + id + ".png";
            Image resourceImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            resourceCard.setImage(resourceImage);
        }
        else if(id>40 && id<=80) //GoldCard
        {
            String pathFlipped = "/ImmaginiCodex/CarteFront/Gold/" + id + ".png";
            Image goldImage= new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFlipped)));
            goldCard.setImage(goldImage);
        }
    }

    @FXML
    private void chooseInitCardFrontOrBack(ActionEvent event) throws IOException {

        out.println(isFront);
        //System.out.printf(""+isFront);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        update();
        game();
        Platform.runLater(() -> primaryStage.setScene(gameScene));
    }


    private void update() throws IOException {
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
            indexCardToBePlacedOn = 0;
            cornerSelected = "TL";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionBottomLeft = new Region();
        regionBottomLeft.setPrefSize(widthWellCards, heightWellCards);
        regionBottomLeft.setStyle("-fx-background-color: yellow;");
        regionBottomLeft.setOnMouseClicked(e->{
            indexCardToBePlacedOn = 0;
            cornerSelected = "BL";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionTopRight = new Region();
        regionTopRight.setPrefSize(widthWellCards, heightWellCards);
        regionTopRight.setStyle("-fx-background-color: green;");
        regionTopRight.setOnMouseClicked(e->{
            indexCardToBePlacedOn = 0;
            cornerSelected = "TR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
        });

        Region regionBottomRight = new Region();
        regionBottomRight.setPrefSize(widthWellCards, heightWellCards);
        regionBottomRight.setStyle("-fx-background-color: blue;");
        regionBottomRight.setOnMouseClicked(e->{
            indexCardToBePlacedOn = 0;
            cornerSelected = "BR";
            System.out.println("hai cliccato il "+cornerSelected+" della carta numero "+indexCardToBePlacedOn);
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


// Creare bottone per playCard: salvataggio della carta da voler piazzare e carta su cui piazzare + angolo salvati in variabili, onClick pulsante piazza carta nel posto giusto ezzz
        handCard1View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard1);
            indexCardToPlace = 0;
        });
        handCard2View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard2);
            indexCardToPlace = 1;
        });
        handCard3View.setOnMouseClicked(event -> {
            System.out.println("Hai selezionato la carta id: "+idHandCard3);
            indexCardToPlace = 2;
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



        //paneForImages.getChildren().addAll(wellCard1View, wellCard2View, wellCard3View, wellCard4View);

        GridPane buttonContainer = new GridPane();

        buttonContainer.add(playCard,0, 0);
        buttonContainer.add(drawCard, 1, 0);
        /*buttonContainer.add(seeYourPoints, 2, 0);
        buttonContainer.add(seeYourSpecificSeeds, 0, 1);
        buttonContainer.add(seeOtherPlayersBoards, 1, 1);
        buttonContainer.add(endTurn, 2, 1);*/


        vboxGame.getChildren().addAll(gridPaneForWellCards, decks,decksText, buttonContainer);
        hboxGame.getChildren().addAll(gameBoard, vboxGame);
        firstColomnOfSecondRow.getChildren().addAll(handCard1View, handCard2View, handCard3View);
        secondRow.getChildren().addAll(firstColomnOfSecondRow);
        vboxContainer.getChildren().addAll(hboxGame, secondRow);
        anchorPane.getChildren().add(vboxContainer);

        gameScene = new Scene(root, 600, 400);

        window.widthProperty().addListener((observable, oldValue, newValue) -> insideScrollPane.setPrefWidth(window.getWidth()*0.8));
        window.heightProperty().addListener((observable, oldValue, newValue) -> insideScrollPane.setPrefHeight(window.getHeight()*0.8));

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

    @FXML
    public void exitClicked(ActionEvent event) {
        try {
            closeConnection(socket);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void closeConnection(Socket socket) throws IOException {
        in.close();
        out.close();
        socket.close();
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
        System.out.println("la topCardResourceDeck is: "+idTopCardResourceDeck);
        out.println("firstCardGoldGui");
        idTopCardGoldDeck = in.readLine();
        System.out.println("la topCardGoldDeck is: "+idTopCardGoldDeck);
    }

    private void checkTypeWellCards()
    {
        typeCard1 = checkType(idCard1);
        typeCard2 = checkType(idCard2);
        typeCard3 = checkType(idCard3);
        typeCard4 = checkType(idCard4);
    }
    private void playerDeck() throws IOException {
        out.println("deckId");
        idHandCard1=in.readLine();
        System.out.println(idHandCard1);
        idHandCard2=in.readLine();
        System.out.println(idHandCard2);
        idHandCard3=in.readLine();
        System.out.println(idHandCard3);
    }
    private void checkTypePlayerDeck() throws IOException {
        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);
        in.readLine(); //spazio
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

