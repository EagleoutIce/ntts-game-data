package de.uulm.team020.validation;

/**
 * Representing if an JSON Object checked by {@link Validator}
 * is valid.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/17/2020
 */
enum iValidType {
    /** Object suffices the schema */
    IS_VALID(true),
    /** Object does not suffice the schema */
    IS_NOT_VALID(false),
    /** Object is no valid JSON*/
    IS_NOT_JSON(false);

    private boolean valid;
    
    iValidType(boolean valid) {
        this.valid = valid;
    }

    /**
     * Returns a boolean representation if the Message is valid 
     */
    public boolean get() {
        return valid;
    }

}