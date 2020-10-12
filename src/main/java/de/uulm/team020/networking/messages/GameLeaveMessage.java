package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The GameLeave-Message, send by clients
 * to close a Connection to the Server.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/28/2020
 */
public class GameLeaveMessage extends MessageContainer {
    
    private static final long serialVersionUID = 7630983891460330082L;


    /**
     * Construct a new GameLeaveMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * 
     * @see #GameLeaveMessage(UUID, String)
     */
    public GameLeaveMessage(UUID clientId) {
        this(clientId, "");
    }

    /**
     * Construct a new GameLeaveMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param debugMessage optional debug message
     */
    public GameLeaveMessage(UUID clientId, String debugMessage) {
        super(MessageTypeEnum.GAME_LEAVE, clientId, debugMessage);
    }

    @Override
    public String toString() {
        return "GameLeaveMessage [<container>=" + super.toString() + "]";
    }

}