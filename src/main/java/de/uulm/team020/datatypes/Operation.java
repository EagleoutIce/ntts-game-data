package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;

/**
 * Implementation of an character-operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/23/2020
 * @since 1.2
 */
public class Operation extends BaseOperation {

    private static final long serialVersionUID = 7333289553629381638L;

    private UUID characterId;

    /**
     * Construct a new Operation
     * 
     * @param type        Type of the operation.
     * @param characterId The id of the character that should perform the operation
     * @param successful  Was the operation successful? This should not be set on
     *                    sending the operation as the server will decide if it was
     *                    successful and may ignore this field or fail on true if
     *                    not.
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     */
    public Operation(OperationEnum type, UUID characterId, Boolean successful, Point target) {
        super(type, successful, target);
        this.characterId = characterId;
    }

    /**
     * Construct a new Operation
     * 
     * @param type        Type of the operation.
     * @param characterId The id of the character that should perform the operation
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     * 
     * @see #Operation(OperationEnum, UUID, Boolean, Point)
     */
    public Operation(OperationEnum type, UUID characterId, Point target) {
        this(type, characterId, false, target);
    }

    public UUID getCharacterId() {
        return this.characterId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Operation [<base-operation>=").append(super.toString()).append(", characterId=")
                .append(characterId).append("]");
        return builder.toString();
    }
}