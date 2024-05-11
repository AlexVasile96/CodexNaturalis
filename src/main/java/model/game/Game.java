package model.game;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.card.*;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ObjectiveDeck;
import model.deck.ResourceDeck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Game implements WhatCanPlayerDo {
    private List<Player> players;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private InitialCardDeck initialCardDeck;
    private ObjectiveDeck objectiveDeck;
    private int currentPlayerIndex;
    private String currentPlayer;
    private Player currentPlayingPLayer;
    private List<Card> well;
    private ObjectiveCard firstObjectiveCommonCard;
    private ObjectiveCard secondObjectiveCommonCard;
    private List<String> dots;
    private static boolean isCornerAlreadyChosen= false;
    private Card selectedCardFromTheDeck=null;
    private Card cardPlayerChoose=null;
    private List<Player> playersFromDisk;
    private Card wellCard1;
    private Card wellCard2;
    private Card wellCard3;
    private Card wellCard4;


    public Game() {                                           //GAME CONSTRUCTOR WHICH INITIALIZED ALL THE CARDS
        this.players = new ArrayList<>();
        this.well= new ArrayList<>();
        this.dots= new ArrayList<>();
        //loadPlayers();
        ResourceCardConstructor constructor = new ResourceCardConstructor();
        resourceDeck = (ResourceDeck) constructor.createCards();
        resourceDeck.shuffle();

        GoldCardConstructor constructorGold = new GoldCardConstructor();
        goldDeck = (GoldDeck) constructorGold.createCards();
        goldDeck.shuffle();

        InitCardConstructor constructorInitial = new InitCardConstructor();
        initialCardDeck = (InitialCardDeck) constructorInitial.createCards();
        initialCardDeck.shuffle();

        ObjectiveCardConstructor constructorObjective = new ObjectiveCardConstructor();
        objectiveDeck = (ObjectiveDeck) constructorObjective.createCards();
        objectiveDeck.shuffle();

        initializeDots();
        initializewell();
        commonObjectiveCards();

    }

    public void addPlayer(Player player) {
        boolean esiste = false;
        for (Player p : players) {
            if (p.getNickName().equals(player.getNickName())) {
                esiste = true;
                break;
            }
        }
        if (!esiste) players.add(player);
    }

    public Player getCurrentPlayingPLayer() {
        return currentPlayingPLayer;
    }

    public void setCurrentPlayingPLayer(Player currentPlayingPLayer) {
        this.currentPlayingPLayer = currentPlayingPLayer;
    }

    public void assignResourcesAndGoldCardsToPlayers() {
        for (Player player : players) {
            player.drawResourceCard(resourceDeck);
            player.drawResourceCard(resourceDeck);
            player.drawGoldCard(goldDeck);
        }
    }

    public String getFirstCardOfResourceDeck(){
        return resourceDeck.sendIdCardToGui();
    }
    public String getFirstCardOfGoldDeck(){
        return goldDeck.sendIdCardToGui();
    }



    public void placeInitialCard(Board board, InitialCard card){
        board.placeFrontInitialCard(card);
    }
    public void placeInitialCardBack(Board board, InitialCard card)
    {
        board.placeBackInitialCard(card);
        card.setCardBack(true);
    }



    public String playCard(Player player, int cardindex, int cardChosenOnTheBoard, String selectedCorner) {
            player.playCard(player.getBoard(),cardindex,cardChosenOnTheBoard,selectedCardFromTheDeck, (InitialCard) cardPlayerChoose, selectedCorner);
            String finalAnswer="Carta piazzata correttamente";
            player.getClientView().update(player);
            return finalAnswer;
    }

    public void endGame(){
        System.out.println("Updating each player points to see who is the real winner!");
        for(Player player: players)
        {
            System.out.println(player.getNickName());
            System.out.println(player.getPlayerScore());
            ObjectiveCard secretCard= player.getSecretChosenCard();
            player.getBoard().createSpecificSecretCard(secretCard,player);
            player.getBoard().createSpecificSecretCard(firstObjectiveCommonCard,player);
            player.getBoard().createSpecificSecretCard(secondObjectiveCommonCard, player);
            System.out.println(player.getPlayerScore());
        }
        Player winner= calculateWinner(players);
        System.out.println("And the winner is...........");
        System.out.println("Suspance...");
        System.out.println(winner.getNickName());
    }

    public Player calculateWinner(List<Player> players) {               //Calculating who has the highest score
        Player winner = null;
        int highestScore = currentPlayingPLayer.getPlayerScore();
        for (Player player : players) {
            int playerScore = player.getPlayerScore();
            if (playerScore > highestScore) {
                highestScore = playerScore;
                winner = player;
            }
        }
        return winner;
    }


    public String showYourspecificSeeds(Player player){
        BoardPoints boardPoints= new BoardPoints();
        String yourSpecificSeeds= boardPoints.countPoints(player.getBoard()).toString();
        System.out.println(yourSpecificSeeds);
        return yourSpecificSeeds;
    }

    @Override
    public String showCards(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    @Override
    public void chosenHandCard() {

    }

    @Override
    public String  visualizeCommonObjective(Player player) {
        StringBuilder cardsAsString = new StringBuilder();
        cardsAsString.append(firstObjectiveCommonCard.toString()).append("\n");
        cardsAsString.append(secondObjectiveCommonCard.toString());
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    public String firstCommonObjectiveCardId(){
        int id= firstObjectiveCommonCard.getId();
        System.out.println(id);
        return String.valueOf(id);
    }

    public String secondCommonObjectiveCardId(){
        int id= secondObjectiveCommonCard.getId();
        return String.valueOf(id);
    }



    @Override
    public String visualizeSecretObjective(Player player) {
        return String.valueOf(player.getSecretChosenCard());
    }




    @Override
    public String showBoard(Player player) {
        return player.getBoard().printBoardForServer();
    }
    public String showAllPlayersBoard(){
        StringBuilder stamp = new StringBuilder();
        for(Player playerz: players)
        {
            stamp.append("////////////////////////////////// INIZIO BOARD: ");
            stamp.append(playerz.getNickName());
            stamp.append(" //////////////////////////////////////////");
            stamp.append("\n");
            String point = showPoints(playerz);
            stamp.append("Current points: ");
            stamp.append(point);
            stamp.append("\n");
            String bord= playerz.getBoard().printBoardForServer();
            stamp.append(bord.replace("fine board", "\n"));
            stamp.append("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
            stamp.append("\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    public String sendWellIdFirstToGui(){
        int id1= well.get(0).getId();
        return String.valueOf(id1);
    }
    public String sendWellIdSecondToGui(){

        int id2= well.get(1).getId();
       return String.valueOf(id2);
    }
    public String sendWellIdThirdToGui(){

        int id3=  well.get(2).getId();
        return String.valueOf(id3);

    }
    public String sendWellIdFourthToGui(){

        int id4=  well.get(3).getId();;
        return String.valueOf(id4);
    }

    @Override
    public String showAllPoints() {
        StringBuilder stamp = new StringBuilder();
        for(Player playerz: players)
        {
            stamp.append(playerz.getNickName());
            stamp.append(" current Point: ");
            stamp.append(showPoints(playerz));
            stamp.append("\n--------------------------------------------------\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    public String showAllSpecificSeed(){
        StringBuilder stamp = new StringBuilder();
        for(Player playerz: players)
        {
            stamp.append(playerz.getNickName());
            stamp.append(" current Seed:\n");
            stamp.append(showYourspecificSeeds(playerz));
            stamp.append("\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }
    public String getDeckID(){
        List<Card> cardToSendToServer = currentPlayingPLayer.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.getId()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa



    }
    public String showBoardForPlacingCards(Player player)
    {
        return player.getBoard().printBoardForServer();
    }
    public String showAvaiableCorners(Player player, int cardindex, int cardChosenOnTheBoard)
    {
            InitialCard initialCard = (InitialCard) player.getBoard().getCardsOnTheBoardList().getFirst();
            selectedCardFromTheDeck= player.checkingTheChosencard(cardindex);
            cardPlayerChoose= player.gettingCardsFromTheBoard(player.getBoard(), cardChosenOnTheBoard);
            String result= player.isTheCardChosenTheInitialcard(cardPlayerChoose, initialCard);
            System.out.println(result);
            isCornerAlreadyChosen=true;
            return result;
    }

    @Override
    public String showPoints(Player player) {
        return String.valueOf(player.getPlayerScore());
    }

    @Override
    public void runEndTurn(Player player) {
        //saveCards();
        savePlayers();
        System.out.println("Carte salvate correttamente");
    }

    public String showWell(){
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : well) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    @Override
    public String drawResourceCard(Player player) {
        return player.drawResourceCard(resourceDeck);
    }

    @Override
    public String drawGoldCard(Player player) {
        return player.drawGoldCard(goldDeck);
    }

    public String drawCardFromWell(Player player, int index) {
        return player.chooseCardFromWellForServer(well, index, resourceDeck, goldDeck);
    }

    @Override
    public void chooseSecretCard(Player player, List<ObjectiveCard> secretCards) {
    }

    @Override
    public void turnYourCard(Card card) {

    }

    @Override
    public int getPlayerScore(Player player) {
        return player.getPlayerScore();
    }

    @Override
    public String getNickName(Player player) {
        return player.getNickName();
    }

    @Override
    public List<Card> getPlayerCards(Player player) {
        return player.getPlayerCards();
    }

    public void updateSingleClientView(Player player){
        player.getClientView().update(player);

    }
    @Override
    public void requestGameInfo(Player player) {

    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    public void setResourceDeck(ResourceDeck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    public void setGoldDeck(GoldDeck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public InitialCardDeck getInitialCardDeck() {
        return initialCardDeck;
    }

    public void setInitialCardDeck(InitialCardDeck initialCardDeck) {
        this.initialCardDeck = initialCardDeck;
    }

    public ObjectiveDeck getObjectiveDeck() {
        return objectiveDeck;
    }

    public void setObjectiveDeck(ObjectiveDeck objectiveDeck) {
        this.objectiveDeck = objectiveDeck;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public int CardsIndeck(){
        return resourceDeck.carteRimaste();
    }
    public int GoldsIndeck(){
        return goldDeck.carteRimaste();
    }

    public synchronized List<String> getDots() {
        return dots;
    }

    public synchronized void removeDot(String stringa) {
        dots.remove(stringa);
    }

    public synchronized boolean isInDots(String stringa) {
        return dots.contains(stringa);
    }

    public List<Card> getWell(){
        return well;
    }

    //PRIVATE METHODS INSIDE GAME

    private void initializewell(){
        resourceDeck.drawCard(well);
         resourceDeck.drawCard(well);
         goldDeck.drawCard(well);
         goldDeck.drawCard(well);
        System.out.println("Cards in the well: ");
        for (Card card : well) {
            System.out.println(card);
        }
        System.out.println("\n");
    }



    private void commonObjectiveCards()
    {
        this.firstObjectiveCommonCard= objectiveDeck.firstCardForEachPlayer(); //common objective cards
        this.secondObjectiveCommonCard= objectiveDeck.firstCardForEachPlayer();
        System.out.println("First common objective card is " + firstObjectiveCommonCard);
        System.out.println("Second common objective card is " + secondObjectiveCommonCard);
    }

    private void initializeDots() {
        dots.add("RED");
        dots.add("BLUE");
        dots.add("GREEN");
        dots.add("YELLOW");
    }

    private Path getDefaultCardPath(){
        String home= ("src/main/resources/savecard.json");
        return Paths.get(home);
    }

    void saveCards(){
        savePath(getDefaultCardPath());
    }

    void savePath(Path path){                                                   //METHOD TO SAVE CARDS
        JsonArray jo= new JsonArray();
        for(Card card: currentPlayingPLayer.getPlayerCards()){
            jo.add(card.toJsonObject());
        }
        String jsonText = jo.toString();
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            fileWriter.write(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveEachPlayerInGame(Path path){                                                   //METHOD TO SAVE CARDS
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            for (Player player : players) {
                String jsonText = player.toJsonObject().toString();
                fileWriter.write(jsonText + "\n"); // Scrive ogni giocatore su una riga separata
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void savePlayers(){
        saveEachPlayerInGame(getDefaultPlayers());
    }


    private Path getDefaultPlayers(){
        String home= ("src/main/resources/saveplayers.json");
        return Paths.get(home);
    }



    void load(){
        loadPath(getDefaultCardPath());
    }

    public void loadPath(Path path) {
        try {
            String jsonText = Files.readString(path);
            JsonArray jsonArray = new Gson().fromJson(jsonText, JsonArray.class);

            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Card card = Card.fromJsonObject(jsonObject);
                currentPlayingPLayer.getPlayerCards().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadPlayers() {
        Path filePath = getDefaultPlayers();
        if (Files.exists(filePath)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                Gson gson = new Gson();
                while ((line = bufferedReader.readLine()) != null) {
                    JsonObject playerJson = gson.fromJson(line, JsonObject.class);
                    String nickName = playerJson.get("nickName").getAsString();
                    int playerScore = playerJson.get("score").getAsInt();
                    Dot dot = Dot.values()[playerJson.get("dot").getAsInt()];
                    Board board=null;
                    if(playerJson.has("board")){
                    board = Board.fromJsonObject(playerJson.getAsJsonObject("board"));}
                    Player player = new Player(nickName, playerScore, dot, board);
                    players.add(player);
                    System.out.println(players);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

