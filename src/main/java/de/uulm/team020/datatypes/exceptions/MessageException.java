package de.uulm.team020.datatypes.exceptions;

/**
 * Will be thrown, when a conversion fails for a message
 * and it should be handled.
 *
 * @author Florian Sihler
 * @version 1.0, 04/08/2020
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = -3162832712346134938L;

    /**
     * Construct a new exception for the message conversion process.
     *
     * @param text The string to describe the exception
     */
    public MessageException(String text) {
        super(text);
    }

}
