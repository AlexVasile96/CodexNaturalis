package model.game;

import exceptions.InvalidCornerException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import model.card.*;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;

import java.util.*;

public class Player implements Observable {
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
        this.playerCards = new ArrayList<>(3);
        this.isCardBack=false;
    }



    public void visualizePlayerCards(List<Card> cards){
        for(Card card: cards){
            System.out.println(card);
        }
    }
    public void visualizePlayerScore(){
        System.out.println(nickName+"score: "+ playerScore);
    }

    public void drawResourceCard(ResourceDeck deck) {
        deck.drawCard(this);
    } //DRAWING RESOURCE CARD
    public void drawGoldCard(GoldDeck deck) {
        deck.drawCard(this);
    } //DRAWING GOLD CARD
    public List<Card> chooseCardFromWell(List<Card>cardwell,ResourceDeck rc, GoldDeck gd) {
            if (this.playerCards.size() < 3) {
                if (cardwell.isEmpty()) {
                    return null; //empty well
                }
                try {
                    for (Card card : cardwell) {
                        System.out.println(card);
                    }
                    System.out.println("\n");
                    System.out.print("Select a card from the well ");
                    Scanner scanner = new Scanner(System.in);
                    int selectedCardIndex = scanner.nextInt();
                    if (selectedCardIndex < 1 || selectedCardIndex > cardwell.size()) {
                        System.out.println("Not valid index");
                        return null;
                    }
                    int realIndex = selectedCardIndex - 1;
                    Card drownCard = cardwell.remove(realIndex);
                    playerCards.add(drownCard);
                    if (drownCard.getId() >= 1 && drownCard.getId() <= 40) {
                        rc.drawCard(cardwell);
                    }
                    if (drownCard.getId() >= 41 && drownCard.getId() <= 80) {
                        gd.drawCard(cardwell);
                    }
                    return cardwell;
                } catch (Exception e) {
                    throw new IllegalStateException("Well is empty"); // Eccezione specifica
                }
            }
            else {
                System.out.println("Player's deck already has 3 cards\n");
                return cardwell;
            }
        }

    public Card chooseCard(int index) {
        try{
        if (index < 0 || index >= playerCards.size()) {
            throw new IndexOutOfBoundsException("Not a valid index");
        }
        }
    catch (IndexOutOfBoundsException e)
            {System.out.println(e.getMessage()); //INDEX GOES FROM 1 TO 3

        }

        return playerCards.get(index);
    }  //METHOD TO CHOOSE WHICH CARD THE PLAYER WANTS TO PLACE ON THE BOARD

    public void chooseSecretCard(List <ObjectiveCard> secretCards){
        for (int i = 0; i < secretCards.size(); i++) {
            Card card = secretCards.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        boolean validIndex = false;
        while(validIndex == false){
        System.out.println("Inserisci il numero della carta obiettivo SEGRETA che vuoi pescare: ");
        int selectedCardIndex = scanner.nextInt();

            try {
                if (selectedCardIndex < 1 || selectedCardIndex > secretCards.size()) {
                    throw new IndexOutOfBoundsException("Not a valid index");
                }
                else {
                    this.secretChosenCard=secretCards.get(selectedCardIndex - 1);
                    System.out.println(secretChosenCard);
                    validIndex = true;
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }
    } //METHOD TO CHOOSE THE SECRET CARD (THE PLAYER HAS A CHOICE BETWEEN 2 CARDS)

    public void showingToTheCurrentPlayerCardsOnTheBoard(List<Card> cardsPlayerCanChooseFrom){
        System.out.println("Cards on the board are:");                          //PRINTING THE CARDS ON THE BOARD
        for (int i = 0; i < cardsPlayerCanChooseFrom.size(); i++) {
            Card card = cardsPlayerCanChooseFrom.get(i);
            System.out.println((i + 1) + ". " + card);
        }
    }
    public Card selectTheCardFromTheBoard(List<Card> cardsPlayerCanChooseFrom, Scanner scanner){
        System.out.print("Select a card on your board you want to place the card from your deck on: ");
        int selectedCardIndex = scanner.nextInt();
        try{
            if (selectedCardIndex < 1 || selectedCardIndex > cardsPlayerCanChooseFrom.size()) {
                throw new IndexOutOfBoundsException("Not a valid index");}
        }
        catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            return null;
        }
        catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }
        return cardsPlayerCanChooseFrom.get(selectedCardIndex - 1);
    }
    public List<Corner> creatingCorners(Card cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER
        availableCorners.add(cardPlayerChoose.getTL());
        availableCorners.add(cardPlayerChoose.getTR());
        availableCorners.add(cardPlayerChoose.getBL());
        availableCorners.add(cardPlayerChoose.getBR());
        return availableCorners;
    }
    public void cardChosenIsTheInitialcard(Card initialCard,List<Corner> availableCorners )
    {
        List<Corner> initialCardCorners = new ArrayList<>();       //THEN ELIMINATING NOTTOBEPLACEDON CORNERS FROM PLAYER DISPLAYER &&CORNER WHOSE VALUE IS 0
        initialCardCorners.add(initialCard.getTL());
        initialCardCorners.add(initialCard.getTR());
        initialCardCorners.add(initialCard.getBL());
        initialCardCorners.add(initialCard.getBR());
        for (int i = initialCardCorners.size() - 1; i >= 0; i--) {
            if (initialCardCorners.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || initialCardCorners.get(i).getValueCounter() == 0) {
                availableCorners.remove(i);
            }
        }
    }
    public void cardChosenIsNotTheInitialcard(List<Corner> availableCorners, List<Corner> corner)
    {
        for (int i = corner.size() - 1; i >= 0; i--) {
            if (corner.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || corner.get(i).getValueCounter() == 0) { //SAMECHECK
                availableCorners.remove(i);
                corner.remove(i);
            }
        }
    }
    public String freeCornersOfTheSelectedCard(List<Corner> availableCorners, Card cardPlayerChoose, Scanner scanner){
        Map<Corner, String> cornerLabels = new HashMap<>();      //PUTTING THE CORRECT CORNERLABEL TO THE CORRECT CORNER
        cornerLabels.put(cardPlayerChoose.getTL(), "TL");
        cornerLabels.put(cardPlayerChoose.getTR(), "TR");
        cornerLabels.put(cardPlayerChoose.getBL(), "BL");
        cornerLabels.put(cardPlayerChoose.getBR(), "BR");

        System.out.println("Free Corners of the selected card "); //DISPLAYING THE POSSIBLE CORNERS
        for (int i = 0; i < availableCorners.size(); i++) {
            Corner corner = availableCorners.get(i);
            String cornerLabel = cornerLabels.get(corner);
            System.out.println((i + 1) + ". " + corner + " -> " + cornerLabel + "|Please press " +cornerLabel + " to select the corner ");
        }
        System.out.print("Choose the corner you want to place the card on: ");
        String selectedCorner = scanner.next().toUpperCase();
        try{
            if (!selectedCorner.equals("TL") && !selectedCorner.equals("TR") && !selectedCorner.equals("BL") && !selectedCorner.equals("BR")) {
                throw new InvalidCornerException("Invalid corner selection.");
            }
        }  catch (InvalidCornerException e){
            System.out.println(e.getMessage());
            return null;
        }
        return selectedCorner;

    }
    public void playYourCardOnTheTopLeftCorner(int x,int y, Card selectedCardFromTheDeck)
    {
        selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1); //PLACED CORNER, I CANNOT PUT ANY OTHER THING ON THIS CORNER

        board.getNode(x - 1, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x - 1, y - 1).getValueCounter() == 2) {
            board.getNode(x - 1, y - 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x - 1, y - 1).getValueCounter() == 1) {
            board.getNode(x - 1, y - 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x - 1, y - 1).setValueCounter(board.getNode(x - 1, y - 1).getValueCounter() - 1);
        // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x - 1, y - 1)); //SAVING THE POSITION


        board.getNode(x - 1, y).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x - 1, y).getValueCounter() == 2) {
            board.getNode(x - 1, y).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x - 1, y).getValueCounter() == 1) {
            board.getNode(x - 1, y).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x - 1, y).setValueCounter(board.getNode(x - 1, y).getValueCounter() - 1); // Decrease the value


        board.getNode(x, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x, y - 1).getValueCounter() == 2) {
            board.getNode(x, y - 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x, y - 1).getValueCounter() == 1) {
            board.getNode(x, y - 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x, y - 1).setValueCounter(board.getNode(x, y - 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x, y).getValueCounter() == 2) {
            board.getNode(x, y).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x, y).getValueCounter() == 1) {
            board.getNode(x, y).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x, y).setValueCounter(board.getNode(x, y).getValueCounter() - 1); // Decrease the value
    }
    public void playYourCardOnTheTopRightCorner(int x,int y, Card selectedCardFromTheDeck)
    {
        selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);

        board.getNode(x - 1, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x - 1, y + 1).getValueCounter() == 2) {
            board.getNode(x - 1, y + 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x - 1, y + 1).getValueCounter() == 1) {
            board.getNode(x - 1, y + 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x - 1, y + 1).setValueCounter(board.getNode(x - 1, y + 1).getValueCounter() - 1); // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x - 1, y + 1)); //SAVING THE POSITION

        board.getNode(x - 1, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x - 1, y + 2).getValueCounter() == 2) {
            board.getNode(x - 1, y + 2).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x - 1, y + 2).getValueCounter() == 1) {
            board.getNode(x - 1, y + 2).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }

        board.getNode(x - 1, y + 2).setValueCounter(board.getNode(x - 1, y + 2).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x, y + 1).getValueCounter() == 2) {
            board.getNode(x, y + 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x, y + 1).getValueCounter() == 1) {
            board.getNode(x, y + 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x, y + 1).setValueCounter(board.getNode(x, y + 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x, y + 2).getValueCounter() == 2) {
            board.getNode(x, y + 2).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x, y + 2).getValueCounter() == 1) {
            board.getNode(x, y + 2).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x, y + 2).setValueCounter(board.getNode(x, y + 2).getValueCounter() - 1); // Decrease the value
    }
    public void playYourCardOnTheBottomLeftCorner(int x,int y, Card selectedCardFromTheDeck){
        selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);


        board.getNode(x + 1, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE

        if (board.getNode(x + 1, y - 1).getValueCounter() == 2) {
            board.getNode(x + 1, y - 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 1, y - 1).getValueCounter() == 1) {
            board.getNode(x + 1, y - 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 1, y - 1).setValueCounter(board.getNode(x + 1, y - 1).getValueCounter() - 1); // Decrease the value

        selectedCardFromTheDeck.setNode(board.getNode(x + 1, y - 1)); //SAVING THE POSITION

        board.getNode(x + 1, y).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y).getValueCounter() == 2) {
            board.getNode(x + 1, y).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 1, y).getValueCounter() == 1) {
            board.getNode(x + 1, y).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 1, y).setValueCounter(board.getNode(x + 1, y).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE

        if (board.getNode(x + 2, y - 1).getValueCounter() == 2) {
            board.getNode(x + 2, y - 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 2, y - 1).getValueCounter() == 1) {
            board.getNode(x + 2, y - 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 2, y - 1).setValueCounter(board.getNode(x + 2, y - 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y).getValueCounter() == 2) {
            board.getNode(x + 2, y).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 2, y).getValueCounter() == 1) {
            board.getNode(x + 2, y).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 2, y).setValueCounter(board.getNode(x + 2, y).getValueCounter() - 1); // Decrease the value
    }
    public void playYourCardOnTheBottomRightCorner(int x,int y, Card selectedCardFromTheDeck){
        selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);

        board.getNode(x + 1, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y + 1).getValueCounter() == 2) {
            board.getNode(x + 1, y + 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 1, y + 1).getValueCounter() == 1) {
            board.getNode(x + 1, y + 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 1, y + 1).setValueCounter(board.getNode(x + 1, y + 1).getValueCounter() - 1); // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x + 1, y + 1)); //SAVING THE POSITION


        board.getNode(x + 1, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y + 2).getValueCounter() == 2) {
            board.getNode(x + 1, y + 2).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 1, y + 2).getValueCounter() == 1) {
            board.getNode(x + 1, y + 2).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 1, y + 2).setValueCounter(board.getNode(x + 1, y + 2).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y + 1).getValueCounter() == 2) {
            board.getNode(x + 2, y + 1).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 2, y + 1).getValueCounter() == 1) {
            board.getNode(x + 2, y + 1).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 2, y + 1).setValueCounter(board.getNode(x + 2, y + 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y + 2).getValueCounter() == 2) {
            board.getNode(x + 2, y + 2).setFirstPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        } else if (board.getNode(x + 2, y + 2).getValueCounter() == 1) {
            board.getNode(x + 2, y + 2).setSecondPlacement(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        }
        board.getNode(x + 2, y + 2).setValueCounter(board.getNode(x + 2, y + 2).getValueCounter() - 1); // Decrease the value
    }


    public void playCard(Board board, int cardIndex) { //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        Scanner scanner = new Scanner(System.in);
        Card selectedCardFromTheDeck = chooseCard(cardIndex);
        checkIfTheCardExist(cardIndex);                                  //CHECKING IF THE CARD TRULY EXISTS
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirments are respected
        if(!canIPLaceTheGoldCard && selectedCardFromTheDeck.getId()>40) return;

        Card initialCard = board.getCardsOnTheBoardList().get(0);               //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList();   //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM

        showingToTheCurrentPlayerCardsOnTheBoard(cardsPlayerCanChooseFrom);  //showing to the current player the cards he/she has on the board

        Card cardPlayerChoose= selectTheCardFromTheBoard(cardsPlayerCanChooseFrom,scanner);  //Choosing the card
        System.out.println("Card correctly chosen");
        List<Corner> availableCorners = creatingCorners(cardPlayerChoose);

        //switchcase
        //4 differenti funzioni in base al corner scelto

        if (cardPlayerChoose.getId() == initialCard.getId()) {        //THE  CARD CHOSEN ON THE BOARD IS THE INITIAL CARD AND WE HAVE TO DELETE THE CORNERS NOT AVAILABLE
            cardChosenIsTheInitialcard(initialCard,availableCorners);
        } else {                                                        //CARD CHOSEN ISN'T THE INITIAL CARD
            List<Corner> corner = creatingCorners(cardPlayerChoose);
            cardChosenIsNotTheInitialcard(availableCorners,corner);
        }

        String selectedCorner= freeCornersOfTheSelectedCard(availableCorners, cardPlayerChoose,scanner); //Showing the available corners of the card and letting the player choose one
        int x = cardPlayerChoose.getNode().getCoordX(); //SAVING THE TOPLEFT COORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y = cardPlayerChoose.getNode().getCoordY();
        switch (selectedCorner) { //SWITCH CASE TO PLACE THE CARD CORRECLTY
            case "TL":
                cardPlayerChoose.getTL().setValueCounter(cardPlayerChoose.getTL().getValueCounter()-1);
                playYourCardOnTheTopLeftCorner(x,y,selectedCardFromTheDeck);
                break;
            case "TR":
                cardPlayerChoose.getTR().setValueCounter(cardPlayerChoose.getTR().getValueCounter()-1);
                playYourCardOnTheTopRightCorner(x,y,selectedCardFromTheDeck);
                break;
            case "BL":
                cardPlayerChoose.getBL().setValueCounter(cardPlayerChoose.getBL().getValueCounter()-1);
                playYourCardOnTheBottomLeftCorner(x,y,selectedCardFromTheDeck);
                break;
            case "BR":
                cardPlayerChoose.getBR().setValueCounter(cardPlayerChoose.getBR().getValueCounter()-1);
                playYourCardOnTheBottomRightCorner(x,y,selectedCardFromTheDeck);
                break;
        }

        selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1); //DECRESING ALL VALUECOUNTER BECAUSE ALL CORNERS ARE GOING TO BE PLACED ON THE BOARD
        selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);
        selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);
        selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);



            // Add the selected card to the board
            selectedCardFromTheDeck.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new index
            board.getCardsOnTheBoardList().add(selectedCardFromTheDeck); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
            this.playerCards.remove(cardIndex); //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
            board.setNumOfEmpty(board.getNumOfEmpty() - 3);
            if (selectedCardFromTheDeck.getId() < 41 && selectedCardFromTheDeck.getId() > 0) { //carta risorsa
                ResourceUpdater resourceUpdater = new ResourceUpdater();
                resourceUpdater.updatePlayerPoints(selectedCardFromTheDeck, this, board);

            } else if (selectedCardFromTheDeck.getId() < 81 && selectedCardFromTheDeck.getId() > 40) {
                GoldUpdater updater = new GoldUpdater();
                updater.updatePlayerPoints(selectedCardFromTheDeck, this, board);

            }
            System.out.println("Your new score is " + playerScore + " points");
            if (playerScore >= 20) {
                System.out.println("Player " + getNickName() + "wins!\n");
                EndGame endGame = new EndGame();
            }
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

    @Override
    public void addListener(InvalidationListener invalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {

    }

    public int checkIfTheCardExist(int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);          //SELECTEDCARDFROMTHEDECK IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCardFromTheDeck == null) {                         //CHECKING IF THE CARD EXISTS, IN CASE RETURN
            return 0;
        }
        return cardIndex;
    }
    public boolean isTheCardGold(Card selectedCard)
    {
        if (selectedCard instanceof GoldCard) {
            return board.placeGoldCard(((GoldCard) selectedCard).getRequirementsForPlacing());
        }
        else {
            return false;
        }
    }


    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickName + '\'' +
                ", playerScore=" + playerScore +
                ", dot=" + dot +
                ", board=" + board +
                ", isCardBack=" + isCardBack +
                ", playerCards=" + playerCards +
                ", secretChosenCard=" + secretChosenCard +
                '}';
    }
}
