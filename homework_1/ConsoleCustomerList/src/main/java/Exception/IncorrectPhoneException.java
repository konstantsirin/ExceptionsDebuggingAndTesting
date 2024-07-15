package Exception;

public class IncorrectPhoneException extends Exception {
    public IncorrectPhoneException() { super(); }
    public IncorrectPhoneException(String message) { super(message); }
    public IncorrectPhoneException(String message, Throwable cause) { super(message, cause); }
    public IncorrectPhoneException(Throwable cause) { super(cause); }
}
