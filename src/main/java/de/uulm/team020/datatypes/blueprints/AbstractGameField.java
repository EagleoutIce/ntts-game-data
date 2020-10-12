package de.uulm.team020.datatypes.blueprints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.IAmWalkable;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.Magpie;

/**
 * Requirements for n-times n, two dimensional Matrix of 'T'-type Fields.
 * 
 * @author Florian Sihler
 * @version 1.3b, 06/19/2020
 */
public abstract class AbstractGameField<T extends IAmWalkable> implements Serializable {

    private static final long serialVersionUID = 1661375113616000169L;

    private static Magpie magpie = Magpie.createMagpieSafe("GameData");

    private static final int DEFAULT_PADDING = 16;

    /** have the registers been assigned? */
    private transient boolean initialized = false;

    /** minimum line-length for supplied Field */
    private transient int minWidth = Integer.MAX_VALUE;
    /** maximum line-length in supplied Field */
    private transient int maxWidth = 0;
    /** minimum col length in supplied Field */
    private transient int minHeight = Integer.MAX_VALUE;
    /** maximum col length in supplied Field */
    private transient int maxHeight = 0;

    /**
     * Initializes:
     * <ul>
     * <li>{@link #minWidth} : int</li>
     * <li>{@link #maxWidth} : int</li>
     * <li>{@link #minHeight} : int</li>
     * <li>{@link #maxHeight} : int</li>
     * </ul>
     * 
     * @param field the field to calculate metadata for
     */
    protected void initDataRegisters(T[][] field) {
        magpie.writeInfo("Calculating Registers for new Field.", "init");
        this.minHeight = field.length;
        this.maxHeight = field.length;
        this.minWidth = Integer.MAX_VALUE;
        this.maxWidth = 0;
        int yOffset = 0;
        // We ćan expect the field to have at least one line, but we want to check
        ArrayList<Integer> currentHeights;
        if (field.length > 0) {
            currentHeights = new ArrayList<>(field[0].length);
        } else {
            currentHeights = new ArrayList<>(0);
            minWidth = 0;
        }

        for (T[] ts : field) {
            if (ts == null) {// if we got a 'null' line in case of a sloppy scenario-file
                yOffset += 1;
                continue;
            }
            initDataRegistersForSingleLine(currentHeights, ts);
        }
        // We have to be careful for consecutive empty lines again
        int minProposal = currentHeights.isEmpty() ? 0 : Collections.min(currentHeights);
        int maxProposal = currentHeights.isEmpty() ? 0 : Collections.max(currentHeights);

        // This is not necessary for the max proposal. If it is zero it would be anyway
        this.minHeight = Math.min(this.minHeight - yOffset, minProposal > 0 ? minProposal : Integer.MAX_VALUE);
        this.maxHeight = Math.max(this.maxHeight - yOffset, maxProposal);
        writeDataForDebug();
        initialized = true;
    }

    private void writeDataForDebug() {
        magpie.writeDebug("Min Width: " + minWidth, "init");
        magpie.writeDebug("Max Width: " + maxWidth, "init");
        magpie.writeDebug("Min Height: " + minHeight, "init");
        magpie.writeDebug("Max Height: " + maxHeight, "init");
    }

    private int initDataRegistersForSingleLine(ArrayList<Integer> currentHeights, T[] ts) {
        // Check if last segment is null :/, which is dirty, but which is sadly allowed
        // by the
        // standardization committee
        int xoffset = 0;
        if (ts.length > 0 && ts[ts.length - 1] == null)
            xoffset = 1;

        this.minWidth = Math.min(this.minWidth, ts.length - xoffset);
        this.maxWidth = Math.max(this.maxWidth, ts.length - xoffset);

        // Update currentHeight-Lengths if this line is longer
        if (ts.length > currentHeights.size()) {
            for (int i = currentHeights.size(); i < this.maxWidth; i++) {
                currentHeights.add(0); // Init with zero
            }
        }
        // We need to find broken columns and increment others, as null fields are
        // forbidden
        // we can make simple cuts
        for (int i = 0; i < ts.length; i++) {
            currentHeights.set(i, currentHeights.get(i) + 1);
        }

        // Mark breaks by resetting to 0
        for (int i = ts.length; i < currentHeights.size(); i++) {
            // We will store the current highest and lowest candidates, if they would
            // be better:
            if (currentHeights.get(i) > 0) {// otherwise consecutive empty lines would default to zero
                this.minHeight = Math.min(this.minHeight, currentHeights.get(i));
                this.maxHeight = Math.max(this.maxHeight, currentHeights.get(i));
            }
            currentHeights.set(i, 0);
        }
        return xoffset;
    }

    /**
     * Create the Copy without populating the data-registers
     * 
     * @param field the (old) field
     * 
     * @return a copy of the supplied field
     */
    public abstract T[][] createCopy(T[][] field);

    /**
     * A convenient Copy-Method, which will automatically assign the necessary
     * fields:
     * <ul>
     * <li>{@link #minWidth} : int</li>
     * <li>{@link #maxWidth} : int</li>
     * <li>{@link #minHeight} : int</li>
     * <li>{@link #maxHeight} : int</li>
     * </ul>
     * 
     * It will use {@link #createDataCopy(IAmWalkable[][])} for that purpose.
     * 
     * @param field the (old) field to use
     * @return a copy of the supplied field
     * 
     */
    protected T[][] createDataCopy(T[][] field) {
        initDataRegisters(field);
        // As we have either to convert or to copy we will do both here
        return createCopy(field);
    }

    /**
     * @return the capsuled field as it is, returns reference if applicable but
     *         never returns a depp-copy!
     */
    public abstract T[][] getField();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(getField());
        result = prime * result + maxHeight;
        result = prime * result + maxWidth;
        result = prime * result + minHeight;
        result = prime * result + minWidth;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractGameField<?>))
            return false;
        AbstractGameField<?> other = (AbstractGameField<?>) obj;
        return Arrays.deepEquals(getField(), other.getField());
    }

    /**
     * Note, if the Field has a break in the rows, this will refer to the shortest
     * piece of that break. This means, in:
     * 
     * <pre>
     * [
     *   ["FREE", "WALL",      "WALL"],
     *   ["WALL", "FIREPLACE", "WALL"],
     *   ["WALL", "FREE",      "FREE"],
     *   ["WALL", "FREE"             ],
     *   ["WALL", "WALL",      "SAFE"]
     *]
     * </pre>
     * 
     * This will return '2' as the fourth column (starting from 1) has only two
     * elements.
     * 
     * @return shortest row in the supplied Field
     */
    public int getMinWidth() {
        if (!initialized)
            initDataRegisters(getField());
        return minWidth;
    }

    /**
     * @return longest row in the supplied Field
     */
    public int getMaxWidth() {
        if (!initialized)
            initDataRegisters(getField());
        return maxWidth;
    }

    /**
     * Note, if the Field has a break in the columns, this will refer to the
     * shortest piece of that break. This means, in:
     * 
     * <pre>
     * [
     *   ["FREE", "WALL",      "WALL"],
     *   ["WALL", "FIREPLACE", "WALL"],
     *   ["WALL", "FREE",      "FREE"],
     *   ["WALL", "FREE"             ],
     *   ["WALL", "WALL",      "SAFE"]
     *]
     * </pre>
     * 
     * This will return '1' as the <i>Safe</i> in the right bottom corner represents
     * a column of height '1'.
     * 
     * @return shortest column in the supplied Field
     */
    public int getMinHeight() {
        if (!initialized)
            initDataRegisters(getField());
        return minHeight;
    }

    /**
     * @return longest column in the supplied Field
     */
    public int getMaxHeight() {
        if (!initialized)
            initDataRegisters(getField());
        return maxHeight;
    }

    /**
     * Specifies who a field is to be printed by the {@link #toString()}-call. The
     * default implementation will just use the {@link #toString()}-implementation
     * of the supplied Type.
     * 
     * @param field The field to print.
     * 
     * @return The string summarizing the readable information for this field.
     */
    protected String print(T field) {
        return field.toString();
    }

    /**
     * @return The constant padding to be used in cells
     */
    protected int getPadding() {
        return DEFAULT_PADDING;
    }

    /**
     * Tries to get the field, return null if invalid (or null)
     * 
     * @param coordinate the field you want
     * @return Field if it exists, {@code null} otherwise
     */
    public T getSpecificField(Point coordinate) {
        final T[][] map = getField();
        // Parse y
        int y = coordinate.getY();
        int x = coordinate.getX();
        return isInMap(x, y) ? map[y][x] : null;
    }

    /**
     * Tries to set the field, return null if not valid
     * 
     * @param coordinate The field you want
     * @param newField   The field to update to
     * @return True if done, false otherwise
     */
    public boolean setSpecificField(Point coordinate, T newField) {
        if (!coordinate.isOnField(this)) {
            return false;
        }
        final T[][] map = getField();
        map[coordinate.getY()][coordinate.getX()] = newField;
        return true;
    }

    /**
     * Checks whether a given {@code coordinate} is in range of the map.
     * 
     * @param x The x-coordinate to be checked
     * @param y The y-coordinate to be checked
     * @return true if in range, false otherwise
     */
    private boolean isInMap(int x, int y) {
        final T[][] map = getField();
        if (y < 0 || y >= map.length) // y direction
            return false;
        // x direction
        return x >= 0 && x < map[y].length;
    }

    /**
     * Returns a set of valid neighbour points for a given field.
     *
     * @param coordinate The field which neighbours you want
     * 
     * @return The neighbour points, empty if the passed {@code coordinate} itself
     *         was invalid
     */
    public Optional<Set<Point>> getNeighboursOfSpecificField(final Point coordinate) {
        if (coordinate == null || !coordinate.isOnField(this))
            return Optional.empty();

        Set<Point> validNeighbours = Arrays.stream(coordinate.getNeighbours()).filter(p -> p.isOnField(this))
                .collect(Collectors.toSet());
        return Optional.of(validNeighbours);
    }

    /**
     * Returns a set of valid neighbour fields for a given field.
     *
     * @param coordinate The field which neighbours you want
     * 
     * @return The neighbour fields, empty if the passed {@code coordinate} itself
     *         was invalid (not on field or {@code null})
     */
    public Optional<List<T>> getNeighbourFieldsOfSpecificField(final Point coordinate) {
        if (coordinate == null || !coordinate.isOnField(this))
            return Optional.empty();

        List<T> validNeighbours = Arrays.stream(coordinate.getNeighbours()).filter(p -> p.isOnField(this))
                .map(this::getSpecificField).collect(Collectors.toList());
        return Optional.of(validNeighbours);
    }

    /**
     * Retrieves the nested the supplied Field in an human-readable way.
     * <p>
     * Please note, that the output will get buffered to avoid recreation.
     * 
     * @return human-readable string-representation.
     */
    @Override
    public String toString() {
        String intro = "AbstractGameField (" + System.identityHashCode(this) + ")\n";
        String metaLine = String.format("Width: [min: %d, max: %d], Height: [min: %d, max: %d]%n", getMinWidth(),
                getMaxWidth(), getMinHeight(), getMaxHeight());
        StringBuilder numbers = new StringBuilder(160);
        numbers.append("     ");
        // We need the maximum column width but won't run the array twice, therefore
        // we will this.buffer the contents
        StringBuilder sb = new StringBuilder(2048);

        int max_cols = 0;
        for (int y = 0; y < getField().length; y++) {
            sb.append(String.format("%3d: [", y));
            appendRowToStringBuilder(sb, y);
            if (getField()[y].length > max_cols)
                max_cols = getField()[y].length;
            sb.append("] :").append(y).append("\n");
        }

        appendNumbersToTheBuilder(numbers, max_cols);
        return intro + metaLine + numbers.toString() + "\n" + sb.toString() + numbers.toString();
    }

    private void appendNumbersToTheBuilder(StringBuilder numbers, int max_cols) {
        for (int i = 0; i < max_cols; i++) {
            numbers.append(String.format(" %-" + (getPadding() - 2) + "s  ", i + ":"));
        }
    }

    private void appendRowToStringBuilder(StringBuilder sb, int y) {
        for (int x = 0; x < getField()[y].length; x++) {
            sb.append(String.format("%-" + getPadding() + "s ",
                    print(getField()[y][x]) + (x < getField()[y].length - 1 ? ", " : "  ")));
        }
    }
}