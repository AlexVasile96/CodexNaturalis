package network.message;
import model.game.Game;
import model.game.Player;

public class Command { //Command Client sends to Server

    public String runCommand(Game game, String commandString, Player player) {
        switch (commandString) {
            case "showYourCardDeck":{
                String deckprinted;
                deckprinted= game.showCards(player);
                return deckprinted;
            }
            case "playCardFromYourHand":
                //game.playCard();
                return "Command executed: Play Card From YOur Hand.";

            case "visualizeCommonObjectiveCards":
                return "";

            case "visualizeSecretObjectiveCard":
                return "a";

            case "showBoard":
                return "b";

            case "showPoints":
                return "c";

            case "drawCardFromDeck":
                return "d";

            case "drawCardFromWell":
                return "e";

            case "endTurn":
                return "f";

            case "actions":
                return "Hai selezionato actions";
            case "help":{
                System.out.println("Sono entrato in help");
                String help;
                help="hai chiesto aiuto";
                return help;
            }
            default:
                return "Unknown command.";
        }
    }
}
