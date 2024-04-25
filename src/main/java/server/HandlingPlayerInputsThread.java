package server;
import controller.GameController;
import controller.TurnController;
import exceptions.ParametersNotValidException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import model.card.InitialCard;
import model.card.ObjectiveCard;
import model.game.Board;
import model.game.Dot;
import model.game.Game;
import model.game.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;



public class HandlingPlayerInputsThread implements Runnable {
    private static GameController gameController;
    private static TurnController turnController;
    private Player threadPlayer=null;
    private final Game game;
    private static Player currentPlayer;
    public BufferedReader stdIn;
    public PrintWriter out;
    private final List<HandlingPlayerInputsThread> clients;
    private static boolean checkGameInizialization;
    private final ServerLobby lobby;
    private List<Player> playersList;
    private Socket clientSocket;
    private String userName;

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException { //Costructor
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.playersList = playersinTheGame;
        boolean isGameStarted = false;
        this.clients = clients;
        gameController= null;
        turnController=null;
        this.lobby=lobby;
        this.userName=null;
        this.game=game;
        checkGameInizialization=false;
    }


    @Override
    public void run() {
        try {
                String clientSaysHello = stdIn.readLine();
                System.out.println("Il client ha detto " + clientSaysHello);    //Client says hello
                threadPlayer = loginEachClient();                               //EveryClient has to log in, we save his name information inside threadPLayer
                handlingTurns(playersList);                                     //Handling turns, first client will be the first player inside the game
                addingPlayersToTheGame();                                        //Adding players to the current game
                synchronized (this){                                             //First thread that accesses this function will assign all the cards to the players
                    if (!checkGameInizialization) {
                        initializeCards();
                    }
                }

                assigningSecretCard();                                            //Each thread will assign the secret card to the player
                assignInitialCard();
                System.out.println(game.getObjectiveDeck().carteRimaste());       //Debugging to check if all cards are given correctly
                startGame();                                                      //Game can eventually start
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

    //GetCurrentPLayer
    //if GetCurrentPlayer==String name -> azione


    private void assignInitialCard() throws IOException {
        InitialCard initialCard= game.getInitialCardDeck().firstCardInitialGame();
        sendMessageToClient("la tua carta iniziale è questa " +initialCard );
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(size);
        game.placeInitialCard(threadPlayer.getBoard(),initialCard);
        System.out.println("Carta iniziale piazzata correttamente");
        threadPlayer.getBoard().printBoard();
    }
    public synchronized void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }
    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }     //metodo per mandare un singolo messaggio al client










    //PRIVATE METHODS INSIDE HANDLINGPLAYERINPUTS

    private Player loginEachClient() throws IOException, InterruptedException {
        Player player = null;
        if(gameController==null) { //if game controller==null it means the player has to log in!!
            try {
                sendMessageToClient("Ciao! Devi fare il login. Inserisci il tuo nome per favore!");
                String request = stdIn.readLine();
                System.out.println("il nome del client è: " + request);
                sendMessageToClient("Login effettuato con successo!");
                Board board = new Board(50, 50);
                Dot dot= chooseClientDotColor(playersList);
                player = new Player(request, 0, dot, board);
                this.userName=request;
                playersList.add(player);
                System.out.println("Giocatori nel gioco: "+ playersList);
                System.out.println(player);
                gameController = lobby.login(request, out);
                System.out.println(gameController);
                setGameSize();
                sendMessageToClient("Sarai messo in sala d'attesa:");
                if (playersList.size() < gameController.getSize()) {
                    waitingForClients();
                }
            } catch (IOException | UsernameAlreadyExistsException | UnknownPlayerNumberException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(gameController);
        return player;
    }
    public synchronized void assigningSecretCard() throws IOException {
        List<ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        sendMessageToClient("scegli la carta obbiettivo:" + secretCards);
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(userName +"ha scelto la carta numero: "+ size);
        threadPlayer.setSecretChosenCard(secretCards.get(size-1));
        System.out.println(threadPlayer.toString());

    }
    private void startGame() throws IOException {
        while (true) {  //quale azione vuoi fare? fino a che non finsice il turno
            String messageFromClient = stdIn.readLine();        //messaggio dal thread client
            if(Objects.equals(currentPlayer.getNickName(), this.userName)){
                sendMessageToClient("è il tuo turno!!");
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
        String message;
        if (!gameController.isSizeSet()) {          //If controller number of players has not been decided
            //Tries to set controller's number of players
            try {
                sendMessageToClient("Scegli il numero di partecipanti al gioco-> Deve essere un numero compreso fra 2 e 4!");
                message= stdIn.readLine();
                int size = Integer.parseInt(message);
                System.out.println("Il numero di giocatori sarà " +size);
                sendMessageToClient("Numero di giocatori scelto correttamente");
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
    private Dot chooseClientDotColor(List<Player> playerlist) throws IOException {
        String message;
        Dot dot = null;
        boolean isTheColorOkay = false;
        while (!isTheColorOkay) {
            sendMessageToClient("Scegli il colore del tuo dot, puoi scegliere fra Blu, Rosso, Verde e Giallo!");
            message = stdIn.readLine();
            dot = Dot.valueOf(message);

            if (playerlist.size() != 0) {
                for (Player player : playerlist) {
                    if (player.getDot() == dot) {
                        sendMessageToClient("Quel colore è gia stato scelto da un altro utente, perfavore inserire un altro colore");
                    } else {
                        System.out.println("Colore scelto dal client: " + message);
                        sendMessageToClient("Colore del dot scelto correttamente");
                        isTheColorOkay = true;

                    }
                }
            } else{
                System.out.println("Colore scelto dal client: " + message);
                sendMessageToClient("Colore del dot scelto correttamente");
                isTheColorOkay = true;
             }
        }
        return dot;
    }
    private void runCommand(String messageFromClient) throws NoSuchElementException {
        //If player has logged in and their game's number of players has been decided
        if (gameController != null ) {


            //Forward player command to controller
            System.out.println("Received command: " + messageFromClient);
            gameController.readCommand(userName, messageFromClient);
        }
    }
    private synchronized void waitingForClients() throws InterruptedException {
        System.out.println("In attesa di altri giocatori");
        while (playersList.size() != 2) {
            {
                wait(10000);
            }
        }
    }
    private void initializeCards(){
        game.assignResourcesAndGoldCardsToPlayers();
        checkGameInizialization = true;
        System.out.println(game.CardsIndeck());
        System.out.println(game.GoldsIndeck());
    }
    private void  addingPlayersToTheGame()
    {
        for(Player playerInGame: playersList)                           //Adding all the players to the Game
        {
            game.addPlayer(playerInGame);
        }
    }
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

}


