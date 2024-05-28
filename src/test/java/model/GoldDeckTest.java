package model;

import it.polimi.ingsw.exceptions.AlredyInException;
import it.polimi.ingsw.exceptions.IllegalAddException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GoldCardConstructor;
import it.polimi.ingsw.model.card.ResourceCardConstructor;
import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Dot;
import it.polimi.ingsw.model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoldDeckTest {

    private Player player,player2;
    Board board;
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor(); //create resource cards
    GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();

    @BeforeEach
    void setUp() {
        board = new Board(50,50);
        player = new Player("Player1",0, Dot.GREEN,board);
        player2 = new Player("Player2",0,Dot.GREEN,board);

    }

    @Test //Testing the method that allows the player to draw from the well

    void cannotDrawMoreThanThree() {
        System.out.println(goldDeck.cardLefInDeck());//Testing max num of cards that the player can have in his hand
        for(int i = 0; i <5; i++){
            player.drawGoldCard(goldDeck);
        }
        assertEquals(3, player.getPlayerCards().size());
    }

    @Test
        // Testing that when a player draws a gold card from well another one takes its place
    void drawFromWell() {
        List<Card> pozzo= new ArrayList<>();
        goldDeck.drawCard(pozzo);
        assertEquals(1, pozzo.size());
        assertEquals(39, goldDeck.cardLefInDeck());
    }

    @Test
    void addCard() {
        Player player2 = new Player("Player2",0,Dot.GREEN,board);
        Card card1 = goldDeck.drawCard(player2);

        //trying to insert a card from another deck
        Card card2 = resourceDeck.drawCard(player);
        assertThrows(IllegalAddException.class, () -> goldDeck.addCard(card2), "La carta non appartiene al mazzo gold");

        //try to insert a duplicate
        Card cartaBomba3 = goldDeck.drawCard(player2);
        goldDeck.addCard(card1);
        assertThrows(AlredyInException.class, () -> goldDeck.addCard(card1));

    }
}