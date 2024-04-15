package model;
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
    public List<Card> getInitialCards() {
        return initialCards;
    }
}
