package model.game;

import model.card.*;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ObjectiveDeck;
import model.deck.ResourceDeck;

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
    private List<Card> well;
    private ObjectiveCard firstObjectiveCommonCard;
    private ObjectiveCard secondObjectiveCommonCard;
    private List<String> dots;
    private static boolean isCornerAlreadyChosen= false;
    private Card selectedCardFromTheDeck=null;
    private Card cardPlayerChoose=null;


    public Game() {                                           //GAME CONSTRUCTOR WHICH INITIALIZED ALL THE CARDS
        this.players = new ArrayList<>();
        this.well= new ArrayList<>();
        this.dots= new ArrayList<>();
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
        players.add(player);
    }

    public void assignResourcesAndGoldCardsToPlayers() {
        for (Player player : players) {
            player.drawResourceCard(resourceDeck);
            player.drawResourceCard(resourceDeck);
            player.drawGoldCard(goldDeck);
        }
    }





    public void placeInitialCard(Board board, InitialCard card){
        board.placeInitialCard(card);
    }

    @Override
    public void drawCard() {

    }




    public String playCard(Player player, int cardindex, int cardChosenOnTheBoard, String selectedCorner) {
            player.playCard(player.getBoard(),cardindex,cardChosenOnTheBoard,selectedCardFromTheDeck,cardPlayerChoose, selectedCorner);
            String finalAnswer="Carta piazzata correttamente";
            player.getClientView().update(player);
        return finalAnswer;
    }

    @Override
    public String showCards(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        //System.out.println(player.getPlayerCards());
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

    @Override
    public String visualizeSecretObjective(Player player) {
        String result= String.valueOf(player.getSecretChosenCard());
        return result;
    }

    @Override
    public String showBoard(Player player) {
        return player.getBoard().printBoardForServer();
    }

    public String showBoardForPlacingCards(Player player)
    {
        return player.getBoard().printBoardForServer();
    }
    public String showAvaiableCorners(Player player, int cardindex, int cardChosenOnTheBoard)
    {
            Card initialCard = player.getBoard().getCardsOnTheBoardList().get(0);
            selectedCardFromTheDeck= player.primostep(player.getBoard(),cardindex, cardChosenOnTheBoard);
            cardPlayerChoose= player.secondostep(player.getBoard(),cardindex, cardChosenOnTheBoard);
            String result= player.terzostep(player.getBoard(),cardindex, cardChosenOnTheBoard, selectedCardFromTheDeck,cardPlayerChoose, initialCard);
            System.out.println(result);
            isCornerAlreadyChosen=true;
            return result;
    }

    @Override
    public String showPoints(Player player) {
        return String.valueOf(player.getPlayerScore());
    }

    public String showWell(){
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : well) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    @Override
    public void runEndTurn() {

    }

    @Override
    public void drawResourceCard(Player player) {
        player.drawResourceCard(resourceDeck);
    }

    @Override
    public void drawGoldCard(Player player) {
        player.drawGoldCard(goldDeck);
    }

    public String drawCardFromWell(Player player, int index) {
        return player.chooseCardFromWellForServer(well, index, resourceDeck, goldDeck);
    }

    @Override
    public void placeCardOnBoard(Player player, int cardIndex, String selectedCorner) { //Da rivedere come gestire questo caso con il client, quando sceglie il corner?
        player.playCard(player.getBoard(),cardIndex);
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

    @Override
    public void endTurn(Player currentPlayer) {

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


}

