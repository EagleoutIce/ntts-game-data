package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The GameOperation-Message, send by server to request character-operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/23/2020
 */
public class GameOperationMessage extends MessageContainer {

    private static final long serialVersionUID = 338210923757945696L;

    private Operation operation;

    /**
     * Construct a new GameOperation which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId  The uuid of the target-client
     * @param operation The operation performed
     * 
     * @see #GameOperationMessage(UUID, Operation, String)
     */
    public GameOperationMessage(UUID clientId, Operation operation) {
        this(clientId, operation, "");
    }

    /**
     * Construct a new GameOperation which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param operation    The operation performed
     * 
     * @param debugMessage optional debug message
     */
    public GameOperationMessage(UUID clientId, Operation operation, String debugMessage) {
        super(MessageTypeEnum.GAME_OPERATION, clientId, debugMessage);
        this.operation = operation;
    }

    public Operation getOperation() {
        return this.operation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GameOperationMessage [<container>=").append(super.toString()).append(", operation=")
                .append(operation).append("]");
        return builder.toString();
    }

}