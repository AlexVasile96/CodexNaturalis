package model.card;

import model.deck.Deck;
import model.deck.ObjectiveDeck;
import model.game.Corner;
import model.game.ObjectiveSpecificTypeOfCard;
import model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectiveCardConstructor {
    public Deck createCards() {
        List<Card> objectiveCardList = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("objectivecard.json"); // Read from JSON file
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: objectivecard.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray mazzoObjective = jsonObject.getJSONArray("objective");
            objectiveCardList = new ArrayList<>(); // Create ArrayList made up of objective cards

            for (int i = 0; i < mazzoObjective.length(); i++) {
                JSONObject card = mazzoObjective.getJSONObject(i);
                int id = card.getInt("id"); // Card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); // Card specific seed (plant, animal...)
                System.out.println(type);
                int value = card.getInt("value"); // These are the points that the card will have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); // Get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); // Get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); // Get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); // Get the bottom right corner attribute
                Corner TL = new Corner(topLeft, 0, 0, type);
                Corner TR = new Corner(topRight, 0, 0, type);
                Corner BL = new Corner(bottomLeft, 0, 0, type);
                Corner BR = new Corner(bottomRight, 0, 0, type);
                int numberOfWhenTheGameEnds = card.getInt("numberOfWhenTheGameEnds"); // Number of times when the game ends
                ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard = ObjectiveSpecificTypeOfCard.valueOf(card.getString("objectiveSpecificTypeOfCard")); // Specific type of card

                ObjectiveCard cards = new ObjectiveCard(id, type, value, TL, TR, BL, BR, numberOfWhenTheGameEnds, objectiveSpecificTypeOfCard); // Creating all the resource cards
                System.out.println(cards);
                objectiveCardList.add(cards); // Adding card to the ArrayList previously created
            }

            // Close the input stream
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ObjectiveDeck(objectiveCardList);
    }
}
