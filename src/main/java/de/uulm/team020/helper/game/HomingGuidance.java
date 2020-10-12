package de.uulm.team020.helper.game;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.WiretapWithEarplugs;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.exceptions.HomingException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.logging.Magpie;

/**
 * This is used for getting possible operation targets. Operator (the character
 * that targets are get for) and the environment (current state) are set via
 * {@link #updateOperation(State, Matchconfig, Character, List)}.
 * <p>
 * This class was named {@code OperationAim}, but changed the name when included
 * in game-data to offer a more 'abstract'-usage.
 *
 * @author Lennart Altenhof
 * @author Florian Sihler
 * 
 * @version 1.1, 06/19/2020
 * @since 1.2
 */
public class HomingGuidance {

    private static final Magpie magpie = Magpie.createMagpieSafe("HomingGuidance");

    /**
     * All points of the map.
     */
    private final Set<Point> mapPoints;
    /**
     * All points of found safes
     */
    private final Set<Point> safePoints;

    /**
     * The direct neighbour points of the {@link #operator}.
     */
    private final Set<Point> neighbours;
    /**
     * The direct neighbour characters of the {@link #operator} without cat and
     * janitor.
     */
    private final Set<Character> neighbourCharacters;
    /**
     * The targets to aim at calculated via {@link #updateTargets()}.
     */
    private final Map<HomingTargetType, Set<Point>> calculatedTargets;
    /**
     * The state used for calculating targets.
     */
    private State state = null;
    /**
     * The character to get targets for.
     */
    private Character operator = null;
    /**
     * The characters that belong to the players faction.
     */
    private List<UUID> ownCharacters = null;
    /**
     * The matchconfig.
     */
    private Matchconfig matchconfig = null;

    /**
     * Create a new homing guidance system which calculates valid points for an
     * operation. To set "your data" you have to call
     * {@link #updateOperation(State, Matchconfig, Character, List)}
     */
    public HomingGuidance() {
        this.mapPoints = new HashSet<>();
        this.safePoints = new HashSet<>();
        this.neighbours = new HashSet<>();
        this.neighbourCharacters = new HashSet<>();
        // synchronized access is assumed as this class does not use multiple operations
        this.calculatedTargets = new EnumMap<>(HomingTargetType.class);
    }

    /**
     * Updates the whole operation by recalculating all targets.
     *
     * @param env           The current state
     * @param matchconfig   The matchconfig
     * @param operator      The character that targets are calculated for
     * @param ownCharacters The characters the operator's faction owns
     */
    public void updateOperation(final State env, final Matchconfig matchconfig, final Character operator,
            final List<UUID> ownCharacters) {
        this.state = env;
        this.matchconfig = matchconfig;
        this.operator = operator;
        this.ownCharacters = ownCharacters;
        updateShortcuts();
        updateTargets();
        magpie.writeInfo("Updated operation: " + this.toString(), "update");
    }

    /**
     * Returns whether targets for aim targets of given {@code type} are available.
     *
     * @param type The type
     * @return Whether targets are available
     */
    public boolean targetsAvailableFor(final HomingOperationEnum type) {
        return !getTargetsFor(type).isEmpty();
    }

    /**
     * Returns whether targets for given aim target are available.
     *
     * @param aimTarget The aim target
     * @return Whether targets are available
     */
    public boolean targetsAvailableFor(final HomingTargetType aimTarget) {
        final Set<Point> targets = this.calculatedTargets.get(aimTarget);
        return targets != null && !targets.isEmpty();
    }

    /**
     * Returns whether targets are available.
     *
     * @return Whether targets are available
     */
    public boolean targetsAvailable() {
        for (final Set<Point> targets : this.calculatedTargets.values()) {
            if (!targets.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all calculated targets for aim targets for given type.
     *
     * @param aimTargetType The type
     * @return Set of possible targets
     */
    public Set<Point> getTargetsFor(final HomingOperationEnum aimTargetType) {
        final Set<Point> res = new HashSet<>();
        this.calculatedTargets.keySet().stream().filter(t -> t.getType() == aimTargetType)
                .forEach(t -> res.addAll(this.calculatedTargets.get(t)));
        return res;
    }

    /**
     * Used for determining needed MP for moving operator's position to passed
     * {@code end} point.
     *
     * @param end The end point
     * @return The amount of MPs needed
     */
    public int getNeededMP(final Point end) {
        return Point.getKingDistance(getOperatorPosition(), end);
    }

    /**
     * Gets the calculated targets for a given aim target.
     *
     * @param aimTarget The aim target
     * @return Set of possible targets
     */
    public Set<Point> getTargetsFor(final HomingTargetType aimTarget) {
        return this.calculatedTargets.get(aimTarget);
    }

    /**
     * Get the character the homing guidance is using as operator
     *
     * @return Selected Character by
     *         {@link #updateOperation(State, Matchconfig, Character, List)}
     */
    public Character getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder("HomingGuidance [");
        stringBuilder.append(this.operator.toString()).append(",\nneighbours: ").append(this.neighbours)
                .append(",\nneighbourCharacterPositions: ")
                .append(this.neighbourCharacters.stream().map(Character::getCoordinates).collect(Collectors.toList()))
                .append(") {");
        for (final Map.Entry<HomingTargetType, Set<Point>> entry : this.calculatedTargets.entrySet()) {
            stringBuilder.append("\n  ").append(entry.getKey()).append(": ").append(entry.getValue());
            stringBuilder.append(",");
        }
        return stringBuilder.append("\n}").toString();
    }

    // =======================================================================================
    // General Helper
    // =======================================================================================

    /**
     * Calculates the targets for a given aim target.
     *
     * @param aimTarget The aim target
     * @return Set of possible targets
     */
    private Set<Point> calcTargetsFor(final HomingTargetType aimTarget) {
        // check if needed preconditions are satisfied
        if (!checkTargetPreconditions(aimTarget)) {
            return Collections.emptySet();
        }

        switch (aimTarget) {
            case SPY:
                return getTargetsForSpyAction();
            case GAMBLE:
                return getTargetsForGamble();
            case HAIRDRYER:
                return getTargetsForHairdryerGadget();
            case MOLEDIE:
                return getTargetsForMoledieGadget();
            case TECHNICOLOUR_PRISM:
                return getTargetsForTechnicolourPrism();
            case BOWLER_BLADE:
                return getTargetsForBowlerBlade();
            case POISON_PILLS:
                return getTargetsForPoisonPills();
            case LASER_COMPACT:
                return getTargetsForLaserCompact();
            case ROCKET_PEN:
                return getTargetsForRocketPen();
            case GAS_GLOSS:
                return getTargetsForGasGloss();
            case MOTHBALL_POUCH:
                return getTargetsForMothballPouch();
            case FOG_TIN:
                return getTargetsForFogTin();
            case GRAPPLE:
                return getTargetsForGrapple();
            case WIRETAP_WITH_EARPLUGS:
                return getTargetsForWiretapWithEarplugs();
            case JETPACK:
                return getTargetsForJetpack();
            case CHICKEN_FEED:
                return getTargetsForChickenFeed();
            case NUGGET:
                return getTargetsForNugget();
            case MIRROR_OF_WILDERNESS:
                return getTargetsForMirrorOfWilderness();
            case COCKTAIL:
                return getTargetsForCocktail();
            case TAKE_COCKTAIL:
                return getTargetsForCocktailTake();
            case MOVEMENT:
                return getTargetsForMovement();
            case BANG_AND_BURN:
                return getTargetsForBangAndBurn();
            case OBSERVATION:
                return getTargetsForObservation();
            case PROPERTY:
                return getTargetsForPropertyAction();
            default:
                throw new UnsupportedOperationException("Dont know any target allocator for " + aimTarget + ".");
        }
    }

    /**
     * This is a helper for {@link #calcTargetsFor(HomingTargetType)} and will
     * verify that all necessary preconditions for generating the target points are
     * met. This merely safes some time if you e.g. want to calculate points for
     * gadget you do not posses
     *
     * @param aimTarget The goal of the operation
     * @return True if it may be performed, false otherwise
     */
    private boolean checkTargetPreconditions(final HomingTargetType aimTarget) {
        switch (aimTarget.getType()) {
            case GADGET:
                // check whether owning the gadget
                return this.operator.getGadgetType(HomingTargetType.toGadget(aimTarget)).isPresent();
            case GAMBLE:
                // check whether character owns any chips
                return this.operator.getChips() > 0;
            case PROPERTY:
                // check if character owns the property
                if ((aimTarget == HomingTargetType.BANG_AND_BURN && !operatorHasProperty(PropertyEnum.BANG_AND_BURN))
                        // wants bang and burn but does not have the property or
                        // wants observation but does not have the property
                        || (aimTarget == HomingTargetType.OBSERVATION
                                && !operatorHasProperty(PropertyEnum.OBSERVATION))) {
                    return false;
                }
                break;
            default:
        }
        return true; // all fine!
    }

    /**
     * Gets all possible targets for the {@link OperationEnum#SPY_ACTION}. This also
     * includes checking for {@link PropertyEnum#FLAPS_AND_SEALS} and known safe
     * combinations.
     *
     * @return Set of possible target coordinates
     */
    protected Set<Point> getTargetsForSpyAction() {
        // add characters (check if existing and not in own faction)
        // as we are able to spy on any neighbour character which is not in our team
        final Set<Point> targets = this.neighbourCharacters.stream() // all neighbour characters
                .filter(this::isNotOneOfOurCharacters) // which are not 'from us'
                .map(Character::getCoordinates) // get their coordinates
                .collect(Collectors.toCollection(HashSet::new)); // collect to a set

        // to check the maximum distance to a safe allowed we will calculate it here:
        final int safeDist = operator.getProperties().contains(PropertyEnum.FLAPS_AND_SEALS) ? 2 : 1;

        // Iterate over all safe keys
        targets.addAll(safesWeCanOpen(safeDist));
        return targets;
    }

    private boolean isNotOneOfOurCharacters(final Character c) {
        return !ownCharacters.contains(c.getCharacterId());
    }

    private Set<Point> safesWeCanOpen(final int safeDist) {
        final Set<Point> targets = new HashSet<>();
        for (final Point safePoint : safePoints) {
            final Field field = getMap().getSpecificField(safePoint);
            // do we not know the combination?
            if (!this.state.getMySafeCombinations().contains(field.getSafeIndex())) {
                continue;
            }
            final int dist = Point.getKingDistance(getOperatorPosition(), safePoint);
            // not the field operator is standing on and in range? (this includes
            // flaps_and_seals)
            if (dist > 0 && dist <= safeDist) {
                // finally add as it is a safe that we can open
                // maybe we should break here as there usually will be only one safe that
                // interests us, the one with the highest number, but this is not the job of
                // this method.
                targets.add(safePoint);
            }
        }
        return targets;
    }

    /**
     * Gets possible targets for {@link OperationEnum#GAMBLE_ACTION}. This checks
     * for neighbour {@link FieldStateEnum#ROULETTE_TABLE} fields and if they are
     * destroyed or have no chips left.
     *
     * @return The possible targets
     */
    protected Set<Point> getTargetsForGamble() {
        return this.neighbours.stream() // for all neighbour points
                .filter(this::fieldIsValidForGamble) // check if it's valid for gamble
                .collect(Collectors.toSet()); // return as set
    }

    /**
     * Helps {@link #getTargetsForGamble()} by identifying if a given field is valid
     *
     * @param p The point to check for
     * @return True if the field is valid, false otherwise
     */
    protected boolean fieldIsValidForGamble(final Point p) {
        final Field field = getMap().getSpecificField(p);
        return field != null // Field must be 'on the board'
                && field.getState() == FieldStateEnum.ROULETTE_TABLE // has to be a roulette table
                && !field.isDestroyed() // which is not destroyed
                && field.getChipAmount() > 0; // and has at least one chip
    }

    /**
     * Gets targets for {@link GadgetEnum#HAIRDRYER} gadget. Possible ones are
     * neighbour characters that own the {@link PropertyEnum#CLAMMY_CLOTHES}
     * property.
     *
     * @return The possible targets
     */
    protected Set<Point> getTargetsForHairdryerGadget() {
        return this.neighbourCharacters.stream() // all neighbour characters
                .filter(c -> operatorHasProperty(PropertyEnum.CLAMMY_CLOTHES)) // all with 'clammy clothes'
                .map(Character::getCoordinates) // get their coordinates
                .collect(Collectors.toSet()); // collect
    }

    /**
     * Gets targets for {@link GadgetEnum#MOLEDIE} gadget. Possible ones are in
     * {@link Matchconfig#getMoledieRange()} and not fields of type
     * {@link FieldStateEnum#WALL}.
     *
     * @return The possible targets
     */
    protected Set<Point> getTargetsForMoledieGadget() {
        return getPointsInRange(getOperatorPosition(), matchconfig.getMoledieRange(), true).stream()
                // all points moledie can hit by range and line of sight
                .filter(p -> getMap().getSpecificField(p).getState() != FieldStateEnum.WALL)
                // filter out the cats and janitors position
                .filter(p -> !isCatOrJanitor(p))
                // get all fields which are no walls
                .collect(Collectors.toSet()); // collect them
    }

    /**
     * Gets targets for the {@link GadgetEnum#TECHNICOLOUR_PRISM} gadget. Possible
     * ones are neighbours and fields of type {@link FieldStateEnum#ROULETTE_TABLE}
     * which are not destroyed.
     *
     * @return The possible targets
     */
    protected Set<Point> getTargetsForTechnicolourPrism() {
        return this.neighbours.stream() // for every neighbour
                .filter(this::fieldIsValidForTechnicolourPrism) // which is valid for the technicolour prism
                .collect(Collectors.toSet()); // collect
    }

    /**
     * This is a helper for {@link #getTargetsForTechnicolourPrism()} as it checks
     * if the field is valid to be targeted by a technicolour prism (the gadget is
     * assumed to be present at this point)
     *
     * @param p The point to check for
     */
    private boolean fieldIsValidForTechnicolourPrism(final Point p) {
        final Field field = getMap().getSpecificField(p);
        return field != null && field.getState() == FieldStateEnum.ROULETTE_TABLE && !field.isDestroyed()
                && field.getGadget() == null;
    }

    /**
     * Gets targets for the {@link GadgetEnum#BOWLER_BLADE} gadget. Possible ones
     * are in line of sight (+ not blocked by other characters) and in
     * {@link Matchconfig#getBowlerBladeRange()} range.
     *
     * @return Set of possible points
     */
    protected Set<Point> getTargetsForBowlerBlade() {
        // iterate over all characters
        final Set<Point> targets = new HashSet<>();
        for (final Character c : getCharactersOnFieldWithoutCatAndJanitor()) {
            // check for line of sight and in range
            final Path path = Point.getLine(getOperatorPosition(), c.getCoordinates());
            if (notAValidBowlerBladeTarget(c, path)) {
                continue;
            }

            // check if any other character is in line of sight
            // add only if nothing in the way :)
            if (pathIsNotBlockedByCharacter(path)) {
                targets.add(c.getCoordinates());
            }
        }
        return targets;
    }

    private boolean pathIsNotBlockedByCharacter(final Path path) {
        for (final Point p : path) {
            // point not start or end and no character on point
            // in theory this can be moved to the path generation of point
            if (pointBetweenStartAndEndIsBlockedByCharacter(path, p)) {
                return false;
            }
        }
        return true;
    }

    private boolean pointBetweenStartAndEndIsBlockedByCharacter(final Path path, final Point p) {
        return !p.equals(path.getEnd()) && !p.equals(path.getStart()) // not start or end and any character on it
                && anyCharacterOnThisPoint(p);
    }

    private boolean anyCharacterOnThisPoint(final Point p) {
        // check for janitor or cat
        if (this.state.getJanitorCoordinates().equals(p) || this.state.getCatCoordinates().equals(p)) {
            return true;
        }
        return this.state.getCharacters().stream().anyMatch(cc -> cc.getCoordinates().equals(p));
    }

    private boolean notAValidBowlerBladeTarget(final Character c, final Path path) {
        return !path.isLineOfSight(getMap()) // is not line of sight or
                || Point.getKingDistance(getOperatorPosition(), c.getCoordinates()) > this.matchconfig
                        .getBowlerBladeRange();// is outside of range
    }

    /**
     * Gets all possible target points for the {@link GadgetEnum#POISON_PILLS}
     * gadget. Possible ones are neighbours with characters that hold cocktails or
     * {@link FieldStateEnum#BAR_SEAT} fields that hold a cocktail.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForPoisonPills() {
        final Set<Point> targets = new HashSet<>();
        // check for characters
        targets.addAll(neighbourCharactersWithCocktail());
        // check for fields
        targets.addAll(this.neighbours.stream() // for all neighbours
                .filter(this::isCocktailOnBarTable) // is it a bar table which holds a cocktail?
                .collect(Collectors.toSet())); // if so: collect to list
        return targets;
    }

    private Set<Point> neighbourCharactersWithCocktail() {
        final Set<Point> targets = new HashSet<>();
        for (final Character c : this.neighbourCharacters) {
            // does the character have a cocktail?
            final Optional<Gadget> mayCocktail = c.getGadgetType(GadgetEnum.COCKTAIL);
            if (mayCocktail.isPresent()) {
                targets.add(c.getCoordinates());
            }
        }
        return targets;
    }

    /**
     * Helper for {@link #getTargetsForPoisonPills()} and
     * {@link #getTargetsForCocktailTake()} which checks if the target field holds a
     * cocktail
     *
     * @param p The point to check for
     * @return True if valid bar table with cocktail, false otherwise
     */
    private boolean isCocktailOnBarTable(final Point p) {
        // check for bar_table and cocktail
        final Field field = getMap().getSpecificField(p);
        // is it not on the field or no bar table?
        if (isNoBarTable(field)) {
            return false;
        }
        // is the gadget placed really a gadget?
        final Gadget gadget = field.getGadget();
        return gadget != null && gadget.getGadget() == GadgetEnum.COCKTAIL;
    }

    private boolean isNoBarTable(final Field field) {
        return field == null || field.getState() != FieldStateEnum.BAR_TABLE;
    }

    /**
     * Gets all possible targets for the {@link GadgetEnum#LASER_COMPACT} gadget.
     * Possible ones are fields or characters in line of sight holding a
     * {@link GadgetEnum#COCKTAIL}.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForLaserCompact() {
        final Set<Point> targets = new HashSet<>();
        // check characters
        for (final Character c : getCharactersOnFieldWithoutCatAndJanitor()) {
            if (characterWithCocktailInLOS(c)) {
                targets.add(c.getCoordinates());
            }
        }
        // check fields, if they have a cocktail and are valid we will collect them as
        // possible targets
        targets.addAll(this.mapPoints.stream().filter(this::isCocktailOnField).collect(Collectors.toSet()));
        return targets;
    }

    private boolean characterWithCocktailInLOS(final Character c) {
        return Point.getLine(getOperatorPosition(), c.getCoordinates()).isLineOfSight(getMap()) // character is in los
                && c.getGadgetType(GadgetEnum.COCKTAIL).isPresent(); // character holds a cocktail
    }

    /**
     * This is a helper for {@link #getTargetsForLaserCompact()}.
     * <p>
     * Currently the implementation is really expensive as it will check every
     * field.
     *
     * @param p The point to check for
     * @return True if the point points to a valid field on the map which holds a
     *         cocktail
     */
    private boolean isCocktailOnField(final Point p) {
        final Field field = getMap().getSpecificField(p);
        return field != null // field is on the map; and holds a cocktail:
                && field.getGadget() != null && field.getGadget().getGadget() == GadgetEnum.COCKTAIL
                && Point.getLine(getOperatorPosition(), p).isLineOfSight(getMap()); // is in line of sight
    }

    /**
     * Gets targets for {@link GadgetEnum#ROCKET_PEN} gadget. Possible ones are in
     * line of sight.
     * <p>
     * <i>NOTE:</i> Current implementation is hardly ineffective for large maps :c.
     * Maybe it is more effective to launch a bfs search to retrieve all fields in
     * los and stop if they are not in los anymore
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForRocketPen() {
        return this.mapPoints.stream() // for all points on the map :/
                .filter(p -> Point.getLine(getOperatorPosition(), p).isLineOfSight(getMap())) // check if in los
                .collect(Collectors.toSet()); // collect
    }

    /**
     * Gets targets for {@link GadgetEnum#GAS_GLOSS} gadget. Possible ones are
     * neighbour characters. This method does not validate if the neighbours are in
     * our team. TODO: maybe just attack non-team-members?
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForGasGloss() {
        // all neighbour characters
        return neighbourCharacters.stream().map(Character::getCoordinates).collect(Collectors.toSet());
    }

    /**
     * Gets targets for {@link GadgetEnum#MOTHBALL_POUCH} gadget. Possible ones are
     * {@link FieldStateEnum#FIREPLACE} fields in
     * {@link Matchconfig#getMoledieRange()} range and in line of sight.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForMothballPouch() {
        return getPointsInRange(this.matchconfig.getMothballPouchRange(), true).stream()
                // for all points in range and los of mothball; which are a fireplace:
                .filter(p -> getMap().getSpecificField(p).getState() == FieldStateEnum.FIREPLACE)
                .collect(Collectors.toSet()); // collect them
    }

    /**
     * Gets targets for {@link GadgetEnum#FOG_TIN} gadget. Possible one are non-wall
     * fields in {@link Matchconfig#getFogTinRange()} range in line of sight.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForFogTin() {
        return getPointsInRange(this.matchconfig.getFogTinRange(), true).stream()
                // for all points in range and los of mothball; which are no wall:
                .filter(p -> getMap().getSpecificField(p).getState() != FieldStateEnum.WALL)
                .collect(Collectors.toSet()); // collect them
    }

    /**
     * Gets targets for {@link GadgetEnum#GRAPPLE} gadget. Possible ones are fields
     * in {@link Matchconfig#getGrappleRange()} range that hold a gadget and are in
     * line of sight.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForGrapple() {
        return getPointsInRange(this.matchconfig.getGrappleRange(), true).stream()
                // for all points in range and los of mothball; which have a gadget:
                .filter(p -> getMap().getSpecificField(p).getGadget() != null) // embrace the formatter!
                .collect(Collectors.toSet()); // collect them
    }

    /**
     * Gets targets for {@link GadgetEnum#WIRETAP_WITH_EARPLUGS} gadget. Possible
     * ones are neighbour characters that do not belong to the operator's faction.
     * <p>
     * <strong>Warning</strong>: This also checks the operator's inventory to
     * determine if the gadget is already active on another character!
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForWiretapWithEarplugs() {
        final Optional<Gadget> mayGadget = this.operator.getGadgetType(GadgetEnum.WIRETAP_WITH_EARPLUGS);
        if (wiretapGadgetIsActive(mayGadget)) {
            return Collections.emptySet();
        }
        return this.neighbourCharacters.stream() // for all neighbour characters
                .filter(this::isNotOneOfOwnCharacters) // which are not in our team
                .map(Character::getCoordinates) // get their coordinates
                .collect(Collectors.toSet()); // collect them
    }

    private boolean isNotOneOfOwnCharacters(final Character c) {
        return !this.ownCharacters.contains(c.getCharacterId());
    }

    private boolean wiretapGadgetIsActive(final Optional<Gadget> mayGadget) {
        return mayGadget.isPresent() && ((WiretapWithEarplugs) mayGadget.get()).getActiveOn() != null;
    }

    /**
     * Gets targets for {@link GadgetEnum#JETPACK} gadget. Possible ones are any
     * {@link FieldStateEnum#FREE} fields on the map.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForJetpack() {
        return this.mapPoints.stream().filter(this::fieldCanBeJetpackTarget).collect(Collectors.toSet());
    }

    /**
     * This is a helper for {@link #getTargetsForJetpack()}
     *
     * @param p The point to check for
     * @return True if the target point points to a coordinate on the board which is
     *         a free field not occupied by a character, by the cat or the janitor
     */
    private boolean fieldCanBeJetpackTarget(final Point p) {
        // note that 'Objects.equals' works here as a 'null' field is guaranteed to be
        // not free (using FieldMaps' specific getter).
        return getMap().getSpecificField(p).getState() == FieldStateEnum.FREE // target field has to be a 'free'-field
                && getCharacterAtPosition(p).isEmpty() // there should be no character on that field
                && !Objects.equals(this.state.getCatCoordinates(), p) // cat not on this field
                && !Objects.equals(this.state.getJanitorCoordinates(), p); // janitor not on this field
    }

    /**
     * Gets targets for {@link GadgetEnum#CHICKEN_FEED} gadget. Possible ones are
     * neighbour characters that do not belong to the player's faction.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForChickenFeed() {
        return getAllNonFriendlyNeighbourCharacterCoordinates(); // collect them to a set
    }

    /**
     * Gets targets for {@link GadgetEnum#NUGGET} gadget. Possible ones are
     * neighbour characters that do not belong to the player's faction.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForNugget() {
        return getAllNonFriendlyNeighbourCharacterCoordinates(); // collect them to a set
    }

    /**
     * Gets targets for {@link GadgetEnum#MIRROR_OF_WILDERNESS} gadget. Possible
     * ones are neighbour characters.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForMirrorOfWilderness() {
        // just get the coordinates of all neighbour characters
        return getAllNeighbourCharacterCoordinates();
    }

    /**
     * Gets targets for using {@link GadgetEnum#COCKTAIL} gadget. Possible ones are
     * neighbour characters or the operator.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForCocktail() {
        // get the coordinates of all neighbour characters TODO: maybe only enemies
        final Set<Point> targets = getAllNeighbourCharacterCoordinates();
        targets.add(operator.getCoordinates());
        return targets;
    }

    /**
     * Gets targets for taking a {@link GadgetEnum#COCKTAIL} gadget. Possible ones
     * are neighbour {@link FieldStateEnum#BAR_TABLE} fields holding a
     * {@link GadgetEnum#COCKTAIL} gadget.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForCocktailTake() {
        // a character cannot hold two cocktails
        if (this.operator.getGadgetType(GadgetEnum.COCKTAIL).isPresent()) {
            return Collections.emptySet();
        }

        // get and collect all cocktails in take-range which are placed on a bar-table
        return this.neighbours.stream().filter(this::isCocktailOnBarTable).collect(Collectors.toSet());
    }

    /**
     * Gets targets for movement action. This returns all possible movement
     * destinations and includes spending multiple mp! MP to be spent can be
     * retrieved via {@link #getNeededMP(Point)}.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForMovement() {
        // yeah this is sad, we simply use range 1 but if we can use longer paths in the
        // future, just change it
        return getPointsInRange(getOperatorPosition(), Math.min(this.operator.getMp(), 1), false).stream()
                // for all points in mp range; that are walkable and not the current position:
                .filter(this::isWalkableMovementTarget).collect(Collectors.toSet()); // collect them
    }

    private boolean isWalkableMovementTarget(final Point p) {
        return getMap().getSpecificField(p).isWalkable() && !p.equals(getOperatorPosition());
    }

    private Set<Point> getTargetsForPropertyAction() {
        final Set<Point> res = new HashSet<>();
        if (operatorHasProperty(PropertyEnum.BANG_AND_BURN)) {
            res.addAll(getTargetsForBangAndBurn());
        }
        if (operatorHasProperty(PropertyEnum.OBSERVATION)) {
            res.addAll(getTargetsForObservation());
        }
        return res;
    }

    /**
     * Gets targets for {@link PropertyEnum#BANG_AND_BURN} property. Possible ones
     * are neighbour {@link FieldStateEnum#ROULETTE_TABLE} fields that are not
     * destroyed.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForBangAndBurn() {
        return this.neighbours.stream() // for all neighbour points
                .filter(this::isBangableRouletteTable) // that are "bangable"
                .collect(Collectors.toSet()); // collect them
    }

    /**
     * This is a helper for {@link #getTargetsForBangAndBurn()} which checks if the
     * field the point points at is a not (already) destroyed roulette table.
     * <p>
     * No regrets for the name though
     *
     * @param p The point
     * @return True if targeted field is a roulette table which is not destroyed
     */
    private boolean isBangableRouletteTable(final Point p) {
        final Field field = getMap().getSpecificField(p);
        return field.getState() == FieldStateEnum.ROULETTE_TABLE && !field.isDestroyed();
    }

    /**
     * Gets targets for {@link PropertyEnum#OBSERVATION} property. Possible ones are
     * characters in line of sight. We will only suggest characters that do not
     * belong to the operator's faction.
     *
     * @return Set of possible targets
     */
    protected Set<Point> getTargetsForObservation() {
        return getCharactersOnFieldWithoutCatAndJanitor().stream().filter(this::isObservableCharacter)
                .map(Character::getCoordinates).collect(Collectors.toSet());
    }

    /**
     * This is a helper for {@link #getTargetsForObservation()} and checks if a
     * character might be observed by the operator
     *
     * @param c The character to check for
     * @return True if the character is observable (in los and not one of own)
     */
    private boolean isObservableCharacter(final Character c) {
        return Point.getLine(getOperatorPosition(), c.getCoordinates()).isLineOfSight(getMap())
                && isNotOneOfOwnCharacters(c);
    }

    /**
     * This updates the {@link #calculatedTargets}.
     */
    protected void updateTargets() {
        this.calculatedTargets.clear();
        // check if operator on foggy field
        final Field field = getMap().getSpecificField(getOperatorPosition());
        if (field == null) {
            throw new HomingException("Operator is not placed on a valid field! Field is null. Operator: " + operator);
        }
        for (final HomingTargetType aimTarget : HomingTargetType.values()) {
            if (aimTarget == HomingTargetType.RETIRE) {
                // we do not want to add anything for retire target but this is needed for
                // callbacks
                continue;
            }
            this.calculatedTargets.put(aimTarget,
                    field.isFoggy() && aimTarget != HomingTargetType.MOVEMENT ? Collections.emptySet()
                            : calcTargetsFor(aimTarget));
        }
    }

    /**
     * Updates the {@link #mapPoints} after
     * {@link #updateOperation(State, Matchconfig, Character, List)} has been called
     * as well as the {@link #neighbours} and {@link #neighbourCharacters}.
     */
    private void updateShortcuts() {
        // map points
        this.mapPoints.clear();
        this.safePoints.clear(); // assume they have changed!
        final int sizeY = getMap().getField().length;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < getMap().getField()[y].length; x++) {
                populatePointToMapAndSafe(y, x);
            }
        }
        // neighbours
        this.neighbours.clear();
        getMap().getNeighboursOfSpecificField(getOperatorPosition()).ifPresent(this.neighbours::addAll);
        // neighbour characters
        this.neighbourCharacters.clear();
        this.neighbourCharacters.addAll(this.state.getCharacters().stream() // all characters registered
                // which are neighbours, cat or janitor should never stay on such a field:
                .filter(this::isNeighbourAndNeitherCatNorJanitor).collect(Collectors.toSet())); // collect and add
    }

    private boolean isNeighbourAndNeitherCatNorJanitor(final Character c) {
        return this.neighbours.contains(c.getCoordinates()) && !isCatOrJanitor(c.getCoordinates());
    }

    private void populatePointToMapAndSafe(final int y, final int x) {
        final Point p = new Point(x, y);
        this.mapPoints.add(p);
        this.safePoints.add(p);
    }

    /**
     * Returns all points (on field!) that are in passed {@code range} excluding the
     * {@code start}.
     *
     * @param start         The start point
     * @param range         The range
     * @param inLineOfSight Whether the points have to be in line of sight. This
     *                      will not check for the start and end field to be in los!
     * @return The points on the field that are in range
     */
    protected Set<Point> getPointsInRange(final Point start, final int range, final boolean inLineOfSight) {
        return this.mapPoints.stream()
                .filter(p -> Point.getKingDistance(start, p) <= range
                        && (!inLineOfSight || Point.getLine(start, p).isLineOfSight(getMap(), false)))
                .collect(Collectors.toSet());
    }

    /**
     * Returns all points (on field!) that are in passed {@code range} from the
     * operators position excluding the his one.
     *
     * @param range         The range
     * @param inLineOfSight Whether the points have to be in line of sight. This
     *                      will not check for the start and end field to be in los!
     * @return The points on the field that are in range
     */
    protected Set<Point> getPointsInRange(final int range, final boolean inLineOfSight) {
        return getPointsInRange(getOperatorPosition(), range, inLineOfSight);
    }

    /**
     * Gets the characters from {@link #state} without cat and janitor. Additionally
     * this will verify that all characters passing are positioned on the field
     *
     * @return Set of characters
     */
    protected Set<Character> getCharactersOnFieldWithoutCatAndJanitor() {
        return this.state.getCharacters().stream().filter(c -> !isCatOrJanitor(c))
                .filter(c -> c.getCoordinates().isOnField(getMap())).collect(Collectors.toSet());
    }

    /**
     * Checks whether a given point is the cat's or janitor's position.
     *
     * @param p The point
     * @return True when the point is the cat's or janitor's position, false
     *         otherwise
     */
    protected boolean isCatOrJanitor(final Point p) {
        final Point cc = this.state.getCatCoordinates();
        final Point jc = this.state.getJanitorCoordinates();
        return Objects.equals(cc, p) || Objects.equals(jc, p);
    }

    /**
     * Checks whether a given character is on the cat's or janitor's position.
     *
     * @param c The character
     * @return True when the character is on the cat's or janitor's position, false
     *         otherwise
     */
    protected boolean isCatOrJanitor(final Character c) {
        return isCatOrJanitor(c.getCoordinates());
    }

    /**
     * Checks whether the {@link #operator} owns the passed {@code property}. If the
     * property is dependent on moledie, this will be also checked.
     *
     * @param property The property to check
     * @return Whether the property is available
     */
    protected boolean operatorHasProperty(final PropertyEnum property) {
        final boolean moledieDependentProperty = property == PropertyEnum.FLAPS_AND_SEALS
                || property == PropertyEnum.TRADECRAFT || property == PropertyEnum.OBSERVATION;
        if (moledieDependentProperty && operator.getGadgetType(GadgetEnum.MOLEDIE).isPresent()) {
            return false;
        }
        return operator.getProperties().contains(property);
    }

    /**
     * Returns all neighbour points which are occupied by characters
     *
     * @return Set of points which are next to the character and occupied by another
     *         character. This will be a guaranteed HashSet!
     * @see #getAllNonFriendlyNeighbourCharacterCoordinates()
     */
    protected Set<Point> getAllNeighbourCharacterCoordinates() {
        return this.neighbourCharacters.stream().map(Character::getCoordinates)
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Returns all neighbour points which are occupied by characters not in our
     * faction.
     *
     * @return Set of points which are next to the character and occupied by another
     *         character not in the same faction. This will be a guaranteed HashSet!
     * @see #getAllNeighbourCharacterCoordinates()
     */
    protected Set<Point> getAllNonFriendlyNeighbourCharacterCoordinates() {
        return this.neighbourCharacters.stream() // for every character next to us
                .filter(this::isNotOneOfOwnCharacters) // which is not from our faction
                .map(Character::getCoordinates) // get the coordinates
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Tries to locate a character on the given point.
     *
     * @param p The point
     * @return The character if on the given point
     */
    protected Optional<Character> getCharacterAtPosition(final Point p) {
        return state == null ? Optional.empty()
                : state.getCharacters().stream().filter(c -> c.getCoordinates().equals(p)).findAny();
    }

    protected FieldMap getMap() {
        return state == null ? null : state.getMap();
    }

    protected Point getOperatorPosition() {
        return operator == null ? null : operator.getCoordinates();
    }

}