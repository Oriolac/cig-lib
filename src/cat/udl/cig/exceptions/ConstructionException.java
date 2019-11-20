package cat.udl.cig.exceptions;


/**
 * The current aim's exception is to inform that the way of creating the construction isn't proper.
 * @author oriol
 */

public class ConstructionException extends ParametersException {

        public ConstructionException() { super(); }

        public ConstructionException(String message) { super(message); }

}
