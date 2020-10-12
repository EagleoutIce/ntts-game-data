package de.uulm.team020.datatypes;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import de.uulm.team020.datatypes.util.Point;

/**
 * Snapshot of the current game-state. Contains the {@link FieldMap}, all active
 * Characters, and the safe-combinations owned by the target team...
 *
 * @author Florian Sihler
 * @version 1.0, 03/29/2020
 */
public class State implements IAmJson {

    private static final long serialVersionUID = 6455115045183255965L;

    private Integer currentRound;

    private FieldMap map;

    private Set<Integer> mySafeCombinations;

    private Set<Character> characters;

    Point catCoordinates;
    Point janitorCoordinates;

    /**
     * Initializes a new State with no validity checking
     *
     * @param currentRound       the round-number starting with 1
     * @param map                the map-state with all changes
     * @param mySafeCombinations combinations, that the target-player owns
     * @param characters         all characters on the field
     * @param catCoordinates     coordinates of the cat, will be out of field or
     *                           null if not active
     * @param janitorCoordinates coordinates of the janitor, will be out of field or
     *                           null if not active
     */
    public State(Integer currentRound, FieldMap map, Set<Integer> mySafeCombinations, Set<Character> characters,
            Point catCoordinates, Point janitorCoordinates) {
        this.currentRound = currentRound;
        this.map = map;
        this.mySafeCombinations = mySafeCombinations;
        this.characters = characters;
        this.catCoordinates = catCoordinates;
        this.janitorCoordinates = janitorCoordinates;
    }

    /**
     * Initialize a new State to be played
     *
     * @param map        the (initial) map to start the game with
     * @param characters the characters active on the field
     */
    public State(FieldMap map, Set<Character> characters) {
        this(1, map, new LinkedHashSet<>(), characters, null, null);
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public FieldMap getMap() {
        return map;
    }

    public Set<Integer> getMySafeCombinations() {
        return mySafeCombinations;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public Point getCatCoordinates() {
        return catCoordinates;
    }

    public Point getJanitorCoordinates() {
        return janitorCoordinates;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public void setMap(FieldMap map) {
        this.map = map;
    }

    public void setMySafeCombinations(Set<Integer> mySafeCombinations) {
        this.mySafeCombinations = mySafeCombinations;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public void setCatCoordinates(Point catCoordinates) {
        this.catCoordinates = catCoordinates;
    }

    public void setJanitorCoordinates(Point janitorCoordinates) {
        this.janitorCoordinates = janitorCoordinates;
    }

    /**
     * @return true if the cat is on the field
     */
    public boolean catIsActive() {
        return catCoordinates != null && map.getSpecificField(catCoordinates) != null;
    }

    /**
     * @return true if the janitor is on the field
     */
    public boolean janitorIsActive() {
        return janitorCoordinates != null && map.getSpecificField(janitorCoordinates) != null;
    }

    @Override
    public String toString() {
        return "State [currentRound=" + currentRound + ", map=" + map + ", mySafeCombinations=" + mySafeCombinations
                + ", characters=" + characters + ", catCoordinates=" + catCoordinates + ", janitorCoordinates="
                + janitorCoordinates + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof State))
            return false;
        State state = (State) o;
        return Objects.equals(currentRound, state.currentRound) && Objects.equals(map, state.map)
                && Objects.equals(mySafeCombinations, state.mySafeCombinations)
                && Objects.equals(characters, state.characters) && Objects.equals(catCoordinates, state.catCoordinates)
                && Objects.equals(janitorCoordinates, state.janitorCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentRound, map, mySafeCombinations, characters, catCoordinates, janitorCoordinates);
    }
}
