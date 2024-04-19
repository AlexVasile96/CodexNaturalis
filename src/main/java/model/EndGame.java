package model;
import model.card.ObjectiveCard;

public class EndGame {
    public void finishGame(Player player, Board board){
        //il gioco finisce quando qualcuno raggiunge 20 punti
        ObjectiveCard secretPlayerObjective = player.getSecretChosenCard();
        board.createSpecificSecretCard(secretPlayerObjective, player);
        System.out.println("Punteggio player: " + player.getPlayerScore());
        System.out.println("GAME ENDED");
    }
}
