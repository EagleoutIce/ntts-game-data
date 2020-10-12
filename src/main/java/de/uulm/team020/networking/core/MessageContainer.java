package de.uulm.team020.networking.core;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.helper.DateHelper;
import de.uulm.team020.validation.GameDataGson;

/**
 * Basic containerformat to capsule network messages. It will contain the
 * following data:
 * <ul>
 * <li>{@code UUID clientId}, the Id of the sending/receiving Player</li>
 * <li>{@link MessageTypeEnum} {@code type} the type of the Message (HELLO,
 * ...)</li>
 * <li>{@code Date creationDate}, creation - timestamp which can be used for
 * debugging or validation (or recaps, ...)</li>
 * <li>{@code String debugMessage}, message which can be used for sending
 * arbitrary or additional information concerning the capsuled message</li>
 * </ul>
 * 
 * @author Florian Sihler
 * @version 1.1, 03/22/2020
 */
public class MessageContainer implements IAmJson {

    private static final long serialVersionUID = 45732886383952065L;

    /**
     * Id of the target-client, will be revealed with the
     * {@link de.uulm.team020.networking.messages.HelloMessage HelloMessage}.
     */
    private UUID clientId;
    /** Type of the message. Used to parse the incoming json-data correctly */
    private MessageTypeEnum type;
    /** Date the message was created on */
    private Date creationDate;
    /** Debugging message to send additional Information to the client */
    private String debugMessage;

    /**
     * Full constructor for building a basic MessageContainer-Object
     *
     * @param type         the type
     * @param clientId     the clientId
     * @param creationDate the creation date
     * @param debugMessage the debug message
     */
    public MessageContainer(MessageTypeEnum type, UUID clientId, Date creationDate, String debugMessage) {
        this.clientId = clientId;
        this.type = type;
        this.creationDate = creationDate;
        this.debugMessage = debugMessage;
    }

    /**
     * Will construct a {@link MessageContainer} implying 'now' as the creation-date
     *
     * @param type         the type
     * @param clientId     the clientId
     * @param debugMessage the debug message
     */
    public MessageContainer(MessageTypeEnum type, UUID clientId, String debugMessage) {
        this(type, clientId, DateHelper.now(), debugMessage);
    }

    /**
     * Will construct a {@link MessageContainer} implying 'now' as the creation-date
     * and without a debugMessage
     *
     * @param type     the type
     * @param clientId the clientId
     */
    public MessageContainer(MessageTypeEnum type, UUID clientId) {
        this(type, clientId, "");
    }

    /** @return the supplied clientId */
    public UUID getClientId() {
        return clientId;
    }

    /** @return the supplied type */
    public MessageTypeEnum getType() {
        return type;
    }

    /** @return the creation date */
    public Date getCreationDate() {
        return creationDate;
    }

    /** @return the supplied debugMessage */
    public String getDebugMessage() {
        return debugMessage;
    }

    public MessageContainer setClientId(UUID clientId) {
        this.clientId = clientId;
        return this;
    }

    public MessageContainer setType(MessageTypeEnum type) {
        this.type = type;
        return this;
    }

    public MessageContainer setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public MessageContainer setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
        return this;
    }

    /**
     * Simple alias for {@link GameDataGson#getContainer(String)}
     * 
     * @param json The message you want to parse
     * @return The MessageContainer embedded in this message, or null if none
     *         found/illegal format
     */
    public static MessageContainer getContainer(String json) {
        return GameDataGson.getContainer(json);
    }

    /**
     * This will try to detect and parse the message to the desired Type, as this
     * will remove type safety this can be used if you know the task will succeed or
     * if you expect it to.
     * 
     * @param <T>  Type of the targetMessage
     * @param json The Message to parse
     * @return Descendent Message
     * 
     * @throws NullPointerException If the supplied json-string is not in valid
     *                              containerformat
     * @throws ClassCastException   If the message does not has the expected type
     */
    @SuppressWarnings("unchecked")
    public static <T extends MessageContainer> T getMessage(String json) {
        final MessageContainer container = Objects.requireNonNull(getContainer(json), "The container wasn't valid");
        return (T) GameDataGson.fromJson(json, container.getType().getTargetClass());
    }

    @Override
    public String toString() {
        return "MessageContainer [clientId=" + clientId + ", creationDate=" + creationDate + ", debugMessage="
                + debugMessage + ", type=" + type + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, debugMessage, type);
    }

    /**
     * Equals conflicting with contract as it does ignore the date comparison -- the
     * standard locks it down to seconds-precision and we do not care about it.
     * 
     * @param obj Obj to compare to
     * 
     * @return Equality-Relation with weakened contract
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MessageContainer)) {
            return false;
        }
        MessageContainer other = (MessageContainer) obj;

        return Objects.equals(clientId, other.clientId)// && Objects.equals(creationDate, other.creationDate)
                && Objects.equals(debugMessage, other.debugMessage) && type == other.type;
    }
}
