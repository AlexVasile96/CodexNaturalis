package model;

import model.card.InitCardConstructor;
import model.deck.InitialCardDeck;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitialCardDeckTest {
    Board board = new Board(50, 50);
    InitCardConstructor initCardConstructor= new InitCardConstructor(); //CREATING INITIAL CARDS
    InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards();
    private Player player;
    @BeforeEach
    void setUp() {
        player = new Player("Player1", 0, Dot.GREEN, board);
    }


    @Test
    void firstCardForEachPlayer() {
        initialCardDeck.firstCardForPlayer(player);
        assertEquals(5,initialCardDeck.remainingCards());
    }
}