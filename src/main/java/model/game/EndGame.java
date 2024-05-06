package model.game;
import model.card.ObjectiveCard;

public class EndGame {
    public void finishGame(Player player, Board board){                            //Game ends when one player reaches 20 points

        ObjectiveCard secretPlayerObjective = player.getSecretChosenCard();
        board.createSpecificSecretCard(secretPlayerObjective, player);
        System.out.println("Punteggio player: " + player.getPlayerScore());
        System.out.println("GAME ENDED");
    }
}
