package de.uulm.team020.datatypes;

import java.util.Arrays;
import java.util.Objects;

import de.uulm.team020.datatypes.blueprints.AbstractGameField;

/**
 * Just a simple-no-extra implementation of an {@link AbstractGameField}
 * 
 * @author Florian Sihler
 * @version 1.0, 04/17/2020
 */
public class SimpleGameField<T extends IAmWalkable> extends AbstractGameField<T> {

    private static final long serialVersionUID = -6254429474368695357L;

    private T[][] field;

    /**
     * A convenient Constructor
     * 
     * @param gameField the old scenario to use
     */
    public SimpleGameField(final T[][] gameField) {
        this.field = super.createDataCopy(gameField);
    }

    /**
     * A convenient Copy-Constructor
     * 
     * @param gameField the old scenario to use
     */
    public SimpleGameField(final SimpleGameField<T> gameField) {
        this(gameField.getField());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[][] createCopy(T[][] field) {
        return (T[][]) Arrays.stream(field).filter(Objects::nonNull).map(IAmWalkable[]::clone)
                .toArray(IAmWalkable[][]::new);
    }

    @Override
    public T[][] getField() {
        return this.field;
    }
}