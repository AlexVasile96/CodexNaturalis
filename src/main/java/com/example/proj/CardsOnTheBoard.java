package com.example.proj;

import java.util.List;

public class CardsOnTheBoard {
    private List<Card> cardsOnTheBoard;

    public CardsOnTheBoard(List<Card> cardsOnTheBoard) {
        this.cardsOnTheBoard = cardsOnTheBoard;
    }

    public List<Card> updateCardsOnTheBoard(List<Card> gigi)
    {
        cardsOnTheBoard= gigi;
        return cardsOnTheBoard;

    }
}
