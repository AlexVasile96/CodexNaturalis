package model;
import java.util.Collections;
import java.util.List;

public class GoldDeck implements Deck {
    private final List<Card> goldCards;
    public GoldDeck(List<Card> cards) {
        this.goldCards=cards;
    }
    public void shuffle() {
        Collections.shuffle(goldCards);
    } //METHOD TO SHUFFLE THE CARDS
    public void printDeck() {
        for (Card card : goldCards) {
            System.out.println(card);
        }
    }
    public Card drawCard(Player player) {
        if (goldCards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        if(player.getPlayerCards().size()<3) {
            Card drownCard = goldCards.remove(0);
            player.getPlayerCards().add(drownCard);
            return drownCard;
        }
        else{
            throw new RuntimeException();
        }
    }
    public void addCard(Card card) {
        goldCards.add(card);
    }
    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }
}
