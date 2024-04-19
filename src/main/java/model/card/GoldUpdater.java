package model.card;

import model.*;

public class GoldUpdater implements Updater {

    @Override
    public void updatePlayerPoints(Card card, Player player, Board board) {

    }
    @Override
    public void updatePlayerPoints(GoldCard card, Player player, Board board) {
        SpecificSeed adder= card.getMultiplier();
        int x = player.getPlayerScore();
        if (adder.equals(SpecificSeed.MISSINGMULTIPLIER)) {
            int y= x + card.getValueWhenPlaced();
            player.setPlayerScore(y);
            return;
        }
        BoardPoints boardPoints= new BoardPoints();
        int y= boardPoints.addictionalPointsForGoldCards(board,adder) + player.getPlayerScore();
        player.setPlayerScore(y);
    }
}
