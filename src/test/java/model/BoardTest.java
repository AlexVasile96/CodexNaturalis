package model;

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
        player = new Player("Vegeta", 0, Dot.YELLOW, board);
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

    @Test
    void placeGoldCard() {

        // Configurazione della board con i semi necessari per piazzare la carta oro
        board.getNode(0, 0).setSpecificNodeSeed(SpecificSeed.MUSHROOM);
        board.getNode(0, 1).setSpecificNodeSeed(SpecificSeed.MUSHROOM);
        board.getNode(1, 0).setSpecificNodeSeed(SpecificSeed.ANIMAL);
        board.getNode(1, 1).setSpecificNodeSeed(SpecificSeed.NOTTOBEPLACEDON);


        // true test
        boolean result = board.placeGoldCard( List.of(SpecificSeed.MUSHROOM, SpecificSeed.MUSHROOM, SpecificSeed.ANIMAL));
        assertTrue(result, "Il posizionamento della carta oro dovrebbe essere possibile");

        // False test
        result = board.placeGoldCard( List.of(SpecificSeed.MUSHROOM, SpecificSeed.MUSHROOM, SpecificSeed.MUSHROOM));
        assertFalse( result, "Il posizionamento della carta oro non dovrebbe essere possibile");
    }

    @Test
    void placeInitialCard() {
        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player);
        board.placeInitialCard(initialCard);


        int[][] centralCoordinates= board.getCentralCoordinates();

        // Verify that the initial card is correctly placed on the board
        Node[][] nodes = board.getNodes();


        assertEquals(initialCard.getTL().getSpecificCornerSeed(), nodes[centralCoordinates[0][0]][centralCoordinates[0][1]].getSpecificNodeSeed(),"Tl");
        assertEquals(initialCard.getTR().getSpecificCornerSeed(), nodes[centralCoordinates[1][0]][centralCoordinates[0][1]].getSpecificNodeSeed(),"TR");
        assertEquals(initialCard.getBL().getSpecificCornerSeed(), nodes[centralCoordinates[0][0]][centralCoordinates[1][1]].getSpecificNodeSeed(),"BL");
        assertEquals(initialCard.getBR().getSpecificCornerSeed(), nodes[centralCoordinates[1][0]][centralCoordinates[1][1]].getSpecificNodeSeed(),"BR");
    }

    @Test
    void printCornerCoordinates() {
    }

    @Test
    void createSpecificSecretCard() {
    }

    @Test
    void setNodes() {
    }

    @Test
    void setInitialCard() {
    }

    @Test
    void setCardsOnTheBoardList() {
    }

    @Test
    void setNumOfEmpty() {
    }

    @Test
    void setInitEmptyValue() {
    }

    @Test
    void getNodes() {
    }

    @Test
    void getInitialCard() {
    }

    @Test
    void getCardsOnTheBoardList() {
    }

    @Test
    void getNumOfEmpty() {
    }

    @Test
    void getInitEmptyValue() {
    }

    @Test
    void getNode() {
    }

    @Test
    void setNode() {
    }
}