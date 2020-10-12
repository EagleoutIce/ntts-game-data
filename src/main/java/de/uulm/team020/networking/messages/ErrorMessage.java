package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The Error-Message, send by a server
 * to close a Connection by Error
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class ErrorMessage extends MessageContainer {
    
    private static final long serialVersionUID = 7630983891460330082L;

    private ErrorTypeEnum reason;

    /**
     * Construct a new ErrorMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param reason reason, the server denied the request.
     * 
     * @see #ErrorMessage(UUID, ErrorTypeEnum, String)
     */
    public ErrorMessage(UUID clientId, ErrorTypeEnum reason) {
        this(clientId, reason, "");
    }

    /**
     * Construct a new ErrorMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param reason reason, the server denied the request.
     * @param debugMessage optional debug message
     * 
     */
    public ErrorMessage(UUID clientId, ErrorTypeEnum reason, String debugMessage) {
        super(MessageTypeEnum.ERROR, clientId, debugMessage);
        this.reason = reason;
    }

    /** @return the supplied reason */
    public ErrorTypeEnum getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ErrorMessage [<container>=" + super.toString() + ", reason=" + reason + "]";
    }
}