package it.polimi.ingsw.exceptions;

public class CantPlaceYourCardHere extends RuntimeException {

    public CantPlaceYourCardHere(String message, Throwable cause) {
        super(message, cause);
    }

}