package model.game;

import model.card.Card;
import java.util.HashMap;
import java.util.Map;

public class BoardPoints {
    public Map<SpecificSeed, Integer> countPoints(Board board) {
        Map<SpecificSeed, Integer> seedCountMap = new HashMap<>();
        for (SpecificSeed seed : SpecificSeed.values()) {
            seedCountMap.put(seed, 0);
        }
        //Controlliamo se dobbiamo aggiungere gli attributi
        for(Card card : board.getCardsOnTheBoardList()){            //checking all the cards
            if(card.isCardBack()){                                  //se il cardback è true, allora aggiungi gli attributi
                for (SpecificSeed seed : card.getAttributes()) {
                    seedCountMap.put(seed, seedCountMap.get(seed) + 1);
                }
            }
        }
        for (int i = 0; i < board.getNodes().length; i++) {
            for (int j = 0; j < board.getNodes()[i].length; j++) {
                SpecificSeed seed = board.getNodes()[i][j].getSpecificNodeSeed();
                seedCountMap.put(seed, seedCountMap.get(seed) + 1);
            }
        }
        System.out.println("Points on the map: " + seedCountMap);
        return seedCountMap;
    }

    public int[] countPointsForObjectiveCards(Board board) {
        int INKWELLCount=0;
        int PARCHMENTCount=0;
        int FEATHERCount=0;
        int[] attributesCounter = new int[3];
        Map<SpecificSeed, Integer> seedCountMap = new HashMap<>();
        for (SpecificSeed seed : SpecificSeed.values()) {
            seedCountMap.put(seed, 0);
        }
        for (int i = 0; i < board.getNodes().length; i++) {
            for (int j = 0; j < board.getNodes()[i].length; j++) {
                SpecificSeed seed = board.getNodes()[i][j].getSpecificNodeSeed();
                seedCountMap.put(seed, seedCountMap.get(seed) + 1);
                if(board.getNodes()[i][j].getSpecificNodeSeed() == SpecificSeed.INKWELL) {
                    INKWELLCount++;
                }
                if(board.getNodes()[i][j].getSpecificNodeSeed() == SpecificSeed.PARCHMENT) {
                    PARCHMENTCount++;
                }
                if(board.getNodes()[i][j].getSpecificNodeSeed() == SpecificSeed.FEATHER) {
                    FEATHERCount++;
                }
            }
        }
        System.out.println("INKWELL: " + INKWELLCount+" PARCHMENT: " + PARCHMENTCount+" FEATHER: "+FEATHERCount);
        System.out.println("Trattini trattini trattini " + seedCountMap);
        attributesCounter[0]= INKWELLCount;
        attributesCounter[1]= PARCHMENTCount;
        attributesCounter[2]= FEATHERCount;
        return  attributesCounter;
    }

    public int addictionalPointsForGoldCards(Board board, SpecificSeed specificSeed) {
        int seedCount = 0;
        for (int i = 0; i < board.getNodes().length; i++) {
            for (int j = 0; j < board.getNodes()[i].length; j++) {
                SpecificSeed seed = board.getNodes()[i][j].getSpecificNodeSeed();
                if (seed == specificSeed) {
                    seedCount++;
                }
            }
        }
        return seedCount;
    }
}