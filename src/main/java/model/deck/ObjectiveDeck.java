package model.deck;
import exceptions.AlreadyThreeCardsException;
import exceptions.EmptyDeckException;
import exceptions.UknownWhyException;
import model.game.Player;
import model.card.Card;
import model.card.ObjectiveCard;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeck implements Deck {
    private List<Card> objectiveCards;
    public ObjectiveDeck(List<Card> cards) {
        this.objectiveCards = cards;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(objectiveCards);
    }

    public void printDeck() {
        for (Card card : objectiveCards) {
            System.out.println(card);
        }
    }

    public ObjectiveCard firstCardForEachPlayer () {
        if (objectiveCards.isEmpty()) {
            throw new EmptyDeckException("Objective Deck is empty");
        }
        try {
            //ObjectiveCard drownCard = (ObjectiveCard) objectiveCards.remove(0);
            return (ObjectiveCard) objectiveCards.remove(0); //return drownCard;
        } catch (Exception e){
            throw new UknownWhyException("Operation Failed", e);
        }
    }

    @Override
    public List<Card> drawCard(List<Card> pozzo) {
        return null;
    }

    public ObjectiveCard drawCard(Player player){
        return (ObjectiveCard) objectiveCards.remove(0); //return drownCard;
    }

    public synchronized ObjectiveCard drawObjectiveCard(){
        if (objectiveCards.isEmpty()) { //CHECKING IF THE CARD IS MADE IN THE CORRECT WAY
            return null;
        }
        try {
            return (ObjectiveCard)objectiveCards.remove(0);
        } catch(Exception e) {
            throw new AlreadyThreeCardsException("Operation Failed",e); // Eccezione specifica
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

    //serve per il test
    public int remainingCards(){
        return objectiveCards.size();
    }
}
