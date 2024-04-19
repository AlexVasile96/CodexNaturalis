package model.card;

import model.Board;
import model.Player;

public class ResourceUpdater implements Updater {

    @Override
    public void updatePlayerPoints(Card card, Player player, Board board) {
        int x = player.getPlayerScore();
            int y = x + card.getValueWhenPlaced();
            player.setPlayerScore(y);
        }

    @Override
    public void updatePlayerPoints(GoldCard card, Player player, Board board) {

    }
}
