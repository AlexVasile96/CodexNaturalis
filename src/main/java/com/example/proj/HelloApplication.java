package com.example.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        CardConstructor resourceCardConstructor = new CardConstructor(); //create resource cards
        Deck resourceDeck = resourceCardConstructor.createCards(); //create Deck for resourcesCards
        resourceDeck.shuffle(); //SHUFFLING THE RESOURCEDECK

        GoldCardConstructor goldcardConstructor = new GoldCardConstructor(); //create gold cards
        Deck goldDeck = goldcardConstructor.createCards(); //create Deck for goldCards
        goldDeck.shuffle(); //SHUFFLING THE GOLDDECK

        InitCardConstructor initCardConstructor= new InitCardConstructor(); //CREATING INITIAL CARDS
        InitialCardDeck initialCardDeck= (InitialCardDeck) initCardConstructor.createCards(); //creating Deck for the six first card, casting because in the costrcutor i provided a deck class

        //CREATING OBJECTIVE CARDS

        Board board = new Board(50, 50); //creating the board which is NOT shared by all the players

        Player player = new Player("Calla", 0, Dot.GREEN, board); //creating a player
        FirstThreeCards firstThreeCards= new FirstThreeCards(player, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        firstThreeCards.yourThreeCards(); //Player Deck initialized
        player.visualizePlayerCards(player.getPlayerCards()); //METHOD TO VISUALIZE THE 3 CARDS THE PLAYER RANDOMLY DREW

        InitialCard initialCard = initialCardDeck.firstCardForEachPlayer(player); //THE SHUFFLE IS ALREADY IMPLEMENTED IN THIS METHOD
        System.out.println(initialCard.toString());                 //PRINTING THE INITIAL CARD
        board.placeInitialCard(initialCard);                        //PLACING THE INITIAL CARD ON THE BOARD, THIS IS WHERE THE GAME STARTS
        board.placeInitialCard(initialCard);                        //JUST CHECKING IF THE METHOD ACTUALLY PREVENTS FROM PLACING 2 INITIAL CARDS
        board.printCornerCoordinates();
        board.printBoard();                                         //GETTING THE INITIAL CARD COORDINATES
        player.chooseCard(0);                                 //Player choose the first card he has on his deck, in this case we talking about a resource card
        player.playCard(board,0);
        board.printBoard();
        System.out.println(board.getCardsOnTheBoardList());
    }
}

