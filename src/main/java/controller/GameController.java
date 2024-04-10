package controller;

import Exceptions.GameFullException;
import Exceptions.UnknownPlayerNumberException;
import Exceptions.UsernameAlreadyExistsException;
import network.message.MessageSender;
import network.message.MessagesEnum;
import network.JsonUtils; // Importa la classe JsonUtils

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameController {
    private final Map<String, PrintWriter> players;
    int size;
    boolean isGameOver;

    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut) {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException, UsernameAlreadyExistsException {
        if (size == 0)
            throw new UnknownPlayerNumberException();
        if (!players.containsKey(username)) {
            if (players.size() >= size || isGameOver) {
                throw new GameFullException();
            }else{
                broadcastMessage(MessagesEnum.PLAYER_CONNECTED, username);
                players.put(username, userOut);
                playerMessage(username, MessagesEnum.CONFIRM_USERNAME, username);
                System.out.println("Added player: " + username + " to current game.");
            }
        }

        preparationForStartingGame();
    }

    private void preparationForStartingGame() {
        if (players.size() != size) {
            broadcastMessage(MessagesEnum.WAIT_PLAYERS, "One player has joined, waiting for more players...");
            return;
        }

        broadcastMessage(MessagesEnum.GAME_START, "The last player has joined, the game will now commence...");

        // Ora il gioco può iniziare
        System.out.println("The game will now start.");
    }

    public void playerMessage(String username, MessagesEnum type, String message) {
        if (players.get(username) != null)
            players.get(username).println(JsonUtils.toJson(new MessageSender(type, message)));
    }

    private void broadcastMessage(MessagesEnum messagesEnum, String s) {
        for (String player : players.keySet()) {
            playerMessage(player, messagesEnum, s);
        }
    }

    public Set<String> getConnectedPlayersUsernames() {
        return players.keySet();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isSizeSet() {
        return false;
    }

    public void setDisconnectedStatus(String username) {
    }
}