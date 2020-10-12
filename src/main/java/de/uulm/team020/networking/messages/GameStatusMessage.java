package de.uulm.team020.networking.messages;

import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The GameStatus-Message, send by the server to signal the start or the end of
 * a game-pause.
 * 
 * @author Florian Sihler
 * 
 * @version 1.0, 04/04/2020
 * @since 1.2
 */
public class GameStatusMessage extends MessageContainer {

    private static final long serialVersionUID = 2170155161241624310L;

    private UUID activeCharacterId;
    private List<BaseOperation> operations;
    private State state;
    private Boolean isGameOver;

    /**
     * Construct a new GameStatusMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId          the uuid of the target-client
     * @param activeCharacterId the characterId of the active player, can be null if
     *                          no character is active
     * @param operations        all operations that happened since the last
     *                          GameStatus
     * @param state             the current game-state
     * @param isGameOver        flag indicating, if the game ends with this status
     *                          message
     */
    public GameStatusMessage(UUID clientId, UUID activeCharacterId, List<BaseOperation> operations, State state,
            Boolean isGameOver) {
        this(clientId, activeCharacterId, operations, state, isGameOver, "");
    }

    /**
     * Construct a new RequestItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId          the uuid of the sending-client
     * @param activeCharacterId the characterId of the active player, can be null if
     *                          no character is active
     * @param operations        all operations that happened since the last
     *                          GameStatus
     * @param state             the current game-state
     * @param isGameOver        flag indicating, if the game ends with this status
     *                          message
     * @param debugMessage      optional debug message
     */
    public GameStatusMessage(UUID clientId, UUID activeCharacterId, List<BaseOperation> operations, State state,
            Boolean isGameOver, String debugMessage) {
        super(MessageTypeEnum.GAME_STATUS, clientId, debugMessage);
        this.activeCharacterId = activeCharacterId;
        this.operations = operations;
        this.state = state;
        this.isGameOver = isGameOver;
    }

    public UUID getActiveCharacterId() {
        return activeCharacterId;
    }

    public List<BaseOperation> getOperations() {
        return operations;
    }

    public State getState() {
        return state;
    }

    public Boolean getIsGameOver() {
        return isGameOver;
    }

    @Override
    public String toString() {
        return "GameStatusMessage [<container>=" + super.toString() + ", activeCharacterId=" + activeCharacterId
                + ", isGameOver=" + isGameOver + ", operations=" + operations + ", state=" + state + "]";
    }

}