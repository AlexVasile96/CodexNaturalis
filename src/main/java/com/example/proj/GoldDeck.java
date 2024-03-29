package com.example.proj;

import java.util.Collections;
import java.util.List;

public class GoldDeck extends Deck {
    private List<Card> goldCards;

    public GoldDeck(List<Card> cards) {
        super(cards);
        this.goldCards=cards;
    }

    public void shuffle() {
        Collections.shuffle(goldCards);
    }

    public void printDeck() {
        for (Card card : goldCards) {
            System.out.println(card);
        }
    }


    public Card drawCard(Player player) {
        if (goldCards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        if(player.getPlayerCards().size()<3) {
            Card drownCard = goldCards.remove(0);
            player.getPlayerCards().add(drownCard);
            return drownCard;
        }
        else{
            throw new RuntimeException();
        }
    }

    public void addCard(Card card) {
        goldCards.add(card);
    }
}
