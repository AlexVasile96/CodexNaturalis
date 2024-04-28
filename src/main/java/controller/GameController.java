package controller;

import exceptions.*;
import model.deck.Deck;
import model.game.Game;
import model.game.Player;
import server.Command;
import server.HandlingPlayerInputsThread;
import view.ClientView;

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
    private Game game;
    private List<HandlingPlayerInputsThread> clients;
    PrintWriter out;
    private boolean isSizeSet;
    public static Deck resourceDeck;
    public static Deck goldDeck;
    public static Deck initialDeck;
    private static Deck objectiveDeck;
    private Map<String, ClientView> clientViews = new HashMap<>();




    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut, List<HandlingPlayerInputsThread> clients, Socket socket, Game game) throws IOException {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        players.put(username, userOut);
        this.game=game;
        this.clients=clients;
        this.out= new PrintWriter(socket.getOutputStream(), true);
        this.isSizeSet=false;
    }


    public synchronized void readCommand(String commandString, Player player, int size) {
        if (game != null) {
            Command command = new Command();
            String result = command.runCommand(game, commandString, player,size);
            sendMessageToClient(result);

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
        players.put(username, userOut);
    }

    public void choosePlayerNumber(int number) throws PlayerNumberAlreadySetException, ParametersNotValidException {
        if (size > 0) {
            throw new PlayerNumberAlreadySetException();
        }

        if (number < 1 || number > 4) {
            throw new ParametersNotValidException();
        }
        size = number;
    }
    public int getNumOfPlayers() {
        return players.size();
    }

    public Map<String, PrintWriter> getPlayers() {
        return players;
    }

    public int getSize() {
        return size;
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<HandlingPlayerInputsThread> getClients() {
        return clients;
    }

    public void setClients(List<HandlingPlayerInputsThread> clients) {
        this.clients = clients;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public static Deck getResourceDeck() {
        return resourceDeck;
    }

    public static void setResourceDeck(Deck resourceDeck) {
        GameController.resourceDeck = resourceDeck;
    }

    public static Deck getGoldDeck() {
        return goldDeck;
    }

    public static void setGoldDeck(Deck goldDeck) {
        GameController.goldDeck = goldDeck;
    }

    public static Deck getInitialDeck() {
        return initialDeck;
    }

    public static void setInitialDeck(Deck initialDeck) {
        GameController.initialDeck = initialDeck;
    }

    public static Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    public static void setObjectiveDeck(Deck objectiveDeck) {
        GameController.objectiveDeck = objectiveDeck;
    }

    public Map<String, ClientView> getClientViews() {
        return clientViews;
    }

    public void setClientViews(Map<String, ClientView> clientViews) {
        this.clientViews = clientViews;
    }

    private void updateClientView(String username, Player player) {
        // Ottieni l'istanza corretta di ClientView associata al client
        ClientView clientView = getClientViewByUsername(username);
        if (clientView != null) {
            // Aggiorna la vista del client con le nuove informazioni del giocatore
            clientView.update(username, player);
        }
    }
    private ClientView getClientViewByUsername(String username) {
        return clientViews.get(username);
    }














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
        return isSizeSet;
    }
    public void setSizeSet(boolean sizeSet) {
        isSizeSet = sizeSet;
    }
    public void setDisconnectedStatus(String username) {
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