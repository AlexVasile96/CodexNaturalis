package it.polimi.ingsw.exceptions;

public class PlayerNumberAlreadySetException extends Exception {
    public String getMessage(){
        return ("Player number already set");
    }
}
