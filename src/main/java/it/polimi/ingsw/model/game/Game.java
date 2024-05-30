package it.polimi.ingsw.model.game;

import com.google.gson.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.InitialCardDeck;
import it.polimi.ingsw.model.deck.ObjectiveDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Game{
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
    private Card selectedCardFromTheDeck = null;
    private Card cPchoose = null;
    private final Semaphore semaphore = new Semaphore(1);
    private int totalNumberOfPLayer=0;
    private boolean endGame = false;
    private String winnerString=null;
    public Game() {                                           //GAME CONSTRUCTOR WHICH INITIALIZED ALL THE CARDS
        this.players = new ArrayList<>();
        this.well = new ArrayList<>();
        this.dots = new ArrayList<>();

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
        initializeWell();
        commonObjectiveCards();

    }

    public synchronized void addPlayer(Player player) {
        boolean exists = false;
        for (Player p : players) {
            if (p.getNickName().equals(player.getNickName())) {
                exists = true;
                break;
            }
        }
        if (!exists) players.add(player);
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
            System.out.println(player.getPlayerCards().getFirst());
            player.drawResourceCard(resourceDeck);
            System.out.println(player.getPlayerCards().get(1));
            player.drawGoldCard(goldDeck);
            System.out.println(player.getPlayerCards().getLast());
        }
    }

    public synchronized String getFirstCardOfResourceDeck() {
        return resourceDeck.sendIdCardToGui();
    }
    public synchronized void resourceDeckUpdateForGUi(){
        resourceDeck.drawCardForGui();
    }
    public synchronized void goldDeckUpdateForGUI(){
        goldDeck.drawCardForGoldGui();
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
            return "Carta piazzata correttamente";
        }
    }

    public void turnCard(Player player, int cardIndex){
        Card chosenCard =player.getPlayerCards().get(cardIndex);
        player.turnYourCard(chosenCard);
    }

    public String endGame() {
        StringBuilder finalText = new StringBuilder();
        finalText.append("The Game is Over\n");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\nUpdating each player points to see who is the real winner!");
        System.out.println(players);
        for (Player player : players) {
            System.out.println("Calcolando punti per " + player);
            System.out.println(player.getNickName());
            System.out.println(player.getPlayerScore());
            ObjectiveCard secretCard = player.getSecretChosenCard();
            System.out.println("secret");
            player.getBoard().createSpecificSecretCard(secretCard, player); //secret player card
            player.getBoard().createSpecificSecretCard(firstObjectiveCommonCard, player);
            System.out.println("common1");
            player.getBoard().createSpecificSecretCard(secondObjectiveCommonCard, player);
            System.out.println("common2");
            System.out.println(player.getPlayerScore());
        }
        Player winner = calculateWinner(players);
        System.out.println("And the winner is...........");
        finalText.append("And the winner is...........\n");
        System.out.println("Suspance...");
        finalText.append("Suspance...\n");
        System.out.println(winner.getNickName());
        finalText.append(winner.getNickName());
        finalText.append("\nPoints:\n");
        for (Player player : players) {
            finalText.append(player.getNickName() + "-> " + player.getPlayerScore()+"\n");
        }
        finalText.append("exit");
        System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println(finalText);
        return String.valueOf(finalText);
    }

    public Player calculateWinner(List<Player> players) {               //Calculating who has the highest score
        Player winner = currentPlayingPLayer;
        System.out.println(winner);
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
        return yourSpecificSeeds;
    }


    public String showCards(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //returns String
    }


    /**
     *Method to visualize the common objective cards in the Game.
     */

    public String visualizeCommonObjective() {
        StringBuilder cardsAsString = new StringBuilder();
        cardsAsString.append(firstObjectiveCommonCard.toString()).append("\n");
        cardsAsString.append(secondObjectiveCommonCard.toString());
        return String.valueOf(cardsAsString);
    }

    /**
     *Method to visualize the first common objective card in the Game.
     */
    public synchronized String firstCommonObjectiveCardId() {
        int id = firstObjectiveCommonCard.getId();
        return String.valueOf(id);
    }

    /**
     *Method to visualize the second common objective card in the Game.
     */

    public synchronized String secondCommonObjectiveCardId() {
        int id = secondObjectiveCommonCard.getId();
        return String.valueOf(id);
    }

    /**
     *Method to visualize the player's secret objective card.
     */

    public String visualizeSecretObjective(Player player) {
        return String.valueOf(player.getSecretChosenCard());
    }

    /**
     *Method to visualize the player's board.
     */

    public String areYouTheFirstWinner(){
        if(winnerString==null)
        {
            winnerString="set";
            return "OK";
        }
        else return "NO";
    }
    public String showBoard(Player player) {
        return player.getBoard().printBoardForServer();
    }

    /**
     *Method to visualize the players' boards.
     */

    public String showAllPlayersBoard() {
        StringBuilder stamp = new StringBuilder();
        for (Player playersInTheGame : players) {
            stamp.append("////////////////////////////////// INIZIO BOARD: ");
            stamp.append(playersInTheGame.getNickName());
            stamp.append(" //////////////////////////////////////////");
            stamp.append("\n");
            String point = showPoints(playersInTheGame);
            stamp.append("Current points: ");
            stamp.append(point);
            stamp.append("\n");
            String bord = playersInTheGame.getBoard().printBoardForServer();
            stamp.append(bord.replace("fine board", "\n"));
            stamp.append("////////////////////////////////// FINE BOARD ////////////////////////////////////////////");
            stamp.append("\n");
        }
        stamp.append("exit");
        return String.valueOf(stamp);
    }

    /**
     * Method to send the first id of the well to the gui
     */

    public synchronized String sendWellIdFirstToGui() {
        try {
            semaphore.acquire();
            int id1 = well.getFirst().getId();
            return String.valueOf(id1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // Manage the exception in a proper way
        } finally {
            semaphore.release();
        }
    }

    /**
     * Method to send the second id of the well to the gui
     */

    public synchronized String sendWellIdSecondToGui() {
        try {
            semaphore.acquire();
            int id2 = well.get(1).getId();
            return String.valueOf(id2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // Manage the exception in a proper way
        } finally {
            semaphore.release();
        }
    }

    /**
     * Method to send the third id of the well to the gui
     */

    public synchronized String sendWellIdThirdToGui() {
        try {
            semaphore.acquire();
            int id3 = well.get(2).getId();
            return String.valueOf(id3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // Manage the exception in a proper way
        } finally {
            semaphore.release();
        }
    }

    /**
     * Method to send the fourth id of the well to the gui
     */

    public synchronized String sendWellIdFourthToGui() {
        try {
            semaphore.acquire();
            int id4 = well.get(3).getId();
            return String.valueOf(id4);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // Manage the exception in a proper way
        } finally {
            semaphore.release();
        }
    }


    public String showAllPoints() {
        StringBuilder stamp = new StringBuilder();
        for (Player player : players) {
            stamp.append(player.getNickName());
            stamp.append(" has: ");
            stamp.append(showPoints(player));
            stamp.append(" points\n");
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
        try {
            semaphore.acquire();
            List<Card> cardToSendToServer = player.getPlayerCards();
            System.out.println(player.getPlayerCards().getFirst());
            System.out.println(player.getPlayerCards().get(1));
            System.out.println(player.getPlayerCards().getLast());
            StringBuilder cardsAsString = new StringBuilder();
            for (Card card : cardToSendToServer) {
                cardsAsString.append(card.getId()).append("\n");
            }
            return String.valueOf(cardsAsString);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null; // Manage the exception in a proper way
        } finally {
            semaphore.release();
        }
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayingPLayer = players.get(currentPlayerIndex);
        currentPlayer=currentPlayingPLayer.getNickName();
        System.out.println("Now it's time for " + currentPlayingPLayer.getNickName() + " to play!");
    }


    public String showAvailableCorners(Player player, int cardIndex, int cardChosenOnTheBoard) {
        System.out.println("The initial card is: " + player.getBoard().getCardsOnTheBoardList().getFirst());
        InitialCard initialCard = (InitialCard) player.getBoard().getCardsOnTheBoardList().getFirst();
        selectedCardFromTheDeck = player.checkingTheChosencard(cardIndex);
        cPchoose = player.gettingCardsFromTheBoard(player.getBoard(), cardChosenOnTheBoard);
        String result = player.isTheCardChosenTheInitialCard(cPchoose, initialCard);
        System.out.println(result);
        return result;
    }


    public String showPoints(Player player) {
        return String.valueOf(player.getPlayerScore());
    }


    public void runEndTurn(Player player) {
        //saveCards();
        savePlayers();
        saveGameStatusToJson();
        saveCurrentPlayingPlayerToJson();
        System.out.println("Player correctly saved");
        System.out.println("Shared information correctly saved!");
        System.out.println("Current player correctly saved in json");
        System.out.println(well.getFirst());
        System.out.println(well.get(1));
        System.out.println(well.get(2));
        System.out.println(well.getLast());
        System.out.println(player.getBoard().printBoardForServer());
        System.out.println(player.getPlayerCards().getFirst());
        System.out.println(player.getPlayerCards().get(1));
        System.out.println(player.getPlayerCards().getLast());
    }

    public String showWell() {
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : well) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //Returns String
    }


    public String drawResourceCard(Player player) {
        return player.drawResourceCard(resourceDeck);
    }


    public String drawGoldCard(Player player) {
        return player.drawGoldCard(goldDeck);
    }

    public String drawCardFromWell(Player player, int index) {
        String result=player.chooseCardFromWellForServer(well, index, resourceDeck, goldDeck);
        sortWell();
        System.out.println(well);
        return result;
    }

    public int getPlayerScore(Player player) {
        return player.getPlayerScore();
    }

    public void updateSingleClientView(Player player) {
        player.getClientView().update(player);

    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public InitialCardDeck getInitialCardDeck() {
        return initialCardDeck;
    }


    public ObjectiveDeck getObjectiveDeck() {
        return objectiveDeck;
    }


    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int cardsInDeck() {
        return resourceDeck.leftCardINDeck();
    }

    public int goldsInDeck() {
        return goldDeck.cardLefInDeck();
    }

    public synchronized List<String> getDots() {
        return dots;
    }

    public synchronized void removeDot(String string) {
        dots.remove(string);
    }

    public synchronized boolean isInDots(String stringa) {
        return dots.contains(stringa);
    }

    //PRIVATE METHODS INSIDE GAME

    private void initializeWell() {
        resourceDeck.drawCard(well);
        resourceDeck.drawCard(well);
        goldDeck.drawCard(well);
        goldDeck.drawCard(well);
        sortWell();
        System.out.println("Cards in the well: ");
        for (Card card : well) {
            System.out.println(card);
        }
        System.out.println("\n");
    }
    private void sortWell() {
        Collections.sort(well, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return Integer.compare(c1.getId(), c2.getId());
            }
        });
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

void saveEachPlayerInGame(Path path) {
    JsonObject gameState = new JsonObject();
    JsonArray playersArray = new JsonArray();

    for (Player player : players) {
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("nickname", player.getNickName());
        playerObject.addProperty("score", player.getPlayerScore());
        playerObject.addProperty("dot", player.getDot().ordinal());

        JsonArray playerDeckArray = new JsonArray();
        for (Card card : player.getPlayerCards()) {
            JsonObject cardObject = card.toJsonObject();
            if (card instanceof InitialCard) {
                cardObject.addProperty("cardType", "InitialCard");}
            playerDeckArray.add(cardObject);
        }
        playerObject.add("player_deck", playerDeckArray);

        JsonObject boardObject = player.getBoard().toJsonObject();
        playerObject.add("board", boardObject);

        if (player.getSecretChosenCard() != null) {
            JsonObject secretChosenCardObject = player.getSecretChosenCard().toJsonObject();
            playerObject.add("secretChosenCard", secretChosenCardObject);
        }
        if (player.getClientView() != null) {
            JsonObject clientViewObject = player.getClientView().toJsonObject();
            playerObject.add("clientView", clientViewObject);
        }

        playersArray.add(playerObject);
    }
    gameState.add("players", playersArray);

    // Saves current player
    gameState.addProperty("currentPlayer", currentPlayer);

    // Saves resources deck
    JsonArray resourceDeckArray = new JsonArray();
    for (Card card : resourceDeck.getRemainingCards()) {
        resourceDeckArray.add(card.toJsonObject());
    }
    gameState.add("resourceDeck", resourceDeckArray);

    // Saves gold deck
    JsonArray goldDeckArray = new JsonArray();
    for (Card card : goldDeck.getRemainingCards()) {
        goldDeckArray.add(card.toJsonObject());
    }
    gameState.add("goldDeck", goldDeckArray);

    try (PrintWriter printWriter = new PrintWriter(new FileWriter(path.toString()))) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(gameState);
        printWriter.println(jsonOutput);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    void savePlayers() {
        saveEachPlayerInGame(getDefaultPlayers());
    }


    private Path getDefaultPlayers() {
        return Paths.get("src/main/resources/saveplayers.json");
    }

    public int getTotalNumberOfPLayer() {
        return totalNumberOfPLayer;
    }

    public void setTotalNumberOfPLayer(int totalNumberOfPLayer) {
        this.totalNumberOfPLayer = totalNumberOfPLayer;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }




    public String checkingIfICanPlaceTheGoldCardOnGui(Player player, int indexOfTheGoldCard)
    {
        System.out.println(indexOfTheGoldCard);
        boolean check= player.checkTheGoldCardForGui(indexOfTheGoldCard);
        System.out.println("Check is " + check);
           if(check)
           {
               System.out.println("Check is: "+ check);
               return "OKAY";
           }
           else return "NO";
    }



    private Path getDefaultGameStatusPath() {
        String home = "src/main/resources/gamestatus.json";
        return Paths.get(home);
    }

    public void saveGameStatusToJson() {
        Path path = getDefaultGameStatusPath();
        JsonObject gameStatus = new JsonObject();
        gameStatus.addProperty("currentPlayer", currentPlayer);
        if (firstObjectiveCommonCard != null) {
            gameStatus.add("firstObjectiveCommonCard", firstObjectiveCommonCard.toJsonObject());
        }
        if (secondObjectiveCommonCard != null) {
            gameStatus.add("secondObjectiveCommonCard", secondObjectiveCommonCard.toJsonObject());
        }
        gameStatus.add("resourceDeck", resourceDeck.toJson());
        gameStatus.add("goldDeck", goldDeck.toJson());
        JsonArray wellArray = new JsonArray();
        for (Card card : well) {
            wellArray.add(card.toJsonObject());
        }
        gameStatus.add("well", wellArray);
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(gameStatus);
            fileWriter.write(jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadGameStatusFromJson() {
        Path path = getDefaultGameStatusPath();

        try (FileReader reader = new FileReader(path.toString())) {
            JsonObject gameStatus = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray resourceDeckArray = gameStatus.getAsJsonArray("resourceDeck");
            resourceDeck = ResourceDeck.fromJson(resourceDeckArray);

            JsonArray goldDeckArray = gameStatus.getAsJsonArray("goldDeck");
            goldDeck = GoldDeck.fromJson(goldDeckArray);

            JsonArray wellArray = gameStatus.getAsJsonArray("well");
            well = new ArrayList<>();
            for (JsonElement element : wellArray) {
                JsonObject cardObject = element.getAsJsonObject();
                Card card = Card.fromJson(cardObject);
                well.add(card);
            }
            System.out.println(well);
            if (gameStatus.has("firstObjectiveCommonCard")) {
                JsonObject firstObjectiveCommonCardObject = gameStatus.getAsJsonObject("firstObjectiveCommonCard");
                firstObjectiveCommonCard = ObjectiveCard.fromJsonObject(firstObjectiveCommonCardObject);
            }
            if (gameStatus.has("secondObjectiveCommonCard")) {
                JsonObject secondObjectiveCommonCardObject = gameStatus.getAsJsonObject("secondObjectiveCommonCard");
                secondObjectiveCommonCard = ObjectiveCard.fromJsonObject(secondObjectiveCommonCardObject);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getCurrentPlayerPath() {
        String home = "src/main/resources/currentplayer.json";
        return Paths.get(home);
    }

    public void saveCurrentPlayingPlayerToJson() {
        Path path = getCurrentPlayerPath();
        JsonObject currentPlayerObject = new JsonObject();

        // Controlla se currentPlayingPLayer non è nullo
        if (currentPlayingPLayer == null) {
            System.out.println("currentPlayingPLayer is null");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject playerJsonObject = new JsonObject();

        playerJsonObject.addProperty("nickname", currentPlayingPLayer.getNickName());
        playerJsonObject.addProperty("score", currentPlayingPLayer.getPlayerScore());
        playerJsonObject.addProperty("dot", currentPlayingPLayer.getDot().ordinal());

        JsonArray playerDeckArray = new JsonArray();
        for (Card card : currentPlayingPLayer.getPlayerCards()) {
            JsonObject cardObject = card.toJsonObject();
            playerDeckArray.add(cardObject);
        }
        playerJsonObject.add("player_deck", playerDeckArray);

        JsonObject boardObject = currentPlayingPLayer.getBoard().toJsonObject();
        playerJsonObject.add("board", boardObject);

        if (currentPlayingPLayer.getSecretChosenCard() != null) {
            JsonObject secretChosenCardObject = currentPlayingPLayer.getSecretChosenCard().toJsonObject();
            playerJsonObject.add("secretChosenCard", secretChosenCardObject);
        }

        currentPlayerObject.add("currentPlayer", playerJsonObject);

        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            String jsonOutput = gson.toJson(currentPlayerObject);
            fileWriter.write(jsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Player loadCurrentPlayingPlayerFromJson() {
        Path path = getCurrentPlayerPath();

        try (FileReader reader = new FileReader(path.toString())) {
            JsonObject currentPlayerObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject playerJsonObject = currentPlayerObject.getAsJsonObject("currentPlayer");

            String nickname = playerJsonObject.get("nickname").getAsString();
            int score = playerJsonObject.get("score").getAsInt();
            Dot dot = Dot.values()[playerJsonObject.get("dot").getAsInt()];

            JsonArray playerDeckArray = playerJsonObject.getAsJsonArray("player_deck");
            List<Card> playerDeck = new ArrayList<>();
            for (JsonElement cardElement : playerDeckArray) {
                JsonObject cardObject = cardElement.getAsJsonObject();
                String cardType = cardObject.get("cardType").getAsString();
                Card card = switch (cardType) {
                    case "GoldCard" -> GoldCard.fromJson(cardObject);
                    case "ResourceCard" -> ResourceCard.fromJsonObject(cardObject);
                    case "ObjectiveCard" -> ObjectiveCard.fromJsonObject(cardObject);
                    default -> Card.fromJson(cardObject);
                };

                if (card != null) {
                    playerDeck.add(card);
                } else {
                    System.err.println("Skipping invalid card in player deck: " + cardObject);
                }
            }

            Board board = Board.fromJson(playerJsonObject.get("board").getAsJsonObject());
            ObjectiveCard secretChosenCard = null;
            if (playerJsonObject.has("secretChosenCard")) {
                JsonObject secretCardObject = playerJsonObject.get("secretChosenCard").getAsJsonObject();
                secretChosenCard = ObjectiveCard.fromJsonObject(secretCardObject);
            }

            currentPlayingPLayer = new Player(nickname, score, dot, board);
            currentPlayingPLayer.setPlayerCards((ArrayList<Card>) playerDeck);
            currentPlayingPLayer.setSecretChosenCard(secretChosenCard);

            System.out.println("Current player loaded: " + currentPlayingPLayer); // Aggiungi un messaggio di debug
            return currentPlayingPLayer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentPlayingPLayer;
    }

}

