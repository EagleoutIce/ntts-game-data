package de.uulm.team020.helper.game;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.exceptions.HomingException;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;

/**
 * Testing the homing guidance :) Everybody wants to it something....
 */
@Tag("Helper")
@Tag("Homing")
public class HomingGuidanceTests extends AbstractHomingTest {

    @Test
    @DisplayName("[Homing] Test simple getter")
    public void test_simpleStateExtractionByGetter() {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, SCENARIO_PATH, 1, new Point(-1, -1), new Point(1, 1));
        // in this case we take characters 1, 2 and 3
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(1).limit(3).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // check before
        Assertions.assertNull(guide.getMap(), "Map should be null as no default");
        Assertions.assertNull(guide.getOperator(), "Operator, should be null as no default");
        Assertions.assertNull(guide.getOperatorPosition(), "Operator position, should be null as no default");

        // update operation
        Character operator = characters.get(1);
        operator.setCoordinates(new Point(2, 3));
        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        // assure data has been set
        Assertions.assertEquals(state.getMap(), guide.getMap(), "Map should be as given in state, no manipulations");
        Assertions.assertEquals(operator, guide.getOperator(), "Operator, should be same as no manipulation");
        Assertions.assertEquals(operator.getCoordinates(), guide.getOperatorPosition(),
                "Operator position, should be same as no manipulation");
    }

    @Test
    @DisplayName("[Homing] Character retrieval")
    public void test_characterRetrieval() {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, SCENARIO_PATH, RandomHelper.rndInt(1, 42), new Point(-1, -1),
                new Point(1, 1));
        // in this case we take characters 1, 2 and 3
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(1).limit(3).map(Character::getCharacterId)
                .collect(Collectors.toList());

        Assertions.assertTrue(guide.getCharacterAtPosition(new Point(0, 0)).isEmpty(),
                "No character at 0, 0 as none set, but: " + guide.getCharacterAtPosition(new Point(0, 0)));

        // update operation
        Character operator = characters.get(1);
        operator.setCoordinates(new Point(2, 3));

        // place some characters :D
        characters.get(0).setCoordinates(new Point(-1, 0));
        characters.get(2).setCoordinates(new Point(4, 5));
        characters.get(3).setCoordinates(new Point(0, 0));
        characters.get(4).setCoordinates(new Point(6, 0));
        characters.get(5).setCoordinates(new Point(0, 9));
        characters.get(6).setCoordinates(new Point(6, 9));
        characters.get(7).setCoordinates(new Point(5, 8));

        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        Assertions.assertEquals(characters.get(0), guide.getCharacterAtPosition(new Point(-1, 0)).get(),
                "Char-Check: 0");
        Assertions.assertEquals(characters.get(1), guide.getCharacterAtPosition(new Point(2, 3)).get(),
                "Char-Check: 1");
        Assertions.assertEquals(characters.get(2), guide.getCharacterAtPosition(new Point(4, 5)).get(),
                "Char-Check: 2");
        Assertions.assertEquals(characters.get(3), guide.getCharacterAtPosition(new Point(0, 0)).get(),
                "Char-Check: 3");
        Assertions.assertEquals(characters.get(4), guide.getCharacterAtPosition(new Point(6, 0)).get(),
                "Char-Check: 4");
        Assertions.assertEquals(characters.get(5), guide.getCharacterAtPosition(new Point(0, 9)).get(),
                "Char-Check: 5");
        Assertions.assertEquals(characters.get(6), guide.getCharacterAtPosition(new Point(6, 9)).get(),
                "Char-Check: 6");
        Assertions.assertEquals(characters.get(7), guide.getCharacterAtPosition(new Point(5, 8)).get(),
                "Char-Check: 7");
        Assertions.assertTrue(guide.getCharacterAtPosition(new Point(9, 8)).isEmpty(),
                "Should be no character at  9/8, but: " + guide.getCharacterAtPosition(new Point(9, 8)));
    }

    @Test
    @DisplayName("[Homing] Get neighbour characters")
    public void test_characterNeighbours() {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/hugeWorld.scenario", 1, new Point(-1, -1),
                new Point(5, 3));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());
        Assertions.assertTrue(guide.getCharacterAtPosition(new Point(0, 0)).isEmpty(),
                "No character at 0, 0 as none set, but: " + guide.getCharacterAtPosition(new Point(0, 0)));

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(new Point(5, 2));

        // place others we place some close
        characters.get(0).setCoordinates(new Point(5, 1)); // close
        characters.get(1).setCoordinates(new Point(4, 1)); // close
        characters.get(2).setCoordinates(new Point(4, 3)); // close, from us
        characters.get(4).setCoordinates(new Point(6, 2)); // close, from us
        characters.get(5).setCoordinates(new Point(2, 9)); // not close, from us
        characters.get(6).setCoordinates(new Point(3, 9)); // not close
        characters.get(7).setCoordinates(new Point(5, 8)); // not close
        characters.get(8).setCoordinates(new Point(7, 4)); // not close

        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        // check for neighbours
        Set<Point> closeOtherPoints = guide.getAllNonFriendlyNeighbourCharacterCoordinates();
        Assertions.assertEquals(2, closeOtherPoints.size(), "Two should be close, but: " + closeOtherPoints);
        Assertions.assertTrue(closeOtherPoints.contains(characters.get(0).getCoordinates()),
                "close others should contain char 0, but: " + closeOtherPoints);
        Assertions.assertTrue(closeOtherPoints.contains(characters.get(1).getCoordinates()),
                "close others should contain char 1, but: " + closeOtherPoints);

        // check close all
        Set<Point> closePoints = guide.getAllNeighbourCharacterCoordinates();
        Assertions.assertEquals(4, closePoints.size(), "Four should be close, but: " + closePoints);
        Assertions.assertTrue(closePoints.contains(characters.get(0).getCoordinates()),
                "close should contain char 0, but: " + closePoints);
        Assertions.assertTrue(closePoints.contains(characters.get(1).getCoordinates()),
                "close should contain char 1, but: " + closePoints);
        Assertions.assertTrue(closePoints.contains(characters.get(2).getCoordinates()),
                "close should contain char 2, but: " + closePoints);
        Assertions.assertTrue(closePoints.contains(characters.get(4).getCoordinates()),
                "close should contain char 4, but: " + closePoints);
    }

    @ParameterizedTest
    @DisplayName("[Homing] Check character has property")
    @CsvSource(value = { "0,true,false", "1,true,false", "2,true,false", "3,true,false", "4,true,false", "5,true,false",
            "6,true,false", "0,false,false", "1,false,false", "2,false,false", "3,false,false", "4,false,false",
            "5,false,false", "6,false,false", "0,true,true", "1,true,true", "2,true,true", "3,true,true", "4,true,true",
            "5,true,true", "6,true,true", "0,false,true", "1,false,true", "2,false,true", "3,false,true",
            "4,false,true", "5,false,true", "6,false,true" })

    // NOTE: fake means that the moledie is applied, but the properties are not
    // removed for any reason
    public void test_characterProperty(int charNum, boolean useMoledie, boolean fakeMoledie)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/hugeWorld.scenario", RandomHelper.rndInt(1, 42),
                new Point(-1, -1), new Point(1, 1));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());
        Assertions.assertTrue(guide.getCharacterAtPosition(new Point(0, 0)).isEmpty(),
                "No character at 0, 0 as none set, but: " + guide.getCharacterAtPosition(new Point(0, 0)));

        // update operation
        Character operator = characters.get(charNum);
        operator.setCoordinates(new Point(RandomHelper.rndInt(0, 10), RandomHelper.rndInt(0, 10)));

        if (fakeMoledie) {
            // set it via reflection as hour game data does not allow this kind of behavior
            Field gadgetList = Character.class.getDeclaredField("gadgets");
            gadgetList.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Gadget> gadgets = (List<Gadget>) gadgetList.get(operator);
            gadgets.add(new Gadget(GadgetEnum.MOLEDIE));
        } else if (useMoledie) {
            operator.getMoledie();
        }

        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        List<PropertyEnum> operatorProperties = operator.getProperties();

        // Those checks base on the character description embedded
        // In this mode character does not have moledie
        for (PropertyEnum property : PropertyEnum.values()) {
            if (operatorProperties.contains(property)
                    // if faked we have to take care
                    && (!fakeMoledie || !Character.MOLEDIE_PROPERTIES.contains(property))) {
                Assertions.assertTrue(guide.operatorHasProperty(property),
                        "Operator which is: " + operator + " should have property: " + property);
            } else {
                Assertions.assertFalse(guide.operatorHasProperty(property),
                        "Operator which is: " + operator + " should not have property: " + property);
            }
        }
    }

    private static Stream<Arguments> generate_catOrJanitor() {
        return Stream.of(Arguments.arguments(new Point(0, 0), new Point(-1, -1), new Point(2, 2), false),
                Arguments.arguments(new Point(1, 2), new Point(-1, -1), new Point(2, 2), false),
                Arguments.arguments(new Point(2, 2), new Point(-1, -1), new Point(2, 2), true),
                Arguments.arguments(new Point(2, 2), new Point(3, 5), new Point(3, 4), false),
                Arguments.arguments(new Point(2, 2), new Point(3, 4), new Point(3, 4), true),
                Arguments.arguments(new Point(2, 2), null, new Point(3, 4), false),
                Arguments.arguments(null, new Point(3, 4), new Point(-1, -1), false),
                Arguments.arguments(null, null, new Point(-1, -1), false),
                Arguments.arguments(new Point(2, 2), null, null, true),
                Arguments.arguments(null, new Point(3, 4), null, true), Arguments.arguments(null, null, null, true));
    }

    @ParameterizedTest
    @DisplayName("[Homing] Cat or Janitor")
    @MethodSource("generate_catOrJanitor")
    public void test_catOrJanitor(Point cat, Point janitor, Point test, boolean isCatOrJanitor) {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/hugeWorld.scenario", 1, janitor, cat);
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(new Point(5, 2));

        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        // default checks
        Assertions.assertEquals(isCatOrJanitor, guide.isCatOrJanitor(test),
                "Should be as stated on test with cat coordinates: " + cat + " janitor coordinates: " + janitor
                        + " and checking: " + test);
    }

    private static Stream<Arguments> generate_pointsInRange() {
        return Stream.of(
                //
                Arguments.arguments(new Point(1, 6), new Point(2, 7), 1, true,
                        Set.of(new Point(1, 5), new Point(2, 5), new Point(2, 6), new Point(2, 7), new Point(0, 5),
                                new Point(1, 6), new Point(0, 6), new Point(1, 7), new Point(0, 7)),
                        Set.of(new Point(3, 6), new Point(2, 6), new Point(3, 7), new Point(2, 7), new Point(3, 8),
                                new Point(1, 6), new Point(2, 8), new Point(1, 7), new Point(1, 8))),
                //
                Arguments.arguments(new Point(1, 6), new Point(2, 7), 1, false,
                        Set.of(new Point(1, 5), new Point(2, 5), new Point(2, 6), new Point(2, 7), new Point(0, 5),
                                new Point(1, 6), new Point(0, 6), new Point(1, 7), new Point(0, 7)),
                        Set.of(new Point(3, 6), new Point(2, 6), new Point(3, 7), new Point(2, 7), new Point(3, 8),
                                new Point(1, 6), new Point(2, 8), new Point(1, 7), new Point(1, 8))),
                //
                Arguments.arguments(new Point(1, 6), new Point(2, 7), 2, true,
                        Set.of(new Point(3, 4), new Point(2, 4), new Point(3, 5), new Point(2, 5), new Point(3, 6),
                                new Point(1, 4), new Point(2, 6), new Point(3, 7), new Point(1, 5), new Point(2, 7),
                                new Point(3, 8), new Point(0, 5), new Point(1, 6), new Point(0, 6), new Point(1, 7),
                                new Point(0, 7)),
                        Set.of(new Point(4, 5), new Point(3, 5), new Point(4, 6), new Point(2, 5), new Point(3, 6),
                                new Point(4, 7), new Point(2, 6), new Point(3, 7), new Point(1, 5), new Point(2, 7),
                                new Point(3, 8), new Point(0, 5), new Point(1, 6), new Point(2, 8), new Point(1, 7),
                                new Point(1, 8))),
                //
                Arguments.arguments(new Point(1, 6), new Point(2, 7), 2, false,
                        Set.of(new Point(3, 4), new Point(2, 4), new Point(3, 5), new Point(2, 5), new Point(3, 6),
                                new Point(1, 4), new Point(2, 6), new Point(3, 7), new Point(0, 4), new Point(1, 5),
                                new Point(2, 7), new Point(3, 8), new Point(0, 5), new Point(1, 6), new Point(2, 8),
                                new Point(0, 6), new Point(1, 7), new Point(0, 7), new Point(1, 8), new Point(0, 8)),
                        Set.of(new Point(4, 5), new Point(3, 5), new Point(4, 6), new Point(2, 5), new Point(3, 6),
                                new Point(4, 7), new Point(2, 6), new Point(3, 7), new Point(4, 8), new Point(1, 5),
                                new Point(2, 7), new Point(3, 8), new Point(0, 5), new Point(1, 6), new Point(2, 8),
                                new Point(0, 6), new Point(1, 7), new Point(0, 7), new Point(1, 8), new Point(0, 8))),
                //
                Arguments.arguments(new Point(3, 5), new Point(3, 6), 1, false,
                        Set.of(new Point(2, 5), new Point(3, 5), new Point(4, 5), new Point(2, 6), new Point(3, 6),
                                new Point(4, 6), new Point(2, 4), new Point(3, 4), new Point(4, 4)),
                        Set.of(new Point(4, 5), new Point(3, 5), new Point(4, 6), new Point(2, 5), new Point(3, 6),
                                new Point(4, 7), new Point(2, 6), new Point(3, 7), new Point(2, 7))));
    }

    @ParameterizedTest
    @DisplayName("[Homing] Points in Range (including update)")
    @MethodSource("generate_pointsInRange")
    public void test_pointsInRange(Point operatorPosition, Point secondOperatorPosition, int range, boolean lineOfSight,
            Set<Point> expected, Set<Point> secondExpected) {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/edge.scenario", 1, new Point(3, 4),
                new Point(1, 1));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        Character operator = characters.get(3);
        operator.setCoordinates(new Point(operatorPosition));

        // operator will be the first character
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        Set<Point> got = guide.getPointsInRange(operatorPosition, range, lineOfSight);
        Assertions.assertEquals(expected, got, "Should be as expected for operator pos: " + operatorPosition
                + ", range: " + range + " lineOfSight: " + lineOfSight);

        operator = characters.get(4);
        operator.setCoordinates(new Point(secondOperatorPosition));
        Set<Point> got2 = guide.getPointsInRange(secondOperatorPosition, range, lineOfSight);
        Assertions.assertEquals(secondExpected, got2, "Second; Should be as expected for second operator pos: "
                + secondOperatorPosition + ", range: " + range + " lineOfSight: " + lineOfSight);

        // should be same if byop

        got = guide.getPointsInRange(range, lineOfSight);
        Assertions.assertEquals(expected, got, "Dir, Should be as expected for operator pos: " + operatorPosition
                + ", range: " + range + " lineOfSight: " + lineOfSight);

        operator = characters.get(4);
        operator.setCoordinates(new Point(secondOperatorPosition));
        guide.updateOperation(state, getMatchconfig(MATCHCONFIG_PATH), operator, us);

        got2 = guide.getPointsInRange(range, lineOfSight);
        Assertions.assertEquals(secondExpected, got2, "Dir, Second; Should be as expected for second operator pos: "
                + secondOperatorPosition + ", range: " + range + " lineOfSight: " + lineOfSight);

    }

    @ParameterizedTest
    @DisplayName("[Homing] Invalid operator configuration on update")
    @CsvSource(value = { "(-1,-1)", "(-3,-4)", "(19,95)", "(42,-42)", "(5,4)" }, delimiter = ':')
    public void test_targetUpdateThrow(final String pointCode) throws PointParseException {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/edge.scenario", 1, new Point(-1, -1),
                new Point(5, 3));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(Point.fromString(pointCode));

        final Matchconfig matchconfig = getMatchconfig(MATCHCONFIG_PATH);
        // operator will be the first character
        Assertions.assertThrows(HomingException.class, () -> guide.updateOperation(state, matchconfig, operator, us),
                "Should throw homing exception as operator is not on field: " + operator);
    }

    @Test
    @DisplayName("[Homing] Operator is on foggy field ")
    public void test_operatorOnFoggy() throws PointParseException {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/edge.scenario", 1, new Point(-1, -1),
                new Point(5, 3));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(new Point(3, 2));
        state.getMap().getSpecificField(operator.getCoordinates()).setFoggy(true);

        final Matchconfig matchconfig = getMatchconfig(MATCHCONFIG_PATH);
        // operator will be the first character
        Assertions.assertDoesNotThrow(() -> guide.updateOperation(state, matchconfig, operator, us),
                "Should not throw anything as valid on foggy: " + operator);

    }

    @Test
    @DisplayName("[Homing] Observable targets")
    public void test_observableTargets() throws PointParseException {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/circles.scenario", RandomHelper.rndInt(1, 42),
                new Point(-1, -1), new Point(5, 3));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(new Point(1, 14));

        characters.get(0).setCoordinates(new Point(2, 14)); // is
        characters.get(1).setCoordinates(new Point(0, 12)); // is
        characters.get(2).setCoordinates(new Point(1, 12)); // from us, not
        characters.get(4).setCoordinates(new Point(2, 13)); // from us, not
        characters.get(5).setCoordinates(new Point(2, 9)); // from us, not
        characters.get(6).setCoordinates(new Point(3, 12)); // is

        final Matchconfig matchconfig = getMatchconfig(MATCHCONFIG_PATH);
        guide.updateOperation(state, matchconfig, operator, us);

        Set<Point> possibleTargets = guide.getTargetsForObservation();
        Assertions.assertEquals(3, possibleTargets.size(), "Should be 3 with: " + possibleTargets);
        Assertions.assertTrue(possibleTargets.contains(characters.get(0).getCoordinates()),
                "char 0 should be in: " + characters.get(0));
        Assertions.assertTrue(possibleTargets.contains(characters.get(1).getCoordinates()),
                "char 1 should be in: " + characters.get(1));
        Assertions.assertTrue(possibleTargets.contains(characters.get(6).getCoordinates()),
                "char 6 should be in: " + characters.get(6));

        // update operator to 2/9
        operator = characters.get(5);
        guide.updateOperation(state, matchconfig, operator, us);
        possibleTargets = guide.getTargetsForObservation();
        Assertions.assertEquals(0, possibleTargets.size(),
                "Should be no valid target for: " + possibleTargets + " on op 5");

    }

    @Test
    @DisplayName("[Homing] Get Bangs and burns")
    public void test_bangsAndBurns() throws PointParseException {
        // build example Guidance
        HomingGuidance guide = new HomingGuidance();
        // generate state data
        State state = getMeAState(CHARACTERS_PATH, "json/files/scenario/circles.scenario", RandomHelper.rndInt(1, 42),
                new Point(-1, -1), new Point(5, 3));
        // in this case we take characters 2, 3 and 4 and 5
        List<Character> characters = new ArrayList<Character>(state.getCharacters());
        List<UUID> us = characters.stream().skip(2).limit(4).map(Character::getCharacterId)
                .collect(Collectors.toList());

        // update operation
        Character operator = characters.get(3);
        operator.setCoordinates(new Point(1, 6)); // may gamble on the left and may gamble on top

        characters.get(0).setCoordinates(new Point(2, 14)); // is
        characters.get(1).setCoordinates(new Point(0, 12)); // is
        characters.get(2).setCoordinates(new Point(1, 12)); // from us, not
        characters.get(4).setCoordinates(new Point(2, 13)); // from us, not
        characters.get(5).setCoordinates(new Point(2, 9)); // from us, not
        characters.get(6).setCoordinates(new Point(3, 12)); // is

        final Matchconfig matchconfig = getMatchconfig(MATCHCONFIG_PATH);
        guide.updateOperation(state, matchconfig, operator, us);

        Set<Point> possibleTargets = guide.getTargetsForBangAndBurn();
        Assertions.assertEquals(2, possibleTargets.size(), "Should be 2 with: " + possibleTargets);
        Assertions.assertTrue(possibleTargets.contains(new Point(1, 5)),
                "upper roulette should be there on 1/5 but isn't");
        Assertions.assertTrue(possibleTargets.contains(new Point(0, 6)),
                "left roulette should be there on 0/6 but isn't");

        // new operator

        // update operator to 2/9
        operator = characters.get(5);
        guide.updateOperation(state, matchconfig, operator, us);
        possibleTargets = guide.getTargetsForBangAndBurn();
        Assertions.assertEquals(0, possibleTargets.size(), "Should be 0 with: " + possibleTargets);

        // return to old but destroy one
        operator = characters.get(3);
        guide.updateOperation(state, matchconfig, operator, us);
        possibleTargets = guide.getTargetsForBangAndBurn();
        Assertions.assertEquals(2, possibleTargets.size(), "Should be 2 with: " + possibleTargets);
        Assertions.assertTrue(possibleTargets.contains(new Point(1, 5)),
                "upper roulette should be there on 1/5 but isn't");
        Assertions.assertTrue(possibleTargets.contains(new Point(0, 6)),
                "left roulette should be there on 0/6 but isn't");

        state.getMap().getSpecificField(new Point(1, 5)).setDestroyed(true);
        possibleTargets = guide.getTargetsForBangAndBurn();
        Assertions.assertEquals(1, possibleTargets.size(), "Should be 1 as one destroyed with: " + possibleTargets);
        Assertions.assertTrue(possibleTargets.contains(new Point(0, 6)),
                "left roulette should be there on 0/6 but isn't");
    }

    // TODO: use test config with 0 range
    // TODO: other tests :D

}
