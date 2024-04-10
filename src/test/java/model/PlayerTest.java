package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PlayerTest {
    ArrayList <Card> playerCards = new ArrayList<>();
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
        List<Card> cardsFromWell= new ArrayList<>();
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        System.out.println(cardsFromWell);
        //Provo a pescare una carta avente Id presente nelle carte del pozzo

        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        player.chooseCardFromWell(cardsFromWell);
        System.out.println("Il mio input: "+ input);
        System.out.println("\n\nPlayer Cards: " + playerCards.size());
        assertFalse(playerCards.isEmpty());


        //////////////////////////////////////////////////////////////////////////////////
        //Provo a pescare una carta dal pozzo vuoto e non lancia l'eccezione --PROBLEMA--
        //assertThrows(IllegalStateException.class, () -> {
         //   player.chooseCardFromWell(cardsFromWell);
        //}); */

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