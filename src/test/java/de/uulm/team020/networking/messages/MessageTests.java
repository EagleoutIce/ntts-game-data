package de.uulm.team020.networking.messages;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.validation.GameDataGson;
import de.uulm.team020.validation.ValidationReport;
import de.uulm.team020.validation.Validator;

/**
 * Tests the basic parsing and construction of all messages, to ensure they are
 * at least able to parse valid messages.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/24/2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class MessageTests {

    private static Stream<Arguments> generate_message_links() {
        return Stream.of(
                // The expected messages
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/hello_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.HELLO_REPLY, "messages/helloreply_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.RECONNECT, "messages/reconnect_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.GAME_STARTED, "messages/gamestarted_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_ITEM_CHOICE, "messages/requestitemchoice_message.json",
                        true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_ITEM_CHOICE, "messages/requestitemchoice_message2.json",
                        true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_ITEM_CHOICE, "messages/requestitemchoice_message3.json",
                        true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_ITEM_CHOICE, "messages/requestitemchoice_message4.json",
                        true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_ITEM_CHOICE, "messages/requestitemchoice_message5.json",
                        true, true),
                Arguments.arguments(MessageTypeEnum.ITEM_CHOICE, "messages/itemchoice_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE,
                        "messages/requestequipmentchoice_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.EQUIPMENT_CHOICE, "messages/equipmentchoice_message.json", true,
                        true),
                Arguments.arguments(MessageTypeEnum.GAME_LEAVE, "messages/gameleave_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.GAME_LEFT, "messages/gameleft_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.ERROR, "messages/error_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.STRIKE, "messages/strike_message.json", true, true),
                Arguments.arguments(MessageTypeEnum.REQUEST_GAME_PAUSE, "messages/requestgamepause_message.json", true,
                        true),
                Arguments.arguments(MessageTypeEnum.GAME_PAUSE, "messages/gamepause_message.json", true, true),

                // Field-error wrong checks
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/hello_message_1.json", false, true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/hello_message_2.json", false, true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/hello_message_3.json", false, true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/hello_message_4.json", false, true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/hello_message_5.json", false, true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/requestitemchoice_message_1.json", false,
                        true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/requestitemchoice_message_2.json", false,
                        true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/requestitemchoice_message_3.json", false,
                        true),
                Arguments.arguments(MessageTypeEnum.HELLO, "messages/invalid/requestitemchoice_message_4.json", false,
                        true),
                // The completely wrongs
                Arguments.arguments(MessageTypeEnum.HELLO_REPLY, "general/invalid.json", false, false));
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(0)
    @DisplayName("[Valid] Test all messages for being valid")
    @MethodSource("generate_message_links")
    public void test_messages_valid(MessageTypeEnum type, String messagePath, boolean validAgainst,
            boolean validContainer) throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/" + messagePath);

        if (!validContainer) {
            Assertions.assertNull(GameDataGson.getType(json),
                    "Message (" + messagePath + ") should not have valid containerformat");
            return;
        }

        if (validAgainst)
            Assertions.assertEquals(type, GameDataGson.getType(json),
                    "Message (" + messagePath + ") should have the requested state");
        // else we do not care!

        if (type.getLinkedSchema() != null) { // let's validate
            ValidationReport report = Validator.validateObject(json, type.getLinkedSchema());
            Assertions.assertEquals(validAgainst, report.isValid(),
                    "The report for '" + messagePath + "' with type: '" + type + "' should be "
                            + (validAgainst ? "valid" : "invalid") + ". Errors: " + report.getReasons());
        }

        // validate that it is invalid against all other schemas
        ValidationReport report;
        for (MessageTypeEnum mtype : MessageTypeEnum.values()) {
            if (mtype == type) // skip same
                continue;
            if (mtype.getLinkedSchema() == null) // skip if no schema attached (for now)
                continue;
            report = Validator.validateObject(json, mtype.getLinkedSchema());
            Assertions.assertFalse(report.isValid(), "The report for '" + messagePath + "' with type: '" + type
                    + "' should be invalid for type: '" + mtype + "'");
        }
    }

    @Test
    @Tag("Network")
    @Order(1)
    @DisplayName("[Hello] Parse the Hello Message")
    public void test_hello_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/hello_message.json");

        Assertions.assertEquals(MessageTypeEnum.HELLO, GameDataGson.getType(json), "Message should be a Hello-Message");

        HelloMessage helloMessage = GameDataGson.fromJson(json, HelloMessage.class);
        // Test MessageContainer (null is specific to the HelloMessage too ^^)
        Assertions.assertNull(helloMessage.getClientId(), "A Hello-Message is not allowed to have a clientId");
        Assertions.assertEquals(MessageTypeEnum.HELLO, helloMessage.getType(), "It should still be a Hello-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 1, 38);
        Assertions.assertEquals(cal.getTime(), helloMessage.getCreationDate(), "The creationDate should be as stated.");
        Assertions.assertEquals("Ich bin eine Testnachricht!", helloMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // HelloMessage-specific
        Assertions.assertEquals("Superduper-Name", helloMessage.getName(), "The name should be as set.");
        Assertions.assertEquals(RoleEnum.SPECTATOR, helloMessage.getRole(), "The role should be as set.");
    }

    private static final Matchconfig DEFAULT_MATCHCONFIG = new Matchconfig(1, 1, 0.25, 4, 0.125, 2, 6, 2, 1, 2, 3, 0.35,
            0.64, 0.35, 0.25, 6, 0.65, 0.25, 0.35, 0.12, 12, 3, 3, 5, 15, 6, 8, 4, 320, 20);

    private static final Scenario DEFAULT_SCENARIO = new Scenario(new FieldStateEnum[][] {
            new FieldStateEnum[] { FieldStateEnum.WALL, FieldStateEnum.WALL, FieldStateEnum.WALL, FieldStateEnum.WALL },
            new FieldStateEnum[] { FieldStateEnum.WALL, FieldStateEnum.FIREPLACE, FieldStateEnum.WALL,
                    FieldStateEnum.BAR_TABLE },
            new FieldStateEnum[] { FieldStateEnum.WALL, FieldStateEnum.BAR_TABLE, FieldStateEnum.FREE,
                    FieldStateEnum.ROULETTE_TABLE },
            new FieldStateEnum[] { FieldStateEnum.WALL, FieldStateEnum.BAR_SEAT, FieldStateEnum.FREE,
                    FieldStateEnum.WALL } });

    private static final CharacterInformation[] DEFAULT_CHARACTERS = new CharacterInformation[] {
            new CharacterInformation(
                    new CharacterDescription("James Bond", "Bester Geheimagent aller Zeiten mit 00-Status.",
                            GenderEnum.DIVERSE,
                            Arrays.asList(PropertyEnum.SPRYNESS, PropertyEnum.TOUGHNESS, PropertyEnum.ROBUST_STOMACH,
                                    PropertyEnum.LUCKY_DEVIL, PropertyEnum.TRADECRAFT)),
                    UUID.fromString("ec03bbd4-d3ea-46b7-9d63-b10560d78709")),
            new CharacterInformation(new CharacterDescription("Meister Yoda",
                    "Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.",
                    null, Arrays.asList(PropertyEnum.LUCKY_DEVIL, PropertyEnum.OBSERVATION, PropertyEnum.TOUGHNESS)),
                    UUID.fromString("d07b4fa6-eca3-4aee-89a2-270f6a3fa3fe")) };

    @Test
    @Tag("Network")
    @Order(2)
    @DisplayName("[HelloReply] Parse the HelloReply Message")
    public void test_helloreply_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/helloreply_message.json");

        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(json),
                "Message should be a HelloReply-Message");

        HelloReplyMessage helloReplyMessage = GameDataGson.fromJson(json, HelloReplyMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                helloReplyMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, helloReplyMessage.getType(),
                "It should still be a HelloReply-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 1, 39);
        Assertions.assertEquals(cal.getTime(), helloReplyMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Das hast du gut gemacht, du Quaker!", helloReplyMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // HelloReplyMessage-specific
        Assertions.assertEquals(UUID.fromString("315a470a-d519-4c18-a29e-db75c7381984"),
                helloReplyMessage.getSessionId(), "The should have the stated sessionId.");
        Assertions.assertEquals(DEFAULT_SCENARIO, helloReplyMessage.getLevel(), "Scenario should be as set.");
        Assertions.assertEquals(DEFAULT_MATCHCONFIG, helloReplyMessage.getSettings(), "Settings should be as set");
        Assertions.assertArrayEquals(DEFAULT_CHARACTERS, helloReplyMessage.getCharacterSettings(),
                "Characters should be as Set");
    }

    @Test
    @Tag("Network")
    @Order(3)
    @DisplayName("[Reconnect] Parse the Reconnect Message")
    public void test_reconnect_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/reconnect_message.json");

        Assertions.assertEquals(MessageTypeEnum.RECONNECT, GameDataGson.getType(json),
                "Message should be a Reconnect-Message");

        ReconnectMessage reconnectMessage = GameDataGson.fromJson(json, ReconnectMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"), reconnectMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.RECONNECT, reconnectMessage.getType(),
                "It should still be a Reconnect-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 2, 39);
        Assertions.assertEquals(cal.getTime(), reconnectMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Recoquaaaack", reconnectMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // ReconnectMessage-specific
        Assertions.assertEquals(UUID.fromString("315a470a-d519-4c18-a29e-db75c7381984"),
                reconnectMessage.getSessionId(), "The message should have the stated sessionId.");
    }

    @Test
    @Tag("Network")
    @Order(4)
    @DisplayName("[Error] Parse the Error Message")
    public void test_error_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/error_message.json");

        Assertions.assertEquals(MessageTypeEnum.ERROR, GameDataGson.getType(json), "Message should be a Error-Message");

        ErrorMessage errorMessage = GameDataGson.fromJson(json, ErrorMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"), errorMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.ERROR, errorMessage.getType(),
                "It should still be a ConnectionError-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 3, 25);
        Assertions.assertEquals(cal.getTime(), errorMessage.getCreationDate(), "The creationDate should be as stated.");
        Assertions.assertEquals("Das erlaube ich dir nicht!", errorMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // ErrorMessage-specific
        Assertions.assertEquals(ErrorTypeEnum.SESSION_DOES_NOT_EXIST, errorMessage.getReason(),
                "The reason should be as stated.");
    }

    @Test
    @Tag("Network")
    @Order(5)
    @DisplayName("[GameStarted] Parse the GameStarted Message")
    public void test_gamestarted_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/gamestarted_message.json");

        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, GameDataGson.getType(json),
                "Message should be a GameStarted-Message");

        GameStartedMessage gameStartedMessage = GameDataGson.fromJson(json, GameStartedMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                gameStartedMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, gameStartedMessage.getType(),
                "It should still be a GameStarted-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 3, 2, 39);
        Assertions.assertEquals(cal.getTime(), gameStartedMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Quack the start", gameStartedMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // GameStarted-specific
        Assertions.assertEquals(UUID.fromString("315a470a-d519-4c18-a29e-db75c7381984"),
                gameStartedMessage.getSessionId(), "It should have the stated sessionId.");
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                gameStartedMessage.getPlayerOneId(), "It should have the stated playerOneId.");
        Assertions.assertEquals(UUID.fromString("ec03bbd4-d3ea-46b7-9d63-b10560d78709"),
                gameStartedMessage.getPlayerTwoId(), "It should have the stated playerTwoId.");
        Assertions.assertEquals("Superduper-Name", gameStartedMessage.getPlayerOneName(),
                "It should have the stated playerOneName.");
        Assertions.assertEquals("Der Petaaaa", gameStartedMessage.getPlayerTwoName(),
                "It should have the stated playerTwoName.");
    }

    @Test
    @Tag("Network")
    @Order(6)
    @DisplayName("[RequestItemChoice] Parse the RequestItemChoice Message")
    public void test_requestitemchoice_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/requestitemchoice_message.json");

        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, GameDataGson.getType(json),
                "Message should be a RequestItemChoice-Message");

        RequestItemChoiceMessage requestItemChoiceMessage = GameDataGson.fromJson(json, RequestItemChoiceMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                requestItemChoiceMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, requestItemChoiceMessage.getType(),
                "It should still be a RequestItemChoice-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 4, 2, 39);
        Assertions.assertEquals(cal.getTime(), requestItemChoiceMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Ich brauche was von dir.", requestItemChoiceMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // RequestItemChoice-specific
        List<UUID> offeredCharacterIds = requestItemChoiceMessage.getOfferedCharacterIds();
        Assertions.assertNotNull(offeredCharacterIds,
                "There are 3 characters emplaced, this should not and never be null.");
        List<UUID> expectedUuids = List.of(UUID.fromString("ec03bbd4-d3ea-46b7-9d63-b10560d78709"),
                UUID.fromString("d07b4fa6-eca3-4aee-89a2-270f6a3fa3fe"),
                UUID.fromString("229a0c7c-ce77-4982-8666-cd6e52fb1f73"));
        Assertions.assertEquals(expectedUuids, offeredCharacterIds, "The uuids should be as stated");
        List<GadgetEnum> offeredGadgets = requestItemChoiceMessage.getOfferedGadgets();
        Assertions.assertNotNull(offeredGadgets, "There are 3 gadgets emplaced, this should not and never be null.");
        List<GadgetEnum> expectedGadgets = List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.FOG_TIN,
                GadgetEnum.CHICKEN_FEED);
        Assertions.assertEquals(expectedGadgets, offeredGadgets, "The gadgets should be as stated");
    }

    @Test
    @Tag("Network")
    @Order(7)
    @DisplayName("[ItemChoice] Parse the ItemChoice Message")
    public void test_itemchoice_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/itemchoice_message.json");

        Assertions.assertEquals(MessageTypeEnum.ITEM_CHOICE, GameDataGson.getType(json),
                "Message should be a ItemChoice-Message");

        ItemChoiceMessage itemChoiceMessage = GameDataGson.fromJson(json, ItemChoiceMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                itemChoiceMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.ITEM_CHOICE, itemChoiceMessage.getType(),
                "It should still be a ItemChoice-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 4, 2, 42);
        Assertions.assertEquals(cal.getTime(), itemChoiceMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals(
                "Ich habe was für dich, das eigentlich nicht erlaubt sein sollte, sowohl ein Charakter aber auch ein Gadget ist gewählt, aber das ist (nur) für diesen Test ok!",
                itemChoiceMessage.getDebugMessage(), "The debugMessage should be as stated.");
        // ItemChoice-specific

        Assertions.assertEquals(UUID.fromString("d07b4fa6-eca3-4aee-89a2-270f6a3fa3fe"),
                itemChoiceMessage.getChosenCharacterId(), "The chosen character should have the correct uuid.");

        Assertions.assertEquals(GadgetEnum.CHICKEN_FEED, itemChoiceMessage.getChosenGadget(),
                "The gadgets should be as stated");
    }

    public static Stream<Arguments> generate_itemchoice_message_validity() {
        return Stream.of(Arguments.arguments(null, null, false),
                Arguments.arguments(null, GadgetEnum.BOWLER_BLADE, true),
                Arguments.arguments(null, GadgetEnum.GAS_GLOSS, true),
                Arguments.arguments(UUID.randomUUID(), null, true), Arguments.arguments(UUID.randomUUID(), null, true),
                Arguments.arguments(UUID.randomUUID(), GadgetEnum.BOWLER_BLADE, false),
                Arguments.arguments(UUID.randomUUID(), GadgetEnum.GAS_GLOSS, false)

        );
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(7)
    @DisplayName("[ItemChoice] Verify the ItemChoice validity check for semantics")
    @MethodSource("generate_itemchoice_message_validity")
    public void test_itemchoice_message_validity(UUID characterId, GadgetEnum gadget, boolean valid) throws Exception {
        ItemChoiceMessage itemChoiceMessage = new ItemChoiceMessage(UUID.randomUUID(), characterId, gadget);
        Assertions.assertEquals(valid, itemChoiceMessage.isValid(),
                "The message should be " + (valid ? "valid." : "invalid."));
    }

    @Test
    @Tag("Network")
    @Order(8)
    @DisplayName("[RequestEquipmentChoice] Parse the RequestEquipmentChoice Message")
    public void test_requestequipmentchoice_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/requestequipmentchoice_message.json");

        Assertions.assertEquals(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, GameDataGson.getType(json),
                "Message should be a RequestEquipmentChoice-Message");

        RequestEquipmentChoiceMessage requestEquipmentChoiceMessage = GameDataGson.fromJson(json,
                RequestEquipmentChoiceMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"),
                requestEquipmentChoiceMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, requestEquipmentChoiceMessage.getType(),
                "It should still be a RequestEquipmentChoice-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 4, 6, 39);
        Assertions.assertEquals(cal.getTime(), requestEquipmentChoiceMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Ich brauche noch etwas von dir.", requestEquipmentChoiceMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // RequestEquipmentChoice-specific
        List<UUID> chosenCharacterIds = requestEquipmentChoiceMessage.getChosenCharacterIds();
        Assertions.assertNotNull(chosenCharacterIds,
                "There are 4 characters emplaced, this should not and never be null.");
        List<UUID> expectedUuids = List.of(UUID.fromString("ec03bbd4-d3ea-46b7-9d63-b10560d78709"),
                UUID.fromString("229a0c7c-ce77-4982-8666-cd6e52fb1f73"),
                UUID.fromString("f36cca4b-7557-483d-b873-d312657c60ca"),
                UUID.fromString("d07b4fa6-eca3-4aee-89a2-270f6a3fa3fe"));
        Assertions.assertEquals(expectedUuids, chosenCharacterIds, "The uuids should be as stated");
        List<GadgetEnum> chosenGadgets = requestEquipmentChoiceMessage.getChosenGadgets();
        Assertions.assertNotNull(chosenGadgets, "There are 4 gadgets emplaced, this should not and never be null.");
        List<GadgetEnum> expectedGadgets = List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.FOG_TIN, GadgetEnum.ROCKET_PEN,
                GadgetEnum.GAS_GLOSS);
        Assertions.assertEquals(expectedGadgets, chosenGadgets, "The gadgets should be as stated");
    }

    @Test
    @Tag("Network")
    @Order(9)
    @DisplayName("[GameLeave] Parse the GameLeave Message")
    public void test_gameleave_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/gameleave_message.json");

        Assertions.assertEquals(MessageTypeEnum.GAME_LEAVE, GameDataGson.getType(json),
                "Message should be a GameLeave-Message");

        GameLeaveMessage gameLeaveMessage = GameDataGson.fromJson(json, GameLeaveMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42abc662-f50c-4965-8cee-f919d7e748e9"), gameLeaveMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.GAME_LEAVE, gameLeaveMessage.getType(),
                "It should still be a GameLeave-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 1, 38);
        Assertions.assertEquals(cal.getTime(), gameLeaveMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Supa trupa.", gameLeaveMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // GameLeave-specific
    }

    @Test
    @Tag("Network")
    @Order(10)
    @DisplayName("[GameLeft] Parse the GameLeft Message")
    public void test_gameleft_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/gameleft_message.json");

        Assertions.assertEquals(MessageTypeEnum.GAME_LEFT, GameDataGson.getType(json),
                "Message should be a GameLeave-Message");

        GameLeftMessage gameLeftMessage = GameDataGson.fromJson(json, GameLeftMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42abc662-f50c-4965-8cee-f919d7e748e9"), gameLeftMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.GAME_LEFT, gameLeftMessage.getType(),
                "It should still be a GameLeave-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 1, 42);
        Assertions.assertEquals(cal.getTime(), gameLeftMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Supa tschupa.", gameLeftMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // GameLeft-specific
        Assertions.assertEquals(UUID.fromString("42abc762-f50c-4965-8cee-f919d7e748e9"),
                gameLeftMessage.getLeftUserId(), "The should have the stated leftUserId.");
    }

    @Test
    @Tag("Network")
    @Order(11)
    @DisplayName("[RequestGamePause] Parse the RequestGamePause Message")
    public void test_requestgamepause_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/requestgamepause_message.json");

        Assertions.assertEquals(MessageTypeEnum.REQUEST_GAME_PAUSE, GameDataGson.getType(json),
                "Message should be a RequestGamePause-Message");

        RequestGamePauseMessage requestGamePauseMessage = GameDataGson.fromJson(json, RequestGamePauseMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42abc662-f50c-4965-8cee-f919d7e748e9"),
                requestGamePauseMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_GAME_PAUSE, requestGamePauseMessage.getType(),
                "It should still be a RequestGamePause-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 2, 3, 38);
        Assertions.assertEquals(cal.getTime(), requestGamePauseMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Isch bin ene messätsch.", requestGamePauseMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // RequestGamePause-specific
        Assertions.assertTrue(requestGamePauseMessage.getGamePause(), "The game should be requested to be paused.");
    }

    @Test
    @Tag("Network")
    @Order(12)
    @DisplayName("[GamePause] Parse the GamePause Message")
    public void test_gamepause_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/gamepause_message.json");

        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(json),
                "Message should be a GamePause-Message");

        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(json, GamePauseMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42abc662-f50c-4965-8cee-f919d7e748e9"), gamePauseMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, gamePauseMessage.getType(),
                "It should still be a GamePause-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 3, 3, 38);
        Assertions.assertEquals(cal.getTime(), gamePauseMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Desch schupa.", gamePauseMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // GameLeft-specific
        Assertions.assertTrue(gamePauseMessage.getGamePaused(), "The game should be paused.");
        Assertions.assertFalse(gamePauseMessage.getServerEnforced(), "The pause should not be server enforced.");
    }

    @Test
    @Tag("Network")
    @Order(13)
    @DisplayName("[EquipmentChoice] Parse the EquipmentChoice Message")
    public void test_equipmentchoice_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/equipmentchoice_message.json");

        Assertions.assertEquals(MessageTypeEnum.EQUIPMENT_CHOICE, GameDataGson.getType(json),
                "Message should be a EquipmentChoice-Message");

        EquipmentChoiceMessage equipmentChoiceMessage = GameDataGson.fromJson(json, EquipmentChoiceMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("43ddc662-f50c-4965-8cee-f919d7e748e9"),
                equipmentChoiceMessage.getClientId(), "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.EQUIPMENT_CHOICE, equipmentChoiceMessage.getType(),
                "It should still be a EquipmentChoice-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.MARCH, 24, 4, 2, 39);
        Assertions.assertEquals(cal.getTime(), equipmentChoiceMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Das ist eine Auswahl, das du bestimmt ganz toll findest!",
                equipmentChoiceMessage.getDebugMessage(), "The debugMessage should be as stated.");
        // EquipmentChoice-specific

        // Create the modelled Map
        Map<UUID, List<GadgetEnum>> goal = Map.of(UUID.fromString("ec03bbd4-d3ea-46b7-9d63-b10560d78709"),
                List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.MOLEDIE),
                UUID.fromString("d07b4fa6-eca3-4aee-89a2-270f6a3fa3fe"), List.of(),
                UUID.fromString("229a0c7c-ce77-4982-8666-cd6e52fb1f73"), List.of(GadgetEnum.CHICKEN_FEED));

        Assertions.assertEquals(goal, equipmentChoiceMessage.getEquipment(), "Equipment should be as stated");
    }

    @Test
    @Tag("Network")
    @Order(14)
    @DisplayName("[Strike] Parse the Strike Message")
    public void test_strike_message() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/messages/strike_message.json");

        Assertions.assertEquals(MessageTypeEnum.STRIKE, GameDataGson.getType(json),
                "Message should be a Strike-Message");

        StrikeMessage strikeMessage = GameDataGson.fromJson(json, StrikeMessage.class);
        // Test MessageContainer
        Assertions.assertEquals(UUID.fromString("42ddc662-f50c-4965-8cee-f919d7e748e9"), strikeMessage.getClientId(),
                "The should have the stated clientId.");
        Assertions.assertEquals(MessageTypeEnum.STRIKE, strikeMessage.getType(), "It should still be a Strike-Message");
        // Construct Calendar
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2020, Calendar.APRIL, 14, 2, 3, 25);
        Assertions.assertEquals(cal.getTime(), strikeMessage.getCreationDate(),
                "The creationDate should be as stated.");
        Assertions.assertEquals("Dis ist de strike.", strikeMessage.getDebugMessage(),
                "The debugMessage should be as stated.");
        // StrikeMessage-specific
        Assertions.assertEquals(3, strikeMessage.getStrikeNr(), "Number of Strikes should be as stated.");
        Assertions.assertEquals(4, strikeMessage.getStrikeMax(), "Number of max Strikes should be as stated.");
        Assertions.assertEquals("Du bisch doof", strikeMessage.getReason(), "The reason should be as stated.");
        Assertions.assertFalse(strikeMessage.limitReached(), "Limit should not be reached");
    }
}