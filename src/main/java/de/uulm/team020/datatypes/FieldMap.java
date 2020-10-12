package de.uulm.team020.datatypes;

import java.util.Arrays;
import java.util.Objects;

import de.uulm.team020.datatypes.blueprints.AbstractGameField;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;

/**
 * Represents the GameBoard (as a board) on the base of {@link Field} it
 * represents a two-dimensional array of Fields.
 * <p>
 * The 'pre-game' brother is the {@link Scenario}.
 * </p>
 *
 * @author Florian Sihler
 * @version 1.0, 03/29/2020
 */
public class FieldMap extends AbstractGameField<Field> implements IAmJson {

    private static final long serialVersionUID = 1L;

    private Field[][] map;

    /**
     * Copy-Constructor for the underlying field
     *
     * @param map the map to copy from
     */
    public FieldMap(Field[][] map) {
        this.map = createDataCopy(map);
    }

    /**
     * Copy-Constructor
     *
     * @param map the map to copy from
     */
    public FieldMap(FieldMap map) {
        this(map.getField());
    }

    public Field[][] getMap() {
        return getField();
    }

    @Override
    public String toString() {
        return "FieldMap [\n" + super.toString() + "\n]";
    }

    /**
     * Create the Copy without populating the data-registers
     *
     * @param field the (old) field
     * @return a copy of the supplied field
     */
    @Override
    public Field[][] createCopy(Field[][] field) {
        // We will further remove null values to pass 'null'-lines
        return Arrays.stream(field).filter(Objects::nonNull).map(Field[]::clone).toArray(Field[][]::new);
    }

    /**
     * @return the capsuled field as it is, returns reference if applicable but
     *         never returns a deep-copy!
     */
    @Override
    public Field[][] getField() {
        return map;
    }

    @Override
    protected String print(Field field) {
        if (field == null)
            return "<null>";
        StringBuilder builder = new StringBuilder();
        // Is it foggy?
        if (field.isFoggy()) {
            builder.append("?");
        }
        // Field id:
        if (field.getState() == FieldStateEnum.FREE && field.getGadget() != null) {
            builder.append("[").append(field.getGadget().toString()).append("]");
        } else {
            builder.append(field.getState().toString());
        }
        if (field.getState() == FieldStateEnum.SAFE) {
            builder.append("(").append(field.getSafeIndex()).append(")");
        } else if (field.getState() == FieldStateEnum.ROULETTE_TABLE) {
            printRouletteTable(field, builder);
        }
        return builder.toString();
    }

    private void printRouletteTable(Field field, StringBuilder builder) {
        if (field.isDestroyed()) {
            builder.append("<D>");
        } else {
            builder.append("<");
            if (field.isInverted()) {
                builder.append("!,");
            }
            builder.append(field.getChipAmount()).append(">");
        }
    }

    @Override
    protected int getPadding() {
        return 20;
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
        if (!(obj instanceof FieldMap))
            return false;
        return super.equals(obj);
    }
}
