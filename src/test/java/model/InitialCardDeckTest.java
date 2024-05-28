package model;

import it.polimi.ingsw.model.card.InitCardConstructor;
import it.polimi.ingsw.model.deck.InitialCardDeck;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Dot;
import it.polimi.ingsw.model.game.Player;
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