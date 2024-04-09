package Exceptions;

public class CantPlaceYourCardHere extends RuntimeException {
    public CantPlaceYourCardHere() {
        super();
    }

    public CantPlaceYourCardHere(String message) {
        super(message);
    }

    public CantPlaceYourCardHere(String message, Throwable cause) {
        super(message, cause);
    }

    public CantPlaceYourCardHere(Throwable cause) {
        super(cause);
    }
}