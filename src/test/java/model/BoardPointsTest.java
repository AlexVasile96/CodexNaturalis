package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardPointsTest {
    private int pointss;
    private Board board = new Board(50,50);
    private ArrayList<SpecificSeed> pointOnTheBoard;
    private Node node = new Node(SpecificSeed.MUSHROOM,0,0);
    private Node node2 = new Node(SpecificSeed.INKWELL,0,0);
    BoardPoints boardPoints = new BoardPoints();
    @Test
    void countPoints() {
        /*
        Setting 4 nodes to the SpecificSeed MUSHROOM
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
        assertEquals(3,boardPoints.addictionalPointsForGoldCards(board,SpecificSeed.INKWELL));
        //OK
    }
}