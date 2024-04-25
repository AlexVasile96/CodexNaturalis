package model.objectiveCardTypes;

import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

public class MixObjectiveCard implements ExtendExtendExtend {
    private final int pointsForEachMicObjectiveCard = 3;
    // Method to check the pattern on the board
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {//CONTI I 3 SIMBOLI PARTICOLARI, TIENI IL NUMERO MINOR DEI 3 SIMBOLI (ES SE HO 2 3 3) TENGO 2, FACCIO 2* 3. Le regole non sono chiare perché dicono 3 punti per ogni set di 3 attributi diversi ma non avrebbe senso
        BoardPoints boardPoints = new BoardPoints(); // Create an instance of BoardPoints to calculate points
        int[] counterOfMinimumPoints = boardPoints.countPointsForObjectiveCards(board); // Count the number of minimum points for the objective cards
        return addMixObjectiveCardPointsToPlayer(counterOfMinimumPoints, player); // Add points to the player based on the minimum count of each seed type;
    }


    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
    //CONTI I 3 SIMBOLI PARTICOLARI, TIENI IL NUMERO MINOR DEI 3 SIMBOLI (ES SE HO 2 3 3) TENGO 2, FACCIO 2* 3.



    // Method to add points to the player based on the counts of seed types
    public boolean addMixObjectiveCardPointsToPlayer(int[] counterOfMinimumPoints, Player player) {
        // Find the minimum count among the three seed types
        int minimo = counterOfMinimumPoints[0];
        if (counterOfMinimumPoints[1] < minimo) {
            minimo = counterOfMinimumPoints[1];
        }
        if (counterOfMinimumPoints[2] < minimo) {
            minimo = counterOfMinimumPoints[2];
        }

        int points = minimo * pointsForEachMicObjectiveCard; // Calculate points based on the minimum count and add to player's score
        player.setPlayerScore(player.getPlayerScore() + points);
        return true;
    }
}