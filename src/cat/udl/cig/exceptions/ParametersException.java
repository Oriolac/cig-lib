package cat.udl.cig.exceptions;

/**
 * The current aim's exception is to inform that some parameters aren't proper to call the method or construction.
 * @author oriol
 */

public class ParametersException extends RuntimeException {

    public ParametersException() { super(); }

    public ParametersException(String message) { super(message); }
}
