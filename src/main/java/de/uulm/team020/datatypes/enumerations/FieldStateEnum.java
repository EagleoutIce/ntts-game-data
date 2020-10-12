package de.uulm.team020.datatypes.enumerations;

import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.IAmAState;
import de.uulm.team020.datatypes.IAmTransparent;
import de.uulm.team020.datatypes.IAmWalkable;

/**
 * Represents a FieldState in a {@link Scenario}
 * 
 * @author Florian Sihler
 * @version 1.2, 06/20/2020
 */
public enum FieldStateEnum implements IAmWalkable, IAmTransparent, IAmAState {
    BAR_TABLE(false, false, "An einem Bar-Tisch lassen sich Cocktails beziehen."),
    ROULETTE_TABLE(false, false, "An einem Roulette-Tisch kann mit Chips gespielt werden."),
    WALL(false, true, "Eine Wand blockiert ein Feld."), FREE(true, false, "Ein freies Feld zum freien begehen."),
    BAR_SEAT(true, false, "Ein Sitzplatz."),
    SAFE(false, false, "Ein Tresor mit Nummer, in dem sich das Diamanthalsband befinden kann."),
    FIREPLACE(false, true, "Ein stimmungsvoller, offener Kamin.");

    /** Can a character walk on this field? */
    private boolean walkable;
    /** Does this field block the line of sight (los)? */
    private boolean blocksLOS;
    /** Further Text about this Field */
    private String description;

    /**
     * Assigns the standardized Metadata to a FieldSate
     * 
     * @param walkable    is the field walkable?
     * @param blocksLOS   does the Field block the Line of Sight?
     * @param description text, describing the field type
     */
    FieldStateEnum(boolean walkable, boolean blocksLOS, String description) {
        this.walkable = walkable;
        this.blocksLOS = blocksLOS;
        this.description = description;
    }

    /**
     * @return true, if a {@link Character}/NPC... is theoretically able to walk on
     *         this field.
     */
    @Override
    public boolean isWalkable() {
        return this.walkable;
    }

    /**
     * @return true, if the field blocks the LOS of the player
     */
    public boolean blocksLOS() {
        return this.blocksLOS;
    }

    /**
     * @return the generic field description
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public FieldStateEnum getState() {
        return this;
    }
}