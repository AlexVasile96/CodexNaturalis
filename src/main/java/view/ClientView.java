package view;

import model.card.Card;
import model.card.ObjectiveCard;
import model.game.Board;
import model.game.Dot;
import model.game.Game;
import model.game.Player;

import java.util.ArrayList;
import java.util.List;

//CLASSE CHE MANTIENE TUTTE LE INFORMAZIONI DEL SINGOLO CLIENT/PLAYER
public class ClientView {
    String userName=null;
    Game game;
    private int playerScore;
    private Dot dot;
    private Board board;
    private boolean isCardBack;
    private int index;
    private ArrayList<Card> playerCards;
    private ObjectiveCard secretChosenCard;
    private int numOfCardsOnTheBoard=1;
    private List<String> cardsOnTheBoard= new ArrayList<>();

    public String getUserName() {
        return userName;
    }
    private List<String> PlayerStringCards= new ArrayList<>();

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Game getGame() {
        return game;
    }


    public void setGame(Game game) {
        this.game = game;
    }
    public void update(Player player) {
            // Aggiorna le informazioni del giocatore corrente
            this.userName= player.getNickName();
            //this.game= game;
            this.playerScore = player.getPlayerScore();
            this.dot = player.getDot();
            this.board = player.getBoard();
            this.isCardBack = player.isCardBack();
            this.playerCards = (ArrayList<Card>) player.getPlayerCards(); // Aggiorna le carte del giocatore
            this.secretChosenCard = player.getSecretChosenCard();

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public List<String> getPlayerStringCards() {
        return PlayerStringCards;
    }

    public void setPlayerStringCards(List<String> playerStringCards) {
        PlayerStringCards = playerStringCards;
    }

    public Dot getDot() {
        return dot;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isCardBack() {
        return isCardBack;
    }

    public void setCardBack(boolean cardBack) {
        isCardBack = cardBack;
    }

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public ObjectiveCard getSecretChosenCard() {
        return secretChosenCard;
    }

    public void setSecretChosenCard(ObjectiveCard secretChosenCard) {
        this.secretChosenCard = secretChosenCard;
    }

    @Override
    public String toString() {
        return "ClientView{" +
                "userName='" + userName + '\'' +
                ", game=" + game +
                ", playerScore=" + playerScore +
                ", dot=" + dot +
                ", board=" + board +
                ", isCardBack=" + isCardBack +
                ", playerCards=" + playerCards +
                ", secretChosenCard=" + secretChosenCard +
                '}';
    }

    public int getNumOfCardsOnTheBoard() {
        return numOfCardsOnTheBoard;
    }

    public void setNumOfCardsOnTheBoard(int numOfCardsOnTheBoard) {
        this.numOfCardsOnTheBoard = numOfCardsOnTheBoard;
    }

    public List<String> getCardsOnTheBoard() {
        return cardsOnTheBoard;
    }

    public void addCardOnTheBoard(String card) {
        this.cardsOnTheBoard.add(card);
    }


}
