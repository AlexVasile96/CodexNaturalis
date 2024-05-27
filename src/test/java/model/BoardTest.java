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
    //private GoldCardConstructor goldCardConstructor = new GoldCardConstructor();

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
    void generalStairsTest() {
        // Setup iniziale
        Deck objectiveDeck = objectiveCardConstructor.createCards(); // non mischiato
        System.out.println("Objective Deck created: " + objectiveDeck);

        InitialCardDeck initialCardDeck = (InitialCardDeck) initCardConstructor.createCards();

        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);
        System.out.println("Initial card placed: " + initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards(); // non mischiato
        System.out.println("Resource Deck created: " + resourceDeck);

        ObjectiveCard stairsCard87 = objectiveDeck.firstCardForEachPlayer();
        System.out.println("Stairs card selected: " + stairsCard87);

        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);

        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        System.out.println("Player chose card: " + cardPlayerChoose);
        player.playCard(board, 0, 0, player.getPlayerCards().getFirst(), initialCard, "TL");

        player.playCard(board, 0, 1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TR");
        player.playCard(board, 0, 1, player.getPlayerCards().getFirst(), cardPlayerChoose, "BL");

        board.printBoard();

        int initialScore = player.getPlayerScore();
        System.out.println("Initial player score: " + initialScore);

        board.createSpecificSecretCard(stairsCard87, player);
        int newScore = player.getPlayerScore();
        System.out.println("New player score after secret card: " + newScore);

        assertEquals(newScore, initialScore + 2);
    }

    @Test
    void testMushroomMushroomInsect() {
        // Setup iniziale
        Deck objectiveDeck = objectiveCardConstructor.createCards(); // non mischiato
        System.out.println("Objective Deck created: " + objectiveDeck);

        InitialCardDeck initialCardDeck = (InitialCardDeck) initCardConstructor.createCards();

        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);
        System.out.println("Initial card placed: " + initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards(); // non mischiato
        System.out.println("Resource Deck created: " + resourceDeck);

        player.drawResourceCard(resourceDeck); // ID 1
        assertEquals(1,player.getPlayerCards().getFirst().getId());
        player.drawResourceCard(resourceDeck); // ID 2
        resourceDeck.putCardOnTopOfDeck(31); // insetto
        player.drawResourceCard(resourceDeck); // ID 31

        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        System.out.println("Player chose card: " + cardPlayerChoose);
        player.playCard(board, 0, 0, player.getPlayerCards().getFirst(), initialCard, "BR");

        player.playCard(board, 0, 4, player.getPlayerCards().getFirst(), cardPlayerChoose, "TR");
        player.playCard(board, 0, 4, player.getPlayerCards().getFirst(), cardPlayerChoose, "BL");

        board.printBoard();

        int initialScore = player.getPlayerScore();
        System.out.println("Initial player score: " + initialScore);

        ObjectiveCard stairsCard87 = objectiveDeck.firstCardForEachPlayer();
        System.out.println("Stairs card selected: " + stairsCard87);
        board.createSpecificSecretCard(stairsCard87, player);
        int newScore = player.getPlayerScore();
        System.out.println("New player score after secret card: " + newScore);

        assertEquals(newScore, initialScore);
    }

    @Test
    void test4MushroomsInStairs() {
        // Setup iniziale
        Deck objectiveDeck = objectiveCardConstructor.createCards(); // non mischiato
        System.out.println("Objective Deck created: " + objectiveDeck);

        InitialCardDeck initialCardDeck = (InitialCardDeck) initCardConstructor.createCards();

        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);
        System.out.println("Initial card placed: " + initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards(); // non mischiato
        System.out.println("Resource Deck created: " + resourceDeck);

        player.drawResourceCard(resourceDeck); // ID 1
        assertEquals(1,player.getPlayerCards().getFirst().getId());
        player.drawResourceCard(resourceDeck); // ID 2
        player.drawResourceCard(resourceDeck); // ID 3

        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        System.out.println("Player chose card: " + cardPlayerChoose);
        player.playCard(board, 0, 0, player.getPlayerCards().getFirst(), initialCard, "BR");

        player.playCard(board, 0, 1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TR");
        Card cardPlayerChooseTwo = player.getPlayerCards().getFirst();
        player.playCard(board, 0, 2, player.getPlayerCards().getFirst(), cardPlayerChoose, "BL");


        player.drawResourceCard(resourceDeck); // ID 4
        player.playCard(board, 0, 3, player.getPlayerCards().getFirst(), cardPlayerChooseTwo, "BL");

        board.printBoard();

        int initialScore = player.getPlayerScore();
        System.out.println("Initial player score: " + initialScore);

        ObjectiveCard stairsCard87 = objectiveDeck.firstCardForEachPlayer();
        System.out.println("Stairs card selected: " +  stairsCard87);
        board.createSpecificSecretCard(stairsCard87, player);
        int newScore = player.getPlayerScore();
        System.out.println("New player score after secret card: " + newScore);

        assertEquals(newScore, initialScore + 2);
    }

    @Test
    void testCreateSpecificSecretCardWithLObjective() {
        ObjectiveDeck objectiveDeck = (ObjectiveDeck) objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        //GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //L cards from 91
        objectiveDeck.putCardOnTopOfDeck(91);
        ObjectiveCard ElleCard91 =objectiveDeck.firstCardForEachPlayer(); //carta REQUISITO L (MUSSHROOM, MUSHROOM, PLANT)
        assertEquals(91, ElleCard91.getId(), "carta id 91");



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

        //HO MESSO LE CARTE NELLA BORD RISPETTANDO IL REQUISITO MINIMO
        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(ElleCard91, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);
    }

    @Test
    void testCreateSpecificSecretCardWithTRISObjective() {
        ObjectiveDeck objectiveDeck = (ObjectiveDeck) objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        //GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //TRIS CARD START AT 95
        objectiveDeck.putCardOnTopOfDeck(95);
        ObjectiveCard objectiveCard95 =objectiveDeck.firstCardForEachPlayer();

        //la 95 vuole un tris di mushrooms
        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);

        assertEquals(1,player.getPlayerCards().getFirst().getId());
        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "TL");
        assertEquals(2,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TR");
        assertEquals(3,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "BL");

        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(objectiveCard95, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);

    }

    @Test
    void testCreateSpecificSecretCardWithMixObjective() {
        ObjectiveDeck objectiveDeck = (ObjectiveDeck) objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        //GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //MIX CARD IS ONLY 99
        objectiveDeck.putCardOnTopOfDeck(99);
        ObjectiveCard mixCard99 =objectiveDeck.firstCardForEachPlayer();
        assertEquals(99,mixCard99.getId());

        //la 95 vuole un tris di mushrooms
        resourceDeck.putCardOnTopOfDeck(5);
        player.drawResourceCard(resourceDeck);
        resourceDeck.putCardOnTopOfDeck(6);
        player.drawResourceCard(resourceDeck);
        resourceDeck.putCardOnTopOfDeck(7);
        player.drawResourceCard(resourceDeck);

        assertEquals(5,player.getPlayerCards().getFirst().getId());
        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "TR");
        assertEquals(6,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TL");
        assertEquals(7,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "BR");

        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(mixCard99, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);

    }

    @Test
    void testCreateSpecificSecretCardWithBisObjective() {
        ObjectiveDeck objectiveDeck = (ObjectiveDeck) objectiveCardConstructor.createCards();//non mischiato

        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);

        ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();//not shuffled
        //GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();//not shuffled

        //MIX CARD IS ONLY 99
        objectiveDeck.putCardOnTopOfDeck(100);
        ObjectiveCard mixCard99 =objectiveDeck.firstCardForEachPlayer();
        assertEquals(100,mixCard99.getId());

        //la 95 vuole un tris di mushrooms
        resourceDeck.putCardOnTopOfDeck(7);
        player.drawResourceCard(resourceDeck);
        resourceDeck.putCardOnTopOfDeck(17);
        player.drawResourceCard(resourceDeck);


        assertEquals(7,player.getPlayerCards().getFirst().getId());
        Card cardPlayerChoose = player.getPlayerCards().getFirst();
        player.playCard(board,0,0, player.getPlayerCards().getFirst(), initialCard, "BL");
        assertEquals(17,player.getPlayerCards().getFirst().getId());
        player.playCard(board,0,1, player.getPlayerCards().getFirst(), cardPlayerChoose, "TL");

        board.printBoard();

        int initialScore = player.getPlayerScore();

        board.createSpecificSecretCard(mixCard99, player);

        // Assuming checkPattern method correctly updates player's score based on the card
        assertTrue(player.getPlayerScore() > initialScore);

    }
}