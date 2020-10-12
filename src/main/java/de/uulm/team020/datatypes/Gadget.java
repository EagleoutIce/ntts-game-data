package de.uulm.team020.datatypes;

import java.util.Objects;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * The Gadget-class as standardized
 *
 * @author Florian Sihler
 * @version 1.1, 05/06/2020
 */
public class Gadget implements IAmJson {

    private static final long serialVersionUID = -7490875687060145843L;

    GadgetEnum gadget;
    int usages;

    /**
     * Constructs a new gadget using default usage-count denoted by
     * {@link Gadget#getUsages()}. If the gadget is of type
     * {@link GadgetEnum#COCKTAIL}, a {@link Cocktail}, or if type
     * {@link GadgetEnum#WIRETAP_WITH_EARPLUGS}, a {@link WiretapWithEarplugs} will
     * be constructed. Otherwise {@link Gadget} will be used.
     * 
     * @param type The gadget type
     * @return The constructed gadget
     */
    public static Gadget constructGadget(GadgetEnum type) {
        if (type == GadgetEnum.COCKTAIL) {
            return new Cocktail(false);
        } else if (type == GadgetEnum.WIRETAP_WITH_EARPLUGS) {
            return new WiretapWithEarplugs();
        } else {
            return new Gadget(type);
        }
    }

    /**
     * Constructs a new gadget using the default usage-count denoted by
     * {@link GadgetEnum#getUsages()}.
     *
     * @param type the type of the gadget
     */
    public Gadget(GadgetEnum type) {
        this(type, type.getUsages());
    }

    /**
     * Constructs a new gadget with initial usages
     *
     * @param type   the type of the gadget
     * @param usages amount of usages left
     */
    public Gadget(GadgetEnum type, int usages) {
        this.gadget = type;
        this.usages = usages;
    }

    public GadgetEnum getGadget() {
        return gadget;
    }

    /**
     * @return Number of (theoretical) usages, -1 means infinite, but does not mean
     *         the gadget cannot be broken to vanish in a nother way.
     */
    public int getUsages() {
        return usages;
    }

    public void setUsages(int usages) {
        this.usages = usages;
    }

    /**
     * Checks if the gadget can be used from a usages-perspective
     * 
     * @return True if usages left, false otherwise
     */
    public boolean hasUsagesLeft() {
        return gadget.canBeUsed() && usages != 0;
    }

    /**
     * Decrements the usages by one.
     */
    public void decrementUsages() {
        this.usages--;
    }

    /**
     * Equals based on the contract, only the {@link #gadget}-type is important for
     * the equality, as there is only one instance of every gadget.
     *
     * @param o the object to compare to
     *
     * @return true if o is equal to this object based on the equals-contract
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof Gadget))
            return false;
        Gadget gadget1 = (Gadget) o;
        return gadget == gadget1.gadget;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gadget);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(gadget);
        if (usages >= 0) {
            builder.append("(").append(usages).append(")");
        }
        return builder.toString();
    }
}
