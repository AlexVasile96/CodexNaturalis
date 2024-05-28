package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GoldCardConstructor implements CardConstructor {
    @Override
    public Deck createCards() {
        List<Card> goldCards = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("goldcard.json"); // Read from JSON file
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: goldcard.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray mazzoGold = jsonObject.getJSONArray("goldCard");
            goldCards = new ArrayList<>(); // Create ArrayList made up of gold cards

            for (int i = 0; i < mazzoGold.length(); i++) {
                JSONObject card = mazzoGold.getJSONObject(i);
                int id = card.getInt("id"); // Card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); // Card specific seed (plant, animal...)
                int value = card.getInt("value"); // These are the points that the card will have when placed
                SpecificSeed multiplier = SpecificSeed.valueOf(card.getString("multiplier"));
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL"));
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR"));
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL"));
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR"));
                Corner TL = new Corner(topLeft, 0, 0, type);
                Corner TR = new Corner(topRight, 0, 0, type);
                Corner BL = new Corner(bottomLeft, 0, 0, type);
                Corner BR = new Corner(bottomRight, 0, 0, type);
                JSONArray requirementsArray = card.getJSONArray("requirements");
                List<SpecificSeed> requirements = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    requirements.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                GoldCard cards = new GoldCard(id, type, value, multiplier, TL, TR, BL, BR, requirements); // Creating all the resource cards
                goldCards.add(cards); // Adding card to the ArrayList previously created
            }

            // Close the input stream
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GoldDeck(goldCards);
    }
}
