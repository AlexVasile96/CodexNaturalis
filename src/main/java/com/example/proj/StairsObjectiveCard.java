package com.example.proj;

public class StairsObjectiveCard implements ExtendExtendExtend{
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes[0].length;
        for (int row = 0; row < rows - 2; row++) {
            for (int columns = 0; columns < cols - 2; columns++) {// Check diagonal pattern from top left to bottom right, -2 cause we don't want to exceed matrix limits
                if (nodes[row][columns].getSpecificNodeSeed() == seed &&
                        nodes[row+1][columns+1].getSpecificNodeSeed() == seed &&
                        nodes[row+2][columns+2].getSpecificNodeSeed() == seed) {
                    return true;
                }
                if (nodes[row][columns+2].getSpecificNodeSeed() == seed &&
                        nodes[row+1][columns+1].getSpecificNodeSeed() == seed &&
                        nodes[row+2][columns].getSpecificNodeSeed() == seed) {
                    return true;
                }// Check diagonal pattern from top right to bottom left
            }
        }
        return false;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }

}
