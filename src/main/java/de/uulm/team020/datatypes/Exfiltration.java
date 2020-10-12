package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.messages.GameOperationMessage;

/**
 * Represents a exfiltration-operation that shall be shipped with an
 * {@link GameOperationMessage}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 */
public class Exfiltration extends Operation {

    private static final long serialVersionUID = -4266029017642250420L;

    private Point from;

    /**
     * Build a new exfiltration operation
     * 
     * @param characterId Desired character that shall perform the move.
     * @param target      The target of the movement operation.
     * @param from        The start field of this operation
     */
    public Exfiltration(UUID characterId, Point target, Point from) {
        super(OperationEnum.EXFILTRATION, characterId, target);
        this.from = from;
    }

    /**
     * Build a new exfiltration operation
     * 
     * @param characterId Desired character that shall perform the move.
     * @param target      The target of the exfiltration operation.
     * @param successful  Was the operation successful?
     * @param from        The start field of this operation
     */
    public Exfiltration(UUID characterId, Point target, boolean successful, Point from) {
        super(OperationEnum.EXFILTRATION, characterId, successful, target);
        this.from = from;
    }

    /**
     * Build a new exfiltration operation
     * 
     * @param character Desired character that shall perform the move.
     * @param target    The target of the exfiltration operation.
     */
    public Exfiltration(Character character, Point target) {
        super(OperationEnum.EXFILTRATION, character.getCharacterId(), target);
        this.from = character.getCoordinates();
    }

    /**
     * Build a new exfiltration operation
     * 
     * @param character  Desired character that shall perform the move.
     * @param target     The target of the exfiltration operation.
     * @param successful Was the operation successful?
     * 
     */
    public Exfiltration(Character character, Point target, boolean successful) {
        super(OperationEnum.EXFILTRATION, character.getCharacterId(), successful, target);
        this.from = character.getCoordinates();
    }

    public Point getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Exfiltration [<base-operation>=").append(super.toString()).append(", from=").append(from)
                .append("]");
        return builder.toString();
    }

}