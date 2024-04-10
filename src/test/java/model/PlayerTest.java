package model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player1;
    private Board board;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private List<Card> cards;
    private List<ObjectiveCard> secretCards;
    @BeforeEach
    public void setUp() {
        CardConstructor resourceCardConstructor = new CardConstructor();
        board = new Board(10,10);
        player1 = new Player("Player1",0,Dot.GREEN,board);
        resourceDeck = new ResourceDeck(cards);
        resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();
        ArrayList<Card> playerCards = new ArrayList<>(3);
        player1.setPlayerCards(playerCards);



    }

    @Test
    void isCardBack() {
    }

    @Test
    void setCardBack() {

    }

    @Test
    void getSecretChosenCard() {
    }

    @Test
    void setSecretChosenCard() {
    }

    @Test
    void visualizePlayerCards() {
    }

    /*@Test
    void drawResourceCard() {
       for(int i = 0; i <5; i++){
            player1.drawResourceCard( resourceDeck);
        }
        assertThrows(RuntimeException.class, () -> {
            player1.drawResourceCard((ResourceDeck) resourceDeck);
        });
        }
*/
    @Test
    void drawGoldCard() {
    }

    @Test
    void chooseCard() {
    }

    @Test
    void chooseSecretCard() {
        int selectedCardIndex = 1;
        assertTrue(selectedCardIndex>0);
        assertFalse((selectedCardIndex<=player1.getPlayerCards().size()));
        assertFalse(selectedCardIndex < 1);
        assertFalse(selectedCardIndex > secretCards.size());
    }

    @Test
    void playCard() {


    }

    @Test
    void turnYourCard() {
    }

    @Test
    void getPlayerCards() {
    }

    @Test
    void setNickName() {
    }

   @Test
    void setPlayerScore() {
        player1.setPlayerScore(400);
        assertFalse(player1.getPlayerScore()<=30);
    }

    @Test
    void setDot() {
    }

    @Test
    void setBoard() {
    }

    @Test
    void setPlayerCards() {
    }

    @Test
    void getNickName() {
    }

    @Test
    void getPlayerScore() {
    }

    @Test
    void getDot() {
    }

    @Test
    void getBoard() {
    }
}