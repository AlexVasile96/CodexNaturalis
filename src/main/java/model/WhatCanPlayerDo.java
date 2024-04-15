package model;

/*
* QUESTA INTERFACCIA, IMPLEMENTATA DA GAME, CONTIENE TUTTI I METODI CHE POSSONO ESSERE UTILIZZATI DIRETTAMENTE DAL CLIENT (ES-> PEESCA UNA PESCA)
* NON SONO CONTENUTI I METODI RELATIVI AD ALTRO -> GETTER, SETTER
* NEL GAME, INVECE, CI SONO TUTTI I METODI (ES-> ASSEGNAZIONE CARTE OBIETTIVO COMUNI, FIRST CARD, CREAZIONE TABELLONE DEI PUNTI ECC..
* */


import network.message.MessagesEnum;

import java.util.List;

public interface WhatCanPlayerDo {

    void drawCard(); //switch con resource, gold oppure pescare dal pozzo
    void playCard(Board board, Card card);
    void showCards();
    void chosenHandCard(); //implementare metodo in player
    void visualizeCommonObjective();
    void visualizeSecretObjective();
    void showBoard();
    void showPoints();
    void runEndTurn();
    void drawResourceCard(Player player);
    void drawGoldCard(Player player);
    void placeCardOnBoard(Player player, int cardIndex, String selectedCorner);
    void chooseSecretCard(Player player, List<ObjectiveCard> secretCards);
    void turnYourCard(Card card);
    void getPlayerScore(Player player);
    void getNickName(Player player);
    void getPlayerCards(Player player);
    void endTurn(Player player);
    void requestGameInfo(Player player); //Richiede le informazioni attuali sullo stato del gioco al server.
}
