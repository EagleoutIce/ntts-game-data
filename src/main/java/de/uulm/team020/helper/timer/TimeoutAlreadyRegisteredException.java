package de.uulm.team020.helper.timer;

/**
 * Executed when there is already a Timeout for this player.
 * 
 * <p>
 * Moved from game-server
 * </p>
 * 
 * @author Florian Sihler
 * @version 1.1, 21/05/2020
 * 
 * @since 1.2
 */
public class TimeoutAlreadyRegisteredException extends TimerScheduleException {

    private static final long serialVersionUID = 8696542230429745998L;

    public TimeoutAlreadyRegisteredException(String message) {
        super(message);
    }

}