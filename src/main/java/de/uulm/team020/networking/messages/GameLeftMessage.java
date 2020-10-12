package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The GameLeftMessage-Message, send by servers
 * to propagate the close of the connection...
 * 
 * @author Florian Sihler
 * @version 1.0, 03/28/2020
 */
public class GameLeftMessage extends MessageContainer {
    
    private static final long serialVersionUID = 7630983891460330082L;

    private UUID leftUserId;

    /**
     * Construct a new GameLeftMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param leftUserId the user, that left
     * 
     * @see #GameLeftMessage(UUID, UUID, String)
     */
    public GameLeftMessage(UUID clientId, UUID leftUserId) {
        this(clientId, leftUserId, "");
    }

    /**
     * Construct a new GameLeftMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param leftUserId the user, that left
     * @param debugMessage optional debug message
     */
    public GameLeftMessage(UUID clientId, UUID leftUserId, String debugMessage) {
        super(MessageTypeEnum.GAME_LEFT, clientId, debugMessage);
        this.leftUserId = leftUserId;
    }

    public UUID getLeftUserId() {
        return this.leftUserId;
    }

    @Override
    public String toString() {
        return "GameLeftMessage [<container>=" + super.toString() + ", leftUserId=" + leftUserId + "]";
    }
}