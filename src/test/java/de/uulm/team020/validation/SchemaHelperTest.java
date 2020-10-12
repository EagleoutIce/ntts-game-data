package de.uulm.team020.validation;

import org.everit.json.schema.Schema;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.stream.Stream;
/**
 * Validates the equal-implementation of the {@link SchemaHelper}. It would be
 * somewhat stupid if it had flaws...
 * 
 * @author Florian Sihler
 * @version 1.0, 03/27/2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class SchemaHelperTest {


    @Test @Tag("Util") @Order(1)
    @DisplayName("[Helper] null is still equal")
    public void test_null_schemas()  {
        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(null, null), "null is equal to null");
    }

        
    @ParameterizedTest @Tag("Util") @Order(2)
    @DisplayName("[Helper] compare schemas with null")
    @ValueSource(strings = {
        "json/schemas/scenario.schema",
        "json/schemas/matchconfig.schema",
        "json/schemas/characters.schema",
    })
    public void test_schemas_with_null(String path) {
        Schema schema = SchemaProvider.loadSchemaFromResources(path);
        Assertions.assertFalse(SchemaHelper.jsonSchemaEquals(schema, null), "schema is not null");
        Assertions.assertFalse(SchemaHelper.jsonSchemaEquals(null, schema), "schema is not null");
    }


    @ParameterizedTest @Tag("Util") @Order(3)
    @DisplayName("[Helper] compare identical schemas")
    @ValueSource(strings = {
        "json/schemas/scenario.schema",
        "json/schemas/matchconfig.schema",
        "json/schemas/characters.schema",
    })
    public void test_identical_schemas(String path) {
        Schema schema = SchemaProvider.loadSchemaFromResources(path);
        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(schema, schema), "identical schemas should be equal");
    }

    @ParameterizedTest @Tag("Util") @Order(4)
    @DisplayName("[Helper] compare schemas with same order")
    @ValueSource(strings = {
        "json/schemas/scenario.schema",
        "json/schemas/matchconfig.schema",
        "json/schemas/characters.schema",
    })
    public void test_same_schemas(String path) {
        Schema aschema = SchemaProvider.loadSchemaFromResources(path);
        Schema bschema = SchemaProvider.loadSchemaFromResources(path);

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(aschema, bschema), "equal schemas should be equal ;)");
    }

    @ParameterizedTest @Tag("Util") @Order(5)
    @DisplayName("[Helper] compare schemas with different order")
    @MethodSource("generate_alter_schema_paths")
    public void test_diff_schemas(String path, String alterPath) {
        Schema aschema = SchemaProvider.loadSchemaFromResources(path);
        Schema bschema = SchemaProvider.loadSchemaFromResources(alterPath);

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(aschema, bschema), "the schemas " + path + " and " + alterPath + " should be equal ;)");
    }

    private static Stream<Arguments> generate_alter_schema_paths() {
        return Stream.of(
            Arguments.of("json/schemas/scenario.schema","json/schemas/alter_scenario.schema"),
            Arguments.of("json/schemas/matchconfig.schema","json/schemas/alter_matchconfig.schema"),
            Arguments.of("json/schemas/characters.schema","json/schemas/alter_characters.schema")
        );
    }

    @ParameterizedTest @Tag("Util") @Order(6)
    @DisplayName("[Helper] compare schemas which differ")
    @MethodSource("generate_diff_schema_paths")
    public void test_alter_schemas(String path, String diffPath) {
        Schema aschema = SchemaProvider.loadSchemaFromResources(path);
        Schema bschema = SchemaProvider.loadSchemaFromResources(diffPath);

        Assertions.assertFalse(SchemaHelper.jsonSchemaEquals(aschema, bschema), "the schemas should not be equal as they differ...");
    }

    private static Stream<Arguments> generate_diff_schema_paths() {
        return Stream.of(
            // differ rotation
            Arguments.of("json/schemas/scenario.schema","json/schemas/alter_matchconfig.schema"),
            Arguments.of("json/schemas/matchconfig.schema","json/schemas/alter_characters.schema"),
            Arguments.of("json/schemas/characters.schema","json/schemas/alter_scenario.schema"),
            // special diff
            Arguments.of("json/schemas/matchconfig.schema","json/schemas/differ1_characters.schema"),
            Arguments.of("json/schemas/matchconfig.schema","json/schemas/differ2_characters.schema"),
            Arguments.of("json/schemas/matchconfig.schema","json/schemas/differ3_characters.schema"),
            Arguments.of("json/schemas/alter_matchconfig.schema","json/schemas/differ1_characters.schema"),
            Arguments.of("json/schemas/alter_matchconfig.schema","json/schemas/differ2_characters.schema"),
            Arguments.of("json/schemas/alter_matchconfig.schema","json/schemas/differ3_characters.schema"),
            // arr sep
            Arguments.of("json/schemas/scenario.schema","json/schemas/differ1_scenario.schema"),
            // cross diff
            Arguments.of("json/schemas/scenario.schema","json/schemas/differ1_characters.schema"),
            Arguments.of("json/schemas/scenario.schema","json/schemas/differ2_characters.schema"),
            Arguments.of("json/schemas/scenario.schema","json/schemas/differ3_characters.schema")
        );
    }

    @ParameterizedTest @Tag("Util") @Order(7)
    @DisplayName("[Helper] compare schemas with different order loaded from string")
    @MethodSource("generate_alter_schema_paths")
    public void test_loaded_from_string(String path, String alterPath) throws IOException {
        Schema aschema = SchemaProvider.loadSchemaFromString(GameDataGson.loadInternalJson(path));
        Schema bschema = SchemaProvider.loadSchemaFromString(GameDataGson.loadInternalJson(alterPath));

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(aschema, bschema), "the schemas " + path + " and " + alterPath + " should be equal ;)");
    }

    @Test @Tag("Util") @Order(7)
    @DisplayName("[Error] Load from invalid path")
    public void test_loaded_from_invalid_path() throws IOException {
        Assertions.assertThrows(NullPointerException.class, () -> SchemaProvider.loadSchemaFromResources("./i/do/no/exist.filepath"), "Should be npe as non-existing");
    }
}