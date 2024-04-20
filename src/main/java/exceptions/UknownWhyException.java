package exceptions;

public class UknownWhyException extends RuntimeException {
    public UknownWhyException(String message, Throwable cause) {
        super(message, cause);
    }
}
