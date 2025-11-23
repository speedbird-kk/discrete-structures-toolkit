package exceptions;

public class NotASubsetException extends RuntimeException {
    public NotASubsetException(String msg) {
        super(msg);
    }
}
