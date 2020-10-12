package de.uulm.team020.datatypes;

import com.google.gson.annotations.SerializedName;
import de.uulm.team020.datatypes.blueprints.AbstractGameField;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a Scenario, loaded by a json-File and used by the Server
 * or the Editor to construct and Build the Map to play on.
 * <p>
 * Example:
 * <pre>
 * {
 *    "scenario": [
 *       ["FREE", "WALL",      "WALL"],
 *       ["WALL", "FIREPLACE", "WALL"],
 *       ["WALL", "FREE",      "FREE"],
 *       ["WALL", "FREE"             ],
 *       ["WALL", "WALL",      "WALL"]
 *    ]
 *}
 * </pre>
 * Every field has to be one of {@link FieldStateEnum}
 * 
 * @author Florian Sihler
 * @version 1.1, 03/21/2020
 */
public class Scenario extends AbstractGameField<FieldStateEnum> implements IAmJson {

    private static final long serialVersionUID = -2495023044572402233L;

    @SerializedName(value =  "scenario", alternate = {"field"})
    private FieldStateEnum[][] field;

    /**
     * A convenient constructor
     * 
     * @param scenario the old scenario to use
     */
    public Scenario(final FieldStateEnum[][] scenario) {
        this.field = super.createDataCopy(scenario);
    }


    /**
     * A convenient copy-constructor
     * 
     * @param scenario the old scenario to use
     */
    public Scenario(final Scenario scenario) {
        this(scenario.getScenario());
    }

    /**
     * @return the capsuled scenario
     */
    public FieldStateEnum[][] getScenario() {
        return this.getField();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Scenario))
            return false;

        return super.equals(obj);
    }

	@Override
	public String toString() {
		return "Scenario [\n" + super.toString() + "\n]";
	}

    @Override
    public FieldStateEnum[][] getField() {
        return this.field;
    }

    @Override
    public FieldStateEnum[][] createCopy(FieldStateEnum[][] field) {
        // We will further remove null values to pass 'null'-lines
        return Arrays.stream(field).filter(Objects::nonNull).map(FieldStateEnum[]::clone).toArray(FieldStateEnum[][]::new);
    }
}