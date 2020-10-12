package de.uulm.team020.helper.timer;

import de.uulm.team020.logging.Magpie;

/**
 * Helper class for {@link SentientThread} to provide useful jet generic
 * {@link iSentientHandler}. There may be more in the future.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/06/2020s
 */
public class Sentients {

    // Hide the default one
    private Sentients() {
    }

    /**
     * Returns a handler which will write the exception to the given
     * {@link Magpie}-instance
     * 
     * @param target The magpie to use
     * 
     * @return An {@link iSentientHandler} to be used for a {@link SentientThread}.
     */
    public static iSentientHandler logToMagpie(Magpie target) {
        return (t, ex) -> target.writeException(ex, t.getName());
    }

}