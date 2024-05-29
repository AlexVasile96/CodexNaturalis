package it.polimi.ingsw.model.game;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.card.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.view.ClientView;

import java.util.*;

public class Player implements Observable {
    private final String nickName;
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

    public synchronized String drawResourceCard(ResourceDeck deck) {
        deck.drawCard(this);
        return "card drawn correctly from Resource deck";
    }
    public synchronized String drawGoldCard(GoldDeck deck) {
        deck.drawCard(this);
        return "card drawn correctly from Gold deck";
    }
    public void chooseCardFromWell(List<Card>cardWell, ResourceDeck rc, GoldDeck gd) {
        Scanner scanner = new Scanner(System.in);
        if (this.playerCards.size() < 3) {
            if (cardWell.isEmpty()) {
                return; //empty well
            }
            try {
                for (Card card : cardWell) {
                    System.out.println(card);
                }
                Card drownCard= choosingTheSpecificCardFromTheWell(scanner, cardWell); //Choosing the card and saving it
                fillingTheWellWithTheCorrectCard(drownCard,rc,gd, cardWell);           //Filling The Well
                playerCards.add(drownCard);                                             //Adding the card to the player hand
            } catch (Exception e) {
                throw new IllegalStateException("Well is empty"); // Specific exception
            }
        }
        else {
            System.out.println("Player's deck already has 3 cards\n");
        }
    }
    public String chooseCardFromWellForServer(List<Card>cardWell, int index, ResourceDeck rc, GoldDeck gd) {
        if (this.playerCards.size() < 3) {
            if (cardWell.isEmpty()) {
                System.out.println("Well is empty");
                return "Well is empty"; //empty well
            }
            try {
                for (Card card : cardWell) {
                    System.out.println(card);
                }
                Card drownCard = choosingTheSpecificCardFromTheWellForServer(index, cardWell); //Choosing the card and saving it
                fillingTheWellWithTheCorrectCard(drownCard,rc,gd, cardWell);           //Filling The Well
                playerCards.add(drownCard);                                             //Adding the card to the player hand
            } catch (Exception e) {
                throw new IllegalStateException("Error in the try-catch statement of ChooseCardFromWellServer"); //Specific exception
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
            System.out.println("Choose the secret card you want to draw: ");
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
            card.getTL().setSpecificCornerSeed(SpecificSeed.EMPTY,card.getTL().getCardSeed()); //SETTING ALL THE CORNERS AS EMPTY
            card.getTR().setSpecificCornerSeed(SpecificSeed.EMPTY,card.getTR().getCardSeed());
            card.getBL().setSpecificCornerSeed(SpecificSeed.EMPTY,card.getBL().getCardSeed());
            card.getBR().setSpecificCornerSeed(SpecificSeed.EMPTY,card.getBR().getCardSeed());
        } else {
            card.setCardBack(false);                                                       //CARD IS ON ITS ORIGINAL CONFIGURATION
            card.getTL().setSpecificCornerSeed(card.getTLBack().getSpecificCornerSeed(),card.getTL().getCardSeed()); //BACK UPPING ALL CORNERS
            card.getTR().setSpecificCornerSeed(card.getTRBack().getSpecificCornerSeed(),card.getTR().getCardSeed());
            card.getBL().setSpecificCornerSeed(card.getBLBack().getSpecificCornerSeed(),card.getBL().getCardSeed());
            card.getBR().setSpecificCornerSeed(card.getBRBack().getSpecificCornerSeed(), card.getBR().getCardSeed());
        }
    }

    public Card checkingTheChosencard( int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);                   //OKAY
        System.out.println("card existence:"+ checkIfTheCardExist(cardIndex)+ " (if zero-> doesn't exists)");                                         //CHECKING IF THE CARD TRULY EXISTS->OKAY
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirements are respected->OKAY
        if(selectedCardFromTheDeck.isCardBack()) return selectedCardFromTheDeck;
        if(!canIPLaceTheGoldCard && selectedCardFromTheDeck.getId()>40) return null;
        return selectedCardFromTheDeck;
    }
    public boolean checkingTheChosenCardForGoldPurpose(int cardIndex) {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);                   //OKAY
        System.out.println("card existence:"+ checkIfTheCardExist(cardIndex)+ "  (if zero-> doesn't exists)");                                         //CHECKING IF THE CARD TRULY EXISTS->OKAY
        boolean canIPLaceTheGoldCard= isTheCardGold(selectedCardFromTheDeck);   //CHECKING IF THE CARD IS GOLD && requirements are respected->OKAY
        if(selectedCardFromTheDeck.isCardBack()) return true;
        return canIPLaceTheGoldCard || selectedCardFromTheDeck.getId() <= 40;
    }

    public boolean checkTheGoldCardForGui(int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);
        return isTheCardGold(selectedCardFromTheDeck);
    }

    public Card gettingCardsFromTheBoard(Board board, int cardChosenONTheBoard)
    {
        Card initialCard = board.getCardsOnTheBoardList().getFirst();            //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList();   //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM
        return cardsPlayerCanChooseFrom.get(cardChosenONTheBoard);
    }
    public String isTheCardChosenTheInitialCard(Card cardPlayerChoose, InitialCard initialCard){

        if (cardPlayerChoose.getId() == initialCard.getId()) {        //THE  CARD CHOSEN ON THE BOARD IS THE INITIAL CARD AND WE HAVE TO DELETE THE CORNERS NOT AVAILABLE
            List<Corner> availableCorners = creatingCorners(initialCard);
            return freeCornersOnTheBoard(availableCorners, cardPlayerChoose);
        } else {                                                        //CARD CHOSEN ISN'T THE INITIAL CARD
            List<Corner> availableCorners= creatingCornersForNotInitialCard(cardPlayerChoose);
            return freeCornersOnTheBoard(availableCorners, cardPlayerChoose);
        }

    }

    public void playInitCardOnBoard(Board board, int cardIndex, Card selectedCardFromTheDeck, InitialCard cardPlayerChoose, String selectedCorner)
    {
        int x = cardPlayerChoose.getNode().getCordX(); //SAVING THE TOP LEFT CORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y = cardPlayerChoose.getNode().getCordY();
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
        decreasingAllTheValuesOfTheCornerPlaced(selectedCardFromTheDeck); //DECREASING ALL VALUE COUNTER BECAUSE ALL CORNERS ARE GOING TO BE PLACED ON THE BOARD
        selectedCardFromTheDeck.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new incremented index
        board.getCardsOnTheBoardList().add(selectedCardFromTheDeck); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
        this.playerCards.remove(cardIndex); //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
        board.setNumOfEmpty(board.getNumOfEmpty() - 3);
        updatingPoints(selectedCardFromTheDeck); //Updating player Points
        if (playerScore >= 20) {                                            //EndGame if the player points=>20 points
            System.out.println("Player " + getNickName() + "has reached 20 points!\n");
        }
    }


    public void playCard(Board board, int cardIndex, int cardChosenONTheBoard,Card selectedCardFromTheDeck, Card cardPlayerChoose, String selectedCorner) { //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        int x = cardPlayerChoose.getNode().getCordX(); //SAVING THE TOP LEFT CORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
        int y = cardPlayerChoose.getNode().getCordY();
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
        decreasingAllTheValuesOfTheCornerPlaced(selectedCardFromTheDeck); //DECREASING ALL VALUE COUNTER BECAUSE ALL CORNERS ARE GOING TO BE PLACED ON THE BOARD
        selectedCardFromTheDeck.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new incremented index
        board.getCardsOnTheBoardList().add(selectedCardFromTheDeck); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARDS ON THE BOARD
        this.playerCards.remove(cardIndex); //REMOVING THE CARD THE PLAYER PLACED FROM HIS HAND
        board.setNumOfEmpty(board.getNumOfEmpty() - 3);
        updatingPoints(selectedCardFromTheDeck); //Updating player Points

        if (playerScore >= 20) {
            System.out.println("Player " + getNickName() + "has reached 20 points!\n");
        }
    }

    //METHODS INVOKED FROM THE PREVIOUS METHODS

    private int checkIfTheCardExist(int cardIndex)
    {
        Card selectedCardFromTheDeck = chooseCard(cardIndex);          //SELECTEDCARDFROMTHEDECK IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCardFromTheDeck == null) {                         //CHECKING IF THE CARD EXISTS, IN CASE RETURN
            return 0;
        }
        return selectedCardFromTheDeck.getId();
    }
    private boolean isTheCardGold(Card selectedCard)
    {
        System.out.println(selectedCard);
        if (selectedCard instanceof GoldCard)
            return board.placeGoldCard(((GoldCard) selectedCard).getRequirementsForPlacing()); //Checking gold card requirements
        else return true; //If the card is not a gold card, I can proceed
    }


    private List<Corner> creatingCorners(InitialCard cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                          //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER
        if(cardPlayerChoose.isCardBack()&& cardPlayerChoose.getIndexOnTheBoard()==1)
        {
            if(IsTheTLCornerUsable(cardPlayerChoose.getTLIBack().getSpecificCornerSeed(), cardPlayerChoose.getNode().getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY())) {
                availableCorners.add(cardPlayerChoose.getTLIBack());
            }
            if(IsTheTRCornerUsable(cardPlayerChoose.getTRIBack().getSpecificCornerSeed(), board.getNode(cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY()+1).getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY()+1)) {
                availableCorners.add(cardPlayerChoose.getTRIBack());
            }
            if(IsTheBLCornerUsable(cardPlayerChoose.getBLIBack().getSpecificCornerSeed(), board.getNode(cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY()).getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY())) {
                availableCorners.add(cardPlayerChoose.getBLIBack());
            }
            if(IsTheBRCornerUsable(cardPlayerChoose.getBRIBack().getSpecificCornerSeed(), board.getNode(cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY()+1).getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY()+1)) {
                availableCorners.add(cardPlayerChoose.getBRIBack());
            }
        }else{
            if(IsTheTLCornerUsable(cardPlayerChoose.getTL().getSpecificCornerSeed(), cardPlayerChoose.getTL().getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY())) {
                availableCorners.add(cardPlayerChoose.getTL());
            }
            if(IsTheTRCornerUsable(cardPlayerChoose.getTR().getSpecificCornerSeed(), cardPlayerChoose.getTR().getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY()+1)) {
                availableCorners.add(cardPlayerChoose.getTR());
            }
            if(IsTheBLCornerUsable(cardPlayerChoose.getBL().getSpecificCornerSeed(), cardPlayerChoose.getBL().getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY())) {
                availableCorners.add(cardPlayerChoose.getBL());
            }
            if(IsTheBRCornerUsable(cardPlayerChoose.getBR().getSpecificCornerSeed(), cardPlayerChoose.getBR().getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY()+1)) {
                availableCorners.add(cardPlayerChoose.getBR());
            }
        }
        return availableCorners;
    }

    private List<Corner> creatingCornersForNotInitialCard(Card cardPlayerChoose){
        List<Corner> availableCorners = new ArrayList<>();                          //CREATING CORNERS THAT WILL BE DISPLAYED TO THE PLAYER

        if(IsTheTLCornerUsable(cardPlayerChoose.getTL().getSpecificCornerSeed(), cardPlayerChoose.getTL().getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY())) {
            availableCorners.add(cardPlayerChoose.getTL());
        }
        if(IsTheTRCornerUsable(cardPlayerChoose.getTR().getSpecificCornerSeed(), cardPlayerChoose.getTR().getValueCounter(), cardPlayerChoose.getNode().getCordX(), cardPlayerChoose.getNode().getCordY()+1)) {
            availableCorners.add(cardPlayerChoose.getTR());
        }
        if(IsTheBLCornerUsable(cardPlayerChoose.getBL().getSpecificCornerSeed(), cardPlayerChoose.getBL().getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY())) {
            availableCorners.add(cardPlayerChoose.getBL());
        }
        if(IsTheBRCornerUsable(cardPlayerChoose.getBR().getSpecificCornerSeed(), cardPlayerChoose.getBR().getValueCounter(), cardPlayerChoose.getNode().getCordX()+1, cardPlayerChoose.getNode().getCordY()+1)) {
            availableCorners.add(cardPlayerChoose.getBR());
        }
        return availableCorners;
    }

    private boolean IsTheTLCornerUsable(SpecificSeed seed, int valueCounter, int x, int y) {
        SpecificSeed north = board.getNode(x-1, y).getSpecificNodeSeed();
        SpecificSeed east = board.getNode(x, y-1).getSpecificNodeSeed();
        SpecificSeed northEast = board.getNode(x-1, y-1).getSpecificNodeSeed();
        return seed != SpecificSeed.NOTTOBEPLACEDON && valueCounter > 0 && north != SpecificSeed.NOTTOBEPLACEDON && east != SpecificSeed.NOTTOBEPLACEDON && northEast != SpecificSeed.NOTTOBEPLACEDON;
    }

    private boolean IsTheTRCornerUsable(SpecificSeed seed, int valueCounter, int x, int y) {
        SpecificSeed west= board.getNode(x, y+1).getSpecificNodeSeed();
        SpecificSeed north = board.getNode(x-1, y).getSpecificNodeSeed();
        SpecificSeed northWest = board.getNode(x-1, y+1).getSpecificNodeSeed();
        return seed != SpecificSeed.NOTTOBEPLACEDON && valueCounter > 0 && west != SpecificSeed.NOTTOBEPLACEDON && north != SpecificSeed.NOTTOBEPLACEDON && northWest != SpecificSeed.NOTTOBEPLACEDON;
    }

    private boolean IsTheBLCornerUsable(SpecificSeed seed, int valueCounter, int x, int y) {
        SpecificSeed east = board.getNode(x, y-1).getSpecificNodeSeed();
        SpecificSeed sud = board.getNode(x+1, y).getSpecificNodeSeed();
        SpecificSeed sudEast = board.getNode(x+1, y-1).getSpecificNodeSeed();
        return seed != SpecificSeed.NOTTOBEPLACEDON && valueCounter > 0 && east != SpecificSeed.NOTTOBEPLACEDON && sud != SpecificSeed.NOTTOBEPLACEDON && sudEast != SpecificSeed.NOTTOBEPLACEDON;
    }

    private boolean IsTheBRCornerUsable(SpecificSeed seed, int valueCounter, int x, int y) {
        SpecificSeed sud = board.getNode(x+1, y).getSpecificNodeSeed();
        SpecificSeed west = board.getNode(x, y+1).getSpecificNodeSeed();
        SpecificSeed sudWest = board.getNode(x+1, y+1).getSpecificNodeSeed();
        return seed != SpecificSeed.NOTTOBEPLACEDON && valueCounter > 0 && sud != SpecificSeed.NOTTOBEPLACEDON && west != SpecificSeed.NOTTOBEPLACEDON && sudWest != SpecificSeed.NOTTOBEPLACEDON;
    }

    private String freeCornersOnTheBoard(List<Corner> availableCorners, Card cardPlayerChoose){
        Map<Corner, String> cornerLabels = new HashMap<>();                                 //PUTTING THE CORRECT CORNER LABEL TO THE CORRECT CORNER
        if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()==1){
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTLIBack(), "TL");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getTRIBack(), "TR");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBLIBack(), "BL");
            cornerLabels.put(((InitialCard) cardPlayerChoose).getBRIBack(), "BR");}
        else if(cardPlayerChoose.isCardBack() && cardPlayerChoose.getIndexOnTheBoard()!=1)
        {
            cornerLabels.put(cardPlayerChoose.getTL(), "TL");
            cornerLabels.put(cardPlayerChoose.getTR(), "TR");
            cornerLabels.put(cardPlayerChoose.getBL(), "BL");
            cornerLabels.put(cardPlayerChoose.getBR(), "BR");

        }
        else if(!cardPlayerChoose.isCardBack())
        {
            cornerLabels.put(cardPlayerChoose.getTL(), "TL");
            cornerLabels.put(cardPlayerChoose.getTR(), "TR");
            cornerLabels.put(cardPlayerChoose.getBL(), "BL");
            cornerLabels.put(cardPlayerChoose.getBR(), "BR");
        }
        StringBuilder options = new StringBuilder(); // String that contains the possible options
        options.append("Free Corners of the selected card\n");

        for (int i = 0; i < availableCorners.size(); i++) {
            Corner corner = availableCorners.get(i);
            String cornerLabel = cornerLabels.get(corner);
            options.append(cornerLabel).append("\n");
            options.append((i + 1)).append(". ").append(corner).append(" -> ").append(cornerLabel).append("|Please press ").append(cornerLabel).append(" to select the corner\n");
        }
        options.append("\nend");
        return options.toString();

    }


    /**
     *
     * @param x is the x coordinate on the board
     * @param y is the y coordinate on the board
     * @param selectedCardFromTheDeck is the deck on which we want to place our card
     */

    private void playYourCardOnTheTopLeftCorner(int x,int y, Card selectedCardFromTheDeck)
    {
        selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1); //PLACED CORNER, I CANNOT PUT ANY OTHER THING ON THIS CORNER

        board.getNode(x - 1, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x - 1, y - 1).getValueCounter() == 2) {
            board.getNode(x - 1, y - 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x - 1, y - 1).getValueCounter() == 1) {
            board.getNode(x - 1, y - 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTL().setValueCounter(0);
        }
        board.getNode(x - 1, y - 1).setValueCounter(board.getNode(x - 1, y - 1).getValueCounter() - 1);
        // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x - 1, y - 1)); //SAVING THE POSITION


        board.getNode(x - 1, y).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x - 1, y).getValueCounter() == 2) {
            board.getNode(x - 1, y).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x - 1, y).getValueCounter() == 1) {
            board.getNode(x - 1, y).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTR().setValueCounter(0);
        }
        board.getNode(x - 1, y).setValueCounter(board.getNode(x - 1, y).getValueCounter() - 1); // Decrease the value


        board.getNode(x, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x, y - 1).getValueCounter() == 2) {
            board.getNode(x, y - 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x, y - 1).getValueCounter() == 1) {
            board.getNode(x, y - 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBL().setValueCounter(0);
        }
        board.getNode(x, y - 1).setValueCounter(board.getNode(x, y - 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed()); //SETTING THE NODE
        if (board.getNode(x, y).getValueCounter() == 2) {
            board.getNode(x, y).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x, y).getValueCounter() == 1) {
            board.getNode(x, y).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBR().setValueCounter(0);
        }
        board.getNode(x, y).setValueCounter(board.getNode(x, y).getValueCounter() - 1); // Decrease the value
    }

    /**
     *
     * @param x is the x coordinate on the board
     * @param y is the y coordinate on the board
     * @param selectedCardFromTheDeck is the deck on which we want to place our card
     */

    private void playYourCardOnTheTopRightCorner(int x,int y, Card selectedCardFromTheDeck)
    {
        selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);

        board.getNode(x - 1, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());
        if (board.getNode(x - 1, y + 1).getValueCounter() == 2) {
            board.getNode(x - 1, y + 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x - 1, y + 1).getValueCounter() == 1) {
            board.getNode(x - 1, y + 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTL().setValueCounter(0);
        }
        board.getNode(x - 1, y + 1).setValueCounter(board.getNode(x - 1, y + 1).getValueCounter() - 1); // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x - 1, y + 1)); //SAVING THE POSITION

        board.getNode(x - 1, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x - 1, y + 2).getValueCounter() == 2) {
            board.getNode(x - 1, y + 2).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x - 1, y + 2).getValueCounter() == 1) {
            board.getNode(x - 1, y + 2).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTR().setValueCounter(0);
        }

        board.getNode(x - 1, y + 2).setValueCounter(board.getNode(x - 1, y + 2).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x, y + 1).getValueCounter() == 2) {
            board.getNode(x, y + 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x, y + 1).getValueCounter() == 1) {
            board.getNode(x, y + 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBL().setValueCounter(0);
        }
        board.getNode(x, y + 1).setValueCounter(board.getNode(x, y + 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x, y + 2).getValueCounter() == 2) {
            board.getNode(x, y + 2).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x, y + 2).getValueCounter() == 1) {
            board.getNode(x, y + 2).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBR().setValueCounter(0);
        }
        board.getNode(x, y + 2).setValueCounter(board.getNode(x, y + 2).getValueCounter() - 1); // Decrease the value
    }

    /**
     *
     * @param x is the x coordinate on the board
     * @param y is the y coordinate on the board
     * @param selectedCardFromTheDeck is the deck on which we want to place our card
     */


    private void playYourCardOnTheBottomLeftCorner(int x,int y, Card selectedCardFromTheDeck){
        selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);


        board.getNode(x + 1, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE

        if (board.getNode(x + 1, y - 1).getValueCounter() == 2) {
            board.getNode(x + 1, y - 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 1, y - 1).getValueCounter() == 1) {
            board.getNode(x + 1, y - 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTL().setValueCounter(0);
        }
        board.getNode(x + 1, y - 1).setValueCounter(board.getNode(x + 1, y - 1).getValueCounter() - 1); // Decrease the value

        selectedCardFromTheDeck.setNode(board.getNode(x + 1, y - 1)); //SAVING THE POSITION

        board.getNode(x + 1, y).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y).getValueCounter() == 2) {
            board.getNode(x + 1, y).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 1, y).getValueCounter() == 1) {
            board.getNode(x + 1, y).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTR().setValueCounter(0);
        }
        board.getNode(x + 1, y).setValueCounter(board.getNode(x + 1, y).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE

        if (board.getNode(x + 2, y - 1).getValueCounter() == 2) {
            board.getNode(x + 2, y - 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 2, y - 1).getValueCounter() == 1) {
            board.getNode(x + 2, y - 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBL().setValueCounter(0);
        }
        board.getNode(x + 2, y - 1).setValueCounter(board.getNode(x + 2, y - 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y).getValueCounter() == 2) {
            board.getNode(x + 2, y).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 2, y).getValueCounter() == 1) {
            board.getNode(x + 2, y).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBR().setValueCounter(0);
        }
        board.getNode(x + 2, y).setValueCounter(board.getNode(x + 2, y).getValueCounter() - 1); // Decrease the value
    }

    /**
     *
     * @param x is the x coordinate on the board
     * @param y is the y coordinate on the board
     * @param selectedCardFromTheDeck is the deck on which we want to place our card
     */


    private void playYourCardOnTheBottomRightCorner(int x,int y, Card selectedCardFromTheDeck){
        selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);

        board.getNode(x + 1, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y + 1).getValueCounter() == 2) {
            board.getNode(x + 1, y + 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 1, y + 1).getValueCounter() == 1) {
            board.getNode(x + 1, y + 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTL().setValueCounter(0);
        }
        board.getNode(x + 1, y + 1).setValueCounter(board.getNode(x + 1, y + 1).getValueCounter() - 1); // Decrease the value
        selectedCardFromTheDeck.setNode(board.getNode(x + 1, y + 1)); //SAVING THE POSITION


        board.getNode(x + 1, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 1, y + 2).getValueCounter() == 2) {
            board.getNode(x + 1, y + 2).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 1, y + 2).getValueCounter() == 1) {
            board.getNode(x + 1, y + 2).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getTR().setValueCounter(0);
        }
        board.getNode(x + 1, y + 2).setValueCounter(board.getNode(x + 1, y + 2).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y + 1).getValueCounter() == 2) {
            board.getNode(x + 2, y + 1).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 2, y + 1).getValueCounter() == 1) {
            board.getNode(x + 2, y + 1).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBL().setValueCounter(0);
        }
        board.getNode(x + 2, y + 1).setValueCounter(board.getNode(x + 2, y + 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 2, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
        if (board.getNode(x + 2, y + 2).getValueCounter() == 2) {
            board.getNode(x + 2, y + 2).setFirstPlacement(selectedCardFromTheDeck.getType());
        } else if (board.getNode(x + 2, y + 2).getValueCounter() == 1) {
            board.getNode(x + 2, y + 2).setSecondPlacement(selectedCardFromTheDeck.getType());
            selectedCardFromTheDeck.getBR().setValueCounter(0);
        }
        board.getNode(x + 2, y + 2).setValueCounter(board.getNode(x + 2, y + 2).getValueCounter() - 1); // Decrease the value
    }
    private void decreasingAllTheValuesOfTheCornerPlaced(Card selectedCardFromTheDeck)
    {
        if(selectedCardFromTheDeck.getBR().getValueCounter()!=0)
        {
            selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1);
        }
        if(selectedCardFromTheDeck.getTR().getValueCounter()!=0)
        {
            selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);
        }
        if(selectedCardFromTheDeck.getTL().getValueCounter()!=0)
        {
            selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);
        }
        if(selectedCardFromTheDeck.getBL().getValueCounter()!=0)
        {
            selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);
        }
    }
    private void updatingPoints(Card selectedCardFromTheDeck) {
        if(!board.getCardsOnTheBoardList().getLast().isCardBack()) {
            if (selectedCardFromTheDeck.getId() < 41 && selectedCardFromTheDeck.getId() > 0) { //resource card
                ResourceUpdater resourceUpdater = new ResourceUpdater();
                resourceUpdater.updatePlayerPoints(selectedCardFromTheDeck, this, board);

            } else if (selectedCardFromTheDeck.getId() < 81 && selectedCardFromTheDeck.getId() > 40) { //gold card
                GoldUpdater updater = new GoldUpdater();
                updater.updatePlayerPoints((GoldCard) selectedCardFromTheDeck, this, board);
            }
            System.out.println("Your new score is " + playerScore + " points");
        }
    }


    public Card choosingTheSpecificCardFromTheWell(Scanner scanner, List<Card>cardWell){
        System.out.print("Select a card from the well ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > cardWell.size()) {
            System.out.println("Not valid index");
            return null;
        }
        int realIndex = selectedCardIndex - 1;
        return cardWell.remove(realIndex);

    }
    public Card choosingTheSpecificCardFromTheWellForServer(int index, List<Card>cardWell){
        if (index < 0 || index > cardWell.size()-1) {
            System.out.println("Not valid index");
            return null;
        }
        return cardWell.remove(index);

    }
    private void fillingTheWellWithTheCorrectCard(Card drownCard, ResourceDeck rc,GoldDeck gd, List<Card>cardWell)
    {
        if (drownCard.getId() >= 1 && drownCard.getId() <= 40) {
            rc.drawCard(cardWell);
        }
        if (drownCard.getId() >= 41 && drownCard.getId() <= 80) {
            gd.drawCard(cardWell);
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


    public JsonObject toJsonObject(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("score", playerScore);
        jsonObject.addProperty("dot", dot.ordinal());
        jsonObject.add("board", board.toJsonObject());
        jsonObject.addProperty("playerCards", String.valueOf(playerCards));
        jsonObject.addProperty("clientView", String.valueOf(clientView));
        jsonObject.addProperty("secretCard", String.valueOf(secretChosenCard));
        return jsonObject;
    }



    public boolean isHasThePlayerGot20Points() {
        return hasThePlayerGot20Points;
    }

    public void setHasThePlayerGot20Points(boolean hasThePlayerGot20Points) {
        this.hasThePlayerGot20Points = hasThePlayerGot20Points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nickName, player.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}

