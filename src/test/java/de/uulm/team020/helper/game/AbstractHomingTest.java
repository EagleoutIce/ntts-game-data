package de.uulm.team020.helper.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.validation.GameDataGson;

public abstract class AbstractHomingTest {

    protected static final String MATCHCONFIG_PATH = "json/files/matchconfig/valid.match";
    protected static final String SCENARIO_PATH = "json/files/scenario/edge.scenario";
    protected static final String CHARACTERS_PATH = "json/files/characters/sample.json";

    // we want a matchconfig for most tests we may manipulate it via reflection if
    // necessary :D

    protected static Matchconfig getMatchconfig(final String path) {
        try {
            return GameDataGson.fromJson(GameDataGson.loadInternalJson(path), Matchconfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // end all tests
        }
    }

    protected static Scenario getScenario(final String path) {
        try {
            return GameDataGson.fromJson(GameDataGson.loadInternalJson(path), Scenario.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // end all tests
        }
    }

    protected static CharacterDescription[] getCharacters(final String path) {
        try {
            return GameDataGson.fromJson(GameDataGson.loadInternalJson(path), CharacterDescription[].class);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // end all tests
        }
    }

    /**
     * Returns all characters populated by this path
     * 
     * @param path The path to populate from
     * 
     * @return character-set
     */
    protected static Set<Character> getMeCharacters(String path) {
        CharacterDescription[] charsToPopulate = getCharacters(path);
        Set<Character> allCharacters = new HashSet<>(charsToPopulate.length);
        for (CharacterDescription characterDescription : charsToPopulate) {
            allCharacters.add(new Character(new CharacterInformation(characterDescription, UUID.randomUUID()),
                    new ArrayList<>()));
        }
        return allCharacters;
    }

    /**
     * Returns field map based on given scenario
     * 
     * @param path The path to populate from
     * 
     * @return field-map
     */
    protected static FieldMap getMeFieldMap(String path) {
        Scenario scenario = getScenario(path);
        FieldStateEnum[][] raw = scenario.getField();
        Field[][] fields = new Field[raw.length][];
        int safeCounter = 1;
        for (int y = 0; y < raw.length; y++) {
            final int width = raw[y].length;
            fields[y] = new Field[width];
            for (int x = 0; x < width; x++) {
                final FieldStateEnum fieldState = raw[y][x];
                if (fieldState == FieldStateEnum.SAFE) {
                    fields[y][x] = new Field(safeCounter);
                    safeCounter += 1;
                } else {
                    fields[y][x] = new Field(fieldState);
                }
            }
        }
        return new FieldMap(fields);
    }

    /**
     * Get yourself a state
     * 
     * @param characterPath Path to load characters from
     * @param scenarioPath  path to load scenario from
     * @param roundNumber   rounds into game
     * @param janitor       janitor pos
     * @param cat           cat pos
     * @return State build based on effects, safe numbers will be deterministic!
     */
    protected static State getMeAState(String characterPath, String scenarioPath, int roundNumber, Point janitor,
            Point cat) {
        return new State(roundNumber, getMeFieldMap(scenarioPath), new HashSet<>(), getMeCharacters(characterPath), cat,
                janitor);
    }

}