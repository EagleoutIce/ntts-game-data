package de.uulm.team020.datatypes;

import java.io.Serializable;

/**
 * Just a simple Interface to be implemented by any FieldTye to check if it can
 * be walked on or not. This is used by the path finding algorithm to check
 * routes. There can be costs applied, but they don't have to. Fields that
 * cannot be walked on will be tagged with a cost of '-1'.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/17/2020
 */
public interface IAmWalkable extends Serializable {

    /**
     * @return True if and only if it is possible to walk on this field
     */
    boolean isWalkable();

    /**
     * @return True if and only if it is NOT possible to walk on this field
     */
    default boolean blocksWay() {
        return !isWalkable();
    }

    /**
     * How expensive is it to walk on this field?
     * 
     * @return 1 if walkable, -1 if not.
     */
    default int wayCost() {
        return isWalkable() ? 1 : -1;
    }

}