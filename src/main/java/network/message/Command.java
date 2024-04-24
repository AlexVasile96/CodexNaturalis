package network.message;

import exceptions.ParametersNotValidException;
import model.game.Game;
import model.game.WhatCanPlayerDo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Command { //Command Client sends to Server
    private Map<String, Object> parameters;



    public String runCommand(Game game, String commandString) {
        switch (commandString) {
            case "DrawACard":
                game.drawCard();
                return "Command executed: Draw a card.";
            // Altri casi per gli altri comandi supportati...
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
