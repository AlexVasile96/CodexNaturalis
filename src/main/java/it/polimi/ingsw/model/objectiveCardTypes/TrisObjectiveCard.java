package it.polimi.ingsw.model.objectiveCardTypes;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.BoardPoints;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.SpecificSeed;

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
private int countNumbersOfSpecificSeed(Board board, SpecificSeed seed){
    BoardPoints boardPoints = new BoardPoints();
    Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
    return seedCountMap.getOrDefault(seed, 0);
}

/*
Add 2 points for every triplet of the given SpecificSeed to player's score
 */
    public void updatePlayersScore(int numberOfSpecificSeed, Player player){
        player.setPlayerScore(player.getPlayerScore()+((numberOfSpecificSeed/3)*2));
    }
}
