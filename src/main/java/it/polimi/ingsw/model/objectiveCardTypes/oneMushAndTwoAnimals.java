package it.polimi.ingsw.model.objectiveCardTypes;

import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Node;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.SpecificSeed;

public class oneMushAndTwoAnimals {
    public void realCheck(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        System.out.println("Seed 1 is" + seed1.toString());
        System.out.println("Seed 2 is" + seed2.toString());
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++)
            {
                if((nodes[row][column].getFirstPlacement()==seed1 && nodes[row][column].getSecondPlacement()==seed2) ||
                        (nodes[row][column].getFirstPlacement()==seed2 && nodes[row][column].getSecondPlacement()==seed1)){
                    if(nodes[row+2][column].getFirstPlacement()==seed2  && !nodes[row+2][column].isAlreadyChecked()){ //26 24-
                        if(nodes[row+3][column].getFirstPlacement()==seed2 && !nodes[row+3][column].isAlreadyChecked() ) {
                            if ((nodes[row + 3][column - 1].getFirstPlacement() == seed2 ||nodes[row + 3][column - 1].getSecondPlacement() == seed2) && !nodes[row + 3][column - 1].isAlreadyChecked()) {
                                if ((nodes[row + 2][column - 1].getFirstPlacement() == seed2 || nodes[row + 2][column - 1].getSecondPlacement() == seed2)  && !nodes[row + 2][column - 1].isAlreadyChecked()) {
                                    player.setPlayerScore(player.getPlayerScore() + 3);
                                    nodes[row][column].setAlreadyChecked(true);
                                    nodes[row+3][column ].setAlreadyChecked(true);
                                    nodes[row+2][column].setAlreadyChecked(true);
                                    nodes[row + 3][column - 1].setAlreadyChecked(true);
                                    nodes[row + 2][column -1].setAlreadyChecked(true);
                                }
                            }
                        }
                    }
                    else  if(nodes[row+2][column].getSecondPlacement()==seed2  && !nodes[row+2][column].isAlreadyChecked()){
                        if(nodes[row+3][column].getSecondPlacement()==seed2 && !nodes[row+3][column].isAlreadyChecked() ) {
                            if (nodes[row + 3][column - 1].getSecondPlacement() == seed2 && !nodes[row + 3][column - 1].isAlreadyChecked()) {
                                if (nodes[row + 2][column - 1].getSecondPlacement() == seed2 && !nodes[row + 2][column - 1].isAlreadyChecked()) {
                                    player.setPlayerScore(player.getPlayerScore() + 3);
                                    nodes[row][column].setAlreadyChecked(true);
                                    nodes[row+3][column ].setAlreadyChecked(true);
                                    nodes[row+2][column].setAlreadyChecked(true);
                                    nodes[row + 3][column - 1].setAlreadyChecked(true);
                                    nodes[row + 2][column -1].setAlreadyChecked(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
