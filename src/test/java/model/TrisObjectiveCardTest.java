package model;

import model.objectiveCardTypes.TrisObjectiveCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrisObjectiveCardTest {
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0,Dot.GREEN,board);

    private SpecificSeed specificSeed = SpecificSeed.MUSHROOM;
    private Node node = new Node(specificSeed,0,0);

    private TrisObjectiveCard trisObjectiveCard = new TrisObjectiveCard();
    @Test
    /*
    The method should return True if at least 3 of the same SpecificSeed are placed
    on the board and give to the player 2 point for each set of three SpecificSeed on the board
     */
    void checkPattern() {
        board.setNode(0,0,node);
        board.setNode(1,1,node);
        board.setNode(2,2,node);
        board.setNode(1,0,node);
        board.setNode(2,1,node);
        board.setNode(3,2,node);

        //Expected true
        assertTrue(trisObjectiveCard.checkPattern(board,SpecificSeed.MUSHROOM,player));
        //Expected playerPoints = 4
        assertEquals(4,player.getPlayerScore());
        //OK
    }
}