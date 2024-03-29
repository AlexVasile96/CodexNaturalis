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
        Deck resourceDeck = resourceCardConstructor.createCards();

        GoldCardConstructor goldcardConstructor = new GoldCardConstructor(); //create gold cards
        Deck goldDeck = goldcardConstructor.createCards();

        Board board = new Board(50, 50);

        Player player = new Player("Calla", 0, Dot.GREEN, board);
        FirstThreeCards firstThreeCards= new FirstThreeCards(player, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        firstThreeCards.yourThreeCards(); //deck inizializzato
        player.visualizePlayerCards(player.getPlayerCards());

        //dare una carta iniziale al bro

        /*System.out.println("\n" + player.getPlayerCards());

        player.drawResourceCard((ResourceDeck) resourceDeck);
        System.out.println("\n" + player.getPlayerCards());

        player.drawGoldCard((GoldDeck) goldDeck);
        System.out.println("\n" + player.getPlayerCards());

        player.drawResourceCard((ResourceDeck) resourceDeck);
        System.out.println("\n" + player.getPlayerCards() + "\n");

        player.visualizePlayerCards(player.getPlayerCards());

        player.drawResourceCard((ResourceDeck) resourceDeck);
        System.out.println("\n" + player.getPlayerCards());

        player.drawResourceCard((ResourceDeck) resourceDeck);
        System.out.println("\n" + player.getPlayerCards());

        player.drawResourceCard((ResourceDeck) resourceDeck);


        //resourceDeck.printDeck();


        System.out.println("\n" + player.getPlayerCards());


        //CardConstructor initialCradConstructor= new CardConstructor();
        //initialCradConstructor.createCards();*/

    }
}








        /*
         List<Card> cartaIniziale = null;
         try {


            FileReader reader = new FileReader("src/main/resources/initcard.json"); //Reading json file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader)); //converting json file
            JSONArray mazzoInit = jsonObject.getJSONArray("init_cards"); //json array
            cartaIniziale = new ArrayList<>(); //creating a new arryalist that contains all the cards
            for (int i = 0; i < mazzoInit.length(); i++) { //for cicle in order to get all the json information
                JSONObject carta = mazzoInit.getJSONObject(i);
                int id = carta.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(carta.getString("type")); //card specific seed (plant,animal...)
                int value = carta.getInt("value"); //that's the point the cart can have when placed
                SpecificSeed giacomo = SpecificSeed.valueOf(carta.getString("TL"));
                SpecificSeed pippo = SpecificSeed.valueOf(carta.getString("TR"));
                SpecificSeed pluto = SpecificSeed.valueOf(carta.getString("BL"));
                SpecificSeed paperino = SpecificSeed.valueOf(carta.getString("BR"));
                Corner TL= new Corner(giacomo);
                Corner TR= new Corner(pippo);
                Corner BL= new Corner(pluto);
                Corner BR= new Corner(paperino);

                JSONArray attributiArray = carta.getJSONArray("attributes");
                List<SpecificSeed> attribute = new ArrayList<>();
                for (int j = 0; j < attributiArray.length(); j++) {
                    attribute.add(SpecificSeed.valueOf(attributiArray.getString(j)));
                }

                InitialCard card = new InitialCard(id, type, value, TL, TR, BL, BR, attribute); //creating all the resource cards
                cartaIniziale.add(card); //adding card to th arraylist previously created
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        InitialCardDeck initialCardDeck = new InitialCardDeck(cartaIniziale);
        //deck.shuffle();
        initialCardDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ initialCardDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ deck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ deck.drawCard()+"\n\n\n");
        initialCardDeck.printDeck();

    }
}*/