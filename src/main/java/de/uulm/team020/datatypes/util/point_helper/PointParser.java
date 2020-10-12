package de.uulm.team020.datatypes.util.point_helper;

import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.Point;

/**
 * This class contains the algorithm to parse Point Data and return a point. The
 * algorithm itself is static
 * 
 * @author Florian Sihler
 * @version 1.0, 07/07/2020
 */
public class PointParser {

    private static final String FORMAT_TXT = "Data for Point is not in valid format: ";

    // Hide the default one
    private PointParser() {
    }

    /**
     * Will parse the point from a String in various formats -- you may use "(x,
     * y)", "&lt;x, y&gt;" or "x/y" and are allowed to prepend it with '+' or '-' to
     * perform shifts.
     * 
     * @param data      The data to parse
     * @param base      The base point -- set this to null if you want not shift
     * @param doShift   Should shifting be enforced? You may do not need to set this
     * @param shiftSign Add on Shift if true, subtract on false
     * @return The resulting point
     * 
     * @since 1.2
     * 
     * @throws PointParseException If the given data invalidated the format
     */
    public static Point fromString(String data, final Point base, final boolean doShift, final boolean shiftSign)
            throws PointParseException {
        // safety guard
        if (data == null || data.isEmpty()) {
            throw new PointParseException("Point cannot be parsed as '" + data + "' suggests empty data");
        }

        // we will ignore all whitespace
        data = data.trim();
        char firstToken = data.charAt(0);

        switch (firstToken) {
            case '+': // expected "+ whatever"
                guardPointDoubleOperation(doShift);
                // gobble first
                return fromString(data.substring(1), base, true, true);
            case '-': // expected "- whatever"
                guardPointDoubleOperation(doShift);
                // gobble first
                return fromString(data.substring(1), base, true, false);
            case '<': // expected "<x,y>"
                return mayShift(fromBracedString(data, '>'), base, doShift, shiftSign);
            case '(': // expected "(x, y)"
                return mayShift(fromBracedString(data, ')'), base, doShift, shiftSign);
            default: // expected old syntax: "x/y"
                return mayShift(fromLegacyString(data, "/"), base, doShift, shiftSign);
        }
    }

    private static Point fromBracedString(String data, final char end) throws PointParseException {
        final int length = data.length();
        if (length < 5 || data.charAt(length - 1) != end) {
            throw new PointParseException(FORMAT_TXT + data + " must be (x, y) or <x, y>.");
        }
        return fromLegacyString(data.substring(1, length - 1), ",\\s*");
    }

    private static Point fromLegacyString(String data, final String separator) throws PointParseException {
        // jay, we may receive double formats
        final int length = data.length();
        if (length < 3) {
            throw new PointParseException(FORMAT_TXT + data + " must be x" + separator + "y.");
        }

        // assumes format "x<separator>y"
        final String[] pointData = data.split(separator);
        if (pointData.length != 2) {
            throw new PointParseException(FORMAT_TXT + data + " must be x" + separator + "y.");
        }

        try {
            return new Point(Integer.parseInt(pointData[0]), Integer.parseInt(pointData[1]));
        } catch (NumberFormatException ex) {
            throw new PointParseException(FORMAT_TXT + data + " as: " + ex.getMessage());
        }
    }

    private static Point mayShift(final Point shift, final Point base, final boolean doShift, final boolean shiftSign)
            throws PointParseException {
        if (!doShift) {
            return new Point(shift);
        } else if (base == null) {
            throw new PointParseException("You wanted to shift the Point: " + shift
                    + " but shifting is not allowed as there is no source-point to shift");
        }
        return new Point(base, shiftSign ? shift : new Point(-shift.getX(), -shift.getY()));
    }

    private static void guardPointDoubleOperation(final boolean doShift) throws PointParseException {
        if (doShift) {
            throw new PointParseException(
                    "You can not perform multiple shift operations. Choose + OR - and do not double them.");
        }
    }

}