package network.server;

import Exceptions.GameFullException;
import controller.GameController;

import java.io.PrintWriter;

public class ServerLobby {
    private GameController singleGame = null;

    /**
     * Allows a player to log in to the lobby.
     *
     * @param username The username of the player trying to log in.
     * @param userOut  The PrintWriter associated with the player's output stream.
     * @return The GameController object associated with the logged-in player.
     */

    public synchronized GameController login(String username, PrintWriter userOut){
        //At the moment we are taking only one player for testing.
        if (singleGame == null || singleGame.getNumOfPlayers() == 0) {
            // If there's no game or the current game has no players, create a new GameController.
            singleGame = new GameController(username, userOut);
            return singleGame;
        } else {

            try {
                // If the current game is not full, add the player to the existing game.
                singleGame.addPlayer(username, userOut);
                return singleGame;
            } catch (GameFullException ignored) {
                // If the current game is full, create a new GameController for the player.
                singleGame = new GameController(username, userOut);
                return singleGame;
            }
        }
    }
    /**
     * Aborts the game associated with the given GameController.
     *
     * @param controller The GameController object representing the game to be aborted.
     */

    public void abortGame(GameController controller) {
        // This method will implement game abortion logic when needed.
    }
}

