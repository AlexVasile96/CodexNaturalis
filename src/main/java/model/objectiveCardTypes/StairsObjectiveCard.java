package model.objectiveCardTypes;

import model.game.Board;
import model.game.Node;
import model.game.Player;
import model.game.SpecificSeed;

public class StairsObjectiveCard implements ExtendExtendExtend {

    // Variable to track whether a specific pattern is found
    private boolean checkvariable = false; //non sono del tutto sicuro che checkvariable funzioni bene, dobbiamo darci un occhio


    // Method to check if a certain pattern is present on the board
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        checkIfDiagonalMadeByINSECTOrPLANT(nodes, rows, cols, seed, player); // Check if a diagonal pattern made by INSECT or PLANT is present
        checkIfDiagonalMadeByANIMALOrMUSHROOM(nodes, rows, cols, seed, player); // Check if a diagonal pattern made by ANIMAL or MUSHROOM is present

        checkvariable = resetCheckvariable(nodes, rows, cols); // Reset the checkvariable after pattern checks

        return checkvariable; // Return whether the pattern is found
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }

    // Method to check if a diagonal pattern made by INSECT or PLANT is present
    public void checkIfDiagonalMadeByINSECTOrPLANT(Node[][] nodes, int rows, int cols, SpecificSeed seed, Player player) {
        // Loop through each cell in the board
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {
                // Check if the cell contains INSECT or PLANT seed
                if (nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.INSECT || nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.PLANT) {
                    // Check if the diagonal pattern starting from this cell matches the seed
                    if ((nodes[row][columns].getSpecificNodeSeed() == seed && !nodes[row][columns].isAlreadyChecked()) &&
                            (nodes[row + 1][columns + 1].getSpecificNodeSeed() == seed && !nodes[row + 1][columns + 1].isAlreadyChecked()) &&
                            (nodes[row + 2][columns + 2].getSpecificNodeSeed() == seed && !nodes[row + 2][columns + 2].isAlreadyChecked())) {
                        // Mark the cells in the pattern as already checked
                        nodes[row][columns].setAlreadyChecked(true);
                        nodes[row + 1][columns + 1].setAlreadyChecked(true);
                        nodes[row + 2][columns + 2].setAlreadyChecked(true);
                        checkvariable = true; // Set checkvariable to true indicating the pattern is found
                        addStairObjectivePointsToPlayer(player); // Add points to the player for completing the pattern
                    }
                }
            }
        }
    }

    // Method to check if a diagonal pattern made by ANIMAL or MUSHROOM is present
    public void checkIfDiagonalMadeByANIMALOrMUSHROOM(Node[][] nodes, int rows, int cols, SpecificSeed seed, Player player) {
        // Loop through each cell in the board
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {
                // Check if the cell contains ANIMAL or MUSHROOM seed
                if (nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.ANIMAL || nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.MUSHROOM) {
                    // Check if the diagonal pattern starting from this cell matches the seed
                    if ((columns + 2) < cols) {
                        if ((nodes[row][columns + 2].getSpecificNodeSeed() == seed && !nodes[row][columns + 2].isAlreadyChecked()) &&
                                (nodes[row + 1][columns + 1].getSpecificNodeSeed() == seed && !nodes[row + 1][columns + 1].isAlreadyChecked()) &&
                                (nodes[row + 2][columns].getSpecificNodeSeed() == seed && !nodes[row + 2][columns].isAlreadyChecked())) {

                            // Mark the cells in the pattern as already checked
                            nodes[row][columns + 2].setAlreadyChecked(true);
                            nodes[row + 1][columns + 1].setAlreadyChecked(true);
                            nodes[row + 2][columns].setAlreadyChecked(true);
                            checkvariable = true; // Set checkvariable to true indicating the pattern is found
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
        System.out.println("player score is " + player.getPlayerScore());
    }

    public boolean resetCheckvariable(Node[][] nodes, int rows, int cols) { //forse questa non serve più perché checkvariable viene inizializzatra a false ogni volta che viene chiamata
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {
                nodes[row][columns].setAlreadyChecked(false);
            }
        }
        return checkvariable;
    }
}




    /*Metedo precedente, l'ho mantenuto nel caso servisse ancora

    for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {





                // Check diagonal pattern from top left to bottom right, -2 cause we don't want to exceed matrix limits
                if(nodes[row][columns].getSpecificNodeSeed()== SpecificSeed.INSECT || nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.PLANT) {
                    if ((nodes[row][columns].getSpecificNodeSeed() == seed && !nodes[row][columns].isAlreadyChecked())&&
                            (nodes[row + 1][columns + 1].getSpecificNodeSeed() == seed && !nodes[row + 1][columns + 1].isAlreadyChecked()) &&
                            (nodes[row + 2][columns + 2].getSpecificNodeSeed() == seed && !nodes[row + 2][columns + 2].isAlreadyChecked())) {
                        player.setPlayerScore(player.getPlayerScore() + 2);
                        nodes[row][columns].setAlreadyChecked(true);
                        nodes[row+1][columns+1].setAlreadyChecked(true);
                        nodes[row+2][columns+2].setAlreadyChecked(true);
                        System.out.println(player.getPlayerScore());
                        checkvariable=true;

                    }
                }else {
                    if (nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.ANIMAL || nodes[row][columns].getSpecificNodeSeed() == SpecificSeed.MUSHROOM) {
                        if ((columns + 2) < cols) {
                            if ((nodes[row][columns + 2].getSpecificNodeSeed() == seed && !nodes[row][columns + 2].isAlreadyChecked()) &&
                                    (nodes[row + 1][columns + 1].getSpecificNodeSeed() == seed && !nodes[row + 1][columns + 1].isAlreadyChecked()) &&
                                    (nodes[row + 2][columns].getSpecificNodeSeed() == seed && !nodes[row + 2][columns].isAlreadyChecked())) {
                                player.setPlayerScore(player.getPlayerScore() + 2);
                                nodes[row][columns + 2].setAlreadyChecked(true);
                                nodes[row + 1][columns + 1].setAlreadyChecked(true);
                                nodes[row + 2][columns].setAlreadyChecked(true);
                                System.out.println(player.getPlayerScore());
                                checkvariable = true;
                            }//PERCHè VENGONO MESSI TUTTI A TRUE E NON SOLO QUELLI SCELTI DA ME?
                        }
                    }
                }
            }
        }*/



