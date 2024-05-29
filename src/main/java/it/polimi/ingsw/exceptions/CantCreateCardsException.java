package it.polimi.ingsw.exceptions;

public class CantCreateCardsException extends RuntimeException {
    public CantCreateCardsException(String s, Exception e) {
        super(s, e);
    }
}
