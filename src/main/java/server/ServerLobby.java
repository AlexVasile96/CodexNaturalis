package server;

import Exceptions.GameFullException;
import Exceptions.UnknownPlayerNumberException;
import Exceptions.UsernameAlreadyExistsException;
import controller.GameController;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ServerLobby {
    private final List<GameController> currentGames;
    public ServerLobby() {
        currentGames = new ArrayList<>();
    }

    public synchronized GameController login(String username, PrintWriter userOut) throws UnknownPlayerNumberException, UsernameAlreadyExistsException {

        //Checks if the given username is already taken, and attempts to add the player to the first game that isn't full or to the one they belonged before disconnection
        for (int i = 0; i < currentGames.size(); i++) {
            GameController game = currentGames.get(i);

            //If all players have left during creation phase, or after the game has ended, remove the game from the list
            if (game.getNumOfPlayers() == 0) {
                System.out.println("Deleted a game.");
                currentGames.remove(game);
                i--;
            } else {
                //Attempts to add the player to the game
                try {
                    game.addPlayer(username, userOut);
                    return game;
                } catch (GameFullException ignored) {}
            }
        }

        //If there were no games waiting for players, create a new one
        GameController newGame = new GameController(username, userOut);
        currentGames.add(newGame);

        return newGame;
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

