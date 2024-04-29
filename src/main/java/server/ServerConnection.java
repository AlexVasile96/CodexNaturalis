package server;
import exceptions.InvalidCornerException;
import exceptions.OperationCancelledException;
import model.card.Card;
import model.card.GoldCard;
import model.game.*;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class ServerConnection implements Runnable {

    private Socket socket;
    private ClientView clientView;
    private BufferedReader in;
    private BufferedReader stdin;
    private PrintWriter out;
    private Player player;
    private Boolean myTurn;
    private int numberOfCardsplaced=1;

    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
            this.clientView=clientView;
            this.socket = server;
            this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));    //ricevere dati dal server
            this.out= new PrintWriter(socket.getOutputStream(), true);
            this.stdin= new BufferedReader(new InputStreamReader(System.in));               //scanner, mi serve per scrivere
            this.player=new Player(null,0,null,null );
            this.myTurn=false;
    }

    @Override
    public void run() {
        String command;
        String isMyTurn;
        try {
            System.out.println("Benvenuto!Sono il server! Scrivere una qualsiasi stringa per iniziare la conversazione\n");
            while (true) {
                try {
                    System.out.print(">");                          //il client scrive un messaggio
                    if (clientView.getUserName() == null) {             //If client hasn't made the login yet, he has to log first.
                        command=stdin.readLine();
                        sendMessageToServer(command);
                        loginPlayer(player);                                  //Actual Login
                        assigningSecretCard();                          //Choosing the secret Card
                        takingTheInitialCard();
                    }
                    else {
                        isMyTurn = in.readLine();                //è il tuo turno
                        System.out.println(isMyTurn);                   //viene stampato è il tuo turno
                        if (isMyTurn != null && isMyTurn.equals("è il tuo turno!!")) {
                            myTurn = true;
                        }
                        if (myTurn) {
                            System.out.println("command:");
                            /*System.out.println("Menu:\n" +
                                    "1.  help - printHelp()\n" +
                                    "2.  status - printStatus()\n" +
                                    "3.  actions - printActions()\n" +
                                    "4.  showYourCardDeck, 0 - showCards()\n" +
                                    "5.  playCardFromYourHand, 1 - chosenHandCard()\n" +
                                    "6.  visualizeCommonObjectiveCards, 2 - visualizeCommonObjective()\n" +
                                    "7.  secret, 3 - visualizeSecretObjective()\n" +
                                    "8.  showBoard, 4 - showBoard()\n" +
                                    "9.  showPoints, 5 - showPoints()\n" +
                                    "10. drawResourceCardFromDeck, 6 - drawResourceCardFromDeck()\n" +
                                    "11. drawGoldCardFromDeck, 7 - drawGoldCardFromDeck()\n" +
                                    "12. drawCardFromWell, 8 - drawCardFromWell()\n" +
                                    "13. endTurn, 9 - runEndTurn()\n" +
                                    "14. quit, 10 - quit()");*/
                            command=stdin.readLine().toLowerCase();
                            /*if(!command.equals("drawcard")) { //aggiunto il controllo cosi da mandare il comando appropriato al server
                                sendMessageToServer(command);
                            }*/
                            actionsInput(command);
                        }
                        else{
                            while (!myTurn) {
                                command=stdin.readLine();
                                System.out.println("attendi...");
                                isMyTurn=in.readLine();
                                if (isMyTurn != null && isMyTurn.equals("è il tuo turno!!")) {
                                    myTurn = true;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
                                                }
    }

    private void takingTheInitialCard() throws IOException {
        String firstCard= in.readLine();
        String FrontalCorners= in.readLine();
        String BackCorners=in.readLine();
        System.out.println("server says: " + firstCard);
        System.out.println("Vuoi girare la tua carta? Scrivere 1 per tenerla con gli angoli davanti, 2 per girarla");
        System.out.println(FrontalCorners);
        System.out.println(BackCorners);
        String intero= stdin.readLine();
        int size = Integer.parseInt(intero);
        out.println(size-1);
    }

    private void receiveYourStartingcards() throws IOException {
        for(int i=0; i<player.getPlayerCards().size();i++)
        {
            String card;
            card = in.readLine();
            //player.getClientView().getPlayerCards().add(card);

        }
    }

    private synchronized void assigningSecretCard() throws IOException {
        String stringSecretCard= in.readLine();
        System.out.println("server says: " + stringSecretCard);
        System.out.println("selaziona la carta: 1-prima carta, 2-seconda carta");
        String intero= stdin.readLine();
        int size = Integer.parseInt(intero);
        out.println(size);


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
        chooseYourDotColor(player);
        chooseNumberOfPlayers();
    }

    public synchronized void sendMessageToServer(String message) {
        out.println(message);
    }     //metodo per mandare un singolo messaggio al server

    private void chooseYourDotColor(Player player) throws IOException {
        boolean isTheColorOkay= false;
        while(!isTheColorOkay) {
            String chooseYourColor = in.readLine();
            System.out.println("server says: " + chooseYourColor);              //Scegli il colore del tuo dot
            System.out.println("Inserire\n -RED per scegliere il dot di colore rosso\n -BLUE per scegliere il colore blu\n -GREEN per scegliere il colore verde\n -YELLOW per scegliere il colore giallo");
            System.out.println(">");
            String dotColor = stdin.readLine();
            dotColor = dotColor.toUpperCase();
            sendMessageToServer(dotColor);
            System.out.println(dotColor);
            String serverAnswer = in.readLine();
            if (serverAnswer.equals("Quel colore è gia stato scelto da un altro utente, perfavore inserire un altro colore")) {
                System.out.println("server says: " + serverAnswer);

            }
            else {
                System.out.println("Server says: " + serverAnswer); //Colore del dot scelto correttamente
                isTheColorOkay=true;
                Dot dot= Dot.valueOf(dotColor);
                clientView.setDot(dot);
                player.getClientView().setDot(dot);
            }
        }

    }

    private void chooseNumberOfPlayers() throws IOException {
        StringBuilder stampa= new StringBuilder();
        String stringNumberOfPlayers; //numero di giocatori

        stampa.append(in.readLine());
        stringNumberOfPlayers = in.readLine();
        stampa.append(stringNumberOfPlayers);
        stampa.append(in.readLine());
        System.out.println("server says: "+ String.valueOf(stampa));              //Scegli il numero di partecipanti
        if(Integer.parseInt(stringNumberOfPlayers)>1) {
        //if(stringNumberOfPlayers.equals("NO")){
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
        System.out.println("Server says: " + serverAnswer); //Numero di giocatori scelto correttamente
        String waitingClients= in.readLine();
        System.out.println("Server says: " + waitingClients);

    }
    private void actionsInput(String userInput) throws IOException { //GAME STARTED
        try {
            switch (userInput) {
                //Display the player's commands options
                case "help" -> {
                    printHelp();


                    //Display the game's status
                }
                case "status" -> printStatus();

                //Display the list of available actions for the player in the current turn phase
                case "actions" -> printActions();

                //The following methods are used to run game actions
                case "showyourcarddeck", "0" -> showCards();
                case "playcard", "1" -> chosenHandCard();
                case "visualizecommonobjectivecards", "2" -> visualizeCommonObjective();
                case "secret", "3" -> visualizeSecretObjective();
                case "showboard", "4" -> showBoard();
                case "showpoints", "5" -> showPoints();
                case "drawcard", "6" -> drawCard();
                case "drawcardfromdeck", "7"-> drawCardFromDeck();
                case "drawcardfromwell", "8" -> drawCardFromWell();
                case "endturn", "9" -> runEndTurn();//run
                case "quit", "10" -> quit();
                case "showwell", "11" -> showWell();
                default -> {
                    System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
                    sendMessageToServer("default");
                    in.readLine();
                }
            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void printHelp() throws IOException {
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
                        - If you type-> 'showYourCardDeck / 0 ': display player's cards
                        - If you type-> 'playCard /1': select the card you want to place from your hand
                        - If you type->  'common /2': visualize the common objective cards
                        - If you type->  'secret /3': visualize your secret objective card
                        - If you type->  'showBoard /4':print your board
                        - If you type->  'showPoints /5': show your points
                        - If you type->  'drawResourceCardFromDeck /6': draw a card from the resource deck
                        - If you type->  'drawGoldCardFromDeck /7': draw a card from the gold deck
                        - If you type->  'drawCardFromWell /8': draw a card from the well
                        - If you type->  'endTurn /9': end your turn
                        - If you type->  'showWell /9': you'll be displayed the well
                        . if you type ->  'quit /10': esci dal gioco\n"""
        );
    }
    private void printStatus(){
        sendMessageToServer("status");
        System.out.println("\n"+clientView.toString());
    }
    private void showCards() throws IOException {
        sendMessageToServer("showYourCardDeck");
        System.out.println("Il tuo mazzo:" );
        String firstCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String secondCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String thirdCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String spatio=in.readLine(); //Hai selezionato di vedere le tue carte
        System.out.println(firstCard);
        System.out.println(secondCard);
        System.out.println(thirdCard);
        player.getClientView().getPlayerStringCards().add(firstCard);
        player.getClientView().getPlayerStringCards().add(secondCard);
        player.getClientView().getPlayerStringCards().add(thirdCard);
        System.out.println(spatio);
        System.out.println("Carte lette correttamente");
        System.out.println(player.getClientView().getPlayerStringCards());
    }

    private void chosenHandCard() throws IOException {
        sendMessageToServer("playCard");
        //Al game controller devo passare in ordine ->
        //carta del player deck che voglio giocare
        //carta della board su cui piazzare la mia carta
        //Angolo su cui voglio piazzare la mia carta
        System.out.println("Hai scelto di giocare una carta dal tuo deck!");
        System.out.println("Questa è la tua attuale board:\n");
        System.out.print("////////////////////////////////// INIZIO BOARD //////////////////////////////////////////\n");
        String actualBoard= in.readLine();
        do{
            System.out.println(actualBoard);
            actualBoard= in.readLine();
        }while (!actualBoard.equals("fine board"));
        System.out.println();
        System.out.println("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
        System.out.println("\n");
        System.out.println("Queste sono le carte del tuo deck: ");
        System.out.println("\n");
        System.out.println(player.getClientView().getPlayerStringCards().get(0));
        System.out.println(player.getClientView().getPlayerStringCards().get(1));
        System.out.println(player.getClientView().getPlayerStringCards().get(2));
        System.out.println("Scegli quale carta vuoi giocare sulla tua board:");
        System.out.println("1-> prima carta\n2-> seconda carta\n3-> terza carta");
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
        String cornerChosen= stdin.readLine();
        System.out.println("Corner scelto correttamente!");
        out.println(cornerChosen);


        //Card selectedCardFromTheDeck = chooseCard(size);
     /*
        checkIfTheCardExist(size);                                              //CHECKING IF THE CARD TRULY EXISTS->OKAY
        //boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirements are respected->OKAY
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);
        if(!canIPLaceTheGoldCard && selectedCardFromTheDeck.getId()>40) return; //DA MODIFICARE
        Scanner scanner= new Scanner(System.in);
        System.out.println("Ti verranno ora mostrate tutte le carte presenti sulla tua board");
        Card initialCard = clientView.getBoard().getCardsOnTheBoardList().get(0);            //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom =clientView.getBoard().getCardsOnTheBoardList();   //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM

        showingToTheCurrentPlayerCardsOnTheBoard(cardsPlayerCanChooseFrom);                     //showing to the current player the cards he/she has on the board
        Card cardPlayerChoose= selectTheCardFromTheBoard(cardsPlayerCanChooseFrom,scanner);     //Choosing the card
        System.out.println("Card correctly chosen");
        List<Corner> availableCorners = creatingCorners(cardPlayerChoose); //Creating 4 corners to handle SelectedCard corners

        if (cardPlayerChoose.getId() == initialCard.getId()) {        //THE  CARD CHOSEN ON THE BOARD IS THE INITIAL CARD AND WE HAVE TO DELETE THE CORNERS NOT AVAILABLE
            cardChosenIsTheInitialcard(initialCard,availableCorners);
        } else {                                                        //CARD CHOSEN ISN'T THE INITIAL CARD
            List<Corner> corner = creatingCorners(cardPlayerChoose);
            cardChosenIsNotTheInitialcard(availableCorners,corner);
        }

        String selectedCorner= freeCornersOfTheSelectedCard(availableCorners, cardPlayerChoose,scanner); //Showing the available corners of the card and letting the player choose one
        */
        numberOfCardsplaced++;
    }
    private void visualizeCommonObjective() throws IOException {
        sendMessageToServer("visualizeCommonObjectiveCards");
        System.out.println("Common Objective Cards:\n");
        System.out.println(in.readLine());//first common card
        System.out.println(in.readLine());//second common card

        System.out.println("Carte lette correttamente");
    }
    private void visualizeSecretObjective() throws IOException {
        sendMessageToServer("secret");
        System.out.println("Hai scelto di visualizzare la tua carta obiettivo segreta\n");
        String result= in.readLine();
        System.out.println(result);
        System.out.println("\n");
        System.out.println("Questa è la tua carta obiettivo!");
    }
    private void showBoard() throws IOException {
        sendMessageToServer("showBoard");
        System.out.println("Hai selezionato la tua board:\n");
        System.out.print("////////////////////////////////// INIZIO BOARD //////////////////////////////////////////");
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
        System.out.println("Hai scelto di visualizzare i tuoi attuali punti!\n");
        String result= in.readLine();
        System.out.println("I tuoi punti attualmente sono: " + result);
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
        System.out.println("You chose to draw a card!\n");
        showWell();
        String pesca;
        do {
            System.out.println("where to draw the card from?\n" +
                    "->deck\n" +
                    "->well");
            pesca = stdin.readLine().toLowerCase();
            if (pesca.equals("deck")) {
                drawCardFromDeck();
            }
            else if (pesca.equals("well")) {
                drawCardFromWell();
            }
            else System.out.println("write 'deck' or 'well'");
        }while (!pesca.equals("well") && !pesca.equals("deck"));
    }
    private void drawCardFromDeck() throws IOException {
        System.out.println("where to draw the card from?\n" +
                "->Resource\n" +
                "->Gold");
        String pesca;
        do{
            pesca = stdin.readLine().toLowerCase();
            if (pesca.equals("resource")) {
                sendMessageToServer("drawCardFromResourceDeck");
                drawCardFromResourceDeck();
            }
            else if (pesca.equals("gold")) {
                sendMessageToServer("drawCardFromGoldDeck");
                drawCardFromGoldDeck();
            }
            else System.out.println("write 'resource' or 'gold'");

        }while (!pesca.equals("resource") && !pesca.equals("gold"));
    }

    private void drawCardFromResourceDeck() {
    }

    private void drawCardFromGoldDeck() {
    }

    private void drawCardFromWell() throws IOException {
        /*
        DEVO CAOIRE PERCHE CONTINUA A STAMPARE è IL TUO TURNO
        PROBABILMENTE è IL WHILE DEL SERVER IL PROBLEMA
         */
        System.out.println("Which card from the well do you want to draw?\n");
        sendMessageToServer("showWell");
        System.out.println("Well:\n------------------------------------------------------------------------------------------");
        System.out.println("select 'well-0' for"+in.readLine());//prima carta nel pozzo
        System.out.println("select 'well-1' for"+in.readLine());//seconda carta nel pozzo
        System.out.println("select 'well-2' for"+in.readLine());//terza carta nel pozzo
        System.out.println("select 'well-3' for"+in.readLine());//quarta carta nel pozzo
        in.readLine();//spazio
        System.out.println("------------------------------------------------------------------------------------------");
        String selectedCard;
        do{
            selectedCard= stdin.readLine().toLowerCase();
            if(!rightResponseWell(selectedCard)) {
                System.out.println("wrong choice, try again");
            }
        }while(!rightResponseWell(selectedCard));
        sendMessageToServer(selectedCard);

        //ora gestisco le risposte del server
        String esito = in.readLine();
        if(Objects.equals(esito, String.valueOf(true))) {
            System.out.println("operation performed correctly");
            showCards();
            showWell();
        }
        else{
            System.out.println("operation performed incorrectly");
            showCards();
            showWell();
        }
    }

    private boolean rightResponseWell(String selectedCard) {
        if(selectedCard.equals("well-0") || selectedCard.equals("well-1") || selectedCard.equals("well-2") || selectedCard.equals("well-3")) {
            return true;
        }
        return false;
    }

    private void quit(){
        sendMessageToServer("quit");
        System.out.println("Hai scelto di quittare!\n");
    }
    private void runEndTurn(){
        sendMessageToServer("endTurn");
        System.out.println("Hai scelto di concludere il tuo turno. La mano passa al gicatore successivo");



    }
    /*private int checkIfTheCardExist(int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);          //SELECTEDCARDFROMTHEDECK IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCardFromTheDeck == null) {                         //CHECKING IF THE CARD EXISTS, IN CASE RETURN
            return 0;
        }
        return cardIndex;
    }*/
    private boolean isTheCardGold(Card selectedCard)
    {
        if (selectedCard instanceof GoldCard) {
            return clientView.getBoard().placeGoldCard(((GoldCard) selectedCard).getRequirementsForPlacing());
        }
        else {
            return false;
        }
    }
    private void showingToTheCurrentPlayerCardsOnTheBoard(List<Card> cardsPlayerCanChooseFrom){
        System.out.println("Cards on the board are:");                          //PRINTING THE CARDS ON THE BOARD
        for (int i = 0; i < cardsPlayerCanChooseFrom.size(); i++) {
            Card card = cardsPlayerCanChooseFrom.get(i);
            System.out.println((i + 1) + ". " + card);
        }
    }
    private Card selectTheCardFromTheBoard(List<Card> cardsPlayerCanChooseFrom, Scanner scanner){
        System.out.print("Select a card on your board you want to place the card from your deck on: ");
        int selectedCardIndex = scanner.nextInt();
        try{
            if (selectedCardIndex < 1 || selectedCardIndex > cardsPlayerCanChooseFrom.size()) {
                throw new IndexOutOfBoundsException("Not a valid index");}
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            return null;
        }
        catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }
        return cardsPlayerCanChooseFrom.get(selectedCardIndex - 1);
    }
    private List<Corner> creatingCorners(Card cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER
        availableCorners.add(cardPlayerChoose.getTL());
        availableCorners.add(cardPlayerChoose.getTR());
        availableCorners.add(cardPlayerChoose.getBL());
        availableCorners.add(cardPlayerChoose.getBR());
        return availableCorners;
    }
    private void cardChosenIsTheInitialcard(Card initialCard,List<Corner> availableCorners )
    {
        List<Corner> initialCardCorners = new ArrayList<>();       //THEN ELIMINATING NOTTOBEPLACEDON CORNERS FROM PLAYER DISPLAYER &&CORNER WHOSE VALUE IS 0
        initialCardCorners.add(initialCard.getTL());
        initialCardCorners.add(initialCard.getTR());
        initialCardCorners.add(initialCard.getBL());
        initialCardCorners.add(initialCard.getBR());
        for (int i = initialCardCorners.size() - 1; i >= 0; i--) {
            if (initialCardCorners.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || initialCardCorners.get(i).getValueCounter() == 0) {
                availableCorners.remove(i);
            }
        }
    }
    private void cardChosenIsNotTheInitialcard(List<Corner> availableCorners, List<Corner> corner)
    {
        for (int i = corner.size() - 1; i >= 0; i--) {
            if (corner.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || corner.get(i).getValueCounter() == 0) { //SAMECHECK
                availableCorners.remove(i);
                corner.remove(i);
            }
        }
    }
    public String chooseCard(int index) {
        try{
            if (index < 0 || index >= clientView.getPlayerCards().size()) {
                throw new IndexOutOfBoundsException("Not a valid index");
            }
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println(e.getMessage()); //INDEX GOES FROM 1 TO 3
        }

        return clientView.getPlayerStringCards().get(index);
    }  //METHOD TO CHOOSE WHICH CARD THE PLAYER WANTS TO PLACE ON THE BOARD





    private String freeCornersOfTheSelectedCard(List<Corner> availableCorners, Card cardPlayerChoose, Scanner scanner){
        Map<Corner, String> cornerLabels = new HashMap<>();      //PUTTING THE CORRECT CORNERLABEL TO THE CORRECT CORNER
        cornerLabels.put(cardPlayerChoose.getTL(), "TL");
        cornerLabels.put(cardPlayerChoose.getTR(), "TR");
        cornerLabels.put(cardPlayerChoose.getBL(), "BL");
        cornerLabels.put(cardPlayerChoose.getBR(), "BR");

        System.out.println("Free Corners of the selected card "); //DISPLAYING THE POSSIBLE CORNERS
        for (int i = 0; i < availableCorners.size(); i++) {
            Corner corner = availableCorners.get(i);
            String cornerLabel = cornerLabels.get(corner);
            System.out.println((i + 1) + ". " + corner + " -> " + cornerLabel + "|Please press " +cornerLabel + " to select the corner ");
        }
        System.out.print("Choose the corner you want to place the card on: ");
        String selectedCorner = scanner.next().toUpperCase();
        try{
            if (!selectedCorner.equals("TL") && !selectedCorner.equals("TR") && !selectedCorner.equals("BL") && !selectedCorner.equals("BR")) {
                throw new InvalidCornerException("Invalid corner selection.");
            }
        }  catch (InvalidCornerException e){
            System.out.println(e.getMessage());
            return null;
        }
        return selectedCorner;

    }

}

