package de.uulm.team020.validation;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Test functionality of {@link de.uulm.team020.validation.Validator}
 * 
 * @author Florian Sihler
 * @version 1.0, 03/17/2020
 */
public class ValidatorTest {

    // @BeforeAll
    // public void disable_magpie() {
    //     // We do not want magpie to log while testing as this would create an unnecessary amount of Files
    //     Magpie.disableAllLogs();
    // }


    // We want to check the buffer aswell so we may do em multiple times!
    @ParameterizedTest @Tag("Core") @Order(1)
    @DisplayName("[JSON] Load a schema from a file")
    @ValueSource(strings = {
        "json/schemas/scenario.schema",
        "json/schemas/scenario.schema",
        "json/schemas/matchconfig.schema",
        "json/schemas/scenario.schema",
        "json/schemas/matchconfig.schema",
        "json/schemas/characters.schema"
    })
    public void test_load_schema(String path) throws Exception {
        Schema schema = SchemaProvider.loadSchemaFromResources(path);

        InputStream ischema = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        JSONObject schemaObj = new JSONObject(new JSONTokener(Objects.requireNonNull(ischema)));
        Schema target =  SchemaLoader.load(schemaObj);
        ischema.close();
        Assertions.assertNotNull(schema);
        Assertions.assertNotNull(schema.getTitle(), "Every schema has a title!");

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(target, schema), "Even if buffered, the schemas should be identical");
    }

    @Test @Tag("Core") @Order(2)
    @DisplayName("[JSON] Validate from file")
    public void test_validation_from_file() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/scenario/valid.scenario");

        Schema schema = SchemaProvider.loadSchemaFromResources("json/schemas/scenario.schema");
        Assertions.assertNotNull(schema);
        Assertions.assertNotNull(schema.getTitle(), "Every schema has a title!");

        ValidationReport report = Validator.validateObject(json, schema);
        Assertions.assertEquals(iValidType.IS_VALID, report.getIsValid(), "The supplied Document is valid");
        Assertions.assertTrue(report.getReason().isBlank(), "There should be no Messages, as the Document is perfectly valid");
    }

    @Test @Tag("Core") @Order(3)
    @DisplayName("[JSON] Invalidate from file")
    public void test_invalidation_from_file() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/scenario/invalid.scenario");

        ValidationReport report = Validator.validateObject(json, SchemaProvider.loadSchemaFromResources("json/schemas/scenario.schema"));

        Assertions.assertEquals(iValidType.IS_NOT_VALID, report.getIsValid(), "The supplied Document is not valid, but valid json");
        // Assertions.assertEquals(1, report.getMessages().size(), "There should be exactly one Message, as the Document contains exactly one error");
    }


    @Test @Tag("Core") @Order(4)
    @DisplayName("[JSON] No valid Json from file")
    public void test_no_valid_json_from_file() throws Exception {
        // Load the scenario-json-Object
    String json = GameDataGson.loadInternalJson("json/files/general/invalid.json");

        ValidationReport report = Validator.validateObject(json, SchemaProvider.loadSchemaFromResources("json/schemas/scenario.schema")); // Should not matter
        Assertions.assertEquals(iValidType.IS_NOT_JSON, report.getIsValid(), "The supplied Document shouldn't be valid and therefore invalid json");
    }

    @Test @Tag("Core") @Order(5)
    @DisplayName("[JSON] Valid JSON Array")
    public void test_valid_json_array() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/characters/valid.json");

        ValidationReport report = Validator.validateObject(json, SchemaProvider.loadSchemaFromResources("json/schemas/characters.schema"));
        Assertions.assertEquals(iValidType.IS_VALID, report.getIsValid(), "The supplied Document is valid");
        Assertions.assertTrue(report.getReason().isBlank(), "There should be no Messages, as the Document is perfectly valid");
    }

    @Test @Tag("Core") @Order(6)
    @DisplayName("[Error] Capsule no schema present")
    public void test_capsule_no_schema() throws Exception {
        // Load the scenario-json-Object
        String json = GameDataGson.loadInternalJson("json/files/characters/valid.json");

        Assertions.assertThrows(NullPointerException.class, () -> Validator.validateObject(json, null), "Throw on null");
    }

    @Test @Tag("Core") @Order(7)
    @DisplayName("[Error] Handle no ValidationReport-Reasons")
    public void test_no_validation_report() throws Exception {
        ValidationReport report = new ValidationReport(List.of(), iValidType.IS_NOT_JSON);
        Assertions.assertNull(report.getReason(), "Has no reason for: " + report);
    }


    @Test @Tag("Core") @Order(8)
    @DisplayName("[Error] Empty Report construct")
    public void test_empty_report_construct() throws Exception {
        ValidationReport report = new ValidationReport();
        Assertions.assertEquals(iValidType.IS_VALID, report.getIsValid(), "Should be valid");
        Assertions.assertTrue(report.getIsValid().get(), "Should be valid on bool");
        Assertions.assertEquals("", report.getReason(), "Report reasons should be empty for being valid.");
    }
}