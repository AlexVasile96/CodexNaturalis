package com.example.proj;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardConstructor {
    public void createResourceCards()
    {
        List<Card> resourceCardsList = null;
        try {

            /*
            this makes possible to save all the resource information in our cards
            */

            FileReader reader = new FileReader("src/main/resources/carte.json"); //Reading json file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader)); //converting json file
            JSONArray mazzoRisorse = jsonObject.getJSONArray("risorse"); //json array
            resourceCardsList = new ArrayList<>(); //creating a new arryalist that contains all the cards
            for (int i = 0; i < mazzoRisorse.length(); i++) { //for cicle in order to get all the json information
                JSONObject carta = mazzoRisorse.getJSONObject(i);
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
                Card card = new Card(id, type, value, TL, TR, BL, BR); //creating all the resource cards
                resourceCardsList.add(card); //adding card to the arraylist previously created
            }
            reader.close();

        } catch (Exception e) { //catching exceptions
            e.printStackTrace();
        }
        ResourceDeck resourceDeck = new ResourceDeck(resourceCardsList);
        //deck.shuffle();
        resourceDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n");
        resourceDeck.printDeck();
    }
}
