package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.network.server.HandlingPlayerInputsThread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class GameController {
    private final Map<String, PrintWriter> players;
    int size;
    boolean isGameOver;
    private Game game;
    private List<HandlingPlayerInputsThread> clients;
    PrintWriter out;
    private boolean isSizeSet;
    private static boolean isCornerAlreadyChosen= false;
    private final CountDownLatch sizeLatch = new CountDownLatch(1);
    private static Player winningPlayer=null;
    private boolean areAllPlayersLogged=false;
    private int currentNumsOfPlayers=0;
    private CountDownLatch numbgames = new CountDownLatch(0);
    private int playerChoseinitialcard=0;
    private static boolean isTheFirstPlayer= false;
    private int logginPlayers=0;

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
        clients.removeIf(client -> client.getThreadPlayer().equals(player));
        currentNumsOfPlayers--;
        sendMessageToAllClients(player.getNickName() + " has disconnected.");

        // Check if there is any client still active
        if (players.isEmpty()) {
            setGameOver(true);
            sendMessageToAllClients("All players have disconnected. Game over.");
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

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
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

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    public void setSizeSet(boolean sizeSet) {
        isSizeSet = sizeSet;
    }




    @Override
    public String toString() {
        return "GameController{" +
                "players=" + players +
                ", size=" + size +
                '}';
    }

    public static void setWinningPlayer(Player winningPlayer) {
        GameController.winningPlayer = winningPlayer;
    }

    public int getCurrentNumsOfPlayers() {
        return currentNumsOfPlayers;
    }

    public int getPlayerChoseinitialcard() {
        return playerChoseinitialcard;
    }

    public void setPlayerChoseinitialcard(int playerChoseInitialCard) {
        this.playerChoseinitialcard = playerChoseInitialCard;
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


}