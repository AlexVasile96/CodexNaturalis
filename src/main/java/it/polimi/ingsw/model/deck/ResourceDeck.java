package it.polimi.ingsw.model.deck;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ObjectiveCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceDeck implements Deck{
    private List<Card> resourceCards; //FINAL TO KEEP HER THREAD SAFE AND IT HAS TO BE INITIALED ONE TIME ONLY
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
    public synchronized Card drawCard(Player player) {  //DRAWING CARD FROM THE DECK
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
            throw new AlreadyThreeCardsException("Player already has 3 cards in his deck", e); // Eccezione specifica
        }
        return null;
    }
    public List<Card> drawCard(List<Card> well) { //METHOD TO PLACE CARDS ON THE WELL
        if (resourceCards.isEmpty()) {
            return null; //empty deck
        }
        try {
                Card drownCard = resourceCards.remove(0);
                well.add(drownCard);
                return well;
        } catch(Exception e) {
            throw new UnreachableWell("Coudn't place card in the well",e); // Eccezione specifica
        }
    }

    public void addCard(Card card) {
        //check if the deck already reached it's full capacity
        if (isDeckFull()) {
            throw new FullDeckException("Deck already has 40 cards");
        }

        // Check if the card belongs to the resource deck
        if (!isAResourceCard(card.getId())) {
            throw new IllegalAddException("Card doesn't belong to this deck");
        }

        // check if the card is already in the deck
        for (Card card2 : resourceCards) {
            if (alreadyInDeck(card.getId(), card2.getId())) throw new AlreadyInException("Card is already in the deck");
        }

        // try to add the card
        try {
            resourceCards.add(card);
        } catch(Exception e) {
            throw new UnknownWhyException("Can't add the card", e);
        }
    }
    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }

    //serve per il test
    public int leftCardINDeck(){
        return resourceCards.size();
    }

    private boolean isDeckFull() {
        return resourceCards.size() >= 40;
    }

    private boolean alreadyInDeck(int id1, int id2) {
        return id1==id2;
    }

    private boolean isAResourceCard(int idCard) {
        return (idCard <= 40 && idCard >0);
    }

    public synchronized String sendIdCardToGui(){
        return String.valueOf(resourceCards.getFirst().getId()); //I take the id of the resource card and give it to the GUI
    }
    public Card returnFirstCard(){
        return resourceCards.getFirst();
    }
    public synchronized void drawCardForGui ()
    {
        resourceCards.removeFirst();
        System.out.println(resourceCards.getFirst());
    }

    public JsonArray toJson() {
        JsonArray jsonArray = new JsonArray();
        for (Card card : resourceCards) {
            jsonArray.add(card.toJsonObject());
        }
        return jsonArray;
    }

    public static ResourceDeck fromJson(JsonArray jsonArray) {
        List<Card> cards = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            cards.add(ResourceCard.fromJsonObject(jsonObject));
        }
        return new ResourceDeck(cards);
    }
    public void setRemainingCards(List<Card> cards) {
        this.resourceCards = new ArrayList<>(cards);
    }
    public List<Card> getRemainingCards() {
        return new ArrayList<>(this.resourceCards); // Supponendo che `deck` sia una lista delle carte rimanenti
    }

    public synchronized void putCardOnTopOfDeck(int i) {
        for (Card r : resourceCards) {
            if (r.getId() == i) {
                resourceCards.remove(r);
                resourceCards.addFirst(r);
                break;
            }
        }
    }
}
