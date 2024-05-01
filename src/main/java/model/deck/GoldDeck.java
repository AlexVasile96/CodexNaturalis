package model.deck;
import exceptions.AlredyInException;
import exceptions.FullDeckExeption;
import exceptions.IllegalAddException;
import exceptions.UknownWhyException;
import model.game.Player;
import model.card.Card;
import model.card.ObjectiveCard;

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
        try {
            if(player.getPlayerCards().size()<3) {
                Card drownCard = goldCards.remove(0);
                player.getPlayerCards().add(drownCard);
                return drownCard;
            }
        } catch(Exception e) {
            throw new IllegalStateException("problema nel blocco try di drawCard nella classe GoldDeck"); // Eccezione specifica
        }
        return null;
    }
    public void addCard(Card card) {
        // Verifica se il mazzo ha già raggiunto la capacità massima
        if(isDeckFull()){
            throw new FullDeckExeption("Il mazzo ha raggiunto la capacità massima di 40 carte.");
        }

        // Verifico che la carta appartiene al mazzo Resource
        if(!isAGoldCard(card.getId())){
            throw new IllegalAddException("La carta non appartiene al mazzo Resource.");
        }

        // Verifica se la carta è già presente nel mazzo
        for (Card card2 : goldCards) {
            if (alreadyInDeck(card.getId(),card2.getId())) throw new AlredyInException("La carta è già presente nel mazzo.");
        }

        // provo ad aggiungere la carta
        try {
            goldCards.add(card);
        } catch(Exception e) {
            throw new UknownWhyException("Operazione 'addCard' non riuscita.", e);
        }
    }



    @Override
    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }
    @Override
    public List<Card> drawCard(List<Card> pozzo) {
        if (goldCards.isEmpty()) {
            return null; //empty deck
        }
        try {
            Card drownCard = goldCards.remove(0);
            pozzo.add(drownCard);
            return pozzo;
        } catch(Exception e) {
            throw new IllegalStateException("Il giocatore ha già tre carte nella mano."); // Eccezione specifica
        }
    }
    public int carteRimaste(){
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

}
