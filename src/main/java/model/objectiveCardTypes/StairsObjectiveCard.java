package model.objectiveCardTypes;

import model.game.Board;
import model.game.Node;
import model.game.Player;
import model.game.SpecificSeed;

public class StairsObjectiveCard implements ExtendExtendExtend {

    // Variable to track whether a specific pattern is found
    private boolean checkVariable = false;


    // Method to check if a certain pattern is present on the board
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        checkIfDiagonalMadeByINSECTOrPLANT(nodes, rows, cols, seed, player); // Check if a diagonal pattern made by INSECT or PLANT is present
        checkIfDiagonalMadeByANIMALOrMUSHROOM(nodes, rows, cols, seed, player); // Check if a diagonal pattern made by ANIMAL or MUSHROOM is present

        checkVariable = resetCheckvariable(nodes, rows, cols); // Reset the check variable after pattern checks

        return checkVariable; // Return whether the pattern is found
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }

    // Method to check if a diagonal pattern made by INSECT or PLANT is present
    public void checkIfDiagonalMadeByINSECTOrPLANT(Node[][] nodes, int rows, int cols, SpecificSeed seed, Player player) {
        // Loop through each cell in the board
        for (int row = 0; row < rows ; row++) {
            for (int columns = 0; columns < cols ; columns++) {
                // Check if the cell contains INSECT or PLANT seed
                if (nodes[row][columns].getFirstPlacement() == SpecificSeed.INSECT || nodes[row][columns].getFirstPlacement() == SpecificSeed.PLANT) {
                    // Check if the diagonal pattern starting from this cell matches the seed
                    if ((nodes[row][columns].getFirstPlacement() == seed && !nodes[row][columns].isAlreadyChecked()) &&
                            (nodes[row + 1][columns + 1].getFirstPlacement() == seed && !nodes[row + 1][columns + 1].isAlreadyChecked()) &&
                            (nodes[row + 2][columns + 2].getFirstPlacement() == seed && !nodes[row + 2][columns + 2].isAlreadyChecked())) {
                        // Mark the cells in the pattern as already checked
                        nodes[row][columns].setAlreadyChecked(true);
                        nodes[row + 1][columns + 1].setAlreadyChecked(true);
                        nodes[row + 2][columns + 2].setAlreadyChecked(true);
                        checkVariable = true; // Set check variable to true indicating the pattern is found
                        addStairObjectivePointsToPlayer(player); // Add points to the player for completing the pattern
                    }
                }
            }
        }
    }

    // Method to check if a diagonal pattern made by ANIMAL or MUSHROOM is present
    public void checkIfDiagonalMadeByANIMALOrMUSHROOM(Node[][] nodes, int rows, int cols, SpecificSeed seed, Player player) {
        // Loop through each cell in the board
        for (int row = 0; row < rows; row++) {
            for (int columns = 0; columns < cols; columns++) {
                // Check if the cell contains ANIMAL or MUSHROOM seed
                if (nodes[row][columns].getFirstPlacement() == SpecificSeed.ANIMAL || nodes[row][columns].getFirstPlacement() == SpecificSeed.MUSHROOM) {
                    // Check if the diagonal pattern starting from this cell matches the seed
                    if ((columns + 2) < cols) {
                        if ((nodes[row][columns].getFirstPlacement() == seed && !nodes[row][columns].isAlreadyChecked()) &&
                                (nodes[row][columns+1].getFirstPlacement() == seed && !nodes[row][columns+1].isAlreadyChecked())&&
                                (nodes[row + 1][columns].getFirstPlacement() == seed && !nodes[row + 1][columns ].isAlreadyChecked() && nodes[row + 1][columns].getSecondPlacement() == seed) &&
                                (nodes[row + 2][columns-1].getFirstPlacement() == seed && !nodes[row + 2][columns-1].isAlreadyChecked()) && nodes[row + 2][columns-1].getSecondPlacement() == seed ) {
                            // Mark the cells in the pattern as already checked
                            //first correct card
                            nodes[row][columns].setAlreadyChecked(true); //22-24
                            nodes[row][columns+1].setAlreadyChecked(true); //22-25
                            nodes[row+1][columns].setAlreadyChecked(true); //23-24
                            nodes[row+1][columns+1].setAlreadyChecked(true); //23-25

                            //second correct card
                            nodes[row+1][columns-1].setAlreadyChecked(true); //23-23
                            nodes[row+2][columns-1].setAlreadyChecked(true); //24-23
                            nodes[row+2][columns].setAlreadyChecked(true); //24-24
                            //third correct card
                            nodes[row+2][columns-2].setAlreadyChecked(true); //24-22
                            nodes[row+3][columns-2].setAlreadyChecked(true); //25-22
                            nodes[row+3][columns-1].setAlreadyChecked(true); //25-23
                            checkVariable = true; // Set check variable to true indicating the pattern is found
                            addStairObjectivePointsToPlayer(player); // Add points to the player for completing the pattern
                        }
                    }
                }
            }
        }
    }

    // Method to add points to the player for completing the pattern
    public void addStairObjectivePointsToPlayer(Player player) {
        int pointsToAddForEachStairCompleted = 2;
        player.setPlayerScore(player.getPlayerScore() + pointsToAddForEachStairCompleted);
        System.out.println("New player score is " + player.getPlayerScore());
    }

    public boolean resetCheckvariable(Node[][] nodes, int rows, int cols) {
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {
                nodes[row][columns].setAlreadyChecked(false);
            }
        }
        return checkVariable;
    }
}



