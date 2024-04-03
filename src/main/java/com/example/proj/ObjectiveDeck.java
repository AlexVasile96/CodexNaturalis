package com.example.proj;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeck extends Deck {
    private List<Card> objectiveCards;

    public ObjectiveDeck(List<Card> cards) {
        super(cards);
        this.objectiveCards = cards;
    }

    public void printDeck() {
        for (Card card : objectiveCards) {
            System.out.println(card);
        }
    }

    public ObjectiveCard firstCardForEachPlayer ()
    {
        ObjectiveCard drownCard = (ObjectiveCard) objectiveCards.remove(0);
        return drownCard;
    }
    public List<Card> getInitialCards() {
        return objectiveCards;
    }
    public List<Card> getObjectiveCards() {
        return objectiveCards;
    }
    public void setObjectiveCards(List<Card> objectiveCards) {
        this.objectiveCards = objectiveCards;
    }
}
