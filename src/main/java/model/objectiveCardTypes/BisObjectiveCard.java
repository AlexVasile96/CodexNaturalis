package model.objectiveCardTypes;
import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BisObjectiveCard implements ExtendExtendExtend {

    /*
    This method checks if the player has at least 2 card of the given specific seed in order to add
    2 points to the player's score
     */
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        System.out.println("Specific seed is "+ seed);
        //Getting number of the SpecificSeed
        if (countNumbersOfSpecificSeed(board, seed) >= 2) {
            //update player's score
            updatePlayersScore(countNumbersOfSpecificSeed(board, seed), player);
            System.out.println("Number of " + seed + " SpecificSeed: " + countNumbersOfSpecificSeed(board, seed) );
            System.out.println("Player score: " + player.getPlayerScore());
            return true;
        }else{
            System.out.println("You don't have enough points!");
            return false;
        }
    }

    public String getNumOfSpecificSeedString(SpecificSeed seed) throws IOException {
        String seedToString = seed.toString();
        String regex = seedToString + "=(\\d{1,2})"; // Modifica la regex per catturare una o due cifre
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(seedToString);

        if (matcher.find()) {
            String matchedGroup = matcher.group(1); // Ottieni il gruppo catturato
            if (matchedGroup.length() == 1 || matchedGroup.length() == 2) {
                return matchedGroup;
            }
        }

        return null;
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
