package model.objectiveCardTypes;

import model.card.Card;
import model.game.*;

public class OnePlantAndTwoMushrooms
{
    public void realCheck(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        System.out.println("Seed 1 is" + seed1.toString());
        System.out.println("Seed 2 is" + seed2.toString());
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++)
            {
                SpecificSeed seed= SpecificSeed.MUSHROOM;
                    //  SE IL PRIMO PLACEMENT è PIANTA E IL SECONDO FUNGO O VICEVERSA
                    if((nodes[row][column].getFirstPlacement()==seed1 && nodes[row][column].getSecondPlacement()==seed2) ||
                            (nodes[row][column].getFirstPlacement()==seed2 && nodes[row][column].getSecondPlacement()==seed1)){ ///26-26 ok

                        //SE TUTTI E 4 I FIRST PLACEMENT SOPRA LA CARTA SONO FUNGO, ALLORA LA CARTA OBIETTIVO è VALIDA
                        if(nodes[row-2][column].getFirstPlacement()==seed2  && !nodes[row-2][column].isAlreadyChecked()){ //26 24-
                            if(nodes[row-3][column].getFirstPlacement()==seed2 && !nodes[row-3][column].isAlreadyChecked() ) {
                                if ((nodes[row - 3][column - 1].getFirstPlacement() == seed2 ||nodes[row - 3][column - 1].getSecondPlacement() == seed2) && !nodes[row - 3][column - 1].isAlreadyChecked()) {
                                    if ((nodes[row - 2][column - 1].getFirstPlacement() == seed2 || nodes[row - 2][column - 1].getSecondPlacement() == seed2)  && !nodes[row - 2][column - 1].isAlreadyChecked()) {
                                        player.setPlayerScore(player.getPlayerScore() + 3);
                                        nodes[row][column].setAlreadyChecked(true);
                                        nodes[row-3][column ].setAlreadyChecked(true);
                                        nodes[row-2][column].setAlreadyChecked(true);
                                        nodes[row - 3][column - 1].setAlreadyChecked(true);
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
