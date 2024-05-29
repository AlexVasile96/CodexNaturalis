package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Player;

public interface Updater {
    void updatePlayerPoints(Card card, Player player, Board board);
    void updatePlayerPoints(GoldCard card, Player player, Board board);
}
