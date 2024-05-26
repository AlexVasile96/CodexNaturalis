package model;

import model.card.*;
import model.deck.*;
import model.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Player player;
    private InitCardConstructor initCardConstructor= new InitCardConstructor();
    private ObjectiveCardConstructor objectiveCardConstructor = new ObjectiveCardConstructor();
    private ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor();
    private GoldCardConstructor goldCardConstructor = new GoldCardConstructor();

    @BeforeEach
    void setUp() {
        board = new Board(50, 50);
        player = new Player("Player1", 0, Dot.YELLOW, board);
    }

    @Test
    void getCentralCoordinates() {
        int[][] centralCoordinates;
        centralCoordinates= board.getCentralCoordinates();
        assertEquals(24, centralCoordinates[0][0], "TOP LEFT");
        assertEquals(24, centralCoordinates[0][1], "TOP RIGHT");
        assertEquals(25, centralCoordinates[1][0], "BOTTOM LEFT");
        assertEquals(25, centralCoordinates[1][1], "BOTTOM RIGHT");
    }

    /*
    Testing if the gold card (which is theoretically placeable) is actually peaceable
     */
    @Test
    void placeGoldCard() {

        // placing the necessary specific seeds on the board -> should allow the placement of the gold card
        board.getNode(0, 0).setSpecificNodeSeed(SpecificSeed.MUSHROOM);
        board.getNode(0, 1).setSpecificNodeSeed(SpecificSeed.MUSHROOM);
        board.getNode(1, 0).setSpecificNodeSeed(SpecificSeed.ANIMAL);
        board.getNode(1, 1).setSpecificNodeSeed(SpecificSeed.NOTTOBEPLACEDON);


        assertDoesNotThrow(() -> board.placeGoldCard( List.of(SpecificSeed.MUSHROOM, SpecificSeed.MUSHROOM, SpecificSeed.MUSHROOM)));
    }

    @Test
    void placeInitialCard() {
        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);

        //Init card should be placeable
        assertTrue(board.placeInitialCard(initialCard));

        // Attempt to place a second initial card
        InitialCard initialCard2 = initialCardDeck.firstCardForPlayer(player);
        assertFalse(board.placeInitialCard(initialCard2));
    }

    @Test
    void testCreateSpecificSecretCardWithStairsObjective() {
        Deck objectiveDeck = objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //stairs card are id: 87,88,89,90
        ObjectiveCard stairsCard87 =objectiveDeck.firstCardForEachPlayer();


        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);

        //assertEquals(1,player.getPlayerCards().getFirst().getId());
        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "TL");
        //assertEquals(2,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TR");
        //assertEquals(3,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "BL");

        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(stairsCard87, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);
    }

    @Test
    void testCreateSpecificSecretCardWithLObjective() {
        Deck objectiveDeck = objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //stairs card are id: 87,88,89,90
        objectiveDeck.firstCardForEachPlayer();
        objectiveDeck.firstCardForEachPlayer();
        objectiveDeck.firstCardForEachPlayer();
        objectiveDeck.firstCardForEachPlayer();
        ObjectiveCard ElleCard91 =objectiveDeck.firstCardForEachPlayer(); //carta REQUISITO L (MUSSHROOM, MUSHROOM, PLANT)



        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);
        resourceDeck.putCardOnTopOfDeck(11);
        player.drawResourceCard(resourceDeck);

        //assertEquals(1,player.getPlayerCards().getFirst().getId());//CARTA ID 1
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "TR");
        //assertEquals(2,player.getPlayerCards().getFirst().getId());
        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "BR");
        assertEquals(11,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "BR");

        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(ElleCard91, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);
    }

    /*@Test
    void testCreateSpecificSecretCardWithMixObjective() {
        //ObjectiveCard mixCard = new ObjectiveCard("MIX", "type3");
        int initialScore = player.getPlayerScore();

    }*/
}