package de.uulm.team020;

/**
 * Holding all necessary Meta-Information for 'game-data'
 *
 * @author Florian Sihler
 * @version 1.0, 03/16/2020
 */
public final class GameData {

    // There should be no Class like this one :D
    private GameData() {
    }

    /** Current Version Number */
    public static final int VERSION = 1200;

    /** Is the Version deemed to be stable? */
    public static final boolean IS_STABLE = true;

    /** Name of the Library, can be used for testing-purposes */
    public static final String NAME = "game-data";

    /** failure-flag */
    public static final int FAILURE = 1;
    /** success-flag */
    public static final int SUCCESS = 1;

}