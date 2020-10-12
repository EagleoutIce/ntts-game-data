package de.uulm.team020.validation;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

// Special Test-Suite to offer test for everything. Will not recover others
public class GameDataGsonTest {

    @Test @Tag("Core") @Order(1)
    @DisplayName("[GSON] Specific Field")
    public void test_specificField() throws Exception {
        String data = "{\"a\":\"b\",\"c\":\"d\"}";
        Assertions.assertEquals("\"b\"", GameDataGson.extractField(data, "a"), "'a' as wanted");
        Assertions.assertEquals("\"d\"", GameDataGson.extractField(data, "c"), "'c' as wanted");
    }

    @Test @Tag("Core") @Order(2)
    @DisplayName("[GSON] Explicit Operation re parse")
    public void test_explicitOperationReParse() throws Exception {
        String data = GameDataGson.loadInternalJson("json/files/messages/gameoperation_message.json");
        MessageContainer container = GameDataGson.getContainer(data);
        Assertions.assertEquals(MessageTypeEnum.GAME_OPERATION, container.getType(), "Should be game-operation");
        Assertions.assertEquals(MessageTypeEnum.GAME_OPERATION, GameDataGson.getType(data), "Should be game-operation");
        // Parse Movement
        Movement movement = GameDataGson.fromJson(data, "operation", Movement.class);
        Assertions.assertEquals(new Point(3,5), movement.getTarget(), "Target should be as given");
        Assertions.assertEquals(UUID.fromString("5f3ed3c4-2065-435d-a07c-2b3e70910f48"), movement.getCharacterId(), "Character should be as given");
        Assertions.assertEquals(new Point(-2,-42), movement.getFrom(), "From should be as given");
    }

    @Test @Tag("Core") @Order(3)
    @DisplayName("[GSON] Parse invalids")
    public void test_parseInvalids() throws Exception {
        Assertions.assertNull(GameDataGson.getContainer(null), "null on null");
        Assertions.assertNull(GameDataGson.getContainer(""), "null on empty");
        Assertions.assertNull(GameDataGson.getContainer("{"), "null on invalid");
        Assertions.assertNull(GameDataGson.getType(null), "type null on null");
        Assertions.assertNull(GameDataGson.getType(""),   "type null on empty");
        Assertions.assertNull(GameDataGson.getType("{"),  "type null on invalid");
    }

    @Test @Tag("Core") @Order(3)
    @DisplayName("[GSON] Gson delegate")
    public void test_gsonDelegate() throws Exception {
        Assertions.assertEquals("{}", GameDataGson.toJson(new Object()), "empty convert");
    }
}