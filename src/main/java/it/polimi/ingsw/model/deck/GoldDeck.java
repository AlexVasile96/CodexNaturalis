package it.polimi.ingsw.model.deck;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.exceptions.AlreadyInException;
import it.polimi.ingsw.exceptions.FullDeckException;
import it.polimi.ingsw.exceptions.IllegalAddException;
import it.polimi.ingsw.exceptions.UnknownWhyException;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ObjectiveCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoldDeck implements Deck {
    private List<Card> goldCards;
    public GoldDeck(List<Card> cards) {
        this.goldCards=cards;
    }
    public void shuffle() {
        Collections.shuffle(goldCards);
    } //METHOD TO SHUFFLE THE CARDS
    public synchronized Card drawCard(Player player) {
        if (goldCards.isEmpty()) {
            return null;
        }
        try {
            if(player.getPlayerCards().size()<3) {
                Card drownCard = goldCards.removeFirst();
                player.getPlayerCards().add(drownCard);
                return drownCard;
            }
        } catch(Exception e) {
            throw new IllegalStateException("Something went wrong in the try-catch statement in GoldDeck class"); //Specific exception
        }
        return null;
    }
    public void addCard(Card card) {
        if(isDeckFull()){
            throw new FullDeckException("Deck is full");
        }

        // Check if the card belongs to the gold deck
        if(!isAGoldCard(card.getId())){
            throw new IllegalAddException("The Card doesn't belong to ResourceDeck");
        }

        // Check if card is already in the deck
        for (Card card2 : goldCards) {
            if (alreadyInDeck(card.getId(),card2.getId())) throw new AlreadyInException("Card is already present in the deck");
        }

        // try to add the card
        try {
            goldCards.add(card);
        } catch(Exception e) {
            throw new UnknownWhyException("AddCard failed", e);
        }
    }

    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }
    @Override
    public synchronized List<Card> drawCard(List<Card> well) {
        if (goldCards.isEmpty()) {
            return null; //empty deck
        }
        try {
            Card drownCard = goldCards.removeFirst();
            well.add(drownCard);
            return well;
        } catch(Exception e) {
            throw new IllegalStateException("Player already has 3 card in his deck"); // Specific Exception
        }
    }
    public int cardLefInDeck(){
        return goldCards.size();
    }

    private boolean isDeckFull() {
        return goldCards.size() >= 40;
    }

    private boolean alreadyInDeck(int id1, int id2) {
        return id1==id2;
    }

    private boolean isAGoldCard(int idCard) {
        return (idCard > 40 && idCard <= 80);
    }

    public synchronized String sendIdCardToGui(){
        return String.valueOf(goldCards.getFirst().getId()); //I take the id of the resource card and give it to the GUI
    }
    public synchronized void drawCardForGoldGui ()
    {
        goldCards.removeFirst();
        System.out.println(goldCards.getFirst());
    }
    public List<Card> getRemainingCards() {
        return new ArrayList<>(this.goldCards); // Assuming that 'deck' is the list of the remaining cards
    }
    public JsonArray toJson() {
        JsonArray jsonArray = new JsonArray();
        for (Card card : goldCards) {
            jsonArray.add(card.toJsonObject());
        }
        return jsonArray;
    }

    public static GoldDeck fromJson(JsonArray jsonArray) {
        List<Card> cards = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            cards.add(GoldCard.fromJson(jsonObject));
        }
        return new GoldDeck(cards);
    }

}
