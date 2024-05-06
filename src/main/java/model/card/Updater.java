package model.card;

import model.game.Board;
import model.game.Player;

public interface Updater {
    void updatePlayerPoints(Card card, Player player, Board board);
    void updatePlayerPoints(GoldCard card, Player player, Board board);
}
