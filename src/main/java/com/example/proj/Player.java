package com.example.proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String nickName;
    private int playerScore;
    private Dot dot;
    private Board board;
    private boolean isCardBack;
    private ArrayList <Card> playerCards;
    private ObjectiveCard secretChosenCard;
    public Player(String nickName, int playerScore, Dot dot, Board board){ //PLAYER CONSTRUCTOR
        this.nickName = nickName;
        this.playerScore = playerScore;
        this.dot = dot;
        this.board = board;
        this.playerCards = new ArrayList<Card>(3);
        this.isCardBack=false;
    }

    public boolean isCardBack() {
        return isCardBack;
    }

    public void setCardBack(boolean cardBack) {
        isCardBack = cardBack;
    }

    public ObjectiveCard getSecretChosenCard() {
        return secretChosenCard;
    }

    public void setSecretChosenCard(ObjectiveCard secretChosenCard) {
        this.secretChosenCard = secretChosenCard;
    }

    public void visualizePlayerCards(List<Card> cards){ //METHOD TO VISUALIZE PLAYER'S CARDS
        for(Card card: cards){
            System.out.println(card);
        }
    }
    public void drawResourceCard(ResourceDeck deck) {
        deck.drawCard(this);
    } //DRAWING RESOURCE CARD
    public void drawGoldCard(GoldDeck deck) {
        deck.drawCard(this);
    } //DRAWING GOLD CARD
    public Card chooseCard(int index) {
        if (index < 0 || index >= playerCards.size()) {
            System.out.println("Not valid index"); //INDEX GOES FROM 1 TO 3
            return null;
        }
        return playerCards.get(index);
    }  //METHOD TO CHOOSE WHICH CARD THE PLAYER WANTS TO PLACE ON THE BOARD

    public void chooseSecretCard(List <ObjectiveCard> secretCards){

        for (int i = 0; i < secretCards.size(); i++) {
            Card card = secretCards.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci il numero della carta obiettivo SEGRETA che vuoi pescare: ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > secretCards.size()) {
            System.out.println("Not valid index");
        }

        this.secretChosenCard=secretCards.get(selectedCardIndex - 1);
        System.out.println(secretChosenCard);
    }

    public void playCard(Board board, int cardIndex) { //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        Card selectedCard = chooseCard(cardIndex); //SELECTEDCARD IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCard == null) {
            return;
        }
        if(selectedCard instanceof GoldCard)
        {
          boolean gigi=   board.placeGoldCard((GoldCard) selectedCard,((GoldCard) selectedCard).getRequirementsForPlacing());
          if (gigi==false) return;
        }
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList(); //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM
        System.out.println("Cards on the board are:");
        for (int i = 0; i < cardsPlayerCanChooseFrom.size(); i++) {
            Card card = cardsPlayerCanChooseFrom.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select the card: ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > cardsPlayerCanChooseFrom.size()) {
            System.out.println("Not valid index");
            return;
        }
        Card cardPlayerChoose = cardsPlayerCanChooseFrom.get(selectedCardIndex - 1);
        System.out.println("Card correctly chosen"); //THE CARD CHOSEN IS OKAY
        List<Corner> omofobo= new ArrayList<>();
        omofobo.add(cardPlayerChoose.getTL());
        omofobo.add(cardPlayerChoose.getTR());
        omofobo.add(cardPlayerChoose.getBL());
        omofobo.add(cardPlayerChoose.getBR());
        List<Corner> availableCorners = new ArrayList<>(); //SAVING THE CORNERS OF THE CHOSEN CARD ON THE BOARD
        availableCorners.add(cardPlayerChoose.getTL());
        availableCorners.add(cardPlayerChoose.getTR());
        availableCorners.add(cardPlayerChoose.getBL());
        availableCorners.add(cardPlayerChoose.getBR());
        for(int i=0; i<3; i++) {
            if (omofobo.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON) {
                availableCorners.remove(i);
                System.out.println("eleiiminato dalla facccia della terra angolo " + i);
            }
        }
        String[]n= {"TL","TR","BL","BR"};
        System.out.println("Free Corners on the board: ");
        for (int i = 0; i < availableCorners.size(); i++) {
            System.out.println((i + 1) + ". " + availableCorners.get(i) +" di posizione " + n[i]);
        }

        System.out.print("Choose the corner you want to place the card on: ");
        int cornerIndex = scanner.nextInt();
        if (cornerIndex < 1 || cornerIndex > availableCorners.size()) {
            System.out.println("You can't place your card here");
            return;
        }


        int x= cardPlayerChoose.getNode().getCoordX(); //SAVING THE TOPLEFT COORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y= cardPlayerChoose.getNode().getCoordY();
        if(cornerIndex==1){//We are in Top Left corner case
            board.getNode(x-1, y-1).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed());//SETTING THE NODE
            if(board.getNode(x-1,y-1).getValueCounter()==2)
            {
             board.getNode(x-1,y-1).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x-1,y-1).getValueCounter()==1)
            {
                board.getNode(x-1,y-1).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x-1, y-1).setValueCounter(board.getNode(x-1, y-1).getValueCounter() - 1); // Decrease the value
            selectedCard.setNode(board.getNode(x-1,y-1)); //SAVING THE POSITION


            board.getNode(x-1, y).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed()); //SETTING THE NODE
            if(board.getNode(x-1,y).getValueCounter()==2)
            {
                board.getNode(x-1,y).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x-1,y).getValueCounter()==1)
            {
                board.getNode(x-1,y).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x-1, y).setValueCounter(board.getNode(x-1, y).getValueCounter() - 1); // Decrease the value


            board.getNode(x, y-1).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed()); //SETTING THE NODE
            if(board.getNode(x,y-1).getValueCounter()==2)
            {
                board.getNode(x,y-1).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x,y-1).getValueCounter()==1)
            {
                board.getNode(x,y-1).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x, y-1).setValueCounter(board.getNode(x, y-1).getValueCounter() - 1); // Decrease the value

            board.getNode(x, y).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed()); //SETTING THE NODE
            if(board.getNode(x,y).getValueCounter()==2)
            {
                board.getNode(x,y).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x,y).getValueCounter()==1)
            {
                board.getNode(x,y).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x, y).setValueCounter(board.getNode(x, y).getValueCounter() - 1); // Decrease the value
            System.out.println(board.getNode(x,y).getFirstPlacement());
        }
        if(cornerIndex==2){//We are in Top Right corner case
            board.getNode(x-1, y+1).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed()); //SETTING THE NODE
            if(board.getNode(x-1,y+1).getValueCounter()==2)
            {
                board.getNode(x-1,y+1).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x-1,y+1).getValueCounter()==1)
            {
                board.getNode(x-1,y+1).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x-1, y+1).setValueCounter(board.getNode(x-1, y+1).getValueCounter() - 1); // Decrease the value
            selectedCard.setNode(board.getNode(x-1,y+1)); //SAVING THE POSITION

            board.getNode(x-1, y +2).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed());//SETTING THE NODE
            if(board.getNode(x-1,y+2).getValueCounter()==2)
            {
                board.getNode(x-1,y+2).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x-1,y+2).getValueCounter()==1)
            {
                board.getNode(x-1,y+2).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }

            board.getNode(x-1, y +2).setValueCounter(board.getNode(x-1, y +2).getValueCounter() - 1); // Decrease the value

            board.getNode(x, y+1).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed());//SETTING THE NODE
            if(board.getNode(x,y+1).getValueCounter()==2)
            {
                board.getNode(x,y+1).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x,y+1).getValueCounter()==1)
            {
                board.getNode(x,y+1).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x, y+1).setValueCounter(board.getNode(x, y+1).getValueCounter() - 1); // Decrease the value

            board.getNode(x, y+2).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed());//SETTING THE NODE
            if(board.getNode(x,y+2).getValueCounter()==2)
            {
                board.getNode(x,y+2).setFirstPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            else if(board.getNode(x,y+2).getValueCounter()==1)
            {
                board.getNode(x,y+2).setSecondPlacement(selectedCard.getTL().getSpecificCornerSeed());
            }
            board.getNode(x, y+2).setValueCounter(board.getNode(x, y+2).getValueCounter() - 1); // Decrease the value
        }
        if(cornerIndex==3){//We are in Bottom Left corner case
            board.getNode(x+1, y-1).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+1, y-1).setValueCounter(board.getNode(x+1, y-1).getValueCounter() - 1); // Decrease the value
            selectedCard.setNode(board.getNode(x+1,y-1)); //SAVING THE POSITION
            board.getNode(x+1, y).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+1, y).setValueCounter(board.getNode(x+1, y).getValueCounter() - 1); // Decrease the value
            board.getNode(x+2, y-1).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+2, y-1).setValueCounter(board.getNode(x+2, y-1).getValueCounter() - 1); // Decrease the value
            board.getNode(x+2, y).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+2, y).setValueCounter(board.getNode(x+2, y).getValueCounter() - 1); // Decrease the value
        }
        if(cornerIndex==4){//We are in Bottom Right corner case
            board.getNode(x+1, y+1).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+1, y+1).setValueCounter(board.getNode(x+1, y+1).getValueCounter() - 1); // Decrease the value
            selectedCard.setNode(board.getNode(x+1,y+1)); //SAVING THE POSITION
            board.getNode(x+1, y+2).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+1, y+2).setValueCounter(board.getNode(x+1, y+2).getValueCounter() - 1); // Decrease the value
            board.getNode(x+2, y+1).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+2, y+1).setValueCounter(board.getNode(x+2, y+1).getValueCounter() - 1); // Decrease the value
            board.getNode(x+2, y+2).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed());//SETTING THE NODE
            board.getNode(x+2, y+2).setValueCounter(board.getNode(x+2, y+2).getValueCounter() - 1); // Decrease the value
        }

        // Add the selected card to the board
        selectedCard.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new index
        board.getCardsOnTheBoardList().add(selectedCard); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
        this.playerCards.remove(cardIndex); //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
        board.setNumOfEmpty(board.getNumOfEmpty()-3);
    }

    public void turnYourCard(Card card) //METHOD TO TURN YOUR CARD IN CASE THE PLAYER WANTS TO PLACE THE CARD ON HER BACK
    {
        if (!card.isCardBack()) {
            card.setCardBack(true); //CARD IS NOW ON HER BACK
            card.getTL().setSpecificCornerSeed(SpecificSeed.EMPTY); //SETTING ALL THE CORNERS AS EMPTY
            card.getTR().setSpecificCornerSeed(SpecificSeed.EMPTY);
            card.getBL().setSpecificCornerSeed(SpecificSeed.EMPTY);
            card.getBR().setSpecificCornerSeed(SpecificSeed.EMPTY);
        } else {
            card.setCardBack(false); //CARD IS ON HER ORIGINAL CONFIGURATION
            card.getTL().setSpecificCornerSeed(card.getTLBack().getSpecificCornerSeed()); //BACKUPPING ALL CORNERS
            card.getTR().setSpecificCornerSeed(card.getTRBack().getSpecificCornerSeed());
            card.getBL().setSpecificCornerSeed(card.getBLBack().getSpecificCornerSeed());
            card.getBR().setSpecificCornerSeed(card.getBRBack().getSpecificCornerSeed());
        }
    }

    //SETTER AND GETTER OF PLAYER CLASS

    public List<Card> getPlayerCards() {
        return playerCards;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
    public void setDot(Dot dot) {
        this.dot = dot;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }
    public String getNickName() {
        return nickName;
    }
    public int getPlayerScore() {
        return playerScore;
    }
    public Dot getDot() {
        return dot;
    }
    public Board getBoard() {
        return board;
    }


}
