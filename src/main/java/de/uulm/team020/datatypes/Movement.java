package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.messages.GameOperationMessage;

/**
 * Represents a movement-operation that shall be shipped with an
 * {@link GameOperationMessage}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 */
public class Movement extends Operation {

    private static final long serialVersionUID = -4266029017642250420L;

    private Point from;

    /**
     * Build a new movement operation
     * 
     * @param characterId Desired character that shall perform the move.
     * @param target      The target of the movement operation.
     * @param from        The start field of this operation
     */
    public Movement(UUID characterId, Point target, Point from) {
        super(OperationEnum.MOVEMENT, characterId, target);
        this.from = from;
    }

    /**
     * Build a new movement operation
     * 
     * @param characterId Desired character that shall perform the move.
     * @param target      The target of the movement operation.
     * @param successful  Was the operation successful?
     * @param from        The start field of this operation
     */
    public Movement(UUID characterId, Point target, boolean successful, Point from) {
        super(OperationEnum.MOVEMENT, characterId, successful, target);
        this.from = from;
    }

    /**
     * Build a new movement operation
     * 
     * @param character Desired character that shall perform the move.
     * @param target    The target of the movement operation.
     */
    public Movement(Character character, Point target) {
        super(OperationEnum.MOVEMENT, character.getCharacterId(), target);
        this.from = character.getCoordinates();
    }

    /**
     * Build a new movement operation
     * 
     * @param character  Desired character that shall perform the move.
     * @param target     The target of the movement operation.
     * @param successful Was the operation successful?
     * 
     */
    public Movement(Character character, Point target, boolean successful) {
        super(OperationEnum.MOVEMENT, character.getCharacterId(), successful, target);
        this.from = character.getCoordinates();
    }

    public Point getFrom() {
        return this.from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Movement [<base-operation>=").append(super.toString()).append(", from=").append(from)
                .append("]");
        return builder.toString();
    }

}