package server;
import model.game.Game;
import model.game.Player;

public class Command { //Command Client sends to Server
    private static int checkIfTheBoardHadAlreadyBeenPrinted= 0;

    public String runCommand(Game game, String commandString, Player player, int size, int paolo, String cornerChosen)  { //chiama sempre il game
        System.out.println(commandString);
        switch (commandString) {

            case "showYourCardDeck":{ //finito-> funzionante
                String deckprinted;
                deckprinted= game.showCards(player); //salva nella stringa il deck del giocatore
                return deckprinted;
            }
            case "playCard": //PIETRO
            {
                if(checkIfTheBoardHadAlreadyBeenPrinted==0){
                    String CornersAvailable= game.showAvaiableCorners(player, size, paolo);
                    checkIfTheBoardHadAlreadyBeenPrinted++;
                    return CornersAvailable;
                }
                else if(checkIfTheBoardHadAlreadyBeenPrinted==1)
                {
                    game.playCard(player,size, paolo, cornerChosen);
                    String finalResult="Carta placed";
                    checkIfTheBoardHadAlreadyBeenPrinted--;
                    return finalResult;
                }
            }

            case "TurnCard":
                game.turnCard(player, size);
                return "carta girata";
            case "visualizeCommonObjectiveCards":
                String commonCards;
                commonCards= game.visualizeCommonObjective(player);
                return commonCards;
            case "secret":
                String secretObjectiveCard;
                secretObjectiveCard= game.visualizeSecretObjective(player); //salva nella stringa la carta obiettivo segreta del giocatore
                return secretObjectiveCard;
            case "showBoard":
                String yourBoard;
                yourBoard= game.showBoard(player);
                return yourBoard;
            case "showPoints":
                String playerPoints;
                playerPoints= game.showPoints(player);
                return playerPoints;
            case "showWell":
                String wellPrinted;
                wellPrinted= game.showWell();
                return wellPrinted;
            case "drawCardFromResourceDeck":
                String cardFromResourceDeck;
                cardFromResourceDeck = game.drawResourceCard(player);
                return cardFromResourceDeck;
            case "drawCardFromGoldDeck":
                String cardFromGoldDeck;
                cardFromGoldDeck = game.drawGoldCard(player);
                return cardFromGoldDeck;
            case "drawCardFromWell":
                String operationResult;
                operationResult= game.drawCardFromWell(player, size);
                return operationResult;
            case "showEachPlayerBoard":
                String allboards;
                allboards= game.showAllPlayersBoard();
                return  allboards;
            case "showYourSpecificSeed":
                String allStrings;
                allStrings= game.showYourspecificSeeds(player);
                return allStrings;
            case "showAllSpecificSeed":
                String allseeds;
                allseeds= game.showAllSpecificSeed();
                return allseeds;
            case "showAllPoints" :
                return game.showAllPoints();
            case "endTurn":
                game.runEndTurn(player);
                return "fine turno";
            case "help":{ //finito
                System.out.println("Sono entrato in help");
                return "You asked for help";
            }
            case"firstCardResourceGui":
                return game.getFirstCardOfResourceDeck();
            case "firstCardGoldGui":
                return game.getFirstCardOfGoldDeck();
            case "persistenceGame":
                //game.alreadyExistsAnotherGame();
            case "status":
                return String.valueOf(game.getCurrentPlayingPLayer().getPlayerScore());
            case "quit":
                game.runEndTurn(player);
                return "quit";
            case "firstCommon":
                return game.firstCommonObjectiveCardId();
            case "secondCommon":
                return game.secondCommonObjectiveCardId();
            case "firstWellId":
                return game.sendWellIdFirstToGui();
            case "secondWellId":
                return game.sendWellIdSecondToGui();
            case "thirdWellId":
                return game.sendWellIdThirdToGui();
            case "fourthWellId":
                return game.sendWellIdFourthToGui();
            case "deckId":
                return game.getDeckID(player);
            case "endgame":
                game.endGame();
            case "resourceDeckUpdate":
                game.resourceDeckUpdateForGUi();
                return "Resource Deck Correctly updated";
            case "goldDeckUpdate":
                game.goldDeckUpdateForGUI();
                return "Gold Deck Correctly Updated";
//            case "SETUPFINISHED":
//                return "SETUPFINISHED";
            case "totPlayers":
                return String.valueOf(game.getTotalNumberOfPLayer());
            case "STARTGUI":
                return "STARTGUI";
            default:
                System.out.println("Something gone wrong");
                System.out.println("Unknown command received: " + commandString);
                return "Unknown command.";
        }
    }
}
