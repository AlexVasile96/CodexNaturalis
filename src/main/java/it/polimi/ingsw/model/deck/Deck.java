package it.polimi.ingsw.model.deck;


import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ObjectiveCard;

import java.util.List;

public interface Deck {
   void shuffle();
   void printDeck();
   Card drawCard(Player player);
   void addCard(Card card);
   ObjectiveCard firstCardForEachPlayer ();
   List<Card> drawCard(List<Card> pozzo);
}

