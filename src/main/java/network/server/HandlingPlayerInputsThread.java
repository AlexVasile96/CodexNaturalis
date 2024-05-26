package network.server;

import com.google.gson.JsonObject;
import controller.GameController;
import controller.TurnController;
import exceptions.ParametersNotValidException;
import exceptions.UnknownPlayerNumberException;
import exceptions.UsernameAlreadyExistsException;
import exceptions.turnPlayerErrorException;
import model.card.Card;
import model.card.GoldCard;
import model.card.InitialCard;
import model.card.ObjectiveCard;
import model.game.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;

public class HandlingPlayerInputsThread implements Runnable {
    private static GameController gameController;
    private static TurnController turnController;
    private Player threadPlayer = null;
    private final Game game;
    private static Player currentPlayer;
    public BufferedReader stdIn;
    public PrintWriter out;
    private final List<HandlingPlayerInputsThread> clients;
    private static boolean checkGameInizialization;
    private final ServerLobby lobby;
    private static List<Player> playersList;
    private Socket clientSocket;
    private String userName;
    private static int index = 0;
    private static int whichplayerAreYou = 0;
    private static Player winningPlayer = new Player(null, 0, Dot.BLACK, null);
    private static HandlingPlayerInputsThread firstClient = null;
    private static HandlingPlayerInputsThread secondClient = null;
    private static HandlingPlayerInputsThread thirdClient = null;
    private static HandlingPlayerInputsThread fourthClient = null;
    private static CountDownLatch setupLatch;
    private static volatile boolean isGameQuit = false;
    boolean allPlayersLoaded = false;
    private boolean clientPersisted=false;
    private static int numPlayers = -1; // Numero di giocatori scelto dal primo client
    private static final Object lock = new Object(); // Oggetto di sincronizzazione

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException {
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        playersList = playersinTheGame;
        this.clients = clients;
        synchronized (HandlingPlayerInputsThread.class) {
            if (gameController == null) {
                gameController = new GameController();
            }
        }
        turnController = null;
        this.lobby = lobby;
        this.userName = null;
        this.game = game;
        checkGameInizialization = false;
        if (firstClient == null) {
            System.out.println("Sono il primo thread");
            firstClient = this;
        } else if (secondClient==null) {
            secondClient=this;
        } else if (thirdClient==null) {
            thirdClient=this;
        } else if (fourthClient==null) {
            fourthClient=this;
        }
        synchronized (HandlingPlayerInputsThread.class) {
            if (setupLatch == null) {
                setupLatch = new CountDownLatch(1);
            }
        }
    }


    @Override
    public void run() {
        synchronized (this) {
            try {
                whichplayerAreYou++;
                String clientSaysHello = stdIn.readLine();
                System.out.println("Client says " + clientSaysHello);
                threadPlayer = loginEachClient();

                if(!clientPersisted)
                {
                    noPersistenceLogin();
                    for (Player player : playersList) {
                        player.setPlayerScore(10);
                    }
                }
                else{

                currentPlayer= game.loadCurrentPlayingPlayerFromJson();
                System.out.println("Current player after loading data is " + currentPlayer.getNickName());
                game.loadGameStatusFromJson();
                sendMessageToAllClients(currentPlayer.getNickName());
                sendMessageToAllClients("endturn");
                }
                boolean hasClientQuit = false;
                while (!hasClientQuit && !isGameQuit) {
                    startGame();
                    hasClientQuit = true;
                }
                handleClientDisconnection();

            } catch (SocketTimeoutException e ) {
                System.out.println("Timeout: client crashed.");
                try {
                    handleClientDisconnection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (IOException e) {
                System.out.println("Connection lost with client");
                try {
                    handleClientDisconnection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                closeResources();
                try {
                    checkIfTheGameControllerIsEmpty();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Method that handles all clients input and ste the endgame when one player gets 20 points
     */

        private void startGame() throws IOException, InterruptedException {
        String messageFromClient;
        boolean endturnphase = false;
        while (!endturnphase) {
            if(currentPlayer.isHasThePlayerGot20Points() ){//stampa esiti
                System.out.println("------------\nEND OF GAME!\n------------");
                sendMessageToAllClients("END OF GAME!");
                runCommand("endgame",currentPlayer);
                stdIn.readLine();//quit
                sendMessageToAllClients("ALL_CLIENTS_QUIT");
                isGameQuit = true;
                break;
            }
            System.out.println("I'm waiting current player " + currentPlayer.getNickName() + " request");
            messageFromClient = stdIn.readLine();
            if(messageFromClient==null)
            {
                return;
            }
            System.out.println("Client typed: " + messageFromClient);
            if(messageFromClient.equals("quit"))
            {
                sendMessageToAllClients("ALL_CLIENTS_QUIT");
                isGameQuit = true;
                gameController.setSize(0);
                break;
            }
            else{
            runCommand(messageFromClient, threadPlayer);}

        }
    }

    /**
     * Method that handles the clients login in persisted and not persisted mode
     * @return the player correctly logged
     */

    private synchronized Player loginEachClient() throws IOException, InterruptedException {
        Player player = null;
        try {
            sendMessageToClient("Hello!! You have to log in, please type your username");
            String request = stdIn.readLine();

            player = game.loadPlayer(request); //If the player exists, I load him
            if (player != null) {
                clientPersisted=true;
                sendMessageToClient("Welcome back, " + request + "! Your data has been loaded.");
                playersList.add(player);
                threadPlayer = player;
                List<String> expectedPlayerNicknames =game.loadPlayerNicknames(); //Checking if all the players had been added correctly
                allPlayersLoaded = game.areAllPlayersLoaded(expectedPlayerNicknames);
                gameController.setSize(gameController.loadGameSizeFromJson());
                GameController.setHowManyPlayersDoIHave(GameController.getHowManyPlayersDoIHave()+1);
                gameController = lobby.login(request, out);
                JsonObject clientViewJson = player.getClientView().toJsonObject();
                out.println(clientViewJson.toString()); //Sending the clientview to the specific client
                game.addPlayer(player); // Adding the player to playerlist
                handlingTurns(playersList); //Handling new turns
                if(GameController.getHowManyPlayersDoIHave()==gameController.getSize())
                {
                    synchronized (this)
                    {
                        notifyAll();
                        sendMessageToAllClients("All players connected. Resuming the game...");
                        return player;
                    }
                }
                else {
                    sendMessageToClient("You have to wait until all clients are connected!");
                    synchronized (this) {
                        System.out.println("Manca un player");
                        wait();
                    }
                    return player;
                }
            }

            //Correct handling if one player tries to set a name that had already been chosen

            while (isUsernameTaken(request)) {
                sendMessageToClient("Username already taken. Please choose another username:");
                System.out.println("Username already taken. Please choose another username:");
                System.out.println("Waiting for new Username");
                request = stdIn.readLine();
                System.out.println("Received new username: " + request);
            }
            System.out.println("Username correct");
            System.out.println("Client name is " + request);
            sendMessageToClient("Login successfully done!");
            Board board = new Board(50, 50);
            Dot dot = chooseClientDotColor();
            player = new Player(request, 0, dot, board);
            this.userName = request;
            synchronized (this) {
                player.setIndex(index);
                index++;
            }
            gameController = lobby.login(request, out);
            System.out.println("Current numb of players: " + gameController.getCurrentNumsOfPlayers());
            int size = setGameSize(player);
            System.out.println(size);
        } catch (IOException | UsernameAlreadyExistsException | UnknownPlayerNumberException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(gameController);
        return player;
    }

    /**
     * Method to assign the initial card to each player
     */

    private synchronized void assigningSecretCard() throws IOException {
        List<ObjectiveCard> secretCards = new ArrayList<>();
        ObjectiveCard firstCard = game.getObjectiveDeck().drawObjectiveCard();
        ObjectiveCard secondCard = game.getObjectiveDeck().drawObjectiveCard();
        secretCards.add(firstCard);
        secretCards.add(secondCard);
        int firstid = firstCard.getId();
        int secondID = secondCard.getId();
        System.out.println(firstid);
        System.out.println(secondID);
        sendMessageToClient(String.valueOf(firstCard));
        sendMessageToClient(String.valueOf(secondCard));
        sendMessageToClient(String.valueOf(firstid));
        sendMessageToClient(String.valueOf(secondID));
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        if (size == 1) {
            System.out.println(userName + " chose card number: " + size);
            threadPlayer.setSecretChosenCard(secretCards.get(size - 1));
            System.out.println(threadPlayer.toString());
        } else {
            System.out.println(userName + " chose card number: " + size);
            threadPlayer.setSecretChosenCard(secretCards.get(size - 1));
            System.out.println(threadPlayer.toString());
        }
    }

    /**
     * method to set the correct game size. Only the first client can set the number of players, other clients will wait until he has finished
     * @param player-> i'll add him to the playerlist if the login goes well
     */

    private synchronized int setGameSize(Player player) throws NoSuchElementException, InterruptedException {
        synchronized (lock) {
            if (numPlayers == -1 && this == firstClient) {
                try {
                    sendMessageToClient("Choose the number of players(2-4): ");
                    String message = stdIn.readLine();
                    numPlayers = Integer.parseInt(message);
                    System.out.println("Number of players will be " + numPlayers);
                    sendMessageToClient("Players number correctly chosen.");
                    gameController.choosePlayerNumber(numPlayers);
                    gameController.setSizeSet(true);
                    setupLatch = new CountDownLatch(numPlayers); // Inizializza il CountDownLatch con il numero di giocatori scelto
                    currentPlayer=this.getThreadPlayer();
                    playersList.add(player);
                    lock.notifyAll(); // unlocking all the clients waiting
                } catch (NumberFormatException ex) {
                    sendMessageToClient("Game's number of players must be an integer.");
                } catch (Exception ex) {
                    sendMessageToClient("Error");
                } catch (ParametersNotValidException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Player doesn't need to choose game number of players.");
                sendMessageToClient("There's already someone online! Please wait");
                while (numPlayers == -1) {
                    try {
                        lock.wait(); // Waiting until gamesiz is set

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        sendMessageToClient("Number of players is: " + numPlayers);
        sendMessageToClient("You have to wait until all clients are connected!");
        gameController.waitingForPLayers();
        return numPlayers;
    }
    private synchronized Dot chooseClientDotColor() throws IOException {
        String message;
        Dot dot;
        do {
            sendMessageToClient("Choose the color of your dot between: " + game.getDots());
            message = stdIn.readLine();
            if (!game.isInDots(message)) {
                sendMessageToClient("Chosen color not available!");
            } else sendMessageToClient("Color chosen correctly:");
        } while (!game.isInDots(message));
        dot = Dot.valueOf(message);
        System.out.println("Client color chosen is " + message);
        game.removeDot(message);
        return dot;
    }

    private void runCommand(String messageFromClient, Player player) throws NoSuchElementException, IOException {
        if (gameController != null) {
            String cornerChosen = null;
            System.out.println("Received command: " + messageFromClient);
            switch (messageFromClient) {
                case"updateLoggedPlayers"->{
                    gameController.setLogginPlayers(gameController.getLogginPlayers()+1);
                    sendMessageToAllClients("Login Players updated correctly");
                }
                case"howManyPlayers"->{
                    int size= gameController.getLogginPlayers();
                    sendMessageToAllClients(String.valueOf(size));
                }
                case"SETUPFINISHED"->{
                    sendMessageToAllClients("SETUPFINISHED");
                    if (!GameController.isIsTheFirstPlayer()) {
                        game.setCurrentPlayer(player.getClientView().getUserName());
                        GameController.setIsTheFirstPlayer(true);
                    }
                    String currentPlayerName = game.getCurrentPlayer();
                    System.out.println("Current Player Name set to: " + currentPlayerName);
                    sendMessageToAllClients(currentPlayerName);
                    game.nextTurn(); // Setting the next turn
                    String nextPlayer = game.getCurrentPlayer();
                    System.out.println("Next Player is: " + nextPlayer);
                    sendMessageToAllClients(nextPlayer);
                }
                case "endTurn" -> {
                    endTurn(player, turnController);
                    gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen);
                }
                case "playCard" -> {
                    int cardChosenFromHisDeck;
                    boolean turnedCardAlready = false;
                    StringBuilder forClientView = new StringBuilder();
                    do {
                        cardChosenFromHisDeck = Integer.parseInt(stdIn.readLine());
                        System.out.println("Player chose card number " + cardChosenFromHisDeck);
                        if (player.checkingTheChosenCardForGoldPurpose(cardChosenFromHisDeck)) {
                            sendMessageToAllClients("puoi procedere");
                        } else {
                            sendMessageToAllClients("Gold Card not placeable");
                            GoldCard cartGold = (GoldCard) player.chooseCard(cardChosenFromHisDeck);
                            sendMessageToAllClients(cartGold.getRequirementsForPlacing().toString());
                            gameController.readCommand("showYourSpecificSeed", player, 0, 0, null);
                            messageFromClient = stdIn.readLine();
                            if (messageFromClient.equals("2")) {
                                System.out.println("Player wants to turn his card");
                                gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                                turnedCardAlready = true;
                            }
                        }
                    } while (!player.checkingTheChosenCardForGoldPurpose(cardChosenFromHisDeck) && !turnedCardAlready);
                    Card chosenCard = player.chooseCard(cardChosenFromHisDeck);
                    if (chosenCard instanceof GoldCard) {
                        forClientView.append("Gold Card\n");
                    } else forClientView.append("Resource Card\n");
                    if (!turnedCardAlready) {
                        messageFromClient = stdIn.readLine();
                        if (messageFromClient.equals("1")) {
                            System.out.println("PLayer wants to turn his card!");
                            gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                            forClientView.append("(back)");
                        } else forClientView.append("(front)");
                    } else forClientView.append("(back)");

                    //For cicle to chose the correct card
                    int boardCardChosen;
                    boolean rightCard;
                    do {
                        boardCardChosen = Integer.parseInt(stdIn.readLine());
                        System.out.println("Il player ha deciso di giocare la propria carta sulla carta numero " + boardCardChosen);
                        gameController.readCommand("playCard", player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                        cornerChosen = stdIn.readLine();
                        if(cornerChosen.equals("clean")) {
                            rightCard=false;
                            System.out.println("The card Has NO free corners");
                        } else rightCard= true;
                        System.out.println(cornerChosen);
                        gameController.readCommand("playCard", player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                    }while (!rightCard);
                    forClientView.append("\n(" + chosenCard.getNode().getCoordY() + " " + chosenCard.getNode().getCoordX() + ")");
                    sendMessageToAllClients(String.valueOf(forClientView));
                    String resultForClientViewInGame= stdIn.readLine();
                    player.getClientView().addCardOnTheBoard(resultForClientViewInGame); // Aggiornamento della ClientView
                    player.getClientView().update(player);
                    messageFromClient = stdIn.readLine();
                    System.out.println("message from client->" + messageFromClient);//showWell
                    runCommand(messageFromClient, player);

                    messageFromClient = stdIn.readLine();
                    System.out.println("message from client->" + messageFromClient);//drawCard;
                    runCommand(messageFromClient, player);
                    messageFromClient = stdIn.readLine();
                    System.out.println("message from client->" + messageFromClient);//stTUS
                    runCommand(messageFromClient, player);
                    //endgame
                    if (currentPlayer.getPlayerScore() >= 20 && !game.isEndGame()) {
                        game.setEndGame(true);
                        currentPlayer.setHasThePlayerGot20Points(true);
                        winningPlayer = currentPlayer;
                        GameController.setWinningPlayer(currentPlayer);
                        System.out.println(userName + " smashed 20 points!!");
                        sendMessageToAllClients("You smashed 20 points!! now everybody got one last turn");
                        sendMessageToAllClients(userName);
                        messageFromClient = stdIn.readLine();
                        runCommand(messageFromClient, threadPlayer);//endturn
                    }
                }
                case "drawCard" -> {
                    messageFromClient = stdIn.readLine();
                    if (messageFromClient.equals("deck")) {
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                    } else if (messageFromClient.equals("well")) {
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                        int index = Integer.parseInt(stdIn.readLine());
                        gameController.readCommand("drawCardFromWell", player, index, 0, null);
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                        messageFromClient = stdIn.readLine();
                        gameController.readCommand(messageFromClient, player, 0, 0, null);
                    } else {
                        System.out.println("Client message wrong ,messageFromClient: " + messageFromClient);
                    }
                }
                default -> gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen);
            }
        }
    }

    private void initializeCards() {
        game.assignResourcesAndGoldCardsToPlayers();
        checkGameInizialization = true;
        System.out.println(game.cardsInDeck());
        System.out.println(game.goldsInDeck());
    }

    private void addingPlayersToTheGame() throws InterruptedException {
        for (Player playerInGame : playersList) {
            game.addPlayer(playerInGame);
            gameController.addPlayer(threadPlayer.getNickName(),out);
        }
    }

    private synchronized void handlingTurns(List<Player> playerList) {
        if (threadPlayer == null) {
            return;
        }
        turnController = new TurnController(playerList);
        currentPlayer = turnController.getCurrentPlayer();
        System.out.println("First player is " + currentPlayer);
        game.setCurrentPlayingPLayer(currentPlayer);
    }

    private void assignInitialCard() throws IOException, InterruptedException {
        InitialCard initialCard = game.getInitialCardDeck().firstCardInitialGame();
        int initCardId = initialCard.getId();
        sendMessageToClient("This is your first card " + initialCard);
        sendMessageToClient(String.valueOf(initCardId));
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(size);
        if (size == 0) {
            game.placeInitialCardBack(threadPlayer.getBoard(), initialCard);
            System.out.println("Initial Card correctly placed");
        } else if (size == 1) {
            game.placeInitialCard(threadPlayer.getBoard(), initialCard);
            System.out.println("Initial Card correctly placed");
        }
        gameController.setPlayerChoseinitialcard(gameController.getPlayerChoseinitialcard() + 1);
        gameController.waitingForPLayersAfterInitialcard();
    }

    private void endTurn(Player currentPlayer, TurnController turnController) {
        if (currentPlayer != turnController.getCurrentPlayer()) {
            throw new turnPlayerErrorException("Current player not correct");
        }
        turnController.nextTurn();
        Player nextPlayer = turnController.getCurrentPlayer();
        setCurrentPlayer(nextPlayer);
    }

    private void setCurrentPlayer(Player currentPlayerName) {
        currentPlayer = currentPlayerName;
        if (gameController.getSize() == 0) {
            System.out.println("All clients quit");
            sendMessageToAllClients("All clients have quit");
        } else {
            System.out.println("Current player: " + currentPlayerName);
            sendMessageToAllClients(currentPlayer.getNickName());
            game.setCurrentPlayingPLayer(currentPlayer);
        }
    }


    public void sendMessageToAllClients(String message) {
        for (HandlingPlayerInputsThread client : clients) {
            client.out.println(message);
        }
    }

    public void sendMessageToClient(String message) {
        out.println(message);
    }

    public void checkIfTheGameControllerIsEmpty() throws IOException {
        if (gameController.getSize() == 0) {
            //System.out.println("All players disconnected, thank you for playing Codex!");
            stdIn.close();
            out.close();
            clientSocket.close();
        }
    }

    private void waitForAllClientsToSetup() {
        try {
            setupLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void notifyClientSetupComplete() {
        setupLatch.countDown();
    }

    private boolean isUsernameTaken(String username) {
        for (Player player : playersList) {
            if (player.getNickName().equals(username)) {
                return true;
            }
        }
        return false;
    }
    private void handleClientDisconnection() throws IOException {
        System.out.println("Connection closed with client " + threadPlayer.getNickName());
        System.out.println("Thank you " + threadPlayer.getNickName() + " for playing Codex!");
        clients.remove(this);
        if (threadPlayer != null) {
            playersList.remove(threadPlayer);
            gameController.removePlayer(threadPlayer);
        }
        closeResources();

        checkIfTheGameControllerIsEmpty();
    }
    public Player getThreadPlayer() {
        return threadPlayer;
    }

    private void closeResources() {
        try {
            if (stdIn != null) {
                stdIn.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing resources: " + e.getMessage());
        }
    }

    private void noPersistenceLogin() throws IOException, InterruptedException {
        if(this!=firstClient)
        {
            playersList.add(threadPlayer);
        }
        handlingTurns(playersList);
        addingPlayersToTheGame();


        synchronized (this) {
            if (!checkGameInizialization) {
                initializeCards();
            }
        }

        sendMessageToClient("All clients connected");
        assigningSecretCard();
        assignInitialCard();
        for (Player player : playersList) {
            game.updateSingleClientView(player);
        }


        System.out.println("player list is :" + playersList);
        System.out.println("Current player in game is " + game.getCurrentPlayingPLayer());
        sendMessageToClient(currentPlayer.getNickName());
        if (this == firstClient) {
            sendMessageToClient("You are the first client");
        }
        notifyClientSetupComplete();
        waitForAllClientsToSetup();
    }
}
