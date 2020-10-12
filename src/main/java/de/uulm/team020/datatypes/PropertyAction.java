package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.validation.GameDataGson;

/**
 * Implementation of a property-operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 * @since 1.2
 */
public class PropertyAction extends Operation {

    private static final long serialVersionUID = 346728470713883210L;

    private PropertyEnum usedProperty;
    private boolean isEnemy;

    /**
     * Construct a new Property-Operation
     * 
     * @param characterId The id of the character that should perform the action
     * @param successful  Was the action successful? This should not be set on
     *                    sending the operation as the server will decide if it was
     *                    successful and may ignore this field or fail on true if
     *                    not.
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the property action.
     * @param property    The property used
     */
    public PropertyAction(UUID characterId, boolean successful, Point target, PropertyEnum property) {
        this(characterId, successful, target, property, false);
    }

    /**
     * Construct a new Property-Operation
     * 
     * @param characterId The id of the character that should perform the action
     * @param successful  Was the action successful? This should not be set on
     *                    sending the operation as the server will decide if it was
     *                    successful and may ignore this field or fail on true if
     *                    not.
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the property action.
     * @param property    The property used
     * @param isEnemy     Only important for Observation - is true if target was
     *                    revealed as Member of the enemy faction.
     */
    public PropertyAction(UUID characterId, boolean successful, Point target, PropertyEnum property, boolean isEnemy) {
        super(OperationEnum.PROPERTY_ACTION, characterId, successful, target);
        this.usedProperty = property;
        this.isEnemy = isEnemy;
    }

    /**
     * Construct a new Property-Operation
     * 
     * @param characterId The id of the character that should perform the action
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the action.
     * @param property    The property used
     * 
     * @see #PropertyAction(UUID, boolean, Point, PropertyEnum)
     * @see #PropertyAction(UUID, boolean, Point, PropertyEnum, boolean)
     */
    public PropertyAction(UUID characterId, Point target, PropertyEnum property) {
        this(characterId, false, target, property);
    }

    public PropertyEnum getProperty() {
        return this.usedProperty;
    }

    /**
     * @return the isEnemy
     */
    public boolean isEnemy() {
        return isEnemy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PropertyAction [<base-operation>=").append(super.toString()).append(", property=")
                .append(usedProperty).append(", isEnemy=").append(isEnemy).append("]");
        return builder.toString();
    }

    @Override
    public String toJson() {
        return GameDataGson.toJson(this);
    }

}