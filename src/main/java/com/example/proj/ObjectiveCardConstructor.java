package com.example.proj;



import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjectiveCardConstructor {
    public ObjectiveDeck createCards() {
        List<ObjectiveCard> objectiveCardList = null;
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
                int numberOfWhenTheGameEnds = card.getInt("numberOFWhenTheGameEnds"); //card id
                ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard = ObjectiveSpecificTypeOfCard.valueOf(card.getString("objectiveSpecificTypeOfCard")); //card specific seed (plant,animal...)
                ObjectiveCard cards = new ObjectiveCard(value,id,type,numberOfWhenTheGameEnds,objectiveSpecificTypeOfCard); //creating all the resource cards
                objectiveCardList.add(cards); //adding card to th arraylist previously created
            }

            // Chiudi il reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectiveDeck objectiveDeck= new ObjectiveDeck(objectiveCardList);
        return objectiveDeck;


    }
}
