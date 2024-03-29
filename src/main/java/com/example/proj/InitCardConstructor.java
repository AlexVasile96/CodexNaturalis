package com.example.proj;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class InitCardConstructor{
    public InitialCardDeck createCards() //method to create cards
    {
        List<InitialCard> initCardList = null;
        try {
            FileReader reader = new FileReader("src/main/resources/initcard.json"); //Reading json file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader)); //converting json file
            JSONArray initMats = jsonObject.getJSONArray("init_cards"); //json array
            initCardList = new ArrayList<>(); //creating a new arryalist that contains all the cards
            for (int i = 0; i < initMats.length(); i++) { //for cicle in order to get all the json information
                JSONObject card = initMats.getJSONObject(i);
                int id = card.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); //card specific seed (plant,animal...)
                int value = card.getInt("value"); //that's the point the cart can have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); //get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); //get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); //get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); //get the bottom right corner attribute
                Corner TL = new Corner(topLeft);
                Corner TR = new Corner(topRight);
                Corner BL = new Corner(bottomLeft);
                Corner BR = new Corner(bottomRight);
                JSONArray requirementsArray = card.getJSONArray("attributes");
                List<SpecificSeed> attributes = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    attributes.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                InitialCard cards = new InitialCard(id,type, value, TL, TR, BL, BR,attributes); //creating all the resource cards
                initCardList.add(cards); //adding card to the arraylist previously created
            }
            reader.close();

        } catch (Exception e) { //catching exceptions
            e.printStackTrace();
        }
        InitialCardDeck initialCardDeck = new InitialCardDeck(initCardList);
        return initialCardDeck;

    }
}
