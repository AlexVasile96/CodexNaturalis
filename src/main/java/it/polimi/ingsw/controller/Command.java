package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Player;

public class Command { //Command Client sends to Server
    private static int checkIfTheBoardHadAlreadyBeenPrinted= 0;

    public String runCommand(Game game, String commandString, Player player, int size, int paolo, String cornerChosen)  {

        switch (commandString) {

            case "showYourCardDeck":{
                String deckprinted;
                deckprinted= game.showCards(player); //saves the player's deck in the string
                return deckprinted;
            }
            case "playCard":
            {
                if(checkIfTheBoardHadAlreadyBeenPrinted==0){
                    String CornersAvailable= game.showAvailableCorners(player, size, paolo);
                    checkIfTheBoardHadAlreadyBeenPrinted++;
                    return CornersAvailable;
                }else if(cornerChosen.equals("clean")){
                    checkIfTheBoardHadAlreadyBeenPrinted=0;
                    return "cleaned";
                }else if(checkIfTheBoardHadAlreadyBeenPrinted==1) {
                    game.playCard(player,size, paolo, cornerChosen);
                    String finalResult="Carta placed";
                    checkIfTheBoardHadAlreadyBeenPrinted=0;
                    return finalResult;
                }
            }

            case "TurnCard":
                game.turnCard(player, size);
                return "carta girata";
            case "visualizeCommonObjectiveCards":
                String commonCards;
                commonCards= game.visualizeCommonObjective();
                return commonCards;
            case "secret":
                String secretObjectiveCard;
                secretObjectiveCard= game.visualizeSecretObjective(player); //save player's secrete objective in the string
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
                allStrings= game.showYourSpecificSeeds(player);
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
            case "help":{
                return "You asked for help";
            }
            case"firstCardResourceGui":
                return game.getFirstCardOfResourceDeck();
            case "firstCardGoldGui":
                return game.getFirstCardOfGoldDeck();
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
                return game.endGame();
            case "resourceDeckUpdate":
                game.resourceDeckUpdateForGUi();
                return "Resource Deck Correctly updated";
            case "goldDeckUpdate":
                game.goldDeckUpdateForGUI();
                return "Gold Deck Correctly Updated";
            case "goldGui0":
                int index=0;
                return game.checkingIfICanPlaceTheGoldCardOnGui(player,index);
            case "goldGui1":
                int secondIndex=1;
                return game.checkingIfICanPlaceTheGoldCardOnGui(player,secondIndex);
            case "goldGui2":
                int thirdIndex=2;
                return game.checkingIfICanPlaceTheGoldCardOnGui(player,thirdIndex);
            case "totPlayers":
                return String.valueOf(game.getTotalNumberOfPLayer());
            case "STARTGUI":
                return "STARTGUI";
            default:
                System.out.println("Unknown command received: " + commandString);
                return "Unknown command.";
        }
    }
}
