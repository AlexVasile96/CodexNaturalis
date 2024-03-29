package com.example.proj;

import java.util.Collections;
import java.util.List;

public class InitialCardDeck {
    private List<InitialCard> initialCards;


    public InitialCardDeck(List<InitialCard> cards) {

        this.initialCards = cards;
    }

    public void printDeck() {
        for (InitialCard card : initialCards) {
            System.out.println(card);
        }
    }

    public InitialCard firstCardForEachPlayer (Player player)
    {
        Collections.shuffle(initialCards);
        InitialCard drownCard = initialCards.remove(0);
        return drownCard;
    }
    public List<InitialCard> getInitialCards() {
        return initialCards;
    }


}
