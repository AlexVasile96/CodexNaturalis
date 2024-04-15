package model;
import Exceptions.*;

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
            throw new AlreadyThreeCardsException("Il giocatore ha già tre carte nella mano.", e); // Eccezione specifica
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
            throw new PozzoUnrechableExeption("Coudn't place card in the pozzo",e); // Eccezione specifica
        }
    }

    public void addCard(Card card) {
        // Verifica se il mazzo ha già raggiunto la capacità massima
        if (resourceCards.size() >= 40) {
            throw new FullDeckExeption("Il mazzo ha raggiunto la capacità massima di 40 carte.");
        }

        // Verifico che la carta appartiene al mazzo Resource
        if (card.getId() > 40) {
            throw new IllegalAddException("La carta non appartiene al mazzo Resource.");
        }

        // Verifica se la carta è già presente nel mazzo
        for (Card card2 : resourceCards) {
            if (card.id == card2.id) throw new AlredyInException("La carta è già presente nel mazzo.");
        }

        // provo ad aggiungere la carta
        try {
            resourceCards.add(card);
        } catch(Exception e) {
            throw new UknownWhyException("Non riesco ad aggiungere la carta.", e);
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
}
