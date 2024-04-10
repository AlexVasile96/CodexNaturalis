package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PlayerTest {
    ArrayList <Card> playerCards = new ArrayList<>(3);
    Board board = new Board(50,50);
    Player player = new Player("Calla",0,Dot.GREEN,board);
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor();
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor();
    ObjectiveCardConstructor objectiveCardConstructor = new ObjectiveCardConstructor();
    Deck resourceDeck = resourceCardConstructor.createCards();
    Deck goldDeck = goldCardConstructor.createCards();
    Deck objectiveDeck = objectiveCardConstructor.createCards();
@BeforeEach
public void setUp(){

}
    @Test
    void isCardBack() {
    }

    @Test
    void setCardBack() {
    }

    @Test
    void drawResourceCard() {
    }

    @Test
    void drawGoldCard() {
    }

    @Test
    void chooseCardFromWell() {
    //Creo il pozzo e ci piazzo dentro due care risorsa e due carte gold
        List<Card> cardsFromWell= new ArrayList<>(3);
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        System.out.println(cardsFromWell);
        //Provo a pescare una carta avente Id presente nelle carte del pozzo

        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        System.out.println("\n\nPlayer Cards: " + player.getPlayerCards().size());
        assertFalse(player.getPlayerCards().isEmpty());

        input = "2";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        System.out.println("\n\nPlayer Cards: " + player.getPlayerCards().size());
        assertFalse(player.getPlayerCards().isEmpty());

        input = "3";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        System.out.println("\n\nPlayer Cards: " + player.getPlayerCards().size());
        assertFalse(player.getPlayerCards().isEmpty());

        input = "4";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        System.out.println("\n\nPlayer Cards: " + player.getPlayerCards().size());
        assertFalse(player.getPlayerCards().isEmpty());


    }



    @Test //controllo che il player possa giocare solo le carte che ha in mano
    void chooseCard() {

    }

    @Test
    void chooseSecretCard() {
    }

    @Test
    void playCard() {
    }

    @Test
    void turnYourCard() {
    }

}