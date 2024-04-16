package model;

public class StairsObjectiveCard implements ExtendExtendExtend{
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes.length;
        boolean checkvariable= false;
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {// Check diagonal pattern from top left to bottom right, -2 cause we don't want to exceed matrix limits
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
        }
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {
                nodes[row][columns].setAlreadyChecked(false);
            }
        }
        return checkvariable;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }

}
