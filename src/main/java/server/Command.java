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
                    String CornersAvaiable= game.showAvaiableCorners(player, size, paolo);
                    checkIfTheBoardHadAlreadyBeenPrinted++;
                    return CornersAvaiable;
                }
                else if(checkIfTheBoardHadAlreadyBeenPrinted==1)
                {
                    game.playCard(player,size, paolo, cornerChosen);
                    String finalResult="Carta placed";
                    checkIfTheBoardHadAlreadyBeenPrinted--;
                    return finalResult;

                }
            }
          case "visualizeCommonObjectiveCards":   //MOMO ->finito-> funzionante
                String commonCards;
                commonCards= game.visualizeCommonObjective(player);
                return commonCards;

            case "secret":                          //PIETRO ->finito-> funzionante
                String secretObjectiveCard;
                secretObjectiveCard= game.visualizeSecretObjective(player); //salva nella stringa la carta obiettivo segreta del giocatore
                return secretObjectiveCard;

            case "showBoard":                //MOMO-> finito-> funzionante
                String yourBoard;
                yourBoard= game.showBoard(player);
                return yourBoard;

            case "showPoints":                 //PIETRO -> finito-> funzionante
                String playerPoints;
                playerPoints= game.showPoints(player);
                return playerPoints;

            case "showWell":        //MOMO
                String wellPrinted;
                wellPrinted= game.showWell(); //salva nella stringa il deck del giocatore
                return wellPrinted;

            case "drawCardFromResourceDeck":        //MOMO
                String esito;
                esito = game.drawResourceCard(player);
                return esito;

            case "drawCardFromGoldDeck":        //MOMO
                String ciao;
                ciao = game.drawGoldCard(player);
                return ciao;

            case "drawCardFromWell":      //MOMO
                String operationResult;
                operationResult= game.drawCardFromWell(player, size); //salva nella stringa il deck del giocatore
                return operationResult;
            case "showEachPlayerBoard":
                String allboards;
                allboards= game.showAllPlayersBoard();
            case "showYourSpecificSeed":
                String allStrings;
                allStrings= game.showYourspecificSeeds();

            case "endTurn":                //PIETRO
                return "fine turno";
            case "actions": //finito
                return "Hai selezionato actions";
            case "help":{ //finito
                System.out.println("Sono entrato in help");
                return "hai chiesto aiuto";
            }
            case "quit": return "suca";
            default:
                return "Unknown command.";
        }
    }
}
