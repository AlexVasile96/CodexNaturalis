package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.exceptions.AlreadyThreeCardsException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.exceptions.UnknownWhyException;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ObjectiveCard;

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

    public ObjectiveCard firstCardForEachPlayer () {
        if (objectiveCards.isEmpty()) {
            throw new EmptyDeckException("Objective Deck is empty");
        }
        try {
            return (ObjectiveCard) objectiveCards.removeFirst(); //return drownCard;
        } catch (Exception e){
            throw new UnknownWhyException("Operation Failed", e);
        }
    }

    @Override
    public List<Card> drawCard(List<Card> well) {
        return null;
    }

    public ObjectiveCard drawCard(Player player){
        return (ObjectiveCard) objectiveCards.removeFirst(); //return drownCard;
    }

    public synchronized ObjectiveCard drawObjectiveCard(){
        if (objectiveCards.isEmpty()) { //CHECKING IF THE CARD IS MADE IN THE CORRECT WAY
            return null;
        }
        try {
            return (ObjectiveCard)objectiveCards.removeFirst();
        } catch(Exception e) {
            throw new AlreadyThreeCardsException("Operation Failed",e); // Specific Exception
        }
    }

    @Override
    public void addCard(Card card) {

    }

    //serve per il test
    public int remainingCards(){
        return objectiveCards.size();
    }

    public synchronized void putCardOnTopOfDeck(int i) {
        for (Card r : objectiveCards) {
            if (r.getId() == i) {
                objectiveCards.remove(r);
                objectiveCards.addFirst(r);
                break;
            }
        }
    }
}
