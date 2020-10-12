package de.uulm.team020.networking.messages;

import java.util.Arrays;
import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestMetaInformationMessage-Message, send by client to request further
 * Information.
 * 
 * @author Florian Sihler
 * @version 1.1, 04/09/2020
 */
public class RequestMetaInformationMessage extends MessageContainer {

    private static final long serialVersionUID = 6614353142309512721L;

    private String[] keys;

    /**
     * Construct a new RequestMetaInformationMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param keys     all requested keys
     * 
     * @see #RequestMetaInformationMessage(UUID, MetaKeyEnum...)
     * @see #RequestMetaInformationMessage(UUID, String[], String)
     */
    public RequestMetaInformationMessage(UUID clientId, String[] keys) {
        this(clientId, keys, "");
    }

    /**
     * Construct a new RequestMetaInformationMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param keys     all requested keys
     * 
     * @see #RequestMetaInformationMessage(UUID, String[])
     * @see #RequestMetaInformationMessage(UUID, String[], String)
     */
    public RequestMetaInformationMessage(UUID clientId, MetaKeyEnum... keys) {
        this(clientId, MetaKeyEnum.toStringArray(keys), "");
    }

    /**
     * Construct a new RequestMetaInformationMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param keys         all requested keys
     * @param debugMessage optional debug message
     */
    public RequestMetaInformationMessage(UUID clientId, String[] keys, String debugMessage) {
        super(MessageTypeEnum.REQUEST_META_INFORMATION, clientId, debugMessage);
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }

    @Override
    public String toString() {
        return "RequestMetaInformationMessage [<container>=" + super.toString() + ", keys=" + Arrays.toString(keys)
                + "]";
    }

}