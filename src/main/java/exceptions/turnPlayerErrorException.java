package exceptions;

public class turnPlayerErrorException extends RuntimeException {
    public turnPlayerErrorException(String ilGiocatoreAttualeÈSfasato) {
        super(ilGiocatoreAttualeÈSfasato);
    }
}
