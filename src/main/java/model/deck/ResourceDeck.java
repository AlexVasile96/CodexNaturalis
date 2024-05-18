package model.deck;
import exceptions.*;
import model.game.Player;
import model.card.Card;
import model.card.ObjectiveCard;

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
    public List<Card> drawCard(List<Card> pozzo) { //METHOD TO PLACE CARDS ON THE POZZO
        if (resourceCards.isEmpty()) {
            return null; //empty deck
        }
        try {
                Card drownCard = resourceCards.remove(0);
                pozzo.add(drownCard);
                return pozzo;
        } catch(Exception e) {
            throw new PozzoUnrechableExeption("Coudn't place card in the well",e); // Eccezione specifica
        }
    }

    public void addCard(Card card) {
        // Verifica se il mazzo ha già raggiunto la capacità massima
        if (isDeckFull()) {
            throw new FullDeckExeption("Deck already has 40 cards");
        }

        // Verifico che la carta appartiene al mazzo Resource
        if (!isAResourceCard(card.getId())) {
            throw new IllegalAddException("Card doesn't belong to this deck");
        }

        // Verifica se la carta è già presente nel mazzo
        for (Card card2 : resourceCards) {
            if (alreadyInDeck(card.getId(), card2.getId())) throw new AlredyInException("Card is already in the deck");
        }

        // provo ad aggiungere la carta
        try {
            resourceCards.add(card);
        } catch(Exception e) {
            throw new UknownWhyException("Can't add the card", e);
        }
    }
    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }

    //serve per il test
    public int carteRimaste(){
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
}
