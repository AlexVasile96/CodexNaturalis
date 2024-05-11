package model.game;

import com.google.gson.JsonObject;
import exceptions.InvalidCornerException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import model.card.*;
import model.deck.GoldDeck;
import model.deck.ResourceDeck;
import view.ClientView;

import java.util.*;

public class Player implements Observable {
    private String nickName;
    private int playerScore;
    private int index;
    private Dot dot;
    private ClientView clientView;
    private Board board;
    private boolean isCardBack;
    private ArrayList <Card> playerCards;
    private ObjectiveCard secretChosenCard;
    private boolean hasThePlayerAlreadyPLacedACard= false;
    private boolean isThePlayerDeckStarted=false;
    private boolean hasThePlayerGot20Points=false;
    //private boolean hasThePlayerPlacedACard=false;
    public Player(String nickName, int playerScore, Dot dot, Board board){ //PLAYER CONSTRUCTOR
        this.nickName = nickName;
        this.playerScore = playerScore;
        this.dot = dot;
        this.board = board;
        this.playerCards = new ArrayList<>(3);
        this.isCardBack=false;
        this.clientView= new ClientView();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isThePlayerDeckStarted() {
        return isThePlayerDeckStarted;
    }

    public void setThePlayerDeckStarted(boolean thePlayerDeckStarted) {
        isThePlayerDeckStarted = thePlayerDeckStarted;
    }

//IN GAME METHODS

    public String drawResourceCard(ResourceDeck deck) {
       deck.drawCard(this);

        return "card drawn correctly from Resource deck";
    }
    public String drawGoldCard(GoldDeck deck) {
        deck.drawCard(this);
        return "card drawn correctly from Gold deck";
    }
    public void chooseCardFromWell(List<Card>cardwell, ResourceDeck rc, GoldDeck gd) {
        Scanner scanner = new Scanner(System.in);
        if (this.playerCards.size() < 3) {
            if (cardwell.isEmpty()) {
                return; //empty well
            }
            try {
                for (Card card : cardwell) {
                    System.out.println(card);
                }
                Card drownCard= choosingTheSpecificCardFromTheWell(scanner, cardwell); //Choosing the card and saving it in drowncard
                fillingTheWellWithTheCorrectCard(drownCard,rc,gd, cardwell);           //Filling The Well
                playerCards.add(drownCard);                                             //Adding the card to the player hand
            } catch (Exception e) {
                throw new IllegalStateException("Well is empty"); // Eccezione specifica
            }
        }
        else {
            System.out.println("Player's deck already has 3 cards\n");
        }
    }
    public String chooseCardFromWellForServer(List<Card>cardwell, int index, ResourceDeck rc, GoldDeck gd) {
        if (this.playerCards.size() < 3) {
            if (cardwell.isEmpty()) {
                System.out.println("Well is empty");
                return "Well is empty"; //empty well
            }
            try {
                for (Card card : cardwell) {
                    System.out.println(card);
                }
                Card drownCard= choosingTheSpecificCardFromTheWellForServer(index, cardwell); //Choosing the card and saving it in drowncard
                fillingTheWellWithTheCorrectCard(drownCard,rc,gd, cardwell);           //Filling The Well
                playerCards.add(drownCard);                                             //Adding the card to the player hand
            } catch (Exception e) {
                throw new IllegalStateException("problema nel blocco try di chooseCardFromWellForServer"); // Eccezione specifica
            }
        }
        else {
            System.out.println("Player's deck already has 3 cards\n");
            return "Player's deck already has 3 cards";
        }
        System.out.println("operation performed correctly");
        return "operation performed correctly";
    }
    public void chooseSecretCard(List <ObjectiveCard> secretCards){
        for (int i = 0; i < secretCards.size(); i++) {
            Card card = secretCards.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        boolean validIndex = false;
        while(!validIndex){
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

    public Card checkingTheChosencard( int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);                   //OKAY
        checkIfTheCardExist(cardIndex);                                         //CHECKING IF THE CARD TRULY EXISTS->OKAY
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirements are respected->OKAY
        if(!canIPLaceTheGoldCard && selectedCardFromTheDeck.getId()>40) return null;
        return selectedCardFromTheDeck;
    }
    public Card gettingCardsFromTheBoard(Board board, int cardChosenONTheBoard)
    {
        Card initialCard = board.getCardsOnTheBoardList().getFirst();               //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList();   //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM
        return cardsPlayerCanChooseFrom.get(cardChosenONTheBoard);
    }
    public String isTheCardChosenTheInitialcard( Card cardPlayerChoose, InitialCard initialCard){

        if (cardPlayerChoose.getId() == initialCard.getId()) {        //THE  CARD CHOSEN ON THE BOARD IS THE INITIAL CARD AND WE HAVE TO DELETE THE CORNERS NOT AVAILABLE
            List<Corner> availableCorners = creatingCorners(initialCard);
            cardChosenIsTheInitialcard(initialCard,availableCorners);
            return freeScornerosi(availableCorners, cardPlayerChoose);
        } else {                                                        //CARD CHOSEN ISN'T THE INITIAL CARD
            List<Corner> availableCorners= creatingCornersForNotInitialcard(cardPlayerChoose);
            List<Corner>corner= creatingCornersForNotInitialcard(cardPlayerChoose);
            cardChosenIsNotTheInitialcard(availableCorners,corner);
            return freeScornerosi(availableCorners, cardPlayerChoose);
        }

    }

    public void playCard(Board board, int cardIndex, int cardChosenONTheBoard,Card selectedCardFromTheDeck, InitialCard cardPlayerChoose, String selectedCorner) { //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        int x = cardPlayerChoose.getNode().getCoordX(); //SAVING THE TOP LEFT CORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y = cardPlayerChoose.getNode().getCoordY();
        switch (selectedCorner) { //SWITCH CASE TO PLACE THE CARD CORRECTLY
            case "TL":
                if(cardPlayerChoose.getIndexOnTheBoard()==1 && cardPlayerChoose.isCardBack())
                {
                    cardPlayerChoose.getTLIBack().setValueCounter(0);
                }
                cardPlayerChoose.getTL().setValueCounter(cardPlayerChoose.getTL().getValueCounter()-1);
                playYourCardOnTheTopLeftCorner(x,y,selectedCardFromTheDeck);
                break;
            case "TR":
                if(cardPlayerChoose.getIndexOnTheBoard()==1 && cardPlayerChoose.isCardBack())
                {
                    cardPlayerChoose.getTRIBack().setValueCounter(0);
                }
                cardPlayerChoose.getTR().setValueCounter(cardPlayerChoose.getTR().getValueCounter()-1);
                playYourCardOnTheTopRightCorner(x,y,selectedCardFromTheDeck);
                break;
            case "BL":
                if(cardPlayerChoose.getIndexOnTheBoard()==1 && cardPlayerChoose.isCardBack())
                {
                    cardPlayerChoose.getBLIBack().setValueCounter(0);
                }
                cardPlayerChoose.getBL().setValueCounter(cardPlayerChoose.getBL().getValueCounter()-1);
                playYourCardOnTheBottomLeftCorner(x,y,selectedCardFromTheDeck);
                break;
            case "BR":
                if(cardPlayerChoose.getIndexOnTheBoard()==1 && cardPlayerChoose.isCardBack())
                {
                    cardPlayerChoose.getBRIBack().setValueCounter(0);
                }
                cardPlayerChoose.getBR().setValueCounter(cardPlayerChoose.getBR().getValueCounter()-1);
                playYourCardOnTheBottomRightCorner(x,y,selectedCardFromTheDeck);
                break;
            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedCorner);
        }
        decreasingAllTheValuesOfTheCornerPlaced(selectedCardFromTheDeck); //DECRESING ALL VALUECOUNTER BECAUSE ALL CORNERS ARE GOING TO BE PLACED ON THE BOARD
        selectedCardFromTheDeck.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new incremented index
        board.getCardsOnTheBoardList().add(selectedCardFromTheDeck); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
        this.playerCards.remove(cardIndex); //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
        board.setNumOfEmpty(board.getNumOfEmpty() - 3);
        updatingPoints(selectedCardFromTheDeck); //Updating player Points
        if (playerScore >= 20) {                                            //EndGame if the playerpoints=>20 points
            System.out.println("Player " + getNickName() + "has reached 20 points!\n");
            EndGame endGame = new EndGame();
        }
    }













    //METHODS INVOKED FROM THE PREVIOUS METHODS



    private int checkIfTheCardExist(int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);          //SELECTEDCARDFROMTHEDECK IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCardFromTheDeck == null) {                         //CHECKING IF THE CARD EXISTS, IN CASE RETURN
            return 0;
        }
        return cardIndex;
    }
    private boolean isTheCardGold(Card selectedCard)
    {
        if (selectedCard instanceof GoldCard) {
            return board.placeGoldCard(((GoldCard) selectedCard).getRequirementsForPlacing());
        }
        else {
            return false;
        }
    }

    private Card selectTheCardFromTheBoard(List<Card> cardsPlayerCanChooseFrom, Scanner scanner){
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

    private List<Corner> creatingCorners(InitialCard cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                          //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER
        if(cardPlayerChoose.isCardBack()&& cardPlayerChoose.getIndexOnTheBoard()==1)
        {
            availableCorners.add(cardPlayerChoose.getTLIBack());
            availableCorners.add(cardPlayerChoose.getTRIBack());
            availableCorners.add(cardPlayerChoose.getBLIBack());
            availableCorners.add(cardPlayerChoose.getBRIBack());
        }
        else if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()!=1)
        {
            availableCorners.add(cardPlayerChoose.getTLBack());
            availableCorners.add(cardPlayerChoose.getTRBack());
            availableCorners.add(cardPlayerChoose.getBLBack());
            availableCorners.add(cardPlayerChoose.getBRBack());
        }
        else if(!cardPlayerChoose.isCardBack()){
            availableCorners.add(cardPlayerChoose.getTL());
            availableCorners.add(cardPlayerChoose.getTR());
            availableCorners.add(cardPlayerChoose.getBL());
            availableCorners.add(cardPlayerChoose.getBR());
        }
        return availableCorners;
    }

    private List<Corner> creatingCornersForNotInitialcard(Card cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                          //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER

     if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()!=1)
        {
            availableCorners.add(cardPlayerChoose.getTLBack());
            availableCorners.add(cardPlayerChoose.getTRBack());
            availableCorners.add(cardPlayerChoose.getBLBack());
            availableCorners.add(cardPlayerChoose.getBRBack());
        }
        else if(!cardPlayerChoose.isCardBack()){
            availableCorners.add(cardPlayerChoose.getTL());
            availableCorners.add(cardPlayerChoose.getTR());
            availableCorners.add(cardPlayerChoose.getBL());
            availableCorners.add(cardPlayerChoose.getBR());
        }
        return availableCorners;
    }


    private void cardChosenIsTheInitialcard(InitialCard initialCard,List<Corner> availableCorners )
    {
        if(initialCard.isCardBack()){
            List<Corner> initialCardCorners = new ArrayList<>();       //THEN ELIMINATING NOTTOBEPLACEDON CORNERS FROM PLAYER DISPLAYER &&CORNER WHOSE VALUE IS 0
            initialCardCorners.add(initialCard.getTLIBack());
            initialCardCorners.add(initialCard.getTRIBack());
            initialCardCorners.add(initialCard.getBLIBack());
            initialCardCorners.add(initialCard.getBRIBack());
            for (int i = initialCardCorners.size() - 1; i >= 0; i--) {
                if (initialCardCorners.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || initialCardCorners.get(i).getValueCounter() == 0) {
                    availableCorners.remove(i);
                }
            }
        }
        else {
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
    }
    private void cardChosenIsNotTheInitialcard(List<Corner> availableCorners, List<Corner> corner)
    {
        for (int i = corner.size() - 1; i >= 0; i--) {
            if (corner.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON || corner.get(i).getValueCounter() == 0) { //SAMECHECK
                availableCorners.remove(i);
                corner.remove(i);
            }
        }
    }





    private String freeCornersOfTheSelectedCard(List<Corner> availableCorners, Card cardPlayerChoose, Scanner scanner){
        Map<Corner, String> cornerLabels = new HashMap<>();      //PUTTING THE CORRECT CORNERLABEL TO THE CORRECT CORNER
        if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()==1){
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTLIBack(), "TLBack");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTRIBack(), "TRBack");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBLIBack(), "BLBack");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBRIBack(), "BRBack");}
        else if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()!=1)
        {
            cornerLabels.put(cardPlayerChoose.getTLBack(), "TLBack");
            cornerLabels.put(cardPlayerChoose.getTRBack(), "TRBack");
            cornerLabels.put(cardPlayerChoose.getBLBack(), "BLBack");
            cornerLabels.put(cardPlayerChoose.getBRBack(), "BRBack");
        }
        else if(!cardPlayerChoose.isCardBack())
        {
            cornerLabels.put(cardPlayerChoose.getTL(), "TL");
            cornerLabels.put(cardPlayerChoose.getTR(), "TR");
            cornerLabels.put(cardPlayerChoose.getBL(), "BL");
            cornerLabels.put(cardPlayerChoose.getBR(), "BR");
        }

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
    private String freeScornerosi(List<Corner> availableCorners, Card cardPlayerChoose){
        Map<Corner, String> cornerLabels = new HashMap<>();                                 //PUTTING THE CORRECT CORNERLABEL TO THE CORRECT CORNER
        if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()==1){
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTLIBack(), "TL");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTRIBack(), "TR");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBLIBack(), "BL");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBRIBack(), "BR");}
        else if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()!=1)
        {
            cornerLabels.put(cardPlayerChoose.getTLBack(), "TL");
            cornerLabels.put(cardPlayerChoose.getTRBack(), "TR");
            cornerLabels.put(cardPlayerChoose.getBLBack(), "BL");
            cornerLabels.put(cardPlayerChoose.getBRBack(), "BR");

        }
        else if(!cardPlayerChoose.isCardBack())
        {
            cornerLabels.put(cardPlayerChoose.getTL(), "TL");
            cornerLabels.put(cardPlayerChoose.getTR(), "TR");
            cornerLabels.put(cardPlayerChoose.getBL(), "BL");
            cornerLabels.put(cardPlayerChoose.getBR(), "BR");
        }
        StringBuilder options = new StringBuilder(); // Stringa contenente le opzioni disponibili
        options.append("Free Corners of the selected card\n");

        for (int i = 0; i < availableCorners.size(); i++) {
            Corner corner = availableCorners.get(i);
            String cornerLabel = cornerLabels.get(corner);
            options.append((i + 1)).append(". ").append(corner).append(" -> ").append(cornerLabel).append("|Please press ").append(cornerLabel).append(" to select the corner\n");
        }
        options.append("\nend");
        System.out.println(options);
        return options.toString();




    }





    private void playYourCardOnTheTopLeftCorner(int x,int y, Card selectedCardFromTheDeck)
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
    private void playYourCardOnTheTopRightCorner(int x,int y, Card selectedCardFromTheDeck)
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
    private void playYourCardOnTheBottomLeftCorner(int x,int y, Card selectedCardFromTheDeck){
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
    private void playYourCardOnTheBottomRightCorner(int x,int y, Card selectedCardFromTheDeck){
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
    private void decreasingAllTheValuesOfTheCornerPlaced(Card selectedCardFromTheDeck)
    {
        selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1);
        selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);
        selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);
        selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);
    }
    private void updatingPoints(Card selectedCardFromTheDeck)
    {
        if (selectedCardFromTheDeck.getId() < 41 && selectedCardFromTheDeck.getId() > 0) { //carta risorsa
            ResourceUpdater resourceUpdater = new ResourceUpdater();
            resourceUpdater.updatePlayerPoints(selectedCardFromTheDeck, this, board);

        } else if (selectedCardFromTheDeck.getId() < 81 && selectedCardFromTheDeck.getId() > 40) {
            GoldUpdater updater = new GoldUpdater();
            updater.updatePlayerPoints(selectedCardFromTheDeck, this, board);
        }
        System.out.println("Your new score is " + playerScore + " points");
    }
    public void visualizePlayerCards(List<Card> cards){
        for(Card card: cards){
            System.out.println(card);
        }
    }

    public Card choosingTheSpecificCardFromTheWell(Scanner scanner, List<Card>cardwell){
        System.out.print("Select a card from the well ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > cardwell.size()) {
            System.out.println("Not valid index");
            return null;
        }
        int realIndex = selectedCardIndex - 1;
        return cardwell.remove(realIndex);

    }
    public Card choosingTheSpecificCardFromTheWellForServer(int index, List<Card>cardwell){
        if (index < 0 || index > cardwell.size()-1) {
            System.out.println("Not valid index");
            return null;
        }
        return cardwell.remove(index);

    }
    private void  fillingTheWellWithTheCorrectCard(Card drownCard, ResourceDeck rc,GoldDeck gd, List<Card>cardwell)
    {
        if (drownCard.getId() >= 1 && drownCard.getId() <= 40) {
            rc.drawCard(cardwell);
        }
        if (drownCard.getId() >= 41 && drownCard.getId() <= 80) {
            gd.drawCard(cardwell);
        }
    }
    public Card chooseCard(int index) {
        try{
            if (index < 0 || index >= playerCards.size()) {
                throw new IndexOutOfBoundsException("Not a valid index");
            }
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println(e.getMessage()); //INDEX GOES FROM 1 TO 3
        }

        return playerCards.get(index);
    }  //METHOD TO CHOOSE WHICH CARD THE PLAYER WANTS TO PLACE ON THE BOARD






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


    public ClientView getClientView() {
        return clientView;
    }

    public void setClientView(ClientView clientView) {
        this.clientView = clientView;
    }


    public boolean isHasThePlayerAlreadyPLacedACard() {
        return hasThePlayerAlreadyPLacedACard;
    }

    public void setHasThePlayerAlreadyPLacedACard(boolean hasThePlayerAlreadyPLacedACard) {
        this.hasThePlayerAlreadyPLacedACard = hasThePlayerAlreadyPLacedACard;
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

    /*
    *
    public void playCard(Board board, int cardIndex) { //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        Scanner scanner = new Scanner(System.in);
        Card selectedCardFromTheDeck = chooseCard(cardIndex);                   //OKAY
        checkIfTheCardExist(cardIndex);                                         //CHECKING IF THE CARD TRULY EXISTS->OKAY
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirements are respected->OKAY
        if(!canIPLaceTheGoldCard && selectedCardFromTheDeck.getId()>40) return; //DA MODIFICARE

        Card initialCard = board.getCardsOnTheBoardList().get(0);               //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList();   //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM

        Card cardPlayerChoose= selectTheCardFromTheBoard(cardsPlayerCanChooseFrom,scanner);  //Choosing the card
        List<Corner> availableCorners = creatingCorners((InitialCard)cardPlayerChoose); //Creating 4 corners to handle SelectedCard corners

        if (cardPlayerChoose.getId() == initialCard.getId()) {        //THE  CARD CHOSEN ON THE BOARD IS THE INITIAL CARD AND WE HAVE TO DELETE THE CORNERS NOT AVAILABLE
            cardChosenIsTheInitialcard((InitialCard) initialCard,availableCorners);
        } else {                                                        //CARD CHOSEN ISN'T THE INITIAL CARD
            List<Corner> corner = creatingCorners((InitialCard) cardPlayerChoose);
            cardChosenIsNotTheInitialcard(availableCorners,corner);
        }

        String selectedCorner= freeCornersOfTheSelectedCard(availableCorners, cardPlayerChoose,scanner); //Showing the available corners of the card and letting the player choose one
        int x = cardPlayerChoose.getNode().getCoordX(); //SAVING THE TOP LEFT CORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y = cardPlayerChoose.getNode().getCoordY();
        switch (selectedCorner) { //SWITCH CASE TO PLACE THE CARD CORRECTLY
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
            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedCorner);
        }
        decreasingAllTheValuesOfTheCornerPlaced(selectedCardFromTheDeck);                       //DECRESING ALL VALUECOUNTER BECAUSE ALL CORNERS ARE GOING TO BE PLACED ON THE BOARD
        selectedCardFromTheDeck.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new index
        board.getCardsOnTheBoardList().add(selectedCardFromTheDeck);                            //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
        this.playerCards.remove(cardIndex);                                                     //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
        board.setNumOfEmpty(board.getNumOfEmpty() - 3);
        updatingPoints(selectedCardFromTheDeck);                                                 //Updating player Points
        if (playerScore >= 20) {                                                                //EndGame if the playerpoints=>20 points
            System.out.println("Player " + getNickName() + "wins!\n");
            EndGame endGame = new EndGame();
        }
    }

    * */

    public JsonObject toJsonObject(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("score", playerScore);
        jsonObject.addProperty("dot", dot.ordinal());
        jsonObject.add("board", board.toJsonObject());
        jsonObject.addProperty("playerCards", String.valueOf(playerCards));
        jsonObject.addProperty("clientView", String.valueOf(clientView));
        return jsonObject;
    }

    public void fromJsonObject(JsonObject jsonObject) {
        this.nickName = jsonObject.get("nickName").getAsString();
        this.playerScore = jsonObject.get("score").getAsInt();
        this.dot = Dot.values()[jsonObject.get("dot").getAsInt()];
        // Assuming Board and Card classes have appropriate methods for deserialization
        this.board = Board.fromJsonObject(jsonObject.get("board").getAsJsonObject());
        // Assuming playerCards and clientView are deserialized appropriately
        // this.playerCards = ...
        // this.clientView = ...
    }



    public boolean isHasThePlayerGot20Points() {
        return hasThePlayerGot20Points;
    }

    public void setHasThePlayerGot20Points(boolean hasThePlayerGot20Points) {
        this.hasThePlayerGot20Points = hasThePlayerGot20Points;
    }
}

