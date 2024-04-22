package controller;

import exceptions.*;
import model.game.Game;
import network.message.Command;
import network.message.MessagesEnum;
import com.google.gson.Gson;
import server.HandlingPlayerInputsThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameController {
    private final Map<String, PrintWriter> players;
    int size;
    boolean isGameOver;
    Gson gson;
    private Game game;
    private List<HandlingPlayerInputsThread> clients;
    PrintWriter out;

    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut, List<HandlingPlayerInputsThread> clients, Socket socket) throws IOException {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
        this.gson = new Gson();
        this.game=new Game();
        this.clients=clients;
        this.out= new PrintWriter(socket.getOutputStream(), true);
    }


    public synchronized void readCommand(String username, String commandString) {
        if (!username.equals(getCurrentPlayerUsername())) {
            sendMessageToClient("It's not your turn to act");
            System.out.println("Wrong player tried to send a command.");

        } else if (isGameOver) {
            sendMessageToClient( "The game has already ended");
            System.out.println("Player tried to send a command after the end of the game.");

        } else {
            try {
                System.out.println("PAPPA");
                Command command = gson.fromJson(commandString, Command.class); //cambiare questo
                String result = command.runCommand(game);
            } catch (Exception ignored) {

            }
        }
    }


    public void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException, UsernameAlreadyExistsException {
        if (size == 0)
            throw new UnknownPlayerNumberException();

        if (!players.containsKey(username)) {
            if (players.size() >= size || isGameOver) {
                throw new GameFullException();
            }
        } else {
            throw new UsernameAlreadyExistsException();
        }
        //sendMessageToAllClients("Un nuovo client si è connesso: " + username);
        players.put(username, userOut);
        //System.out.println("Added player: " + username + " to current game.");

        //preparationForStartingGame();
    }

    public void choosePlayerNumber(int number) throws PlayerNumberAlreadySetException, ParametersNotValidException {
        if (size > 0) {
            throw new PlayerNumberAlreadySetException();
        }

        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        size = number;
        //preparationForStartingGame();
    }
    public int getNumOfPlayers() {
        return players.size();
    }
    /*
    private void preparationForStartingGame() {
        if (players.size() != size) {
            broadcastMessage(MessagesEnum.WAIT_PLAYERS, "One player has joined, waiting for more players...");
            return;
        }

        broadcastMessage(MessagesEnum.GAME_START, "The last player has joined, the game will now commence...");
        game = new Game();
        // Ora il gioco può iniziare
        System.out.println("The game will now start.");
    }



    /*private void broadcastMessage(String s) {
        for (String player : players.keySet()) {
            playerMessage(player, messagesEnum, s);
        }
    }*/
    public synchronized void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }


    public Set<String> getConnectedPlayerUsername() {
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

    @Override
    public String toString() {
        return "GameController{" +
                "players=" + players +
                ", size=" + size +
                '}';
    }
    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }
}