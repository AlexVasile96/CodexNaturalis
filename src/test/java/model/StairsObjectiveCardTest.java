package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
The method checks if 3 cards of the correct specific seed are placed in the right way, then gives
2 points to the player for each triplet placed correctly
*/
class StairsObjectiveCardTest {
    //INITIALIZATIONS
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0,Dot.GREEN,board);

    private SpecificSeed specificSeed = SpecificSeed.MUSHROOM;
    private SpecificSeed specificSeed2 = SpecificSeed.PLANT;
    private Node node = new Node(specificSeed,0,0);
    private Node node2 = new Node(specificSeed2,0,0);

    private StairsObjectiveCard stairsObjectiveCard = new StairsObjectiveCard();


    @Test
    void checkPattern() {

        /*
        I PLACE A TRIPLET OF MUSHROOM DIAGONALLY, FROM THE LEFT GOING DOWN TO THE RIGHT
         */

        board.setNode(0, 0, node);
        board.setNode(1, 1, node);
        board.setNode(2, 2, node);

       /*
        THE METHOD SHOULD RETURN FALSE BECAUSE THE CARDS BELONG TO A SPECIFIC SEED
        THAT MUST BE PLACED DIAGONALLY FROM THE LEFT GOING UP TO THE RIGHT
        */
        assertFalse(stairsObjectiveCard.checkPattern(board, specificSeed, player));

        //SHOULD GIVE 0 POINTS TO THE PLAYER
        assertEquals(0, player.getPlayerScore());

        //OK
    }
       @Test
        void checkPattern2() {
        /*
        I PLACE A TRIPLET OF MUSHROOM DIAGONALLY, FROM THE LEFT GOING UP TO THE RIGHT
         */

            board.setNode(2, 2, node);
            board.setNode(1, 3, node);
            board.setNode(0, 4, node);

       /*
        THE METHOD SHOULD RETURN TRUE BECAUSE THE CARDS BELONG TO A SPECIFIC SEED
        THAT MUST BE PLACED DIAGONALLY FROM THE LEFT GOING UP TO THE RIGHT
        */
            assertTrue(stairsObjectiveCard.checkPattern(board, specificSeed, player));
            //SHOULD GIVE 2 POINTS TO THE PLAYER
            assertEquals(2, player.getPlayerScore());
            //OK
        }
        @Test
        void checkPatter3() {
        /*
            I PLACE TWO TRIPLETS OF PLANT FROM THE LEFT GOING DOWN DIAGONALLY TO THE RIGHT
        */

        board.setNode(0,0,node2);
        board.setNode(1,1,node2);
        board.setNode(2,2,node2);
        board.setNode(1,0,node2);
        board.setNode(2,1,node2);
        board.setNode(3,2,node2);

         /*
            THE METHOD SHOULD RETURN TRUE BECAUSE THE CARDS BELONG TO A SPECIFIC SEED
            THAT MUST BE PLACED DIAGONALLY FROM THE LEFT GOING DOWN TO THE RIGHT
        */
        assertTrue(stairsObjectiveCard.checkPattern(board,specificSeed2,player));
        //SHOULD GIVE 4 POINTS TO THE PLAYER
        //assertEquals(4,player.getPlayerScore()); commented until class update
        // TO BE FIXED
    }
    @Test
    void checkPatter4() {
        /*
            I PLACE A TRIPLET OF PLANT FROM THE LEFT GOING UP DIAGONALLY TO THE RIGHT
        */

        board.setNode(2, 0, node2);
        board.setNode(1, 1, node2);
        board.setNode(0, 2, node2);

         /*
            THE METHOD SHOULD RETURN FALSE BECAUSE THE CARDS BELONG TO A SPECIFIC SEED
            THAT MUST BE PLACED DIAGONALLY FROM THE LEFT GOING DOWN TO THE RIGHT
        */
        assertFalse(stairsObjectiveCard.checkPattern(board,specificSeed2,player));
        //SHOULD GIVE 0 POINTS TO THE PLAYER
        assertEquals(0,player.getPlayerScore());
        //TO BE FIXED
    }

}
