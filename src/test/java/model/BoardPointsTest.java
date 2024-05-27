package model;
/*
Description: the method countPoints count the number of occurrences of a given specific seed and returns
that value. In the test we'll set the board to 4 MUSHROOMS and 3 INKWELL. We'll count how many MUSHROOM
the player has on the board.
 */
import model.game.Board;
import model.game.BoardPoints;
import model.game.Node;
import model.game.SpecificSeed;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardPointsTest {

    private Board board = new Board(50,50);
    private ArrayList<SpecificSeed> pointOnTheBoard;
    private Node node = new Node(SpecificSeed.MUSHROOM,0,0,SpecificSeed.MUSHROOM);
    private Node node2 = new Node(SpecificSeed.INKWELL,0,0,SpecificSeed.INKWELL);
    BoardPoints boardPoints = new BoardPoints();
    @Test
    void countPoints() {
        /*
        Setting 4 nodes to the SpecificSeed MUSHROOM and 3 INKWELL
         */
        board.setNode(25,25,node);
        board.setNode(27,27,node);
        board.setNode(29,29,node);
        board.setNode(31,31,node);

        /*
        The Function should return 4
         */
        assertEquals(4, boardPoints.countPoints(board).get(SpecificSeed.MUSHROOM));
        //OK
    }

    @Test
    void addictionalPointsForGoldCards() {
        /*
        Setting three nodes to SecificSeed INKWELL
         */
        board.setNode(25,25,node2);
        board.setNode(27,27,node2);
        board.setNode(29,29,node2);
        /*
        The method should return 3
         */
        assertEquals(3,boardPoints.additionalPointsForGoldCards(board,SpecificSeed.INKWELL));
        //OK
    }
}