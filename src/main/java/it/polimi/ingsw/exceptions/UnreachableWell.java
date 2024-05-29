package it.polimi.ingsw.exceptions;

public class UnreachableWell extends RuntimeException {
    public UnreachableWell(String message, Throwable cause) {
        super(message, cause);
    }
}
