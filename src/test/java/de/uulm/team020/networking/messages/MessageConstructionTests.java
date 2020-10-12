package de.uulm.team020.networking.messages;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.validation.GameDataGson;

// Tests construction and rebuilding of all messages
@TestMethodOrder(OrderAnnotation.class)
public class MessageConstructionTests {
    // maybe they should use buffers?
    private static final Scenario SCENARIO;
    static {
        try {
            SCENARIO = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/scenario/testminimums.scenario"),
                    Scenario.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage()); // end all tests
        }
    }

    private static final Matchconfig MATCHCONFIG;
    static {
        try {
            MATCHCONFIG = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/matchconfig/valid.match"),
                    Matchconfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage()); // end all tests
        }
    }

    private static final CharacterInformation[] CHARACTERS;
    static {
        try {
            CHARACTERS = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/characters/valid.json"),
                    CharacterInformation[].class);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage()); // end all tests
        }
    }

    private static final UUID PLAYER = UUID.randomUUID();
    private static final UUID PLAYER2 = UUID.randomUUID();
    private static final UUID SESSION = UUID.randomUUID();

    // Generic helper
    private void assertDataAsWantedGeneric(MessageContainer message) {
        String data = message.toJson();
        Object messageObj = MessageContainer.getMessage(data);
        Assertions.assertEquals(message, messageObj, "Message should be as wanted for data: " + data);
        Assertions.assertEquals(message.hashCode(), messageObj.hashCode(),
                "Have to obey hash code contract for: " + message + " and: " + messageObj);
    }

    // HELLO

    @Test
    @Order(1)
    @DisplayName("[Message] HelloMessage")
    public void test_helloMessage() {
        HelloMessage message = new HelloMessage("walter", RoleEnum.AI);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals("walter", message.getName());
        Assertions.assertEquals(RoleEnum.AI, message.getRole());
    }

    @Test
    @Order(1)
    @DisplayName("[Message] HelloMessage 2")
    public void test_helloMessage2() {
        HelloMessage message = new HelloMessage("walter2", RoleEnum.SPECTATOR, "I am a hello Message");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals("walter2", message.getName());
        Assertions.assertEquals(RoleEnum.SPECTATOR, message.getRole());
        Assertions.assertEquals("I am a hello Message", message.getDebugMessage());
    }

    // HELLO_REPLY

    @Test
    @Order(2)
    @DisplayName("[Message] HelloReplyMessage")
    public void test_helloReplyMessage() throws IOException {
        HelloReplyMessage message = new HelloReplyMessage(PLAYER, SESSION, SCENARIO, MATCHCONFIG, CHARACTERS);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(SCENARIO, message.getLevel());
        Assertions.assertEquals(MATCHCONFIG, message.getSettings());
        Assertions.assertArrayEquals(CHARACTERS, message.getCharacterSettings());
    }

    @Test
    @Order(2)
    @DisplayName("[Message] HelloReplyMessage 2")
    public void test_helloReplyMessage2() throws IOException {
        HelloReplyMessage message = new HelloReplyMessage(PLAYER, SESSION, SCENARIO, MATCHCONFIG, CHARACTERS, "Wuff");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(SCENARIO, message.getLevel());
        Assertions.assertEquals(MATCHCONFIG, message.getSettings());
        Assertions.assertArrayEquals(CHARACTERS, message.getCharacterSettings());
        Assertions.assertEquals("Wuff", message.getDebugMessage());
    }

    // RECONNECT_MESSAGE
    @Test
    @Order(3)
    @DisplayName("[Message] ReconnectMessage")
    public void test_reconnectMessage() throws IOException {
        ReconnectMessage message = new ReconnectMessage(PLAYER, SESSION);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
    }

    @Test
    @Order(3)
    @DisplayName("[Message] ReconnectMessage2")
    public void test_reconnectMessage2() throws IOException {
        ReconnectMessage message = new ReconnectMessage(PLAYER, SESSION, "Miau");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals("Miau", message.getDebugMessage());
    }

    // GAME_STARTED

    @Test
    @Order(4)
    @DisplayName("[Message] GameStartedMessage")
    public void test_gameStartedMessage() throws IOException {
        GameStartedMessage message = new GameStartedMessage(PLAYER, PLAYER, PLAYER2, "Hugo", "Egbert", SESSION);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(PLAYER, message.getPlayerOneId());
        Assertions.assertEquals(PLAYER2, message.getPlayerTwoId());
        Assertions.assertEquals("Hugo", message.getPlayerOneName());
        Assertions.assertEquals("Egbert", message.getPlayerTwoName());
    }

    @Test
    @Order(4)
    @DisplayName("[Message] GameStartedMessage 2")
    public void test_gameStartedMessage2() throws IOException {
        GameStartedMessage message = new GameStartedMessage(PLAYER, PLAYER, PLAYER2, "Hugo", "Egbert", SESSION,
                "Haffel daffel");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(PLAYER, message.getPlayerOneId());
        Assertions.assertEquals(PLAYER2, message.getPlayerTwoId());
        Assertions.assertEquals("Hugo", message.getPlayerOneName());
        Assertions.assertEquals("Egbert", message.getPlayerTwoName());
        Assertions.assertEquals("Haffel daffel", message.getDebugMessage());
    }

    @Test
    @Order(4)
    @DisplayName("[Message] GameStartedMessage 3")
    public void test_gameStartedMessage3() throws IOException {
        GameStartedMessage message = new GameStartedMessage(PLAYER, PLAYER, PLAYER2, "Hugo", "Egbert", SESSION);
        message.exchangeClientId(PLAYER2);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER2, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(PLAYER, message.getPlayerOneId());
        Assertions.assertEquals(PLAYER2, message.getPlayerTwoId());
        Assertions.assertEquals("Hugo", message.getPlayerOneName());
        Assertions.assertEquals("Egbert", message.getPlayerTwoName());
    }

    // REQUEST_ITEM_CHOICE
    @Test
    @Order(5)
    @DisplayName("[Message] RequestItemChoiceMessage")
    public void test_requestItemChoiceMessage() throws IOException {
        // chars:
        List<UUID> chars = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<GadgetEnum> gadgets = List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.COCKTAIL);
        RequestItemChoiceMessage message = new RequestItemChoiceMessage(PLAYER, chars, gadgets);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(chars, message.getOfferedCharacterIds());
        Assertions.assertEquals(gadgets, message.getOfferedGadgets());
    }

    @Test
    @Order(5)
    @DisplayName("[Message] RequestItemChoiceMessage2")
    public void test_requestItemChoiceMessage2() throws IOException {
        // chars:
        List<UUID> chars = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<GadgetEnum> gadgets = List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.COCKTAIL);
        RequestItemChoiceMessage message = new RequestItemChoiceMessage(PLAYER, chars, gadgets, "Juchei");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(chars, message.getOfferedCharacterIds());
        Assertions.assertEquals(gadgets, message.getOfferedGadgets());
        Assertions.assertEquals("Juchei", message.getDebugMessage());
    }

    // ITEM_CHOICE
    @Test
    @Order(6)
    @DisplayName("[Message] ItemChoiceMessage")
    public void test_itemChoiceMessage() throws IOException {
        UUID chosenChar = UUID.randomUUID();
        ItemChoiceMessage message = new ItemChoiceMessage(PLAYER, chosenChar, null);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(chosenChar, message.getChosenCharacterId());
        Assertions.assertNull(message.getChosenGadget());
        Assertions.assertTrue(message.isValid());
    }

    @Test
    @Order(6)
    @DisplayName("[Message] ItemChoiceMessage 2")
    public void test_itemChoiceMessage2() throws IOException {
        ItemChoiceMessage message = new ItemChoiceMessage(PLAYER, null, GadgetEnum.GAS_GLOSS);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertNull(message.getChosenCharacterId());
        Assertions.assertEquals(GadgetEnum.GAS_GLOSS, message.getChosenGadget());
        Assertions.assertTrue(message.isValid());
    }

    @Test
    @Order(6)
    @DisplayName("[Message] ItemChoiceMessage 3")
    public void test_itemChoiceMessage3() throws IOException {
        UUID chosenChar = UUID.randomUUID();
        ItemChoiceMessage message = new ItemChoiceMessage(PLAYER, chosenChar, GadgetEnum.GAS_GLOSS);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(chosenChar, message.getChosenCharacterId());
        Assertions.assertEquals(GadgetEnum.GAS_GLOSS, message.getChosenGadget());
        Assertions.assertFalse(message.isValid());
    }

    @Test
    @Order(6)
    @DisplayName("[Message] ItemChoiceMessage 4")
    public void test_itemChoiceMessage4() throws IOException {
        ItemChoiceMessage message = new ItemChoiceMessage(PLAYER, null, GadgetEnum.GAS_GLOSS, "Prodegdion");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertNull(message.getChosenCharacterId());
        Assertions.assertEquals(GadgetEnum.GAS_GLOSS, message.getChosenGadget());
        Assertions.assertTrue(message.isValid());
        Assertions.assertEquals("Prodegdion", message.getDebugMessage());
    }

    @Test
    @Order(6)
    @DisplayName("[Message] ItemChoiceMessage 5")
    public void test_itemChoiceMessage5() throws IOException {
        ItemChoiceMessage message = new ItemChoiceMessage(null, null, GadgetEnum.GAS_GLOSS);
        assertDataAsWantedGeneric(message);
        Assertions.assertNull(message.getClientId());
        Assertions.assertNull(message.getChosenCharacterId());
        Assertions.assertEquals(GadgetEnum.GAS_GLOSS, message.getChosenGadget());
        Assertions.assertFalse(message.isValid());
    }

    // REPLAY

    @Test
    @Order(7)
    @DisplayName("[Message] ReplayMessage")
    public void test_replayMessage() throws IOException {
        Date start = new Date();
        Date end = new Date();
        ReplayMessage message = new ReplayMessage(PLAYER, SESSION, start, end, PLAYER, PLAYER2, "Justaf", "grelbman",
                42, SCENARIO, MATCHCONFIG, CHARACTERS, new String[0]);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(PLAYER, message.getPlayerOneId());
        Assertions.assertEquals(PLAYER2, message.getPlayerTwoId());
        Assertions.assertEquals("Justaf", message.getPlayerOneName());
        Assertions.assertEquals("grelbman", message.getPlayerTwoName());
        Assertions.assertEquals(42, message.getRounds());
        Assertions.assertEquals(SCENARIO, message.getLevel());
        Assertions.assertEquals(MATCHCONFIG, message.getSettings());
        Assertions.assertArrayEquals(CHARACTERS, message.getCharacterSettings());
        Assertions.assertArrayEquals(new String[0], message.getMessages());
    }

    @Test
    @Order(7)
    @DisplayName("[Message] ReplayMessage2")
    public void test_replayMessage2() throws IOException {
        Date start = new Date();
        Date end = new Date();
        ReplayMessage message = new ReplayMessage(PLAYER, SESSION, start, end, PLAYER, PLAYER2, "Justaf", "grelbman",
                42, SCENARIO, MATCHCONFIG, CHARACTERS, new String[0], "Tschukadu");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(SESSION, message.getSessionId());
        Assertions.assertEquals(PLAYER, message.getPlayerOneId());
        Assertions.assertEquals(PLAYER2, message.getPlayerTwoId());
        Assertions.assertEquals("Justaf", message.getPlayerOneName());
        Assertions.assertEquals("grelbman", message.getPlayerTwoName());
        Assertions.assertEquals(42, message.getRounds());
        Assertions.assertEquals(SCENARIO, message.getLevel());
        Assertions.assertEquals(MATCHCONFIG, message.getSettings());
        Assertions.assertArrayEquals(CHARACTERS, message.getCharacterSettings());
        Assertions.assertArrayEquals(new String[0], message.getMessages());
        Assertions.assertEquals("Tschukadu", message.getDebugMessage());
    }

    // META_INFORMATION

    @Test
    @Order(8)
    @DisplayName("[Message] MetaInformation")
    public void test_metaInformationMessage() throws IOException, MessageException {
        Map<String, Object> information = new HashMap<>(12);
        // The server has to test the semantics...
        for (MetaKeyEnum type : MetaKeyEnum.values()) {
            information.put(type.getKey(), null);
        }
        MetaInformationMessage message = new MetaInformationMessage(PLAYER, information);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertFalse(message.getInformation().isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("[Message] MetaInformation2")
    public void test_metaInformationMessage2() throws IOException, MessageException {
        Map<String, Object> information = new HashMap<>(12);
        // The server has to test the semantics...
        for (MetaKeyEnum type : MetaKeyEnum.values()) {
            information.put(type.getKey(), null);
        }
        MetaInformationMessage message = new MetaInformationMessage(PLAYER, information, "rebbaq");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertFalse(message.getInformation().isEmpty());
        Assertions.assertEquals("rebbaq", message.getDebugMessage());
    }

    @Test
    @Order(8)
    @DisplayName("[Message] MetaInformation3")
    public void test_metaInformationMessage3() throws IOException, MessageException {
        Map<String, Object> information = new HashMap<>(6);
        information.put(MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION.getKey(), CHARACTERS);
        information.put(MetaKeyEnum.CONFIGURATION_MATCHCONFIG.getKey(), MATCHCONFIG);
        information.put(MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(), SCENARIO);
        information.put(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), 42);
        information.put(MetaKeyEnum.SPECTATOR_COUNT.getKey(), 3);
        information.put(MetaKeyEnum.PLAYER_COUNT.getKey(), 1);
        information.put(MetaKeyEnum.SPECTATOR_NAMES.getKey(), new String[] { "Jens", "Müller", "Dieter" });
        information.put(MetaKeyEnum.PLAYER_NAMES.getKey(), new String[] { "Paul" });
        information.put(MetaKeyEnum.FACTION_PLAYER2.getKey(), new UUID[] { PLAYER2, SESSION });
        information.put(MetaKeyEnum.GADGETS_PLAYER1.getKey(),
                new GadgetEnum[] { GadgetEnum.NUGGET, GadgetEnum.ANTI_PLAGUE_MASK });
        information.put(MetaKeyEnum.AUTHOR_DUMP.getKey(), "I am Dump");
        information.put("Random unknown crap", new RuntimeException("Data"));

        MetaInformationMessage message = new MetaInformationMessage(PLAYER, information);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(information, message.getInformation());
    }

    private static Stream<Arguments> generate_metaInformation() {
        return Stream.of(//
                Arguments.arguments(MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION.getKey(), MATCHCONFIG),
                Arguments.arguments(MetaKeyEnum.CONFIGURATION_MATCHCONFIG.getKey(), SCENARIO),
                Arguments.arguments(MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(), CHARACTERS),
                Arguments.arguments(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), -42.3),
                Arguments.arguments(MetaKeyEnum.SPECTATOR_COUNT.getKey(), CHARACTERS),
                Arguments.arguments(MetaKeyEnum.PLAYER_COUNT.getKey(), 5.7),
                Arguments.arguments(MetaKeyEnum.SPECTATOR_NAMES.getKey(), new UUID[] { PLAYER2, SESSION }),
                Arguments.arguments(MetaKeyEnum.PLAYER_NAMES.getKey(), new Integer[] { 3, 4, -3 }),
                Arguments.arguments(MetaKeyEnum.FACTION_PLAYER2.getKey(), new UUID[] { PLAYER2, SESSION }),
                Arguments.arguments(MetaKeyEnum.GADGETS_PLAYER1.getKey(), new String[] { "Jens", "Müller", "Dieter" }),
                Arguments.arguments(MetaKeyEnum.AUTHOR_DUMP.getKey(), 42));
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("[Message] MetaInformation4 (failures)")
    @MethodSource("generate_metaInformation")
    public void test_metaInformationMessage4(String key, Object value) throws IOException, MessageException {
        Map<String, Object> information = new HashMap<>(1);
        information.put(key, value);

        MetaInformationMessage message = new MetaInformationMessage(PLAYER, information);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        try {
            Assertions.assertEquals(information, message.getInformation());
        } catch (MessageException ex) {
            // already desired :D
        }
    }

    // REQUEST_EQUIPMENT_CHOICE

    @Test
    @Order(9)
    @DisplayName("[Message] RequestEquipmentChoice")
    public void test_requestEquipmentMessage() throws IOException, MessageException {

        List<UUID> characters = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<GadgetEnum> gadgets = List.of(GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.DIAMOND_COLLAR, GadgetEnum.JETPACK);

        RequestEquipmentChoiceMessage message = new RequestEquipmentChoiceMessage(PLAYER, characters, gadgets);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(message.getChosenCharacterIds(), characters, "Chosen Characters");
        Assertions.assertEquals(message.getChosenGadgets(), gadgets, "Chosen Gadgets");
    }

    @Test
    @Order(9)
    @DisplayName("[Message] RequestEquipmentChoice2")
    public void test_requestEquipmentMessage2() throws IOException, MessageException {
        List<UUID> characters = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<GadgetEnum> gadgets = List.of(GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.DIAMOND_COLLAR, GadgetEnum.JETPACK);

        RequestEquipmentChoiceMessage message = new RequestEquipmentChoiceMessage(PLAYER, characters, gadgets,
                "Pettaaa");
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(message.getChosenCharacterIds(), characters, "Chosen Characters");
        Assertions.assertEquals(message.getChosenGadgets(), gadgets, "Chosen Gadgets");
        Assertions.assertEquals("Pettaaa", message.getDebugMessage());
    }

    // REQUEST_EQUIPMENT_CHOICE

    @Test
    @Order(10)
    @DisplayName("[Message] EquipmentChoice")
    public void test_equipmentMessage() throws IOException, MessageException {
        Map<UUID, List<GadgetEnum>> equipment = new HashMap<>();
        equipment.put(UUID.randomUUID(), List.of(GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.DIAMOND_COLLAR));
        equipment.put(UUID.randomUUID(), List.of(GadgetEnum.JETPACK, GadgetEnum.COCKTAIL, GadgetEnum.LASER_COMPACT));

        EquipmentChoiceMessage message = new EquipmentChoiceMessage(PLAYER, equipment);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(equipment, message.getEquipment(), "Equipment");
    }

    @Test
    @Order(10)
    @DisplayName("[Message] EquipmentChoice2")
    public void test_equipmentMessage2() throws IOException, MessageException {
        Map<UUID, List<GadgetEnum>> equipment = new HashMap<>();
        equipment.put(UUID.randomUUID(), List.of(GadgetEnum.ANTI_PLAGUE_MASK, GadgetEnum.DIAMOND_COLLAR));
        equipment.put(UUID.randomUUID(), List.of(GadgetEnum.JETPACK, GadgetEnum.COCKTAIL, GadgetEnum.LASER_COMPACT));

        EquipmentChoiceMessage message = new EquipmentChoiceMessage(PLAYER, equipment, "Paulsson");
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(equipment, message.getEquipment(), "Equipment");
        Assertions.assertEquals("Paulsson", message.getDebugMessage());
    }

    // REQUEST_REPLAY_CHOICE

    @Test
    @Order(11)
    @DisplayName("[Message] RequestReplay")
    public void test_requestReplayMessage() throws IOException {
        RequestReplayMessage message = new RequestReplayMessage(PLAYER);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
    }

    @Test
    @Order(11)
    @DisplayName("[Message] ReconnectMessage2")
    public void test_requestReplayMessage2() throws IOException {
        RequestReplayMessage message = new RequestReplayMessage(PLAYER, "Jawaffel");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals("Jawaffel", message.getDebugMessage());
    }

    // GAME_LEAVE

    @Test
    @Order(12)
    @DisplayName("[Message] GameLeave")
    public void test_gameLeaveMessage() throws IOException {
        GameLeaveMessage message = new GameLeaveMessage(PLAYER);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
    }

    @Test
    @Order(12)
    @DisplayName("[Message] GameLeave2")
    public void test_gameLeaveMessage2() throws IOException {
        GameLeaveMessage message = new GameLeaveMessage(PLAYER, "dra pa ja kla");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals("dra pa ja kla", message.getDebugMessage());
    }

    // REQUEST_GAME_PAUSE

    @Test
    @Order(13)
    @DisplayName("[Message] RequestGamePause")
    public void test_requestGamePauseMessage() throws IOException {
        boolean rndBool = RandomHelper.flip();
        RequestGamePauseMessage message = new RequestGamePauseMessage(PLAYER, rndBool);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(rndBool, message.getGamePause(), "Should be as set: " + rndBool);
    }

    @Test
    @Order(13)
    @DisplayName("[Message] RequestGamePause2")
    public void test_requestGamePauseMessage2() throws IOException {
        boolean rndBool = RandomHelper.flip();
        RequestGamePauseMessage message = new RequestGamePauseMessage(PLAYER, rndBool, "Chabuff");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(rndBool, message.getGamePause(), "Should be as set: " + rndBool);
        Assertions.assertEquals("Chabuff", message.getDebugMessage());
    }

    // GAME_LEFT

    @Test
    @Order(14)
    @DisplayName("[Message] GameLeft")
    public void test_gameLeftMessage() throws IOException {
        GameLeftMessage message = new GameLeftMessage(PLAYER, PLAYER2);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(PLAYER2, message.getLeftUserId(), "Player set");
    }

    @Test
    @Order(14)
    @DisplayName("[Message] GameLeft2")
    public void test_gameLeftMessage2() throws IOException {
        GameLeftMessage message = new GameLeftMessage(PLAYER, PLAYER2, "ula-hueeb");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(PLAYER2, message.getLeftUserId(), "Player set");
        Assertions.assertEquals("ula-hueeb", message.getDebugMessage());
    }

    // ERROR

    @Test
    @Order(15)
    @DisplayName("[Message] Error")
    public void test_errorMessage() throws IOException {
        ErrorTypeEnum error = RandomHelper.rndPick(ErrorTypeEnum.values());
        ErrorMessage message = new ErrorMessage(PLAYER, error);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(error, message.getReason(), "Reason");
    }

    @Test
    @Order(15)
    @DisplayName("[Message] Error2")
    public void test_errorMessage2() throws IOException {
        ErrorTypeEnum error = RandomHelper.rndPick(ErrorTypeEnum.values());
        ErrorMessage message = new ErrorMessage(PLAYER, error, "Dis is serious");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(error, message.getReason(), "Reason");
        Assertions.assertEquals("Dis is serious", message.getDebugMessage());
    }

    // GAME_PAUSE

    @Test
    @Order(16)
    @DisplayName("[Message] GamePause")
    public void test_gamePauseMessage() throws IOException {
        boolean rndBool = RandomHelper.flip(); // genPause
        boolean rndBool2 = RandomHelper.flip(); // enforced
        GamePauseMessage message = new GamePauseMessage(PLAYER, rndBool, rndBool2);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(rndBool, message.getGamePaused(), "Pause should be as set: " + rndBool);
        Assertions.assertEquals(rndBool2, message.getServerEnforced(), "Enforced should be as set: " + rndBool2);
    }

    @Test
    @Order(16)
    @DisplayName("[Message] GamePause2")
    public void test_gamePauseMessage2() throws IOException {
        boolean rndBool = RandomHelper.flip(); // genPause
        boolean rndBool2 = RandomHelper.flip(); // enforced
        GamePauseMessage message = new GamePauseMessage(PLAYER, rndBool, rndBool2, "Is this Boston?");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(rndBool, message.getGamePaused(), "Pause should be as set: " + rndBool);
        Assertions.assertEquals(rndBool2, message.getServerEnforced(), "Enforced should be as set: " + rndBool2);
        Assertions.assertEquals("Is this Boston?", message.getDebugMessage());
    }

    // REQUEST_META_INFORMATION

    @Test
    @Order(17)
    @DisplayName("[Message] RequestMetaInformation")
    public void test_requestMetaMessage() throws IOException {
        String[] wanted = new String[] { MetaKeyEnum.FACTION_NEUTRAL.getKey(), "Quaffel", "Player.Lol" };
        RequestMetaInformationMessage message = new RequestMetaInformationMessage(PLAYER, wanted);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertArrayEquals(wanted, message.getKeys(), "Keys should be as desired");
    }

    @Test
    @Order(17)
    @DisplayName("[Message] RequestMetaInformation2")
    public void test_requestMetaMessage2() throws IOException {
        String[] wanted = new String[] { MetaKeyEnum.FACTION_NEUTRAL.getKey(), "Quaffel", "Player.Lol" };
        RequestMetaInformationMessage message = new RequestMetaInformationMessage(PLAYER, wanted,
                "Isch bin die Feuerwehr.");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertArrayEquals(wanted, message.getKeys(), "Keys should be as desired");
        Assertions.assertEquals("Isch bin die Feuerwehr.", message.getDebugMessage());
    }

    // STRIKE

    @Test
    @Order(18)
    @DisplayName("[Message] Strike")
    public void test_strikeMessage() throws IOException {
        StrikeMessage message = new StrikeMessage(PLAYER, 0, 5, "jens");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(0, message.getStrikeNr(), "StrikeNum");
        Assertions.assertEquals(5, message.getStrikeMax(), "StrikeMax");
        Assertions.assertEquals("jens", message.getReason(), "Reason");
    }

    @Test
    @Order(18)
    @DisplayName("[Message] Strike2")
    public void test_strikeMessage2() throws IOException {
        StrikeMessage message = new StrikeMessage(PLAYER, 0, 5, "jens", "SCHDRIGE!");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(0, message.getStrikeNr(), "StrikeNum");
        Assertions.assertEquals(5, message.getStrikeMax(), "StrikeMax");
        Assertions.assertEquals("jens", message.getReason(), "Reason");
        Assertions.assertEquals("SCHDRIGE!", message.getDebugMessage());
    }

    // GAME_OPERATION

    @Test
    @Order(19)
    @DisplayName("[Message] GameOperation")
    public void test_gameOperationMessage() throws IOException {
        Movement movement = new Movement(PLAYER2, new Point(RandomHelper.rndInt(9) - 3, RandomHelper.rndInt(6) - 4),
                new Point(RandomHelper.rndInt(12) - 9, RandomHelper.rndInt(9)));
        GameOperationMessage message = new GameOperationMessage(PLAYER, movement);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        BaseOperation got = message.getOperation();
        Assertions.assertTrue(got instanceof Movement, "Should be movement for: " + movement);
        Movement gotMovement = (Movement) got;
        Assertions.assertEquals(movement.getCharacterId(), gotMovement.getCharacterId(), "CharId");
        Assertions.assertEquals(movement.getFrom(), gotMovement.getFrom(), "CharId");
        Assertions.assertEquals(movement.getTarget(), gotMovement.getTarget(), "CharId");
    }

    @Test
    @Order(19)
    @DisplayName("[Message] Strike2")
    public void test_gameOperationMessage2() throws IOException {
        Movement movement = new Movement(PLAYER2, new Point(RandomHelper.rndInt(9) - 3, RandomHelper.rndInt(6) - 4),
                new Point(RandomHelper.rndInt(12) - 9, RandomHelper.rndInt(9)));
        GameOperationMessage message = new GameOperationMessage(PLAYER, movement, "Ohglahomma");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        BaseOperation got = message.getOperation();
        Assertions.assertTrue(got instanceof Movement, "Should be movement for: " + movement);
        Movement gotMovement = (Movement) got;
        Assertions.assertEquals(movement.getCharacterId(), gotMovement.getCharacterId(), "CharId");
        Assertions.assertEquals(movement.getFrom(), gotMovement.getFrom(), "CharId");
        Assertions.assertEquals(movement.getTarget(), gotMovement.getTarget(), "CharId");
        Assertions.assertEquals("Ohglahomma", message.getDebugMessage());
    }

    // REQUEST_GAME_OPERATION

    @Test
    @Order(20)
    @DisplayName("[Message] RequestGameOperation")
    public void test_requestGameOperationMessage() throws IOException {
        RequestGameOperationMessage message = new RequestGameOperationMessage(PLAYER, PLAYER2);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(PLAYER2, message.getCharacterId(), "CharId");
    }

    @Test
    @Order(20)
    @DisplayName("[Message] RequestGameOperation2")
    public void test_requestGameOperationMessage2() throws IOException {
        RequestGameOperationMessage message = new RequestGameOperationMessage(PLAYER, PLAYER2, "Trahaffel");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(PLAYER2, message.getCharacterId(), "CharId");
        Assertions.assertEquals("Trahaffel", message.getDebugMessage());
    }

    // GAME_STATUS

    @Test
    @Order(21)
    @DisplayName("[Message] GameStatus")
    public void test_gameStatusMessage() throws IOException {
        List<BaseOperation> operations = List.of(new BaseOperation(OperationEnum.CAT_ACTION, true, new Point(1, 3)));
        Character peter = new Character(SESSION, "Günther", new Point(2, 3), 12, 13, 14, 15, 16,
                List.of(PropertyEnum.AGILITY), List.of(new Gadget(GadgetEnum.NUGGET)));
        State state = new State(new FieldMap(new Field[0][0]), Set.of(peter));
        boolean go = RandomHelper.flip();
        GameStatusMessage message = new GameStatusMessage(PLAYER, PLAYER2, operations, state, go, "Jamoi");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(operations, message.getOperations(), "ops");
        Assertions.assertEquals(state, message.getState(), "state");
        Assertions.assertEquals(go, message.getIsGameOver(), "game over");
    }

    @Test
    @Order(21)
    @DisplayName("[Message] GameStatus2")
    public void test_gameStatusMessage2() throws IOException {
        List<BaseOperation> operations = List.of(new BaseOperation(OperationEnum.CAT_ACTION, true, new Point(1, 3)));
        Character peter = new Character(SESSION, "Günther", new Point(2, 3), 12, 13, 14, 15, 16,
                List.of(PropertyEnum.AGILITY), List.of(new Gadget(GadgetEnum.NUGGET)));
        State state = new State(new FieldMap(new Field[0][0]), Set.of(peter));
        boolean go = RandomHelper.flip();
        GameStatusMessage message = new GameStatusMessage(PLAYER, PLAYER2, operations, state, go, "Jamoi");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(operations, message.getOperations(), "ops");
        Assertions.assertEquals(state, message.getState(), "state");
        Assertions.assertEquals(go, message.getIsGameOver(), "game over");
        Assertions.assertEquals(PLAYER2, message.getActiveCharacterId(), "Set active Char");
        Assertions.assertEquals("Jamoi", message.getDebugMessage());
    }

    // STATISTICS

    @Test
    @Order(22)
    @DisplayName("[Message] GameStatus")
    public void test_statisticsMessage() throws IOException {
        boolean rndBool = RandomHelper.flip(); // has replay
        StatisticsMessage message = new StatisticsMessage(PLAYER, null, PLAYER2, VictoryEnum.VICTORY_BY_IP, rndBool);
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(null, message.getStatistics(), "statistics");
        Assertions.assertEquals(PLAYER2, message.getWinner(), "winner");
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_IP, message.getReason(), "reason");
        Assertions.assertEquals(rndBool, message.hasReplay(), "replay");
    }

    @Test
    @Order(22)
    @DisplayName("[Message] GameStatus2")
    public void test_statisticsMessage2() throws IOException {
        boolean rndBool = RandomHelper.flip(); // has replay
        StatisticsMessage message = new StatisticsMessage(PLAYER, null, PLAYER2, VictoryEnum.VICTORY_BY_IP, rndBool,
                "albagga juchei");
        assertDataAsWantedGeneric(message);
        Assertions.assertEquals(PLAYER, message.getClientId());
        Assertions.assertEquals(null, message.getStatistics(), "statistics");
        Assertions.assertEquals(PLAYER2, message.getWinner(), "winner");
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_IP, message.getReason(), "reason");
        Assertions.assertEquals(rndBool, message.hasReplay(), "replay");
        Assertions.assertEquals("albagga juchei", message.getDebugMessage());
    }

}