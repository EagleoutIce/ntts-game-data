package de.uulm.team020.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * Representation of the wiretapWithEarplugs-Gadget to identify the target and
 * if it works.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/23/2020
 */
public class WiretapWithEarplugs extends Gadget {

    private static final long serialVersionUID = -4935014348996183148L;

    private boolean working;
    private UUID activeOn;

    /**
     * Construct the new Gadget
     */
    public WiretapWithEarplugs() {
        super(GadgetEnum.WIRETAP_WITH_EARPLUGS);
        working = true;
        activeOn = null;
    }

    public void setActiveOn(UUID id) {
        this.activeOn = id;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public boolean getWorking() {
        return this.working;
    }

    public UUID getActiveOn() {
        return this.activeOn;
    }
}