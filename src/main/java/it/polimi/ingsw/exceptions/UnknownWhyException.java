package it.polimi.ingsw.exceptions;

public class UnknownWhyException extends RuntimeException {
    public UnknownWhyException(String message, Throwable cause) {
        super(message, cause);
    }
}
