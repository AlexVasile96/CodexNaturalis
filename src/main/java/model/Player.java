package model;

import java.util.*;

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
        this.playerCards = new ArrayList<>(3);
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
    } //METHOD TO CHOOSE THE SECRET CARD (THE PLAYER HAS A CHOICE BETWEEN 2 CARDS)

    public void playCard(Board board, int cardIndex) {      //METHOD TO PLACE THE CARD CHOSEN BEFORE ON THE BOARD
        Card selectedCardFromTheDeck = chooseCard(cardIndex);          //SELECTEDCARD IS THE CARD CHOSEN FROM THE PLAYER DECK
        if (selectedCardFromTheDeck == null) {                         //CHECKING IF THE CARD EXISTS, IN CASE RETURN
            return;
        }
        if (selectedCardFromTheDeck instanceof GoldCard) {
            boolean checker = board.placeGoldCard(((GoldCard) selectedCardFromTheDeck).getRequirementsForPlacing()); //CHECKING IF THE REQUIRMENTS ARE RESPECTED
            if (!checker) return; //checker==false
        }
        Card initialCard = board.getCardsOnTheBoardList().get(0); //putting inside initialCard the firstPlacedCard on the board
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList(); //VISUALIZING ALL THE CARDS ON THE BOARD SO THE PLAYER CAN CHOOSE ONE OF THEM
        System.out.println("Cards on the board are:");                        //PRINTING THE CARDS ON THE BOARD
        for (int i = 0; i < cardsPlayerCanChooseFrom.size(); i++) {
            Card card = cardsPlayerCanChooseFrom.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a card on your board you want to place the card from your deck on: ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > cardsPlayerCanChooseFrom.size()) {
            System.out.println("Not valid index");
            return;
        }

        Card cardPlayerChoose = cardsPlayerCanChooseFrom.get(selectedCardIndex - 1);
        System.out.println("Card correctly chosen");
        List<Corner> availableCorners = new ArrayList<>();
        availableCorners.add(cardPlayerChoose.getTL());
        availableCorners.add(cardPlayerChoose.getTR());
        availableCorners.add(cardPlayerChoose.getBL());
        availableCorners.add(cardPlayerChoose.getBR());

        if (cardPlayerChoose.getId() == initialCard.getId()) {
            List<Corner> initialCardCorners = new ArrayList<>();
            initialCardCorners.add(initialCard.getTL());
            initialCardCorners.add(initialCard.getTR());
            initialCardCorners.add(initialCard.getBL());
            initialCardCorners.add(initialCard.getBR());
            for (int i = initialCardCorners.size() - 1; i >= 0; i--) {
                if (initialCardCorners.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON) {
                    availableCorners.remove(i);
                }
                if (initialCardCorners.get(i).getValueCounter() == 0) {
                    availableCorners.remove(i);
                }
            }
        } else {
            List<Corner> corner = new ArrayList<>();
            corner.add(cardPlayerChoose.getTL());
            corner.add(cardPlayerChoose.getTR());
            corner.add(cardPlayerChoose.getBL());
            corner.add(cardPlayerChoose.getBR());
            for (int i = corner.size() - 1; i >= 0; i--) {
                if (corner.get(i).getSpecificCornerSeed() == SpecificSeed.NOTTOBEPLACEDON) {
                    availableCorners.remove(i);
                    corner.remove(i);
                }
                if (corner.get(i).getValueCounter() == 0) {
                    availableCorners.remove(i);
                    corner.remove(i);
                }
            }
        }

        Map<Corner, String> cornerLabels = new HashMap<>();
        cornerLabels.put(cardPlayerChoose.getTL(), "TL");
        cornerLabels.put(cardPlayerChoose.getTR(), "TR");
        cornerLabels.put(cardPlayerChoose.getBL(), "BL");
        cornerLabels.put(cardPlayerChoose.getBR(), "BR");

        System.out.println("Free Corners of the selected card ");
        for (int i = 0; i < availableCorners.size(); i++) {
            Corner corner = availableCorners.get(i);
            String cornerLabel = cornerLabels.get(corner);
            System.out.println((i + 1) + ". " + corner + " -> " + cornerLabel);
        }

        System.out.print("Choose the corner you want to place the card on: ");
        String selectedCorner = scanner.next().toUpperCase();
        if (!selectedCorner.equals("TL") && !selectedCorner.equals("TR") && !selectedCorner.equals("BL") && !selectedCorner.equals("BR")) {
            System.out.println("Invalid corner selection.");
            return;
        }

        int cornerIndex = -1;
        switch (selectedCorner) {
            case "TL":
                cardPlayerChoose.getTL().setValueCounter(cardPlayerChoose.getTL().getValueCounter()-1);
                cornerIndex = 1;
                break;
            case "TR":
                cardPlayerChoose.getTR().setValueCounter(cardPlayerChoose.getTR().getValueCounter()-1);
                cornerIndex = 2;
                break;
            case "BL":
                cardPlayerChoose.getBL().setValueCounter(cardPlayerChoose.getBL().getValueCounter()-1);
                cornerIndex = 3;
                break;
            case "BR":
                cardPlayerChoose.getBR().setValueCounter(cardPlayerChoose.getBR().getValueCounter()-1);
                cornerIndex = 4;
                break;
        }


        int x = cardPlayerChoose.getNode().getCoordX(); //SAVING THE TOPLEFT COORDS OF THE CARD THE PLAYER DECIDED TO PLACE THE SELECTED CARD ON
            int y = cardPlayerChoose.getNode().getCoordY();

        selectedCardFromTheDeck.getBR().setValueCounter(selectedCardFromTheDeck.getBR().getValueCounter()-1);
        selectedCardFromTheDeck.getBL().setValueCounter(selectedCardFromTheDeck.getBL().getValueCounter()-1);
        selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);
        selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);


        if (cornerIndex == 1) {//We are in Top Left corner case

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

                //cardPlayerChoose.getTL().setValueCounter(cardPlayerChoose.getTL().getValueCounter()-1);


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
                System.out.println(board.getNode(x, y).getFirstPlacement());
            }
            if (cornerIndex == 2) {//We are in Top Right corner case

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
            if (cornerIndex == 3) {//We are in Bottom Left corner case

                selectedCardFromTheDeck.getTR().setValueCounter(selectedCardFromTheDeck.getTR().getValueCounter()-1);


                board.getNode(x + 1, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 1, y - 1).setValueCounter(board.getNode(x + 1, y - 1).getValueCounter() - 1); // Decrease the value
                selectedCardFromTheDeck.setNode(board.getNode(x + 1, y - 1)); //SAVING THE POSITION
                board.getNode(x + 1, y).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 1, y).setValueCounter(board.getNode(x + 1, y).getValueCounter() - 1); // Decrease the value
                board.getNode(x + 2, y - 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 2, y - 1).setValueCounter(board.getNode(x + 2, y - 1).getValueCounter() - 1); // Decrease the value
                board.getNode(x + 2, y).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 2, y).setValueCounter(board.getNode(x + 2, y).getValueCounter() - 1); // Decrease the value
            }
            if (cornerIndex == 4) {//We are in Bottom Right corner case

                selectedCardFromTheDeck.getTL().setValueCounter(selectedCardFromTheDeck.getTL().getValueCounter()-1);

                board.getNode(x + 1, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getTL().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 1, y + 1).setValueCounter(board.getNode(x + 1, y + 1).getValueCounter() - 1); // Decrease the value
                selectedCardFromTheDeck.setNode(board.getNode(x + 1, y + 1)); //SAVING THE POSITION
                board.getNode(x + 1, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getTR().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 1, y + 2).setValueCounter(board.getNode(x + 1, y + 2).getValueCounter() - 1); // Decrease the value
                board.getNode(x + 2, y + 1).setSpecificNodeSeed(selectedCardFromTheDeck.getBL().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 2, y + 1).setValueCounter(board.getNode(x + 2, y + 1).getValueCounter() - 1); // Decrease the value
                board.getNode(x + 2, y + 2).setSpecificNodeSeed(selectedCardFromTheDeck.getBR().getSpecificCornerSeed());//SETTING THE NODE
                board.getNode(x + 2, y + 2).setValueCounter(board.getNode(x + 2, y + 2).getValueCounter() - 1); // Decrease the value
            }

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
            System.out.println("Your new score is " + playerScore + "points");
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
}
