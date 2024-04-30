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
                return "d";

            case "drawCardFromGoldDeck":        //MOMO
                String esito;
                esito = game.drawCardFromWell(player, size);
                return esito;

            case "drawCardFromWell":      //MOMO
                String operationResult;
                operationResult= game.drawCardFromWell(player, size); //salva nella stringa il deck del giocatore
                return operationResult;

            case "endTurn":                //PIETRO -> non funziona
                game.endTurn(player);
                return "Hai selezionato endTurn";


            case "actions": //finito
                return "Hai selezionato actions";
            case "help":{ //finito
                System.out.println("Sono entrato in help");
                String help;
                help="hai chiesto aiuto";
                return help;
            }
            case "quit": return "suca";
            default:
                return "Unknown command.";
        }
    }
}
