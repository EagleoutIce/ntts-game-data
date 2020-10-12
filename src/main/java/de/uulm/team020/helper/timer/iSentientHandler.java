package de.uulm.team020.helper.timer;

/**
 * The functional interface to be used by a {@link SentientThread} if any
 * uncaught exception occurs.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/06/2020
 */
@FunctionalInterface
public interface iSentientHandler {

    /**
     * Method to be called in case of an exception.
     * 
     * @param t  The Thread the exception occurred on
     * @param ex The exception that has been thrown.
     */
    void handle(Thread t, Throwable ex);

}