package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoldDeckTest {

    private Player player,player2;
    Board board;
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor(); //create resource cards
    GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();

    @BeforeEach
    void setUp() {
        board = new Board(50,50);
        player = new Player("Momo",0,Dot.GREEN,board);
        player2 = new Player("Goku",0,Dot.GREEN,board);

    }

    @Test //Test del metodo che fa pescare una carta al player dal deck risorse
    void drawCardPlayerNonDeveAverePiuDiTre() {//verifico che il player non possa avere più di tre carte in mano
        for(int i = 0; i <5; i++){
            goldDeck.drawCard(player);
        }
        assertEquals(3, player.getPlayerCards().size());
        assertEquals(37, goldDeck.carteRimaste());
    }

    @Test //Test del metodo che aggiunge una carta risorse dal deck carte risorse alle carte del pozzo
    void drawCardNelPozzo() {
        List<Card> pozzo= new ArrayList<>();
        goldDeck.drawCard(pozzo);
        assertEquals(1, pozzo.size());
        assertEquals(39, goldDeck.carteRimaste());
    }

    @Test
    void addCard() {
        Player player2 = new Player("Goku",0,Dot.GREEN,board);
        Card cartaBomba = goldDeck.drawCard(player2);

        //provo ad inserire una carta di un'altro deck
        Card cartaBomba2 = resourceDeck.drawCard(player);
        assertThrows(Exceptions.IllegalAddException.class, () -> goldDeck.addCard(cartaBomba2), "La carta non appartiene al mazzo gold");

        //provo ad inserire un duplicato
        Card cartaBomba3 = goldDeck.drawCard(player2);
        goldDeck.addCard(cartaBomba);
        assertThrows(Exceptions.AlredyInException.class, () -> goldDeck.addCard(cartaBomba));

    }
}