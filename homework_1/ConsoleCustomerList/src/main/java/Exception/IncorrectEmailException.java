package Exception;

public class IncorrectEmailException extends Exception {
    public IncorrectEmailException() { super(); }
    public IncorrectEmailException(String message) { super(message); }
    public IncorrectEmailException(String message, Throwable cause) { super(message, cause); }
    public IncorrectEmailException(Throwable cause) { super(cause); }
}
