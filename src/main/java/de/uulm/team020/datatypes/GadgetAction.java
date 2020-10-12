package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;

/**
 * Implementation of a gadget-operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 * @since 1.2
 */
public class GadgetAction extends Operation {

    private static final long serialVersionUID = 346728470713883210L;

    private GadgetEnum gadget;

    /**
     * Construct a new Gadget-Operation
     * 
     * @param characterId The id of the character that should perform the action
     * @param successful  Was the action successful? This should not be set on
     *                    sending the operation as the server will decide if it was
     *                    successful and may ignore this field or fail on true if
     *                    not.
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     * @param gadget      The gadget used
     */
    public GadgetAction(UUID characterId, boolean successful, Point target, GadgetEnum gadget) {
        super(OperationEnum.GADGET_ACTION, characterId, successful, target);
        this.gadget = gadget;
    }

    /**
     * Construct a new Gadget-Operation
     * 
     * @param characterId The id of the character that should perform the action
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     * @param gadget      The gadget used
     * 
     * @see #GadgetAction(UUID, boolean, Point, GadgetEnum)
     */
    public GadgetAction(UUID characterId, Point target, GadgetEnum gadget) {
        this(characterId, false, target, gadget);
    }

    public GadgetEnum getGadget() {
        return this.gadget;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GadgetAction [<base-operation>=").append(super.toString()).append(", gadget=").append(gadget)
                .append("]");
        return builder.toString();
    }

}