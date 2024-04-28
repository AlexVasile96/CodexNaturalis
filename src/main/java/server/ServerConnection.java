package server;
import exceptions.InvalidCornerException;
import exceptions.OperationCancelledException;
import model.card.Card;
import model.game.Corner;
import model.game.Dot;
import model.game.Player;
import model.game.SpecificSeed;
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


    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
            this.clientView=clientView;
            this.socket = server;
            this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));    //ricevere dati dal server
            this.out= new PrintWriter(socket.getOutputStream(), true);
            this.stdin= new BufferedReader(new InputStreamReader(System.in));               //scanner, mi serve per scrivere
        }

    @Override
    public void run() {
        String command;
        try {
            System.out.println("Benvenuto!Sono il server! Scrivere una qualsiasi stringa per iniziare la conversazione\n");
            while (true) {
                try {
                    System.out.print(">");
                    command=stdin.readLine();                           //il client scrive un messaggio
                    if (clientView.getUserName() == null) {             //If client hasn't made the login yet, he has to log first.
                        sendMessageToServer(command);
                        loginPlayer();                                  //Actual Login
                        assigningSecretCard();                          //Choosing the secret Card
                        takingTheInitialCard();
                    }
                    else {//If client has made the login, he can start asking for inputs if it's his turn
                        String isMyTurn = in.readLine();                //è il tuo turno
                        sendMessageToServer(command);
                        System.out.println(isMyTurn);                   //viene stampato è il tuo turno
                        if (isMyTurn.equals("è il tuo turno!!")) {
                            /*System.out.println("Menu:\n" +
                                    "1.  help - printHelp()\n" +
                                    "2.  status - printStatus()\n" +
                                    "3.  actions - printActions()\n" +
                                    "4.  showYourCardDeck, 0 - showCards()\n" +
                                    "5.  playCard, 1 - chosenHandCard()\n" +
                                    "6.  visualizeCommonObjectiveCards, 2 - visualizeCommonObjective()\n" +
                                    "7.  secret, 3 - visualizeSecretObjective()\n" +
                                    "8.  showBoard, 4 - showBoard()\n" +
                                    "9.  showPoints, 5 - showPoints()\n" +
                                    "10. drawResourceCardFromDeck, 6 - drawResourceCardFromDeck()\n" +
                                    "11. drawGoldCardFromDeck, 7 - drawGoldCardFromDeck()\n" +
                                    "12. drawCardFromWell, 8 - drawCardFromWell()\n" +
                                    "13. endTurn, 9 - runEndTurn()\n" +
                                    "14. quit, 10 - quit()");*/
                                //mando showYourCardDeck
                            actionsInput(command);
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

    private synchronized void assigningSecretCard() throws IOException {
        String stringSecretCard= in.readLine();
        System.out.println("server says: " + stringSecretCard);
        System.out.println("selaziona la carta: 1-prima carta, 2-seconda carta");
        String intero= stdin.readLine();
        int size = Integer.parseInt(intero);
        out.println(size);
    }

    private void loginPlayer() throws IOException, InterruptedException { //LOGIN METHOD
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse); //Inserisci il tuo nome per favore
        System.out.println(">");
        String loginName = stdin.readLine();
        sendMessageToServer(loginName);
        String correctLogin = in.readLine();
        System.out.println("Server says: " + correctLogin); //Login effettuato con successo
        clientView.setUserName(loginName);                      //UPDATING CLIENT VIEW
        chooseYourDotColor();
        chooseNumberOfPlayers();

    }

    public synchronized void sendMessageToServer(String message) {
        out.println(message);
    }     //metodo per mandare un singolo messaggio al server

    private void chooseYourDotColor() throws IOException {
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
                case "showYourCardDeck", "0" -> showCards();
                case "playCardFromYourHand", "1" -> chosenHandCard();
                case "visualizeCommonObjectiveCards", "2" -> visualizeCommonObjective();
                case "secret", "3" -> visualizeSecretObjective();
                case "showBoard", "4" -> showBoard();
                case "showPoints", "5" -> showPoints();
                case "drawResourceCardFromDeck", "6" -> drawResourceCardFromDeck();
                case  "drawGoldCardFromDeck", "7"->drawGoldCardFromDeck();
                case "drawCardFromWell", "8" -> drawCardFromWell();
                case "endTurn", "9" -> runEndTurn();//run
                case "quit", "10" -> quit();
                default -> System.out.println("This command is not supported. Press 'help' for a list of all available commands.");

            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void printHelp() throws IOException {
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
        String serviceString=in.readLine();
        System.out.println(serviceString);
        System.out.println(
                """
                        Supported commands:
                        - If you type-> 'showYourCardDeck / 0 ': display player's cards
                        - If you type-> 'playCardFromYourHand /1': select the card you want to place from your hand
                        - If you type->  'common /2': visualize the common objective cards
                        - If you type->  'secret /3': visualize your secret objective card
                        - If you type->  'showBoard /4':print your board
                        - If you type->  'showPoints /5': show your points
                        - If you type->  'drawResourceCardFromDeck /6': draw a card from the resource deck
                        - If you type->  'drawGoldCardFromDeck /7': draw a card from the gold deck
                        - If you type->  'drawCardFromWell /8': draw a card from the well
                        - If you type->  'endTurn /9': end your turn
                        . if you type ->  'quit /10': esci dal gioco\n"""
        );
    }
    private void printStatus(){
        System.out.println("\n"+clientView.toString());
    }
    private void showCards() throws IOException {
        System.out.println("Il tuo mazzo:" );
        String firstCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String secondCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String thirdCard=in.readLine(); //Hai selezionato di vedere le tue carte
        String spazio=in.readLine(); //Hai selezionato di vedere le tue carte
        System.out.println(firstCard);
        System.out.println(secondCard);
        System.out.println(thirdCard);
        System.out.println(spazio);
        System.out.println("Carte lette correttamente");
    }

    private void chosenHandCard() throws IOException {
        System.out.println("Hai scelto di giocare una carta dal tuo deck!\n");
        for(Card cards: clientView.getPlayerCards())
        {
            System.out.println(cards);
        }
        System.out.println("Scegli quale carta vuoi piazzare sulla tua board!");
        String intero= stdin.readLine();
        int size = Integer.parseInt(intero);
        out.println(size);
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


    }
    private void visualizeCommonObjective(){
        System.out.println("Hai scelto di visualizzare gli obiettivi comuni a tutti!\n");
    }
    private void visualizeSecretObjective() throws IOException {
        System.out.println("Hai scelto di visualizzare la tua carta obiettivo segreta\n");
        String result= in.readLine();
        System.out.println(result);
        System.out.println("\n");
        System.out.println("Questa è la tua carta obiettivo!");
    }
    private void showBoard() throws IOException {
        System.out.println("Hai scelto di visualizzare la tua board!\n");
        String result= in.readLine();
        System.out.println(result);
    }
    private void showPoints() throws IOException {
        System.out.println("Hai scelto di visualizzare i tuoi attuali punti!\n");
        String result= in.readLine();
        System.out.println("I tuoi punti attualmente sono: " + result);
    }
    private void drawResourceCardFromDeck(){
        System.out.println("Hai scelto di pescare una carta dal deck Risorsa!\n");
    }
    private void drawGoldCardFromDeck(){
        System.out.println("Hai scelto di pescare una carta dal deck Gold\n");
    }
    private void drawCardFromWell(){
        System.out.println("Hai scelto di pescare una carta dal pozzo!\n");
    }
    private void quit(){
        System.out.println("Hai scelto di quittare!\n");
    }
    private void runEndTurn(){
        System.out.println("Hai scelto di concludere il tuo turno. La mano passa al gicatore successivo");



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

