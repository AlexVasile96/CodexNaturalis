package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardPoints {
    public Map<SpecificSeed, Integer> countPoints(Board board) {
        int INKWELLCount=0;
        int PARCHMENTCount=0;
        int FEATHERCount=0;
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
        return seedCountMap;
    }

    public int[] countPointsForObjectiveCards(Board board) {
        int INKWELLCount=0;
        int PARCHMENTCount=0;
        int FEATHERCount=0;
        int[] sucascuaCounter = new int[3];
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
        sucascuaCounter[0]= INKWELLCount;
        sucascuaCounter[1]= PARCHMENTCount;
        sucascuaCounter[2]= FEATHERCount;
        return  sucascuaCounter;
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
