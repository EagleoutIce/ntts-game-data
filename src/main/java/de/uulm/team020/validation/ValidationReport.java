package de.uulm.team020.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uulm.team020.logging.Magpie;

/**
 * Report returned by a request to {@link Validator}
 * 
 * @author Florian Sihler
 * @version 2.0, 03/19/2020
 */
public class ValidationReport implements Serializable {

    private static final long serialVersionUID = -773893520816786151L;

    /** Logging-Instance */
    private static Magpie magpie = Magpie.createMagpieSafe("Validator");

    /** Set of all Reasons why the document is invalid */
    private List<String> reasons = new ArrayList<>(1);

    /** Flag indicating, if the Object is Valid (is {@link iValidType#IS_VALID} if so) */
    private iValidType isValid;

    /**
     * Construct a new Validation-Report
     *  
     * @param reason textual representation
     * @param isValid is the Object valid?
     */
    public ValidationReport(String reason, iValidType isValid) {
        this.reasons.add(reason); 
        this.isValid = isValid;
        magpie.writeDebug("Building new " + this, "Validation");
    }


    /**
     * Construct a new Validation-Report
     *  
     * @param reasons textual representation of all reasons
     * @param isValid is the Object valid?
     */
    public ValidationReport(List<String> reasons, iValidType isValid) {
        this.reasons = reasons;
        this.isValid = isValid;
        magpie.writeDebug("Building new " + this, "Validation");
    }


    /**
     * Constructs a successful report, with no message/reason attached
     */
    public ValidationReport() {
        this("", iValidType.IS_VALID);
    }

    /**
     * @return the first reason, if available - returns 'null' otherwise
     */
    public String getReason() {
        if (!this.reasons.isEmpty())
            return this.reasons.get(0);
        return null;
    }


    /**
     * @return all reasons
     */
    public List<String> getReasons() {
        return this.reasons;
    }

    /**
     * @return is the document valid or even json?
     */
    public iValidType getIsValid() {
        return isValid;
    }

    /**
     * @return true if the document is valid, false otherwise
     */
    public boolean isValid() {
        return isValid.get();
    }

    @Override
    public String toString() {
        return "ValidationReport [isValid=" + isValid + ", reasons=" + reasons + "]";
    }

}