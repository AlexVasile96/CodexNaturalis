package model;
import java.util.Collections;
import java.util.List;

public class ResourceDeck implements Deck{
    private final List<Card> resourceCards; //FINAL TO KEEP HER THREAD SAFE AND IT HAS TO BE INITIALED ONE TIME ONLY
    public ResourceDeck(List<Card> cards) {
        this.resourceCards = cards;
    }
    public void shuffle() {
        Collections.shuffle(resourceCards);
    }
    public void printDeck() {
        for (Card card : resourceCards) {
            System.out.println(card);
        }
    }
    @Override
    public Card drawCard(Player player) {  //DRAWING CARD FROM THE DECK
        if (resourceCards.isEmpty()) {
            return null; //empty deck
        }
        try {
            if (player.getPlayerCards().size() < 3) {
                Card drownCard = resourceCards.remove(0);
                player.getPlayerCards().add(drownCard);
                return drownCard;
            }
        } catch(Exception e) {
            throw new IllegalStateException("Il giocatore ha già tre carte nella mano."); // Eccezione specifica
        }
        return null;
    }
    public List<Card> drawCard(List<Card> pozzo) { //METHOD TO PLACE CARDS ON THE POZZO
        if (resourceCards.isEmpty()) {
            return null; //empty deck
        }
        try {
                Card drownCard = resourceCards.remove(0);
                pozzo.add(drownCard);
                return pozzo;
        } catch(Exception e) {
            throw new IllegalStateException("Il giocatore ha già tre carte nella mano."); // Eccezione specifica
        }
    }

    public void addCard(Card card) {
        resourceCards.add(card);
    }
    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }
}
