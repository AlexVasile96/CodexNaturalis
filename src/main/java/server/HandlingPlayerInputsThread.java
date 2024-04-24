package server;

import com.google.gson.Gson;
import controller.GameController;
import controller.TurnController;
import exceptions.OperationCancelledException;
import exceptions.ParametersNotValidException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import model.card.ObjectiveCard;
import model.game.Board;
import model.game.Dot;
import model.game.Game;
import model.game.Player;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


/*
* login ✔️
* client chiede di potere svolgere un'azione ✔️
* questo messaggio viene ricevuto dal server.
* server-> metodo runCommand->GameController
* gameController-> Command(WhatCanPlayerDo)
* whatcanplayerdo-> Game
*
*
*
* endTurn-> nextTurn da turn controller per passare il gioco al prossimo giocatore
* */



public class HandlingPlayerInputsThread implements Runnable {
    private static GameController gameController;
    private static TurnController turnController;
    private Player threadPlayer=null;
    private Game game;
    private static Player currentPlayer;
    public BufferedReader stdIn;
    public PrintWriter out;
    private ClientView clientView;
    private List<HandlingPlayerInputsThread> clients;
    private boolean isGameStarted;
    private static boolean checkGameInizialization;
    private final ServerLobby lobby;
    private Gson gson;
    private List<Player> playersList;
    private Socket clientSocket;
    private String userName;

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException { //Costructor
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientView = clientView;
        this.gson = new Gson();
        this.playersList = playersinTheGame;
        this.isGameStarted = false;
        this.clients = clients;
        this.gameController= null;
        this.turnController=null;
        this.lobby=lobby;
        this.userName=null;
        this.game=game;
        this.checkGameInizialization=false;
    }


    @Override
    public void run() {
        try {
                String firstMessag = stdIn.readLine();
                System.out.println("Il client ha detto " + firstMessag);
                threadPlayer = loginEachClient();
                //setGameSize();

                handlingTurns(playersList);
                for(Player playerInGame: playersList)
                {
                    game.addPlayer(playerInGame);
                }
                synchronized (this){
                    if (!checkGameInizialization) {
                        game.assignResourcesAndGoldCardsToPlayers();
                        checkGameInizialization = true;
                        System.out.println(game.CardsIndeck());
                        System.out.println(game.GoldsIndeck());
                    }
                }
                assigningSecretCard();
                System.out.println(game.getObjectiveDeck().carteRimaste());
                //firstObjectiveCard(); //carta segreta obiettivo
                startGame();

        } catch (IOException e) {
            System.err.println("Io exception client handler");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
            try {
                stdIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Player loginEachClient() throws IOException, InterruptedException {
        Player player = null;
            if(gameController==null) { //if game controller==null it means the player has to log in!!

                try {
                    out.println("Ciao! Devi fare il login. Inserisci il tuo nome per favore!");
                    String request = stdIn.readLine();
                    System.out.println("il nome del login è: " + request);
                    out.println("Login effettuato con successo");
                    Board board = new Board(50, 50);
                    player = new Player(request, 0, Dot.BLACK, board);
                    this.userName=request;
                    playersList.add(player);
                    System.out.println("Giocatori nel gioco: "+ playersList);
                    System.out.println(player);
                    gameController = lobby.login(request, out);

                    System.out.println(gameController);
                    setGameSize();
                    out.println("Sarai messo in sala d'attesa:");
                    if (playersList.size() < 2) {
                        inattesa();
                        //stampa punteggio giocatori *da spostare
                        for (Player p : playersList) {
                            p.visualizePlayerScore();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (UnknownPlayerNumberException e) {
                    throw new RuntimeException(e);
                } catch (UsernameAlreadyExistsException e) {
                    throw new RuntimeException(e);
                }
            }
                System.out.println(gameController);
                //notifyGameStart();
        return player;
    }

    public synchronized void assigningSecretCard() throws IOException {
        List<ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        out.println("scegli la carta obbiettivo:" + secretCards);
        String strgIntero = stdIn.readLine();
        int size = Integer.parseInt(strgIntero);
        System.out.println(userName +"ha scelto la carta numero: "+ size);
        threadPlayer.setSecretChosenCard(secretCards.get(size-1));
        threadPlayer.toString();

    }

    private void startGame() throws IOException {
        while (true) {  //quale azione vuoi fare? fino a che non finsice il turno
            String messageFromClient = stdIn.readLine();        //messaggio dal thread client
            if(Objects.equals(currentPlayer.getNickName(), this.userName)){
                out.println("è il tuo turno!!");
                System.out.println(game.CardsIndeck());
                System.out.println(game.GoldsIndeck());
                System.out.println("Il client ha selezionato: " + messageFromClient);
                runCommand(messageFromClient);
            }
            else{
                out.println("Aspetta non è il tuo turno!");
            }
        }
    }





    private void setGameSize() throws NoSuchElementException {
        String messaggio=null;
        if (!gameController.isSizeSet()) {          //If controller number of players has not been decided
            //Tries to set controller's number of players
            try {
                out.println("Scegli il numero di partecipanti al gioco-> Deve essere un numero compreso fra 2 e 4!");
                messaggio= stdIn.readLine();
                int size = Integer.parseInt(messaggio);
                System.out.println("Il numero di giocatori sarà " +size);
                out.println("Numero di giocatori scelto correttamente");
                gameController.choosePlayerNumber(size);
                gameController.setSizeSet(true);

            } catch (NumberFormatException ex) {
               sendMessageToClient("Game's number of players must be an integer.");
            } catch (Exception ex) {
                sendMessageToClient("Errore");
            } catch (ParametersNotValidException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Player doesn't need to choose game number of players");
            sendMessageToClient("NO");
        }
    }
    private void runCommand(String messageFromClient) throws NoSuchElementException {
        //If player has logged in and their game's number of players has been decided
        if (gameController != null ) {
            //if(gameController.isSizeSet())

            //Forward player command to controller
            System.out.println("Received command: " + messageFromClient);
            gameController.readCommand(userName, messageFromClient);
        }
    }


    public synchronized void inattesa() throws InterruptedException {
        System.out.println("In attesa di altri giocatori");
        while (playersList.size() != 2) {
            {
                wait(10000);
            }
        }
        notifyAll();
        return;
    } //metodo che mette in attesa i client fino a che non si è raggiunto il numero voluto di giocatori

    /*public synchronized void notifyGameStart() {
        if (!isGameStarted) {
            isGameStarted = true;
            for (HandlingPlayerInputsThread thread : clients) {
                thread.sendMessageToClient("Il gioco è iniziato!");
                int i = 1;
                for (Player p : playersList) {
                    thread.sendBooleanToClient(true);
                    thread.sendMessageToClient("il " + i + "giocatore è: " + p.getNickName());
                    i++;
                }
                thread.sendBooleanToClient(false);
            }
        }
    }*/

    //GetCurrentPLayer
    //if GetCurrentPlayer==String name -> azione









    private void handlingTurns(List<Player> playerList)
    {
        turnController= new TurnController(playerList);
        System.out.println(turnController.getPlayers());
        currentPlayer = turnController.getCurrentPlayer(); //viene preso il primo
        System.out.println("Il primo giocatore è " + currentPlayer);

        //while(currentplayer.getUsername==this.username)
        //a-> current player
        //b
        //c
        //d

    }







    public synchronized void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }
    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }     //metodo per mandare un singolo messaggio al client
    public synchronized boolean sendBooleanToClient(boolean value) {   //metodo per mandare un singolo boolean al client
        out.println(value);
        return value;
    }

}


