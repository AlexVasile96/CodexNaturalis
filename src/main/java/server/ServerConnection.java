package server;
import exceptions.OperationCancelledException;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.List;

public class ServerConnection implements Runnable {
    private static boolean setsize= false;
    private Socket socket;
    private ClientView clientView;
    private BufferedReader in;
    private BufferedReader stdin;
    private PrintWriter out;


    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
            this.clientView=clientView;
            this.socket = server;
            this.in= new BufferedReader(new InputStreamReader(socket.getInputStream())); //ricevere dati dal server
            this.out= new PrintWriter(socket.getOutputStream(), true);
            this.stdin= new BufferedReader(new InputStreamReader(System.in)); //scanner, mi serve per scrivere
        }

    @Override
    public void run() {
        String command; //inizializzazimone della stringa
        try {
            System.out.println("Benvenuto!Sono il server! Scrivere una qualsiasi stringa per iniziare la conversazione\n");
            while (true) {
                try {
                    System.out.print(">");
                    command=stdin.readLine();
                    if (clientView.getUserName() == null) { //If client hasn't made the login yet, he has to log first.
                        out.println(command);
                        loginPlayer();
                        respondToNumberOfPLayers();
                    }
                    else    {                                     //If client has made the login, he can start asking for inputs if it's his turn
                        out.println(command);
                        actionsInput(command);
                        }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                            }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
                                                }
    }

    private void loginPlayer() throws IOException, InterruptedException { //LOGIN METHOD
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse); //Inserisci il tuo nome per favore
        System.out.println(">");
        String loginName = stdin.readLine();
        out.println(loginName);
        String risposta = in.readLine();
        System.out.println("Server says: " + risposta); //Login effettuato con successo
        String okay = in.readLine();
        System.out.println("Server says: " + okay);   //Sarai messo in sala d'attesa
        System.out.println("sei in attesa");
        String ascolto = in.readLine();
        System.out.println("Server says: " + ascolto);
        ordinePlayer(in);
        clientView.setUserName(loginName);                      //UPDATING CLIENT VIEW
    }

    private void respondToNumberOfPLayers() throws IOException {
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse);
        if(serverResponse.equals("NO")){
          return;
        }
        System.out.println(">");
        String messaggio= stdin.readLine();
        int size = Integer.parseInt(messaggio);
        out.println(size);
        System.out.println(size);
    }


    public static void ordinePlayer(BufferedReader input) throws IOException, InterruptedException {
        Boolean uscitaCheck = Boolean.valueOf(input.readLine());
        while (uscitaCheck != false) {
            String ordinePlayer = input.readLine();
            System.out.println("Server says: " + ordinePlayer);
            uscitaCheck = Boolean.valueOf(input.readLine());
        }
    }


    private void actionsInput(String userInput) throws IOException { //GAME STARTED
        try {
            switch (userInput) {
                //Display the player's commands options
                case "help" -> printHelp();

                //Display the game's status
                case "status" -> printStatus();

                //Display the list of available actions for the player in the current turn phase
                case "actions" -> printActions();

                //The following methods are used to run game actions
                case "showYourCardDeck", "0" -> showCards();
                case "playCardFromYourHand", "1" -> chosenHandCard();
                case "visualizeCommonObjectiveCards", "2" -> visualizeCommonObjective();
                case "visualizeSecretObjectiveCard", "3" -> visualizeSecretObjective();
                case "showBoard", "4" -> showBoard();
                case "showPoints", "5" -> showPoints();
                case "drawCardFromDeck", "6" -> drawCardFromDeck();
                case "drawCardFromWell", "7" -> drawCardFromWell();
                case "endTurn", "8" -> runEndTurn();//run
                default -> {
                    System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
                }

            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void printHelp()
    {
        System.out.println("Commands:\n");
        System.out.println(
                "Supported commands are: " +
                        "\n" +
                        "\n- 'status': show the player who is currently taking their turn and the turn phase" +
                        "\n- 'show': display a specific game element" +
                        "\n- 'actions': display all currently allowed game actions" +
                        "\n");
    }

    private void printActions() throws IOException {
        System.out.println(
                "Supported commands:" +
                        "\n- If you type-> 'showYourCardDeck / 0 ': display player's cards" +
                        "\n- If you type-> 'playCardFromYourHand /1': select the card you want to place from your hand" +
                        "\n- If you type->  'visualizeCommonObjectiveCards /2': visualize the common objective cards" +
                        "\n- If you type->  'visualizeSecretObjectiveCard /3': visualize your secret objective card" +
                        "\n- If you type->  'showBoard /4':print your board" +
                        "\n- If you type->  'showPoints /5': show your points" +
                        "\n- If you type->  'drawCardFromDeck /6': draw a card from the resource/gold deck" +
                        "\n- If you type->  'drawCardFromWell /7': draw a card from the well" +
                        "\n- If you type->  'endTurn /8': end your turn"
        );
    }
    private void printStatus(){
        System.out.println("\n"+clientView.getGame());
    }
    private void showCards(){}
    private void chosenHandCard(){}
    private void visualizeCommonObjective(){}
    private void visualizeSecretObjective(){}
    private void showBoard(){}
    private void showPoints(){}
    private void drawCardFromDeck(){}
    private void drawCardFromWell(){}
    private void runEndTurn(){}

}

