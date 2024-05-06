package model.objectiveCardTypes;
import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

import java.util.Map;

public class TrisObjectiveCard implements ExtendExtendExtend {
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        if (countNumbersOfSpecificSeed(board, seed) >= 3) {
            updatePlayersScore(countNumbersOfSpecificSeed(board, seed), player);
            System.out.println("Number of " + seed + " SpecificSeed: " + countNumbersOfSpecificSeed(board, seed) );
            System.out.println("Player score: " + player.getPlayerScore());
            return true;
        }else{
            return false;
        }
    }

/*
Get the number of given SpecificSeed on the board
 */
    public int countNumbersOfSpecificSeed(Board board, SpecificSeed seed){
        BoardPoints boardPoints = new BoardPoints();
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
        return seedCountMap.get(seed);
    }

/*
Add 2 points for every triplet of the given SpecificSeed to player's score
 */
    public void updatePlayersScore(int numberOfSpecificSeed, Player player){
        player.setPlayerScore(player.getPlayerScore()+((numberOfSpecificSeed/3)*2));
    }


    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
}
