package model.objectiveCardTypes;
import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

import java.util.Map;

public class BisObjectiveCard implements ExtendExtendExtend {
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        BoardPoints boardPoints = new BoardPoints();                                    //Initializing BoardPoints to count the number of Specific_seed on the board
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);       //Creating Map
        int numberOfSpecificSeed = seedCountMap.get(seed); //5                          //Gettin number of the specificseed
        int redistributable= (numberOfSpecificSeed/2)*2;
        System.out.println("Number of " + seed + " SpecificSeed: " + numberOfSpecificSeed);
        player.setPlayerScore(player.getPlayerScore()+ redistributable);
        System.out.println("Player score: " + player.getPlayerScore());
        if (numberOfSpecificSeed >= 2) {
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
