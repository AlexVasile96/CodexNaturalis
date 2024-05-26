package model.card;
import exceptions.CantCreateCardsException;
import model.game.Corner;
import model.deck.Deck;
import model.deck.ResourceDeck;
import model.game.SpecificSeed;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceCardConstructor implements CardConstructor{
    @Override
    public Deck createCards() {
        List<Card> resourceCardsList = null; //INITIALIZING THE CONSTRUCTOR
        try {
            FileReader reader = new FileReader("src/main/resources/carte.json");    //Reading json file
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));                //converting json file
            JSONArray mazzoRisorse = jsonObject.getJSONArray("risorse");                //json array
            resourceCardsList = new ArrayList<>();                                           //creating a new arraylist that contains all the cards
            for (int i = 0; i < mazzoRisorse.length(); i++) {                               //for cicle in order to get all the json information
                JSONObject card = mazzoRisorse.getJSONObject(i);
                int id = card.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(card.getString("type")); //card specific seed (plant,animal...)
                int value = card.getInt("value"); //that's the point the cart can have when placed
                SpecificSeed topLeft = SpecificSeed.valueOf(card.getString("TL")); //get the top left corner attribute
                SpecificSeed topRight = SpecificSeed.valueOf(card.getString("TR")); //get the top right corner attribute
                SpecificSeed bottomLeft = SpecificSeed.valueOf(card.getString("BL")); //get the bottom left corner attribute
                SpecificSeed bottomRight = SpecificSeed.valueOf(card.getString("BR")); //get the bottom right corner attribute
                Corner TL= new Corner(topLeft,0,0,type);
                Corner TR= new Corner(topRight,0,0,type);
                Corner BL= new Corner(bottomLeft,0,0,type);
                Corner BR= new Corner(bottomRight,0,0,type);
                Card cards = new Card(id, type, value, TL, TR, BL, BR);                 //creating all the resource cards

                resourceCardsList.add(cards);                                           //adding card to the arraylist previously created
            }
            reader.close();

        } catch (Exception e) { //catching exceptions
            throw new CantCreateCardsException("Coudn't create cards.", e);
        }
        return new ResourceDeck(resourceCardsList);                                             //returning the resource deck (INTELLIJ SUGGESTION)
    }
    }

