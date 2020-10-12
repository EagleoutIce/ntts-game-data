package de.uulm.team020.networking.messages;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The HelloReply-Message, send by servers to accept the connection setup
 * initiated by a {@link HelloMessage}
 * 
 * @author Florian Sihler
 * @version 1.0, 03/24/2020
 */
public class HelloReplyMessage extends MessageContainer {

    private static final long serialVersionUID = 7630983891460330082L;

    private UUID sessionId;
    private Scenario level;
    private Matchconfig settings;
    private CharacterInformation[] characterSettings;

    /**
     * Construct a new HelloReplyMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId          the uuid of the target-client
     * @param sessionId         the uuid of the current session
     * @param level             the {@link Scenario} to be played
     * @param settings          the Settings ({@link Matchconfig}) to be used
     * @param characterSettings the Character-Settings
     *                          ({@link CharacterInformation}) to be used
     * 
     * @see #HelloReplyMessage(UUID, UUID, Scenario, Matchconfig,
     *      CharacterInformation[], String)
     */
    public HelloReplyMessage(UUID clientId, UUID sessionId, Scenario level, Matchconfig settings,
            CharacterInformation[] characterSettings) {
        this(clientId, sessionId, level, settings, characterSettings, "");
    }

    /**
     * Construct a new HelloReplyMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId          the uuid of the target-client
     * @param sessionId         the uuid of the current session
     * @param level             the {@link Scenario} to be played
     * @param settings          the Settings ({@link Matchconfig}) to be used
     * @param characterSettings the Character-Settings
     *                          ({@link CharacterInformation}) to be used
     * @param debugMessage      optional debug message
     * 
     */
    public HelloReplyMessage(UUID clientId, UUID sessionId, Scenario level, Matchconfig settings,
            CharacterInformation[] characterSettings, String debugMessage) {
        super(MessageTypeEnum.HELLO_REPLY, clientId, debugMessage);
        this.sessionId = sessionId;
        this.level = level;
        this.settings = settings;
        this.characterSettings = characterSettings;
    }

    /** @return the supplied sessionId */
    public UUID getSessionId() {
        return sessionId;
    }

    /** @return the supplied level ({@link Scenario}) */
    public Scenario getLevel() {
        return level;
    }

    /** @return the supplied Settings ({@link Matchconfig}) */
    public Matchconfig getSettings() {
        return settings;
    }

    /** @return the supplied character settings */
    public CharacterInformation[] getCharacterSettings() {
        return characterSettings;
    }

    @Override
    public String toString() {
        return "HelloReplyMessage [<container>=" + super.toString() + ", characterSettings="
                + Arrays.toString(characterSettings) + ", level=" + level + ", sessionId=" + sessionId + ", settings="
                + settings + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof HelloReplyMessage))
            return false;
        HelloReplyMessage other = (HelloReplyMessage) obj;
        return Arrays.equals(characterSettings, other.characterSettings) && Objects.equals(level, other.level)
                && Objects.equals(sessionId, other.sessionId) && Objects.equals(settings, other.settings) && super.equals(obj);
    }

}