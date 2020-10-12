package de.uulm.team020.validation;


import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.uulm.team020.logging.Magpie;

/**
 * This Class will be used to validate JSON-Objects against their JSON Schema.
 * It wraps around the json schema validation by everit handled with the
 * Apache 2 License.
 * 
 * @author Florian Sihler
 * @version 2.1, 03/26/2020
 */
public class Validator {

    // Hide the public one
    private Validator() {}

    private static final String VALIDATION = "Validation";

    /** Logging-Instance */
    private static Magpie magpie = Magpie.createMagpieSafe("Validator");

    /**
     * As the standard-committee proposed json - documents only containing arrays are also valid.
     * Therefore this method tries to find the appropriate JSON-Type for this document.
     * <p>
     * As the java standard proposes: it's easier to ask for forgiveness....
     * 
     * @param json the json data to check
     * 
     * @return if possible a {@link JSONObject} or a {@link JSONArray}, null otherwise (throws exception).
     */
    private static Object tryCastIsObjectOrArray(String json){
        Object obj;
        try {
            obj = new JSONObject(json);
        } catch(JSONException ignored) {
            magpie.writeDebug("Objekt ist schonmal kein gültiges JSON-Objekt, prüfe auf JSON-Array:", VALIDATION);
            // maybe it is a String?
            obj = new JSONArray(json);
        }
        return obj;
    }

    /**
     * Validates a JSON-Object given by string-representation with a schema
     * given by string-representation as well. The schema itself won't be checked
     * for validity this can be implemented in the future.
     * 
     * @param json the json data
     * @param schema the schema data
     * @return the report, see {@link ValidationReport}
     */
    public static ValidationReport validateObject(String json, Schema schema) {
        if(schema == null)
            throw new NullPointerException("You cant validate the Object without a schema");
        magpie.writeDebug("Validating schema '" + schema.getTitle() + "' with " + json, VALIDATION);
        try {
            Object obj = tryCastIsObjectOrArray(json);
            
            schema.validate(obj);
            
            return new ValidationReport("", iValidType.IS_VALID);

        } catch (ValidationException ex) {
            magpie.writeExceptionShort(ex, VALIDATION);
            return new ValidationReport(ex.getAllMessages(), iValidType.IS_NOT_VALID);
        } catch (Exception ex) {
            magpie.writeExceptionShort(ex, VALIDATION);
            return new ValidationReport(ex.getMessage(), iValidType.IS_NOT_JSON);
        }
    }
    


}