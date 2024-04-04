package com.example.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();
        Map<SpecificSeed, Integer> seedCountMap = new HashMap<>();
        CardConstructor resourceCardConstructor = new CardConstructor(); //create resource cards
        Deck resourceDeck = resourceCardConstructor.createCards(); //create Deck for resourcesCards
        resourceDeck.shuffle(); //SHUFFLING THE RESOURCEDECK

        GoldCardConstructor goldcardConstructor = new GoldCardConstructor(); //create gold cards
        Deck goldDeck = goldcardConstructor.createCards(); //create Deck for goldCards
        goldDeck.shuffle(); //SHUFFLING THE GOLDDECK

        InitCardConstructor initCardConstructor= new InitCardConstructor(); //CREATING INITIAL CARDS
        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards(); //creating Deck for the six first card, casting because in the costrcutor i provided a deck class

        ObjectiveCardConstructor objectiveCardConstructor= new ObjectiveCardConstructor(); //CREATING OBJECTIVE CARDS
        Deck objectiveDeck= objectiveCardConstructor.createCards(); //creating deck for objective cards
        objectiveDeck.shuffle();
        ObjectiveCard firstCommonObjective= objectiveDeck.firstCardForEachPlayer();
        ObjectiveCard secondCommonObjective= objectiveDeck.firstCardForEachPlayer();
        System.out.println("Il primo obiettivo è: " + firstCommonObjective);
        System.out.println("Il secondo obiettivo comune è: "+ secondCommonObjective);
        BoardPoints boardPoints= new BoardPoints();


        //CREATING OBJECTIVE CARDS

        Board board = new Board(50, 50); //creating the board which is NOT shared by all the players
        boardPoints.countPoints(board);
        Player player = new Player("Calla", 0, Dot.GREEN, board); //creating a player
        ObjectiveCard firstChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);
        ObjectiveCard secondChoiceSecret = (ObjectiveCard) objectiveDeck.drawCard(player);

        List <ObjectiveCard> secretCards=new ArrayList<>();
        secretCards.add(firstChoiceSecret);
        secretCards.add(secondChoiceSecret);

        player.chooseSecretCard(secretCards); //player choooses his card
        board.createSpecificSecretCard(player.getSecretChosenCard());

        FirstThreeCards firstThreeCards= new FirstThreeCards(player, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        firstThreeCards.yourThreeCards(); //Player Deck initialized
        player.visualizePlayerCards(player.getPlayerCards()); //METHOD TO VISUALIZE THE 3 CARDS THE PLAYER RANDOMLY DREW
        InitialCard initialCard = initialCardDeck.firstCardForEachPlayer(player); //THE SHUFFLE IS ALREADY IMPLEMENTED IN THIS METHOD
        System.out.println(initialCard.toString());                 //PRINTING THE INITIAL CARD
        board.placeInitialCard(initialCard);                        //PLACING THE INITIAL CARD ON THE BOARD, THIS IS WHERE THE GAME STARTS
        board.placeInitialCard(initialCard);                        //JUST CHECKING IF THE METHOD ACTUALLY PREVENTS FROM PLACING 2 INITIAL CARDS
        board.printCornerCoordinates();
        board.printBoard();//GETTING THE INITIAL CARD COORDINATES
        boardPoints.countPoints(board);


        //Player choose the first card he has on his deck, in this case we talking about a resource card
        player.playCard(board,0);                           //Player places his cards
        board.printBoard();                                         //printing the board
        System.out.println(board.getCardsOnTheBoardList());           //printing the cards on the board
        player.drawResourceCard((ResourceDeck) resourceDeck);
        player.visualizePlayerCards(player.getPlayerCards());
        player.playCard(board,0);
        board.printBoard();                                         //printing the board
        System.out.println(board.getCardsOnTheBoardList());
        boardPoints.countPoints(board);
        TrisObjectiveCard trisObjectiveCard= new TrisObjectiveCard();
        trisObjectiveCard.checkPattern(board,SpecificSeed.MUSHROOM,player); //Funziona!

    }
}

