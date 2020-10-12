package de.uulm.team020.datatypes;

import java.util.LinkedList;
import java.util.List;

import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;

/**
 * CharacterDescription data type as standardized.
 * 
 * @author Florian Sihler
 * @version 1.1, 05/05/2020
 */
public class CharacterDescription implements IAmJson {

    private static final long serialVersionUID = -7849441731555961727L;

    /** Name of the Character */
    private String name;

    /** Description of the Character */
    private String description;

    /** Gender of the Character */
    private GenderEnum gender;

    /** List of Features of the Character */
    private List<PropertyEnum> features;

    /**
     * Construct a new Character-Description
     * 
     * @param name        name of the character
     * @param description description of the character
     * @param gender      gender of the character
     * @param features    features of the character
     */
    public CharacterDescription(String name, String description, GenderEnum gender, List<PropertyEnum> features) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.features = features;
    }

    /**
     * Construct a new Character-Description based on an old one with changed name
     * 
     * @param name name of the character
     * @param old  the description to copy
     */
    public CharacterDescription(String name, CharacterDescription old) {
        this.name = name;
        this.description = old.getDescription();
        this.gender = old.getGender();
        this.features = new LinkedList<>(old.getFeatures());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public List<PropertyEnum> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "CharacterDescription [description=" + description + ", features=" + features + ", gender=" + gender
                + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * equals with a loosened equals-contract! Two CharacterDescriptions are equal
     * if they contain the same elements - without respecting their order!
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterDescription))
            return false;
        CharacterDescription other = (CharacterDescription) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (features == null) {
            if (other.features != null)
                return false;
        } else if (!features.containsAll(other.features) || !other.features.containsAll(features))
            return false;
        if (gender != other.gender)
            return false;
        if (name == null) {
            return other.name == null;
        } else
            return name.equals(other.name);
    }

}