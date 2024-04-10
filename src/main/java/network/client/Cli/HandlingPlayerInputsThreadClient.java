package network.client.Cli;

import Exceptions.OperationCancelledException;
import network.message.MessageSender;
import network.message.MessagesEnum;
import view.ClientView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HandlingPlayerInputsThreadClient implements Runnable {

    private BufferedReader stdIn;
    private PrintWriter out;
    private ClientView clientView;
    private boolean doClose;
    private Gson gson;

    public HandlingPlayerInputsThreadClient(BufferedReader stdIn, PrintWriter out, ClientView clientView) { //Costructor
        this.stdIn = stdIn;
        this.out = out;
        this.clientView = clientView;
        this.doClose = false;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        String userInput;
        while (!doClose) {
            try {
                userInput = stdIn.readLine();
                if (clientView.getUsername() == null) {
                    sendMessageToServer(MessagesEnum.USERNAME, userInput);
                }else
                {
                    initializePlayer(userInput);
                    actionsInput(userInput); //switch case gioco iniziato
                }
            } catch (IOException eE) {
                System.out.println("IO exception");
            }
        }

    }

    public void sendMessageToServer(MessagesEnum type, String content) {
        out.println(gson.toJson(new MessageSender(type, content)));
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


    private void actionsInput(String userInput) { //GAME STARTED
        try {
            switch (userInput) {

                //Display the player's commands options
                case "help" -> printHelp();

                //Display the game's status
                case "status" -> printStatus();

                //Display one of the game's elements
                case "show" -> printShow();

                //Display the list of available actions for the player in the current turn phase
                case "actions" -> printActions();

                //The following methods are used to run game actions
                case "showCards" -> showCards();


                case "endTurn", "18" -> runEndTurn();

                default -> {
                    if (clientView.getGame() == null)
                        out.println(userInput);
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

    private void printShow(){}
    private void printActions(){}
    private void printStatus(){}
    private void runEndTurn(){}
    private void showCards() {
        sendMessageToServer(MessagesEnum.GET_CARDS, "");
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


    public void doClose() {
        doClose = true;
        System.out.println("Server connection lost, press any key to terminate.");
    }

}
