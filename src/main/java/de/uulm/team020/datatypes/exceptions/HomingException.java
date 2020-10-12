package de.uulm.team020.datatypes.exceptions;

import de.uulm.team020.helper.game.HomingGuidance;

/**
 * Exception to be thrown if there is an error while using
 * {@link HomingGuidance}.
 *
 * @author Florian Sihler
 * @version 1.1, 06/19/2020
 * 
 * @since 1.2
 */
public class HomingException extends RuntimeException {

    private static final long serialVersionUID = -2834688478051177495L;

    /**
     * Build a new homing exception
     * 
     * @param message The message to pass with it
     */
    public HomingException(String message) {
        super(message);
    }

    /**
     * Build a new homing exception
     * 
     * @param cause The original reason to wrap
     */
    public HomingException(Throwable cause) {
        super(cause);
    }

}