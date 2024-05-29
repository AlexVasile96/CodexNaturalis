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
    public void printDeck() {
        for (Card card : initialCards) {
            System.out.println(card);
        }
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
    public List<Card> drawCard(List<Card> pozzo) {
        return null;
    }

    public InitialCard firstCardForPlayer(Player player)
    {
        Collections.shuffle(initialCards);
        InitialCard drownCard = (InitialCard) initialCards.remove(0);
        return drownCard;
    }
    public InitialCard firstCardInitialGame(){
        Collections.shuffle(initialCards);
        InitialCard drownCard = (InitialCard) initialCards.remove(0);
        return drownCard;
    }
    public List<Card> getInitialCards() {
        return initialCards;
    }

    //needed for testing
    public int remainingCards(){
        return initialCards.size();
    }
}
