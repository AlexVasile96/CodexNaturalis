package server;

import exceptions.GameFullException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import controller.GameController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerLobby {
    private final List<GameController> currentGames;
    private final List<HandlingPlayerInputsThread> clients;
    private Socket socket;
    public ServerLobby(List<HandlingPlayerInputsThread> clients, Socket socket) {
        currentGames = new ArrayList<>();
        this.clients=clients;
        this.socket=socket;
    }

    public synchronized GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, UsernameAlreadyExistsException, IOException {
        if (currentGames.isEmpty() || currentGames.get(0).getNumOfPlayers() >= 4) {
            GameController newGame = new GameController(username, userOut, clients, socket);
            currentGames.add(newGame);
            return newGame;
        } else {
            GameController game = currentGames.get(0);
            try {
                game.addPlayer(username, userOut);
                return game;
            } catch (GameFullException ignored) {
                // Il gioco è pieno, quindi non aggiungere il giocatore
                return null;
            }
        }
    }
    public void abortGame(GameController controller) {
        // This method will implement game abortion logic when needed.
    }
}

