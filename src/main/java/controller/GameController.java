package controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import Exceptions.GameFullException;
import Exceptions.UnknownPlayerNumberException;
import Exceptions.UsernameAlreadyExistsException;
import com.google.gson.Gson;
import model.Game;
import network.message.MessageSender;
import network.message.Messages;

public class GameController {
    private final Gson gson;
    private Game game;
    private final Map<String, PrintWriter> players;
    int size;
    boolean isGameOver;

    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut) {
        this.gson = new Gson();
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
        //playerMessage(username, MessageType.CONFIRM_USERNAME, username);
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
                broadcastMessage(Messages.PLAYER_CONNECTED, username);
                players.put(username, userOut);
                playerMessage(username, Messages.CONFIRM_USERNAME, username);
                System.out.println("Added player: " + username + " to current game.");
            }
        }


        preparationForStartingGame();
    }
    private void preparationForStartingGame() {
        if (game != null) {
            return;
        }
        if (players.size() != size) {
            broadcastMessage(Messages.WAIT_PLAYERS, "One player has joined, waiting for more players...");
            return;
        }

        broadcastMessage(Messages.GAME_START, "The last player has joined, the game will now commence...");

        game = new Game(players.keySet());

        System.out.println("The game will now start.");
    }
    public void playerMessage(String username, Messages type, String message) {
        if (players.get(username) != null)
            players.get(username).println(
                    gson.toJson(
                            new MessageSender(type, message)));
    }

    private void broadcastMessage(Messages messages, String s) {
        for (String player : players.keySet()) {
            playerMessage(player, messages, s);
        }
    }

}
