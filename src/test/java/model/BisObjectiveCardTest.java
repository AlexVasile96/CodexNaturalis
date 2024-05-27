package model;
/*
Description: testing if the objective card that gives 2 point to the player if it has at least
2 attributes of the given specific seed on the board.
In the test the player has 3 MUSHROOMS and 2 ANIMALS, and we'll assume that the objective card
will be an unreal objective card that will give 2 point for each couple of animals and mushrooms.
 */



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

    private final SpecificSeed specificSeed = SpecificSeed.MUSHROOM;
    private final SpecificSeed specificSeed2 = SpecificSeed.ANIMAL;
    private final SpecificSeed specificSeed3 = SpecificSeed.PLANT;
    private Node node = new Node(specificSeed,0,0,specificSeed);
    private Node node2 = new Node(specificSeed2,0,0,specificSeed);
    private final BisObjectiveCard bisObjectiveCard = new BisObjectiveCard();
    private Node node3 = new Node(specificSeed3,0,0,specificSeed);

    @Test
    void checkPattern() {

//        SETTING 3 NODES TO MUSHROOM, 2 ANIMALS and 2 PLANTS


        board.setNode(0,0,node);
        board.setNode(2,2,node);
        board.setNode(3,8,node);
        board.setNode(3,9,node2);
        board.setNode(4,6,node2);
        board.setNode(4,7,node3);
        board.setNode(4,8,node3);


//      THE METHOD SHOULD RETURN TRUE AND GIVE 2 POINTS TO THE PLAYER FOR EACH COUPLE OF SPECIFIC SEED (MUSHROOM and ANIMAL) and ignore the PLANT nodes
        assertTrue(bisObjectiveCard.checkPattern(board,specificSeed,player) && (bisObjectiveCard.checkPattern(board,specificSeed2,player)));
        assertEquals(4,player.getPlayerScore());


//      OK
    }
}