package com.example.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
       CardConstructor cx= new CardConstructor();
       cx.createResourceCards();






        /*List<Card> carteGold = null;

        try {
            // Leggi il file JSON
            FileReader reader = new FileReader("src/main/resources/goldcard.json");

            // Converti il file JSON in un oggetto JSONObject
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            // Ottieni l'array "mazzo" dal JSON
            JSONArray mazzoGold = jsonObject.getJSONArray("goldCard");
            carteGold = new ArrayList<>();
            // Itera attraverso le carte nel mazzo
            for (int i = 0; i < mazzoGold.length(); i++) {
                JSONObject carta = mazzoGold.getJSONObject(i);

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
                JSONArray requirementsArray = carta.getJSONArray("requirements");
                List<SpecificSeed> requirements = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    requirements.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                GoldCard cards = new GoldCard(id, type, value, TL, TR, BL, BR,requirements); //creating all the resource cards
                carteGold.add(cards); //adding card to th arraylist previously created

                // Puoi fare quello che vuoi con valore e seme, come stamparli a console
                //System.out.println("Carta " +id+ " di " +type+ "\t\tvalore: "+ value +"\t\tTL: "+TL+ "\t\tTR: " +TR+ "\t\tBL: " +BL+ "\t\tBR: " +BR+ "\t\trequisiti: " +requirements);
            }

            // Chiudi il reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GoldDeck goldDeck = new GoldDeck(carteGold);
        goldDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        goldDeck.printDeck();

        List<Card> cartaIniziale = null;*/
        /*try {


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
        initialCardDeck.printDeck();*/

    }
}