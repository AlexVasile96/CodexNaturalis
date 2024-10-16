package model;

import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.card.GoldCardConstructor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.ObjectiveCardConstructor;
import it.polimi.ingsw.model.card.ResourceCardConstructor;
import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ObjectiveDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Dot;
import it.polimi.ingsw.model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
TEST INUTILE -> LO CANCELLIAMO
 */


class ObjectiveDeckTest {

    private Player player,player2;
    Board board;
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor(); //create gold cards
    GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();
    ObjectiveCardConstructor objectiveCardConstructor= new ObjectiveCardConstructor(); //CREATING OBJECTIVE CARDS
    ObjectiveDeck objectiveDeck= (ObjectiveDeck) objectiveCardConstructor.createCards(); //creating deck for objective cards

    @BeforeEach
    void setUp() {
        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        board = new Board(50,50);
        player = new Player("Player1",0, Dot.GREEN,board);
        player2 = new Player("Player2",0,Dot.GREEN,board);
    }

    @Test
    void firstCardForEachPlayer() {
        ObjectiveCard firstCommonObjective= objectiveDeck.firstCardForEachPlayer();
        assertEquals(15, objectiveDeck.remainingCards());

        for(int i =0; i<15; i++){
            firstCommonObjective= objectiveDeck.firstCardForEachPlayer();
        }
        assertThrows(EmptyDeckException.class, ()->objectiveDeck.firstCardForEachPlayer(), "già estratto tutte le carte");
    }

    @Test
    void drawCardPozzo() {
        //NULLA DA FARE
    }

    //CHE SENSO HA?? IL GIOCATORE NON PUò PESCARE DA QUESTO MAZZO DURANTE IL GIOCO!!
    @Test
    void DrawCardPlayer() {
        objectiveDeck.drawCard(player);
        assertEquals(15, objectiveDeck.remainingCards());
        objectiveDeck.drawCard(player);
        objectiveDeck.drawCard(player);
        objectiveDeck.drawCard(player);
        assertEquals(12, objectiveDeck.remainingCards());
    }

    //DA IMPLEMENTARE
    @Test
    void addCard() {
    }
}