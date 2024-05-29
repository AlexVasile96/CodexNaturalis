package it.polimi.ingsw.model.objectiveCardTypes;

import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Node;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.SpecificSeed;

public class OneInsectAndTwoPlants {
    public void realCheck(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
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
