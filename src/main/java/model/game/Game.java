package model.game;

import model.card.*;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ObjectiveDeck;
import model.deck.ResourceDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public Game() {
        this.players = new ArrayList<>();
        this.well= new ArrayList<>();
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

        initializewell();
        commonObjectiveCards();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }



    public void initializewell(){
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

    public void assignResourcesAndGoldCardsToPlayers() {
        for (Player player : players) {
            player.drawResourceCard(resourceDeck);
            player.drawResourceCard(resourceDeck);
            player.drawGoldCard(goldDeck);
        }
    }

    public void commonObjectiveCards()
    {
        this.firstObjectiveCommonCard= objectiveDeck.firstCardForEachPlayer(); //common objective cards
        this.secondObjectiveCommonCard= objectiveDeck.firstCardForEachPlayer();
        System.out.println("First common objective card is " + firstObjectiveCommonCard);
        System.out.println("Second common objective card is " + secondObjectiveCommonCard);
    }


    @Override
    public void drawCard() {

    }

    @Override
    public void playCard(Board board, Card card) {

    }

    @Override
    public void showCards() {

    }

    @Override
    public void chosenHandCard() {

    }

    @Override
    public void visualizeCommonObjective() {

    }

    @Override
    public void visualizeSecretObjective() {

    }

    @Override
    public void showBoard() {

    }

    @Override
    public void showPoints() {

    }

    @Override
    public void runEndTurn() {

    }

    @Override
    public void drawResourceCard(Player player) {

    }

    @Override
    public void drawGoldCard(Player player) {

    }

    @Override
    public void placeCardOnBoard(Player player, int cardIndex, String selectedCorner) {

    }

    @Override
    public void chooseSecretCard(Player player, List<ObjectiveCard> secretCards) {

    }

    @Override
    public void turnYourCard(Card card) {

    }

    @Override
    public void getPlayerScore(Player player) {

    }

    @Override
    public void getNickName(Player player) {

    }

    @Override
    public void getPlayerCards(Player player) {

    }

    @Override
    public void endTurn(Player player) {

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

}

