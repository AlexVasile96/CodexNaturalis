package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class ResourceDeckTest {
    private Player player1;
    private Board board;
    private GoldDeck goldDeck;
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    Deck resourceDeck = resourceCardConstructor.createCards();


    @BeforeEach
    void setUp() {
        CardConstructor resourceCardConstructor = new ResourceCardConstructor();
        board = new Board(10,10);
        player1 = new Player("Player1",0,Dot.GREEN,board);
    }

    @Test
    void drawCard() {
            for(int i = 0; i <4; i++){
                resourceDeck.drawCard(player1);
            }
            Exception exception =  assertThrows(IllegalStateException.class, () -> {
                resourceDeck.drawCard(player1);
            });
            assertEquals("Il giocatore ha già tre carte nella mano",exception.getMessage() );
        }


    @Test
    void testDrawCard() {
    }

    @Test
    void addCard() {
    }

    @Test
    void firstCardForEachPlayer() {
    }
}