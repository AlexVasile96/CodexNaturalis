package model;


import model.card.*;
import model.deck.Deck;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ResourceDeck;
import model.game.*;
import model.objectiveCardTypes.TrisObjectiveCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PROVA {
    public static void main(String[] args) {
        //INITIALIZING THE BOARD
        BoardPoints boardPoints = new BoardPoints(); //creating the board which is NOT shared by all the players
        Board board = new Board(50, 50);
        Player player = new Player("Calla", 0, Dot.GREEN, board); //creating a player
        //int numOfMush=0;
        ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
        Deck resourceDeck = resourceCardConstructor.createCards();                      //create Deck for resourcesCards
        resourceDeck.shuffle();                                                          //SHUFFLING THE RESOURCEDECK
        GoldCardConstructor goldcardConstructor = new GoldCardConstructor();                //create gold cards
        Deck goldDeck = goldcardConstructor.createCards();                                      //create Deck for goldCards
        goldDeck.shuffle();                                                                      //SHUFFLING THE GOLDDECK
        ObjectiveCardConstructor objectiveCardConstructor = new ObjectiveCardConstructor();     //CREATING OBJECTIVE CARDS
        Deck objectiveDeck = objectiveCardConstructor.createCards();                            //creating deck for objective cards
        objectiveDeck.shuffle();
        InitCardConstructor initCardConstructor = new InitCardConstructor(); //CREATING INITIAL CARDS
        InitialCardDeck initialCardDeck = (InitialCardDeck) initCardConstructor.createCards(); //creating Deck for the six first card, casting because in the constructor I provided a deck class

        //common cards for every player
        List<Card> cardsWell = new ArrayList<>(); //placing 2 golds cards and 2 golds resources in the well
        resourceDeck.drawCard(cardsWell);
        resourceDeck.drawCard(cardsWell);
        goldDeck.drawCard(cardsWell);
        goldDeck.drawCard(cardsWell);
        System.out.println("Common Cards in the well: ");
        for (Card card : cardsWell) {
            System.out.println(card);
        }
        System.out.println("\n");

        ObjectiveCard firstCommonObjective = objectiveDeck.firstCardForEachPlayer(); //common objective cards
        ObjectiveCard secondCommonObjective = objectiveDeck.firstCardForEachPlayer();
        System.out.println("First common objective card is " + firstCommonObjective);
        System.out.println("Second common objective card is " + secondCommonObjective);

        //creazione carte segrete personali
        List<ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add((ObjectiveCard) objectiveDeck.drawCard(player));
        secretCards.add((ObjectiveCard) objectiveDeck.drawCard(player));
        player.chooseSecretCard(secretCards); //player chooses his card //DA FINIRE STAIRS//
        //board.createSpecificSecretCard(player.getSecretChosenCard(), player);
        FirstThreeCards firstThreeCards = new FirstThreeCards(player, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck); //GIVING THE FIRST THREE CARDS TO THE PLAYER
        firstThreeCards.yourThreeCards();                                                                               //Player Deck initialized
        player.visualizePlayerCards(player.getPlayerCards());                                                           //METHOD TO VISUALIZE THE 3 CARDS THE PLAYER RANDOMLY DREW
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player); //THE SHUFFLE IS ALREADY //aggiungere opzione gira carta
        System.out.println("\n" + initialCard.toString());                 //PRINTING THE INITIAL CARD
        boolean towhileloop = false;                                  //boolean to check the while loop
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now your card corners are the front ones");
        while (towhileloop == false) {                                 //looping to be sure a client chooses 1 or 2
            System.out.println("\nFlip the card?\n");
            System.out.println("1 = YES    2 = NO\n");
            int turnCard = scanner.nextInt();
            if (turnCard == 1) {
                initialCard.setCardBack(true);
                System.out.println("Okay, placing initial card!");
                towhileloop = true;
            } else if (turnCard == 2) {
                initialCard.setCardBack(false);
                System.out.println("Okay, placing initial card!");
                towhileloop = true;
            } else {
                System.out.println("The index you chose is incorrect, please choose 1 or 2");
            }
        }

        board.placeInitialCard(initialCard);                        //PLACING THE INITIAL CARD ON THE BOARD, THIS IS WHERE THE GAME STARTS
        board.printCardsOnTheBoard();
        board.printBoard();                                          //printing the board
        boardPoints.countPoints(board);                             //telling the player how many specificseeds he got
        //NOW PLAYER HAS TO CHOOSE WHICH CARDS HE WANTS TO PLAY
        while (player.getPlayerScore() < 20) {
            System.out.println("Which card do you want to place on the board?");
            player.visualizePlayerCards(player.getPlayerCards());
            System.out.println("If you wanto to play the first card, please type 1\nIf you wanto to play the second card, please type 2\nIf you wanto to play the third card, please type 3\n");
            int cardChosenFromHisDeck = scanner.nextInt() - 1;
            switch (cardChosenFromHisDeck) {
                case 0:
                    player.playCard(board, 0);
                    break;
                case 1:
                    player.playCard(board, 1);
                    break;
                case 2:
                    player.playCard(board, 2);
                    break;
            }
            board.printBoard();

            System.out.println("Common Cards in the well: ");
            for (Card card : cardsWell) {
                System.out.println(card);
            }
            System.out.println("\n");
            boolean presente = false;
            do {
                System.out.println("What kind of card you want?\n 1-Resources 2-Gold");
                int cardChoice = scanner.nextInt();
                if (cardChoice == 1) {
                    System.out.println("From resource deck or from well?\n Resource deck = 0; Well: 1\n");
                    cardChoice = scanner.nextInt();
                    if (cardChoice == 0) {
                        resourceDeck.drawCard(player);
                        System.out.println("\nCard successfully drawn from the resource Deck");
                        presente = true;
                    } else if(cardChoice == 1){
                        System.out.println("Scegli la carta dal pozzo, bastardo: ");
                        System.out.println(cardsWell+"\n");
                        int idScelto = scanner.nextInt()-1;
                        for (Card card : cardsWell) {
                            if (idScelto == card.getId() && card.getId() <= 40) {
                                player.getPlayerCards().add(card);
                                cardsWell.remove(card.getId());
                                resourceDeck.drawCard(cardsWell);
                                System.out.println("card drawn correctly from the resource well");
                                presente = true;
                            }
                        }
                        if (!presente) {
                            System.out.println("\nchosen id not present in the well");
                        }
                    }
                } else if (cardChoice == 2) {
                    System.out.println("From gold deck or from well: gold deck = 0; well: select card id\n");
                    int chosenCard = scanner.nextInt();
                    if (chosenCard == 0) {
                        goldDeck.drawCard(player);
                        System.out.printf("\nCard successfully drawn from the gold Deck");
                        presente = true;
                    } else {
                        for (Card card : cardsWell) {
                            if (chosenCard == card.getId() && card.getId() > 40) {
                                player.getPlayerCards().add(card);
                                cardsWell.remove(card.getId());
                                goldDeck.drawCard(cardsWell);
                                System.out.printf("card drawn correctly from the gold well");
                                presente = true;
                            }
                        }
                        if (!presente) {
                            System.out.println("\nchosen id not present in the well");
                        }
                    }
                }
            } while (!presente);

            player.visualizePlayerCards(player.getPlayerCards());
            player.playCard(board, 0);
            //printing the board
            board.printBoard();
            System.out.println(board.getCardsOnTheBoardList());
            boardPoints.countPoints(board);
            TrisObjectiveCard trisObjectiveCard = new TrisObjectiveCard();
            trisObjectiveCard.checkPattern(board, SpecificSeed.MUSHROOM, player); //Funziona!

        }
    }
}
