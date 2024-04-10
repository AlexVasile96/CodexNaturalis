package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceDeckTest {
    private Player player;
    private Board board;
    private GoldDeck goldDeck;
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    Deck resourceDeck = resourceCardConstructor.createCards();


    @BeforeEach
    void setUp() {
        CardConstructor resourceCardConstructor = new ResourceCardConstructor();
        board = new Board(10,10);
        player = new Player("Player1",0,Dot.GREEN,board);
    }

    @Test //Test del metodo che fa pescare una carta al player dal deck risorse
    void drawCard() {//verifico che il player non possa avere più di tre carte in mano
            for(int i = 0; i <5; i++){
                resourceDeck.drawCard(player);
            }
             assertEquals(3, player.getPlayerCards().size());
        }


    @Test //Test del metodo che aggiunge una carta risorse dal deck carte risorse alle carte del pozzo
    //NE DEVO PARLARE CON GLI ALTRI PERCHè NON HO CAPITO BENE STA COSA
    void testDrawCard() {

    }

    @Test
    void addCard() {
    }

    @Test
    void firstCardForEachPlayer() {
    }
}