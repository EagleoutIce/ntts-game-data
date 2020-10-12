package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestReplay-Message, send by a client to request a replay
 * 
 * @author Florian Sihler
 * @version 1.0, 05/05/2020
 */
public class RequestReplayMessage extends MessageContainer {

    private static final long serialVersionUID = -7158536250360454947L;

    /**
     * Construct a new RequestReplayMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * 
     * @see #RequestReplayMessage(UUID, String)
     */
    public RequestReplayMessage(UUID clientId) {
        this(clientId, "");
    }

    /**
     * Construct a new RequestReplayMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param debugMessage optional debug message
     */
    public RequestReplayMessage(UUID clientId, String debugMessage) {
        super(MessageTypeEnum.REQUEST_REPLAY, clientId, debugMessage);
    }

    @Override
    public String toString() {
        return "RequestReplayMessage [<container>=" + super.toString() + "]";
    }

}