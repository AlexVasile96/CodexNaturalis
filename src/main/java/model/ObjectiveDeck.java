package model;
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
        return (ObjectiveCard) objectiveCards.remove(0); //ObjectiveCard drownCard = (ObjectiveCard) objectiveCards.remove(0);
                                                                //return drownCard;
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
            throw new IllegalStateException("Il giocatore ha già tre carte nella mano."); // Eccezione specifica
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
