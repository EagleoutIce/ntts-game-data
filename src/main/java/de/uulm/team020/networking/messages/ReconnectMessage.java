package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The ReconnectMessage-Message, send by client
 * to re-initiate a previously lost connection.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/24/2020
 */
public class ReconnectMessage extends MessageContainer {
    
    private static final long serialVersionUID = 7630983891460330082L;

    private UUID sessionId;

    /**
     * Construct a new ReconnectMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the sending-client
     * @param sessionId the uuid of the current session
     * 
     * @see #ReconnectMessage(UUID, UUID, String)
     */
    public ReconnectMessage(UUID clientId, UUID sessionId) {
        this(clientId, sessionId, "");
    }

    /**
     * Construct a new ReconnectMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the sending-client
     * @param sessionId the uuid of the current session
     * @param debugMessage optional debug message
     * 
     */
    public ReconnectMessage(UUID clientId, UUID sessionId, String debugMessage) {
        super(MessageTypeEnum.RECONNECT, clientId, debugMessage);
        this.sessionId = sessionId;
    }

    /** @return the supplied sessionId */
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "ReconnectMessage [<container>=" + super.toString() + ", sessionId=" + sessionId + "]";
    }
}