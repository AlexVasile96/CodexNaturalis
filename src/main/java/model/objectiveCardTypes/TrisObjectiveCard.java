package model.objectiveCardTypes;
import model.*;

import java.util.Map;

//METODO CHE CHECCKA LA CARTA OBEITTIVO TRIS, OVVERO LA CARTA OBIETTIVO CHE ASSEGNA 2 PUNTI PER OGNI 3 SPECIFICSEED PRESENTI SULLA BOARD
//ES-> SE HO 5 MUSHROOM, LA CARTA DEVE RESTITUIRE 2 PUNTI.
// SE HO 6 MUSHROOM, LA CARTA DEVE RESTITUIRE 4 PUNTI
//CONTROLLARE CHE IL METODO FUNZIONI CORRETTAMENTE

public class TrisObjectiveCard implements ExtendExtendExtend {
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        BoardPoints boardPoints = new BoardPoints();
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(board);
        int numberOfSpecificSeed = seedCountMap.get(seed);
        int value= (numberOfSpecificSeed/3)*2;
        System.out.println("Number of " + seed + " SpecificSeed: " + numberOfSpecificSeed);
        player.setPlayerScore(player.getPlayerScore()+ value);
        System.out.println("Player score: " + player.getPlayerScore());
        if (numberOfSpecificSeed >= 3) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
}
