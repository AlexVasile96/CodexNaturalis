package it.polimi.ingsw.model.objectiveCardTypes;

import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.BoardPoints;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.SpecificSeed;

public class MixObjectiveCard implements ExtendExtendExtend {
    // Method to check the pattern on the board
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        BoardPoints boardPoints = new BoardPoints(); // Create an instance of BoardPoints to calculate points
        int[] counterOfMinimumPoints = boardPoints.countPointsForObjectiveCards(board); // Count the number of minimum points for the objective cards
        return addMixObjectiveCardPointsToPlayer(counterOfMinimumPoints, player); // Add points to the player based on the minimum count of each seed type;
    }

    //Count the 3 particular seed, keep the lowest number of the seeds (ex if I got 2 3 3, keep 2), do 2x3
    // Method to add points to the player based on the counts of seed types

    private boolean addMixObjectiveCardPointsToPlayer(int[] counterOfMinimumPoints, Player player) {
        // Find the minimum count among the three seed types
        int minimum = counterOfMinimumPoints[0];
        if (counterOfMinimumPoints[1] < minimum) {
            minimum = counterOfMinimumPoints[1];
        }
        if (counterOfMinimumPoints[2] < minimum) {
            minimum = counterOfMinimumPoints[2];
        }
        int pointsForEachMicObjectiveCard = 3;
        int points = minimum * pointsForEachMicObjectiveCard; // Calculate points based on the minimum count and add to player's score
        player.setPlayerScore(player.getPlayerScore() + points);
        return true;
    }

}