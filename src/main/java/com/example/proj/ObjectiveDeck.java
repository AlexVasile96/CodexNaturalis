package com.example.proj;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeck implements Deck {
    private List<Card> objectiveCards;

    public ObjectiveDeck(List<Card> cards) {
        this.objectiveCards = cards;
    }

    @Override
    public void shuffle() {

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
    public ObjectiveCard drawCard(Player player){
        if (objectiveCards.isEmpty()) { //CHECKING IF THE CARD IS MADE IN THE CORRECT WAY
            return null;
        }
        if(player.getPlayerCards().size()<3){
            ObjectiveCard drownCard = (ObjectiveCard)objectiveCards.remove(0); //REMOVING THE CHOSEN CARD FROM THE DECK
            return drownCard;                          //RETURING THE CHOSEN CARD
        }
        else{
            throw new RuntimeException("Player's deck is already full");
        }
    }

    @Override
    public void addCard(Card card) {

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
