package com.example.proj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceDeckTest {
    private Player player1;
    private Board board;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private List<Card> cards;
    @BeforeEach
    public void setUp() {
        CardConstructor resourceCardConstructor = new CardConstructor();
        board = new Board(10,10);
        player1 = new Player("Player1",0,Dot.GREEN,board);
        resourceDeck = new ResourceDeck(cards);
        resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();
    }


   @Test
    void drawCard() {
        for(int i = 0; i <3; i++){
            player1.drawResourceCard((ResourceDeck) resourceDeck);
        }
        assertThrows(RuntimeException.class, () -> {
            player1.drawResourceCard((ResourceDeck) resourceDeck);
        });
    }

    @Test
    void addCard() {
    }

    @Test
    void firstCardForEachPlayer() {
    }
}