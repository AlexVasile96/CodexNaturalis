package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.InitialCard;
import it.polimi.ingsw.model.card.ObjectiveCard;

import java.util.Collections;
import java.util.List;

public class InitialCardDeck implements Deck {
    private final List<Card> initialCards;
    public InitialCardDeck(List<Card> cards) {
        this.initialCards = cards;
    }
    @Override
    public void shuffle() {
    }
    @Override
    public Card drawCard(Player player) {
        return null;
    }
    @Override
    public void addCard(Card card) {
    }
    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }

    @Override
    public List<Card> drawCard(List<Card> well) {
        return null;
    }

    public InitialCard firstCardForPlayer(Player player)
    {
        Collections.shuffle(initialCards);
        return (InitialCard) initialCards.removeFirst();
    }
    public InitialCard firstCardInitialGame(){
        Collections.shuffle(initialCards);
        return (InitialCard) initialCards.removeFirst();
    }

    //needed for testing
    public int remainingCards(){
        return initialCards.size();
    }
}
