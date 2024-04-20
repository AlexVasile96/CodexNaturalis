package exceptions;

public class AlreadyThreeCardsException extends RuntimeException {
    public AlreadyThreeCardsException(String message, Throwable cause) {
        super(message, cause);
    }
}
