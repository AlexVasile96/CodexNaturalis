package network.message;

import Exceptions.ParametersNotValidException;
import model.WhatCanPlayerDo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Command { //Command Client sends to Server
    private Map<String, Object> parameters;
    private CommandsTaipEnum commandType;
    public Command(CommandsTaipEnum commandType, Map<String, Object> parameters) throws ParametersNotValidException {
        if (commandType == null)
            throw new ParametersNotValidException();
        this.commandType = commandType;
        this.parameters = Objects.requireNonNullElseGet(parameters, HashMap::new);
    }


    public String runCommand(WhatCanPlayerDo game) {
        Method commandMethod;
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
        return null;
    }


    /*
    * private void chooseBonusResourceType(UserCommandsInterface game) throws NotEnoughResourceException, WrongTurnPhaseException {
        Resource resource = extractResource(parameters.get("resource"));
        int quantity = extractInt(parameters.get("quantity"));
        game.chooseBonusResourceType(resource, quantity);
    }*/
}
