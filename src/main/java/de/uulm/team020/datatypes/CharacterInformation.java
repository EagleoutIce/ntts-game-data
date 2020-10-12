package de.uulm.team020.datatypes;

import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A normal {@link CharacterDescription} wrapped with the UUID. This will be the
 * data, the server sends to the client.
 * 
 * @author Florian Sihler
 * @version 1.2, 03/26/2020
 */
public class CharacterInformation extends CharacterDescription {

    private static final long serialVersionUID = -4966305196001111615L;

    /** The UUID that the server assigns to the Character */
    private UUID characterId;

    /**
     * Creates a new character.
     * <p>
     * The scope is protected to avoid the creation of 'new' Characters without
     * having a {@link CharacterDescription} to back them up.
     * 
     * @param name name of the character
     * @param description description of the character
     * @param gender gender of the character
     * @param features features of the character
     * @param characterId the UUID of the character
     */
    protected CharacterInformation(String name, String description, GenderEnum gender, List<PropertyEnum> features, UUID characterId) {
        super(name, description, gender, features);
        this.characterId = characterId;
    }

    /**
     * Creates a new character based on a {@link CharacterDescription}
     * 
     * @param character the old CharacterDescription
     * @param characterId the UUID of the character
     */
    public CharacterInformation(CharacterDescription character, UUID characterId) {
        // Maybe do this with a copy-constructor?
        super(character.getName(), character.getDescription(), character.getGender(), character.getFeatures());
        this.characterId = characterId;
    }
    

    @Override
    public String toString() {
        return "CharacterInformation [character=" + super.toString() + ", characterId=" + characterId + "]";
    }

    /**
     * @return The Character-UUID set by the server
     */
    public UUID getId() {
        return characterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((characterId == null) ? 0 : characterId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof CharacterInformation))
            return false;
        CharacterInformation other = (CharacterInformation) obj;
        if (characterId == null) {
            if (other.characterId != null)
                return false;
        } else if (!characterId.equals(other.characterId)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Will convert an array of {@link CharacterInformation} to it's
     * JSON-representation
     * 
     * @param data the data to convert
     * @return The JSON-Array
     */
    public static String arrayToJson(CharacterInformation[] data) {
        return "[" +
                    String.join(", ", Arrays.stream(data).map(IAmJson::toJson).toArray(String[]::new)) +
                "]";
    }
}