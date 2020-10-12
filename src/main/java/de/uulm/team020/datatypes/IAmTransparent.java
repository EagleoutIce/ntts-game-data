package de.uulm.team020.datatypes;

import java.io.Serializable;

import de.uulm.team020.helper.pathfinding.Path;

/**
 * Just a simple Interface to be implemented by any FieldTye to check if it can
 * be seen through on or not. This is used by the
 * {@link Path#isLineOfSight(Path, de.uulm.team020.helper.pathfinding.AbstractGameField)}
 * to check if this field can be seen through or not
 * 
 * @author Florian Sihler
 * @version 1.0, 05/03/2020
 */
public interface IAmTransparent extends Serializable {

    /**
     * @return True if and only if it is possible to see through this field
     */
    default boolean isTransparent() {
        return !this.blocksLOS();
    }

    /**
     * @return True if and only if it is NOT possible to see through this field
     */
    boolean blocksLOS();

}