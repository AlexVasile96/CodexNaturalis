package model;


import java.util.List;

public interface Deck {
   void shuffle();
   void printDeck();
   Card drawCard(Player player);
   void addCard(Card card);
   ObjectiveCard firstCardForEachPlayer ();
   List<Card> drawCard(List<Card> pozzo);
}

