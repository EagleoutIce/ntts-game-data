package de.uulm.team020.datatypes.exceptions;

/**
 * Exception to be thrown if any deserialization exception occurs
 *
 * @author Florian Sihler
 * 
 * @version 1.1, 06/07/2020
 */
public class GameDataDeserializationException extends RuntimeException {

    private static final long serialVersionUID = 6895021183330039151L;

    /**
     * Build a new deserialization exception
     * 
     * @param message The message to pass with it
     */
    public GameDataDeserializationException(String message) {
        super(message);
    }

    /**
     * Build a new deserialization exception
     * 
     * @param cause The original reason to wrap
     */
    public GameDataDeserializationException(Throwable cause) {
        super(cause);
    }

}