package it.polimi.ingsw.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.InitialCard;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Dot;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Player;

import java.util.ArrayList;
import java.util.List;

//CLASS THAT KEEPS EVERY PLAYER/CLIENT INFO


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
    private String objectiveCard;
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
            // update current player's info
            this.userName= player.getNickName();
            this.playerScore = player.getPlayerScore();
            this.dot = player.getDot();
            this.board = player.getBoard();
            this.isCardBack = player.isCardBack();
            this.playerCards = (ArrayList<Card>) player.getPlayerCards(); // Update player's cards
            this.secretChosenCard = player.getSecretChosenCard();
            this.cardsOnTheBoard.clear();
            int cardNumber = 1;
        for (Card card : player.getBoard().getCardsOnTheBoardList()) {
            String cardType = (card instanceof InitialCard) ? "Initial Card" : card.getType().toString();
            String cardString = cardNumber + "->" + cardType + ": (" + card.getNode().getCoordY() + " " + card.getNode().getCoordX() + ") " + (card.isCardBack() ? "(back)" : "(front)");
            this.cardsOnTheBoard.add(cardString);
            cardNumber++;
        }


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

    public void setObjectiveCard(String objectiveCard) {
        this.objectiveCard = objectiveCard;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("playerScore", playerScore);
        jsonObject.addProperty("dot", dot.toString());
        jsonObject.add("board", board.toJsonObject());
        jsonObject.addProperty("isCardBack", isCardBack);
        jsonObject.addProperty("numOfCardsOnTheBoard", numOfCardsOnTheBoard);


        JsonArray cardsOnTheBoardArray = new JsonArray();
        for (String card : cardsOnTheBoard) {
            cardsOnTheBoardArray.add(card);
        }
        jsonObject.add("cardsOnTheBoard", cardsOnTheBoardArray);

        JsonArray playerStringCardsArray = new JsonArray();
        for (String card : PlayerStringCards) {
            playerStringCardsArray.add(card);
        }
        jsonObject.add("playerStringCards", playerStringCardsArray);

        if (secretChosenCard != null) {
            jsonObject.add("objectiveCard", secretChosenCard.toJsonObject());
        }

        return jsonObject;
    }

    public static ClientView fromJsonObject(JsonObject jsonObject) {
        ClientView clientView = new ClientView();
        clientView.userName = jsonObject.get("userName").getAsString();
        clientView.playerScore = jsonObject.get("playerScore").getAsInt();
        clientView.dot = Dot.valueOf(jsonObject.get("dot").getAsString()); // Assuming Dot has a method valueOf
        clientView.board = Board.fromJson(jsonObject.getAsJsonObject("board")); // Deserialize Board from JsonObject
        clientView.isCardBack = jsonObject.get("isCardBack").getAsBoolean();

        clientView.numOfCardsOnTheBoard = jsonObject.get("numOfCardsOnTheBoard").getAsInt();

        JsonArray cardsOnTheBoardJsonArray = jsonObject.getAsJsonArray("cardsOnTheBoard");
        for (int i = 0; i < cardsOnTheBoardJsonArray.size(); i++) {
            clientView.cardsOnTheBoard.add(cardsOnTheBoardJsonArray.get(i).getAsString());
        }

        JsonArray playerStringCardsJsonArray = jsonObject.getAsJsonArray("playerStringCards");
        if (playerStringCardsJsonArray != null) {
            for (int i = 0; i < playerStringCardsJsonArray.size(); i++) {
                String card = playerStringCardsJsonArray.get(i).getAsString();
                if (card != null && !card.isEmpty()) { // Check if the card is not null or empty
                    clientView.PlayerStringCards.add(card);
                }
            }
        }

        return clientView;
    }

}
