package network.message;

import exceptions.ParametersNotValidException;
import model.game.Game;
import model.game.Player;
import model.game.WhatCanPlayerDo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Command { //Command Client sends to Server
    private Map<String, Object> parameters;



    public String runCommand(Game game, String commandString, Player player) {
        switch (commandString) {
            case "showYourCardDeck":
                String deckprinted;
                deckprinted= game.showCards(player);
                return deckprinted;
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
            case "help":
                return "Hai chiesto aiuto";
            default:
                return "Unknown command.";

        }

    }


    /*
    * private void chooseBonusResourceType(WhatCanPlayerDo game) throws NotEnoughResourceException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.chooseBonusResourceType(resource, quantity);
    }*/
      /*Method commandMethod;
        try {
            commandMethod = Command.class.getDeclaredMethod(commandType.toString(), WhatCanPlayerDo.class);
        } catch (SecurityException | NoSuchMethodException ex) {
            return "Command not valid.";
        }
        try {
            commandMethod.invoke(this, game);
        } catch (InvocationTargetException ex) {
            Exception methodEx = (Exception) ex.getTargetException();
            return methodEx.getMessage();
        } catch (Exception ex) {
            return "Error";
        }
        return null;*/
}
