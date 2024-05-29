package it.polimi.ingsw.exceptions;

public class turnPlayerErrorException extends RuntimeException {
    public turnPlayerErrorException(String wrongPlayer) {
        super(wrongPlayer);
    }
}
