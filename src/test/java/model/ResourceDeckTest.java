package model;

import it.polimi.ingsw.exceptions.AlreadyInException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceDeckTest {
    private Player player;
    Board board;
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor(); //create resource cards
    GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();
    ResourceCardConstructor resourceCardConstructor2 = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck2 = (ResourceDeck) resourceCardConstructor2.createCards();


    @BeforeEach
    void setUp() {
        //CardConstructor resourceCardConstructor = new ResourceCardConstructor();
        board = new Board(50,50);
        player = new Player("Momo",0, Dot.GREEN,board);
    }

    @Test //Checking that player can have max 3 cards in hand and that if he chooses to draw from the resource deck
    //the number of resources cards left in the resource deck is decremented
    void drawCardPlayer() {
        for(int i = 0; i <5; i++){
            resourceDeck.drawCard(player);
        }
        assertEquals(3, player.getPlayerCards().size());
        assertEquals(37, resourceDeck.leftCardINDeck());
    }

    @Test //Test del metodo che aggiunge una carta risorse dal deck carte risorse alle carte del pozzo
    void fromResourceDeckToWellCards() {
        List<Card> well= new ArrayList<>();
        resourceDeck.drawCard(well);
        assertEquals(1, well.size());
        assertEquals(39, resourceDeck.leftCardINDeck());

    }

    @Test
    void addCard() {
        Player player2 = new Player("Player1",0,Dot.GREEN,board);
        Card duplicateCard = resourceDeck.drawCard(player2);

        //trying to insert a gold card in the resource deck
        Card illegalCard = goldDeck.drawCard(player);
        assertThrows(IllegalAddException.class, () -> resourceDeck.addCard(illegalCard));

        //trying to insert a duplicate
        Card cartaBomba3 = resourceDeck.drawCard(player2);
        resourceDeck.addCard(duplicateCard);
        assertThrows(AlreadyInException.class, () -> resourceDeck.addCard(duplicateCard));

    }
}