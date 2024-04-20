package model;

import model.card.Card;
import model.card.GoldCardConstructor;
import model.card.ResourceCardConstructor;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceDeckTest {
    private Player player;
    Board board;
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck = (ResourceDeck) resourceCardConstructor.createCards();
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor(); //create resource cards
    GoldDeck goldDeck = (GoldDeck) goldCardConstructor.createCards();
    ResourceCardConstructor resourceCardConstructor2 = new ResourceCardConstructor(); //create resource cards
    ResourceDeck resourceDeck2 = (ResourceDeck) resourceCardConstructor2.createCards();


    @BeforeEach
    void setUp() {
        //CardConstructor resourceCardConstructor = new ResourceCardConstructor();
        board = new Board(50,50);
        player = new Player("Momo",0, Dot.GREEN,board);
    }

    @Test //Test del metodo che fa pescare una carta al player dal deck risorse
    void drawCardPlayer() {//verifico che il player non possa avere più di tre carte in mano
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
        Card cartaBomba = resourceDeck.drawCard(player2);

        //provo ad inserire una carta di un'altro deck
        Card cartaBomba2 = goldDeck.drawCard(player);
        assertThrows(Exceptions.IllegalAddException.class, () -> resourceDeck.addCard(cartaBomba2), "La carta non appartiene al mazzo Resource");

        //provo ad inserire un duplicato
        Card cartaBomba3 = resourceDeck.drawCard(player2);
        resourceDeck.addCard(cartaBomba);
        assertThrows(Exceptions.AlredyInException.class, () -> resourceDeck.addCard(cartaBomba));

    }
}