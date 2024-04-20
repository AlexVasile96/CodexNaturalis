package model;

import model.game.*;
import model.objectiveCardTypes.BisObjectiveCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BisObjectiveCardTest {
    /*
    INITIALIZATION
     */
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0, Dot.GREEN,board);

    private SpecificSeed specificSeed = SpecificSeed.MUSHROOM;
    private SpecificSeed specificSeed2 = SpecificSeed.ANIMAL;
    private Node node = new Node(specificSeed,0,0);
    private Node node2 = new Node(specificSeed2,0,0);
    private BisObjectiveCard bisObjectiveCard = new BisObjectiveCard();

    @Test
    void checkPattern() {

//        SETTING 3 NODES TO MUSHROOM and 2 ANIMALS


        board.setNode(0,0,node);
        board.setNode(2,2,node);
        board.setNode(3,8,node);
        board.setNode(3,9,node2);
        board.setNode(4,9,node2);


//      THE METHOD SHOULD RETURN TRUE AND GIVE 2 POINTS TO THE PLAYER FOR EACH COUPLE OF SPECIFIC SEED
        assertTrue(bisObjectiveCard.checkPattern(board,specificSeed,player) && (bisObjectiveCard.checkPattern(board,specificSeed2,player)));
        assertEquals(4,player.getPlayerScore());


//      OK
    }
}