package model.objectiveCardTypes;
import model.game.Board;
import model.game.Node;
import model.game.Player;
import model.game.SpecificSeed;

public class LObjectiveCard implements ExtendExtendExtend {
    public boolean checkColumnPattern(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
       if(seed1==SpecificSeed.PLANT && seed2==SpecificSeed.MUSHROOM){
           onePlantAndTwoMushrooms(board,player,seed1,seed2);
       }
       else if(seed1==SpecificSeed.MUSHROOM && seed2==SpecificSeed.ANIMAL){
           oneMushAndTwoPlants(board,player,seed1,seed2);
       }
       else if(seed1== SpecificSeed.ANIMAL && seed2==SpecificSeed.INSECT)
       {
           oneAnimalAndTwoInsects(board,player,seed1,seed2);
       }
       else if(seed1==SpecificSeed.INSECT&& seed2==SpecificSeed.PLANT)
       {
           oneInsectAndTwoPlants(board,player,seed1,seed2);
       }
    return true;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        return false;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }

    private void onePlantAndTwoMushrooms(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++)
            {
                //  SE IL PRIMO PLACEMENT è PIANTA E IL SECONDO FUNGO O VICEVERSA
                if((nodes[row][column].getFirstPlacement()==SpecificSeed.PLANT && nodes[row][column].getSecondPlacement()==SpecificSeed.MUSHROOM) ||
                        (nodes[row][column].getFirstPlacement()==SpecificSeed.MUSHROOM && nodes[row][column].getSecondPlacement()==SpecificSeed.PLANT)){

                        //SE TUTTI E 4 I FIRST PLACEMENT SOPRA LA CARTA SONO FUNGO, ALLORA LA CARTA OBIETTIVO è VALIDA
                            if(nodes[row][column-2].getFirstPlacement()==SpecificSeed.MUSHROOM  && !nodes[row][column - 2].isAlreadyChecked()){
                                if(nodes[row][column-3].getFirstPlacement()==SpecificSeed.MUSHROOM && !nodes[row][column - 3].isAlreadyChecked() ) {
                                    if (nodes[row - 1][column - 3].getFirstPlacement() == SpecificSeed.MUSHROOM && !nodes[row - 1][column - 3].isAlreadyChecked()) {
                                        if (nodes[row - 1][column - 2].getFirstPlacement() == SpecificSeed.MUSHROOM && !nodes[row - 1][column - 2].isAlreadyChecked()) {
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
                            else  if(nodes[row][column-2].getSecondPlacement()==SpecificSeed.MUSHROOM  && !nodes[row][column - 2].isAlreadyChecked()){
                                    if(nodes[row][column-3].getSecondPlacement()==SpecificSeed.MUSHROOM && !nodes[row][column - 3].isAlreadyChecked() ) {
                                        if (nodes[row - 1][column - 3].getSecondPlacement() == SpecificSeed.MUSHROOM && !nodes[row - 1][column - 3].isAlreadyChecked()) {
                                            if (nodes[row - 1][column - 2].getSecondPlacement() == SpecificSeed.MUSHROOM && !nodes[row - 1][column - 2].isAlreadyChecked()) {
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
    private void oneMushAndTwoPlants(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
    }
    private void oneAnimalAndTwoInsects(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
    }
    private void oneInsectAndTwoPlants(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
    }


}
