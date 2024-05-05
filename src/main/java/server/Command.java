package server;
import model.game.Game;
import model.game.Player;

public class Command { //Command Client sends to Server
    private static int checkIfTheBoardHadAlreadyBeenPrinted= 0;

    public String runCommand(Game game, String commandString, Player player, int size, int paolo, String cornerChosen) { //chiama sempre il game
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
                String allPoints = game.showAllPoints();
                return allPoints;
            case "endTurn":
                game.runEndTurn(player);
                return "fine turno";
            case "actions": //finito
                return "Hai selezionato actions";
            case "help":{ //finito
                System.out.println("Sono entrato in help");
                return "hai chiesto aiuto";
            }
            case "quit":
                game.runEndTurn(player);
                return "quit";
            default:
                return "Unknown command.";
        }
    }
}
