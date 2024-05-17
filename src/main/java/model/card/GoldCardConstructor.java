package model.card;


import model.game.Corner;
import model.deck.Deck;
import model.deck.GoldDeck;
import model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GoldCardConstructor implements CardConstructor{
    @Override
    public Deck createCards() {
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
                SpecificSeed multiplier = SpecificSeed.valueOf(card.getString("multiplier"));
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL"));
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR"));
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL"));
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR"));
                Corner TL= new Corner(topLeft,0,0,type);
                Corner TR= new Corner(topRight,0,0,type);
                Corner BL= new Corner(bottomLeft,0,0,type);
                Corner BR= new Corner(bottomRight,0,0,type);
                JSONArray requirementsArray = card.getJSONArray("requirements");
                List<SpecificSeed> requirements = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    requirements.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                GoldCard cards = new GoldCard(id, type, value, multiplier, TL, TR, BL, BR,requirements); //creating all the resource cards
                goldCards.add(cards); //adding card to th arraylist previously created
            }


            reader.close(); // closing the reader
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GoldDeck(goldCards);
    }
}
