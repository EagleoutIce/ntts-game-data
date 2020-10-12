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
public class GambleAction extends Operation {

    private static final long serialVersionUID = 346728470713883210L;

    private int stake;

    /**
     * Construct a new Gamble-Operation
     * 
     * @param characterId The id of the character that should perform the gamble
     * @param successful  Was the gamble successful? This should not be set on
     *                    sending the operation as the server will decide if it was
     *                    successful and may ignore this field or fail on true if
     *                    not.
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     * @param stake       Amount of chips placed for the gamble
     */
    public GambleAction(UUID characterId, Boolean successful, Point target, int stake) {
        super(OperationEnum.GAMBLE_ACTION, characterId, successful, target);
        this.stake = stake;
    }

    /**
     * Construct a new Gamble-Operation
     * 
     * @param characterId The id of the character that should perform the gamble
     * @param target      Position-Target of the operation, e.g. on a move this will
     *                    set the target field of the movement.
     * @param stake       Amount of chips placed for the gamble
     * 
     * @see #GambleAction(UUID, Boolean, Point, int)
     */
    public GambleAction(UUID characterId, Point target, int stake) {
        this(characterId, false, target, stake);
    }

    public int getStake() {
        return this.stake;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GambleAction [<base-operation>=").append(super.toString()).append(", stake=").append(stake)
                .append("]");
        return builder.toString();
    }

}