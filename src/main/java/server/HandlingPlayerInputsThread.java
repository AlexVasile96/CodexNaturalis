package server;
import controller.GameController;
import controller.TurnController;
import exceptions.ParametersNotValidException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import exceptions.turnPlayerErrorException;
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
    private static int index=0;


    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException { //Costructor
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.playersList = playersinTheGame;
        //boolean isGameStarted = false;
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
        synchronized (this) {
            try {
                String clientSaysHello = stdIn.readLine();
                System.out.println("Il client ha detto " + clientSaysHello);    //Client says hello
                threadPlayer = loginEachClient();                               //EveryClient has to log in, we save his name information inside threadPLayer
                handlingTurns(playersList);                                     //Handling turns, first client will be the first player inside the game
                addingPlayersToTheGame();                                        //Adding players to the current game
                synchronized (this) {                                             //First thread that accesses this function will assign all the cards to the players
                    if (!checkGameInizialization) {
                        initializeCards();
                    }
                }
                assigningSecretCard();                                            //Each thread will assign the secret card to the player
                assignInitialCard();                                                //Each client places his first card
                //sendingClientHisFirstThreeCards();
                for (Player player : playersList) {
                    game.updateSingleClientView(player);                      //Updating each player ClientView
                    System.out.println(player.getClientView());
                }// Momo: perche non solo quella del thread player?
                System.out.println(game.getObjectiveDeck().carteRimaste());         //Debugging to check if all cards are given correctly
                sendMessageToClient(currentPlayer.getNickName());
                boolean hasClientQuit= false;
                while (!hasClientQuit){
                    startGame();
                    System.out.println("Il client è cambiato, ora tocca a " + currentPlayer);
                    hasClientQuit=true;
                }
                clients.remove(this);
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.err.println("Errore durante la chiusura del socket del client: " + ex.getMessage());
                }
                System.out.println("Connessione chiusa dal client.");

            } catch (IOException e) {

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startGame() throws IOException, InterruptedException {
        String messageFromClient;
        boolean endturnphase=false;
        while (!endturnphase) {
            System.out.println("Sto aspettando che il client " + currentPlayer.getNickName() + " mi faccia richiesta");
            messageFromClient = stdIn.readLine();
            System.out.println("Il client ha selezionato: " + messageFromClient);
            //if(messageFromClient.equals("endTurn")) endturnphase=true;
            runCommand(messageFromClient, threadPlayer); //->run
            if(messageFromClient.equals("quit")){
                endturnphase=true;
            }

        }
    }
    private void sendingClientHisFirstThreeCards() throws IOException {
        runCommand("showYourCardDeck", threadPlayer);
    }







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
                Dot dot= chooseClientDotColor();
                player = new Player(request, 0, dot, board);
                this.userName=request;
                synchronized (this){
                player.setIndex(index);
                index++;
                }
                playersList.add(player);
                System.out.println("Giocatori nel gioco: "+ playersList);
                System.out.println(player);
                gameController = lobby.login(request, out);
                System.out.println(gameController);
                setGameSize();
                sendMessageToClient("Sarai messo in sala d'attesa:");
                if (playersList.size() < gameController.getSize()) {
                    System.out.println("in attesa di: "+ (gameController.getSize()-playersList.size()) + "giocatori");
                    waitingForClients();
                }
            } catch (IOException | UsernameAlreadyExistsException | UnknownPlayerNumberException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(gameController);
        return player;
    }
    private synchronized void assigningSecretCard() throws IOException {
        List<ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        secretCards.add( game.getObjectiveDeck().drawObjectiveCard());
        sendMessageToClient("scegli la carta obiettivo:" + secretCards);
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(userName +"ha scelto la carta numero: "+ size);
        threadPlayer.setSecretChosenCard(secretCards.get(size-1));
        System.out.println(threadPlayer.toString());
    }
    private void setGameSize() throws NoSuchElementException {
        String message;
        if (!gameController.isSizeSet()) {          //If controller number of players has not been decided
            //Tries to set controller's number of players
            try {
                sendMessageToClient("At the moment there is: ");
                sendMessageToClient("1");
                sendMessageToClient(" player. Choose how many players you want to play with-> players have to be from 2 to 4.");
                message= stdIn.readLine();
                int size = Integer.parseInt(message);
                System.out.println("Numbers of Player will be " +size);
                sendMessageToClient("Players number correctly chosen ");
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
            sendMessageToClient("There's already someone online! You will be ");
            sendMessageToClient(String.valueOf(gameController.getSize()));
            sendMessageToClient(" players");
        }
    }
    private Dot chooseClientDotColor() throws IOException {
        String message;
        Dot dot;
        do{
            sendMessageToClient("Choose the color of your dot, you can choose between: " + game.getDots());
            message = stdIn.readLine();
            if (!game.isInDots(message)) {
                sendMessageToClient("Chosen color not available!");
            }
            else sendMessageToClient("color chosen correctly:");
        }while(!game.isInDots(message));
        dot = Dot.valueOf(message);
        System.out.println("Colore scelto dal client: " + message);
        game.removeDot(message);
        return dot;
    }
    private void runCommand(String messageFromClient, Player player) throws NoSuchElementException, IOException {
        if (gameController != null ) {
            String cornerChosen=null;
            System.out.println("Received command: " + messageFromClient); //Forward player command to controller
            switch (messageFromClient) {
                case "endTurn" -> {
                    endTurn(player, turnController);
                    gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen);
                }
                case "playCard" -> {
                    String sentBoard = "showBoard";
                    System.out.println("Sono in runCommand");
                    gameController.readCommand(sentBoard, player, 0, 0, cornerChosen); //In questo modo, al player viene fatta visualizzare la propria Board
                    String indexCardChosen = stdIn.readLine(); //Memorizzo quale carta del proprio deck il player ha deciso di giocare
                    int cardChosenFromHisDeck = Integer.parseInt(indexCardChosen);
                    System.out.println("Il player ha scelto la carta numero " + cardChosenFromHisDeck);
                    String CardOnTheBoardChosen = stdIn.readLine();
                    int boardCardChosen = Integer.parseInt(CardOnTheBoardChosen);
                    System.out.println("Il player ha deciso di giocare la proria carta sulla carta numero " + boardCardChosen);
                    System.out.println("Mando tutto al GameController");
                    gameController.readCommand(messageFromClient, player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                    cornerChosen = stdIn.readLine();
                    System.out.println(cornerChosen);
                    gameController.readCommand(messageFromClient, player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                    messageFromClient = stdIn.readLine();
                    runCommand(messageFromClient, player);
                }
                //else if(messageFromClient.equals("drawCardFromWell")){
                case "drawCard" -> {
                    //intro
                    messageFromClient = stdIn.readLine();
                    gameController.readCommand(messageFromClient, player, 0, 0, null);// showwell


                    //well o deck?
                    messageFromClient = stdIn.readLine();
                    if (messageFromClient.equals("deck")) {
                        messageFromClient = stdIn.readLine();//gold o resource
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                        messageFromClient = stdIn.readLine();//showdeck
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                    } else if (messageFromClient.equals("well")) {
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);//showwell
                        int index = Integer.parseInt(stdIn.readLine());
                        gameController.readCommand("drawCardFromWell", player, index, 0, null);//drawCardFromWll
                        messageFromClient = stdIn.readLine();//showdeck
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                        messageFromClient = stdIn.readLine();//showwell
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                    } else {
                        System.out.println("messagio dal client errato ,messageFromClient: " + messageFromClient);
                    }

                }
                default ->
                        gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen); //sto passando una stringa e un player
            }
        }
    }
    private synchronized void waitingForClients() throws InterruptedException {
        System.out.println("In attesa di altri giocatori");
        while (playersList.size() < gameController.getSize()) {
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
    private void assignInitialCard() throws IOException {
        InitialCard initialCard= game.getInitialCardDeck().firstCardInitialGame();
        sendMessageToClient("la tua carta iniziale è questa " +initialCard.toString() );
        initialCard.toString();
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(size);
        game.placeInitialCard(threadPlayer.getBoard(),initialCard);
        System.out.println("Carta iniziale piazzata correttamente");
        threadPlayer.getBoard().printBoard();
    }


    private void endTurn(Player currentPlayer, TurnController turnController) {
        if(currentPlayer != turnController.getCurrentPlayer()){
            throw new turnPlayerErrorException("il giocatore attuale è sfasato");
        }
        turnController.nextTurn();
        Player nextPlayer = turnController.getCurrentPlayer();
        setCurrentPlayer(nextPlayer);
    }
    private void setCurrentPlayer(Player currentPlayerName) {
        currentPlayer = currentPlayerName;
        System.out.println("Current player: " + currentPlayerName);
        sendMessageToAllClients(currentPlayer.getNickName());
    }


    public void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }
    public void sendMessageToClient(String message) {
        out.println(message);
    }


}


