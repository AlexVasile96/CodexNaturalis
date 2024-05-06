package model.game;

/*
* QUESTA INTERFACCIA, IMPLEMENTATA DA GAME, CONTIENE TUTTI I METODI CHE POSSONO ESSERE UTILIZZATI DIRETTAMENTE DAL CLIENT (ES-> PEESCA UNA PESCA)
* NON SONO CONTENUTI I METODI RELATIVI AD ALTRO -> GETTER, SETTER
* NEL GAME, INVECE, CI SONO TUTTI I METODI (ES-> ASSEGNAZIONE CARTE OBIETTIVO COMUNI, FIRST CARD, CREAZIONE TABELLONE DEI PUNTI ECC..
* */


import model.card.Card;
import model.card.InitialCard;
import model.card.ObjectiveCard;

import java.util.List;

public interface WhatCanPlayerDo {


    public void placeInitialCard(Board board, InitialCard card);
    String showCards(Player player);
    void chosenHandCard(); //implementare metodo in player
    String visualizeCommonObjective(Player player);
    String visualizeSecretObjective(Player player);
    String showBoard(Player player);
    String showPoints(Player player);
    void runEndTurn(Player player);
    String drawResourceCard(Player player);
    String drawGoldCard(Player player);
    String drawCardFromWell(Player player, int index);
    void chooseSecretCard(Player player, List<ObjectiveCard> secretCards);
    void turnYourCard(Card card);
    int getPlayerScore(Player player);
    String getNickName(Player player);
    List<Card> getPlayerCards(Player player);
    void requestGameInfo(Player player); //Richiede le informazioni attuali sullo stato del gioco al server.

    String showAllPoints();
    String showAllSpecificSeed();
}
