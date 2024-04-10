package network.server;

import Exceptions.GameFullException;
import controller.GameController;

import java.io.PrintWriter;

public class ServerLobby {
    private GameController singleGame = null;

    public synchronized GameController login(String username, PrintWriter userOut){
        //At the moment we are taking only one player for testing.
        if (singleGame == null || singleGame.getNumOfPlayers() == 0) {

            singleGame = new GameController(username, userOut);
            return singleGame;
        } else {

            try {
                singleGame.addPlayer(username, userOut);
                return singleGame;
            } catch (GameFullException ignored) {

                singleGame = new GameController(username, userOut);
                return singleGame;
            }
        }
    }

    public void abortGame(GameController controller) {
    }
}

