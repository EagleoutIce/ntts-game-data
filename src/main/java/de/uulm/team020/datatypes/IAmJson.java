package de.uulm.team020.datatypes;

import java.io.Serializable;

import de.uulm.team020.validation.GameDataGson;

/**
 * Classes implementing this interface will offer the convenient functions:
 * 
 * <ul>
 * <li>{@code toJson() : String}
 * <p>
 * to get the json-representation of the class</li>
 * </ul>
 */
public interface IAmJson extends Serializable {
    /**
     * Will convert the Object to an json object
     * 
     * @return String representation of the Object
     */
    default String toJson() {
        return GameDataGson.toJson(this);
    }
}