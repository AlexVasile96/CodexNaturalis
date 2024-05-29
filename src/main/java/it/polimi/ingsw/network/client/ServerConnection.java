package it.polimi.ingsw.network.client;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exceptions.OperationCancelledException;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.view.ClientView;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class ServerConnection implements Runnable {
    private static int index=0;
    private Socket socket;
    private ClientView clientView;
    private final BufferedReader in;
    private final BufferedReader stdin;
    private final PrintWriter out;
    private final Player player;
    private String currentPlayer= null;
    private boolean isConnectionClosed= false;
    private boolean isTheWhileActive=false;
    private boolean hasTheServerCrashed=false;
    private boolean endGameForWinningPlayer = false;
    private boolean lastTurn = false;
    private String winningPlayer = null;
    private boolean clientPersisted=false;
    private boolean hasSomebodyQuit=false;
    private static int totalPlayers=0;
    private static int loggedInPlayers = 0;  // Static field to keep track of logged-in players


    public ServerConnection(Socket server,ClientView clientView ) throws IOException {
        this.clientView=clientView;
        this.socket = server;
        this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));    //receiving server data
        this.out= new PrintWriter(socket.getOutputStream(), true);
        this.stdin= new BufferedReader(new InputStreamReader(System.in));
        this.player=new Player(null,0,null,null );
    }

    /**
     * This method runs the server-client communication.
     * It manages the login process, game initialization, and game moves.
     */
    @Override
    public void run() {
    String command;
    try {
        socket.setSoTimeout(180000); // Sets the socket timeout to 60 seconds
        System.out.println("Welcome! I'm the server, please type anything to start the conversation!\n");
        while (!isTheWhileActive) {
            try {
                // If the client hasn't typed a message yet
                if (clientView.getUserName() == null) { // If client hasn't logged in yet, prompt for login
                    System.out.print(">");
                    command = stdin.readLine();
                    sendMessageToServer(command);
                    loginPlayer(player); // Perform actual login
                    if (clientPersisted) {
                        System.out.println("You don't need to log in again!");
                        System.out.println("Your data are being processed...");
                    } else {
                        noPersistenceLogin();
                    }
                } else {
                    // If the client has already logged in, handle the game loop
                    while (!isTheWhileActive) {
                        if (isConnectionClosed) {
                            isTheWhileActive = true;
                        } else {
                            waitUntilItsYourTurn();
                            if (hasSomebodyQuit) {
                                return;
                            }
                            makeYourMoves();
                        }
                    }
                }
            } catch (IOException e) {
                // If an IOException occurs, the server has likely crashed
                System.out.println("Timeout: server crashed");
                hasTheServerCrashed = true;
                isTheWhileActive = true;
            }
        }
        if (hasTheServerCrashed) {
            // Close the connection if the server has crashed
            in.close();
            out.close();
            socket.close();
            System.out.println("Connection with server has been closed, thank you for playing Codex!");
        } else {
            try {
                in.close();
                out.close();
                socket.close();
                System.out.println("Connection with server has been closed, thank you for playing Codex!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } catch (SocketException e) {
        System.out.println("Timeout: the server did not respond within 60 seconds.");
        // Handle the socket closure or other necessary actions
        // Close the socket and exit the thread, if necessary
        isTheWhileActive = true; // Exit the main loop
    } catch (InterruptedException | IOException e) {
        throw new RuntimeException(e);
    }
    }



    /**
     * Waits until it is the client's turn to play.
     * This method continuously checks if the current player is the client and handles various game events.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void waitUntilItsYourTurn() throws IOException {
        System.out.println("I'm waiting for the server to pronounce my name!");
        while (!clientView.getUserName().equals(getCurrentPlayer())) {
            if (hasSomebodyQuit) {
                return; // Exit if someone has quit
            }
            String waitForCall = in.readLine(); // Read the next input from the server

            // If it's the client's turn
            if (waitForCall.equals(clientView.getUserName())) {
                setCurrentPlayer(waitForCall); // Set the current player
                System.out.println(getCurrentPlayer()); // Print the current player
                in.readLine(); // Read the "endturn" signal from the server

                // If someone has reached 20 points
            } else if (waitForCall.equals("You smashed 20 points!! now everybody got one last turn")) {
                lastTurn = true; // Set the last turn flag
                winningPlayer = in.readLine(); // Read the winning player

                // If the game has ended
            } else if (waitForCall.equals("END OF GAME!")) {
                System.out.println("------------\nEND GAME\n------------");
                waitForCall = in.readLine();
                do{
                    System.out.println(waitForCall);
                    waitForCall = in.readLine();
               }while (!waitForCall.equals("exit"));
            }
            else if(waitForCall.equals("ALL_CLIENTS_QUIT"))
            {
                in.readLine();
                System.out.println("One client decided to quit, so the game will end for everyone!");
                in.close(); // Close the input stream
                out.close(); // Close the output stream
                socket.close(); // Close the socket
                System.out.println("Connection with server has been closed, thank you for playing Codex!");
                hasSomebodyQuit = true; // Set the quit flag

                // If the current player is still deciding
            } else {
                System.out.println("Current Player is still deciding what's his next move...");
            }
        }
    }


    /**
     * Handles the player's turn to make moves.
     * This method checks if the player's deck has started, processes end game scenarios, and manages the player's commands.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void makeYourMoves() throws IOException {
        // Check if the player's deck has started
        if (!player.isThePlayerDeckStarted()) {
            player.setThePlayerDeckStarted(true); // Mark the player's deck as started
            showCards(); // Display the player's cards
        }

        // Check if the game should end for the winning player
        if (endGameForWinningPlayer) {
            System.out.println("------------\nEND GAME\n------------");
            String waitForCall = in.readLine();
            do{
                System.out.println(waitForCall);
                waitForCall = in.readLine();
            }while (!waitForCall.equals("exit"));
            quit();
            in.readLine();
            System.out.println(in.readLine());
            quit(); // Quit the game
            return;
        }


        if (lastTurn && !player.isHasThePlayerAlreadyPLacedACard()) {
            System.out.println("-----------------------------------------------------------\n" + winningPlayer + " has reached 20Pts! This is the last turn!\n-----------------------------------------------------------");
        }

        // Notify the player that it's their turn
        System.out.println("It's your turn!");
        System.out.println("What do you want to do?");
        System.out.println("Please type help if you want to see which moves you can make.");

        // Read the player's command
        String command = stdin.readLine().toLowerCase();

        // Validate the player's command
        if ((command.equals("playcard") || command.equals("1")) && player.isHasThePlayerAlreadyPLacedACard()) {
            System.out.println("You already placed and drew a card!");
            return;
        } else if ((command.equals("endturn") || command.equals("7")) && !player.isHasThePlayerAlreadyPLacedACard()) {
            System.out.println("You have to place a card first");
            return;
        }

        // Process the player's action based on the command
        actionsInput(command);
    }

    /**
     * Processes the player's input commands and performs the corresponding actions.
     * This method handles various game commands such as showing the deck, playing a card, and ending the turn.
     *
     * @param userInput the input command entered by the player.
     * @throws IOException if an I/O error occurs while performing actions.
     */
    private void actionsInput(String userInput) throws IOException {
        try {
            switch (userInput) {
                case "help" -> printHelp(); // Show help message
                case "actions" -> printActions(); // Show list of actions
                case "showdeck", "0" -> showCards(); // Show the player's deck
                case "playcard", "1" -> playCardFromYourDeck(); // Play a card from the player's deck
                case "common", "2" -> visualizeCommonObjective(); // Visualize the common objective
                case "secret", "3" -> visualizeSecretObjective(); // Visualize the secret objective
                case "board", "4" -> showBoard(); // Show the game board
                case "points", "5" -> showPoints(); // Show the player's points
                case "showwell", "6" -> showWell(); // Show the well
                case "endturn", "7" -> runEndTurn(); // End the player's turn
                case "allboards", "8" -> showEachPlayerBoard(); // Show each player's board
                case "yourseeds", "9" -> showYourSpecificSeed(); // Show the player's specific seeds
                case "allseed", "10" -> showAllSpecificSeed(); // Show all specific seeds
                case "allpoints", "11" -> showAllPoints(); // Show all points
                case "quit", "12" -> quit(); // Quit the game
                default ->
                        System.out.println("This command is not supported. Press 'help' for a list of all available commands."); // Handle unsupported commands
            }
        } catch (OperationCancelledException exception) {
            System.out.println(exception.getMessage()); // Print exception message if an operation is cancelled
        }
    }

    /**
     * Requests and displays the points of all players.
     * This method sends a request to the server to get all players' points and prints them until the server sends an "exit" message.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */

    private void showAllPoints() throws IOException {
        sendMessageToServer("showAllPoints"); // Send request to the server
        String messageFromServer = in.readLine(); // Read the first line from the server
        do {
            System.out.println(messageFromServer); // Print the server message
            messageFromServer = in.readLine(); // Read the next line from the server
        } while (!messageFromServer.equals("exit")); // Continue until "exit" is received
    }

    /**
     * Requests and displays the player's card deck.
     * This method sends a request to the server to get the player's card deck and prints the cards.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void showCards() throws IOException {
        sendMessageToServer("showYourCardDeck"); // Send request to the server
        System.out.println("Your deck:");
        System.out.println("--------------------------------------------------------------------------------------");
        receivingPrintingUpdatingCards(); // Receive, print, and update the cards
        System.out.println("--------------------------------------------------------------------------------------");
    }

    /**
     * Receives, prints, and updates the player's cards.
     * This method reads three cards from the server, updates the view, and prints the cards.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void receivingPrintingUpdatingCards() throws IOException {
        String firstCard = in.readLine(); // Read the first card from the server
        String secondCard = in.readLine(); // Read the second card from the server
        String thirdCard = in.readLine(); // Read the third card from the server
        in.readLine(); // Read the space line
        updatingView(firstCard, secondCard, thirdCard); // Update the view with the new cards


        // Print the player's cards
        for (String s : player.getClientView().getPlayerStringCards()) {
            System.out.println(s);
        }
    }

    /**
     * Requests and displays the boards of all players.
     * This method sends a request to the server to get each player's board and prints them until the server sends an "exit" message.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void showEachPlayerBoard() throws IOException {
        sendMessageToServer("showEachPlayerBoard"); // Send request to the server
        System.out.println("You decided to print all players boards!");
        String messageFromServer = in.readLine(); // Read the first line from the server
        do {
            System.out.println(messageFromServer); // Print the server message
            messageFromServer = in.readLine(); // Read the next line from the server
        } while (!messageFromServer.equals("exit")); // Continue until "exit" is received
        System.out.println("All Boards Printed!");
    }

    /**
     * Requests and displays the player's specific seeds.
     * This method sends a request to the server to get the player's specific seeds and prints them.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void showYourSpecificSeed() throws IOException {
        sendMessageToServer("showYourSpecificSeed"); // Send request to the server
        System.out.println("Your specific seeds: ");
        String yourSeeds = in.readLine(); // Read the specific seeds from the server
        System.out.println(yourSeeds); // Print the specific seeds
    }

    /**
     * Requests and displays the specific seeds of all players.
     * This method sends a request to the server to get all specific seeds and prints them until the server sends an "exit" message.
     *
     * @throws IOException if an I/O error occurs while reading from the input stream.
     */
    private void showAllSpecificSeed() throws IOException {
        sendMessageToServer("showAllSpecificSeed"); // Send request to the server
        String messageFromServer = in.readLine(); // Read the first line from the server
        do {
            System.out.println(messageFromServer); // Print the server message
            messageFromServer = in.readLine(); // Read the next line from the server
        } while (!messageFromServer.equals("exit")); // Continue until "exit" is received
    }

    /**
     * Updates the player's view with new cards.
     * This method updates the player's card view by adding new cards if they are not already present.
     *
     * @param firstCard the first card to update.
     * @param secondCard the second card to update.
     * @param thirdCard the third card to update.
     */
    private void updatingView(String firstCard, String secondCard, String thirdCard) {
        String[] carte = {firstCard, secondCard, thirdCard}; // Create an array of the new cards
        if (player.getClientView().getPlayerStringCards().isEmpty()) { // If the player's card view is empty
            for (int i = 0; i < 3; i++) {
                player.getClientView().getPlayerStringCards().add(carte[i]); // Add all new cards
            }
            return;
        }

        // Check if each card is already present in the player's card view
        boolean present = false;
        for (int i = 0; i < 3; i++) {
            for (String viewCard : player.getClientView().getPlayerStringCards()) {
                if (carte[i].equals(viewCard)) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                player.getClientView().getPlayerStringCards().add(carte[i]); // Add the card if not present
            }
            present = false; // Reset the flag for the next card
        }
    }


    /**
     * Handles the process of playing a card from the player's deck.
     * This method facilitates the player to choose and play a card from their deck, interact with the board, and update the game state.
     *
     * @throws IOException if an I/O error occurs while reading from the input or output streams.
     */
    private void playCardFromYourDeck() throws IOException {
        String messageFromServer, inputFromClient;
        int size;
        boolean check;

        // Set flag to indicate that the player has played a card for this turn
        player.setHasThePlayerAlreadyPLacedACard(true);

        // Prompt the player to play a card and display current game state
        System.out.println("Play a card from your deck!");
        showBoard(); // Show the current state of the game board
        showYourSpecificSeed(); // Display the player's specific seeds
        sendMessageToServer("playCard"); // Notify the server that the player is playing a card

        // Choose a card from the player's deck
        System.out.println("\n------------------------------------------------------------------------------------------------");
        System.out.println("These are your deck cards: ");
        System.out.println(player.getClientView().getPlayerStringCards().get(0));
        System.out.println(player.getClientView().getPlayerStringCards().get(1));
        System.out.println(player.getClientView().getPlayerStringCards().get(2));
        System.out.println("------------------------------------------------------------------------------------------------");
        boolean turnedCardAlready = false;
        do {
            // Prompt the player to choose a card from their deck
            System.out.println("Which card do you want to play on the board?\n1-> first card\n2-> second card\n3-> third card");
            size = Integer.parseInt(controlInputFromUser(new String[]{"1", "2", "3"}));
            out.println(size - 1); // Send the chosen card index to the server
            messageFromServer = in.readLine(); // Receive server response

            // Handle special case if a Gold Card cannot be placed
            if (messageFromServer.equals("Gold Card not placeable")) {
                // Notify the player about the requirements for placing a Gold Card
                messageFromServer = in.readLine();
                System.out.println("Gold card requires: " + messageFromServer);

                // Notify the player about the specific seed they possess
                messageFromServer = in.readLine();
                System.out.println("You got: " + messageFromServer);

                // Prompt the player to choose another card or turn the current card
                System.out.println("You can:\n1-> choose another card\n2-> turn the card");
                inputFromClient = controlInputFromUser(new String[]{"1", "2"});
                sendMessageToServer(inputFromClient);

                // Check if the player decided to turn the card
                if (inputFromClient.equals("2")) {
                    turnedCardAlready = true;
                    messageFromServer = "puoi procedere"; // Continue message
                    in.readLine(); // Receive server response after turning the card
                }
            }
        } while (!messageFromServer.equals("puoi procedere")); // Continue loop until the player can proceed

        // Remove the chosen card from the player's deck
        player.getClientView().getPlayerStringCards().remove(size - 1);

        // Decide whether to turn the card if not turned already
        if (!turnedCardAlready) {
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("Do you want to turn your card?\n(Back of all cards has 4 empty corners and 1 attribute representing the specific seed of the card)");
            System.out.println("Please type\n1-> To TURN your card\n2-> To NOT TURN your card");
            inputFromClient = controlInputFromUser(new String[]{"1", "2"});
            sendMessageToServer(inputFromClient);
            if (inputFromClient.equals("1")) in.readLine(); // Receive server response after turning the card
        }

        // Place the card on the board
        boolean rightCard;
        String[] corners;
        String[] validInputs;
        int numCardsOnBoard;
        do {
            // Display the cards on the board and prompt the player to choose a card to place the new card on
            System.out.println("Your cards on the board: ");
            for (String s : player.getClientView().getCardsOnTheBoard()) {
                System.out.println(s);
            }
            System.out.println("\nWhich card on the board do you want to place your card on?");

            // Initialize valid inputs array for card selection
            numCardsOnBoard = player.getClientView().getNumOfCardsOnTheBoard();
            validInputs = new String[numCardsOnBoard]; // If only the initial card is present, it's equal to 1
            for (int j = 0; j < numCardsOnBoard; j++) {
                validInputs[j] = String.valueOf(j + 1);
            }
            int chosenBoardCard = Integer.parseInt(controlInputFromUser(validInputs)); // Store the chosen card index
            out.println(chosenBoardCard - 1);

            // Available corners for placing the new card
            corners = new String[]{"TL", "TR", "BR", "BL"};
            validInputs = new String[4];
            check = false;
            size = 0;
            messageFromServer = in.readLine();
            do {
                for (String corner : corners) {
                    if (!check && messageFromServer.equals(corner)) {
                        validInputs[size] = messageFromServer;
                        size++;
                        check = true;
                    }
                }
                if (!check) System.out.println(messageFromServer);
                check = false;
                messageFromServer = in.readLine();
            } while (!messageFromServer.equals("end"));

            // Check if the chosen card has available corners for placement
            if (size == 0) {
                rightCard = false;
                System.out.println("-----------------------------------------------------------\nThe chosen card has no free corners! Choose another card!\n-----------------------------------------------------------");
                out.println("clean"); // Notify the server to clean the input buffer
            } else rightCard = true;
        } while (!rightCard); // Continue loop until a valid card is chosen for placement

        // Prompt the player to choose the corner to place the card on
        System.out.print("Choose the corner you want to place the card on: ");
        if (size < 4) {
            // If there are fewer available corners than the total, create a new array with the correct size
            corners = new String[size];
            for (int i = 0; i < size; i++) {
                corners[i] = validInputs[i];
            }
            // Prompt the player to choose a corner from the available options
            inputFromClient = controlInputFromUser(corners);
        } else {
            // If all corners are available, prompt the player to choose any corner
            inputFromClient = controlInputFromUser(validInputs);
        }
        out.println(inputFromClient); // Send the chosen corner to the server

        // Finalize the placement of the card on the board and update the view
        System.out.println(in.readLine()); // Display the confirmation message from the server
        String typeCard = in.readLine(); // Receive the type of card placed
        String isBack = in.readLine(); // Receive information about whether the card is facing back
        String coordinateTL = in.readLine(); // Receive the coordinates of the top-left corner
        // Add the newly placed card to the player's view of the board
        player.getClientView().addCardOnTheBoard((numCardsOnBoard + 1) + "->" + typeCard + ": " + coordinateTL + " " + isBack);
        String newCard=(numCardsOnBoard + 1) + "->" + typeCard + ": " + coordinateTL + " " + isBack;
        out.println(newCard);
        player.getClientView().setNumOfCardsOnTheBoard(numCardsOnBoard + 1); // Increment the number of cards on the board

        // Draw a new card and calculate the player's current points
        drawCard();
        int points = status(); // Retrieve the player's current points
        System.out.println();

        // Check if the player has reached 20 points and it's not the last turn
        if (points >= 20 && !lastTurn) {
            System.out.println(in.readLine()); // Display the end game message from the server
            System.out.println("----> " + in.readLine() + " got 20 points"); // Display the player who reached 20 points
            System.out.println("Your turn is over!");
            endGameForWinningPlayer = true; // Set flag to indicate the end game for the winning player
            runEndTurn(); // Run the end turn method to finalize the player's turn
        }
    }

    /**
     * Validates the user input against the provided elements and returns the validated input.
     *
     * @param elements An array of strings containing the valid elements for the input.
     * @return The validated input provided by the user.
     * @throws IOException If an input/output error occurs while reading user input.
     */
    private String controlInputFromUser(String[] elements) throws IOException {
        String inputClient;
        boolean found = false;
        do {
            // Reads the input from the user and converts it to uppercase for consistency
            inputClient = stdin.readLine().toUpperCase();
            // Checks if the provided input is present in the array of valid elements
            for (String element : elements) {
                if (inputClient.equals(element.toUpperCase())) {
                    found = true;
                    break;
                }
            }
            // If the input is invalid, prints an error message and prompts for new input
            if (!found) {
                System.out.println("Invalid input! Please try again!");
            }
        } while (!found);
        return inputClient;
    }

    /**
     * Gets the current player's status from the server and updates the player's score in the client view.
     *
     * @return The current player's score.
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private int status() throws IOException {
        sendMessageToServer("status");
        // Gets the current player's score from the server
        String points = in.readLine();
        if(points.equals("")) points = in.readLine();
        // Prints the current player's score
        System.out.println("You have obtained: " + points + " points!");
        // Updates the player's score in the client view
        clientView.setPlayerScore(Integer.parseInt(points));
        return Integer.parseInt(points);
    }


    /**
     * Displays the available common objective cards in the game.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void visualizeCommonObjective() throws IOException {
        sendMessageToServer("visualizeCommonObjectiveCards");
        // Displays the available common objective cards
        System.out.println("The common objective cards are:\n");
        System.out.println(in.readLine()); // First common card
        System.out.println(in.readLine()); // Second common card
    }

    /**
     * Displays the player's secret objective card.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void visualizeSecretObjective() throws IOException {
        sendMessageToServer("secret");
        // Displays the player's secret objective card
        System.out.println("You chose to visualize your secret card!\n");
        String result = in.readLine();
        System.out.println(result);
        System.out.println("\n");
        System.out.println("This is your objective card!");
    }

    /**
     * Displays the current state of the game board received from the server.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void showBoard() throws IOException {
        sendMessageToServer("showBoard");
        // Prints the header of the game board
        System.out.print("////////////////////////////////////////////////////// START BOARD ///////////////////////////////////////////////////////////// \n");
        String result = in.readLine();
        // Displays the contents of the board until "end board" is received
        do {
            System.out.println(result);
            result = in.readLine();
        } while (!result.equals("fine board"));
        // Prints the footer of the game board
        System.out.println();
        System.out.println("////////////////////////////////////////////////////// END BOARD ///////////////////////////////////////////////////////////////");
    }


    /**
     * Displays the current player's score received from the server.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void showPoints() throws IOException {
        sendMessageToServer("showPoints");
        // Displays the current player's score
        System.out.println("You chose to visualize your points!\n");
        String result = in.readLine();
        // Sets the player's score in the client view
        clientView.setPlayerScore(Integer.parseInt(result));
        System.out.println("At the moment your points are: " + result);
    }

    /**
     * Displays the cards present in the common well of the game received from the server.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void showWell() throws IOException {
        sendMessageToServer("showWell");
        // Displays the cards present in the common well
        System.out.println("Common Well:\n------------------------------------------------------------------------------------------");
        System.out.println(in.readLine()); // First card in the well
        System.out.println(in.readLine()); // Second card in the well
        System.out.println(in.readLine()); // Third card in the well
        System.out.println(in.readLine()); // Fourth card in the well
        in.readLine(); // Space
        System.out.println("------------------------------------------------------------------------------------------");
    }

    /**
     * Allows the player to draw a card from the deck or the common well.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void drawCard() throws IOException {
        showWell(); // Displays the cards present in the common well
        sendMessageToServer("drawCard");
        System.out.println("You chose to draw a card!\n");
        String drawnCard;
        do {
            // Asks the player to select where to draw the card from (deck or well)
            System.out.println("""
            Where do you want to draw your card from?
            1-> deck
            2-> well""");
            drawnCard = stdin.readLine().toLowerCase();
            if (drawnCard.equals("deck") || drawnCard.equals("1")) {
                sendMessageToServer("deck");
                drawCardFromDeck();
            }
            else if (drawnCard.equals("well") || drawnCard.equals("2")) {
                sendMessageToServer("well");
                drawCardFromWell();
            }
            else System.out.println("Write 'deck' or 'well'");
        } while (!drawnCard.equals("well") && !drawnCard.equals("deck") && !drawnCard.equals("1") && !drawnCard.equals("2"));
    }



    /**
     * Allows the player to draw a card from the resource deck or the gold deck.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void drawCardFromDeck() throws IOException {
        System.out.println("""
        Where do you want to draw your card from?
        1-> Resource
        2-> Gold""");
        String drawnCard;
        do {
            drawnCard = stdin.readLine().toLowerCase();
            if (drawnCard.equals("resource") || drawnCard.equals("1")) {
                drawCardFromResourceDeck(); // Draws a card from the resource deck
            } else if (drawnCard.equals("gold") || drawnCard.equals("2")) {
                drawCardFromGoldDeck(); // Draws a card from the gold deck
            } else {
                System.out.println("Write 'resource' or 'gold'");
            }
        } while (!drawnCard.equals("resource") && !drawnCard.equals("gold") && !drawnCard.equals("1") && !drawnCard.equals("2"));
    }

    /**
     * Draws a card from the resource deck.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void drawCardFromResourceDeck() throws IOException {
        sendMessageToServer("drawCardFromResourceDeck"); // Sends the command to the server to draw a card from the resource deck
        System.out.println(in.readLine()); // Displays the confirmation message received from the server
        sendMessageToServer("showYourCardDeck"); // Requests the server to display the player's deck
        System.out.println("Your Deck:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingPrintingUpdatingCards(); // Receives, prints, and updates the cards in the player's deck
        System.out.println("--------------------------------------------------------------------------------------");
    }

    /**
     * Draws a card from the gold deck.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void drawCardFromGoldDeck() throws IOException {
        sendMessageToServer("drawCardFromGoldDeck"); // Sends the command to the server to draw a card from the gold deck
        System.out.println(in.readLine()); // Displays the confirmation message received from the server
        sendMessageToServer("showYourCardDeck"); // Requests the server to display the player's deck
        System.out.println("Your Deck:" );
        System.out.println("--------------------------------------------------------------------------------------");
        receivingPrintingUpdatingCards(); // Receives, prints, and updates the cards in the player's deck
        System.out.println("--------------------------------------------------------------------------------------");
    }



    /**
     * Draws a card from the common well.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void drawCardFromWell() throws IOException {
        sendMessageToServer("showWell");
        System.out.println("Which card from the well do you want to draw?");
        System.out.println("------------------------------------------------------------------------------------------");
        // Displays the available options for cards in the well
        System.out.println("Select '0' for " + in.readLine());
        System.out.println("Select '1' for " + in.readLine());
        System.out.println("Select '2' for " + in.readLine());
        System.out.println("Select '3' for " + in.readLine());
        in.readLine(); // Space
        System.out.println("------------------------------------------------------------------------------------------");

        String selectedCard;
        do {
            selectedCard = readMessageFromUser(); // Reads user input
            if (wrongChoice(selectedCard)) {
                System.out.println("Wrong choice, try again");
            }
        } while (wrongChoice(selectedCard));

        sendMessageToServer(selectedCard); // Sends the chosen card selection to the server

        // Handles server responses
        String result = in.readLine();
        if (result.equals("operation performed correctly")) {
            System.out.println("Operation 'Draw card from Well' performed correctly");
            sendMessageToServer("showYourCardDeck");
            System.out.println("Your Deck:");
            System.out.println("--------------------------------------------------------------------------------------");
        } else {
            System.out.println("Operation failed");
            System.out.println("Server says: " + result);
            System.out.println("Your Deck:");
            System.out.println("--------------------------------------------------------------------------------------");
            sendMessageToServer("showYourCardDeck");
        }
        receivingPrintingUpdatingCards();
        System.out.println("--------------------------------------------------------------------------------------");
        showWell();
    }

    /**
     * Handles incorrect user input for selecting a card from the well.
     *
     * @param selectedCard The card selected by the user.
     * @return True if the input is incorrect, otherwise false.
     */
    private boolean wrongChoice(String selectedCard) {
        int num = Integer.parseInt(selectedCard);
        return num < 0 || num > 3;
    }

    /**
     * Ends the connection with the server and closes the application.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void quit() throws IOException {
        sendMessageToServer("quit");
        System.out.println("You chose to quit Codex :c \n");
        System.out.println(in.readLine()); // Confirmation response from the server
        isConnectionClosed = true; // Sets the flag to indicate the connection is closed
    }



    /**
     * Handles the end of the player's turn.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void runEndTurn() throws IOException {
        sendMessageToServer("endTurn"); // Sends the command to the server to end the turn

        // If it's not the last turn for the winning player, reset the current player's turn state
        if (!endGameForWinningPlayer) {
            player.setHasThePlayerAlreadyPLacedACard(false); // Player can place a card again
            System.out.println("You chose to end your turn.");
        }

        String answer = in.readLine(); // Gets the name of the next player
        System.out.println("Next player will be " + answer);
        setCurrentPlayer(answer); // Sets the new current player
        String updatingCurrentPlayer = in.readLine(); // Updates the current player on the server
        System.out.println(updatingCurrentPlayer);
        cleanTheSocket(); // Cleans up the socket after communicating with the server
    }

    /**
     * Handles exiting the game.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void exitFromGame() throws IOException {
        sendMessageToServer("endTurn"); // Sends the command to the server to end the turn
        String answer = in.readLine(); // Gets the name of the next player
        setCurrentPlayer(answer); // Sets the new current player

        // If all clients have quit the game, show an appropriate message
        if (answer.equals("All clients have quit")) {
            System.out.println("All clients have quit");
        } else {
            System.out.println("Current player: " + currentPlayer);
            String updatingCurrentPlayer = in.readLine(); // Updates the current player on the server
            System.out.println(updatingCurrentPlayer);
        }
        cleanTheSocket(); // Cleans up the socket after communicating with the server
    }



    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Handles the assignment of the initial card to the player.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void takingTheInitialCard() throws IOException {
        String firstCard = in.readLine(); // Gets the first card from the server
        String FrontalCorners = in.readLine(); // Gets the front corners of the card from the server
        String BackCorners = in.readLine(); // Gets the back corners of the card from the server
        in.readLine(); // For GUI purposes, can be skipped
        System.out.println("Server says: " + firstCard); // Prints the first card received from the server
        System.out.println(FrontalCorners); // Prints the front corners of the card
        System.out.println(BackCorners); // Prints the back corners of the card
        int size;
        System.out.println("Do you want to turn your card?");
        System.out.println("1-> To turn your card\n2->to NOT turn your card");
        size = Integer.parseInt(controlInputFromUser(new String[]{"1", "2"})); // Reads user input
        String isBack;
        if (size == 1) isBack = "(back)"; // If the user chooses to turn the card, sets type to "back"
        else isBack = "(front)"; // Otherwise, sets type to "front"
        out.println(size - 1); // Sends to the server whether the card has been turned or not
        System.out.println("Initial Card correctly placed!");
        // Updates the player's view with the initial card placed
        player.getClientView().addCardOnTheBoard("1->Initial Card: (24 24) " + isBack);
    }

    /**
     * Handles the assignment of the secret card to the player.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private synchronized void assigningSecretCard() throws IOException {
        boolean isNumberCorrect = false; // Flag to check if the chosen number is correct
        String stringSecretCard = in.readLine(); // Gets the first secret card from the server
        String stringSecondCard = in.readLine(); // Gets the second secret card from the server
        in.readLine(); // ID of the first card, can be skipped
        in.readLine(); // ID of the second card, can be skipped
        System.out.println("Server says: your first objective card is " + stringSecretCard);
        System.out.println("Server says: your second objective card is " + stringSecondCard);
        while (!isNumberCorrect) {
            System.out.println("Choose the card you want to draw:\n1-> First card\n2-> Second card");
            String numberChosen = controlInputFromUser(new String[]{"1", "2"}); // Reads user input
            int size = Integer.parseInt(numberChosen);
            if (size != 1 && size != 2) {
                System.out.println("Please choose your card correctly!");
            } else {
                System.out.println("Card chosen correctly");
                out.println(size); // Sends the chosen card to the server
                isNumberCorrect = true; // Sets the flag to true to indicate the chosen number is correct
            }
        }
    }

    /**
     * Handles the player's login process to the server.
     *
     * @param player The player who is logging in.
     * @throws IOException If an input/output error occurs while communicating with the server.
     * @throws InterruptedException If the waiting is interrupted while the thread is waiting.
     */
    private synchronized void loginPlayer(Player player) throws IOException, InterruptedException {
        boolean isTheNameAlreadyTaken = false; // Flag to check if the username is already taken
        String serverResponse = in.readLine(); // Gets the server response
        String loginName = null; // User login name
        String correctLogin = null; // Correct login response from the server
        System.out.println("Server says: " + serverResponse); // Asks for the username
        System.out.print(">");
        while (!isTheNameAlreadyTaken) {
            loginName = stdin.readLine(); // Reads the username entered by the user
            sendMessageToServer(loginName); // Sends the username to the server for checking
            correctLogin = in.readLine(); // Gets the response from the server
            if ((correctLogin).equals("Username already taken. Please choose another username:")) {
                System.out.println("Username already taken. Please choose another username:");
                System.out.print(">");
            } else if ((correctLogin).equals("Welcome back, " + loginName + "! Your data has been loaded.")) {
                clientPersisted=true;
                System.out.println("Welcome back, " + loginName + "! Your data has been loaded.");
                clientView.setUserName(loginName); // Set the username in client view
                player.getClientView().setUserName(loginName); // Set the username in player model
                String clientViewJsonString = in.readLine();
                JsonObject clientViewJson = JsonParser.parseString(clientViewJsonString).getAsJsonObject();
                ClientView clientView = ClientView.fromJsonObject(clientViewJson);
                player.setClientView(clientView);
                System.out.println(player.getClientView());
                System.out.println("Cards on the board: " + clientView.getCardsOnTheBoard());
                System.out.println(clientView.getNumOfCardsOnTheBoard());
                waitForGameStart();
                return;
            } else isTheNameAlreadyTaken = true; // Sets the flag to true if the username is available
        }

        System.out.println("Server says: " + correctLogin); // Login successfully done
        player.getClientView().setUserName(loginName); // Sets the username in the client model
        clientView.setUserName(loginName); // Updates the client view with the username
        synchronized (this) {
            clientView.setIndex(index); // Sets the index in the client model
            index++;
        }
        synchronized (this) {
            chooseYourDotColor(); // Asks to choose the dot color
        }
        chooseNumberOfPlayers(); // Asks to choose the number of players
    }


    /**
     * Allows the player to choose their dot color.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private void chooseYourDotColor() throws IOException {
        String messageFromServer;
        do {
            messageFromServer = in.readLine(); // Gets the message from the server
            System.out.println("Server says: " + messageFromServer); // Prints the message
            System.out.print(">");
            String dotColor = stdin.readLine(); // Reads the dot color chosen by the user
            dotColor = dotColor.toUpperCase(); // Converts the color to uppercase
            sendMessageToServer(dotColor); // Sends the color to the server
            messageFromServer = in.readLine(); // Gets the response from the server
            System.out.println(messageFromServer); // Prints the response
        } while (messageFromServer.equals("Chosen color not available!")); // Continues until the chosen color is not available
    }

    /**
     * Allows the player to choose the number of players.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private synchronized void chooseNumberOfPlayers() throws IOException {
        String serverMessage = in.readLine(); // Gets the message from the server to choose the number of players
        System.out.println(serverMessage); // Prints the message
        if (serverMessage.equals("There's already someone online! Please wait")) {
            System.out.println(in.readLine()); // Gets the message from the server
            System.out.println(in.readLine()); // Gets the message from the server
            return;
        }
        int size = Integer.parseInt(stdin.readLine()); // Reads the number of players entered by the user
        out.println(size); // Sends the number of players to the server
        System.out.println("The number of players is: " + size); // Prints the number of players
        String serverAnswer = in.readLine(); // Gets the response from the server
        System.out.println("Server says: " + serverAnswer); // Prints the server response
        System.out.println(in.readLine()); // Gets the message from the server
        System.out.println(in.readLine()); // Gets the message from the server
    }

    /**
     * Prints the list of supported commands.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */
    private synchronized void printHelp() throws IOException {
        sendMessageToServer("help"); // Sends the help request to the server
        String serviceString = in.readLine(); // Gets the service string from the server
        System.out.println(serviceString); // Prints the service string
        System.out.println("""
            Supported commands are:
            - 'actions': shows all currently allowed game actions
            """); // Prints the list of supported commands
    }

    private void printActions() {
        System.out.println(
                """
                        Supported commands:
                        - If you type-> 'showdeck / 0 ': display player's cards
                        - If you type-> 'playcard /1': select the card you want to place from your hand
                        - If you type->  'common /2': visualize the common objective cards
                        - If you type->  'secret /3': visualize your secret objective card
                        - If you type->  'board /4':print your board
                        - If you type->  'points /5': show your points
                        - If you type->  'showWell /6': you'll be displayed the well
                        - If you type->  'endturn /7': end your turn
                        - If you type->  'allboards /8': you'll be displayed your opponent boards
                        - if you type ->  'yourseeds /9': you'll be displayed all the specific seed you have on your board
                        - if you type ->  'allseed /10': you'll be displayed all your opponent specific seed
                        - if you type ->  'allpoints /11': you'll be displayed your opponents' points
                        - if you type ->  'quit /11': you'll quit the game
                        """
        );
    }

    private void cleanTheSocket() {
        out.flush();
    }
    public void sendMessageToServer(String message) {
        out.println(message);
    }
    public String readMessageFromUser() throws IOException {
        return stdin.readLine();
    }
    private void waitForGameStart() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            System.out.println(message);
            if (message.equals("All players connected. Resuming the game...")) {
                //waitUntilItsYourTurn();
                return;
            }
        }
    }

    /**
     * Handles the login process without persistence.
     *
     * @throws IOException If an input/output error occurs while communicating with the server.
     */

    private void noPersistenceLogin() throws IOException {
        System.out.println(in.readLine()); // All clients connected
        String whatIsYourIndex;
        int gameSize;
        gameSize= Integer.parseInt(in.readLine());
        whatIsYourIndex=in.readLine();
        if(Integer.parseInt(whatIsYourIndex)<=gameSize) {
            assigningSecretCard(); // Choosing the secret Card
            takingTheInitialCard(); // Taking the initial Card
            System.out.println("Login phase ended!");
            waitAllPlayers();
        }
        else{
            System.out.println("Lobby is already full, try later!");
            in.close(); // Close the input stream
            out.close(); // Close the output stream
            socket.close(); // Close the socket
            System.exit(0);
        }
    }




    private void waitUntilLastMessage() throws IOException {
        String messageFromServer = in.readLine();
        while (!messageFromServer.equals("STARTGUI")) {
            System.out.println("Server says " + messageFromServer);
            messageFromServer = in.readLine();
            if (messageFromServer.equals("One client decided to quit, so the game will end for every player.")) {
                try {

                    socket.close();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Game finally starting!");
        makeYourMoves();

    }
    private void waitAllPlayers() throws IOException {
        String message=in.readLine();
        switch (message) {
            case "All clients chose the init Card" -> {
                System.out.println(message);
                handleInitCardChoice();
            }
            case "SETUPFINISHED" -> {
                System.out.println(message);
                currentPlayer = in.readLine();
                System.out.println("Current Player:" + currentPlayer);
                String nextPlayer = in.readLine();
                System.out.println("Next PLayer is:" + nextPlayer);

                if (nextPlayer.equals(clientView.getUserName())) {
                    System.out.println("Setup finished, starting game...");
                    gameDataElaboration();
                } else {
                    System.out.println("Not your turn, waiting for setup...");
                    waitAllPlayers();
                }
            }
            case "STARTGUI" -> System.out.println("All clients logged!");
            default -> {
                System.out.println("Not your turn yet, please wait...");
                waitAllPlayers();
            }
        }

    }

    private void gameDataElaboration() throws IOException {
        System.out.println("Initializing game data for client: " + clientView.getUserName());
        out.println("updateLoggedPlayers");
        System.out.println("Server says: " + in.readLine());
        out.println("howManyPlayers");
        loggedInPlayers= Integer.parseInt(in.readLine());
        System.out.println("Logged in players: " + loggedInPlayers);
        out.println("totPlayers");
        totalPlayers= Integer.parseInt(in.readLine());
        System.out.println("Total PLayers in the game: " + totalPlayers);
        if (loggedInPlayers<totalPlayers) {
            out.println("SETUPFINISHED");
            System.out.println(in.readLine());
            System.out.println("First Player in game is "+ in.readLine());
            System.out.println("Next player to setup is " + in.readLine());
        }
        if(loggedInPlayers==totalPlayers)
        {
            System.out.println("LAST PLAYER!");
            out.println("STARTGUI");
            String STARTGUI=in.readLine();
            System.out.println("All clients updated the data, Server says " + STARTGUI);
        }
        if (currentPlayer.equals(clientView.getUserName())) {
            System.out.println("You have to wait until all clients have correctly finished the setup!");
            waitUntilLastMessage();
        } else {
            waitUntilItsYourTurn();
        }
    }

    private void handleInitCardChoice() throws IOException {
        currentPlayer = in.readLine(); // Who is the current player?
        System.out.println("CurrentPlayerNickname is: " + currentPlayer);
        if (in.readLine().equals("You are the first client")) {
            System.out.println("You are the first client");
            System.out.println("Initializing game data for the first client!");
            gameDataElaboration();
        }
        else{
            System.out.println("Initializing GameData for others clients");
            System.out.println("Waiting for fame to start...");
            waitAllPlayers();
        }
    }
}

