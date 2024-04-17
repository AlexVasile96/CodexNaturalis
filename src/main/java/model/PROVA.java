package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PROVA {
    public static void main(String[] args) {
        //INITIALIZING THE BOARD
        BoardPoints boardPoints = new BoardPoints();
        //creating the board which is NOT shared by all the players
        Board board = new Board(50, 50);
        //creating a player
        Player player = new Player("Calla", 0, Dot.GREEN, board);

        //creazione deck
        ResourceCardConstructor resourceCardConstructor = new ResourceCardConstructor(); //create resource cards
        Deck resourceDeck = resourceCardConstructor.createCards(); //create Deck for resourcesCards
        resourceDeck.shuffle(); //SHUFFLING THE RESOURCEDECK
        GoldCardConstructor goldcardConstructor = new GoldCardConstructor(); //create gold cards
        Deck goldDeck = goldcardConstructor.createCards(); //create Deck for goldCards
        goldDeck.shuffle(); //SHUFFLING THE GOLDDECK
        ObjectiveCardConstructor objectiveCardConstructor = new ObjectiveCardConstructor(); //CREATING OBJECTIVE CARDS
        Deck objectiveDeck = objectiveCardConstructor.createCards(); //creating deck for objective cards
        objectiveDeck.shuffle();
        InitCardConstructor initCardConstructor = new InitCardConstructor(); //CREATING INITIAL CARDS
        InitialCardDeck initialCardDeck = (InitialCardDeck) initCardConstructor.createCards(); //creating Deck for the six first card, casting because in the costrcutor i provided a deck class

        //carte comuni a tutti
        List<Card> cardsWell = new ArrayList<>();
        //piazzo nel pozzo 2 risorse e due gold
        resourceDeck.drawCard(cardsWell);
        resourceDeck.drawCard(cardsWell);
        goldDeck.drawCard(cardsWell);
        goldDeck.drawCard(cardsWell);
        System.out.println("Common Cards: ");
        for (Card card : cardsWell) {
            System.out.println(card);
        }
        System.out.println("\n");

        //carte obbiettivo comuni a tutti
        ObjectiveCard firstCommonObjective = objectiveDeck.firstCardForEachPlayer();
        ObjectiveCard secondCommonObjective = objectiveDeck.firstCardForEachPlayer();
        System.out.println("First common objective is " + firstCommonObjective);
        System.out.println("Second common objective is " + secondCommonObjective);

        //creazione carte segrete personali
        List<ObjectiveCard> secretCards = new ArrayList<>();
        secretCards.add((ObjectiveCard) objectiveDeck.drawCard(player));
        secretCards.add((ObjectiveCard) objectiveDeck.drawCard(player));
        player.chooseSecretCard(secretCards); //player choooses his card //non specifica il tipo di "stairs"
        board.createSpecificSecretCard(player.getSecretChosenCard(), player);

        //

        FirstThreeCards firstThreeCards = new FirstThreeCards(player, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        firstThreeCards.yourThreeCards(); //Player Deck initialized
        player.visualizePlayerCards(player.getPlayerCards()); //METHOD TO VISUALIZE THE 3 CARDS THE PLAYER RANDOMLY DREW
        InitialCard initialCard = initialCardDeck.firstCardForPlayer(player); //THE SHUFFLE IS ALREADY //aggiungere opzione gira carta
        System.out.println("\n" +initialCard.toString());                 //PRINTING THE INITIAL CARD

        System.out.println("Flip the card?\n");
        System.out.println("1 = YES    2 = NO\n");

        Scanner scanner = new Scanner(System.in);
        int turnCard = scanner.nextInt();
        if(turnCard == 1){
            initialCard.setCardBack(true);
        }



        board.placeInitialCard(initialCard);                        //PLACING THE INITIAL CARD ON THE BOARD, THIS IS WHERE THE GAME STARTS

        board.placeInitialCard(initialCard);                        //JUST CHECKING IF THE METHOD ACTUALLY PREVENTS FROM PLACING 2 INITIAL CARDS
        board.printCornerCoordinates();
        //GETTING THE INITIAL CARD COORDINATES
        board.printBoard();
        boardPoints.countPoints(board);
        //Player choose the first card he has on his deck, in this case we're talking about a resource card
        //Player places his cards
        player.playCard(board, 0);
        //printing the board
        board.printBoard();
        //printing the cards on the board
        System.out.println(board.getCardsOnTheBoardList());
        //player.drawResourceCard((ResourceDeck) resourceDeck);

        System.out.println("Common Cards: ");

        for (Card card : cardsWell) {
            System.out.println(card);
        }
        System.out.println("\n");
        System.out.println("What kind of card you want: 1-Resources 2-Gold");
        int cardChoice = scanner.nextInt();
        if(cardChoice == 1){
            System.out.println("From resource deck or from well: resource deck = 0; well: select card id\n");
            int chosenCard = scanner.nextInt();
            if(chosenCard == 0){
                resourceDeck.drawCard(player);
            }
            else{
                Card tempCard;
                for(Card card : cardsWell){
                    if(chosenCard == card.id){
                        tempCard = card;
                        player.getPlayerCards().add(tempCard);
                        cardsWell.remove(card.id);
                        resourceDeck.drawCard(cardsWell);
                    }
                }
            }
        }

        player.chooseCardFromWell(cardsWell, (ResourceDeck) resourceDeck, (GoldDeck) goldDeck);
        player.visualizePlayerCards(player.getPlayerCards());
        player.playCard(board, 0);
        //printing the board
        board.printBoard();
        System.out.println(board.getCardsOnTheBoardList());
        boardPoints.countPoints(board);
        TrisObjectiveCard trisObjectiveCard = new TrisObjectiveCard();
        trisObjectiveCard.checkPattern(board, SpecificSeed.MUSHROOM, player); //Funziona!
    }
}
