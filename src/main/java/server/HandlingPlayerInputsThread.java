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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

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
    private static int whichplayerAreYou=0;
    private static Player winningPlayer=new Player(null,0, Dot.BLACK, null);
    private final CountDownLatch sizeLatch = new CountDownLatch(1);

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients, ServerLobby lobby, Game game) throws IOException { //Costructor
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
                //loadPlayerDataFromDisk();
                whichplayerAreYou++;
                String clientSaysHello = stdIn.readLine();
                System.out.println("Il client ha detto " + clientSaysHello);    //Client says hello
                threadPlayer = loginEachClient();                               //EveryClient has to log in, we save his name information inside threadPLayer
                handlingTurns(playersList);                                     //Handling turns, first client will be the first player inside the game
                addingPlayersToTheGame();                                       //Adding players to the current game
                synchronized (this) {                                           //First thread that accesses this function will assign all the cards to the players
                    if (!checkGameInizialization) {
                        initializeCards();
                    }
                }
                assigningSecretCard();                                            //Each thread will assign the secret card to the player
                assignInitialCard();                                              //Each client places his first card

                for (Player player : playersList) {
                    game.updateSingleClientView(player);                          //Updating each player ClientView
                    System.out.println(player.getClientView());
                }
                System.out.println(game.getObjectiveDeck().remainingCards());       //Debugging to check if all cards are given correctly
                sendMessageToClient(currentPlayer.getNickName()); //Mnadato a tutti i client il current player
                boolean hasClientQuit= false;

                //SETTING POINTS FOR ENDGAME DEBUGGING
                for(Player player: playersList)
                {
                    player.setPlayerScore(15);
                }
                while (!hasClientQuit){
                    startGame();
                    System.out.println("Client changed, now " + currentPlayer +" is playing");
                    hasClientQuit=true;
                }
                clients.remove(this);
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.err.println("Errore durante la chiusura del socket del client: " + ex.getMessage());
                }
                System.out.println("Connection closed with client");

                if(gameController.getSize()==0)
                {
                    System.out.println("All players disconnected, thank you for playing Codex!");
                    stdIn.close();
                    out.close();
                    clientSocket.close();
                }

            } catch (IOException e) {
                System.out.println("Connection lost with client number "+ whichplayerAreYou );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void startGame() throws IOException, InterruptedException {
        String messageFromClient;
        boolean endturnphase=false;
        while (!endturnphase) {
            if(currentPlayer.getPlayerScore()>=20 && !currentPlayer.isHasThePlayerGot20Points())
            {
                currentPlayer.setHasThePlayerGot20Points(true);
                winningPlayer=currentPlayer;
                GameController.setWinningPlayer(currentPlayer);
                runCommand("status", threadPlayer);
                sendMessageToAllClients("All players have one last turn and then the game will end");
                runCommand("endTurn",threadPlayer);
            }
            if(Objects.equals(currentPlayer.getNickName(), winningPlayer.getNickName()))
            {
                System.out.println("END OF GAME!");
                runCommand("endgame",threadPlayer);
            }
            System.out.println("I'm waiting current player" + currentPlayer.getNickName() + " request");
            messageFromClient = stdIn.readLine();
            System.out.println("Client typed: " + messageFromClient);
            runCommand(messageFromClient, threadPlayer); //->run

            if(messageFromClient.equals("quit")){
                gameController.setSize(gameController.getSize()-1);
                messageFromClient = stdIn.readLine(); //endturn
                runCommand(messageFromClient, threadPlayer); //->run
                endturnphase=true;
            }

        }
    }






    //PRIVATE METHODS INSIDE HANDLINGPLAYERINPUTS

    private synchronized Player loginEachClient() throws IOException, InterruptedException {
        Player player = null;
            try {
                sendMessageToClient("Hello!! You have to log in, please type your username");
                String request = stdIn.readLine();
                System.out.println("Client name is " + request);
                sendMessageToClient("Login successfully done!");
                Board board = new Board(50, 50);
                Dot dot= chooseClientDotColor();
                player = new Player(request, 0, dot, board);
                this.userName=request;
                synchronized (this){
                player.setIndex(index);
                index++;
                }
                playersList.add(player);
                System.out.println("Players in game "+ playersList);
                System.out.println(player);
                gameController = lobby.login(request, out);
                System.out.println(gameController);
                System.out.println("Current numb of players: " + gameController.getCurrentNumsOfPlayers());
                int size= setGameSize();
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
        ObjectiveCard firstCard= game.getObjectiveDeck().drawObjectiveCard();
        ObjectiveCard secondCard= game.getObjectiveDeck().drawObjectiveCard();
        secretCards.add(firstCard);
        secretCards.add(secondCard);
        int firstid= firstCard.getId();
        int secondID= secondCard.getId();
        System.out.println(firstid); //debugging
        System.out.println(secondID);

        sendMessageToClient(String.valueOf(firstCard));
        sendMessageToClient(String.valueOf(secondCard));
        sendMessageToClient(String.valueOf(firstid)); //sending the correct card id to the client
        sendMessageToClient(String.valueOf(secondID));
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        if(size==1){
            System.out.println(userName +"chose card number: "+ size);
            threadPlayer.setSecretChosenCard(secretCards.get(size-1));
            System.out.println(threadPlayer.toString());
        }
        else {
            System.out.println(userName +"chose card number: "+ size);
            threadPlayer.setSecretChosenCard(secretCards.get(size-1));
            System.out.println(threadPlayer.toString());
        }

    }
    private synchronized int setGameSize() throws NoSuchElementException {
        String message;
        int size=0;
        if (!gameController.isSizeSet()) {          //If controller number of players has not been decided
            try {
                sendMessageToClient("At the moment there is: ");
                sendMessageToClient("1");
                sendMessageToClient(" player. Choose how many players you want to play with-> players have to be from 2 to 4.");
                message= stdIn.readLine();
                size = Integer.parseInt(message);
                System.out.println("Numbers of Player will be " +size);
                sendMessageToClient("Players number correctly chosen ");
                gameController.choosePlayerNumber(size);
                gameController.setSizeSet(true);
            } catch (NumberFormatException ex) {
                sendMessageToClient("Game's number of players must be an integer.");
            } catch (Exception ex) {
                sendMessageToClient("Error");
            } catch (ParametersNotValidException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Player doesn't need to choose game number of players");
            sendMessageToClient("There's already someone online! You will be ");
            sendMessageToClient(String.valueOf(gameController.getSize()));
            sendMessageToClient(" players");
        }
        return size;
    }
    private synchronized Dot chooseClientDotColor() throws IOException {
        String message;
        Dot dot;
        do{
            sendMessageToClient("Choose the color of your dot between: " + game.getDots());
            message = stdIn.readLine();
            if (!game.isInDots(message)) {
                sendMessageToClient("Chosen color not available!");
            }
            else sendMessageToClient("Color chosen correctly:");
        }while(!game.isInDots(message));
        dot = Dot.valueOf(message);
        System.out.println("Client color chosen is " + message);
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
                    //scelta carta dal tuo mazzo
                    int cardChosenFromHisDeck;
                    boolean turnedCardAlredy = false;
                    StringBuilder forClientView = new StringBuilder();
                    do {
                        cardChosenFromHisDeck = Integer.parseInt(stdIn.readLine());
                        System.out.println("Player chose card number " + cardChosenFromHisDeck);
                        if(player.checkingTheChosenCardForGoldPurpose(cardChosenFromHisDeck)){
                            sendMessageToAllClients("puoi procedere");
                        }
                        else {
                            sendMessageToAllClients("Gold Card not placeable");
                            GoldCard cartGold = (GoldCard) player.chooseCard(cardChosenFromHisDeck);
                            sendMessageToAllClients(cartGold.getRequirementsForPlacing().toString());
                            gameController.readCommand("showYourSpecificSeed", player, 0, 0, null);
                            messageFromClient = stdIn.readLine();
                            if(messageFromClient.equals("2")){
                                System.out.println("il player vuole girare la carta");
                                gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                                turnedCardAlredy = true;
                            }
                        }

                    }while (!player.checkingTheChosenCardForGoldPurpose(cardChosenFromHisDeck) && !turnedCardAlredy);
                    //aggiorno la client view
                    Card chosenCard = player.chooseCard(cardChosenFromHisDeck);
                    if (chosenCard instanceof GoldCard) {
                        forClientView.append("Gold Card\n");
                    }else forClientView.append("Resource Card\n");

                    //scelta se girare la carta
                    if(!turnedCardAlredy) {
                        messageFromClient = stdIn.readLine();
                        if (messageFromClient.equals("1")) {
                            System.out.println("il player vuole girare la carta");
                            gameController.readCommand("TurnCard", player, cardChosenFromHisDeck, 0, null);
                            forClientView.append("(back)");
                        }else forClientView.append("(front)");
                    }else forClientView.append("(back)");

                    //scelta carta della board su cui piazzare
                    String CardOnTheBoardChosen = stdIn.readLine();
                    int boardCardChosen = Integer.parseInt(CardOnTheBoardChosen);
                    System.out.println("Il player ha deciso di giocare la proria carta sulla carta numero " + boardCardChosen);
                    gameController.readCommand("playCard", player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                    cornerChosen = stdIn.readLine();
                    //
                    System.out.println(cornerChosen);
                    gameController.readCommand("playCard", player, cardChosenFromHisDeck, boardCardChosen, cornerChosen);
                    forClientView.append("\n("+ chosenCard.getNode().getCoordY()+ " "+ chosenCard.getNode().getCoordX()+")");
                    sendMessageToAllClients(String.valueOf(forClientView));
                    messageFromClient = stdIn.readLine();
                    runCommand(messageFromClient, player);
                }
                //else if(messageFromClient.equals("drawCardFromWell")){
                case "drawCard" -> {
                    //intro //MOMO NON RIMETTERE QUESTO METODO PLS O CRASHA TUTTO, LASCIA CHE IL WELL NON SI VEDA AL MOMENTO
                    //messageFromClient = stdIn.readLine();
                    //gameController.readCommand(messageFromClient, player, 0, 0, null);// showwell
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
                        System.out.println("Client message wrong ,messageFromClient: " + messageFromClient);
                    }

                }
                default ->
                        gameController.readCommand(messageFromClient, player, 0, 0, cornerChosen); //sto passando una stringa e un player
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
        System.out.println("First player is " + currentPlayer);
        game.setCurrentPlayingPLayer(currentPlayer);

    }
    private void assignInitialCard() throws IOException {
        InitialCard initialCard= game.getInitialCardDeck().firstCardInitialGame();
        int initCardId= initialCard.getId();                                //For gui purpose
        sendMessageToClient("This is your first card " +initialCard ); //Sending the card
        sendMessageToClient(String.valueOf(initCardId)); //Sending the id (for gui purpose)
        String integerString = stdIn.readLine();
        int size = Integer.parseInt(integerString);
        System.out.println(size);
        if(size==0)
        {
            game.placeInitialCardBack(threadPlayer.getBoard(), initialCard);
            System.out.println("Initial Card correctly placed");
        }
        else if(size==1)
        {

            game.placeInitialCard(threadPlayer.getBoard(),initialCard);
            System.out.println("Initial Card correctly placed");
        }
        threadPlayer.getBoard().printBoard();

    }


    private void endTurn(Player currentPlayer, TurnController turnController) {
        if(currentPlayer != turnController.getCurrentPlayer()){
            throw new turnPlayerErrorException("Current player not correct");
        }
        turnController.nextTurn();
        Player nextPlayer = turnController.getCurrentPlayer();
        setCurrentPlayer(nextPlayer);
    }
    private void setCurrentPlayer(Player currentPlayerName) {
        currentPlayer = currentPlayerName;
        if(gameController.getSize()==0)
        {
            System.out.println("All clients quit");
            sendMessageToAllClients("All clients have quit");
        }
        else{
        System.out.println("Current player: " + currentPlayerName);
        sendMessageToAllClients(currentPlayer.getNickName());
        game.setCurrentPlayingPLayer(currentPlayer);}
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


