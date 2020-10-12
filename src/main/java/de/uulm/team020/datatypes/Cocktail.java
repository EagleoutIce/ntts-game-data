package de.uulm.team020.datatypes;

import java.util.Objects;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * Representation of the cocktail-Gadget to identify the target and if it works.
 * 
 * maybe make equals?
 * 
 * @author Florian Sihler
 * @version 1.0, 04/23/2020
 */
public class Cocktail extends Gadget {

    private static final long serialVersionUID = -329011568798412029L;

    private boolean isPoisoned;

    /**
     * Construct the new Gadget
     * 
     * @param isPoisoned Is the gadget poisoned
     */
    public Cocktail(boolean isPoisoned) {
        super(GadgetEnum.COCKTAIL);
        this.isPoisoned = isPoisoned;
    }

    public void setPoisoned(boolean isPoisoned) {
        this.isPoisoned = isPoisoned;
    }

    public boolean getPoisoned() {
        return this.isPoisoned;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(isPoisoned);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Cocktail)) {
            return false;
        }
        Cocktail other = (Cocktail) obj;
        return isPoisoned == other.isPoisoned;
    }

}