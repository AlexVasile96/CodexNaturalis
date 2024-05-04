package server;
import exceptions.OperationCancelledException;
import model.game.*;
import view.ClientView;

import java.io.*;
import java.net.Socket;



public class ServerConnection implements Runnable {
    private static int index=0;
    private Socket socket;
    private ClientView clientView;
    private final BufferedReader in;
    private final BufferedReader stdin;
    private final PrintWriter out;
    private final Player player;
    private String currentPlayer= null;
    private boolean isConnectionClosed= false;
    private boolean isTheWhileActive=false;

    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
            this.clientView=clientView;
            this.socket = server;
            this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));    //ricevere dati dal server
            this.out= new PrintWriter(socket.getOutputStream(), true);
            this.stdin= new BufferedReader(new InputStreamReader(System.in));               //scanner, mi serve per scrivere
            this.player=new Player(null,0,null,null );
    }

@Override
    public void run() {
    String command;
    synchronized (this) {
        try {
            System.out.println("Welcome! I'm the server, please type anything to start the conversation!\n");
            while (!isTheWhileActive) {
                try {                                                       //il client scrive un messaggio
                    if (clientView.getUserName() == null) {             //If client hasn't made the login yet, he has to log first.
                        System.out.print(">");
                        command = stdin.readLine();
                        sendMessageToServer(command);
                        loginPlayer(player);                                  //Actual Login
                        assigningSecretCard();                                //Choosing the secret Card
                        takingTheInitialCard();                               //Taking the initial Card
                        //receivingAndPrintingCards();
                        currentPlayer = in.readLine();                         //who is the current player?
                        System.out.println("Server says that first player will be " + currentPlayer);
                    } else {
                        while (!isTheWhileActive) {
                            if(isConnectionClosed)
                            {
                                isTheWhileActive=true;
                            }
                            else{
                                waitUntilItsYourTurn();
                                makeYourMoves();
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                exitFromGame();
                in.close();
                out.close();
                socket.close();
                System.out.println("Connection with server has been closed, thank you for playing Codex!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

private void waitUntilItsYourTurn() throws IOException {
   //currentPlayer= getCurrentPlayer();
        while(!clientView.getUserName().equals(getCurrentPlayer()))
        {
            String waitForCall= in.readLine();
            if(waitForCall.equals(clientView.getUserName())){
                setCurrentPlayer(waitForCall);
                System.out.println(getCurrentPlayer());
                in.readLine(); //Si mangia il "fine turno"
            }
            else {System.out.println("Current Player "+ currentPlayer + "typed " +waitForCall);}
        }
}
    private void makeYourMoves() throws IOException {
        System.out.println("It's your turn!");
        System.out.println("What do you want to do?");
        String command= stdin.readLine();
        actionsInput(command);
    }



    private void actionsInput(String userInput) throws IOException { //GAME STARTED
        try {
            switch (userInput) {
                case "help"-> printHelp();
                case "status" -> printStatus();
                case "actions" -> printActions();
                case "showdeck", "0" -> showCards();
                case "playcard", "1" -> chosenHandCard();
                case "common", "2" -> visualizeCommonObjective();
                case "secret", "3" -> visualizeSecretObjective();
                case "board", "4" -> showBoard();
                case "points", "5" -> showPoints();
                case "showwell", "6" -> showWell();
                //case "drawcard", "11" -> drawCard();              //il player pesca solo se gioca una carta
                //case "drawcardfromdeck", "7"-> drawCardFromDeck();
                //case "drawcardfromwell", "8" -> drawCardFromWell();
                case "endturn", "9" -> runEndTurn();//run
                case "allboards", "10" -> showEachPlayerBoard();
                case "yourseeds", "11" -> showYourSpecificSeed();
                case "allseed", "12" -> showAllSpecificSeed();
                case "quit", "13" -> quit();
                default -> 
                    System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
                    
            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage());
        }
    }


    private synchronized void printHelp() throws IOException {
        sendMessageToServer("help");
        String serviceString=in.readLine();
        System.out.println(serviceString);
        System.out.println(
                """
                        Supported commands are:\s
                        - 'status': show the player who is currently taking their turn and the turn phase
                        - 'show': display a specific game element
                        - 'actions': display all currently allowed game actions
                        """);
    }

    private void printActions() throws IOException {
        sendMessageToServer("actions");
        String serviceString=in.readLine();
        System.out.println(serviceString);
        System.out.println(
                """
                        Supported commands:
                        - If you type-> 'showdeck / 0 ': display player's cards
                        - If you type-> 'playcard /1': select the card you want to place from your hand
                        - If you type->  'common /2': visualize the common objective cards
                        - If you type->  'secret /3': visualize your secret objective card
                        - If you type->  'board /4':print your board
                        - If you type->  'points /5': show your points
                        - If you type->  'drawResourceCardFromDeck /6': draw a card from the resource deck
                        - If you type->  'drawGoldCardFromDeck /7': draw a card from the gold deck
                        - If you type->  'drawCardFromWell /8': draw a card from the well
                        - If you type->  'endturn /9': end your turn
                        - If you type->  'showWell /9': you'll be displayed the well
                        . if you type ->  'quit /10': esci dal gioco"""
        );
    }

    private void printStatus(){
        sendMessageToServer("status");
        System.out.println("\n"+clientView.toString());
    }
    private void cleanTheSocket() {
        out.flush();
    }

    private void showCards() throws IOException {
        sendMessageToServer("showYourCardDeck");
        System.out.println("Il tuo mazzo:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingAndPrintingCards();
        System.out.println("--------------------------------------------------------------------------------------");
    }


    private void showEachPlayerBoard() throws IOException {
        sendMessageToServer("showEachPlayerBoard");
        System.out.println("Hai deciso di stampare tutte le board di tutti i players");
        String allBoards= in.readLine();
        System.out.println(allBoards);
    }
    private void showYourSpecificSeed() throws IOException {
        sendMessageToServer("showYourSpecificSeed");
        System.out.println("I tuoi seeds: ");
        String yourseeds= in.readLine();
        System.out.println(yourseeds);
    }

    private void showAllSpecificSeed(){
        sendMessageToServer("showAllSpecificSeed");
    }



    private void receivingAndPrintingCards() throws IOException {
        String firstCard = in.readLine();
        String secondCard = in.readLine();
        String thirdCard = in.readLine();
        in.readLine();//è solo lo spazio
        updatingView(firstCard, secondCard, thirdCard);
        for (String s: player.getClientView().getPlayerStringCards()){
            System.out.println(s);
        }
    }
    private void updatingView(String firstCard, String secondCard, String thirdCard){
        String[] carte = {firstCard, secondCard, thirdCard};
        if(player.getClientView().getPlayerStringCards().isEmpty()) {
            for (int i = 0; i < 3; i++){
                player.getClientView().getPlayerStringCards().add(carte[i]);
            }
            return;
        }
        boolean present = false;
        for (int i = 0; i < 3; i++){
            for (String viewCard : player.getClientView().getPlayerStringCards()){
                if (carte[i].equals(viewCard)){present = true; break;}
            }
            if (!present){player.getClientView().getPlayerStringCards().add(carte[i]);}
            present = false;
        }
    }

    private void chosenHandCard() throws IOException {
        sendMessageToServer("playCard");
        System.out.println("You chose to play a card from your deck!");
        System.out.println("This is your board:\n");
        System.out.print("////////////////////////////////// INIZIO BOARD //////////////////////////////////////////\n");
        String actualBoard= in.readLine();
        do{
            System.out.println(actualBoard);
            actualBoard= in.readLine();
        }while (!actualBoard.equals("fine board"));
        System.out.println();
        System.out.println("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
        System.out.println("\n");
        System.out.println("These are your deck cards: ");
        System.out.println("\n");
        System.out.println(player.getClientView().getPlayerStringCards().get(0));
        System.out.println(player.getClientView().getPlayerStringCards().get(1));
        System.out.println(player.getClientView().getPlayerStringCards().get(2));
        System.out.println("Which card do you want to play on the board?");
        System.out.println("1-> first card\n2-> second card\n3-> third card");
        String result= stdin.readLine();
        int size= Integer.parseInt(result);
        out.println(size-1); //Carta scelta dal deck del player, sto mandando al server
        System.out.println("Su quale carta della board vuoi andare a piazzare la tua carta?");
        System.out.println("1-> corrisponde alla carta iniziale");
        String chosenCardOnTheBoard= stdin.readLine();
        int paolo= Integer.parseInt(chosenCardOnTheBoard);
        out.println(paolo-1);
        String avaiableCorners= in.readLine();
        do{
            System.out.println(avaiableCorners);
            avaiableCorners= in.readLine();
        } while(!avaiableCorners.equals("end"));
        System.out.println();
        System.out.print("Choose the corner you want to place the card on: ");
        String cornerChosen= stdin.readLine().toUpperCase();
        System.out.println("Corner correctly chosen!");
        out.println(cornerChosen);
        String ultimo= in.readLine();
        System.out.println(ultimo);
        //da gestire nel caso l'operazione fallisca
        player.getClientView().getPlayerStringCards().remove(size-1);
        drawCard();
    }

    private void visualizeCommonObjective() throws IOException {
        sendMessageToServer("visualizeCommonObjectiveCards");
        System.out.println("Common Objective Cards are:\n");
        System.out.println(in.readLine());//first common card
        System.out.println(in.readLine());//second common card
    }
    private void visualizeSecretObjective() throws IOException {
        sendMessageToServer("secret");
        System.out.println("You chose to visualize your secret card!\n");
        String result= in.readLine();
        System.out.println(result);
        System.out.println("\n");
        System.out.println("This is your objective card!");
    }
    private void showBoard() throws IOException {
        sendMessageToServer("showBoard");
        System.out.println("You chose to visualize your board!");
        System.out.print("////////////////////////////////// INIZIO BOARD ////////////////////////////////////////// \n");
        String result= in.readLine();
        do{
            System.out.println(result);
            result= in.readLine();
        }while (!result.equals("fine board"));
        System.out.println();
        System.out.println("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
    }
    private void showPoints() throws IOException {
        sendMessageToServer("showPoints");
        System.out.println("You chose to visualize your points!\n");
        String result= in.readLine();
        System.out.println("At the moment your points are: " + result);
    }

    private void showWell() throws IOException {
        sendMessageToServer("showWell");
        System.out.println("Common Well:\n------------------------------------------------------------------------------------------");
        System.out.println(in.readLine());//prima carta nel pozzo
        System.out.println(in.readLine());//seconda carta nel pozzo
        System.out.println(in.readLine());//terza carta nel pozzo
        System.out.println(in.readLine());//quarta carta nel pozzo
        in.readLine();//spazio
        System.out.println("------------------------------------------------------------------------------------------");

    }
    private void drawCard() throws IOException {
        sendMessageToServer("drawCard");
        System.out.println("You chose to draw a card!\n");
        showWell();
        String drawnCard;
        do {
            System.out.println("""
                    where do you want to draw the card from?
                    ->deck
                    ->well""");
            drawnCard = stdin.readLine().toLowerCase();
            if (drawnCard.equals("deck")) {
                sendMessageToServer(drawnCard);
                drawCardFromDeck();
            }
            else if (drawnCard.equals("well")) {
                sendMessageToServer(drawnCard);
                drawCardFromWell();
            }
            else System.out.println("write 'deck' or 'well'");
        }while (!drawnCard.equals("well") && !drawnCard.equals("deck"));
    }
    private void drawCardFromDeck() throws IOException {
        System.out.println("""
                Where do you want to draw your card from?
                ->Resource
                ->Gold""");
        String drawnCard;
        do{
            drawnCard = stdin.readLine().toLowerCase();
            if (drawnCard.equals("resource")) {
                drawCardFromResourceDeck();
            }
            else if (drawnCard.equals("gold")) {
                drawCardFromGoldDeck();
            }
            else System.out.println("Write 'resource' or 'gold'");

        }while (!drawnCard.equals("resource") && !drawnCard.equals("gold"));
    }
    private void drawCardFromResourceDeck() throws IOException {
        sendMessageToServer("drawCardFromResourceDeck");
        System.out.println(in.readLine());
        sendMessageToServer("showYourCardDeck");
        System.out.println("Your Deck:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingAndPrintingCards();
        System.out.println("--------------------------------------------------------------------------------------");
    }
    private void drawCardFromGoldDeck() throws IOException {
        sendMessageToServer("drawCardFromGoldDeck");
        System.out.println(in.readLine());
        sendMessageToServer("showYourCardDeck");
        System.out.println("Your Deck:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingAndPrintingCards();
        System.out.println("--------------------------------------------------------------------------------------");
    }
    private void drawCardFromWell() throws IOException {
        sendMessageToServer("showWell");
        System.out.println("Which card from the well do you want to draw?");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("select '0' for"+in.readLine());//prima carta nel pozzo
        System.out.println("select '1' for"+in.readLine());//seconda carta nel pozzo
        System.out.println("select '2' for"+in.readLine());//terza carta nel pozzo
        System.out.println("select '3' for"+in.readLine());//quarta carta nel pozzo
        in.readLine();//spazio
        System.out.println("------------------------------------------------------------------------------------------");
        String selectedCard;
        do{
            selectedCard= readMessageFromUser();
            if(sceltaNonIdonea(selectedCard)) {
                System.out.println("wrong choice, try again");
            }
        }while(sceltaNonIdonea(selectedCard));
        sendMessageToServer(selectedCard);

        //ora gestisco le risposte del server
        String result = in.readLine();
        if(result.equals("operation performed correctly")) {
            System.out.println("Operation 'Draw card from Well' performed correctly");
            sendMessageToServer("showYourCardDeck");
            System.out.println("Your Deck:" );
            System.out.println("--------------------------------------------------------------------------------------");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            showWell();
        }
        else{
            System.out.println("operation performed incorrectly");
            System.out.println("Server says: "+ result);
            System.out.println("Your Deck:" );
            System.out.println("--------------------------------------------------------------------------------------");
            sendMessageToServer("showYourCardDeck");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            showWell();
        }
    }

    private boolean sceltaNonIdonea(String selectedCard) {
        int num = Integer.parseInt(selectedCard);
        return num < 0 || num > 3;
    }

    private void quit() throws IOException {
        sendMessageToServer("quit");
        System.out.println("You chose to quit Codex :c \n");
        System.out.println(in.readLine()); //quit
        isConnectionClosed=true;

    }
    private void runEndTurn() throws IOException {
        sendMessageToServer("endTurn");
        System.out.println("You chose to end your turn.");
        String answer= in.readLine();
        System.out.println(answer);
        setCurrentPlayer(answer);
        String updatingCurrentPlayer= in.readLine(); //-> aggiornamento del currentPLayer
        System.out.println(updatingCurrentPlayer);
        cleanTheSocket();
    }

    private void exitFromGame() throws IOException {
        sendMessageToServer("endTurn");
        String answer= in.readLine();
        setCurrentPlayer(answer);
        System.out.println("Current player: " + currentPlayer);
        String updatingCurrentPlayer= in.readLine(); //-> aggiornamento del currentPLayer
        System.out.println(updatingCurrentPlayer);
        cleanTheSocket();
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    private void takingTheInitialCard() throws IOException {                                                                            //DA FINIRE
        boolean hasTheCardAlreadyBeenTurn= false;
        String firstCard= in.readLine();
        String FrontalCorners= in.readLine();
        String BackCorners=in.readLine();
        in.readLine(); //For gui purpose
        System.out.println("server says: " + firstCard);
        System.out.println(FrontalCorners);
        System.out.println(BackCorners);
        String integer=null;
        int size;
        System.out.println("Do you want to turn your card?");
        while(!hasTheCardAlreadyBeenTurn){
        System.out.println("Please type 1 if you want to turn your card\nPlease type 2 if you don't want to turn your card");
        integer= stdin.readLine();
        if(integer.equals("1")|| integer.equals("2")){
            System.out.println("Okay!");
            hasTheCardAlreadyBeenTurn=true;
         }
        else{
            System.out.println("You didn't type correctly!");
            }
        }
        size = Integer.parseInt(integer);
        out.println(size-1);
        System.out.println("Initial Card correctly placed!");
    }
    private synchronized void assigningSecretCard() throws IOException {
        boolean isNumberCorrect=false;
        String stringSecretCard= in.readLine(); //carta
        String stringSecondCard= in.readLine(); //carta
        String firstCardID= in.readLine(); //id
        String secondCardID= in.readLine(); //id
        int firstID= Integer.parseInt(firstCardID);
        int secondID= Integer.parseInt(secondCardID);
        System.out.println("Server says: your first objective card is" + stringSecretCard);
        System.out.println("Server says: your second objective card is" + stringSecondCard);
        while(!isNumberCorrect){
        System.out.println("Choose the card you want to draw:\nType 1 if you want to select the first card\nType 2 if you want to select the second card");
        String numberChosen= stdin.readLine();
        int size = Integer.parseInt(numberChosen);
        if(size==1 || size==2){
            System.out.println("Card chosen correctly");
            out.println(size);
            isNumberCorrect=true;
        }
        else{
            System.out.println("Please choose your card correctly!");
            }
        }
    }
    private void loginPlayer(Player player) throws IOException, InterruptedException { //LOGIN METHOD
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse); //Inserisci il tuo nome per favore
        System.out.println(">");
        String loginName = stdin.readLine();
        sendMessageToServer(loginName);
        String correctLogin = in.readLine();
        System.out.println("Server says: " + correctLogin); //Login effettuato con successo
        player.getClientView().setUserName(loginName);
        clientView.setUserName(loginName);                      //UPDATING CLIENT VIEW
        synchronized (this)
        {
            clientView.setIndex(index);
            index++;
        }
        chooseYourDotColor();
        chooseNumberOfPlayers();
    }
    public void sendMessageToServer(String message) {
        out.println(message);
    }
    public String readMessageFromUser() throws IOException {
        return stdin.readLine();
    }
    private void chooseYourDotColor() throws IOException {
            String messageFromServer;
            do {
                messageFromServer = in.readLine();
                System.out.println("Server says: " + messageFromServer);
                System.out.println("Type\n -RED if you want the red dot\n -BLUE if you want the blue dot\n -GREEN if you want the green dot\n -YELLOW if you want the yellow dot");
                System.out.println(">");
                String dotColor = stdin.readLine();
                dotColor = dotColor.toUpperCase();
                sendMessageToServer(dotColor);
                messageFromServer = in.readLine();
                System.out.println(messageFromServer);
            }while (messageFromServer.equals("Chosen color not available!"));
    }
    private void chooseNumberOfPlayers() throws IOException {
        StringBuilder numberOfPlayers= new StringBuilder();
        String stringNumberOfPlayers;
        numberOfPlayers.append(in.readLine());
        stringNumberOfPlayers = in.readLine();
        numberOfPlayers.append(stringNumberOfPlayers);
        numberOfPlayers.append(in.readLine());
        System.out.println("server says: "+ numberOfPlayers);              //SChoose numbers of players
        if(Integer.parseInt(stringNumberOfPlayers)>1) {
            String answer = in.readLine();
            System.out.println("Server says: " + answer);
            return;
        }
        System.out.println(">");
        String numbersOfPlayers= stdin.readLine();
        int size = Integer.parseInt(numbersOfPlayers);
        out.println(size);
        System.out.println(size);
        String serverAnswer = in.readLine();
        System.out.println("Server says: " + serverAnswer); //PLayers nuumber correctly chosen
        String waitingClients= in.readLine();
        System.out.println("Server says: " + waitingClients);
    }
}

