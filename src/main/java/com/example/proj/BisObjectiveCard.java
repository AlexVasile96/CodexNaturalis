package com.example.proj;

import java.util.Map;

public class BisObjectiveCard implements ExtendExtendExtend{
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        BoardPoints boardPoints = new BoardPoints();
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
        int numberOfSpecificSeed = seedCountMap.get(seed); //5
        int valuedaattribuire= (numberOfSpecificSeed/2)*2;
        System.out.println("Number of " + seed + " sbleurus: " + numberOfSpecificSeed);
        player.setPlayerScore(player.getPlayerScore()+ valuedaattribuire);
        System.out.println("Player score: " + player.getPlayerScore());
        if (numberOfSpecificSeed >= 3) {
            return true;
        } else {
            return false;
        }
    }
}
