package network.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller {

    BufferedReader in;
    PrintWriter out;

    public Controller(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected) throws IOException {
        if(indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null){ //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
            return;
        }
        out.println("playCard"); //sends to server the message to start the playCard method
        try {

            out.println(indexCardToPlace); //sends to server the id of the card you want your card to be placed on
            System.out.println(in.readLine());

            out.println(2); //non voglio girare la carta

            String messageFromServer = in.readLine();
            do{
                System.out.println(messageFromServer);
                messageFromServer= in.readLine();
            }while (!messageFromServer.equals("exit"));

            out.println(indexCardToBePlacedOn); //sends to server the index of the card you want your card to be placed on

            String avaiableCorners= in.readLine();
            do{
                System.out.println(avaiableCorners);
                avaiableCorners= in.readLine();
            } while(!avaiableCorners.equals("end"));


            out.println(cornerSelected); //sends to server the corner of the already placed cards you want your card to be placed on

            String ultimo = in.readLine();
            System.out.println(ultimo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String drawCard(String wellOrDeck, String selectedDeck, Integer indexCardPlaced) throws IOException {
        String carta1;
        String carta2;
        String carta3;

        out.println("drawCard");

        if(wellOrDeck.equals("deck")){
            if(selectedDeck.equals("resource")){
                out.println("drawCardFromResourceDeck");
                in.readLine();
                out.println("showYourCardDeck");

                carta1 = in.readLine();
                carta2 = in.readLine();
                carta3 = in.readLine();
                in.readLine(); //space
                if(indexCardPlaced == 0){
                    return carta1;
                }
                if(indexCardPlaced == 1){
                    return carta2;
                }
                if(indexCardPlaced == 2){
                    return carta3;
                }
            }
            } else if (selectedDeck.equals("gold")) {
                out.println("drawCardFromGoldDeck");
                in.readLine();
                out.println("showYourCardDeck");
                carta1 = in.readLine();
                carta2 = in.readLine();
                carta3 = in.readLine();
                in.readLine(); //space
            }
        }


    }

}
