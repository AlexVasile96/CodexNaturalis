package com.example.proj;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjectiveCardConstructor {
    public Deck createCards() {
        List<Card> objectiveCardList = null;
        try {
            FileReader reader = new FileReader("src/main/resources/objectivecard.json"); //read from JSON file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray mazzoObjective = jsonObject.getJSONArray("objective");
            objectiveCardList = new ArrayList<>(); //create arrayList made up of gold cards

            for (int i = 0; i < mazzoObjective.length(); i++) {
                JSONObject card = mazzoObjective.getJSONObject(i);
                int id = card.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); //card specific seed (plant,animal...)
                int value = card.getInt("value"); //these are the points that the card will have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); //get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); //get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); //get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); //get the bottom right corner attribute
                Corner TL = new Corner(topLeft,0,0);
                Corner TR = new Corner(topRight,0,0);
                Corner BL = new Corner(bottomLeft,0,0);
                Corner BR = new Corner(bottomRight,0,0);
                int numberOfWhenTheGameEnds = card.getInt("numberOfWhenTheGameEnds"); //card id
                ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard = ObjectiveSpecificTypeOfCard.valueOf(card.getString("objectiveSpecificTypeOfCard")); //card specific seed (plant,animal...)
                ObjectiveCard cards = new ObjectiveCard(id,type,value,TL,TR, BL, BR, numberOfWhenTheGameEnds,objectiveSpecificTypeOfCard); //creating all the resource cards
                objectiveCardList.add(cards); //adding card to th arraylist previously created
            }

            // Chiudi il reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ObjectiveDeck(objectiveCardList);
    }
}
