package de.uulm.team020.datatypes;

import de.uulm.team020.datatypes.enumerations.FieldStateEnum;

/**
 * This interface enforces the subtype to offer a field
 * 
 * @author Florian Sihler
 * @version 1.0, 06/20/2020
 */
public interface IAmAState {

    FieldStateEnum getState();

}