package Exceptions;

public class AlreadyThreeCardsException extends RuntimeException {
    public AlreadyThreeCardsException(String message, Throwable cause) {
        super(message, cause);
    }
}
