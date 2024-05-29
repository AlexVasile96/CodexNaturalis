package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.network.server.HandlingPlayerInputsThread;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;

//import static server.HandlingPlayerInputsThread.turnController;

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
    private static boolean isCornerAlreadyChosen= false;
    private final CountDownLatch sizeLatch = new CountDownLatch(1);
    private static final String SAVE_FILE_PATH = "src/main/resources/saveplayers.json";
    private static Player winningPlayer=null;
    private boolean areAllPlayersLogged=false;
    private int currentNumsOfPlayers=0;
    private CountDownLatch numbgames = new CountDownLatch(0);
    private int playerChoseinitialcard=0;
    private static boolean isTheFirstPlayer= false;
    private static String currentPlayerName=null;
    private int logginPlayers=0;
    private static int howManyPlayersDoIHave=0;
    private boolean goldcard=false;



    //CONSTRUCTORS

    public GameController(String username, PrintWriter userOut, List<HandlingPlayerInputsThread> clients, Socket socket, Game game) throws IOException {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        //players.put(username, userOut);
        this.game=game;
        this.clients=clients;
        this.out= new PrintWriter(socket.getOutputStream(), true);
        this.isSizeSet=false;
    }

    public GameController() {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        this.isSizeSet = false;
    }


    public synchronized void readCommand(String commandString, Player player, int size, int paolo, String cornerChosen) {
        if (game != null) {
            Command command = new Command();
            System.out.println("Command received :" + commandString + " i'm in gamecontroller");
            String result;
            switch (commandString) {
                case "playCard":
                    if (!isCornerAlreadyChosen) {
                        String cornersAvailable = command.runCommand(game, commandString, player, size, paolo, cornerChosen);
                        sendMessageToAllClients(cornersAvailable); // Mando al client i corners disponibili
                        isCornerAlreadyChosen = true;
                    } else if(cornerChosen.equals("clean")){
                        isCornerAlreadyChosen = false;
                        command.runCommand(game, commandString, player, size, paolo, cornerChosen);
                    } else {
                        String cornersChosen = command.runCommand(game, commandString, player, size, paolo, cornerChosen);
                        sendMessageToAllClients(cornersChosen);
                        isCornerAlreadyChosen = false;
                    }
                    break;
                default:
                    result = command.runCommand(game, commandString, player, size, paolo, cornerChosen);
                    sendMessageToAllClients(result);
                    break;
            }
        }
    }



    public synchronized void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException, UsernameAlreadyExistsException, InterruptedException {
        if (!isSizeSet){
            sizeLatch.await();
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
            numbgames = new CountDownLatch(size-1);
            sizeLatch.countDown();
            System.out.println(size);
            setSize(size);
            System.out.println("size: " + getSize());
            setSizeSet(true);
            game.setTotalNumberOfPLayer(size);
            saveGameSizeToJson(); // Saving game dimension inside json
    }


public synchronized void waitingForPLayers() throws InterruptedException {
        if(currentNumsOfPlayers==getSize())
        {
            notifyAll();
        }
        else{
            if(currentNumsOfPlayers==0){
                System.out.println("Current numb of players is " + currentNumsOfPlayers);
                currentNumsOfPlayers=currentNumsOfPlayers+2;
            }
            else currentNumsOfPlayers++;
            wait();
        }
}


    public synchronized void waitingForPLayersAfterInitialcard() throws InterruptedException {
        if(playerChoseinitialcard==getSize())
        {
            sendMessageToAllClients("All clients chose the init Card");
            notifyAll();
        }
        else{
            wait();
        }
    }

    public synchronized void removePlayer(Player player) {
        // Remove players from player's map
        players.remove(player.getNickName());

        // Remove player from client's list
        clients.removeIf(client -> client.getThreadPlayer().equals(player));

        // Updating number of connected players
        currentNumsOfPlayers--;

        // Notify to all players
        sendMessageToAllClients(player.getNickName() + " has disconnected.");

        // Check if there is any client still active
        if (players.isEmpty()) {
            setGameOver(true);
            sendMessageToAllClients("All players have disconnected. Game over.");
        } else {
            // Update turns and game logic if necessary

        }
    }




    public int getNumOfPlayers() {
        return players.size();
    }

    public Map<String, PrintWriter> getPlayers() {
        return players;
    }

    public synchronized int getSize() {
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


    private ClientView getClientViewByUsername(String username) {
        return clientViews.get(username);
    }














    public synchronized void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }

    public void setSize(int size) {
        this.size = size;
        sizeLatch.countDown(); // Sblocca il thread che sta aspettando la dimensione del gioco
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

    public static Player getWinningPlayer() {
        return winningPlayer;
    }

    public static void setWinningPlayer(Player winningPlayer) {
        GameController.winningPlayer = winningPlayer;
    }

    public boolean isAreAllPlayersLogged() {
        return areAllPlayersLogged;
    }

    public void setAreAllPlayersLogged(boolean areAllPlayersLogged) {
        this.areAllPlayersLogged = areAllPlayersLogged;
    }

    public int getCurrentNumsOfPlayers() {
        return currentNumsOfPlayers;
    }

    public void setCurrentNumsOfPlayers(int currentNumsOfPlayers) {
        this.currentNumsOfPlayers = currentNumsOfPlayers;
    }

    public int getPlayerChoseinitialcard() {
        return playerChoseinitialcard;
    }

    public void setPlayerChoseinitialcard(int playerChoseinitialcard) {
        this.playerChoseinitialcard = playerChoseinitialcard;
    }

    public int getLogginPlayers() {
        return logginPlayers;
    }

    public static boolean isIsTheFirstPlayer() {
        return isTheFirstPlayer;
    }

    public static void setIsTheFirstPlayer(boolean isTheFirstPlayer) {
        GameController.isTheFirstPlayer = isTheFirstPlayer;
    }

    public void setLogginPlayers(int logginPlayers) {
        this.logginPlayers = logginPlayers;
    }

    public void saveGameSizeToJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("size", this.size);
        try (FileWriter file = new FileWriter("src/main/resources/gameSize.json")) {
            file.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int loadGameSizeFromJson() {
        try (FileReader reader = new FileReader("src/main/resources/gameSize.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            this.size = jsonObject.get("size").getAsInt();
            this.isSizeSet = true;
           // sizeLatch.countDown(); // Sblocca il thread che sta aspettando la dimensione del gioco
            return size;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getHowManyPlayersDoIHave() {
        return howManyPlayersDoIHave;
    }

    public static void setHowManyPlayersDoIHave(int howManyPlayersDoIHave) {
        GameController.howManyPlayersDoIHave = howManyPlayersDoIHave;
    }
}