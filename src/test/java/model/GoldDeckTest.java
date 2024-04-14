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
        goldDeck.drawCard(player2);
        Card cartaBomba = player2.getPlayerCards().get(0);
        goldDeck.addCard(cartaBomba);
        assertEquals(40, goldDeck.carteRimaste());//OK LA CARTA VIENE AGGIUNTA NEL MAZZO
        //caso in cui si faccia confusione con i deck RISORSA
        resourceDeck.drawCard(player2);
        cartaBomba = player2.getPlayerCards().get(1);
        goldDeck.addCard(cartaBomba);
        //ERRORE, NON DOVREBBE PERMETTERE UNA CARTA RISORSA NEL DECK ED INOLTRE A PRESCINDERE NON DOVREBBE SUPERARE LE 40 A MAZZO
        assertEquals(40, goldDeck.carteRimaste());
    }
}