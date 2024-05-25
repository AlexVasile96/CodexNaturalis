package model;

import model.card.*;
import model.deck.Deck;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Board board = new Board(50,50);
    Player player = new Player("Calla",0, Dot.GREEN,board);
    ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor();
    GoldCardConstructor goldCardConstructor = new GoldCardConstructor();
    ObjectiveCardConstructor objectiveCardConstructor = new ObjectiveCardConstructor();
    Deck resourceDeck = resourceCardConstructor.createCards();
    Deck goldDeck = goldCardConstructor.createCards();
    Deck objectiveDeck = objectiveCardConstructor.createCards();

    @Test
     /*
    Check if the player draws a resource card by the id
     */
    void drawResourceCard() {
    resourceDeck.shuffle();
    resourceDeck.drawCard(player);
    assertEquals(1,player.getPlayerCards().size());
        System.out.println(player.getPlayerCards().getFirst());
        assertTrue(player.getPlayerCards().getFirst().getId() >=1 && player.getPlayerCards().getFirst().getId() <=40);
    }

    @Test
    /*
    Check if the player draws a gold card by the id
     */
    void drawGoldCard() {
        goldDeck.shuffle();
        goldDeck.drawCard(player);
        assertEquals(1,player.getPlayerCards().size());
        System.out.println(player.getPlayerCards().getFirst());
        assertTrue(player.getPlayerCards().getFirst().getId() >=41 && player.getPlayerCards().getFirst().getId() <=80);
    }


    @Test
    void chooseCardFromWell() {
    //initialization
        List<Card> cardsFromWell= new ArrayList<>(3);
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);

        //Trying to give more than 3 cards to the player
        for(int i = 1; i < 5; i++){
            String input = String.valueOf(i);
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
            assertFalse(player.getPlayerCards().isEmpty());
        }
        assertEquals(3,player.getPlayerCards().size());
    }



    @Test //player cannot choose a card which is not in his hand (having index > 3)
    void chooseCard() {
        ObjectiveCard firstChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        ObjectiveCard secondChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        List <ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add(firstChoiceSecret);
        secretCards.add(secondChoiceSecret);
        for(int i = 0; i<3; i++) {
        resourceDeck.drawCard(player);
        }

        assertThrows(IndexOutOfBoundsException.class,() -> player.chooseCard(4));

    }

    @Test
            //Check that player can't choose a card which isn't in the secret objective cards array

    //checking if the chosen card is correct
    void chooseSecretCard() {
        ObjectiveCard firstChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        ObjectiveCard secondChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        List <ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add(firstChoiceSecret);
        secretCards.add(secondChoiceSecret);

        /*TRYING TO SELECT AN INVALID CARD*/
        String input = "4";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream stampa = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stampa));
        assertThrows(NoSuchElementException.class, ()->{
            player.chooseSecretCard(secretCards);
            });


        /*TESTING IF THE METHOD PLACES THE RIGHT CARD*/
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
        //testing the placement of the card having index = 1 (in the player's card array)
        List<Card> cardsFromWell= new ArrayList<>(3);
        resourceDeck.drawCard(cardsFromWell);
        resourceDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        goldDeck.drawCard(cardsFromWell);
        String input = "1\ntl";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        player.chooseCardFromWell(cardsFromWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        int index = player.getPlayerCards().getFirst().getId();
        assertEquals(1,index);

    }

    @Test
    void turnYourCard() {
        resourceDeck.drawCard(player);
        Card cardToFlip=player.getPlayerCards().getFirst();
        player.turnYourCard(cardToFlip);
        boolean result=cardToFlip.isCardBack();
        assertTrue(result);
    }

}