package de.uulm.team020.helper.game;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;

/**
 * Aim targets used in {@link HomingGuidance}. It will map all possible actions
 * (that may be used for a cleaner usage to the corresponding type the
 * {@link HomingGuidance} is using) to the correct Message-Action and serves as
 * a bridge between the <i>Homing</i>-Interface and the rest of the application.
 * <p>
 * This class was named {@code OperationAimTarget}, but changed the name when
 * included in game-data to offer a more 'abstract'-usage.
 * 
 * @author Lennart Altenhof
 * @author Florian Sihler
 * 
 * @version 1.1, 06/18/2020
 * 
 * @since 1.2
 */
public enum HomingTargetType {
    /**
     * Retirement as encountered on a {@link OperationEnum#RETIRE}
     */
    RETIRE(HomingOperationEnum.RETIRE, OperationEnum.RETIRE),
    /**
     * Movement as encountered on a {@link OperationEnum#MOVEMENT}. In fact this
     * will return all fields that you can move to, even if you have to spend
     * multiple MP (see
     * {@link HomingGuidance#getNeededMP(de.uulm.team020.datatypes.util.Point)}
     */
    MOVEMENT(HomingOperationEnum.MOVEMENT, OperationEnum.MOVEMENT),
    /**
     * Spy as encountered on a {@link OperationEnum#SPY_ACTION}
     */
    SPY(HomingOperationEnum.SPY, OperationEnum.SPY_ACTION),
    /**
     * Gamble as encountered on a {@link OperationEnum#GAMBLE_ACTION}
     */
    GAMBLE(HomingOperationEnum.GAMBLE, OperationEnum.GAMBLE_ACTION),
    /**
     * Usage of the hairdryer. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#HAIRDRYER}-Gadget.
     */
    HAIRDRYER(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the moledie. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#MOLEDIE}-Gadget.
     */
    MOLEDIE(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the technicolour prism. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#TECHNICOLOUR_PRISM}-Gadget.
     */
    TECHNICOLOUR_PRISM(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the bowler bade. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#BOWLER_BLADE}-Gadget.
     */
    BOWLER_BLADE(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the poison pills. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#POISON_PILLS}-Gadget.
     */
    POISON_PILLS(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the laser compact. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#LASER_COMPACT}-Gadget.
     */
    LASER_COMPACT(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the rocket pen. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#ROCKET_PEN}-Gadget.
     */
    ROCKET_PEN(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the gas gloss. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#GAS_GLOSS}-Gadget. Please note, that valid gas-gloss
     * targets do include your current team members so if you don't want to attack
     * them, it is up to you to filter them.
     */
    GAS_GLOSS(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the mothball pouch. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#MOTHBALL_POUCH}-Gadget.
     */
    MOTHBALL_POUCH(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the fog tin. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#FOG_TIN}-Gadget.
     */
    FOG_TIN(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the grapple. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#GRAPPLE}-Gadget.
     */
    GRAPPLE(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the wiretap-gadget. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#WIRETAP_WITH_EARPLUGS}-Gadget.
     */
    WIRETAP_WITH_EARPLUGS(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the jetpack. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#JETPACK}-Gadget.
     */
    JETPACK(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the chicken feed. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#CHICKEN_FEED}-Gadget.
     */
    CHICKEN_FEED(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the nugget. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#NUGGET}-Gadget.
     */
    NUGGET(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the mirror of wilderness. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#MIRROR_OF_WILDERNESS}-Gadget.
     */
    MIRROR_OF_WILDERNESS(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the cocktail <i>on</i> something which is not taking it. This is
     * encountered for a {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#COCKTAIL}-Gadget. It differs from {@link #TAKE_COCKTAIL} by
     * the features of the target field.
     * 
     * @see #TAKE_COCKTAIL
     */
    COCKTAIL(HomingOperationEnum.GADGET, OperationEnum.GADGET_ACTION),
    /**
     * Take the cocktail from a bar-table. This is encountered for a
     * {@link OperationEnum#GADGET_ACTION} using the
     * {@link GadgetEnum#COCKTAIL}-Gadget. It differs from {@link #COCKTAIL} by the
     * features of the target field.
     * 
     * @see #COCKTAIL
     */
    TAKE_COCKTAIL(HomingOperationEnum.TAKE_COCKTAIL, OperationEnum.GADGET_ACTION),
    /**
     * Usage of the bang and burn property. This is encountered for a
     * {@link OperationEnum#PROPERTY_ACTION} using the
     * {@link PropertyEnum#BANG_AND_BURN}-Property.
     */
    BANG_AND_BURN(HomingOperationEnum.PROPERTY, OperationEnum.PROPERTY_ACTION),
    /**
     * Usage of the observation property. This is encountered for a
     * {@link OperationEnum#PROPERTY_ACTION} using the
     * {@link PropertyEnum#OBSERVATION}-Property.
     */
    OBSERVATION(HomingOperationEnum.PROPERTY, OperationEnum.PROPERTY_ACTION),
    /**
     * Usage of any usable property. This is encountered for a general
     * {@link OperationEnum#PROPERTY_ACTION}. This serves as combination for
     * {@link #BANG_AND_BURN} and {@link #OBSERVATION} (as long as the character has
     * both, if not this will calculate only the one the character is allowed to
     * have).
     */
    PROPERTY(HomingOperationEnum.PROPERTY, OperationEnum.PROPERTY_ACTION);

    /**
     * The type of the target.
     */
    private final HomingOperationEnum type;
    /**
     * The type of the operation.
     */
    private final OperationEnum operationEnum;

    HomingTargetType(HomingOperationEnum type, OperationEnum operationEnum) {
        this.type = type;
        this.operationEnum = operationEnum;
    }

    /**
     * Gets the aim target for a given gadget type. This method translate a gadget
     * into its corresponding target-type for the homing algorithms.
     * <p>
     * Note that a {@link GadgetEnum#COCKTAIL}-Action will never be translated to
     * {@link #TAKE_COCKTAIL}. It will always translate to {@link #COCKTAIL}.
     *
     * @param gadget The gadget type
     * 
     * @return The aim target if existing for given gadget, {@code null} otherwise
     */
    public static HomingTargetType ofGadget(final GadgetEnum gadget) {
        switch (gadget) {
            case HAIRDRYER:
                return HAIRDRYER;
            case MOLEDIE:
                return MOLEDIE;
            case TECHNICOLOUR_PRISM:
                return TECHNICOLOUR_PRISM;
            case BOWLER_BLADE:
                return BOWLER_BLADE;
            case POISON_PILLS:
                return POISON_PILLS;
            case LASER_COMPACT:
                return LASER_COMPACT;
            case ROCKET_PEN:
                return ROCKET_PEN;
            case GAS_GLOSS:
                return GAS_GLOSS;
            case MOTHBALL_POUCH:
                return MOTHBALL_POUCH;
            case FOG_TIN:
                return FOG_TIN;
            case GRAPPLE:
                return GRAPPLE;
            case WIRETAP_WITH_EARPLUGS:
                return WIRETAP_WITH_EARPLUGS;
            case JETPACK:
                return JETPACK;
            case CHICKEN_FEED:
                return CHICKEN_FEED;
            case NUGGET:
                return NUGGET;
            case MIRROR_OF_WILDERNESS:
                return MIRROR_OF_WILDERNESS;
            case COCKTAIL:
                return COCKTAIL;
            default:
                return null;
        }
    }

    /**
     * Converts a given aim target to a gadget enum. This method translate a target
     * into its corresponding gadget if it presents a valid gadget-target.
     *
     * @param aimTarget The aim target
     * @return The gadget enum if valid, {@code null} otherwise
     * 
     * @see #ofGadget(GadgetEnum)
     * @see #toGadget()
     * @see #toProperty(HomingTargetType)
     */
    public static GadgetEnum toGadget(final HomingTargetType aimTarget) {
        switch (aimTarget) {
            case HAIRDRYER:
                return GadgetEnum.HAIRDRYER;
            case MOLEDIE:
                return GadgetEnum.MOLEDIE;
            case TECHNICOLOUR_PRISM:
                return GadgetEnum.TECHNICOLOUR_PRISM;
            case BOWLER_BLADE:
                return GadgetEnum.BOWLER_BLADE;
            case POISON_PILLS:
                return GadgetEnum.POISON_PILLS;
            case LASER_COMPACT:
                return GadgetEnum.LASER_COMPACT;
            case ROCKET_PEN:
                return GadgetEnum.ROCKET_PEN;
            case GAS_GLOSS:
                return GadgetEnum.GAS_GLOSS;
            case MOTHBALL_POUCH:
                return GadgetEnum.MOTHBALL_POUCH;
            case FOG_TIN:
                return GadgetEnum.FOG_TIN;
            case GRAPPLE:
                return GadgetEnum.GRAPPLE;
            case WIRETAP_WITH_EARPLUGS:
                return GadgetEnum.WIRETAP_WITH_EARPLUGS;
            case JETPACK:
                return GadgetEnum.JETPACK;
            case CHICKEN_FEED:
                return GadgetEnum.CHICKEN_FEED;
            case NUGGET:
                return GadgetEnum.NUGGET;
            case MIRROR_OF_WILDERNESS:
                return GadgetEnum.MIRROR_OF_WILDERNESS;
            case COCKTAIL:
            case TAKE_COCKTAIL:
                return GadgetEnum.COCKTAIL;
            default:
                return null;
        }
    }

    /**
     * Converts the aim target to a property enum. This method translate a target
     * into its corresponding property if it presents a valid property-target.
     *
     * @param aimTarget The target-type that shall be translated to a (valid)
     *                  property
     * 
     * @return The property enum if valid, {@code null} otherwise
     * 
     * @see #toProperty()
     * @see #toGadget(HomingTargetType)
     */
    public static PropertyEnum toProperty(final HomingTargetType aimTarget) {
        switch (aimTarget) {
            case OBSERVATION:
                return PropertyEnum.OBSERVATION;
            case BANG_AND_BURN:
            default:
                return PropertyEnum.BANG_AND_BURN;
        }
    }

    /**
     * Converts the aim target to a gadget enum.
     *
     * @return The gadget enum if valid, {@code null} otherwise
     * 
     * @see #toGadget(HomingTargetType)
     */
    public GadgetEnum toGadget() {
        return toGadget(this);
    }

    /**
     * Converts the aim target to a property enum.
     *
     * @return The property enum if valid, {@code null} otherwise
     * 
     * @see #toProperty(HomingTargetType)
     */
    public PropertyEnum toProperty() {
        return toProperty(this);
    }

    /**
     * Get the embedded operation for the homing algorithm to use.
     * 
     * @return Operation choice for the homing algorithm
     */
    public HomingOperationEnum getType() {
        return type;
    }

    /**
     * Get the embedded operation enum
     * 
     * @return Corresponding operation enum
     */
    public OperationEnum getOperationEnum() {
        return operationEnum;
    }
}