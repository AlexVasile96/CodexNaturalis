package network.server;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import controller.*;
public class Server {
    private final GameController gameController; //IMPORTING GAME CONTROLLER
    private final Map<String, ClientHandler> clientHandlerMap;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final Object lock;

    public Server(GameController gameController) { //COSTRUCTOR
        this.gameController = gameController;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
        this.lock = new Object();
    }

    public void addPlayer(String nickname, ClientHandler clientHandler) {
        VirtualView vv = new VirtualView(clientHandler);

        if (!gameController.isGameStarted()) {
            if (gameController.checkLoginNickname(nickname, vv)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, vv);
            }
        } else {
            vv.showLoginResult(true, false, null);
            clientHandler.disconnect();
        }

    }

    public void removeClient(String nickname, boolean notifyEnabled) {
        clientHandlerMap.remove(nickname);
        gameController.removeVirtualView(nickname, notifyEnabled);
        LOGGER.info(() -> "Removed " + nickname + " from the client list.");
    }

    public void onMessageReceived(Message message) {
        gameController.onMessageReceived(message);
    }

    public void onDisconnect(ClientHandler clientHandler) {
        synchronized (lock) {
            String nickname = getNicknameFromClientHandler(clientHandler);
            if (nickname != null) {
                boolean gameStarted = gameController.isGameStarted();
                removeClient(nickname, !gameStarted); // enable lobby notifications only if the game didn't start yet.

                if(gameController.getTurnController() != null &&
                        !gameController.getTurnController().getNicknameQueue().contains(nickname)) {
                    return;
                }

                // Resets server status only if the game was already started.
                // Otherwise the server will wait for a new player to connect.
                if (gameStarted) {
                    gameController.broadcastDisconnectionMessage(nickname, " disconnected from the server. GAME ENDED.");

                    gameController.endGame();
                    clientHandlerMap.clear();
                }
            }
        }
    }


    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
