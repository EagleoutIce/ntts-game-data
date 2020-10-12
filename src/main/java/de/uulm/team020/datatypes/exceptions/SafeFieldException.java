package de.uulm.team020.datatypes.exceptions;

/**
 * Thrown wenn a Safe has an invalid Index configuration
 * 
 * @author Florian Sihler
 * @version 1.0, 04/03/2020
 */
public class SafeFieldException extends RuntimeException {

    private static final long serialVersionUID = -2053905651829318395L;

    public SafeFieldException(String message) {
        super(message); 
    }
}