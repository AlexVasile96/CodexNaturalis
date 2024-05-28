package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.InitialCardDeck;
import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InitCardConstructor implements CardConstructor {
    public Deck createCards() {
        List<Card> initCardList = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("initcard.json"); // Reading JSON file
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: initcard.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream)); // Converting JSON file
            JSONArray initMats = jsonObject.getJSONArray("init_cards"); // JSON array
            initCardList = new ArrayList<>(); // Creating a new ArrayList that contains all the cards

            for (int i = 0; i < initMats.length(); i++) { // For cycle in order to get all the JSON information
                JSONObject card = initMats.getJSONObject(i);
                int id = card.getInt("id"); // Card ID
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); // Card specific seed (plant, animal...)
                int value = card.getInt("value"); // The points the card can have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); // Get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); // Get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); // Get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); // Get the bottom right corner attribute
                SpecificSeed TL_INIT_BACK = SpecificSeed.valueOf(card.getString("TL_INIT_BACK"));
                SpecificSeed TR_INIT_BACK = SpecificSeed.valueOf(card.getString("TR_INIT_BACK"));
                SpecificSeed BL_INIT_BACK = SpecificSeed.valueOf(card.getString("BL_INIT_BACK"));
                SpecificSeed BR_INIT_BACK = SpecificSeed.valueOf(card.getString("BR_INIT_BACK"));
                Corner TL = new Corner(topLeft, 0, 0, type);
                Corner TR = new Corner(topRight, 0, 0, type);
                Corner BL = new Corner(bottomLeft, 0, 0, type);
                Corner BR = new Corner(bottomRight, 0, 0, type);
                Corner TL_I_BACK = new Corner(TL_INIT_BACK, 0, 0, type);
                Corner TR_I_BACK = new Corner(TR_INIT_BACK, 0, 0, type);
                Corner BL_I_BACK = new Corner(BL_INIT_BACK, 0, 0, type);
                Corner BR_I_BACK = new Corner(BR_INIT_BACK, 0, 0, type);

                JSONArray requirementsArray = card.getJSONArray("attributes");
                List<SpecificSeed> attributes = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    attributes.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }

                InitialCard cards = new InitialCard(id, type, value, TL, TR, BL, BR, TL_I_BACK, TR_I_BACK, BL_I_BACK, BR_I_BACK, attributes); // Creating all the resource cards
                initCardList.add(cards); // Adding card to the ArrayList previously created
            }
            inputStream.close();

        } catch (Exception e) { // Catching exceptions
            e.printStackTrace();
        }
        InitialCardDeck initialDeck = new InitialCardDeck(initCardList);
        return initialDeck;
    }
}
