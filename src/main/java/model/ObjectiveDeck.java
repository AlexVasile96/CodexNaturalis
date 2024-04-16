package model;
import Exceptions.AlreadyThreeCardsException;
import Exceptions.EmptyDeckException;
import Exceptions.UknownWhyException;

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
            throw new EmptyDeckException("Deck obbiettivo vuoto");
        }
        try {
            //ObjectiveCard drownCard = (ObjectiveCard) objectiveCards.remove(0);
            return (ObjectiveCard) objectiveCards.remove(0); //return drownCard;
        } catch (Exception e){
            throw new UknownWhyException("Operazione di estrazione di Objective Card non riuscita", e);
        }
    }

    @Override
    public List<Card> drawCard(List<Card> pozzo) {
        return null;
    }

    public ObjectiveCard drawCard(Player player){
        if (objectiveCards.isEmpty()) { //CHECKING IF THE CARD IS MADE IN THE CORRECT WAY
            return null;
        }

        try {
            if(player.getPlayerCards().size()<3){
                return (ObjectiveCard)objectiveCards.remove(0);                          //RETURING THE CHOSEN CARD
            }
        } catch(Exception e) {
            throw new AlreadyThreeCardsException("Il giocatore ha già tre carte nella mano.",e); // Eccezione specifica
        }
        return null;
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
    public int carteRimaste(){
        return objectiveCards.size();
    }
}
