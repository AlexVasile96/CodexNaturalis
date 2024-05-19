package model.game;

import com.google.gson.*;
import model.card.*;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ObjectiveDeck;
import model.deck.ResourceDeck;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Game implements WhatCanPlayerDo {
    private List<Player> players;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private InitialCardDeck initialCardDeck;
    private ObjectiveDeck objectiveDeck;
    private int currentPlayerIndex;
    private String currentPlayer;
    private Player currentPlayingPLayer;
    private List<Card> well;
    private ObjectiveCard firstObjectiveCommonCard;
    private ObjectiveCard secondObjectiveCommonCard;
    private List<String> dots;
    private static boolean isCornerAlreadyChosen = false;
    private Card selectedCardFromTheDeck = null;
    private Card cPchoose = null;
    private List<Player> playersFromDisk;



    public Game() {                                           //GAME CONSTRUCTOR WHICH INITIALIZED ALL THE CARDS
        this.players = new ArrayList<>();
        this.well = new ArrayList<>();
        this.dots = new ArrayList<>();
        //loadPlayers();
        ResourceCardConstructor constructor = new ResourceCardConstructor();
        resourceDeck = (ResourceDeck) constructor.createCards();
        resourceDeck.shuffle();

        GoldCardConstructor constructorGold = new GoldCardConstructor();
        goldDeck = (GoldDeck) constructorGold.createCards();
        goldDeck.shuffle();

        InitCardConstructor constructorInitial = new InitCardConstructor();
        initialCardDeck = (InitialCardDeck) constructorInitial.createCards();
        initialCardDeck.shuffle();

        ObjectiveCardConstructor constructorObjective = new ObjectiveCardConstructor();
        objectiveDeck = (ObjectiveDeck) constructorObjective.createCards();
        objectiveDeck.shuffle();

        initializeDots();
        initializewell();
        commonObjectiveCards();

    }

    public void addPlayer(Player player) {
        boolean esiste = false;
        for (Player p : players) {
            if (p.getNickName().equals(player.getNickName())) {
                esiste = true;
                break;
            }
        }
        if (!esiste) players.add(player);
    }

    public Player getCurrentPlayingPLayer() {
        return currentPlayingPLayer;
    }

    public void setCurrentPlayingPLayer(Player currentPlayingPLayer) {
        this.currentPlayingPLayer = currentPlayingPLayer;
    }

    public synchronized void assignResourcesAndGoldCardsToPlayers() {
        for (Player player : players) {
            player.drawResourceCard(resourceDeck);
            player.drawResourceCard(resourceDeck);
            player.drawGoldCard(goldDeck);
        }
    }

    public synchronized String getFirstCardOfResourceDeck() {
        return resourceDeck.sendIdCardToGui();
    }
    public synchronized void resourceDeckUpdateForGUi(){
        resourceDeck.drawCardForGui();
    }

    public synchronized String getFirstCardOfGoldDeck() {
        return goldDeck.sendIdCardToGui();
    }

    public void placeInitialCard(Board board, InitialCard card) {
        board.placeFrontInitialCard(card);
    }

    public void placeInitialCardBack(Board board, InitialCard card) {
        board.placeBackInitialCard(card);
        card.setCardBack(true);
    }


    public String playCard(Player player, int cardindex, int cardChosenOnTheBoard, String selectedCorner) {
        if (cPchoose.getId() >= 81 && cPchoose.getId() <= 86) {
            //DA FIXARE
            //Card is the initial Card
            InitialCard helpcard = new InitialCard(cPchoose.getId(), cPchoose.getType(), cPchoose.getValueWhenPlaced(), cPchoose.getTL(), cPchoose.getTR(), cPchoose.getBL(), cPchoose.getBR(), cPchoose.getTLBack(), cPchoose.getTRBack(), cPchoose.getBLBack(), cPchoose.getBRBack(), cPchoose.getAttributes());
            helpcard.setIndexOnTheBoard(cPchoose.getIndexOnTheBoard());
            helpcard.setNode(cPchoose.getNode());
            helpcard.setCardBack(cPchoose.isCardBack());
            player.playInitCardOnBoard(player.getBoard(), cardindex, selectedCardFromTheDeck, helpcard, selectedCorner);
            player.getClientView().update(player);
            return "Carta piazzata correttamente";
        } else {
            //Card chosen is not the initial card
            player.playCard(player.getBoard(), cardindex, cardChosenOnTheBoard, selectedCardFromTheDeck, cPchoose, selectedCorner);
            player.getClientView().update(player);
            return "Carta piazzata correttamente";
        }
    }

    public void turnCard(Player player, int cardIndex){
        Card cartaScelta =player.getPlayerCards().get(cardIndex);
        player.turnYourCard(cartaScelta);
    }

    public void endGame() {
        System.out.println("Updating each player points to see who is the real winner!");
        for (Player player : players) {
            System.out.println(player.getNickName());
            System.out.println(player.getPlayerScore());
            ObjectiveCard secretCard = player.getSecretChosenCard();
            player.getBoard().createSpecificSecretCard(secretCard, player);
            player.getBoard().createSpecificSecretCard(firstObjectiveCommonCard, player);
            player.getBoard().createSpecificSecretCard(secondObjectiveCommonCard, player);
            System.out.println(player.getPlayerScore());
        }
        Player winner = calculateWinner(players);
        System.out.println("And the winner is...........");
        System.out.println("Suspance...");
        System.out.println(winner.getNickName());
    }

    public Player calculateWinner(List<Player> players) {               //Calculating who has the highest score
        Player winner = null;
        int highestScore = currentPlayingPLayer.getPlayerScore();
        for (Player player : players) {
            int playerScore = player.getPlayerScore();
            if (playerScore > highestScore) {
                highestScore = playerScore;
                winner = player;
            }
        }
        return winner;
    }


    public String showYourspecificSeeds(Player player) {
        BoardPoints boardPoints = new BoardPoints();
        String yourSpecificSeeds = boardPoints.countPoints(player.getBoard()).toString();
        System.out.println(yourSpecificSeeds);
        return yourSpecificSeeds;
    }

    @Override
    public String showCards(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    @Override
    public void chosenHandCard() {

    }

    @Override
    public String visualizeCommonObjective(Player player) {
        StringBuilder cardsAsString = new StringBuilder();
        cardsAsString.append(firstObjectiveCommonCard.toString()).append("\n");
        cardsAsString.append(secondObjectiveCommonCard.toString());
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    public synchronized String firstCommonObjectiveCardId() {
        int id = firstObjectiveCommonCard.getId();
        System.out.println(id);
        return String.valueOf(id);
    }

    public synchronized String secondCommonObjectiveCardId() {
        int id = secondObjectiveCommonCard.getId();
        return String.valueOf(id);
    }


    @Override
    public String visualizeSecretObjective(Player player) {
        return String.valueOf(player.getSecretChosenCard());
    }

    @Override
    public String showBoard(Player player) {
        return player.getBoard().printBoardForServer();
    }

    public String showAllPlayersBoard() {
        StringBuilder stamp = new StringBuilder();
        for (Player playerz : players) {
            stamp.append("////////////////////////////////// INIZIO BOARD: ");
            stamp.append(playerz.getNickName());
            stamp.append(" //////////////////////////////////////////");
            stamp.append("\n");
            String point = showPoints(playerz);
            stamp.append("Current points: ");
            stamp.append(point);
            stamp.append("\n");
            String bord = playerz.getBoard().printBoardForServer();
            stamp.append(bord.replace("fine board", "\n"));
            stamp.append("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
            stamp.append("\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    public synchronized String sendWellIdFirstToGui() {
        int id1 = well.getFirst().getId();
        return String.valueOf(id1);
    }

    public synchronized String sendWellIdSecondToGui() {

        int id2 = well.get(1).getId();
        return String.valueOf(id2);
    }

    public synchronized String sendWellIdThirdToGui() {

        int id3 = well.get(2).getId();
        return String.valueOf(id3);

    }

    public synchronized String sendWellIdFourthToGui() {

        int id4 = well.get(3).getId();
        ;
        return String.valueOf(id4);
    }

    @Override
    public String showAllPoints() {
        StringBuilder stamp = new StringBuilder();
        for (Player playerz : players) {
            stamp.append(playerz.getNickName());
            stamp.append(" current Point: ");
            stamp.append(showPoints(playerz));
            stamp.append("\n--------------------------------------------------\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    public String showAllSpecificSeed() {
        StringBuilder stamp = new StringBuilder();
        for (Player playerz : players) {
            stamp.append(playerz.getNickName());
            stamp.append(" current Seed:\n");
            stamp.append(showYourspecificSeeds(playerz));
            stamp.append("\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    public synchronized String getDeckID(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.getId()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa


    }

    public String showBoardForPlacingCards(Player player) {
        return player.getBoard().printBoardForServer();
    }

    public String showAvaiableCorners(Player player, int cardindex, int cardChosenOnTheBoard) {
        InitialCard initialCard = (InitialCard) player.getBoard().getCardsOnTheBoardList().getFirst();
        selectedCardFromTheDeck = player.checkingTheChosencard(cardindex);
        cPchoose = player.gettingCardsFromTheBoard(player.getBoard(), cardChosenOnTheBoard);
        String result = player.isTheCardChosenTheInitialcard(cPchoose, initialCard);
        System.out.println(result);
        isCornerAlreadyChosen = true;
        return result;
    }

    @Override
    public String showPoints(Player player) {
        return String.valueOf(player.getPlayerScore());
    }

    @Override
    public void runEndTurn(Player player) {
        //saveCards();
        savePlayers();
        System.out.println("Carte salvate correttamente");
    }

    public String showWell() {
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : well) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //ritorna stringa
    }

    @Override
    public String drawResourceCard(Player player) {
        return player.drawResourceCard(resourceDeck);
    }

    @Override
    public String drawGoldCard(Player player) {
        return player.drawGoldCard(goldDeck);
    }

    public String drawCardFromWell(Player player, int index) {
        return player.chooseCardFromWellForServer(well, index, resourceDeck, goldDeck);
    }

    @Override
    public void chooseSecretCard(Player player, List<ObjectiveCard> secretCards) {
    }

    @Override
    public void turnYourCard(Card card) {

    }


    @Override
    public int getPlayerScore(Player player) {
        return player.getPlayerScore();
    }

    @Override
    public String getNickName(Player player) {
        return player.getNickName();
    }

    @Override
    public List<Card> getPlayerCards(Player player) {
        return player.getPlayerCards();
    }

    public void updateSingleClientView(Player player) {
        player.getClientView().update(player);

    }

    @Override
    public void requestGameInfo(Player player) {

    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    public void setResourceDeck(ResourceDeck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    public void setGoldDeck(GoldDeck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public InitialCardDeck getInitialCardDeck() {
        return initialCardDeck;
    }

    public void setInitialCardDeck(InitialCardDeck initialCardDeck) {
        this.initialCardDeck = initialCardDeck;
    }

    public ObjectiveDeck getObjectiveDeck() {
        return objectiveDeck;
    }

    public void setObjectiveDeck(ObjectiveDeck objectiveDeck) {
        this.objectiveDeck = objectiveDeck;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int CardsIndeck() {
        return resourceDeck.carteRimaste();
    }

    public int GoldsIndeck() {
        return goldDeck.carteRimaste();
    }

    public synchronized List<String> getDots() {
        return dots;
    }

    public synchronized void removeDot(String stringa) {
        dots.remove(stringa);
    }

    public synchronized boolean isInDots(String stringa) {
        return dots.contains(stringa);
    }

    public List<Card> getWell() {
        return well;
    }

    //PRIVATE METHODS INSIDE GAME

    private void initializewell() {
        resourceDeck.drawCard(well);
        resourceDeck.drawCard(well);
        goldDeck.drawCard(well);
        goldDeck.drawCard(well);
        System.out.println("Cards in the well: ");
        for (Card card : well) {
            System.out.println(card);
        }
        System.out.println("\n");
    }


    private synchronized void commonObjectiveCards() {
        this.firstObjectiveCommonCard = objectiveDeck.firstCardForEachPlayer(); //common objective cards
        this.secondObjectiveCommonCard = objectiveDeck.firstCardForEachPlayer();
        System.out.println("First common objective card is " + firstObjectiveCommonCard);
        System.out.println("Second common objective card is " + secondObjectiveCommonCard);
    }

    private void initializeDots() {
        dots.add("RED");
        dots.add("BLUE");
        dots.add("GREEN");
        dots.add("YELLOW");
    }

    private Path getDefaultCardPath() {
        String home = ("src/main/resources/savecard.json");
        return Paths.get(home);
    }

    void saveCards() {
        savePath(getDefaultCardPath());
    }

    void savePath(Path path) {                                                   //METHOD TO SAVE CARDS
        JsonArray jo = new JsonArray();
        for (Card card : currentPlayingPLayer.getPlayerCards()) {
            jo.add(card.toJsonObject());
        }
        String jsonText = jo.toString();
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            fileWriter.write(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveEachPlayerInGame(Path path) {
        JsonObject playersObject = new JsonObject();
        JsonArray playersArray = new JsonArray();

        for (Player player : players) {
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("nickname", player.getNickName());
            playerObject.addProperty("score", player.getPlayerScore());
            playerObject.addProperty("dot", player.getDot().ordinal());

            JsonArray playerDeckArray = new JsonArray();
            for (Card card : player.getPlayerCards()) {
                JsonObject cardObject = new JsonObject();
                cardObject.addProperty("id", card.getId());
                cardObject.addProperty("type", card.getType().toString());
                cardObject.addProperty("value", card.getValueWhenPlaced());
                cardObject.add("TL", card.getTL().toJsonObject());
                cardObject.add("TR", card.getTR().toJsonObject());
                cardObject.add("BL", card.getBL().toJsonObject());
                cardObject.add("BR", card.getBR().toJsonObject());


                // Check if the card has requisites
                /*if (card.getRequisites() != null) {
                    JsonArray requisitesArray = new JsonArray();
                    for (String requisite : card.get()) {
                        requisitesArray.add(requisite);
                    }
                    cardObject.add("requisites", requisitesArray);
                }*/

                playerDeckArray.add(cardObject);
            }
            playerObject.add("player_deck", playerDeckArray);
            Board playerBoard = player.getBoard();
            JsonObject boardObject = playerBoard.toJsonObject();
            playerObject.add("board", boardObject);
            if (player.getSecretChosenCard() != null) {
                JsonObject secretChosenCardObject = new JsonObject();
                secretChosenCardObject.addProperty("id", player.getSecretChosenCard().getId());
                secretChosenCardObject.addProperty("type", player.getSecretChosenCard().getType().toString());
                secretChosenCardObject.addProperty("value", player.getSecretChosenCard().getValue());
                // Aggiungi altri attributi se necessario
                playerObject.add("secretChosenCard", secretChosenCardObject);
            }
            playersArray.add(playerObject);
        }
        playersObject.add("players", playersArray);
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(path.toString()))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(playersObject);
            printWriter.println(jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void savePlayers() {
        saveEachPlayerInGame(getDefaultPlayers());
    }


    private Path getDefaultPlayers() {
        String home = ("src/main/resources/saveplayers.json");
        return Paths.get(home);
    }

    public List<Player> loadPlayersFromGame(Path path) {
        try {
            Gson gson = new Gson();
            JsonObject playersObject;
            try (Reader reader = new FileReader(path.toString())) {
                playersObject = gson.fromJson(reader, JsonObject.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JsonArray playersArray = playersObject.getAsJsonArray("players");
            Player[] players = new Player[playersArray.size()];
            //METODO DA FINIRE

            return null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


    }
    public String serializeGameState() {
        JsonObject gameState = new JsonObject();
        gameState.add("well", serializeWell()); //Well serializing
        //gameState.add("resourceDeck", serializeResourceDeck(resourceDeck));
       // gameState.add("goldDeck", serializeGoldDeck(goldDeck));
        return new Gson().toJson(gameState);
    }

    private JsonArray serializeWell() {
        JsonArray wellArray = new JsonArray();
        for (Card card : well) {
            JsonObject cardObject = new JsonObject();
            cardObject.addProperty("id", card.getId());
            // Aggiungi altri attributi della carta, se necessario
            wellArray.add(cardObject);
        }
        return wellArray;
    }

   /* private JsonArray serializeResourceDeck(ResourceDeck deck) {
        JsonArray deckArray = new JsonArray();
        for (Card card : deck.getCards()) {
            JsonObject cardObject = new JsonObject();
            cardObject.addProperty("id", card.getId());
            cardObject.addProperty("type", card.getType());
            // Aggiungi altri attributi della carta, se necessario
            deckArray.add(cardObject);
        }
        return deckArray;

    }*/
   /* private JsonArray serializeGoldDeck(GoldDeck deck) {
        JsonArray deckArray = new JsonArray();
        for (Card card : deck.getCards()) {
            JsonObject cardObject = new JsonObject();
            cardObject.addProperty("id", card.getId());
            // Aggiungi altri attributi della carta, se necessario
            deckArray.add(cardObject);
        }
        return deckArray;
    }*/


}

