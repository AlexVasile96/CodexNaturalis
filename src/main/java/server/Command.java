package server;
import model.game.Game;
import model.game.Player;

public class Command { //Command Client sends to Server

    public String runCommand(Game game, String commandString, Player player) { //chiama sempre il game
        switch (commandString) {
            case "showYourCardDeck":{ //finito-> funzionante
                String deckprinted;
                deckprinted= game.showCards(player); //salva nella stringa il deck del giocatore
                return deckprinted;
            }
            case "playCardFromYourHand": //PIETRO
                //game.playCard();
                return "Command executed: Play Card From Your Hand.";

            case "common":   //MOMO
                return "";

            case "secret":                          //PIETRO ->finito-> funzionante
                String secretObjectiveCard;
                secretObjectiveCard= game.visualizeSecretObjective(player); //salva nella stringa la carta obiettivo segreta del giocatore
                return secretObjectiveCard;

            case "showBoard":                //MOMO
                String yourBoard;
                yourBoard= game.showBoard(player);
                return yourBoard;

            case "showPoints":                 //PIETRO
                String playerPoints;
                playerPoints= game.showPoints(player);
                return playerPoints;

            case "drawResourceCardFromDeck":        //MOMO
                return "d";

            case "drawGoldCardFromWell":      //MOMO
                return "e";

            case "endTurn":                //PIETRO
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
