package model.card;

import model.game.Board;
import model.game.Player;

public interface Updater {
    public void updatePlayerPoints(Card card, Player player, Board board);
    public void updatePlayerPoints(GoldCard card, Player player, Board board);
}
