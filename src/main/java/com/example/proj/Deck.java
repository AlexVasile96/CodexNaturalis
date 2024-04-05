package com.example.proj;


public interface Deck {
   void shuffle();
   void printDeck();
   Card drawCard(Player player);
   void addCard(Card card);
   ObjectiveCard firstCardForEachPlayer ();
}

