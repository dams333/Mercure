package ch.dams333.mercure.utils.exceptions;

/**
 * Error when a plugin's information miss
 * @author Dams333
 * @version 1.0.0
 */
public class NoInformationException extends Exception {
   /**
     * Serial Version ID
     * @since 1.0.0
     */
    private static final long serialVersionUID = -2014190342375703554L;

    /**
     * Exception's definition
     * @since 1.0.0
     */
    public NoInformationException(String s) {
        super(s);
    }
}
