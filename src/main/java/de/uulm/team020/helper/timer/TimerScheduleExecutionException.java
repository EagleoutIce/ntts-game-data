package de.uulm.team020.helper.timer;

/**
 * Thrown, whenever there is an error or a generic exception happening when
 * executing the task you provided as a target.
 * 
 * @author Florian Sihler
 * @version 1.0, 21/05/2020
 * 
 * @since 1.2
 */
public class TimerScheduleExecutionException extends TimerScheduleException {
    private static final long serialVersionUID = -513572836025925576L;

    /**
     * Build a new execution exception for the timer schedule
     * 
     * @param message Message describing the problem
     */
    public TimerScheduleExecutionException(String message) {
        super(message);
    }

    /**
     * Build a new execution exception for the timer schedule
     * 
     * @param throwable The throwable that has been thrown
     */
    public TimerScheduleExecutionException(Throwable throwable) {
        super(throwable);
    }

}