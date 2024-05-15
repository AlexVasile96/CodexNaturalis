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
            this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));    //receiving server data
            this.out= new PrintWriter(socket.getOutputStream(), true);
            this.stdin= new BufferedReader(new InputStreamReader(System.in));
            this.player=new Player(null,0,null,null );
    }

@Override
    public void run() {
    String command;
        try {
            System.out.println("Welcome! I'm the server, please type anything to start the conversation!\n");
            while (!isTheWhileActive) {
                try {                                                       //il client type a message
                    if (clientView.getUserName() == null) {                 //If client hasn't made the login yet, he has to log first.
                        System.out.print(">");
                        command = stdin.readLine();
                        sendMessageToServer(command);
                        loginPlayer(player);                                  //Actual Login

                        String areAllConnected= in.readLine(); //Allplayers are connected
                        System.out.println(areAllConnected);
                        assigningSecretCard();                                //Choosing the secret Card
                        takingTheInitialCard();                               //Taking the initial Card
                        System.out.println("Login phase ended!");
                        currentPlayer = in.readLine();                      //who is the current player?
                    } else {
                        while (!isTheWhileActive){
                            if(isConnectionClosed){
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



private void waitUntilItsYourTurn() throws IOException {
        while(!clientView.getUserName().equals(getCurrentPlayer()))
        {
            String waitForCall= in.readLine();
            if(waitForCall.equals(clientView.getUserName())){
                setCurrentPlayer(waitForCall);
                System.out.println(getCurrentPlayer());
                in.readLine(); //"endturn"
            }
            else {
                System.out.println("Current Player is still deciding what's his next move...");}
        }
}
    private void makeYourMoves() throws IOException {
        if(!player.isThePlayerDeckStarted())
        {
            player.setThePlayerDeckStarted(true);
            showCards();
        }
        System.out.println("It's your turn!");
        System.out.println("What do you want to do?");
        System.out.println("Please type help if you want to see which moves you can make.");
        String command= stdin.readLine().toLowerCase();
        if(command.equals("playcard") && player.isHasThePlayerAlreadyPLacedACard())
        {
            System.out.println("You already placed and drew a card!");
            return;
        }
        if(command.equals("endturn")&& !player.isHasThePlayerAlreadyPLacedACard())
        {
            System.out.println("You have to place a card first");
            return;
        }
        actionsInput(command);
    }



    private void actionsInput(String userInput) throws IOException { //GAME STARTED
        try {
            switch (userInput) {
                case "help"-> printHelp();
                case "actions" -> printActions();
                case "showdeck", "0" -> showCards();
                case "playcard", "1" -> playCardFromYourDeck();
                case "common", "2" -> visualizeCommonObjective();
                case "secret", "3" -> visualizeSecretObjective();
                case "board", "4" -> showBoard();
                case "points", "5" -> showPoints();
                case "showwell", "6" -> showWell();
                case "endturn", "7" -> runEndTurn();//run
                case "allboards", "8" -> showEachPlayerBoard();
                case "yourseeds", "9" -> showYourSpecificSeed();
                case "allseed", "10" -> showAllSpecificSeed();
                case "allpoints", "11" -> showAllPoints();
                case "quit", "12" -> quit();
                default -> 
                    System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
                    
            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void showAllPoints() throws IOException {
        sendMessageToServer("showAllPoints");
        String messageFromServer = in.readLine();
        do{
            System.out.println(messageFromServer);
            messageFromServer = in.readLine();
        }while (!messageFromServer.equals("exit"));
    }

    private void showCards() throws IOException {
        sendMessageToServer("showYourCardDeck");
        System.out.println("Your deck:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingAndPrintingCards();
        System.out.println("--------------------------------------------------------------------------------------");
    }

    private void receivingAndPrintingCards() throws IOException {
        String firstCard = in.readLine(); //Reading all three cards
        String secondCard = in.readLine();
        String thirdCard = in.readLine();
        in.readLine();//the space
        updatingView(firstCard, secondCard, thirdCard);
        for (String s : player.getClientView().getPlayerStringCards()) {
            System.out.println(s);
        }
    }


    private void showEachPlayerBoard() throws IOException {
        sendMessageToServer("showEachPlayerBoard");
        System.out.println("You decided to print all players boards!");
        String messageFromServer = in.readLine();
        do{
            System.out.println(messageFromServer);
            messageFromServer = in.readLine();
        }while (!messageFromServer.equals("exit"));
        System.out.println("All Boards Printed!");
    }
    private void showYourSpecificSeed() throws IOException {
        sendMessageToServer("showYourSpecificSeed");
        System.out.println("Your specific seeds: ");
        String yourseeds= in.readLine();
        System.out.println(yourseeds);
    }

    private void showAllSpecificSeed() throws IOException {
        sendMessageToServer("showAllSpecificSeed");
        String messageFromServer = in.readLine();
        do{
            System.out.println(messageFromServer);
            messageFromServer = in.readLine();
        }while (!messageFromServer.equals("exit"));
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

    private void playCardFromYourDeck() throws IOException {
        String messageFromServer, inputFromClient;
        int size;
        String[] validInputs;
        boolean check;

        player.setHasThePlayerAlreadyPLacedACard(true);//player puo giocare una volta per turno
        System.out.println("Play a card from your deck!");
        showBoard();
        showYourSpecificSeed();
        sendMessageToServer("playCard");

        //scelta carta dal mazzo
        System.out.println("\n------------------------------------------------------------------------------------------------");
        System.out.println("These are your deck cards: ");
        System.out.println(player.getClientView().getPlayerStringCards().get(0));
        System.out.println(player.getClientView().getPlayerStringCards().get(1));
        System.out.println(player.getClientView().getPlayerStringCards().get(2));
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("Which card do you want to play on the board?");
        System.out.println("1-> first card\n2-> second card\n3-> third card");
        validInputs = new String[]{"1", "2", "3"};
        do {
            size = Integer.parseInt(controlInputFromUser(validInputs));
            out.println(size - 1); //Carta scelta dal deck del player, sto mandando al server
            messageFromServer = in.readLine();
            if(messageFromServer.equals("Gold Card not placeable")){
                messageFromServer = in.readLine();
                System.out.println("gold card requires: " + messageFromServer);
                messageFromServer = in.readLine();
                System.out.println("you got: " + messageFromServer);
                System.out.println("For now choose another card");
            }
        }while (!messageFromServer.equals("puoi procedere"));
        player.getClientView().getPlayerStringCards().remove(size-1);   //rimuovo dalla view la carta scelta

        //scelta se girare la carta
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("Do you want to turn your card?\n(Back of all cards has 4 empty corners and 1 attribute representing the specific seed of the card)");
        System.out.println("Please type\n" + "-> 1 if you want to turn your card! " + "-> 2 if you don't want to turn your card!");
        validInputs = new String[]{"1", "2"};
        inputFromClient = controlInputFromUser(validInputs);
        sendMessageToServer(inputFromClient); //outprintln 2
        //DA FINIRE

        //carte sulla board
        System.out.println("Su quale carta della board vuoi andare a piazzare la tua carta?");
        messageFromServer = in.readLine();
        int numeroCarte=0;
        do{
            if(messageFromServer.equals("incrementaContatore")) numeroCarte++;
            else System.out.println(messageFromServer);
            messageFromServer= in.readLine();
        }while (!messageFromServer.equals("exit"));

        //controllo input
        validInputs = new String[numeroCarte];
        for (int j=0; j<numeroCarte; j++)
            validInputs[j] = String.valueOf(j+1);
        size= Integer.parseInt(controlInputFromUser(validInputs));
        out.println(size-1);


        //avaiableCorners
        String[] angoli ={"TL","TR","BR","BL"};
        validInputs = new String[4];
        check = false;
        size=0;
        messageFromServer = in.readLine();
        do{
            for (String corner: angoli){
                if(messageFromServer.equals(corner) && !check){
                    validInputs[size]=messageFromServer;
                    size++;
                    check = true;
                }
            }
            if(!check) System.out.println(messageFromServer);
            check = false;
            messageFromServer= in.readLine();
        } while(!messageFromServer.equals("end"));
        System.out.println();
        System.out.print("Choose the corner you want to place the card on: ");
        if(size<4){
            angoli = new String[size];
            for (int i=0; i<size; i++) {
                angoli[i]=validInputs[i];
            }
            inputFromClient = controlInputFromUser(angoli);
        }
        else inputFromClient = controlInputFromUser(validInputs);

        out.println(inputFromClient);

        String ultimo= in.readLine();
        System.out.println(ultimo);
        drawCard();
        status();

        if(clientView.getPlayerScore()>=20)
        {
            System.out.println(in.readLine()); //all players have on last turn and then the game will end
            player.setHasThePlayerAlreadyPLacedACard(false);
            System.out.println("You chose to end your turn.");
            String answer= in.readLine();
            System.out.println("Next player will be " + answer);   //next player will be +...
            setCurrentPlayer(answer);
            String updatingCurrentPlayer= in.readLine(); //-> aggiornamento del currentPLayer
            System.out.println(updatingCurrentPlayer);
            cleanTheSocket();
        }

    }

    private String controlInputFromUser(String[] elements) throws IOException {
        String inputClient;
        boolean found = false;
        do {
            inputClient = stdin.readLine().toUpperCase();
            for (String element : elements) {
                if (inputClient.equals(element.toUpperCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Input errato! Riprova!");
            }
        } while (!found);
        return inputClient;
    }


    private void status() throws IOException {
        sendMessageToServer("status");
        String points= in.readLine();
        System.out.println("you got: "+ points + " points!");
        clientView.setPlayerScore(Integer.parseInt(points));
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
        System.out.print("////////////////////////////////////////////////////// INIZIO BOARD ////////////////////////////////////////////////////////////// \n");
        String result= in.readLine();
        do{
            System.out.println(result);
            result= in.readLine();
        }while (!result.equals("fine board"));
        System.out.println();
        System.out.println("////////////////////////////////////////////////////// FINE BOARD ////////////////////////////////////////////////////////////////");
    }
    private void showPoints() throws IOException {
        sendMessageToServer("showPoints");
        System.out.println("You chose to visualize your points!\n");
        String result= in.readLine();
        clientView.setPlayerScore(Integer.parseInt(result));
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
        //showWell();
        sendMessageToServer("drawCard");
        System.out.println("You chose to draw a card!\n");
        String drawnCard;
        do {
            System.out.println("""
                    Where do you want to draw the card from?
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
        System.out.println("Select '0' for"+in.readLine()); //cards in the well
        System.out.println("Select '1' for"+in.readLine());
        System.out.println("Select '2' for"+in.readLine());
        System.out.println("Select '3' for"+in.readLine());
        in.readLine();//spazio
        System.out.println("------------------------------------------------------------------------------------------");
        String selectedCard;
        do{
            selectedCard= readMessageFromUser();
            if(wrongChoice(selectedCard)) {
                System.out.println("Wrong choice, try again");
            }
        }while(wrongChoice(selectedCard));
        sendMessageToServer(selectedCard);

        //ora gestisco le risposte del server
        String result = in.readLine();
        if(result.equals("Operation performed correctly")) {
            System.out.println("Operation 'Draw card from Well' performed correctly");
            sendMessageToServer("showYourCardDeck");
            System.out.println("Your Deck:" );
            System.out.println("--------------------------------------------------------------------------------------");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            showWell();
        }
        else{
            System.out.println("Operation performed incorrectly");
            System.out.println("Server says: "+ result);
            System.out.println("Your Deck:" );
            System.out.println("--------------------------------------------------------------------------------------");
            sendMessageToServer("showYourCardDeck");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            showWell();
        }
    }

    private boolean wrongChoice(String selectedCard) {
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

        player.setHasThePlayerAlreadyPLacedACard(false);
        System.out.println("You chose to end your turn.");
        String answer= in.readLine();
        System.out.println(answer);
        setCurrentPlayer(answer);
        String updatingCurrentPlayer= in.readLine(); //-> updating currentPLayer
        System.out.println(updatingCurrentPlayer);
        //clientView.update(player);
        cleanTheSocket();
    }

    private void exitFromGame() throws IOException {
        sendMessageToServer("endTurn");
        String answer= in.readLine();
        setCurrentPlayer(answer);
        if(answer.equals("All clients have quit")){
            System.out.println("All clients have quit");
        }
        else{
            System.out.println("Current player: " + currentPlayer);
            String updatingCurrentPlayer= in.readLine(); //-> updating currentPLayer
            System.out.println(updatingCurrentPlayer);
        }
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
        String stringSecretCard= in.readLine(); //card
        String stringSecondCard= in.readLine(); //card
        in.readLine(); //id
         in.readLine(); //id
        System.out.println("Server says: your first objective card is" + stringSecretCard);
        System.out.println("Server says: your second objective card is" + stringSecondCard);
        while(!isNumberCorrect){
        System.out.println("Choose the card you want to draw:\nType 1 if you want to select the first card\nType 2 if you want to select the second card");
        String numberChosen= stdin.readLine();
        int size = Integer.parseInt(numberChosen);
        if(size!=1 && size!=2){
            System.out.println("Please choose your card correctly!");
        }
        else {
            System.out.println("Card chosen correctly");
            out.println(size);
            isNumberCorrect=true;
            }
        }
    }

    private synchronized void loginPlayer(Player player) throws IOException, InterruptedException { //LOGIN METHOD
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse); //Inserisci il tuo nome per favore
        System.out.println(">");
        String loginName = stdin.readLine();
        sendMessageToServer(loginName);
        String correctLogin = in.readLine();
        System.out.println("Server says: " + correctLogin);     //Login succesfully done
        player.getClientView().setUserName(loginName);
        clientView.setUserName(loginName);                      //UPDATING CLIENT VIEW
        synchronized (this)
        {
            clientView.setIndex(index);
            index++;
        }
        synchronized (this){
        chooseYourDotColor();
        }
        chooseNumberOfPlayers();
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

    private synchronized void chooseNumberOfPlayers() throws IOException, InterruptedException {
        StringBuilder numberOfPlayers= new StringBuilder();
        String stringNumberOfPlayers;
        numberOfPlayers.append(in.readLine());
        stringNumberOfPlayers = in.readLine();
        numberOfPlayers.append(stringNumberOfPlayers);
        numberOfPlayers.append(in.readLine());
        System.out.println("server says: "+ numberOfPlayers);              //Choose numbers of players
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

    private synchronized void printHelp() throws IOException {
        sendMessageToServer("help");
        String serviceString=in.readLine();
        System.out.println(serviceString);
        System.out.println(
                """
                        Supported commands are:\s
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
                        - If you type->  'showWell /6': you'll be displayed the well
                        - If you type->  'endturn /7': end your turn
                        - If you type->  'allboards /8': you'll be displayed your opponent boards
                        - if you type ->  'yourseeds /9': you'll be displayed all the specific seed you have on your board
                        - if you type ->  'allseed /10': you'll be displayed all your opponent specific seed
                        - if you type ->  'allpoints /11': you'll be displayed your opponents' points
                        - if you type ->  'quit /11': you'll quit the game
                        """
        );
    }

    private void cleanTheSocket() {
        out.flush();
    }
    public void sendMessageToServer(String message) {
        out.println(message);
    }
    public String readMessageFromUser() throws IOException {
        return stdin.readLine();
    }
}

