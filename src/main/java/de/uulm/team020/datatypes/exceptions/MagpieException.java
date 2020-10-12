package de.uulm.team020.datatypes.exceptions;

/**
 * Thrown wenn theres a problem with {@link de.uulm.team020.logging.Magpie
 * Magpie}
 * 
 * @author Florian Sihler
 * @version 1.0, 04/03/2020
 */
public class MagpieException extends RuntimeException {

    private static final long serialVersionUID = -2053905651829318395L;

    public MagpieException(String message) {
        super(message);
    }
}