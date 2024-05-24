package server;

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
import java.net.SocketException;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
    private List<Player> playersList;
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
    private static int updateOrder = 0;
    private static volatile boolean isGameQuit = false;




    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException {
        this.clientSocket = socket;
        stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.playersList = playersinTheGame;
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
            firstClient = this;
            System.out.println("ciao sono il primo thread");
        } else if (secondClient==null) {
            secondClient=this;
            System.out.println("ciao sono il secondo thread");
        } else if (thirdClient==null) {
            thirdClient=this;
            System.out.println("Hi i'm the third thread");
        } else if (fourthClient==null) {
            fourthClient=this;
            System.out.println("HI i'm the fourth thread");
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
                sendMessageToClient(currentPlayer.getNickName());
                if (this == firstClient) {
                    sendMessageToClient("You are the first client");
                }
                notifyClientSetupComplete();
                waitForAllClientsToSetup();
                boolean hasClientQuit = false;
                for (Player player : playersList) {
                    player.setPlayerScore(10);
                }
                while (!hasClientQuit && !isGameQuit) {
                    startGame();
                    System.out.println("Client changed, now " + currentPlayer + " is playing");
                    hasClientQuit = true;
                }
                handleClientDisconnection();
                stdIn.close();
                out.close();
                clientSocket.close();
                if(gameController.getSize()==0)
                {
                    System.out.println("All clients quit, thank you for playing Codex !");
                }
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error while closing client's socket " + ex.getMessage());
                }
                System.out.println("Connection closed with client");
                checkIfTheGameControllerIsEmpty();
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout: client crashed.");
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                clients.remove(this);
            } catch (IOException e) {
                System.out.println("Connection lost with client");
                try {
                    checkIfTheGameControllerIsEmpty();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startGame() throws IOException, InterruptedException {
        String messageFromClient;
        boolean endturnphase = false;
        while (!endturnphase) {
            if(currentPlayer.isHasThePlayerGot20Points() ){//stampa esiti
                System.out.println("------------\nEND OF GAME!\n------------");
                sendMessageToAllClients("END OF GAME!");
                sendMessageToAllClients("ci siamo!!");
                //runCommand("endgame", threadPlayer);
                stdIn.readLine();//quit
                sendMessageToAllClients("ALL_CLIENTS_QUIT");
                isGameQuit = true;
                break;
            }
            System.out.println("I'm waiting current player" + currentPlayer.getNickName() + " request");
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

    private synchronized Player loginEachClient() throws IOException, InterruptedException {
        Player player = null;
        try {
            sendMessageToClient("Hello!! You have to log in, please type your username");
            String request = stdIn.readLine();

            // Carica il giocatore se esiste
            player = game.loadPlayer(request);
            if (player != null) {
                sendMessageToClient("Welcome back, " + request + "! Your data has been loaded.");
                playersList.add(player);
                // Imposta il giocatore come threadPlayer per questo thread
                threadPlayer = player;
                return player;
            }

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
            playersList.add(player);
            System.out.println("Players in game " + playersList);
            System.out.println(player);
            gameController = lobby.login(request, out);
            System.out.println("Current numb of players: " + gameController.getCurrentNumsOfPlayers());
            int size = setGameSize();
            System.out.println(size);
            sendMessageToClient("You have to wait until all clients are connected!");
            gameController.waitingForPLayers();

        } catch (IOException | UsernameAlreadyExistsException | UnknownPlayerNumberException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(gameController);
        return player;
    }

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

    private synchronized int setGameSize() throws NoSuchElementException {
        String message;
        int size = 0;
        if (this==firstClient) {
            try {
                sendMessageToClient("Choose the number of players(2-4): ");
                message = stdIn.readLine();
                size = Integer.parseInt(message);
                System.out.println("Numbers of Player will be " + size);
                sendMessageToClient("Players number correctly chosen ");
                gameController.choosePlayerNumber(size);
                gameController.setSizeSet(true);
                setupLatch = new CountDownLatch(size); // Inizializza il CountDownLatch con il numero di giocatori scelto
            } catch (NumberFormatException ex) {
                sendMessageToClient("Game's number of players must be an integer.");
            } catch (Exception ex) {
                sendMessageToClient("Error");
            } catch (ParametersNotValidException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Player doesn't need to choose game number of players");
            sendMessageToClient("There's already someone online!Please wait");

        }
        return size;
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
                    break;
                }
                case"howManyPlayers"->{
                    int size= gameController.getLogginPlayers();
                    sendMessageToAllClients(String.valueOf(size));
                    break;
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
                    game.nextTurn(); // Avanza al turno successivo
                    String nextPlayer = game.getCurrentPlayer();
                    System.out.println("Next Player is: " + nextPlayer);
                    sendMessageToAllClients(nextPlayer);
                    break;
                }
                case "endTurn" -> {
                    endTurn(player, turnController);
                    gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen);
                }
                case "playCard" -> {
                    int cardChosenFromHisDeck;
                    boolean turnedCardAlredy = false;
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
                                System.out.println("il player vuole girare la carta");
                                gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                                turnedCardAlredy = true;
                            }
                        }
                    } while (!player.checkingTheChosenCardForGoldPurpose(cardChosenFromHisDeck) && !turnedCardAlredy);
                    Card chosenCard = player.chooseCard(cardChosenFromHisDeck);
                    if (chosenCard instanceof GoldCard) {
                        forClientView.append("Gold Card\n");
                    } else forClientView.append("Resource Card\n");
                    if (!turnedCardAlredy) {
                        messageFromClient = stdIn.readLine();
                        if (messageFromClient.equals("1")) {
                            System.out.println("PLayer wants to turn his card!");
                            gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                            forClientView.append("(back)");
                        } else forClientView.append("(front)");
                    } else forClientView.append("(back)");

                    //ciclo scelta carta sulla board
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
                    messageFromClient = stdIn.readLine();
                    runCommand(messageFromClient, player);//showWell
                    messageFromClient = stdIn.readLine();
                    runCommand(messageFromClient, player);//drawCard
                    messageFromClient = stdIn.readLine();
                    runCommand(messageFromClient, player);//status

                    //endgame
                    if (currentPlayer.getPlayerScore() >= 20 && !game.isEndGame()) {
                        game.setEndGame(true);
                        currentPlayer.setHasThePlayerGot20Points(true);
                        winningPlayer = currentPlayer;
                        GameController.setWinningPlayer(currentPlayer);
                        System.out.println(userName + "smashed 20 points!!");
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
        System.out.println(game.CardsIndeck());
        System.out.println(game.GoldsIndeck());
    }

    private void addingPlayersToTheGame() {
        for (Player playerInGame : playersList) {
            game.addPlayer(playerInGame);
        }
    }

    private void handlingTurns(List<Player> playerList) {
        if (threadPlayer == null) {
            return;
        }
        turnController = new TurnController(playerList);
        System.out.println(turnController.getPlayers());
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
            System.out.println("All players disconnected, thank you for playing Codex!");
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
    public synchronized void incrementUpdateOrder() {
        updateOrder++;
        notifyAll();
    }

    public synchronized int getUpdateOrder() {
        return updateOrder;
    }

    public synchronized void waitForTurn(int clientOrder) throws InterruptedException {
        while (updateOrder < clientOrder) {
            wait();
        }
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
        clients.remove(this);
        if (threadPlayer != null) {
            playersList.remove(threadPlayer);
            gameController.removePlayer(threadPlayer); // Chiama il metodo removePlayer del GameController

        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("Error while closing client's socket " + ex.getMessage());
        }
        System.out.println("Connection closed with client");
        checkIfTheGameControllerIsEmpty();
    }
    public Player getThreadPlayer() {
        return threadPlayer;
    }
    public synchronized void sendQuitMessageToAllClients() {
        for (HandlingPlayerInputsThread client : clients) {
            client.sendMessageToClient("ALL_CLIENTS_QUIT");
        }
        isGameQuit = true;
    }

}
