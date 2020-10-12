package de.uulm.team020.helper.timer;

/**
 * Thrown, whenever there is an error concerning {@link TimeoutSchedule}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 * 
 * @since 1.2
 */
public class TimerScheduleException extends RuntimeException {
    private static final long serialVersionUID = -513572836025925576L;

    /**
     * May be thrown for a generic timer schedule exception
     * 
     * @param message Message describing the problem
     */
    protected TimerScheduleException(String message) {
        super(message);
    }

    /**
     * May be thrown for a generic timer schedule exception
     * 
     * @param throwable The throwable that has been thrown
     */
    protected TimerScheduleException(Throwable throwable) {
        super(throwable);
    }

}