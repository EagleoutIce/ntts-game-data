package de.uulm.team020.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.everit.json.schema.loader.internal.DefaultSchemaClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.uulm.team020.logging.Magpie;

/**
 * Ease-To-Use Ressource-Provider which aims on concentrate any "hardcoded"
 * resource paths to one location.
 * 
 * @author Florian Sihler
 * @version 1.2b, 03/27/2020
 */
public class SchemaProvider {

    private static final String LOADING_TXT = "Loading";

    private static Magpie magpie = Magpie.createMagpieSafe("Validator");

    // Buffer:
    private static String last_schema_path = "";
    private static Schema last_schema;

    // There should be no Class like this one :D
    private SchemaProvider() {
    }

    /** Root path which targets at the main location of schemas */
    public static final String SCHEMA_ROOT_PATH = "json/schemas/";

    /** Root path which targets at the main location of message-schemas */
    public static final String SCHEMA_MESSAGES_ROOT_PATH = SCHEMA_ROOT_PATH + "messages/";

    private static SchemaClient provideClient() {
        return new JarClassPathAwareSchemaClient(new DefaultSchemaClient());
    }

    /**
     * Constructs a Schema based on a String
     * 
     * @param schema the String-Representation of the Schema
     * 
     * @return the loaded Schema
     * 
     * @see Validator#validateObject(String, Schema)
     */
    public static Schema loadSchemaFromString(String schema) {
        magpie.writeInfo("Loading schema from path " + schema, "Init");
        JSONObject schemaObj = new JSONObject(schema);
        SchemaLoader schemaLoader = SchemaLoader.builder().draftV7Support().schemaClient(provideClient())
                .schemaJson(schemaObj).resolutionScope("classpath://" + SCHEMA_ROOT_PATH).build();
        return schemaLoader.load().build();
    }

    /**
     * Constructs a Schema based on a File
     * <p>
     * Note: This method will buffer the last used file so multiple requests will
     * not cause the regeneration of the schema, but instead return the already
     * generated.
     * 
     * @param path the internal Path to the Schema
     * 
     * @return the loaded Schema
     * 
     * @see Validator#validateObject(String, Schema)
     */
    public static Schema loadSchemaFromResources(String path) {
        magpie.writeInfo("Loading schema from resource: '" + path + "'", LOADING_TXT);
        if (path.equals(last_schema_path))
            return last_schema;

        try (InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            JSONObject schemaObj = new JSONObject(new JSONTokener(Objects.requireNonNull(schema)));
            last_schema_path = path;
            SchemaLoader schemaLoader = SchemaLoader.builder().draftV7Support().schemaClient(provideClient())
                    .schemaJson(schemaObj).resolutionScope("classpath://" + SCHEMA_ROOT_PATH).build();
            last_schema = schemaLoader.load().build();
            return last_schema;
        } catch (IOException ex) {
            magpie.writeException(ex, LOADING_TXT);
        } catch (Exception ex) {
            magpie.writeException(ex, LOADING_TXT);
            throw ex; // rethrow but document
        }
        return null;
    }

    // Files ===================================================

    /** Json Schema for Scenario-Files */
    public static final String SCENARIO_SCHEMA_PATH = SCHEMA_ROOT_PATH + "scenario.schema";

    /** Json Schema for Matchconfiguration */
    public static final String MATCHCONFIG_SCHEMA_PATH = SCHEMA_ROOT_PATH + "matchconfig.schema";

    /** Json Schema for CharacterDescriptions */
    public static final String CHARACTERS_SCHEMA_PATH = SCHEMA_ROOT_PATH + "characters.schema";

    /**
     * Returns the Schema for Scenario-Files, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema SCENARIO_SCHEMA = loadSchemaFromResources(SCENARIO_SCHEMA_PATH);

    /**
     * Returns the Schema for Matchconfig-Files, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema MATCHCONFIG_SCHEMA = loadSchemaFromResources(MATCHCONFIG_SCHEMA_PATH);

    /**
     * Returns the Schema for CharacterDescription-Files, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema CHARACTERS_SCHEMA = loadSchemaFromResources(CHARACTERS_SCHEMA_PATH);

    // Messages ================================================

    /** Json Schema for Message-Container */
    public static final String MESSAGE_CONTAINER_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "message_container.schema";

    /** Json Schema for Hello-Message */
    public static final String HELLO_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "hello_message.schema";

    /** Json Schema for HelloReply-Message */
    public static final String HELLO_REPLY_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "helloreply_message.schema";

    /** Json Schema for Reconnect-Message */
    public static final String RECONNECT_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "reconnect_message.schema";

    /** Json Schema for GameStarted-Message */
    public static final String GAME_STARTED_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "gamestarted_message.schema";

    /** Json Schema for RequestItemChoice-Message */
    public static final String REQUEST_ITEM_CHOICE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "requestitemchoice_message.schema";

    /** Json Schema for ItemChoice-Message */
    public static final String ITEM_CHOICE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "itemchoice_message.schema";

    /** Json Schema for GameLeave-Message */
    public static final String GAME_LEAVE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "gameleave_message.schema";

    /** Json Schema for GameLeft-Message */
    public static final String GAME_LEFT_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "gameleft_message.schema";

    /** Json Schema for Error-Message */
    public static final String ERROR_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "error_message.schema";

    /** Json Schema for RequestGamePause-Message */
    public static final String REQUEST_GAME_PAUSE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "requestgamepause_message.schema";

    /** Json Schema for GamePause-Message */
    public static final String GAME_PAUSE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "gamepause_message.schema";

    /** Json Schema for RequestEquipmentChoice-Message */
    public static final String REQUEST_EQUIPMENT_CHOICE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "requestequipmentchoice_message.schema";

    /** Json Schema for EquipmentChoice-Message */
    public static final String EQUIPMENT_CHOICE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH
            + "equipmentchoice_message.schema";

    /** Json Schema for StrikeChoice-Message */
    public static final String STRIKE_MESSAGE_SCHEMA_PATH = SCHEMA_MESSAGES_ROOT_PATH + "strike_message.schema";

    /**
     * Returns the Schema for Message-Container, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema MESSAGE_CONTAINER_SCHEMA = loadSchemaFromResources(MESSAGE_CONTAINER_SCHEMA_PATH);

    /**
     * Returns the Schema for Hello-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema HELLO_MESSAGE_SCHEMA = loadSchemaFromResources(HELLO_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for HelloReply-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema HELLO_REPLY_MESSAGE_SCHEMA = loadSchemaFromResources(HELLO_REPLY_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for Reconnect-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema RECONNECT_MESSAGE_SCHEMA = loadSchemaFromResources(RECONNECT_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GameStarted-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema GAME_STARTED_MESSAGE_SCHEMA = loadSchemaFromResources(GAME_STARTED_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for RequestItemChoice-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema REQUEST_ITEM_CHOICE_MESSAGE_SCHEMA = loadSchemaFromResources(
            REQUEST_ITEM_CHOICE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for ItemChoice-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema ITEM_CHOICE_MESSAGE_SCHEMA = loadSchemaFromResources(ITEM_CHOICE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GamePause-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema REQUEST_EQUIPMENT_CHOICE_MESSAGE_SCHEMA = loadSchemaFromResources(
            REQUEST_EQUIPMENT_CHOICE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GamePause-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema EQUIPMENT_CHOICE_MESSAGE_SCHEMA = loadSchemaFromResources(
            EQUIPMENT_CHOICE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GameLeave-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema GAME_LEAVE_MESSAGE_SCHEMA = loadSchemaFromResources(GAME_LEAVE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GameLeft-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema GAME_LEFT_MESSAGE_SCHEMA = loadSchemaFromResources(GAME_LEFT_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for Error-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema ERROR_MESSAGE_SCHEMA = loadSchemaFromResources(ERROR_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for Strike-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema STRIKE_MESSAGE_SCHEMA = loadSchemaFromResources(STRIKE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for RequestGamePause-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema REQUEST_GAME_PAUSE_MESSAGE_SCHEMA = loadSchemaFromResources(
            REQUEST_GAME_PAUSE_MESSAGE_SCHEMA_PATH);

    /**
     * Returns the Schema for GamePause-Messages, can be passed to
     * {@link Validator#validateObject(String, Schema)}
     */
    public static final Schema GAME_PAUSE_MESSAGE_SCHEMA = loadSchemaFromResources(GAME_PAUSE_MESSAGE_SCHEMA_PATH);

}