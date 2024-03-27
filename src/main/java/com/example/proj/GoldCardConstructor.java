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
        List<Card> carteGold = null;
        try {
            FileReader reader = new FileReader("src/main/resources/goldcard.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray mazzoGold = jsonObject.getJSONArray("goldCard");
            carteGold = new ArrayList<>();

            for (int i = 0; i < mazzoGold.length(); i++) {
                JSONObject carta = mazzoGold.getJSONObject(i);

                int id = carta.getInt("id"); //card id
                SpecificSeed type = SpecificSeed.valueOf(carta.getString("type")); //card specific seed (plant,animal...)
                int value = carta.getInt("value"); //that's the point the cart can have when placed
                SpecificSeed giacomo = SpecificSeed.valueOf(carta.getString("TL"));
                SpecificSeed pippo = SpecificSeed.valueOf(carta.getString("TR"));
                SpecificSeed pluto = SpecificSeed.valueOf(carta.getString("BL"));
                SpecificSeed paperino = SpecificSeed.valueOf(carta.getString("BR"));
                Corner TL= new Corner(giacomo);
                Corner TR= new Corner(pippo);
                Corner BL= new Corner(pluto);
                Corner BR= new Corner(paperino);
                JSONArray requirementsArray = carta.getJSONArray("requirements");
                List<SpecificSeed> requirements = new ArrayList<>();
                for (int j = 0; j < requirementsArray.length(); j++) {
                    requirements.add(SpecificSeed.valueOf(requirementsArray.getString(j)));
                }
                GoldCard cards = new GoldCard(id, type, value, TL, TR, BL, BR,requirements); //creating all the resource cards
                carteGold.add(cards); //adding card to th arraylist previously created
            }

            // Chiudi il reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GoldDeck goldDeck = new GoldDeck(carteGold);
        goldDeck.printDeck();
        System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        //System.out.println("\n\n\ncarta pescata: "+ goldDeck.drawCard()+"\n\n\n");
        goldDeck.printDeck();

    }
}
