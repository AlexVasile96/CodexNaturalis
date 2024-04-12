package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
Il metodo deve controllare che tre carte del seme giusto siano posizionate in diagonale
Ritorna un boolean (true/false)
*/
class StairsObjectiveCardTest {
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0,Dot.GREEN,board);

    private SpecificSeed specificSeed = SpecificSeed.MUSHROOM;
    private Node node = new Node(specificSeed,0,0);

    private StairsObjectiveCard stairsObjectiveCard = new StairsObjectiveCard();

    @Test
    void checkPattern() {
        //Metto mushroom in tre nodi posizionati tutti sulla stessa riga, il metodo dovrebbe ridare false
        board.setNode(0,0,node);
        board.setNode(1,1,node);
        board.setNode(2,2,node);
        board.setNode(1,0,node);
        board.setNode(2,1,node);
        board.setNode(3,2,node);
        //verifico che il metodo restituisca false
        //PER ORA IL TEST FALLISCE, ASSEGNA SOLO 2 PUNTI INVECE DI 4
        assertTrue(stairsObjectiveCard.checkPattern(board,SpecificSeed.MUSHROOM,player));
        assertEquals(4,player.getPlayerScore());

    }
}