package Exceptions;

public class InitialCardAlreadyPlacedException extends RuntimeException {
    public InitialCardAlreadyPlacedException(String s) {
        super(s);
    }
}
