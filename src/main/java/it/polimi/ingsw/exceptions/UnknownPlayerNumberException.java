package it.polimi.ingsw.exceptions;

public class UnknownPlayerNumberException extends RuntimeException {
    public String getMessage(){
        return ("Unknown player number");
    }
}
