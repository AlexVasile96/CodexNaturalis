package com.example.proj;
import java.util.Map;

public class TrisObjectiveCard implements ExtendExtendExtend{
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        BoardPoints boardPoints = new BoardPoints();
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
        int numberOfSpecificSeed = seedCountMap.get(seed);
        int value= (numberOfSpecificSeed/3)*2;
        System.out.println("Number of " + seed + " SpecificSeed: " + numberOfSpecificSeed);
        player.setPlayerScore(player.getPlayerScore()+ value);
        System.out.println("Player score: " + player.getPlayerScore());
        if (numberOfSpecificSeed >= 3) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
}
