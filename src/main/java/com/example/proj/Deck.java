package com.example.proj;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void printDeck() {
        for (Card card : cards) {
            System.out.println(card);
        }
    }
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        return cards.remove(0);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
