package model.objectiveCardTypes;

import model.card.Card;
import model.game.*;

public class OnePlantAndTwoMushrooms
{
    public void realCheck(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;

        /*for (int row = 0; row < rows - 1; row++) { // Adjusted to prevent out of bounds
            for (int column = 0; column < cols - 1; column++) { // Adjusted to prevent out of bounds

                // Check the 2x2 grid of corners
                Corner currentCorner = nodes[row][column].getCorner();
                Corner rightCorner = nodes[row][column + 1].getCorner();
                Corner belowCorner = nodes[row + 1][column].getCorner();
                Corner diagonalCorner = nodes[row + 1][column + 1].getCorner();

                if (isMushroomCorner(currentCorner) && isMushroomCorner(rightCorner) &&
                        isMushroomCorner(belowCorner) && isMushroomCorner(diagonalCorner) &&
                        !nodes[row][column].isAlreadyChecked() &&
                        !nodes[row][column + 1].isAlreadyChecked() &&
                        !nodes[row + 1][column].isAlreadyChecked() &&
                        !nodes[row + 1][column + 1].isAlreadyChecked()) {

                    // Check if the node contains a plant card
                    if ((currentCorner.getCardSeed() == SpecificSeed.PLANT &&
                            rightCorner.getCardSeed() == SpecificSeed.PLANT &&
                            belowCorner.getCardSeed() == SpecificSeed.PLANT &&
                            diagonalCorner.getCardSeed() == SpecificSeed.PLANT) ||

                            (currentCorner.getCardSeed() == SpecificSeed.MUSHROOM &&
                                    rightCorner.getCardSeed() == SpecificSeed.MUSHROOM &&
                                    belowCorner.getCardSeed() == SpecificSeed.MUSHROOM &&
                                    diagonalCorner.getCardSeed() == SpecificSeed.MUSHROOM)) {

                        // All conditions met, add points to the player and mark nodes as checked
                        player.setPlayerScore(player.getPlayerScore() + 3);
                        nodes[row][column].setAlreadyChecked(true);
                        nodes[row][column + 1].setAlreadyChecked(true);
                        nodes[row + 1][column].setAlreadyChecked(true);
                        nodes[row + 1][column + 1].setAlreadyChecked(true);
                    }
                }
            }
        }
    }
    private boolean isMushroomCorner(Corner corner) {
        return corner != null && corner.getCardSeed() == SpecificSeed.MUSHROOM;
    }

    private boolean isPlantCorner(Corner corner) {
        return corner != null && corner.getCardSeed() == SpecificSeed.PLANT;
    }*/

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++)
            {
                SpecificSeed seed= SpecificSeed.MUSHROOM;
                if((nodes[row][column].getFirstPlacement()==seed))
                    //  SE IL PRIMO PLACEMENT è PIANTA E IL SECONDO FUNGO O VICEVERSA
                    if((nodes[row][column].getFirstPlacement()==seed1 && nodes[row][column].getSecondPlacement()==seed2) ||
                            (nodes[row][column].getFirstPlacement()==seed2 && nodes[row][column].getSecondPlacement()==seed1)){ ///Mushoroom seed 2

                        //SE TUTTI E 4 I FIRST PLACEMENT SOPRA LA CARTA SONO FUNGO, ALLORA LA CARTA OBIETTIVO è VALIDA
                        if(nodes[row][column-2].getFirstPlacement()==seed2  && !nodes[row][column - 2].isAlreadyChecked()){
                            if(nodes[row][column-3].getFirstPlacement()==seed2 && !nodes[row][column - 3].isAlreadyChecked() ) {
                                if (nodes[row - 1][column - 3].getFirstPlacement() == seed2 && !nodes[row - 1][column - 3].isAlreadyChecked()) {
                                    if (nodes[row - 1][column - 2].getFirstPlacement() == seed2 && !nodes[row - 1][column - 2].isAlreadyChecked()) {
                                        player.setPlayerScore(player.getPlayerScore() + 3);
                                        nodes[row][column].setAlreadyChecked(true);
                                        nodes[row][column - 2].setAlreadyChecked(true);
                                        nodes[row][column - 3].setAlreadyChecked(true);
                                        nodes[row - 1][column - 3].setAlreadyChecked(true);
                                        nodes[row - 1][column - 2].setAlreadyChecked(true);
                                    }
                                }
                            }
                            //SE CIò NON è VERO, ALLORA TUTTI E 4 I SECOND FIRST PLACEMENT DEVONO ESSERE FUNGO(?)
                        }
                        else  if(nodes[row][column-2].getSecondPlacement()==seed2  && !nodes[row][column - 2].isAlreadyChecked()){
                            if(nodes[row][column-3].getSecondPlacement()==seed2 && !nodes[row][column - 3].isAlreadyChecked() ) {
                                if (nodes[row - 1][column - 3].getSecondPlacement() == seed2 && !nodes[row - 1][column - 3].isAlreadyChecked()) {
                                    if (nodes[row - 1][column - 2].getSecondPlacement() == seed2 && !nodes[row - 1][column - 2].isAlreadyChecked()) {
                                        player.setPlayerScore(player.getPlayerScore() + 3);
                                        nodes[row][column].setAlreadyChecked(true);
                                        nodes[row][column - 2].setAlreadyChecked(true);
                                        nodes[row][column - 3].setAlreadyChecked(true);
                                        nodes[row - 1][column - 3].setAlreadyChecked(true);
                                        nodes[row - 1][column - 2].setAlreadyChecked(true);
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}
