package de.uulm.team020.networking.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.everit.json.schema.Schema;

import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.networking.messages.EquipmentChoiceMessage;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameLeaveMessage;
import de.uulm.team020.networking.messages.GameLeftMessage;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.ReconnectMessage;
import de.uulm.team020.networking.messages.ReplayMessage;
import de.uulm.team020.networking.messages.RequestEquipmentChoiceMessage;
import de.uulm.team020.networking.messages.RequestGameOperationMessage;
import de.uulm.team020.networking.messages.RequestGamePauseMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.networking.messages.RequestReplayMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.networking.messages.StrikeMessage;
import de.uulm.team020.validation.SchemaProvider;

/**
 * Basic identification of the network messages, including Metadata about who is
 * allowed to send this messages. All Message-Handler are to comply with this
 * policies.
 * <p>
 * 
 * @author Florian Sihler
 * @version 1.4, 04/29/2020
 */
public enum MessageTypeEnum {
    /* NAME (SERVER, CLIENTS) */
    /* NAME (SERVER, PLAYER, SPECTATOR, AI) */
    // Spielinitialisierung
    /** A client initiates the connection to the server with this type. */
    HELLO(HelloMessage.class, false, true, SchemaProvider.HELLO_MESSAGE_SCHEMA),
    /**
     * The Server answers a {@link MessageTypeEnum#HELLO ::HELLO} with this
     * message/reply.
     */
    HELLO_REPLY(HelloReplyMessage.class, true, false, SchemaProvider.HELLO_REPLY_MESSAGE_SCHEMA),
    RECONNECT(ReconnectMessage.class, false, true, false, true, SchemaProvider.RECONNECT_MESSAGE_SCHEMA),
    GAME_STARTED(GameStartedMessage.class, true, false, SchemaProvider.GAME_STARTED_MESSAGE_SCHEMA),
    // Wahlphase
    REQUEST_ITEM_CHOICE(RequestItemChoiceMessage.class, true, false, SchemaProvider.REQUEST_ITEM_CHOICE_MESSAGE_SCHEMA),
    ITEM_CHOICE(ItemChoiceMessage.class, false, true, false, true, SchemaProvider.ITEM_CHOICE_MESSAGE_SCHEMA),
    REQUEST_EQUIPMENT_CHOICE(RequestEquipmentChoiceMessage.class, true, false,
            SchemaProvider.REQUEST_EQUIPMENT_CHOICE_MESSAGE_SCHEMA),
    EQUIPMENT_CHOICE(EquipmentChoiceMessage.class, false, true, false, true,
            SchemaProvider.EQUIPMENT_CHOICE_MESSAGE_SCHEMA),
    // Spielphase
    GAME_STATUS(GameStatusMessage.class, true, false, null),
    REQUEST_GAME_OPERATION(RequestGameOperationMessage.class, true, false, null),
    GAME_OPERATION(GameOperationMessage.class, false, true, false, true, null),
    // Spielende
    STATISTICS(StatisticsMessage.class, true, false, null),
    // Kontrollnachrichten
    GAME_LEAVE(GameLeaveMessage.class, false, true, SchemaProvider.GAME_LEAVE_MESSAGE_SCHEMA),
    GAME_LEFT(GameLeftMessage.class, true, false, SchemaProvider.GAME_LEFT_MESSAGE_SCHEMA),
    REQUEST_GAME_PAUSE(RequestGamePauseMessage.class, false, true, false, false,
            SchemaProvider.REQUEST_GAME_PAUSE_MESSAGE_SCHEMA),
    GAME_PAUSE(GamePauseMessage.class, true, false, SchemaProvider.GAME_PAUSE_MESSAGE_SCHEMA),
    // REQUEST_CONFIG_DELIVERY (false, true, null),
    // CONFIG_DELIVERY (true, false, null),
    REQUEST_META_INFORMATION(RequestMetaInformationMessage.class, false, true, null),
    META_INFORMATION(MetaInformationMessage.class, true, false, null),
    STRIKE(StrikeMessage.class, true, false, SchemaProvider.STRIKE_MESSAGE_SCHEMA),
    ERROR(ErrorMessage.class, true, false, SchemaProvider.ERROR_MESSAGE_SCHEMA),
    // Optionale Komponenten
    REQUEST_REPLAY(RequestReplayMessage.class, false, true, true, false, null),
    REPLAY(ReplayMessage.class, true, false, null);

    private final Class<? extends MessageContainer> targetClass;

    private final boolean sendByServer;
    private final boolean sendByPlayer;
    private final boolean sendBySpectator;
    private final boolean sendByAi;

    private final Schema linkedSchema;

    MessageTypeEnum(Class<? extends MessageContainer> targetClass, boolean sendByServer, boolean sendByClients,
            Schema linkedSchema) {
        this(targetClass, sendByServer, sendByClients, sendByClients, sendByClients, linkedSchema);
    }

    MessageTypeEnum(Class<? extends MessageContainer> targetClass, boolean sendByServer, boolean sendByPlayer,
            boolean sendBySpectator, boolean sendByAi, Schema linkedSchema) {
        this.targetClass = targetClass;
        this.sendByServer = sendByServer;
        this.sendByPlayer = sendByPlayer;
        this.sendBySpectator = sendBySpectator;
        this.sendByAi = sendByAi;
        this.linkedSchema = linkedSchema;
    }

    public boolean isSendByServer() {
        return this.sendByServer;
    }

    public boolean isSendByPlayer() {
        return this.sendByPlayer;
    }

    public boolean isSendBySpectator() {
        return this.sendBySpectator;
    }

    public boolean isSendByAi() {
        return this.sendByAi;
    }

    public Schema getLinkedSchema() {
        return this.linkedSchema;
    }

    public Class<? extends MessageContainer> getTargetClass() {
        return this.targetClass;
    }

    /**
     * Get a random Enum-Constant which validates the constraints:
     * 
     * @param server    Should the message be sendable by a server?
     * @param player    Should the message by sendable by a Player?
     * @param spectator Should the message by sendable by a Spectator?
     * @return A random enum constant which validates all the constraints, if there
     *         is none this will be null
     */
    public static MessageTypeEnum getRandom(boolean server, boolean player, boolean spectator) {

        List<MessageTypeEnum> possibles = Arrays.stream(values()).filter(x -> (x.isSendByServer() == server
                && x.isSendByPlayer() == player && x.isSendBySpectator() == spectator)).collect(Collectors.toList());

        return RandomHelper.rndPick(possibles);
    }

    /**
     * Get a random Enum-Constant sendable by a Spectator
     * 
     * @return A random enum constant which validates all the constraints, if there
     *         is none this will be null
     */
    public static MessageTypeEnum getRandomSpectator() {

        List<MessageTypeEnum> possibles = Arrays.stream(values()).filter(MessageTypeEnum::isSendBySpectator)
                .collect(Collectors.toList());

        return RandomHelper.rndPick(possibles);
    }

    /**
     * Get a random Enum-Constant sendable by a (human) player
     * 
     * @return A random enum constant which validates all the constraints, if there
     *         is none this will be null
     */
    public static MessageTypeEnum getRandomPlayer() {

        List<MessageTypeEnum> possibles = Arrays.stream(values()).filter(MessageTypeEnum::isSendByPlayer)
                .collect(Collectors.toList());

        return RandomHelper.rndPick(possibles);
    }

    /**
     * Get a random Enum-Constant sendable by a (ai) player
     * 
     * @return A random enum constant which validates all the constraints, if there
     *         is none this will be null
     */
    public static MessageTypeEnum getRandomAi() {

        List<MessageTypeEnum> possibles = Arrays.stream(values()).filter(MessageTypeEnum::isSendByAi)
                .collect(Collectors.toList());

        return RandomHelper.rndPick(possibles);
    }
}
