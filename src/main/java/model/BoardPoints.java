package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardPoints {
    private ArrayList<SpecificSeed> pointsOnTheBoard;
    private int pointss;
    public Map<SpecificSeed, Integer> countPoints(Board board) {
        Map<SpecificSeed, Integer> seedCountMap = new HashMap<>();
        for (SpecificSeed seed : SpecificSeed.values()) {
            seedCountMap.put(seed, 0);
        }
        for (int i = 0; i < board.getNodes().length; i++) {
            for (int j = 0; j < board.getNodes()[i].length; j++) {
                SpecificSeed seed = board.getNodes()[i][j].getSpecificNodeSeed();
                seedCountMap.put(seed, seedCountMap.get(seed) + 1);
            }
        }
        System.out.println("Trattini trattini trattini " + seedCountMap);
        return seedCountMap;
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










    public ArrayList<SpecificSeed> getPointsOnTheBoard() {
        return pointsOnTheBoard;
    }
    public void setPointsOnTheBoard(ArrayList<SpecificSeed> pointsOnTheBoard) {
        this.pointsOnTheBoard = pointsOnTheBoard;
    }
    public int getPointss() {
        return pointss;
    }
    public void setPointss(int pointss) {
        this.pointss = pointss;
    }
}
