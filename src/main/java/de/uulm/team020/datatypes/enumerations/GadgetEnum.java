package de.uulm.team020.datatypes.enumerations;

import de.uulm.team020.datatypes.Cocktail;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.WiretapWithEarplugs;

/**
 * Representing the Gadget-Type
 * 
 * @author Florian Sihler
 * @version 1.3, 06/07/2020
 */
public enum GadgetEnum {
    HAIRDRYER("Allows to dry the character or another character standing close to it.", -1),
    MOLEDIE("Disables some properties, can be thrown in LOS to remove.", -1),
    TECHNICOLOUR_PRISM("Can be used to invert probabilities on a table once.", 1),
    BOWLER_BLADE(
            "Will be lost after throwing (attack a character in blocking LOS). Can be collected when lying on a field.",
            -1),
    MAGNETIC_WATCH("Protects yourself from an BowlerBlade-Attack.", -1),
    POISON_PILLS("Poisons a cocktail placed on a neighbour field or held by an neighbour character.", 5),
    LASER_COMPACT("Can destroy a cocktail in LOS by chance.", -1),
    ROCKET_PEN("Destroys any and surrounding walls in LOS, will deal Damage to characters in range.", 1),
    GAS_GLOSS("Can Deal damage to a neighbour Character", 1),
    MOTHBALL_POUCH("Thrown into a fireplace, this will deal damage in a configured range", 5),
    FOG_TIN("Can be thrown to place fog on target and surrounding fields. Will stay there for this and the next two rounds and prevent characters from taking actions",
            1),
    GRAPPLE("Acquire any Gadget/Cocktail in LOS which is not hold by a character", -1),
    WIRETAP_WITH_EARPLUGS("Can be cast once on a nearby agent to gain his share of IP-points.", 1,
            WiretapWithEarplugs.class),
    DIAMOND_COLLAR("The mighty gadget to be brought to the mighty cat of mighty cuteness.", -1),
    JETPACK("Can bring you to any free field.", 1),
    CHICKEN_FEED("Can be used on a nearby person to equal ip points from an enemy, non-neutral character", 1),
    NUGGET("Can be used to recruit an npc. If used on an enemy this will give this item to him without consumption.",
            1),
    MIRROR_OF_WILDERNESS("Swap IP with any nearby agent. Can fail and break.", -1),
    POCKET_LITTER("The character appears as an NPC when observed.", -1),
    ANTI_PLAGUE_MASK("The wearer of this mask may receive 10 hp on start of it's turn.", -1),
    COCKTAIL("Placed on bar-tables, can be drunk, can be poisoned, can be used to make other agents wet", 1,
            Cocktail.class);

    private int usages;
    private boolean hasSubclass;
    private String description;
    private Class<? extends Gadget> targetClass;

    GadgetEnum(String description, int usages) {
        this(description, usages, Gadget.class);
    }

    /**
     * Is there a subclass representing that gadget?
     */
    GadgetEnum(String description, int usages, Class<? extends Gadget> targetClass) {
        this.description = description;
        this.usages = usages;
        this.hasSubclass = targetClass != Gadget.class;
        this.targetClass = targetClass;
    }

    public boolean hasSubclass() {
        return this.hasSubclass;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Checks if the gadget can be used from an theoretical perspective
     * 
     * @return True, if the gadget is not the diamond collar
     */
    public boolean canBeUsed() {
        return this != DIAMOND_COLLAR;
    }

    /**
     * Usage count
     * 
     * @return How often this gadget can be used initially, is -1 if
     *         infinite/doesn't matter
     */
    public int getUsages() {
        return this.usages;
    }

    /**
     * Target class for this gadget type will be used on deserialization
     * 
     * @return The targetClass desired
     */
    public Class<? extends Gadget> getTargetClass() {
        return targetClass;
    }
}