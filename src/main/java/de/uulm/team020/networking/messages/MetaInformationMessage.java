package de.uulm.team020.networking.messages;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonSyntaxException;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.validation.GameDataGson;

/**
 * The MetaInformationMessage-Message, send by a server to send further
 * Information.
 * 
 * @author Florian Sihler
 * @version 1.2, 04/08/2020
 */
public class MetaInformationMessage extends MessageContainer {

    private static final long serialVersionUID = -2251696184792280849L;

    private final Map<String, Object> information;

    /**
     * GSON will convert Integers we've sent into Doubles. As this is an unwanted
     * behavior, this flag will execute an automated
     */
    private transient boolean cleansedDoubles;

    /**
     * Construct a new MetaInformationMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId    the uuid of the target-client
     * @param information the information by the server
     * 
     * @see #MetaInformationMessage(UUID, Map, String)
     */
    public MetaInformationMessage(final UUID clientId, final Map<String, Object> information) {
        this(clientId, information, "");
    }

    /**
     * Construct a new GameLeaveMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param information  the information by the server
     * @param debugMessage optional debug message
     */
    public MetaInformationMessage(final UUID clientId, final Map<String, Object> information,
            final String debugMessage) {
        super(MessageTypeEnum.META_INFORMATION, clientId, debugMessage);
        this.information = information;
        cleansedDoubles = false;
    }

    /**
     * Get the information-map (this will parse the data to the correct type) If the
     * type is not correct, this will go silent as it is for the client to handle
     * this correctly.
     * 
     * @return The embedded data, it will be cleansed (unwanted gson doubles =&gt;
     *         integer)
     * 
     * @throws MessageException If there is a type inside of the
     *                          {@link #information} that does not meet the
     *                          expectance
     */
    public Map<String, Object> getInformation() throws MessageException {
        if (!cleansedDoubles) {
            for (final Entry<String, Object> entry : information.entrySet()) {
                final String key = entry.getKey();

                final MetaKeyEnum keyEnum = MetaKeyEnum.getMetaKey(key);
                if (keyEnum == null || entry.getValue() == null)
                    continue; // pass unknown keys or null values
                Class<?> type = keyEnum.getExpectedType();
                if (type == Integer.class) {
                    Object bValue = entry.getValue();
                    if (bValue instanceof Double) {
                        final Double value = (Double) bValue;
                        if (value == Math.rint(value))
                            information.put(key, value.intValue());
                    }
                } else {
                    // As we do not want to make a special type adapter
                    // This should help us do the conversion to the correct type - which is quite
                    // nice :D
                    try {
                        Object value = GameDataGson.fromJson(GameDataGson.toJson(entry.getValue()), type);
                        information.put(key, value);
                    } catch (JsonSyntaxException exception) {
                        throw new MessageException("When casting for key: " + key + " having value: " + entry.getValue()
                                + " the wanted target type: " + type + " had a failure in conversion as: "
                                + exception.getMessage());
                    }
                }
            }
            cleansedDoubles = true;
        }
        return information;
    }

    /**
     * Get the information-map (this will <i>not</i> parse the data to the correct
     * type)
     * 
     * @return the embedded data, it will not be cleansed
     * 
     * @see #getInformation()
     */
    public Map<String, Object> getRawInformation() {
        return this.information;
    }

    @Override
    public String toString() {
        return "MetaInformationMessage [<container>=" + super.toString() + ", keys=" + this.information + "]";
    }

}