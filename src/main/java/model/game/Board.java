package model.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import exceptions.CantPlaceYourCardHere;
import exceptions.IllegalPlacementException;
import model.card.Card;
import model.card.InitialCard;
import model.card.ObjectiveCard;
import model.objectiveCardTypes.*;

import java.util.*;


public class Board {
    private Node[][] nodes;
    private InitialCard initialCard;
    private ArrayList<Card> cardsOnTheBoardList;
    private int numOfEmpty; //int that counts all the empty SpecificSeed on the Board
    private SpecificSeed initEmptyValue; //this helps us to initialize all the nodes as empty at the start

    public Board(int rows, int cols) {                                                      //initializing all the nodes using the constructor
        cardsOnTheBoardList = new ArrayList<>();
        nodes = new Node[rows][cols];
        this.initEmptyValue =SpecificSeed.EMPTY;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++) {
                nodes[row][column] = new Node(SpecificSeed.EMPTY, row, column);
                this.numOfEmpty++;                                                           //NumOfEmpty is an int that checks if we're doing things correctly
            }
        }
    } // BOARD CONSTRUCTOR
    public void printBoard() { //printBoard method
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if(nodeIsUsed(nodes[i][j].getValueCounter()))
                {
                    System.out.print(nodes[i][j].getSpecificNodeSeed() + "\t" + j + "\t" + i + ", value:" + nodes[i][j].getValueCounter() + " |");
                }
                else {
                    System.out.print("[VUOTO]\t" + j + "\t" + i + ", value:0 |");
                }
            }
            System.out.println();
        }
    } //PRINTING THE BOARD

   public String printBoardForServer() { //printBoard method
        StringBuilder stringBoard = new StringBuilder();
        Boolean aCapo=false;
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if(nodeIsUsed(nodes[i][j].getValueCounter())){
                    if(!aCapo){
                        stringBoard.append("\n");
                        aCapo=true;
                    }
                    stringBoard.append(nodes[i][j].getSpecificNodeSeed() + "\t" + j + "\t" + i + ", value:" + nodes[i][j].getValueCounter() + " |");
                }
            }
            aCapo=false;
        }
        stringBoard.append("\nfine board");
        return String.valueOf(stringBoard);
    }





    private boolean nodeIsUsed(int node) {
        return node<2;
    }

    public int[][] getCentralCoordinates() {
        int rows = nodes.length;
        int cols = nodes[0].length;
        int[][] centralCoordinates = new int[2][2];
        centralCoordinates[0][0] = rows / 2 - 1; // x
        centralCoordinates[0][1] = cols / 2 - 1; // y
        centralCoordinates[1][0] = rows / 2;     // x
        centralCoordinates[1][1] = cols / 2;     // y
        return centralCoordinates;
    } //these coordinates are going to be the default-initial cards-coordinates (middle of the board)

    public boolean placeGoldCard( List<SpecificSeed> requirementsForPlacing) {
        BoardPoints boardPoints = new BoardPoints(); //CREATING THE BOARD-POINTS TO COUNT ALL THE SPECIFIED ON THE BOARD
        Map<SpecificSeed, Integer> seedCountMap = boardPoints.countPoints(this); //PASSING THE BOARD
        Map<SpecificSeed, Integer> requiredCountMap = new HashMap<>(); //CREATING A MAP TO CHECK THE ATTRIBUTES
        for (SpecificSeed seed : requirementsForPlacing) {
            requiredCountMap.put(seed, requiredCountMap.getOrDefault(seed, 0) + 1); //COUNTING
        }
        for (Map.Entry<SpecificSeed, Integer> entry : requiredCountMap.entrySet()) {
            SpecificSeed requiredSeed = entry.getKey(); //GET KEY TAKES THE SPECIFIC SEED IN THE REQUIREMENTS
            int requiredCount = entry.getValue();       //GET VALUE TAKES THE NUMBER OF THE ATTRIBUTES
            int actualCount = seedCountMap.getOrDefault(requiredSeed, 0); //HOW MANY SPECIFIED DO WE HAVE ACTUALLY ON THE BOARD
            if (actualCount < requiredCount) { //IN CASO CORREGGERE IL SEGNO
                throw new IllegalPlacementException("Not enough "+ requiredSeed + " on the board!");
            }
        }
        return true;
    } //method to see if the attributes are on the board for the gold cards

    public boolean placeInitialCard(InitialCard initialCard) { //METHOD TO PLACE THE FIRST CARD WHICH IS CHOSEN RANDOMLY
        int[][] centralCoordinates = getCentralCoordinates(); //GETTING THE CENTRAL COORDINATES OF THE BOARD
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        //IS THE INITIAL CARD ALREADY BEEN PLACED?
        if (nodeIsUsed(getNode(centerX, centerY).getValueCounter())) { //USING VALUE-COUNTER TO CHECK IF A INITIAL CARD HAD ALREADY BEEN PLACED
            System.out.printf("Initial card id:" + initialCard.getId() + " Already Placed!");
            return false;
        }
        if (initialCard.isCardBack()) { //initial card shows back corners cause the attribute is true
            return placeBackInitialCard(initialCard);
        } else {
            return placeFrontInitialCard(initialCard);
        }
    }//method to place the first card

    public void printCornerCoordinates() {
        int[][] centralCoordinates = getCentralCoordinates();
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        // PRINTING THE COORDINATES JUST TO DEBUG
        System.out.println("Initial card coordinates:");
        System.out.println("TL: (" + centerX + ", " + centerY + ")");
        System.out.println("TR: (" + centerX + ", " + (centerY + 1) + ")");
        System.out.println("BL: (" + (centerX + 1) + ", " + centerY + ")");
        System.out.println("BR: (" + (centerX + 1) + ", " + (centerY + 1) + ")");
    } //METHOD TO PRINT THE COORDINATES OF THE CENTRAL COORDINATES

    //CREATING SPECIFIC OBJECTIVE REQUIREMENTS
    public ExtendExtendExtend createSpecificSecretCard(ObjectiveCard card, Player player) {
        if(seedCardEquals("STAIRS", card)){

            StairsObjectiveCard stairsObjectiveCard=new StairsObjectiveCard();
            stairsObjectiveCard.checkPattern(this, card.getType(), player);
        }
        if(seedCardEquals("L", card))
        {
            LObjectiveCard lObjectiveCard=new LObjectiveCard();
            lObjectiveCard.checkPattern(this, card.getType(), player);
        }
        if(seedCardEquals("MIX", card)) {

            MixObjectiveCard mixObjectiveCard=new MixObjectiveCard();
            mixObjectiveCard.checkPattern(this, card.getType(), player);
        }
        if(seedCardEquals("TRIS", card))
        {

            TrisObjectiveCard trisObjectiveCard=new TrisObjectiveCard();
            trisObjectiveCard.checkPattern(this, card.getType(), player);
        }
        if(seedCardEquals("BIS", card))
        {
            BisObjectiveCard bisObjectiveCard=new BisObjectiveCard();
            bisObjectiveCard.checkPattern(this, card.getType(), player);
        }

        return null;
    }

    private boolean seedCardEquals(String seed, ObjectiveCard card) {
        return (Objects.equals(card.getObjectiveSpecificTypeOfCard(), seed));
    }

    public void printCardsOnTheBoard() {
        System.out.println("Cards on the board are:"); //PLN
        for (Card card : cardsOnTheBoardList) {
            System.out.println(card);
        }
        System.out.println("Card finished"); //I PRINTED ALL THE CARDS I HAVE ON MY BOARD
    }

    public boolean placeBackInitialCard(InitialCard initialCard) {
        int[][] centralCoordinates = getCentralCoordinates(); //GETTING THE CENTRAL COORDINATES OF THE BOARD
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        try {
            if(CanIPlaceTheCard(centerX, centerY)){//CHECKING IF I CAN PLACE THE CARD ON THE BOARD
                Corner TOPLEFT = initialCard.getTLIBack();                            //TOP LEFT BACK
                SpecificSeed TOPLEFTING = TOPLEFT.getSpecificCornerSeed();
                getNode(centerX, centerY).setSpecificNodeSeed(TOPLEFTING);       //Setting the node of the initial Card
                getNode(centerX, centerY).setFirstPlacement(TOPLEFTING);             //Keeping trace of the history on the board
                getNode(centerX, centerY).setValueCounter(getNode(centerX, centerY).getValueCounter() - 1); //VALUE-COUNTER OF THE SPECIFIC NODE -1
                initialCard.setNode(getNode(centerX, centerY));                     //SETTING THE COORDINATE OF THE INITIAL CARD
                TOPLEFT.setValueCounter(TOPLEFT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner TOPRIGHT = initialCard.getTRIBack();                           //UPRIGHT
                SpecificSeed TOPRIGHTING = TOPRIGHT.getSpecificCornerSeed();
                getNode(centerX, centerY + 1).setSpecificNodeSeed(TOPRIGHTING); //Setting the node of the initial Card
                getNode(centerX, centerY + 1).setFirstPlacement(TOPRIGHTING);      //Keeping trace of the history on the board
                getNode(centerX, centerY + 1).setValueCounter(getNode(centerX, centerY + 1).getValueCounter() - 1); //VALUE-COUNTER OF THE SPECIFIC NODE -1
                TOPRIGHT.setValueCounter(TOPRIGHT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner BOTTOMLEFT = initialCard.getBLIBack();                             //BOTTOMLEFT
                SpecificSeed BOTTOMLEFTING = BOTTOMLEFT.getSpecificCornerSeed();
                getNode(centerX + 1, centerY).setSpecificNodeSeed(BOTTOMLEFTING);  //Setting the node of the initial Card
                getNode(centerX + 1, centerY).setFirstPlacement(BOTTOMLEFTING);    //Keeping trace of the history on the board
                getNode(centerX + 1, centerY).setValueCounter(getNode(centerX + 1, centerY).getValueCounter() - 1);//VALUE-COUNTER OF THE SPECIFIC NODE -1
                BOTTOMLEFT.setValueCounter(BOTTOMLEFT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner BOTTOMRIGHT = initialCard.getBRIBack();                                    //BOTTOM-RIGHT
                SpecificSeed BOTTOMRIGHITING = BOTTOMRIGHT.getSpecificCornerSeed();
                getNode(centerX + 1, centerY + 1).setSpecificNodeSeed(BOTTOMRIGHITING);      //Setting the node of the initial Card
                getNode(centerX + 1, centerY + 1).setFirstPlacement(BOTTOMRIGHITING);        //Keeping trace of the history on the board
                getNode(centerX + 1, centerY + 1).setValueCounter(getNode(centerX + 1, centerY + 1).getValueCounter() - 1);//VALUE-COUNTER OF THE SPECIFIC NODE -1
                BOTTOMRIGHT.setValueCounter(BOTTOMRIGHT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                this.numOfEmpty = numOfEmpty - 4;
                initialCard.setIndexOnTheBoard(1); //SETTING THE INDEX ON THE FIRST CARD PLACED
                cardsOnTheBoardList.add(initialCard); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARD PLACED ON THE BOARD ****HISTORIC****
                System.out.println("Cards on the board are:"); //PLN
                for (Card card : cardsOnTheBoardList) {
                    System.out.println(card);
                }
                System.out.println("Card finished"); //I PRINTED ALL THE CARDS I HAVE ON MY BOARD
            }
        } catch (Exception e) {
            throw new CantPlaceYourCardHere("Can't Place your card", e);
        }
        System.out.println("Initial Card correctly placed"); //THE CARD HAD BEEN PLACED CORRECTLY

        return true;
    }

    public boolean placeFrontInitialCard(InitialCard initialCard) {
        int[][] centralCoordinates = getCentralCoordinates(); //GETTING THE CENTRAL COORDINATES OF THE BOARD
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        try {
            if(CanIPlaceTheCard(centerX, centerY)){ //CHECKING IF I CAN PLACE THE CARD ON THE BOARD
                Corner TOPLEFT = initialCard.getTL();                            //TOP LEFT
                SpecificSeed TOPLEFTING = TOPLEFT.getSpecificCornerSeed();
                getNode(centerX, centerY).setSpecificNodeSeed(TOPLEFTING);       //Setting the node of the initial Card
                getNode(centerX, centerY).setFirstPlacement(TOPLEFTING);             //Keeping trace of the history on the board
                getNode(centerX, centerY).setValueCounter(getNode(centerX, centerY).getValueCounter() - 1); //VALUE-COUNTER OF THE SPECIFIC NODE -1
                initialCard.setNode(getNode(centerX, centerY));                     //SETTING THE COORDINATE OF THE INITIAL CARD
                TOPLEFT.setValueCounter(TOPLEFT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner TOPRIGHT = initialCard.getTR();                           //UPRIGHT
                SpecificSeed TOPRIGHTING = TOPRIGHT.getSpecificCornerSeed();
                getNode(centerX, centerY + 1).setSpecificNodeSeed(TOPRIGHTING); //Setting the node of the initial Card
                getNode(centerX, centerY + 1).setFirstPlacement(TOPRIGHTING);      //Keeping trace of the history on the board
                getNode(centerX, centerY + 1).setValueCounter(getNode(centerX, centerY + 1).getValueCounter() - 1); //VALUE-COUNTER OF THE SPECIFIC NODE -1
                TOPRIGHT.setValueCounter(TOPRIGHT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner BOTTOMLEFT = initialCard.getBL();                             //BOTTOMLEFT
                SpecificSeed BOTTOMLEFTING = BOTTOMLEFT.getSpecificCornerSeed();
                getNode(centerX + 1, centerY).setSpecificNodeSeed(BOTTOMLEFTING);  //Setting the node of the initial Card
                getNode(centerX + 1, centerY).setFirstPlacement(BOTTOMLEFTING);    //Keeping trace of the history on the board
                getNode(centerX + 1, centerY).setValueCounter(getNode(centerX + 1, centerY).getValueCounter() - 1);//VALUE-COUNTER OF THE SPECIFIC NODE -1
                BOTTOMLEFT.setValueCounter(BOTTOMLEFT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                Corner BOTTOMRIGHT = initialCard.getBR();                                    //BOTTOM-RIGHT
                SpecificSeed BOTTOMRIGHITING = BOTTOMRIGHT.getSpecificCornerSeed();
                getNode(centerX + 1, centerY + 1).setSpecificNodeSeed(BOTTOMRIGHITING);      //Setting the node of the initial Card
                getNode(centerX + 1, centerY + 1).setFirstPlacement(BOTTOMRIGHITING);        //Keeping trace of the history on the board
                getNode(centerX + 1, centerY + 1).setValueCounter(getNode(centerX + 1, centerY + 1).getValueCounter() - 1);//VALUE-COUNTER OF THE SPECIFIC NODE -1
                BOTTOMRIGHT.setValueCounter(BOTTOMRIGHT.getValueCounter() - 1);               //DECREASING ALSO THE CORNER VALUE

                this.numOfEmpty = numOfEmpty - 4;
                initialCard.setIndexOnTheBoard(1); //SETTING THE INDEX ON THE FIRST CARD PLACED
                cardsOnTheBoardList.add(initialCard); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARD PLACED ON THE BOARD ****HISTORIC****
            }
        } catch (Exception e) {
            throw new CantPlaceYourCardHere("Can't Place your card", e);
        }
        System.out.println("Initial Card correctly placed"); //THE CARD HAD BEEN PLACED CORRECTLY
        return true;
    }

    private boolean CanIPlaceTheCard(int centerX, int centerY) {
        return (centerX >= 0 && centerX < nodes.length && centerY >= 0 && centerY < nodes[0].length);
    }

    //GETTER AND SETTER

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }
    public void setInitialCard(InitialCard initialCard) {
        this.initialCard = initialCard;
    }
    public void setCardsOnTheBoardList(ArrayList<Card> cardsOnTheBoardList) {
        this.cardsOnTheBoardList = cardsOnTheBoardList;
    }
    public void setNumOfEmpty(int numOfEmpty) {
        this.numOfEmpty = numOfEmpty;
    }
    public void setInitEmptyValue(SpecificSeed initEmptyValue) {
        this.initEmptyValue = initEmptyValue;
    }
    public Node[][] getNodes() {
        return nodes;
    }
    public InitialCard getInitialCard() {
        return initialCard;
    }
    public ArrayList<Card> getCardsOnTheBoardList() {
        return cardsOnTheBoardList;
    }
    public int getNumOfEmpty() {
        return numOfEmpty;
    }
    public SpecificSeed getInitEmptyValue() {
        return initEmptyValue;
    }
    public Node getNode(int x, int y) { //getnode method
        return nodes[x][y];
    }
    public void setNode(int x, int y, Node node) {
        nodes[x][y] = node;
    }
    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();

        // Convert nodes to JSON
        JsonArray nodesArray = new JsonArray();
        for (int row = 0; row < nodes.length; row++) {
            JsonArray rowArray = new JsonArray();
            for (int col = 0; col < nodes[row].length; col++) {
                JsonObject nodeObject = new JsonObject();
                nodeObject.addProperty("seed", nodes[row][col].getSeed().ordinal());
                nodeObject.addProperty("row", row);
                nodeObject.addProperty("col", col);
                rowArray.add(nodeObject);
            }
            nodesArray.add(rowArray);
        }
        jsonObject.add("nodes", nodesArray);

        // Add other properties
        jsonObject.addProperty("numOfEmpty", numOfEmpty);
        jsonObject.addProperty("initEmptyValue", initEmptyValue.ordinal());

        return jsonObject;
    }
}

}
