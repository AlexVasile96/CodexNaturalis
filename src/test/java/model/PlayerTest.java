package model;

import model.card.*;
import model.deck.Deck;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    ArrayList <Card> playerCards = new ArrayList<>(3);
    Board board = new Board(50,50);
    Player player = new Player("Calla",0, Dot.GREEN,board);
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
    void drawResourceCard() {
    }

    @Test
    void drawGoldCard() {
    }

    @Test
    void chooseCardFromWell() {
    //Creo il pozzo e ci piazzo dentro due carte risorsa e due carte gold
        List<Card> cardsFromWell= new ArrayList<>(3);
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);

        //Provo a pescare 4 carte dal pozzo, dovrebbe pescarne al massimo 3
        for(int i = 1; i < 5; i++){
            String input = String.valueOf(i);
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
            assertFalse(player.getPlayerCards().isEmpty());
        }
        assertEquals(3,player.getPlayerCards().size());
    }



    @Test //controllo che il player possa giocare solo le carte che ha in mano
    void chooseCard() {
        ObjectiveCard firstChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        ObjectiveCard secondChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        List <ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add(firstChoiceSecret);
        secretCards.add(secondChoiceSecret);
        for(int i = 0; i<6; i++) {
        resourceDeck.drawCard(player);
        }

        //Verifico che il metodo ritorni null se si sceglie una carta non presente nella mano del player
        assertNull(player.chooseCard(5));
        //assertThrows(IndexOutOfBoundsException.class,() ->{
        //player.chooseCard(4);
        //});

    }

    @Test //Verifica che non si possa scegliere una carta non presente nell'array delle carte obiettivo segreto
    //verifico che la carta scelta sia effettivamente quella giusta
    void chooseSecretCard() {
        ObjectiveCard firstChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        ObjectiveCard secondChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        List <ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add(firstChoiceSecret);
        secretCards.add(secondChoiceSecret);

        /*PROVO A SELEZIONARE UNA CARTA IL CUI INDICE NON E' VALIDO*/
        String input = "4";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream stampa = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stampa));
        assertThrows(IndexOutOfBoundsException.class, ()->{
            player.chooseSecretCard(secretCards);
            });


        /*CONTROLLO CHE IL METODO SCELGA LA CARTA CORRETTA PASSANDOGLI 1 COME SCELTA*/
        String input2 = "1";
        InputStream in2 = new ByteArrayInputStream(input2.getBytes());
        System.setIn(in2);
        assertDoesNotThrow(() ->{
        player.chooseSecretCard(secretCards);
        });
        assertEquals(firstChoiceSecret.getId(),player.getSecretChosenCard().getId());


}

    @Test
    void playCard() {
        List<Card> cardsFromWell= new ArrayList<>(3);
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        String input = "1\ntl";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        int index = player.getPlayerCards().getFirst().id;
        assertEquals(1,index);

    }

    @Test
    void turnYourCard() {
    }

}