package Exception;

public class IncorrectNameException extends Exception {
    public IncorrectNameException() { super(); }
    public IncorrectNameException(String message) { super(message); }
    public IncorrectNameException(String message, Throwable cause) { super(message, cause); }
    public IncorrectNameException(Throwable cause) { super(cause); }
}
