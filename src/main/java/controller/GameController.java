package controller;

import exceptions.*;
import model.card.GoldCardConstructor;
import model.card.InitCardConstructor;
import model.card.ObjectiveCardConstructor;
import model.card.ResourceCardConstructor;
import model.deck.Deck;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;
import model.game.Game;
import model.game.Player;
import network.message.Command;
import network.message.MessagesEnum;
import com.google.gson.Gson;
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
    Gson gson;
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
        this.gson = new Gson();
        this.game=game;
        this.clients=clients;
        this.out= new PrintWriter(socket.getOutputStream(), true);
        this.isSizeSet=false;
    }


    public synchronized void readCommand(String username, String commandString) {
        if (game != null) {
            Command command = new Command();
            String result = command.runCommand(game, commandString);
            // Gestisci il risultato come desideri, ad esempio inviandolo al client
            /*if (result != null) {
                sendMessageToClient(result);
            }*/
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