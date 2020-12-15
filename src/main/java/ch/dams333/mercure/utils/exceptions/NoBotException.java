package ch.dams333.mercure.utils.exceptions;

/**
 * Error when no bot was found
 * @author Dams333
 * @version 1.0.0
 */
public class NoBotException extends Exception {
    /**
     * Serial Version ID
     * @since 1.0.0
     */
    private static final long serialVersionUID = -2014190342375703554L;

    /**
     * Exception's definition
     * @since 1.0.0
     */
    public NoBotException(String s) {
        super(s);
    }
}
