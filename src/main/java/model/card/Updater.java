package model.card;

import model.Board;
import model.Player;
import model.card.Card;
import model.card.GoldCard;

public interface Updater {
    public void updatePlayerPoints(Card card, Player player, Board board);
    public void updatePlayerPoints(GoldCard card, Player player, Board board);
}
