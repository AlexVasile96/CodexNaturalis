package com.example.proj;

import java.util.List;

public class CardsOnTheBoard { //CLASS THAT READS THE CARD THAT HAD BEEN PLACED ON THE BOARD
    private List<Card> cardsOnTheBoard;
    public CardsOnTheBoard(List<Card> cardsOnTheBoard) {
        this.cardsOnTheBoard = cardsOnTheBoard;
    }
    public List<Card> updateCardsOnTheBoard(List<Card> cards)
    {
        cardsOnTheBoard= cards;
        return cardsOnTheBoard;
    } //construcotr
}
