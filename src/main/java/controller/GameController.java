package controller;

import Exceptions.*;
import model.Game;
import network.JsonUtils;
import network.message.Command;
import network.message.MessageSender;
import network.message.MessagesEnum;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameController {
    private final Map<String, PrintWriter> players;
    int size;
    boolean isGameOver;
    Gson gson;
    private Game game;

    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut) {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
        this.gson = new Gson();
        playerMessage(username, MessagesEnum.CONFIRM_USERNAME, username);
    }

    public int getNumOfPlayers() {
        return players.size();
    }
    public synchronized void readCommand(String username, String commandString) {
        if (game == null) {

            playerMessage(username, MessagesEnum.ERROR, "The game has not yet started, as some players are still missing.");
            System.out.println("Player tried sending a command before the game start.");

        } else if (!username.equals(getCurrentPlayerUsername())) {

            playerMessage(username, MessagesEnum.ERROR, "It is not your turn to act.");
            System.out.println("Wrong player tried to send a command.");

        } else if (isGameOver) {

            playerMessage(username, MessagesEnum.ERROR, "The game has already ended");
            System.out.println("Player tried to send a command after the end of the game.");

        } else {
            try {

                Command command = gson.fromJson(commandString, Command.class);

                String result = command.runCommand(game);

                if (result != null) {
                    playerMessage(username, MessagesEnum.ERROR, result);
                    System.out.println("Error: " + result);
                }

            } catch (Exception ex) {

                playerMessage(username, MessagesEnum.ERROR, "The message is not in json format.");
                System.out.println("Player sent a message that was not a json.");
            }
        }
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
    public void choosePlayerNumber(int number) throws PlayerNumberAlreadySetException, ParametersNotValidException {
        if (size > 0) {
            throw new PlayerNumberAlreadySetException();
        }

        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        size = number;
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
    public String getCurrentPlayerUsername() {
        if (game == null) {
            return null;
        }
        return game.getCurrentPlayer().getNickName();
    }

}