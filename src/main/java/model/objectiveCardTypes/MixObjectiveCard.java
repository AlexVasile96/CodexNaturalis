package model.objectiveCardTypes;

import model.*;

public class MixObjectiveCard implements ExtendExtendExtend {
    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {//CONTI I 3 SIMBOLI PARTICOLARI, TIENI IL NUMERO MINOR DEI 3 SIMBOLI (ES SE HO 2 3 3) TENGO 2, FACCIO 2* 3. Le regole non sono chiare perché dicono 3 punti per ogni set di 3 attributi diversi ma non avrebbe senso
        BoardPoints boardPoints=new BoardPoints();
        int[] blabla = boardPoints.countPointsForObjectiveCards(board);
        int minimo = blabla[0];
        if (blabla[1] < minimo) {
            minimo = blabla[1];
        }
        if (blabla[2] < minimo) {
            minimo = blabla[2];
        }

        int points = minimo*3;
        player.setPlayerScore(player.getPlayerScore()+points);
        return true;
    }


    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
    //CONTI I 3 SIMBOLI PARTICOLARI, TIENI IL NUMERO MINOR DEI 3 SIMBOLI (ES SE HO 2 3 3) TENGO 2, FACCIO 2* 3.
}
