package de.uulm.team020.datatypes.exceptions;

/**
 * Will be thrown if dataset that should parse a point was not in the expected
 * format. May be caught to provide a default point, rethrow it otherwise.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/13/2020
 * 
 * @since 1.2
 */
public class PointParseException extends Exception {

    private static final long serialVersionUID = 2851470620596855215L;

    /**
     * Build a new PointParse Exception
     * 
     * @param message The message that shall be served with it.
     */
    public PointParseException(final String message) {
        super(message);
    }

}