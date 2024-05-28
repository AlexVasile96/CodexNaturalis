package it.polimi.ingsw.exceptions;

public class OperationCancelledException extends RuntimeException {
    public OperationCancelledException(String message) {
        super(message);
    }
}
