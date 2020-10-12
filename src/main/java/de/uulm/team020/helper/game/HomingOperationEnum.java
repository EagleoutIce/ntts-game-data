package de.uulm.team020.helper.game;

/**
 * Type of a target used in {@link HomingTargetType}.
 * <p>
 * This class was named {@code OperationAimTargetType}, but changed the name
 * when included in game-data to offer a more 'abstract'-usage.
 *
 * @author Lennart Altenhof
 * @author Florian Sihler
 * @version 1.0, 06/05/2020
 * 
 * @since 1.2
 */
public enum HomingOperationEnum {
    /**
     * Calculate movement targets.
     */
    MOVEMENT,
    /**
     * Calculate spy targets
     */
    SPY,
    /**
     * Calculate targets for a gadget
     */
    GADGET,
    /**
     * Calculate targets for a gamble
     */
    GAMBLE,
    /**
     * Calculate targets to take a cocktail from
     */
    TAKE_COCKTAIL,
    /**
     * Calculate targets to use properties on
     */
    PROPERTY,
    /**
     * Calculate targets for retirement
     */
    RETIRE
}