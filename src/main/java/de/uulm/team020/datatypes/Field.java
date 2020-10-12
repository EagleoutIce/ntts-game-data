package de.uulm.team020.datatypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.exceptions.SafeFieldException;

/**
 * Will contain the current state of a field while playing. This excludes
 * entities (Characters, ...) staying on this field. They are not included to
 * provide more flexibility.
 *
 * @author Florian Sihler
 * @version 1.3, 06/20/2020
 */
public class Field implements IAmJson, IAmWalkable, IAmTransparent, IAmAState {

    private static final String FIELD_TYPE_TXT = "Field type '";

    private static final long serialVersionUID = 6629007101071516498L;

    private FieldStateEnum state;
    private Gadget gadget;

    private boolean isDestroyed;
    private boolean isInverted;
    private int chipAmount;

    private int safeIndex;

    private boolean isFoggy;

    private transient int foggyRoundsRemaining = 0;
    private boolean isUpdated;

    /**
     * Construct a new field, this method should not be called to construct a Field,
     * as it is to be base on a {@link Scenario#getField()} and therefore a
     * {@link FieldStateEnum}-basis.
     * <p>
     * This constructor is here for testing-purposes and to block the default one
     * </p>
     *
     * @param state       The {@link FieldStateEnum} type of the field.
     * @param gadget      References the {@link Gadget} present on the field, null
     *                    if none.
     * @param isDestroyed only relevant if {@link #state} is
     *                    {@link FieldStateEnum#ROULETTE_TABLE}. Indicates if the
     *                    table has been destroyed.
     * @param isInverted  only relevant if {@link #state} is
     *                    {@link FieldStateEnum#ROULETTE_TABLE}. Indicates if the
     *                    probabilities have been inverted.
     * @param chipAmount  only relevant if {@link #state} is
     *                    {@link FieldStateEnum#ROULETTE_TABLE}. Indicates the
     *                    number of chips which are currently set.
     * @param safeIndex   only relevant if {@link #state} is
     *                    {@link FieldStateEnum#SAFE}. Indicates the number of the
     *                    safe.
     * @param isFoggy     is this field swayed by the {@link GadgetEnum#FOG_TIN}?
     * @param isUpdated   was this field changed in the last GameState-report?
     */
    protected Field(FieldStateEnum state, Gadget gadget, boolean isDestroyed, boolean isInverted, Integer chipAmount,
            Integer safeIndex, boolean isFoggy, boolean isUpdated) {
        if (state == FieldStateEnum.SAFE && safeIndex <= 0)
            throw new SafeFieldException("A Safe has to have a valid safeIndex > 0");
        this.state = state;
        this.gadget = gadget;
        this.isDestroyed = isDestroyed;
        this.isInverted = isInverted;
        this.chipAmount = chipAmount;
        this.safeIndex = safeIndex;
        this.isFoggy = isFoggy;
        this.isUpdated = isUpdated;
    }

    /**
     * Constructs a new Field. The state should be without additional information on
     * construction. If the field is going to be a {@link FieldStateEnum#SAFE} use
     * {@link #Field(Integer)} instead.
     *
     * @param state The type of the field.
     * 
     * @see #Field(FieldStateEnum, Gadget, boolean, boolean, Integer, Integer,
     *      boolean, boolean)
     */
    public Field(FieldStateEnum state) {
        this(state, null, false, false, 0, 0, false, false);
    }

    /**
     * Constructs a Safe-Field with the supplied field. The {@link #state} will be
     * set to {@link FieldStateEnum#SAFE}.
     *
     * @param safeIndex index of the state, should start with 1
     *
     * @see #Field(FieldStateEnum, Gadget, boolean, boolean, Integer, Integer,
     *      boolean, boolean)
     */
    public Field(Integer safeIndex) {
        this(FieldStateEnum.SAFE, null, false, false, 0, safeIndex, false, false);
    }

    public FieldStateEnum getState() {
        return state;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean isInverted() {
        return isInverted;
    }

    public int getChipAmount() {
        return chipAmount;
    }

    public int getSafeIndex() {
        return safeIndex;
    }

    public boolean isFoggy() {
        return isFoggy;
    }

    public boolean getUpdated() {
        return isUpdated;
    }

    /**
     * Simple helper to identify if the configuration of this Field is valid or if
     * it violates a rule.
     * <p>
     * If it is valid, the returned List will be empty, otherwise it will contain
     * all errors found while validating. So if you're just interested in checking
     * for validity use: {@code isValid().isEmpty()}
     * </p>
     * This is somewhat of a first draft, there may be changes in the future
     * altering the 'validation'-principle. It should not be up to the client to
     * invalidate a field.
     *
     * @return list of errors, if there are any
     *
     * @see #isValid()
     */
    public List<String> checkValidity() {
        List<String> errors = new LinkedList<>();
        switch (state) {
            case FIREPLACE:
            case BAR_TABLE:
            case FREE:
            case WALL:
            case BAR_SEAT:
            case SAFE:
                assureThatNotDestroyedInvertedOrChipped(errors);
                assureThatIsEitherNoSafeOrChipped(errors);
                break;
            case ROULETTE_TABLE:
                assureThatIsEitherNoSafeOrChipped(errors);
                break;
        }
        return errors;
    }

    private void assureThatIsEitherNoSafeOrChipped(List<String> errors) {
        if (state != FieldStateEnum.SAFE && safeIndex > 0)
            errors.add(FIELD_TYPE_TXT + state + "' can't have a safeIndex.");
        else if (state == FieldStateEnum.SAFE && safeIndex <= 0) // should be caught on construction
            errors.add(FIELD_TYPE_TXT + state + "' has to have a safeIndex.");
    }

    private void assureThatNotDestroyedInvertedOrChipped(List<String> errors) {
        if (isDestroyed)
            errors.add(FIELD_TYPE_TXT + state + "' can't be destroyed.");
        if (isInverted)
            errors.add(FIELD_TYPE_TXT + state + "' can't be inverted.");
        if (chipAmount > 0)
            errors.add(FIELD_TYPE_TXT + state + "' can't hold any chips.");
    }

    /**
     * Shortcut for {@link #checkValidity()} chained with {@code isEmpty()}.
     *
     * @return True if the field is valid, false otherwise.
     *
     * @see #checkValidity()
     */
    public boolean isValid() {
        return checkValidity().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Field))
            return false;
        Field field = (Field) o;
        return state == field.state && Objects.equals(gadget, field.gadget)
                && Objects.equals(isDestroyed, field.isDestroyed) && Objects.equals(isInverted, field.isInverted)
                && Objects.equals(chipAmount, field.chipAmount) && Objects.equals(safeIndex, field.safeIndex)
                && Objects.equals(isFoggy, field.isFoggy) && Objects.equals(isUpdated, field.isUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, gadget, isDestroyed, isInverted, chipAmount, safeIndex, isFoggy, isUpdated);
    }

    @Override
    public String toString() {
        return "Field [state=" + state + ", gadget=" + gadget + ", isDestroyed=" + isDestroyed + ", isInverted="
                + isInverted + ", chipAmount=" + chipAmount + ", safeIndex=" + safeIndex + ", isFoggy=" + isFoggy
                + ", isUpdated=" + isUpdated + ']';
    }

    public void setState(FieldStateEnum state) {
        this.state = state;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }

    public void setChipAmount(Integer chipAmount) {
        this.chipAmount = chipAmount;
    }

    public void setFoggy(boolean foggy) {
        isFoggy = foggy;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Override
    public boolean isWalkable() {
        return this.state.isWalkable();
    }

    /**
     * This allows to set the safe-index later, this shouldn't be used after ghe
     * game Started, but <i>could</i> if there are reasons to do this in the future.
     * 
     * @param num The new safe number to use
     */
    public void setSafeIndex(int num) {
        if (this.state != FieldStateEnum.SAFE)
            throw new SafeFieldException("Only fields containing a safe can hae a safeIndex");
        this.safeIndex = num;
    }

    @Override
    public boolean blocksLOS() {
        return this.state.blocksLOS() || this.isFoggy;
    }

    public int getFoggyRoundsRemaining() {
        return foggyRoundsRemaining;
    }

    public void setFoggyRoundsRemaining(int foggyRoundsRemaining) {
        this.foggyRoundsRemaining = foggyRoundsRemaining;
    }
}
