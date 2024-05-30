package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.GameFullException;
import it.polimi.ingsw.exceptions.UnknownPlayerNumberException;
import it.polimi.ingsw.exceptions.UsernameAlreadyExistsException;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.game.Game;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerLobby {
    private final static List<GameController> currentGames= new ArrayList<>();
    private final List<HandlingPlayerInputsThread> clients;
    private Socket socket;
    private Game game;
    public ServerLobby(List<HandlingPlayerInputsThread> clients, Socket socket, Game game) {
        this.clients=clients;
        this.socket=socket;
        this.game=game;
    }

    public synchronized GameController login() throws UnknownPlayerNumberException, UsernameAlreadyExistsException, IOException {
        if (currentGames.isEmpty() || currentGames.getFirst().getNumOfPlayers() >= 4) {
            GameController newGame = new GameController(clients, socket, game);
            currentGames.add(newGame);
            return newGame;
        } else {
            GameController game = currentGames.getFirst();
            try {
                return game;
            } catch (GameFullException ignored) {
                return null;
            }
        }
    }

}


