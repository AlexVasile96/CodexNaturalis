package model.card;

import model.game.Board;
import model.game.BoardPoints;
import model.game.Player;
import model.game.SpecificSeed;

public class GoldUpdater implements Updater {

    @Override
    public void updatePlayerPoints(Card card, Player player, Board board) {

    }
    @Override
    public void updatePlayerPoints(GoldCard card, Player player, Board board) {
        SpecificSeed adder= card.getMultiplier();
        System.out.println("Adder: "+adder);
        int x = player.getPlayerScore();
        if (adder.equals(SpecificSeed.MISSINGMULTIPLIER)) {
            int y= x + card.getValueWhenPlaced();
            player.setPlayerScore(y);
            return;
        }
        BoardPoints boardPoints= new BoardPoints();
        if(adder.equals(SpecificSeed.NUMOFCOVEREDCORNERS))
        {
            numOfCoveredCornersByTheCard(card,player);
        }
        else {
            int additionalPoints = boardPoints.additionalPointsForGoldCards(board, adder);
            System.out.println("Additional point for player are: " + additionalPoints);
            int y = additionalPoints + player.getPlayerScore();
            System.out.println("New current player score is: " + y);
            player.setPlayerScore(y);
        }
    }
    private void numOfCoveredCornersByTheCard(GoldCard card,Player player){
       int tlValue= card.getTL().getValueCounter();
       int trValue= card.getTR().getValueCounter();
       int blValue= card.getBL().getValueCounter();
       int brValue= card.getBR().getValueCounter();
       if(tlValue==0)
       {
           player.setPlayerScore(player.getPlayerScore()+2);
       }
        if(trValue==0)
        {
            player.setPlayerScore(player.getPlayerScore()+2);
        }
        if(blValue==0)
        {
            player.setPlayerScore(player.getPlayerScore()+2);
        }
        if(brValue==0)
        {
            player.setPlayerScore(player.getPlayerScore()+2);
        }

    }
}
