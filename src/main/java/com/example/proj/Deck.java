package com.example.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    public Deck(List<Card> cards) {
        this.cards = cards;
    } //DECK CONSTRUCTOR
    public void shuffle() {
        Collections.shuffle(cards);
    } //shuffle the deck
    public void printDeck() {
        for (Card card : cards) {
            System.out.println(card);
        }
    } //METHOD TO PRINT THE DECK
    public Card drawCard(Player player) {
        if (cards.isEmpty()) { //CHECKING IF THE CARD IS MADE IN THE CORRECT WAY
            return null;
        }
        if(player.getPlayerCards().size()<3){
            Card drownCard = cards.remove(0); //REMOVING THE CHOSEN CARD FROM THE DECK
            player.getPlayerCards().add(drownCard); //ADDING THE CARD TO PLAYER DECK
            return drownCard;                          //RETURING THE CHOSEN CARD
        }
        else{
            throw new RuntimeException("Player's deck is already full");
        }
    }
    public void addCard(Card card) {
        cards.add(card);
    } //METHOD TO ADD THE CARD TO PLAYER LIST

    public ObjectiveCard firstCardForEachPlayer() {
        return null;
    }
}
