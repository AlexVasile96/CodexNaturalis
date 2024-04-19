package server;

import com.google.gson.Gson;
import view.ClientView;

import java.io.*;
import java.net.Socket;

public class HandlingPlayerInputsThread implements Runnable {
    public BufferedReader stdIn;
    public PrintWriter out;
    private ClientView clientView;
    private boolean doClose;
    private Gson gson;
    private Socket clientSocket;

    public HandlingPlayerInputsThread(Socket socket) throws IOException { //Costructor
        this.clientSocket= socket;
        stdIn= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out= new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientView = clientView;
        this.doClose = false;
        this.gson = new Gson();

    }

    @Override
    public void run() {

        try {
            while(true) {
                String ciao = stdIn.readLine();
                System.out.println("Il client ha detto " + ciao);
                out.println("ciao! Devi fare il login");
                String request = stdIn.readLine();
                System.out.println("il nome del login è" + request);
                out.println("Login effettuato con successo");


            }
        } catch (IOException e) {
            System.err.println("Io exception client handler");
        }
        finally {
            out.close();
            try {
                stdIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }










        /*public void sendMessageToServer (MessagesEnum type, String content) throws IOException {
            out.writeObject(gson.toJson(new MessageSender(type, content)));
        }



    private void initializePlayer(String userInput)
    {
        //scelta del colore del dot
        //assegnazione carta iniziale
        //2 carte risorsa e 1 carta gold
        //scelta della carta obiettivo
        //inizializzazione dei punti(=0)
        //creazione della sua tabella dei punti -> piazzamento del dot
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
                case "showCards", "0" -> showCards();//run

                case "chooseCardFromHand", "1" -> chosenHandCard();//run

                case "selectCardFromBoard", "2" -> selectedBoardCard();//run

                case "selectCornerFromChosenCard", "3" -> selectedCorner();//run

                case "placeCard", "4" -> placeSelectedCard();//run

                case "visualizeCommonObjectiveCards", "5" -> visualizeCommonObjective();//run

                case "visualizeSecretObjectiveCard", "6" -> visualizeSecretObjective();//run

                case "showBoard", "7" -> showBoard();//run

                case "showPoints", "8" -> showPoints();//run

                case "endTurn", "9" -> runEndTurn();//run

                default -> {
                    if (clientView.getGame() == null)
                        out.writeObject(userInput);
                    else
                        System.out.println("This command is not supported. Press 'help' for a list of all available commands.");
                }

            }
        } catch (OperationCancelledException ex) {
            System.out.println(ex.getMessage());
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
                        "\n- 'Cards': display player's cards" +
                        "\n- 'HandCard': select the card you want to place from your hand" +
                        "\n- 'BoardCard': select the card from the board, that you want to add your card to" +
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

}
