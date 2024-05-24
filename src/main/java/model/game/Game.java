package model.game;

import com.google.gson.*;
import model.card.*;
import model.deck.GoldDeck;
import model.deck.InitialCardDeck;
import model.deck.ObjectiveDeck;
import model.deck.ResourceDeck;

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
    private List<Player> playersFromDisk;
    private final Semaphore semaphore = new Semaphore(1);
    private int totalNumberOfPLayer=0;
    private boolean endGame = false;

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
            player.getClientView().update(player);
            return "Carta piazzata correttamente";
        }
    }

    public void turnCard(Player player, int cardIndex){
        Card cartaScelta =player.getPlayerCards().get(cardIndex);
        player.turnYourCard(cartaScelta);
    }

    public String endGame() {
        StringBuilder finalText = new StringBuilder();
        finalText.append("The Game is Over\n");
        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\nUpdating each player points to see who is the real winner!");
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
        finalText.append("And the winner is...........\n");
        System.out.println("Suspance...");
        finalText.append("Suspance...\n");
        System.out.println(winner.getNickName());
        finalText.append(">>>>>>>>>>>>>>>>>>>>>>>>"+winner.getNickName()+"<<<<<<<<<<<<<<<<<<<<<<<<\n\n");
        finalText.append("Points:\n");
        for (Player player : players) {
            finalText.append(player.getPlayerScore() + "-> " + player.getPlayerScore()+"\n");
        }
        finalText.append("exit");
        System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        return String.valueOf(finalText);
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


    public String showCards(Player player) {
        List<Card> cardToSendToServer = player.getPlayerCards();
        StringBuilder cardsAsString = new StringBuilder();
        for (Card card : cardToSendToServer) {
            cardsAsString.append(card.toString()).append("\n");
        }
        return String.valueOf(cardsAsString); //returns String
    }



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



    public String visualizeSecretObjective(Player player) {
        return String.valueOf(player.getSecretChosenCard());
    }


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
        try {
            semaphore.acquire();
            List<Card> cardToSendToServer = player.getPlayerCards();
            System.out.println("STAMPANDO LE CARTE");
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
        System.out.println("Turno del giocatore: " + currentPlayingPLayer.getNickName());
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
        return result;
    }


    public String showPoints(Player player) {
        return String.valueOf(player.getPlayerScore());
    }


    public void runEndTurn(Player player) {
        //saveCards();
        savePlayers();
        saveGameStatusToJson();
        System.out.println("Player correctly saved");
        System.out.println("Shared information correctly saved!");
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


    public void chooseSecretCard(Player player, List<ObjectiveCard> secretCards) {
    }


    public void turnYourCard(Card card) {

    }



    public int getPlayerScore(Player player) {
        return player.getPlayerScore();
    }


    public String getNickName(Player player) {
        return player.getNickName();
    }


    public List<Card> getPlayerCards(Player player) {
        return player.getPlayerCards();
    }

    public void updateSingleClientView(Player player) {
        player.getClientView().update(player);

    }


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
        return resourceDeck.leftCardINDeck();
    }

    public int GoldsIndeck() {
        return goldDeck.cardLefInDeck();
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

//    void saveEachPlayerInGame(Path path) {
//        JsonObject playersObject = new JsonObject();
//        JsonArray playersArray = new JsonArray();
//
//        for (Player player : players) {
//            JsonObject playerObject = new JsonObject();
//            playerObject.addProperty("nickname", player.getNickName());
//            playerObject.addProperty("score", player.getPlayerScore());
//            playerObject.addProperty("dot", player.getDot().ordinal());
//
//            JsonArray playerDeckArray = new JsonArray();
//            for (Card card : player.getPlayerCards()) {
//                JsonObject cardObject = new JsonObject();
//                cardObject.addProperty("id", card.getId());
//                cardObject.addProperty("type", card.getType().toString());
//                cardObject.addProperty("value", card.getValueWhenPlaced());
//                cardObject.add("TL", card.getTL().toJsonObject());
//                cardObject.add("TR", card.getTR().toJsonObject());
//                cardObject.add("BL", card.getBL().toJsonObject());
//                cardObject.add("BR", card.getBR().toJsonObject());
//
//                playerDeckArray.add(cardObject);
//            }
//            playerObject.add("player_deck", playerDeckArray);
//            Board playerBoard = player.getBoard();
//            JsonObject boardObject = playerBoard.toJsonObject();
//            playerObject.add("board", boardObject);
//            if (player.getSecretChosenCard() != null) {
//                JsonObject secretChosenCardObject = new JsonObject();
//                secretChosenCardObject.addProperty("id", player.getSecretChosenCard().getId());
//                secretChosenCardObject.addProperty("type", player.getSecretChosenCard().getType().toString());
//                secretChosenCardObject.addProperty("value", player.getSecretChosenCard().getValue());
//                playerObject.add("secretChosenCard", secretChosenCardObject);
//            }
//            playersArray.add(playerObject);
//        }
//        playersObject.add("players", playersArray);
//        try (PrintWriter printWriter = new PrintWriter(new FileWriter(path.toString()))) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String jsonOutput = gson.toJson(playersObject);
//            printWriter.println(jsonOutput);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
void saveEachPlayerInGame(Path path) {
    JsonObject gameState = new JsonObject();
    JsonArray playersArray = new JsonArray();

    // Salva i giocatori
    for (Player player : players) {
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("nickname", player.getNickName());
        playerObject.addProperty("score", player.getPlayerScore());
        playerObject.addProperty("dot", player.getDot().ordinal());

        JsonArray playerDeckArray = new JsonArray();
        for (Card card : player.getPlayerCards()) {
            JsonObject cardObject = card.toJsonObject();
            playerDeckArray.add(cardObject);
        }
        playerObject.add("player_deck", playerDeckArray);

        JsonObject boardObject = player.getBoard().toJsonObject();
        playerObject.add("board", boardObject);

        if (player.getSecretChosenCard() != null) {
            JsonObject secretChosenCardObject = player.getSecretChosenCard().toJsonObject();
            playerObject.add("secretChosenCard", secretChosenCardObject);
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

    public Player loadPlayer(String nickname) {
        Path path = getDefaultPlayers();
        try (Reader reader = new FileReader(path.toString())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray playersArray = jsonObject.getAsJsonArray("players");

            for (JsonElement element : playersArray) {
                JsonObject playerObject = element.getAsJsonObject();
                if (playerObject.get("nickname").getAsString().equals(nickname)) {
                    int score = playerObject.get("score").getAsInt();
                    Dot dot = Dot.values()[playerObject.get("dot").getAsInt()];

                    JsonArray playerDeckArray = playerObject.getAsJsonArray("player_deck");
                    List<Card> playerDeck = new ArrayList<>();
                    for (JsonElement cardElement : playerDeckArray) {
                        JsonObject cardObject = cardElement.getAsJsonObject();
                        String cardType = cardObject.get("cardType").getAsString();
                        Card card = null;

                        switch (cardType) {
                            case "GoldCard":
                                card = GoldCard.fromJson(cardObject);
                                break;
                            case "ResourceCard":
                                card = ResourceCard.fromJsonObject(cardObject);
                                break;
                            case "ObjectiveCard":
                                card = ObjectiveCard.fromJsonObject(cardObject);
                                break;
                            default:
                                card = Card.fromJson(cardObject);
                                break;
                        }

                        if (card != null) {
                            playerDeck.add(card);
                            System.out.println(card);
                        } else {
                            System.err.println("Skipping invalid card in player deck: " + cardObject);
                        }
                    }

                    Board board = Board.fromJson(playerObject.get("board").getAsJsonObject());
                    ObjectiveCard secretChosenCard = null;
                    if (playerObject.has("secretChosenCard")) {
                        JsonObject secretCardObject = playerObject.get("secretChosenCard").getAsJsonObject();
                        secretChosenCard = ObjectiveCard.fromJsonObject(secretCardObject);
                        System.out.println(secretChosenCard);
                    }
                    Player player = new Player(nickname, score, dot, board);
                    player.setPlayerCards((ArrayList<Card>) playerDeck);
                    player.setSecretChosenCard(secretChosenCard);
                    System.out.println(player);
                    return player;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    void savePlayers() {
        saveEachPlayerInGame(getDefaultPlayers());
    }


    private Path getDefaultPlayers() {
        String home = ("src/main/resources/saveplayers.json");
        return Paths.get(home);
    }
    public List<String> loadPlayerNicknames() {
        List<String> playerNicknames = new ArrayList<>();
        Path path = getDefaultPlayers();
        try (Reader reader = new FileReader(path.toString())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray playersArray = jsonObject.getAsJsonArray("players");

            for (JsonElement element : playersArray) {
                JsonObject playerObject = element.getAsJsonObject();
                String nickname = playerObject.get("nickname").getAsString();
                playerNicknames.add(nickname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerNicknames;
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
       boolean check= player.checkingTheChosenCardForGoldPurpose(indexOfTheGoldCard);


       if(check)
       {
           System.out.println("Check is: "+ check);
           return "OKAY";
       }

       else return "NO";
    }
    public boolean areAllPlayersLoaded(List<String> expectedPlayerNicknames) {
        for (String nickname : expectedPlayerNicknames) {
            boolean playerLoaded = false;
            for (Player player : players) {
                if (player.getNickName().equals(nickname)) {
                    playerLoaded = true;
                    break;
                }
            }
            if (!playerLoaded) {
                return false;
            }
        }
        return true;
    }


    private Path getDefaultGameStatusPath() {
        String home = "src/main/resources/gamestatus.json";
        return Paths.get(home);
    }

    public void saveGameStatusToJson() {
        Path path = getDefaultGameStatusPath();
        JsonObject gameStatus = new JsonObject();
        gameStatus.addProperty("currentPlayer", currentPlayer);
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

            // Carica il nome del current player
            currentPlayer = gameStatus.get("currentPlayer").getAsString();

            // Carica le carte rimaste nel resource deck
            JsonArray resourceDeckArray = gameStatus.getAsJsonArray("resourceDeck");
            resourceDeck = ResourceDeck.fromJson(resourceDeckArray);

            // Carica le carte rimaste nel gold deck
            JsonArray goldDeckArray = gameStatus.getAsJsonArray("goldDeck");
            goldDeck = GoldDeck.fromJson(goldDeckArray);

            // Carica le carte presenti nel pozzo (well)
            JsonArray wellArray = gameStatus.getAsJsonArray("well");
            well = new ArrayList<>();
            for (JsonElement element : wellArray) {
                JsonObject cardObject = element.getAsJsonObject();
                Card card = Card.fromJson(cardObject);
                well.add(card);
            }

            // Aggiorna il currentPlayingPlayer
            for (Player player : players) {
                if (player.getNickName().equals(currentPlayer)) {
                    currentPlayingPLayer = player;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

