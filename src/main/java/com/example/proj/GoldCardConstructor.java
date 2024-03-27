package com.example.proj;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GoldCardConstructor extends CardConstructor{
    @Override
    public void createCards() {
        List<Card> goldCards = null;
        try {
            FileReader reader = new FileReader("src/main/resources/goldcard.json"); //read from JSON file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray mazzoGold = jsonObject.getJSONArray("goldCard");
            goldCards = new ArrayList<>(); //create arrayList made up of gold cards

            for (int i = 0; i < mazzoGold.length(); i++) {
                JSONObject card = mazzoGold.getJSONObject(i);

                int id = card.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); //card specific seed (plant,animal...)
                int value = card.getInt("value"); //these are the points that the card will have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL"));
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR"));
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL"));
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR"));
                Corner TL= new Corner(topLeft);
                Corner TR= new Corner(topRight);
                Corner BL= new Corner(bottomLeft);
                Corner BR= new Corner(bottomRight);
                JSONArray requirementsArray = card.getJSONArray("requirements");
                List<SpecificSeed> requirements = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    requirements.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                GoldCard cards = new GoldCard(id, type, value, TL, TR, BL, BR,requirements); //creating all the resource cards
                goldCards.add(cards); //adding card to th arraylist previously created
            }

            // Chiudi il reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GoldDeck goldDeck = new GoldDeck(goldCards);
        goldDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        goldDeck.printDeck();

    }
}
