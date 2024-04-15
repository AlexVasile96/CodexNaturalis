package model;

import java.util.List;
import java.util.Set;

public class Game implements WhatCanPlayerDo{
    private Player player;
    private String username;

    public Game(Set<String> playerName){
    }

    public Player getCurrentPlayer() {
        return player;
    }
    public String getUsername()
    {
        return username;
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



       /* playcard(chosencard)
        {
            currentplayer.playCard();
        }*/

}

