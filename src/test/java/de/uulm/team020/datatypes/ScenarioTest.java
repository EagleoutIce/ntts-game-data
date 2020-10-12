package de.uulm.team020.datatypes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.validation.GameDataGson;
import de.uulm.team020.validation.SchemaProvider;
import de.uulm.team020.validation.Validator;

/**
 * Tests {@link de.uulm.team020.datatypes.Scenario} and
 * {@link de.uulm.team020.datatypes.blueprints.AbstractGameField} for
 * functionality.
 * 
 * @author Florian Sihler
 * @version 1.1, 06/19/2020
 */
public class ScenarioTest {

    @Test
    @Tag("Core")
    @Order(1)
    @DisplayName("[Scenario] Satisfies standard, de-serialization")
    public void test_scenarioSatisfiesStandard() {
        // Create an example field:
        FieldStateEnum[][] field = new FieldStateEnum[][] {
                new FieldStateEnum[] { FieldStateEnum.FREE, FieldStateEnum.FIREPLACE },
                new FieldStateEnum[] { FieldStateEnum.FREE, FieldStateEnum.FREE } };
        // Create an example scenario
        Scenario scenario = new Scenario(field);

        // Generate json-data
        String json = GameDataGson.toJson(scenario);

        // Validate against schema
        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");
    }

    @Test
    @Tag("Core")
    @Order(2)
    @DisplayName("[Scenario] Satisfies standard, serialization")
    public void test_scenarioSatisfiesStandardParse() {
        // Example json data
        String json = "{\"scenario\":[[\"FREE\",\"FIREPLACE\"],[\"FREE\",\"FREE\"]]}";
        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);

        // Dimensions have to be correct
        Assertions.assertEquals(2, scenario.getMinWidth(), "MinWidth: Scenario is 2x2");
        Assertions.assertEquals(2, scenario.getMaxWidth(), "MaxWidth: Scenario is 2x2");
        Assertions.assertEquals(2, scenario.getMinHeight(), "MinHeight: Scenario is 2x2");
        Assertions.assertEquals(2, scenario.getMaxHeight(), "MaxHeight: Scenario is 2x2");
    }

    @Test
    @Tag("Core")
    @Order(3)
    @DisplayName("[AbstractGameField] Width/Height Calculation")
    public void test_widthHeightCalculation() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/edge.scenario");

        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);

        // Dimensions have to be correct
        Assertions.assertEquals(5, scenario.getMinWidth(), "MinWidth: Scenario is 7x9");
        Assertions.assertEquals(7, scenario.getMaxWidth(), "MaxWidth: Scenario is 7x9");
        Assertions.assertEquals(3, scenario.getMinHeight(), "MinHeight: Scenario is 7x9");
        Assertions.assertEquals(9, scenario.getMaxHeight(), "MaxHeight: Scenario is 7x9");
    }

    @Test
    @Tag("Core")
    @Order(4)
    @DisplayName("[AbstractGameField] Width/Height Calculation on splitt-Maps")
    public void test_splitWidthHeightCalculation() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/testminimums.scenario");

        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);
        // Dimensions have to be correct
        Assertions.assertEquals(2, scenario.getMinWidth(), "MinWidth: Scenario is 3x5");
        Assertions.assertEquals(3, scenario.getMaxWidth(), "MaxWidth: Scenario is 3x5");
        Assertions.assertEquals(1, scenario.getMinHeight(), "MinHeight: Scenario is 3x5");
        Assertions.assertEquals(5, scenario.getMaxHeight(), "MaxHeight: Scenario is 3x5");
    }

    @Test
    @Tag("Core")
    @Order(5)
    @DisplayName("[AbstractGameField] Width/Height Calculation on splitt-Maps with island")
    public void test_splitWidthHeightCalculationIsles() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/thepits.scenario");

        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);

        // Dimensions have to be correct
        Assertions.assertEquals(0, scenario.getMinWidth(), "MinWidth: Scenario is 7x8");
        Assertions.assertEquals(7, scenario.getMaxWidth(), "MaxWidth: Scenario is 7x8");
        Assertions.assertEquals(3, scenario.getMinHeight(), "MinHeight: Scenario is 7x8");
        Assertions.assertEquals(8, scenario.getMaxHeight(), "MaxHeight: Scenario is 7x8");
    }

    @Test
    @Tag("Core")
    @Order(6)
    @DisplayName("[AbstractGameField] Width/Height Calculation on lonely last lines")
    public void test_splitWidthHeightCalculationLastLines() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/edgeminimum.scenario");

        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);
        // Dimensions have to be correct
        Assertions.assertEquals(5, scenario.getMinWidth(), "MinWidth: Scenario is 7x6");
        Assertions.assertEquals(7, scenario.getMaxWidth(), "MaxWidth: Scenario is 7x6");
        Assertions.assertEquals(4, scenario.getMinHeight(), "MinHeight: Scenario is 7x6");
        Assertions.assertEquals(6, scenario.getMaxHeight(), "MaxHeight: Scenario is 7x6");
    }

    @Test
    @Tag("Core")
    @Order(7)
    @DisplayName("[AbstractGameField] Width/Height Calculation on short firsts")
    public void test_splitWidthHeightCalculationShortFirst() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/thesword.scenario");

        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);
        // Dimensions have to be correct
        Assertions.assertEquals(2, scenario.getMinWidth(), "MinWidth: Scenario is 5x8");
        Assertions.assertEquals(8, scenario.getMaxWidth(), "MaxWidth: Scenario is 5x8");
        Assertions.assertEquals(1, scenario.getMinHeight(), "MinHeight: Scenario is 5x8");
        Assertions.assertEquals(5, scenario.getMaxHeight(), "MaxHeight: Scenario is 5x8");
    }

    @Test
    @Tag("Core")
    @Order(8)
    @DisplayName("[AbstractGameField] Width/Height Calculation on dirty json files")
    public void test_widthHeightCalculationDirty() throws Exception {
        String json = GameDataGson.loadInternalJson("json/files/scenario/dirty.scenario");

        // It has sadly to be valid against the current standardisation
        Assertions.assertTrue(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should uphold to the standard");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);
        // Dimensions have to be correct
        Assertions.assertEquals(5, scenario.getMinWidth(), "MinWidth: Scenario is 7x9");
        Assertions.assertEquals(7, scenario.getMaxWidth(), "MaxWidth: Scenario is 7x9");
        Assertions.assertEquals(3, scenario.getMinHeight(), "MinHeight: Scenario is 7x9");
        Assertions.assertEquals(9, scenario.getMaxHeight(), "MaxHeight: Scenario is 7x9");
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(9)
    @DisplayName("[AbstractGameField] Test HashCodes and Equals")
    @MethodSource("hashing_tests")
    public void test_hashingEq(final String pathA, final String pathB, boolean shouldEq) throws Exception {
        String jsonA = GameDataGson.loadInternalJson(pathA);
        Assertions.assertTrue(Validator.validateObject(jsonA, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "The a-scenario should uphold to the standard");

        String jsonB = GameDataGson.loadInternalJson(pathB);
        Assertions.assertTrue(Validator.validateObject(jsonB, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "The b-scenario should uphold to the standard");

        Scenario scenarioA = GameDataGson.fromJson(jsonA, Scenario.class);
        Scenario scenarioB = GameDataGson.fromJson(jsonB, Scenario.class);

        Assertions.assertNotNull(scenarioA, "Cannot be Null, otherwise loading/casting failed");
        Assertions.assertNotNull(scenarioB, "Cannot be Null, otherwise loading/casting failed");

        Assertions.assertEquals(shouldEq, scenarioA.hashCode() == scenarioB.hashCode(),
                "Hashes should be " + (shouldEq ? "same" : "different"));

        Assertions.assertEquals(shouldEq, scenarioA.equals(scenarioB),
                "Should be " + (shouldEq ? "equal" : "not equal"));
    }

    private static Stream<Arguments> hashing_tests() {
        return Stream.of(
                Arguments.arguments("json/files/scenario/thepits.scenario", "json/files/scenario/thesword.scenario",
                        false),
                Arguments.arguments("json/files/scenario/thepits.scenario", "json/files/scenario/thepits.scenario",
                        true),
                Arguments.arguments("json/files/scenario/thesword.scenario", "json/files/scenario/edgeminimum.scenario",
                        false));
    }

    @Test
    @Tag("Core")
    @Order(8)
    @DisplayName("[AbstractGameField] Width/Height Calculation on empty field")
    public void test_widthHeightEmpty() {
        // Create an example field, which is empty
        FieldStateEnum[][] field = new FieldStateEnum[][] {};
        // Create an example scenario
        Scenario scenario = new Scenario(field);

        // Generate json-data
        String json = GameDataGson.toJson(scenario);

        // Validate against schema
        Assertions.assertFalse(Validator.validateObject(json, SchemaProvider.SCENARIO_SCHEMA).isValid(),
                "the scenario should not uphold to the standard, as it misses lines");

        // Dimensions have to be correct
        Assertions.assertEquals(0, scenario.getMinWidth(), "MinWidth: Scenario is empty");
        Assertions.assertEquals(0, scenario.getMaxWidth(), "MaxWidth: Scenario is empty");
        Assertions.assertEquals(0, scenario.getMinHeight(), "MinHeight: Scenario is empty");
        Assertions.assertEquals(0, scenario.getMaxHeight(), "MaxHeight: Scenario is empty");
    }

    private static boolean listCompare(List<?> a, List<?> b) {
        return Objects.equals(a, b) || (a.size() == b.size() && a.containsAll(b) && b.containsAll(a));
    }

    @Test
    @Tag("Core")
    @Order(9)
    @DisplayName("[AbstractGameField] Neighbour Fields of specific Field on field ")
    public void test_neighbourFieldsOfSpecific() throws IOException {
        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/scenario/edge.scenario"),
                Scenario.class);
        // get neighbours of 0,0
        Optional<List<FieldStateEnum>> simpleNeighbours = scenario.getNeighbourFieldsOfSpecificField(new Point(0, 0));
        Assertions.assertTrue(simpleNeighbours.isPresent(), "Has neighbours for: " + simpleNeighbours);
        List<FieldStateEnum> got = simpleNeighbours.get();
        List<FieldStateEnum> expected = List.of(FieldStateEnum.WALL, FieldStateEnum.WALL, FieldStateEnum.FIREPLACE);
        Assertions.assertTrue(listCompare(expected, got), "Should be as wanted: " + expected + " but got: " + got);
        // null coordinate
        Assertions.assertTrue(scenario.getNeighbourFieldsOfSpecificField(null).isEmpty(), "No fields for null");
        Assertions.assertTrue(scenario.getNeighbourFieldsOfSpecificField(new Point(-1, -1)).isEmpty(),
                "No fields for invalid");

        // get neighbours of 3,5
        simpleNeighbours = scenario.getNeighbourFieldsOfSpecificField(new Point(3, 5));
        Assertions.assertTrue(simpleNeighbours.isPresent(), "Has neighbours for: " + simpleNeighbours);
        got = simpleNeighbours.get();
        expected = List.of(FieldStateEnum.FREE, FieldStateEnum.FREE, FieldStateEnum.FREE, FieldStateEnum.FREE,
                FieldStateEnum.WALL, FieldStateEnum.WALL, FieldStateEnum.WALL, FieldStateEnum.WALL);
        Assertions.assertTrue(listCompare(expected, got),
                "Should be as wanted on 3,4: " + expected + " but was: " + got);
    }

    @Test
    @Tag("Core")
    @Order(10)
    @DisplayName("[AbstractGameField] Neighbours of specific Field on field ")
    public void test_neighboursOfSpecific() throws IOException {
        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/scenario/edge.scenario"),
                Scenario.class);
        // get neighbours of 0,0
        Optional<Set<Point>> simpleNeighbours = scenario.getNeighboursOfSpecificField(new Point(0, 0));
        Assertions.assertTrue(simpleNeighbours.isPresent(), "Has neighbours for: " + simpleNeighbours);
        Set<Point> got = simpleNeighbours.get();
        Set<Point> expected = Set.of(new Point(1, 0), new Point(0, 1), new Point(1, 1));
        Assertions.assertEquals(expected, got, "Should be as wanted: " + expected);
        // null coordinate
        Assertions.assertTrue(scenario.getNeighboursOfSpecificField(null).isEmpty(), "No coordinates for null");
        Assertions.assertTrue(scenario.getNeighboursOfSpecificField(new Point(-1, -1)).isEmpty(),
                "No coordinates for invalid");

        // get neighbours of 3,5
        simpleNeighbours = scenario.getNeighboursOfSpecificField(new Point(3, 5));
        Assertions.assertTrue(simpleNeighbours.isPresent(), "Has neighbours for: " + simpleNeighbours);
        got = simpleNeighbours.get();
        expected = Set.of(new Point(2, 4), new Point(3, 4), new Point(4, 4), new Point(2, 6), new Point(3, 6),
                new Point(4, 6), new Point(2, 5), new Point(4, 5));
        Assertions.assertEquals(expected, got, "Should be as wanted on 3,5: " + expected);

    }

    @ParameterizedTest
    @Tag("Core")
    @Order(11)
    @DisplayName("[AbstractGameField] Is in Map")
    @CsvSource(value = { "(0,0):true:SAFE", "(1,0):true:FREE", "(-1,0):false:DUMMY", "(-1,-2):false:DUMMY",
            "(12,42):false:DUMMY", "(-5,4):false:DUMMY", "(5,4):false:DUMMY", "(5,3):true:FIREPLACE" }, delimiter = ':')
    public void test_isInMap(String point, boolean onField, String newField) throws IOException, PointParseException {
        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("json/files/scenario/edge.scenario"),
                Scenario.class);
        final Point pt = Point.fromString(point);
        final FieldStateEnum old = scenario.getSpecificField(pt);
        Assertions.assertEquals(onField, old != null,
                "got for point: " + point + " differs on scenario: " + scenario.getSpecificField(pt));
        if (onField) {
            FieldStateEnum newState = FieldStateEnum.valueOf(newField);
            Assertions.assertTrue(scenario.setSpecificField(pt, newState), "Should be ad-able for: " + pt);
            // assert correct
            Assertions.assertEquals(newState, scenario.getSpecificField(pt), "Should have changed");
        } else {
            Assertions.assertFalse(scenario.setSpecificField(pt, FieldStateEnum.FREE),
                    "Should not be ad-able for: " + pt);
            // should not be changed
            Assertions.assertEquals(old, scenario.getSpecificField(pt), "Should be same as old as not changed");
        }

    }

}