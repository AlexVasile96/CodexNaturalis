package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceDeckTest {
    private Player player;
    Board board;
    private GoldDeck goldDeck;
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();


    @BeforeEach
    void setUp() {
        //CardConstructor resourceCardConstructor = new ResourceCardConstructor();
        board = new Board(50,50);
        player = new Player("Momo",0,Dot.GREEN,board);
    }

    @Test //Test del metodo che fa pescare una carta al player dal deck risorse
    void drawCardPlayerNonDeveAverePiuDiTre() {//verifico che il player non possa avere più di tre carte in mano
        for(int i = 0; i <5; i++){
            resourceDeck.drawCard(player);
        }
        assertEquals(3, player.getPlayerCards().size());
        assertEquals(37, resourceDeck.carteRimaste());
    }

    @Test //Test del metodo che aggiunge una carta risorse dal deck carte risorse alle carte del pozzo
    void drawCardNelPozzo() {
        List<Card> pozzo= new ArrayList<>();
        resourceDeck.drawCard(pozzo);
        assertEquals(1, pozzo.size());
        assertEquals(39, resourceDeck.carteRimaste());

    }

    @Test
    void addCard() {
        Player player2 = new Player("Goku",0,Dot.GREEN,board);
        resourceDeck.drawCard(player2);
        assertEquals(39, resourceDeck.carteRimaste());
        Card cartaBomba = player2.getPlayerCards().get(0);
        resourceDeck.addCard(cartaBomba);
        assertEquals(40, resourceDeck.carteRimaste());
        //ERRORE NON DOVREBBE PERMETTERE L'INSERIMENTO DI CARTA GOLD NELLE RISORSE, E A PRESCINDERE SUPERARE LE 40 PER MAZZO
        resourceDeck.addCard(cartaBomba);
        assertEquals(40, resourceDeck.carteRimaste());
    }
}