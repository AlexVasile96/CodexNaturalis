package com.example.proj;

import java.util.Collections;
import java.util.List;

public class InitialCardDeck extends Deck {
    private List<Card> initialCards;

    public InitialCardDeck(List<Card> cards) {
        super(cards);
        this.initialCards = cards;
    }

    public void printDeck() {
        for (Card card : initialCards) {
            System.out.println(card);
        }
    }

    public InitialCard firstCardForEachPlayer (Player player)
    {
        Collections.shuffle(initialCards);
        InitialCard drownCard = (InitialCard) initialCards.remove(0);
        return drownCard;
    }

    public List<Card> getInitialCards() {
        return initialCards;
    }
}
