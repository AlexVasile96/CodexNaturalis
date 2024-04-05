package com.example.proj;

public class LObjectiveCard implements ExtendExtendExtend {
    public boolean checkColumnPattern(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
        Node[][] nodes = board.getNodes();
        int rows = nodes.length;
        int cols = nodes[0].length;
        for (int i = 0; i < rows - 2; i++) {
            for (int j = 0; j < cols; j++) {
                // Check if two adjacent nodes in the same column have the same seed
                if (nodes[i][j].getFirstPlacement() == seed2)
                {
                    if (nodes[i][j].getSecondPlacement() == seed1) {
                        if (nodes[i][j - 2].getFirstPlacement() == seed1 || nodes[i][j - 3].getFirstPlacement() == seed1 || nodes[i - 1][j - 2].getFirstPlacement() == seed1 || nodes[i - 1][j - 3].getFirstPlacement() == seed1) {
                            player.setPlayerScore(player.getPlayerScore() + 3);
                            return true;
                        }
                    }
                }
                else
                    if(nodes[i][j].getFirstPlacement()==seed1){
                        if(nodes[i][j].getSecondPlacement()==seed2)
                            {
                                if(nodes[i][j-2].getFirstPlacement()==seed1 || nodes[i][j-3].getFirstPlacement()==seed1 || nodes[i-1][j-2].getFirstPlacement()==seed1|| nodes[i-1][j-3].getFirstPlacement()==seed1)
                                {
                                    player.setPlayerScore(player.getPlayerScore()+3);
                                    return true;
                                }
                            }
                        else System.out.println("Carta obiettivo non valida");
                        return false;
                    }
                    }
            }
        return false;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        return false;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed sburuuuuuus) {
        return false;
    }
}
