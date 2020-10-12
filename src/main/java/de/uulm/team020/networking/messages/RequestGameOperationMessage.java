package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestGameOperation-Message, send by server to request
 * character-operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/23/2020
 */
public class RequestGameOperationMessage extends MessageContainer {

    private static final long serialVersionUID = 7630983891460330082L;

    private UUID characterId;

    /**
     * Construct a new RequestGameOperation which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId    The uuid of the target-client
     * @param characterId The uuid of the character whose turn it is
     * 
     * @see #RequestGameOperationMessage(UUID, UUID, String)
     */
    public RequestGameOperationMessage(UUID clientId, UUID characterId) {
        this(clientId, characterId, "");
    }

    /**
     * Construct a new RequestGameOperation which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param characterId  The uuid of the character whose turn it is
     * 
     * @param debugMessage optional debug message
     */
    public RequestGameOperationMessage(UUID clientId, UUID characterId, String debugMessage) {
        super(MessageTypeEnum.REQUEST_GAME_OPERATION, clientId, debugMessage);
        this.characterId = characterId;
    }

    public UUID getCharacterId() {
        return this.characterId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RequestGameOperationMessage [<container>=").append(super.toString()).append(",characterId=")
                .append(characterId).append("]");
        return builder.toString();
    }

}