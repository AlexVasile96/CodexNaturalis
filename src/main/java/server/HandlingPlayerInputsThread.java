package server;

import com.google.gson.Gson;
import exceptions.OperationCancelledException;
import model.game.Board;
import model.game.Dot;
import model.game.Player;
import view.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HandlingPlayerInputsThread implements Runnable {
    public BufferedReader stdIn;
    public PrintWriter out;
    private ClientView clientView;
    private List<HandlingPlayerInputsThread> clients;
    private boolean isGameStarted;
    private boolean doClose;
    private Gson gson;
    private List<Player> playersList = new ArrayList<>();
    private Socket clientSocket;
    private Integer x;

    public HandlingPlayerInputsThread(Socket socket, List<Player> playersinTheGame, List<HandlingPlayerInputsThread> clients) throws IOException { //Costructor
        this.clientSocket= socket;
        stdIn= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out= new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientView = clientView;
        this.doClose = false;
        this.gson = new Gson();
        this.playersList= playersinTheGame;
        this.x=0;
        this.isGameStarted = false;
        this.clients=clients;

    }


    //NELLA RUN
    /*
    * CONTROLLO-> IF CLIENTVIEW.GETUSERNAME==NULL->LOGIN          LOGIN
    * CLIENT IN ATTESA
    *
    *
    *
    *
    *
    * */








    @Override
    public void run() {
        try {
            while (true) {
                //IF NOT LOGIN->LOGIN
                //IF ACTIONINPUTS
                System.out.println("Siamo nella run");
                Board board = new Board(50, 50);
                String firstMessag = stdIn.readLine();
                System.out.println("Il client ha detto " + firstMessag);
                out.println("Ciao! Devi fare il login. Inserisci il tuo nome per favore!");
                String request = stdIn.readLine();
                System.out.println("il nome del login è: " + request);
                out.println("Login effettuato con successo");
                Player player = new Player(request, 0, Dot.BLACK, board);
                playersList.add(player);
                System.out.println(player);
                out.println("Sarai messo in sala d'attesa:");
                if(playersList.size()<2) {
                    inattesa();
                    //stampa punteggio giocatori *da spostare
                    for(Player p: playersList) {
                    p.visualizePlayerScore();
                    }
                }
                notifyGameStart();
                //startGame();


            }
        } catch (IOException e) {
            System.err.println("Io exception client handler");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
            try {
                stdIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startGame() {
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

    public synchronized void inattesa() throws InterruptedException {
            System.out.println("In attesa di altri giocatori");
            while (playersList.size()!=2) {
                {
                    wait(10000);
                }
            }
            notifyAll();
            return;
        } //metodo che mette in attesa i client fino a che non si è raggiunto il numero voluto di giocatori

    public synchronized void notifyGameStart() {
        if (!isGameStarted) {
            isGameStarted = true;
            for (HandlingPlayerInputsThread thread : ServerMain.getClients()) {
                thread.sendMessageToClient("Il gioco è iniziato!");
                int i=1;
                for (Player p : playersList) {
                    thread.sendBooleanToClient(true);
                    thread.sendMessageToClient("il "+i+"giocatore è: " + p.getNickName());
                    i++;
                }
                thread.sendBooleanToClient(false);
            }
        }
    }

   //metodo per mandare un singolo messaggio al client
    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }

    //metodo per mandare un singolo boolean al client
    public synchronized boolean sendBooleanToClient(boolean value) {
        out.println(value);
        return value;
    }

    public synchronized void sendMessageToAllClients(String message)
    {
        for(HandlingPlayerInputsThread client: clients)
        {
            client.out.println(message);
        }
    }

}










    /*






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
    private void showCards() throws IOException {
        sendMessageToServer(MessagesEnum.GET_CARDS, "");
    }
    private void chosenHandCard() throws IOException {
        sendMessageToServer(MessagesEnum.CHOSEN_CARD_FROM_HAND, "");
    }
    private void selectedBoardCard() throws IOException {
        sendMessageToServer(MessagesEnum.SELECTED_CARD, "");
    }
    private void selectedCorner() throws IOException {
        sendMessageToServer(MessagesEnum.SELECTED_CORNER, "");
    }
    private void placeSelectedCard() throws IOException {
        sendMessageToServer(MessagesEnum.PLACE_SELECTED_CARD, "");
    }
    private void visualizeCommonObjective() throws IOException {
        sendMessageToServer(MessagesEnum.COMMON_OBJECTIVE_CARDS, "");
    }
    private void visualizeSecretObjective() throws IOException {
        sendMessageToServer(MessagesEnum.SECRET_OBJECTIVE_CARD,"");
    }
    private void showBoard() throws IOException {
        sendMessageToServer(MessagesEnum.PLAYER_BOARD,"");
    }
    private void showPoints() throws IOException {
        sendMessageToServer(MessagesEnum.PLAYER_SCORE,"");
    }




    //pesca carta risorsa
    //pesca carta gold
    //piazza una carta-> playcard->scegli angolo->fine turno
    //visualizza le tue carte
    //visualizza obiettivi comuni
    //visualizza obiettivo segreto
    //visualizza la board
    //visualizzza il tuo punteggio
    //visualizza board degli altri


    private void doClose() {
        doClose = true;
        System.out.println("Server connection lost, press any key to terminate.");
    }*/
