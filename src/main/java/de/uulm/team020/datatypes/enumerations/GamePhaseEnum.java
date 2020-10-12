package de.uulm.team020.datatypes.enumerations;

/**
 * Enum representing all the valid pahses of a game, which <i>include</i>
 * buildup phases which may only be used or exist for the server.
 * Therefore, you can get the 'next' logical phase the Program should
 * dive in with:
 * <ul>
 *  <li>{@link #getNextClient()} for the next Client-Phase</li>
 *  <li>{@link #getNextServer()} for the next Server-Phase</li>
 * </ul>
 * Currently there is no implementation for
 * getting the previous phase, as there is no case
 * present, where this should be desireable. 
 * <p>
 * The {@link #END}-Phase will always be present for both,
 * Client and Server and will point to itself.
 * <p> 
 * There are sub-phases, like 'GamePaused' which are
 * not represented with as they are considered to be
 * states and not explicit phases.
 * 
 * @author Florian Sihler
 * @version 1.3, 04/12/2020
 */
public enum GamePhaseEnum {
    /* Layout is: NAME (IS_CLIENT, IS_SERVER) */

    /** Preparation-Phase, parse commandline args... */
    INITIALIZATION      (true,  true),
    /** Loading resources pre-game, server will load matchconfig... */
    COLLECT_RESOURCES   (true,  true),
    /** Initialize and Setup WebSocketServer/WebSocketClient */
    INIT_NETWORK        (true,  true),
    /** Players and spectators can connect to the server */
    CONNECT_TO_SERVER   (true,  false),
    /** Server will wait for players to connect */
    WAIT_FOR_PLAYERS    (false, true),
    /** Both Players have connected, wait-phase to allow spectators to join */
    WAIT_FOR_GAME_START (true,  true),
    /** The Game has started, clients may switch screen, sever switches logic */
    GAME_START          (true,  true),
    /** Server combines phases to allow both phases different for both players */
    ITEM_ASSIGNMENT     (false, true),
    /** Players can select Items ("Wahlphase ") */
    SELECT_ITEMS        (true,  false),
    /** Players can equip Items ("Ausrüstungsphase ") */
    EQUIP_ITEMS         (true,  false),
    /** The player, or in case of the server: both players have equipped, game can start */
    MAIN_GAME_READY     (true,  true),
    /** Main game-play, consist of possible pauses, strikes, kicks... */
    MAIN_GAME_PLAY      (true,  true),
    /** Game has ended for any reason, winner may be announced. */
    MAIN_GAME_END       (true,  true),
    /** End phase, all Game-Concerning stuff is over */
    END                 (true,  true),
    /* Special Phases */
    /** Special phase, whenever the game is paused. */
    GAME_PAUSED         (true, true),
    GAME_FORCE_PAUSED   (true, true);




    /** is this phase present for the client? */
    private boolean isClient;
    /** is this phase present for the server? */
    private boolean isServer;

    private static final GamePhaseEnum[] values = GamePhaseEnum.values();

    /**
     * @return the first phase of the Phase-Architecture
     */
    public static GamePhaseEnum getFirst(){
        return GamePhaseEnum.INITIALIZATION;
    }

    /**
     * @return the last phase of the Phase-Architecture
     */
    public static GamePhaseEnum getLast(){
        return GamePhaseEnum.END;
    }

    GamePhaseEnum(boolean isClient, boolean isServer) {
        this.isClient = isClient;
        this.isServer = isServer;
    }

    /**
     * @return true, if this phase does exist for the server
     */
    public boolean isServer(){
        return this.isServer;
    }

    /**
     * @return true, if this phase does exist for the client
     */
    public boolean isClient(){
        return this.isClient;
    }

    /**
     * Shouldn't be used really, please refer to
     * {@link #getNextClient()} or {@link #getNextServer()}.
     * 
     * @return The 'next' phase in definition-order.
     */
    protected GamePhaseEnum getNext(){
        return values[Math.min(this.ordinal()+1, GamePhaseEnum.END.ordinal())];   
    }

    /**
     * The next phase the server will go into.<p>
     * Please note, that {@link #END} will point to itself,
     * marking the end of phases.
     * 
     * @return the next phase
     */
    public GamePhaseEnum getNextServer(){
        GamePhaseEnum candidate = getNext();
        while(!candidate.isServer()){
            candidate = candidate.getNext();
        }
        return candidate;
    }

    /**
     * The next phase the client will go into.<p>
     * Please note, that {@link #END} will point to itself,
     * marking the end of phases.
     * 
     * @return the next phase
     */
    public GamePhaseEnum getNextClient(){
        GamePhaseEnum candidate = getNext();
        while(!candidate.isClient()){
            candidate = candidate.getNext();
        }
        return candidate;
    }

    /**
     * Checks how to phases compare to each other.
     *
     * @param marker phase to be checked against
     *
     * @return Marker according to {@link PhaseComparator}
     */
    public PhaseComparator compareWith(GamePhaseEnum marker) {
        if (this.ordinal() > getLast().ordinal() || marker.ordinal() > getLast().ordinal())
            return PhaseComparator.IS_NOT_COMPARABLE;

        if (this.ordinal() < marker.ordinal())
            return PhaseComparator.IS_BEFORE;
        else if(this.ordinal() == marker.ordinal())
            return PhaseComparator.IS_EQUAL;
        else 
            return PhaseComparator.IS_AFTER;
    }

    /**
     * @param marker phase to be checked against
     * 
     * @return True only if {@link #compareWith(GamePhaseEnum)} returns {@link PhaseComparator#IS_BEFORE}
     * 
     * @see GamePhaseEnum#compareWith(GamePhaseEnum)
     */
    public boolean isBefore(GamePhaseEnum marker) {
        return this.compareWith(marker) == PhaseComparator.IS_BEFORE;
    }


    /**
     * @param marker phase to be checked against
     *
     * @return True only if {@link #compareWith(GamePhaseEnum)} returns {@link PhaseComparator#IS_AFTER}
     *
     * @see GamePhaseEnum#compareWith(GamePhaseEnum)
     */
    public boolean isAfter(GamePhaseEnum marker) {
        return this.compareWith(marker) == PhaseComparator.IS_AFTER;
    }

    /**
     * @param marker phase to be checked against
     *
     * @return True only if {@link #compareWith(GamePhaseEnum)} returns {@link PhaseComparator#IS_AFTER} or {@link PhaseComparator#IS_EQUAL}
     *
     * @see GamePhaseEnum#compareWith(GamePhaseEnum)
     */
    public boolean isAfterOrEqual(GamePhaseEnum marker) {
        return this.compareWith(marker) == PhaseComparator.IS_AFTER || this.compareWith(marker) == PhaseComparator.IS_EQUAL ;
    }

    /**
     * Evaluates to true exactly when this game phase is between the given boundaries.
     * Both bounds are inclusive.
     * 
     * @param inclusiveLower The inclusive lower bound.
     * @param inclusiveUpper The inclusive upper bound.
     * @return True if the constant is in between the (inclusive) bounds
     */
    public boolean isInBetween(GamePhaseEnum inclusiveLower, GamePhaseEnum inclusiveUpper){
        return !this.isBefore(inclusiveLower) && !this.isAfter(inclusiveUpper);
    }
}