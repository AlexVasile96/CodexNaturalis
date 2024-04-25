package model.objectiveCardTypes;
import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

import java.util.Map;

public class BisObjectiveCard implements ExtendExtendExtend {

    /*
    This method checks if the player has at least 2 card of the given specific seed in order to add
    2 points to the player's score
     */
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {

        //Getting number of the SpecificSeed
        if (countNumbersOfSpecificSeed(board, seed) >= 2) {
            //update player's score
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
    private int countNumbersOfSpecificSeed(Board board, SpecificSeed seed){
        BoardPoints boardPoints = new BoardPoints();
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
        return seedCountMap.get(seed);
    }

    /*
    Add 2 points for every couple of the given SpecificSeed to the player's score
     */
    private void updatePlayersScore(int numberOfSpecificSeed, Player player){
        player.setPlayerScore(player.getPlayerScore()+((numberOfSpecificSeed/2)*2));
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
}
