package server;
import exceptions.OperationCancelledException;
import model.game.*;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class ServerConnection implements Runnable {
    private static int index=0;
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
                        sendMessageToServer(clientView.getUserName());
                        isMyTurn = in.readLine();                       //è il tuo turno
                        System.out.println(isMyTurn);                   //viene stampato è il tuo turno
                        //if(!myTurn) showCards();
                        if (isMyTurn != null && isMyTurn.equals("è il tuo turno!!")) {
                            myTurn = true;
                        }
                        else myTurn=false;
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
                                wait();
                                stdin.readLine();
                                System.out.println("attendi...");
                                isMyTurn=in.readLine();
                                if (isMyTurn != null && isMyTurn.equals(player.getNickName())) {
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
       synchronized (this)
       {
           clientView.setIndex(index);
           index++;
        }

        chooseYourDotColor(player);
        chooseNumberOfPlayers();

    }

    public synchronized void sendMessageToServer(String message) {
        out.println(message);
    }     //metodo per mandare un singolo messaggio al server

    public String readMessageFromUser() throws IOException {
        return stdin.readLine();
    }

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
                case "showdeck", "0" -> showCards();
                case "playcard", "1" -> chosenHandCard();
                case "common", "2" -> visualizeCommonObjective();
                case "secret", "3" -> visualizeSecretObjective();
                case "board", "4" -> showBoard();
                case "points", "5" -> showPoints();
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
                        . if you type ->  'quit /10': esci dal gioco\n"""
        );
    }
    private void printStatus(){
        sendMessageToServer("status");
        System.out.println("\n"+clientView.toString());
    }

    private void initializeEachClientCards(){
        //DA IMPLEMENTARE E DA LEVARE IL METODO DA SHOWCARDS, ATM PER FARE IN MODO CHE TUTTO FUNZIONI DEVE ESSERE
        //PRIMA INVOCATO IL METODO SHOWCARDS O LE CARTE NON VENGONO SALVATE
    }


    private void showCards() throws IOException {
        sendMessageToServer("showYourCardDeck");
        System.out.println("Il tuo mazzo:" );
        receivingAndPrintingCards();
        System.out.println("Carte lette correttamente");
    }
    private void receivingAndPrintingCards() throws IOException {
        List<String> cards = new ArrayList<>();
        cards.add(in.readLine());
        cards.add(in.readLine());
        cards.add(in.readLine());
        in.readLine();//è solo lo spazio
        updatingView(cards);
        for (String s: player.getClientView().getPlayerStringCards()){
            System.out.println(s);
        }
    }
    private void updatingView(List<String> cards){
        if(player.getClientView().getPlayerStringCards().isEmpty()) {
            for (String newCard : cards){
                player.getClientView().getPlayerStringCards().add(newCard);
            }
            return;
        }
        boolean present = false;
        for (String newCard : cards){
            for (String viewCard : player.getClientView().getPlayerStringCards()){
                if (newCard.equals(viewCard)){present = true; break;}
            }
            if (!present){player.getClientView().getPlayerStringCards().add(newCard);}
        }
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
        String cornerChosen= stdin.readLine().toUpperCase();
        System.out.println("Corner scelto correttamente!");
        out.println(cornerChosen);
        String ultimo= in.readLine();
        System.out.println(ultimo);
        numberOfCardsplaced++;
        //da gestire nel caso l'operazione fallisca
        //player.getClientView().getPlayerStringCards().remove(size-1);
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
        System.out.println("Hai selezionato la tua board:");
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
        in.readLine();
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
        sendMessageToServer("drawCardFromWell");
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
        in.readLine();//butto via è il tuo turno
        if(result.equals("operation performed correctly")) {
            System.out.println("Operation 'Draw card from Well' performed correctly");
            sendMessageToServer("showYourCardDeck");
            System.out.println("Il tuo mazzo:" );
            System.out.println("--------------------------------------------------------------------------------------");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            in.readLine();//butto via è il tuo turno
            showWell();
        }
        else{
            System.out.println("operation performed incorrectly");
            System.out.println("Server says: "+ result);
            System.out.println("Il tuo mazzo:" );
            System.out.println("--------------------------------------------------------------------------------------");
            sendMessageToServer("showYourCardDeck");
            receivingAndPrintingCards();
            System.out.println("--------------------------------------------------------------------------------------");
            in.readLine();//butto via è il tuo turno
            showWell();
        }
    }

    private boolean sceltaNonIdonea(String selectedCard) {
        int num = Integer.parseInt(selectedCard);
        return num < 0 || num > 3;
    }

    private void quit(){
        sendMessageToServer("quit");
        System.out.println("Hai scelto di quittare!\n");
    }
    private void runEndTurn(){
        sendMessageToServer("endTurn");
        System.out.println("Hai scelto di concludere il tuo turno. La mano passa al gicatore successivo");
            notifyAll();

    }

}

