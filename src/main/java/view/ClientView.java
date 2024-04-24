package view;

import model.card.Card;
import model.card.ObjectiveCard;
import model.game.Board;
import model.game.Dot;
import model.game.Game;
import model.game.Player;

import java.util.ArrayList;

//CLASSE CHE MANTIENE TUTTE LE INFORMAZIONI DEL SINGOLO CLIENT/PLAYER
public class ClientView {
    String userName=null;
    Game game;
    private int playerScore;
    private Dot dot;
    private Board board;
    private boolean isCardBack;
    private ArrayList<Card> playerCards;
    private ObjectiveCard secretChosenCard;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public void update(String username, Player player) {
        if (username.equals(userName)) {
            // Aggiorna le informazioni del giocatore corrente
            this.playerScore = player.getPlayerScore();
            this.dot = player.getDot();
            this.board = player.getBoard();
            this.isCardBack = player.isCardBack();
            this.playerCards = (ArrayList<Card>) player.getPlayerCards(); // Aggiorna le carte del giocatore
            this.secretChosenCard = player.getSecretChosenCard();
        }
    }
}
