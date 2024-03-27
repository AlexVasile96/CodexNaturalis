package com.example.proj;

import java.util.Collections;
import java.util.List;

public class GoldDeck extends Deck {
    private List<Card> goldCards;

    public GoldDeck(List<Card> cards) {
        super(cards);
    }

    public void shuffle() {
        Collections.shuffle(goldCards);
    }

    public void printDeck() {
        for (Card card : goldCards) {
            System.out.println(goldCards);
        }
    }

    public Card drawCard() {
        if (goldCards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        return goldCards.remove(0);
    }

    public void addCard(Card card) {
        goldCards.add(card);
    }
}
