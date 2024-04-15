package Exceptions;

public class AlreadyThreeCardsExeption extends RuntimeException {
    public AlreadyThreeCardsExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
