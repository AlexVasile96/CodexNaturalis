package com.example.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    } //shuffle the deck

    public void printDeck() {
        for (Card card : cards) {
            System.out.println(card);
        }
    }
    public Card drawCard(Player player) {
        if (cards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        if(player.getPlayerCards().size()<3){
            Card drownCard = cards.remove(0);
            player.getPlayerCards().add(drownCard);
            return drownCard;
        }
        else{
            throw new RuntimeException();
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

}
