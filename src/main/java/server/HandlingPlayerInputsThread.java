package server;

import com.google.gson.Gson;
import controller.GameController;
import exceptions.OperationCancelledException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


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
*
* */



public class HandlingPlayerInputsThread implements Runnable {
    public BufferedReader stdIn;
    public PrintWriter out;
    private ClientView clientView;
    private List<HandlingPlayerInputsThread> clients;
    private boolean isGameStarted;
    private boolean doClose;
    private final ServerLobby lobby;
    private Gson gson;
    private List<Player> playersList = new ArrayList<>();
    private Socket clientSocket;
    private Integer x;
    private GameController gameController;
    private String userName;

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby) throws IOException { //Costructor
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientView = clientView;
        this.doClose = false;
        this.gson = new Gson();
        this.playersList = playersinTheGame;
        this.x = 0;
        this.isGameStarted = false;
        this.clients = clients;
        this.gameController= null;
        this.lobby=lobby;
        this.userName=null;
    }


    @Override
    public void run() {
        try {
                String firstMessag = stdIn.readLine();
                System.out.println("Il client ha detto " + firstMessag);
                loginEachClient();
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

    private void initializePLayersResources() {
    }

    private void loginEachClient() throws IOException, InterruptedException {
            if(gameController==null) { //if game controller==null it means the player has to log in!!
                try {
                    out.println("Ciao! Devi fare il login. Inserisci il tuo nome per favore!");
                    String request = stdIn.readLine();
                    System.out.println("il nome del login è: " + request);
                    out.println("Login effettuato con successo");
                    Board board = new Board(50, 50);
                    Player player = new Player(request, 0, Dot.BLACK, board);
                    this.userName=request;
                    playersList.add(player);
                    System.out.println(player);
                    out.println("Sarai messo in sala d'attesa:");
                    gameController = lobby.login(request, out);
                    System.out.println(gameController);
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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

                System.out.println(gameController);
                notifyGameStart();
    }

    private void startGame() throws IOException {
        while (true) {
            //quale azione vuoi fare? fino a che non finsice il turno
            String messageFromClient = stdIn.readLine();
            System.out.println("Il client ha seleionato: " + messageFromClient);
            switch (messageFromClient) {
                case "COMMAND" -> //runCommand(messageFromClient);
                        System.out.println("ciao");
                case "PING" -> {
                }
                case "help" -> System.out.println("Il player ha chiesto una mano");
                default -> System.out.println("Client sent an unexpected message: ");

            }

        }
    }
    private void runCommand(String messageFromClient) throws NoSuchElementException {

        //If player has logged in and their game's number of players has been decided
        if (gameController != null && gameController.isSizeSet()) {

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

    public synchronized void notifyGameStart() {
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
    }

    //metodo per mandare un singolo messaggio al client
    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }

    //metodo per mandare un singolo boolean al client
    public synchronized boolean sendBooleanToClient(boolean value) {
        out.println(value);
        return value;
    }

    public synchronized void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }

}


