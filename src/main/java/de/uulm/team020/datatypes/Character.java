package de.uulm.team020.datatypes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.NumericHelper;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.Magpie;

/**
 * Represents a Character in-game. This means it will be build based on a
 * {@link CharacterInformation} and will be constantly modified by the server.
 *
 * @author Florian Sihler
 * @version 1.2b, 06/19/2020
 */
public class Character implements IAmJson {

    private static final long serialVersionUID = 7149259525639675103L;

    private static Magpie magpie = Magpie.createMagpieSafe("GameData");

    private static final String CHARACTER_TXT = "Character";
    private static final String CHARACTER_DP_TXT = "Character:";

    public static final Set<PropertyEnum> MOLEDIE_PROPERTIES = Set.of(PropertyEnum.TRADECRAFT,
            PropertyEnum.FLAPS_AND_SEALS, PropertyEnum.OBSERVATION);

    private UUID characterId;
    private String name;

    private Point coordinates;

    private int mp;
    private int ap;
    private int hp;
    private int ip;
    private int chips;

    private List<PropertyEnum> properties;

    private List<Gadget> gadgets;

    private transient boolean exfiltrated;
    private transient boolean didRetire;
    // used to buffer properties
    private transient Set<PropertyEnum> moledieBuffer = null;

    /**
     * Sets the character, no validity checks will be performed
     *
     * @param characterId The uuid of the character
     * @param name        The name of the character
     * @param coordinates The (starting) coordinates of the character
     * @param mp          The (initial) movement points of the character
     * @param ap          The (initial) action points of the character
     * @param hp          The (initial) action points of the character (no clamping)
     * @param ip          The (initial) intelligence points of the character
     * @param chips       The (initial) chips of the character
     * @param properties  The (initial) properties of the character
     * @param gadgets     The (initial) gadgets of the character
     */
    @SuppressWarnings("java:S107")
    public Character(UUID characterId, String name, Point coordinates, int mp, int ap, int hp, int ip, int chips,
            List<PropertyEnum> properties, List<Gadget> gadgets) {
        this.characterId = characterId;
        this.name = name;
        this.coordinates = coordinates;
        this.mp = mp;
        this.ap = ap;
        setHp(hp);
        this.ip = ip;
        this.chips = chips;
        this.properties = new LinkedList<>(properties);
        this.gadgets = new LinkedList<>(gadgets);
        this.didRetire = false;
        this.exfiltrated = false;
    }

    /**
     * Initializes a Character with the Gadgets
     *
     * @param info    the CharacterInfo propagated by the Server to base the
     *                creation on
     * @param gadgets the gadgets the character has, when the Game starts.
     */
    public Character(CharacterInformation info, List<Gadget> gadgets) {
        this(info.getId(), info.getName(), new Point(-1, -1), 0, 0, 100, 0, 10, info.getFeatures(), gadgets);
    }

    public UUID getCharacterId() {
        return this.characterId;
    }

    public String getName() {
        return this.name;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }

    public int getMp() {
        return this.mp;
    }

    public int getAp() {
        return this.ap;
    }

    /**
     * Will return true if the character has at least one mp or ap, is not
     * exfiltrated, and did not retire
     * <p>
     * This will never return true if the character was exfiltrated.
     * 
     * @param map The field-map this character is placed on - will be used to
     *            validate foggy fields etc.
     * 
     * @return True if the character can to another action
     */
    public boolean hasActionsLeft(FieldMap map) {
        return hasMpLeft() || hasApLeft(map);
    }

    /**
     * This will check if the character has still some ap and if the character is
     * allowed to use them
     * 
     * @param map The field-map this character is placed on - will be used to
     *            validate foggy fields etc.
     * 
     * @return True if usable AP left, false otherwise
     */
    public boolean hasApLeft(FieldMap map) {
        if (this.didRetire || this.exfiltrated) {
            return false;
        }
        Field field = map.getSpecificField(this.coordinates);
        if (field == null) {
            // no valid field but still
            return this.ap > 0;
        } else {
            // not foggy and at least 1 ap
            return !field.isFoggy() && this.ap > 0;
        }
    }

    /**
     * This will check if the character has still some mp and if the character is
     * allowed to use them.
     * 
     * @return True if usable MP left, false otherwise
     */
    public boolean hasMpLeft() {
        if (this.didRetire || this.exfiltrated) {
            return false;
        }
        return this.mp > 0;
    }

    /**
     * Called by the server to mark the character as retired, will make
     * {@link #hasActionsLeft(FieldMap)} return false.
     */
    public void retire() {
        this.didRetire = true;
    }

    public int getHp() {
        return this.hp;
    }

    public int getIp() {
        return this.ip;
    }

    public int getChips() {
        return this.chips;
    }

    public List<PropertyEnum> getProperties() {
        return this.properties;
    }

    /**
     * Checks if the character has a gadget of a certain type
     * 
     * @param type The type to search for
     * @return The gadget if found, empty optional otherwise
     */
    public Optional<Gadget> getGadgetType(GadgetEnum type) {
        return this.gadgets == null ? Optional.empty()
                : this.gadgets.stream().filter(g -> g.getGadget().equals(type)).findAny();
    }

    public List<Gadget> getGadgets() {
        return this.gadgets;
    }

    public boolean isExfiltrated() {
        return this.exfiltrated;
    }

    public Set<PropertyEnum> getMoledieBuffer() {
        return this.moledieBuffer;
    }

    /**
     * Sets this character to be exfiltrated, please note, that this will <b>not</b>
     * perform the replace-routine. This has to be done separately
     */
    public void exfiltrate() {
        this.exfiltrated = true;
        this.hp = 1;
    }

    /**
     * Resets the characters exfiltration. This won't heal the character. It will
     * just remove the exfiltrated tag
     */
    public void clearExfiltration() {
        this.exfiltrated = false;
    }

    /**
     * Sets the Hp of the character, they will be capped between 0 and 100
     *
     * @param newHp The newHP the character should get
     *
     * @return True, if the character is ready for an {@link #exfiltrate()
     *         exfiltration} (hp==0), else otherwise
     */
    public boolean setHp(int newHp) {
        this.hp = NumericHelper.getInBounds(newHp, 0, 100);
        return hp == 0;
    }

    /**
     * Calls {@link #setHp(int)} with the proper maximum health
     */
    public void healMax() {
        setHp(100);
    }

    /**
     * The Hp that should be removed
     *
     * @param deltaHp hp to remove
     *
     * @return True, if the character is ready for an {@link #exfiltrate()
     *         exfiltration} (hp==0), else otherwise
     * 
     * @see #setHp(int)
     */
    public boolean removeHp(int deltaHp) {
        return setHp(getHp() - deltaHp);
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    /**
     * Reset the mp and ap amount (recalculating) if the character is not
     * exfiltrated.
     * <p>
     * This implementation will use {@link RandomHelper#flip()} as a flip-supplier
     * 
     * @see #resetMpAp(BooleanSupplier, BooleanSupplier)
     */
    public void resetMpAp() {
        resetMpAp(RandomHelper::flip, RandomHelper::flip);
    }

    /**
     * Reset the mp and ap amount (recalculating) if the character is not
     * exfiltrated.
     * 
     * @param flip1 the supplier for the first flip
     * @param flip2 the supplier for the second flip
     */
    public void resetMpAp(BooleanSupplier flip1, BooleanSupplier flip2) {
        // this will pass for an exfiltrated character
        if (exfiltrated) {
            return;
        }
        this.mp = 2;
        this.ap = 1;
        this.didRetire = false; // can do stuff again

        if (this.properties.contains(PropertyEnum.SLUGGISHNESS)) {
            this.mp = 1;
        } else if (this.properties.contains(PropertyEnum.NIMBLENESS)) {
            this.mp = 3;
        }

        if (this.properties.contains(PropertyEnum.SPRYNESS)) { //
            this.ap = 2;
        }

        if (this.properties.contains(PropertyEnum.AGILITY)) {
            // give one at random
            if (flip1.getAsBoolean()) {
                mp += 1;
                magpie.writeDebug(CHARACTER_DP_TXT + getName() + " has agility and got one mp! Has now: " + mp,
                        CHARACTER_TXT);
            } else {
                ap += 1;
                magpie.writeDebug(CHARACTER_DP_TXT + getName() + " has agility and got one ap! Has now: " + ap,
                        CHARACTER_TXT);
            }
        }

        if (this.properties.contains(PropertyEnum.PONDEROUSNESS)) {
            // remove one at random
            if (flip2.getAsBoolean()) {
                mp -= 1;
                magpie.writeDebug(CHARACTER_DP_TXT + getName() + " has ponderousness and lost one mp! Has now: " + mp,
                        CHARACTER_TXT);
            } else {
                ap -= 1;
                magpie.writeDebug(CHARACTER_DP_TXT + getName() + "  has ponderousness and lost one ap! Has now: " + ap,
                        CHARACTER_TXT);
            }
        }
    }

    /**
     * Removes exactly one ap and returns the remaining amount
     * <p>
     * There will be no error if the character does not have enough ap. But it will
     * return a negativ amount in that case of course.
     * 
     * @return Remaining AP of the character after the operation.
     */
    public int removeAp() {
        return removeAp(1);
    }

    /**
     * Removes {@code n} ap and returns the remaining amount
     * <p>
     * There will be no error if the character does not have enough ap. But it will
     * return a negativ amount in that case of course.
     * 
     * @param n The amount of ap to remove
     * 
     * @return Remaining AP of the character after the operation.
     */
    public int removeAp(int n) {
        ap -= n;
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public void addIp(int ip) {
        this.ip += ip;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    /**
     * Moves the character with {@link Point#move(int, int)}
     * <p>
     * This operation will not perform the movement if the you do not have enough
     * movement-points or if the movement is more than one field.
     * </p>
     * 
     * @param deltaX relative x
     * @param deltaY relative y
     * 
     * @return True, if the move was possible
     */
    public boolean move(int deltaX, int deltaY) {
        if (this.mp < 1) // not enough movement points
            return false;
        Point possibleTarget = new Point(this.coordinates).move(deltaX, deltaY);
        if (!Point.isNeighbour(this.coordinates, possibleTarget)) // is no neighbor
            return false;
        this.coordinates.move(deltaX, deltaY);
        return true;
    }

    /**
     * Moves the character with {@link Point#move(int, int)} and checks if this move
     * is valid on the given {@link FieldMap}. This will include:
     * <ul>
     * <li>out of bord (leaving the board dimensions</li>
     * <li>target field is not valid</li>
     * </ul>
     *
     * The move operation won't be performed if it is invalid targeted to an invalid
     * field or if you have no movement-points left.
     *
     * @param deltaX Relative x
     * @param deltaY Relative y
     * @param field  The field to work upon
     *
     * @return True if the operation was performed, False otherwise
     */
    public boolean move(int deltaX, int deltaY, FieldMap field) {
        Point possibleTarget = new Point(this.coordinates).move(deltaX, deltaY);
        Field target = field.getSpecificField(possibleTarget);
        if (target == null) // out of field-bounds
            return false;
        if (!target.getState().isWalkable()) // target cannot be walked on
            return false;
        return move(deltaX, deltaY);
    }

    /**
     * Add a new property
     *
     * @param property the property to add
     *
     * @return True if it could be added, False if the character already possessed
     *         this property
     */
    public boolean addProperty(PropertyEnum property) {
        if (this.properties.contains(property))
            return false;
        this.properties.add(property);
        return true;
    }

    /**
     * Removes a property
     *
     * @param propertyEnum the property to remove
     * @return True if the property was removed, False if the character did not
     *         possess this property
     */
    public boolean removeProperty(PropertyEnum propertyEnum) {
        return this.properties.remove(propertyEnum);
    }

    /**
     * Add a new gadget. This will automatically call {@link #getMoledie(Gadget)} if
     * the gadget is a moledie
     *
     * @param gadget the gadget to add
     *
     * @return True if it could be added, False if the character already possessed
     *         this gadget
     */
    public boolean addGadget(Gadget gadget) {
        if (this.gadgets.contains(gadget))
            return false;
        if (gadget.getGadget() == GadgetEnum.MOLEDIE) {
            getMoledie(gadget);
        } else {
            this.gadgets.add(gadget);
        }
        return true;
    }

    /**
     * Removes a gadget
     *
     * @param gadget the gadget to remove
     * @return True if the gadget was removed, False if the character did not
     *         possess this gadget
     */
    public boolean removeGadget(Gadget gadget) {
        if (gadget.getGadget() == GadgetEnum.MOLEDIE) {
            return this.removeMoledie() != null;
        } else {
            return this.gadgets.remove(gadget);
        }
    }

    /**
     * Will remove the properties: Tradecraft, Flaps and Seals and observation, from
     * the stats. They will get buffered.
     * 
     * @param moledie The moledie to receive
     * 
     * @return true if buffered, false if the character already has the moledie
     *         (which would be weird, to be honest)
     * 
     * @see #removeMoledie()
     */
    public boolean getMoledie(Gadget moledie) {
        if (this.moledieBuffer != null) {
            return false;
        }
        this.gadgets.add(moledie);
        getMoledie();
        return true;
    }

    /**
     * Basically works like {@link #getMoledie(Gadget)} but will not add any gadget
     * or change the gadgets of the character - it can be used for character
     * initialization. Do not use without guards as this could mean the buffer gets
     * lost!
     */
    public void getMoledie() {
        this.moledieBuffer = new HashSet<>();
        for (PropertyEnum propertyEnum : MOLEDIE_PROPERTIES) {
            if (this.properties.remove(propertyEnum)) {
                moledieBuffer.add(propertyEnum);
            }
        }
    }

    /**
     * Will remove the moledie from the gadgets of this character. The character
     * will 're-get' all previously removed properties.
     * 
     * @return The moledie removed. Please note, that this will be null if the
     *         character did not hold the moledie!
     */
    public Gadget removeMoledie() {
        Optional<Gadget> mayMoledie = getGadgetType(GadgetEnum.MOLEDIE);
        if (!mayMoledie.isPresent()) {
            return null;
        }
        if (moledieBuffer != null) {
            this.properties.addAll(moledieBuffer);
        }
        this.moledieBuffer = null;
        Gadget moledie = mayMoledie.get();
        this.gadgets.remove(moledie);
        return moledie;
    }

    /**
     * Is the character retired? This is bound to {@link #retire()} and is probably
     * not useful if you do not set it yourself
     * 
     * @return True if the character did retire false otherwise
     */
    public boolean isRetired() {
        return this.didRetire;
    }

    @Override
    public String toString() {
        return "Character [characterId=" + characterId + ", name='" + name + '\'' + ", coordinates=" + coordinates
                + ", mp=" + mp + ", ap=" + ap + ", hp=" + hp + "/100" + ", ip=" + ip + ", chips=" + chips
                + ", properties=" + properties + ", gadgets=" + gadgets + ", exfiltrated=" + exfiltrated + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Character))
            return false;
        Character character = (Character) o;
        return Objects.equals(characterId, character.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

}
