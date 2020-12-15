package ch.dams333.mercure.utils.exceptions;

/**
 * Error when a exception appear with voice connection
 * @author Dams333
 * @version 1.0.0
 */
public class VoiceException extends Exception {
    /**
     * Serial Version ID
     * @since 1.0.0
     */
    private static final long serialVersionUID = -2014190342375703554L;

    /**
     * Exception's definition
     * @since 1.0.0
     */
    public VoiceException(String s) {
        super(s);
    }
}
