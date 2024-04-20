package model.deck;


import model.game.Player;
import model.card.Card;
import model.card.ObjectiveCard;

import java.util.List;

public interface Deck {
   void shuffle();
   void printDeck();
   Card drawCard(Player player);
   void addCard(Card card);
   ObjectiveCard firstCardForEachPlayer ();
   List<Card> drawCard(List<Card> pozzo);
}

