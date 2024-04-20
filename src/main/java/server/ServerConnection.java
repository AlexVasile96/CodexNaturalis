package server;
import exceptions.OperationCancelledException;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.List;

public class ServerConnection implements Runnable {
    private Socket socket;
    private ClientView clientView;
    private BufferedReader in;
    private BufferedReader stdin;
    private PrintWriter out;


    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
        this.clientView=clientView;
        this.socket = server;
       this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
       this.out= new PrintWriter(socket.getOutputStream(), true);
       this.stdin= new BufferedReader(new InputStreamReader(System.in)); //scanner
    }

    @Override //Handling inputs from the server, just waiting for the server to say something
    public void run() {
        String command=null; //inizializzazimone della stringa
        try {
            System.out.println("Benvenuto!Sono il server\n");
            while (true) {
                try {
                    System.out.println(">");
                    command=stdin.readLine(); ///ciao
                    if (clientView.getUserName() == null) {
                        out.println(command);
                        loginPlayer();
                        //break;
                    }
                    else{
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

    private void loginPlayer() throws IOException, InterruptedException {
        String serverResponse = in.readLine();
        System.out.println("Server says: " + serverResponse);
        System.out.println(">");
        String loginName = stdin.readLine();
        out.println(loginName);
        String risposta = in.readLine();
        System.out.println("Server says: " + risposta);
        String okay = in.readLine();
        System.out.println("Server says: " + okay);
        System.out.println("sei in attesa");
        String ascolto = in.readLine();
        System.out.println("Server says: " + ascolto);
        ordinePlayer(socket, stdin, in);
        clientView.setUserName(loginName);
    }

    public static void ordinePlayer(Socket clientSocket, BufferedReader stdIn, BufferedReader input) throws IOException, InterruptedException {
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
                //case "status" -> printStatus();

                //Display the list of available actions for the player in the current turn phase
                //case "actions" -> printActions();

                //The following methods are used to run game actions
                //case "showCards", "0" -> showCards();//run

                //case "chooseCardFromHand", "1" -> chosenHandCard();//run

                //case "selectCardFromBoard", "2" -> selectedBoardCard();//run

                //case "selectCornerFromChosenCard", "3" -> selectedCorner();//run

                //case "placeCard", "4" -> placeSelectedCard();//run

                //case "visualizeCommonObjectiveCards", "5" -> visualizeCommonObjective();//run

                //case "visualizeSecretObjectiveCard", "6" -> visualizeSecretObjective();//run

                //case "showBoard", "7" -> showBoard();//run

                //case "showPoints", "8" -> showPoints();//run

                //case "endTurn", "9" -> runEndTurn();//run

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
  /*
    private void printActions() throws IOException {
        System.out.println(
                "Supported commands:" +
                        "\n- 'Cards': display player's cards" +
                        "\n- 'HandCard': select the card you want to place from your hand" +
                        "\n- 'BoardCard': select the card from the board you want to play your card to" +
                        "\n- 'Corner': select the corner you want to cover with your card" +
                        "\n- 'Place': place the card" +
                        "\n- 'Objective': visualize the common objective cards" +
                        "\n- 'Secret': visualize your secret objective card" +
                        "\n- 'Board': visualize your entire board" +
                        "\n- 'Points': visualize your points"
        );
    }
    private void printStatus(){
        System.out.println("\n"+clientView.getGame());
    }
    private void runEndTurn(){}

    private void doClose() {
        doClose = true;
        System.out.println("Server connection lost, press any key to terminate.");
    }*/

}

