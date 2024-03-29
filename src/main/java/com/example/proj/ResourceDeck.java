package com.example.proj;


import java.util.Collections;
import java.util.List;

public class ResourceDeck extends Deck{
    private List<Card> resourceCards;

    public ResourceDeck(List<Card> cards) {
        super(cards);
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
    public Card drawCard(Player player) {
        if (resourceCards.isEmpty()) {
            return null; // Mazzo vuoto
        }
        if(player.getPlayerCards().size()<3) {
            Card drownCard = resourceCards.remove(0);
            player.getPlayerCards().add(drownCard);
            return drownCard;
        }
        else{
            throw new RuntimeException();
        }
    }


    public void addCard(Card card) {
        resourceCards.add(card);
    }
}
