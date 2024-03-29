package com.example.proj;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardConstructor {
    public Deck createCards() //method to create cards
    {
        List<Card> resourceCardsList = null;
        try {
            FileReader reader = new FileReader("src/main/resources/carte.json"); //Reading json file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader)); //converting json file
            JSONArray mazzoRisorse = jsonObject.getJSONArray("risorse"); //json array
            resourceCardsList = new ArrayList<>(); //creating a new arryalist that contains all the cards
            for (int i = 0; i < mazzoRisorse.length(); i++) { //for cicle in order to get all the json information
                JSONObject card = mazzoRisorse.getJSONObject(i);
                int id = card.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); //card specific seed (plant,animal...)
                int value = card.getInt("value"); //that's the point the cart can have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); //get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); //get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); //get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); //get the bottom right corner attribute
                Corner TL= new Corner(topLeft);
                Corner TR= new Corner(topRight);
                Corner BL= new Corner(bottomLeft);
                Corner BR= new Corner(bottomRight);
                Card cards = new Card(id, type, value, TL, TR, BL, BR); //creating all the resource cards
                resourceCardsList.add(cards); //adding card to the arraylist previously created
            }
            reader.close();

        } catch (Exception e) { //catching exceptions
            e.printStackTrace();
        }
        //return resourceCardsList; PORCODIO
        ResourceDeck resourceDeck = new ResourceDeck(resourceCardsList);
        return resourceDeck;
        /*
        //deck.shuffle();
        resourceDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n"); //print the drown card
        //System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ resourceDeck.drawCard()+"\n\n\n");
        resourceDeck.printDeck();*/
    }
}
