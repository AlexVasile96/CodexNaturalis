package model;

import model.card.InitCardConstructor;
import model.card.InitialCard;
import model.deck.InitialCardDeck;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import model.game.SpecificSeed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Player player;
    private InitCardConstructor initCardConstructor= new InitCardConstructor();

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
    void createSpecificSecretCardTest(){

    }
}