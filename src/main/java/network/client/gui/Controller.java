package network.client.gui;

import javafx.stage.Stage;
import model.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Controller {

    BufferedReader in;
    PrintWriter out;
    private int gameSize;
    private String currentPlayerNickname;
    private Socket socket;
    public Controller(BufferedReader in, PrintWriter out, Socket socket) throws IOException {
        this.in = in;
        this.out = out;
        this.socket=socket;
        this.socket.setSoTimeout(10000); // Imposta il timeout a 10 secondi
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected, String isTheCardFlipped) throws IOException {
        System.out.println("indexToBePlacedOn "+indexCardToBePlacedOn + " indexToPlace " + indexCardToPlace + " corner " + cornerSelected);
        if(indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null){ //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
        }
        else{
        System.out.println("Client decided to place a card");
        out.println("playCard"); //sends to server the message to start the playCard method
        try {

            out.println(indexCardToPlace); //Sending to server the index of the card we want to place
            System.out.println(in.readLine()); //Server says we can continue
            if(isTheCardFlipped == null || isTheCardFlipped.equals("Front"))
            {
                out.println(2); //My card is not flipped
            }
            else{
                out.println(1); //My card is flipped
            }
            out.println(indexCardToBePlacedOn-1); //Sending to the server the index of the card on the board
            String[] angoli ={"TL","TR","BR","BL"};
            String [] validInputs = new String[4];
            boolean check = false;
            int size=0;
            String  messageFromServer = in.readLine();
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
            System.out.print("Choose the corner you want to place the card on: ");
            switch(cornerSelected) {
                case "TL":
                    out.println("TL");
                    break;
                case "TR":
                    out.println("TR");
                    break;
                case "BL":
                    out.println("BL");
                    break;
                case "BR":
                    out.println("BR");
                    break;
                default:
                    out.println("Invalid corner selected");
                    break;
            }

            System.out.println(in.readLine()); //card correctly placed
            String typeCard = in.readLine();
            String isBack = in.readLine();
            String coordinate = in.readLine();
            System.out.println(typeCard);
            System.out.println(isBack);
            System.out.println(coordinate);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
            }
        }
    }

    public void drawCard(String wellOrDeck, String selectedDeck, Integer indexSelectedCard) throws IOException {
        out.println("drawCard");
        System.out.println("You chose to draw a card!");
        if (wellOrDeck.equals("deck")) {
            out.println(wellOrDeck);
            if (selectedDeck.equals("resource")) {

                out.println("drawCardFromResourceDeck");
                in.readLine();
            } else if (selectedDeck.equals("gold")) {
                out.println("firstCardGoldGui");
                String cartaPescata = in.readLine();

                out.println("drawCardFromGoldDeck");
                in.readLine();
            }
        } else if (wellOrDeck.equals("well")) {
            out.println("well");
            out.println("showWell");
            System.out.println("Which card from the well do you want to draw?");
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("Select '0' for"+in.readLine()); //cards in the well
            System.out.println("Select '1' for"+in.readLine());
            System.out.println("Select '2' for"+in.readLine());
            System.out.println("Select '3' for"+in.readLine());
            in.readLine();//spazio
            System.out.println("------------------------------------------------------------------------------------------");
            out.println(indexSelectedCard);

            //ora gestisco le risposte del server
            String result = in.readLine();
            if(result.equals("operation performed correctly")) {

                System.out.println("Operation 'Draw card from Well' performed correctly");
                out.println("showYourCardDeck");
                System.out.println("Your Deck:" );
                System.out.println("--------------------------------------------------------------------------------------");
                receivingAndPrintingCards();
                System.out.println("--------------------------------------------------------------------------------------");
                showWell();
            }
            else{
                System.out.println("Operation failed");
            }
        }
    }

    public String firstCommon() throws IOException {
        out.println("firstCommon");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String secondCommon() throws IOException {
        out.println("secondCommon");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String secretCard() throws IOException {
        out.println("secret");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showWell() throws IOException {
        out.println("showWell");
        try {
            System.out.println("Common Well:\n------------------------------------------------------------------------------------------");
            System.out.println(in.readLine()); // prima carta nel pozzo
            System.out.println(in.readLine()); // seconda carta nel pozzo
            System.out.println(in.readLine()); // terza carta nel pozzo
            System.out.println(in.readLine()); // quarta carta nel pozzo
            in.readLine(); // spazio
            System.out.println("------------------------------------------------------------------------------------------");
        } catch (SocketTimeoutException e) {
            handleDisconnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void quit(Stage primaryStage) throws IOException {
        out.println("quit");
        try {
            System.out.println(in.readLine()); // ALL_CLIENTS_QUIT
            QuitScene quitScene = new QuitScene();
            quitScene.quit(primaryStage);
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (SocketTimeoutException e) {
            handleDisconnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public String showSpecificSeed() throws IOException {
        out.println("showYourSpecificSeed");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String showPoints() throws IOException {
        out.println("showPoints");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String endTurn() throws IOException {
        out.println("endTurn");
        try {
            System.out.println("Your turn ended");
            String currentPlayerNickname = in.readLine();
            System.out.println(currentPlayerNickname);
            String update = in.readLine();
            System.out.println(update);
            out.flush();
            return currentPlayerNickname;
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receivingAndPrintingCards() throws IOException {
        String firstCard = in.readLine(); //Reading all three cards
        String secondCard = in.readLine();
        String thirdCard = in.readLine();
        in.readLine();//the space

    }
    public void waitForTurn(String playerNickname, Stage primaryStage) throws IOException {
        String message;
        System.out.println("Sono entrato nella wait, aspetto il mio nome");
        while (!(message = in.readLine()).equals(playerNickname)) {
            System.out.println("Received message while waiting for turn: " + message);
            if(message.equals("ALL_CLIENTS_QUIT"))
            {
                quit(primaryStage);
            }
            else System.out.println("Not your turn, please wait");

        }
        System.out.println("It's now " + playerNickname + "'s turn");
        System.out.println("Time to play some Codex LESGO!");
    }
    private void handleDisconnection() {
        System.out.println("Client disconnected or crashed.");
        try {

            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
