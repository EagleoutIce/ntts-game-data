package de.uulm.team020.networking.messages;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * All MetaKeys, viable for the {@link RequestMetaInformationMessage}.
 *
 * @author Florian Sihler
 * @version 1.1, 04/19/2020
 */
public enum MetaKeyEnum {
    /* Number of Spectators currently registered */
    SPECTATOR_COUNT("Spectator.Count", false, Integer.class),
    /* Names of Spectators currently registered */
    SPECTATOR_NAMES("Spectator.Names", false, String[].class),
    /* Number of Players registered */
    PLAYER_COUNT("Player.Count", false, Integer.class),
    /* Names of Players registered */
    PLAYER_NAMES("Player.Names", false, String[].class),
    /* The loaded Scenario */
    CONFIGURATION_SCENARIO("Configuration.Scenario", true, Scenario.class),
    /* The loaded Matchconfig */
    CONFIGURATION_MATCHCONFIG("Configuration.Matchconfig", true, Matchconfig.class),
    /* The loaded character-information, including uuids */
    CONFIGURATION_CHARACTER_INFORMATION("Configuration.CharacterInformation", true, CharacterInformation[].class),
    /* Remaining Time of the shortest pause in seconds */
    GAME_REMAINING_PAUSE_TIME("Game.RemainingPauseTime", false, Integer.class),
    /* Members of the P1-Faction, null if P2 asks */
    FACTION_PLAYER1("Faction.Player1", true, UUID[].class),
    /* Members of the P2-Faction, null if P1 asks */
    FACTION_PLAYER2("Faction.Player2", true, UUID[].class),
    /* Members of the Neutral-Faction, null if no Spectator asks */
    FACTION_NEUTRAL("Faction.Neutral", true, UUID[].class),
    /*
     * Gadgets of the P1-Faction, null if P2 asks, mustn't be valid after game start
     */
    GADGETS_PLAYER1("Gadgets.Player1", true, GadgetEnum[].class),
    /*
     * Gadgets of the P2-Faction, null if P1 asks, mustn't be valid after game start
     */
    GADGETS_PLAYER2("Gadgets.Player2", true, GadgetEnum[].class),
    /*
     * Enforces the Server to perform a server-dump. Probably only supported by the
     * t020 server.
     */
    AUTHOR_DUMP("Author.Dump", false, String.class);

    private String key;
    private boolean mandatory;
    private Class<?> expectedType;

    MetaKeyEnum(String key, boolean mandatory, Class<?> expectedType) {
        this.key = key;
        this.mandatory = mandatory;
        this.expectedType = expectedType;
    }

    /**
     * @return The string constant to be used in an
     *         {@link RequestMetaInformationMessage}
     */
    public String getKey() {
        return key;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    /**
     * Gets the MetaKey conforming to the string value, null otherwise
     * 
     * @param key the desired key
     * @return Null if not found, else the enum-constant
     */
    public static MetaKeyEnum getMetaKey(String key) {
        for (MetaKeyEnum metaKey : MetaKeyEnum.values()) {
            if (Objects.equals(metaKey.getKey(), key))
                return metaKey;
        }
        return null;
    }

    /**
     * Convenience Function to convert the Enums to a String array to be used with
     * an {@link RequestMetaInformationMessage}.
     *
     * @param keys the keys you want
     * @return the keys to be decoded - just to make alternatives possible
     */
    public static String[] toStringArray(MetaKeyEnum[] keys) {
        return Arrays.stream(keys).map(MetaKeyEnum::getKey).toArray(String[]::new);
    }

}