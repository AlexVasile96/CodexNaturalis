package com.example.proj;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeck {
    private List<ObjectiveCard> objectiveCards;

    public ObjectiveDeck(List<ObjectiveCard> cards) {

        this.objectiveCards = cards;
    }

    public void printDeck() {
        for (ObjectiveCard card : objectiveCards) {
            System.out.println(card);
        }
    }

    public ObjectiveCard firstCardForEachPlayer (Player player)
    {
        Collections.shuffle(objectiveCards);
        ObjectiveCard drownCard = objectiveCards.remove(0);
        return drownCard;
    }
    public List<ObjectiveCard> getInitialCards() {
        return objectiveCards;
    }



}
