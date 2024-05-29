package it.polimi.ingsw.exceptions;

public class GameFullException extends RuntimeException{
    public String getMessage(){
        return ("Game full");
    }
}
