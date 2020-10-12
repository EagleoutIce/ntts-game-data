package de.uulm.team020.validation;

import org.everit.json.schema.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test functionality of {@link de.uulm.team020.validation.SchemaProvider}.
 * <p>
 * As we only can check for the correct file paths, this Test is rather an
 * ensuring-addon, the main 'testing' routine is nested within
 * {@link ValidatorTest}.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/18/2020
 */
public class SchemaProviderTest {

    @Test @Tag("Provider") @Order(1)
    @DisplayName("[RESOURCE] Scenario schema accessability")
    public void test_scenario_schema_accessability() {
        Schema scenario = SchemaProvider.loadSchemaFromResources("json/schemas/scenario.schema");
        Schema target = SchemaProvider.SCENARIO_SCHEMA;

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(target, scenario), "Schema should be as requested" );
    }

    @Test @Tag("Provider") @Order(2)
    @DisplayName("[RESOURCE] Matchconfig schema accessability")
    public void test_matchconfig_schema_accessability() {
        Schema matchconfig = SchemaProvider.loadSchemaFromResources("json/schemas/matchconfig.schema");
        Schema target = SchemaProvider.MATCHCONFIG_SCHEMA;

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(target, matchconfig), "Schema should be as requested" );
    }

    @Test @Tag("Provider") @Order(3)
    @DisplayName("[RESOURCE] Characters schema accessability")
    public void test_characters_schema_accessability() {
        Schema characters = SchemaProvider.loadSchemaFromResources("json/schemas/characters.schema");
        Schema target = SchemaProvider.CHARACTERS_SCHEMA;

        Assertions.assertTrue(SchemaHelper.jsonSchemaEquals(target, characters), "Schema should be as requested" );
    }
}