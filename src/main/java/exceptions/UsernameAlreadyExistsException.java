package exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public String getMessage(){
        return ("The selected username already exists.");
    }
}
