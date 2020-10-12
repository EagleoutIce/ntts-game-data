package de.uulm.team020.parser.expander;

/**
 * Thrown by the {@link Expander} if the recursive limit is reached
 *
 * @author Florian Sihler
 * @version 1.0, 03/30/2020
 */
public class RecursiveExpansionException extends RuntimeException {

    private static final long serialVersionUID = 3901112868040209141L;

    public RecursiveExpansionException(String message) {
        super(message);
    }
}
