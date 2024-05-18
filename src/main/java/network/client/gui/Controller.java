package network.client.gui;

import model.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {

    BufferedReader in;
    PrintWriter out;
    private int gameSize;
    private String currentPlayerNickname;

    public Controller(BufferedReader in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected) throws IOException {
        System.out.println("indexToBePlacedOn"+indexCardToBePlacedOn + " indexToPlace" + indexCardToPlace + " corner" + cornerSelected);
        if(indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null){ //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
            return;
        }
        System.out.println("il cliente ha detto playcard");
        out.println("playCard"); //sends to server the message to start the playCard method
        try {
            out.println(indexCardToPlace); //267
            System.out.println(in.readLine()); //puoi procedere
            //Vuoi girare la tua carta?
            out.println(2); //Non voglio girare la mia carta
            System.out.println("non voglio girare la mia carta");
            out.println(indexCardToBePlacedOn-1);
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
            out.println("TL");
            System.out.println(in.readLine()); //carta placed
            String typeCard = in.readLine();
            String isBack = in.readLine();
            String cordinateTl = in.readLine();
            System.out.println(typeCard);
            System.out.println(isBack);
            System.out.println(cordinateTl);
            //player.getClientView().addCardOnTheBoard((numeroCarte+1)+"->"+typeCard+": "+cordinateTl+" "+ isBack);
            //player.getClientView().setNumOfCardsOnTheBoard(numeroCarte+1);
        } catch (IOException e) {
            throw new RuntimeException(e);
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


    private void showWell() throws IOException {
        out.println("showWell");
        System.out.println("Common Well:\n------------------------------------------------------------------------------------------");
        System.out.println(in.readLine());//prima carta nel pozzo
        System.out.println(in.readLine());//seconda carta nel pozzo
        System.out.println(in.readLine());//terza carta nel pozzo
        System.out.println(in.readLine());//quarta carta nel pozzo
        in.readLine();//spazio
        System.out.println("------------------------------------------------------------------------------------------");

    }


    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public String showSpecificSeed() throws IOException {
        out.println("showYourSpecificSeed");
        return in.readLine();
    }

    public String showPoints() throws IOException {
        out.println("showPoints");
        return in.readLine();
    }

    public String endTurn() throws IOException {
        out.println("endTurn");
        System.out.println("Your turn ended");
        String currentPlayerNickname = in.readLine();
        System.out.println(currentPlayerNickname);
        String update = in.readLine();
        System.out.println(update);
        out.flush();
        return currentPlayerNickname;
    }

    public void showBoards() throws IOException {
        //è da implementare diversamente da gui, però non so come
    }

    private boolean wrongChoice(String selectedCard) {
        int num = Integer.parseInt(selectedCard);
        return num < 0 || num > 3;
    }
    private void receivingAndPrintingCards() throws IOException {
        String firstCard = in.readLine(); //Reading all three cards
        String secondCard = in.readLine();
        String thirdCard = in.readLine();
        in.readLine();//the space

    }
    public void waitForTurn(String playerNickname) throws IOException {
        String message;
        while (!(message = in.readLine()).equals(playerNickname)) {
            // Scarta i messaggi ricevuti finché non è il turno del giocatore
            System.out.println("Received message while waiting for turn: " + message);
        }
        System.out.println("It's now " + playerNickname + "'s turn");
    }

}
