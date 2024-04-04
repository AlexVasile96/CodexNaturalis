package com.example.proj;

public class StairsObjectiveCard implements ExtendExtendExtend{
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes[0].length;

        for (int i = 0; i < rows - 2; i++) {
            for (int j = 0; j < cols - 2; j++) {// Check diagonal pattern from top left to bottom right
                if (nodes[i][j].getSpecificNodeSeed() == seed &&
                        nodes[i+1][j+1].getSpecificNodeSeed() == seed &&
                        nodes[i+2][j+2].getSpecificNodeSeed() == seed) {
                    return true;
                }
                if (nodes[i][j+2].getSpecificNodeSeed() == seed &&
                        nodes[i+1][j+1].getSpecificNodeSeed() == seed &&
                        nodes[i+2][j].getSpecificNodeSeed() == seed) {
                    return true;
                }// Check diagonal pattern from top right to bottom left
            }
        }
        return false;
    }

}
