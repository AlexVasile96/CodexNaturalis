package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exceptions.CantCreateCardsException;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceCardConstructor implements CardConstructor {
    @Override
    public Deck createCards() {
        List<Card> resourceCardsList = null; // Initializing the constructor
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("carte.json"); // Reading json file
            if (inputStream == null) {
                throw new IllegalStateException();
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream)); // Converting json file
            JSONArray mazzoRisorse = jsonObject.getJSONArray("risorse"); // JSON array
            resourceCardsList = new ArrayList<>(); // Creating a new arraylist that contains all the cards

            for (int i = 0; i < mazzoRisorse.length(); i++) { // For cycle in order to get all the JSON information
                JSONObject card = mazzoRisorse.getJSONObject(i);
                int id = card.getInt("id"); // Card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); // Card specific seed (plant, animal...)
                int value = card.getInt("value"); // That's the point the card can have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); // Get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); // Get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); // Get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); // Get the bottom right corner attribute
                Corner TL = new Corner(topLeft, 0, 0, type);
                Corner TR = new Corner(topRight, 0, 0, type);
                Corner BL = new Corner(bottomLeft, 0, 0, type);
                Corner BR = new Corner(bottomRight, 0, 0, type);
                Card cards = new Card(id, type, value, TL, TR, BL, BR); // Creating all the resource cards

                resourceCardsList.add(cards); // Adding card to the arraylist previously created
            }
            inputStream.close();

        } catch (Exception e) { // Catching exceptions
            throw new CantCreateCardsException("Couldn't create cards.", e);
        }
        return new ResourceDeck(resourceCardsList); // Returning the resource deck (INTELLIJ SUGGESTION)
    }
}
