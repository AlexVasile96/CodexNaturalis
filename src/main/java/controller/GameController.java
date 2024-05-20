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
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public GameController() {
        this.players = new HashMap<>();
        this.size = 0;
        this.isGameOver = false;
        this.isSizeSet = false;
    }




    public synchronized void readCommand(String commandString, Player player, int size, int paolo, String cornerChosen) {
        if (game != null) {
            Command command = new Command();
            System.out.println("Command received :" + commandString +" i'm in gamecontroller");
            if(commandString.equals("SETUPFINISHED"))
            {
                String result = command.runCommand(game, commandString, player,size,paolo, cornerChosen);
                sendMessageToAllClients(result);
                if(!isTheFirstPlayer){
                game.setCurrentPlayer(player.getClientView().getUserName());
                isTheFirstPlayer=true;
                }
                sendMessageToAllClients(game.getCurrentPlayer());
            }
            if(commandString.equals("playCard"))
            {
                if(!isCornerAlreadyChosen) {
                    String cornersAvailable = command.runCommand(game, commandString, player, size, paolo, cornerChosen);
                    sendMessageToAllClients(cornersAvailable); //Mando al client i corners disponibili
                    isCornerAlreadyChosen=true;
                }
                else {
                    String cornersChosen= command.runCommand(game,commandString,player,size,paolo,cornerChosen);
                    sendMessageToAllClients(cornersChosen);
                    isCornerAlreadyChosen=false;
                }
            }
            else {
                String result = command.runCommand(game, commandString, player,size,paolo, cornerChosen);
                sendMessageToAllClients(result);
            }
        }
    }




    public synchronized void addPlayer(String username, PrintWriter userOut) throws GameFullException, UnknownPlayerNumberException, UsernameAlreadyExistsException, InterruptedException {
        if (!isSizeSet){
            sizeLatch.await();
        }
        if (!players.containsKey(username)) {
            if (players.size() >= size || isGameOver) {
                throw new GameFullException();
            }
        } else {
            throw new UsernameAlreadyExistsException();
        }
        players.put(username, userOut);
        if(getCurrentNumsOfPlayers()==0)
        {
            setCurrentNumsOfPlayers(getCurrentNumsOfPlayers()+2);
        }
        else  setCurrentNumsOfPlayers(getCurrentNumsOfPlayers()+1);

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

    }
    public void loadGameOrStartNewGame() {

        List<String> savedPlayers = loadSavedPlayers();
        System.out.println(savedPlayers);
        if (allClientsMatchSavedPlayers(savedPlayers)) {

            System.out.println("Loading existing game");
        } else {

            System.out.println("Creating a new game");
        }
    }

    private List<String> loadSavedPlayers() {
        List<String> savedPlayers = null;
        try {
            Path filePath = Paths.get(SAVE_FILE_PATH);
            savedPlayers = Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedPlayers;
    }

    private boolean allClientsMatchSavedPlayers(List<String> savedPlayers) {
        return savedPlayers != null && new HashSet<>(savedPlayers).containsAll(players.keySet());
    }

public synchronized void waitingForPLayers() throws InterruptedException {
        if(currentNumsOfPlayers==getSize())
        {
            //numbgames.countDown();
            sendMessageToAllClients("All clients connected");
            notifyAll();

        }
        else{
            System.out.println("current numb of players da gamecontroller " + currentNumsOfPlayers);
            System.out.println("size dal gamecontroller "+  getSize());
            //numbgames.await();
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
            System.out.println("current numb of players da gamecontroller " + currentNumsOfPlayers);
            System.out.println("size dal gamecontroller "+  getSize());
            wait();
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
    public Set<String> getConnectedPlayerUsername() {
        return players.keySet();
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
}