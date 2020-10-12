package de.uulm.team020.datatypes.util;

import java.util.List;
import java.util.Objects;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.datatypes.blueprints.AbstractGameField;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.point_helper.PointAlgorithms;
import de.uulm.team020.datatypes.util.point_helper.PointParser;
import de.uulm.team020.helper.NumericHelper;
import de.uulm.team020.helper.pathfinding.Path;

/**
 * A point implementation without having to use other libraries. This one is
 * extended to offer some nice features considering the reference to
 * {@link de.uulm.team020.datatypes.FieldMap FieldMap}.
 * <p>
 * Please note, that this Point implementation, implements a Point2D, which
 * means there is no support for higher dimensions, it is meant to be used in
 * the game-context only and named this way to be easier understandable, in
 * context of the standard.
 * </p>
 * <i>Information: </i> the method names differ from the one used in awt as i am
 * more familiar with the ones stated here.
 *
 * @author Florian Sihler
 * @version 1.5, 05/05/2020
 */
public class Point implements IAmJson {

    private static final long serialVersionUID = -8987995343618512998L;

    private int x;
    private int y;

    /**
     * Creates a new Point
     *
     * @param xCord the x-coordinate
     * @param yCord the y-coordinate
     */
    public Point(final int xCord, final int yCord) {
        x = xCord;
        y = yCord;
    }

    /**
     * Convenient Copy-Constructor
     *
     * @param old The point to copy
     */
    public Point(final Point old) {
        this(old.getX(), old.getY());
    }

    /**
     * Convenient Copy-Constructor including move
     *
     * @param old  The point to copy
     * @param move The point to move the other from
     */
    public Point(final Point old, final Point move) {
        this(old.getX() + move.getX(), old.getY() + move.getY());
    }

    /**
     * Initializes the point at (0,0)
     */
    public Point() {
        this(0, 0);
    }

    /** @return x coordinate of the point */
    public int getX() {
        return this.x;
    }

    /** @return y coordinate of the point */
    public int getY() {
        return this.y;
    }

    /**
     * @param xCord new x coordinate of the point
     *
     * @return The point for chaining
     */
    public Point setX(final int xCord) {
        this.x = xCord;
        return this;
    }

    /**
     * @param yCord new y coordinate of the point
     *
     * @return The point for chaining
     */
    public Point setY(final int yCord) {
        this.y = yCord;
        return this;
    }

    /**
     * Sets new x and y at the same time
     * 
     * @param xCord new x coordinate of the point
     * @param yCord new y coordinate of the point
     *
     * @return The point for chaining
     *
     * @see #setX(int)
     * @see #setY(int)
     */
    public Point set(final int xCord, final int yCord) {
        return this.setX(xCord).setY(yCord);
    }

    /**
     * Sets this point to the coordinates of p1
     * 
     * @param p1 the point to get the coordinates from
     *
     * @return The point for chaining
     *
     * @see #set(int, int)
     */
    public Point set(final Point p1) {
        return set(p1.getX(), p1.getY());
    }

    /**
     * Moves the point relative on the x-axis
     * 
     * @param deltaX relative new x coordinate
     *
     * @return The point for chaining
     *
     * @see #setX(int)
     */
    public Point moveX(final int deltaX) {
        return setX(getX() + deltaX);
    }

    /**
     * Moves the point relative on the y-axis
     * 
     * @param deltaY relative new y coordinate
     *
     * @return The point for chaining
     *
     * @see #setY(int)
     */
    public Point moveY(final int deltaY) {
        return setY(getY() + deltaY);
    }

    /**
     * Moves the point relative in x and y dir
     * 
     * @param deltaX relative new x coordinate
     * @param deltaY relative new y coordinate
     *
     * @return The point for chaining
     *
     * @see #moveX(int)
     * @see #moveY(int)
     */
    public Point move(final int deltaX, final int deltaY) {
        return this.moveX(deltaX).moveY(deltaY);
    }

    /**
     * Moves the point p1 relative in x and y dir
     *
     * @param p1     the point to move to
     * @param deltaX relative new x coordinate
     * @param deltaY relative new y coordinate
     *
     * @return The point for chaining
     *
     * @see #moveX(int)
     * @see #moveY(int)
     */
    public static Point move(final Point p1, final int deltaX, final int deltaY) {
        return p1.move(deltaX, deltaY);
    }

    /**
     * Moves this point by the 'vector' p1
     *
     * @param p1 the point to get the relative coordinates from
     *
     * @return The point for chaining
     *
     * @see #move(int, int)
     */
    public Point move(final Point p1) {
        return move(p1.getX(), p1.getY());
    }

    /**
     * Moves p1 by the 'vector' p2
     *
     * @param p1 the point to move
     * @param p2 the point to get the relative coordinates from
     *
     * @return The point for chaining
     *
     * @see #move(Point, int, int)
     */
    public static Point move(final Point p1, final Point p2) {
        return move(p1, p2.getX(), p2.getY());
    }

    /**
     * Calculates the euclidean distance between both points
     *
     * @param p1 point one
     * @param p2 point two
     * @return Euclidean distance between p1 and p2
     */
    public static double getDistance(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - (double) p2.getX(), 2) + Math.pow(p1.getY() - (double) p2.getY(), 2));
    }

    /**
     * Calculates the euclidean distance between both points
     *
     * @param aX x coordinate of Point a
     * @param aY y coordinate of Point a
     * @param bX x coordinate of Point b
     * @param bY y coordinate of Point b
     * 
     * @return Euclidean distance between a(X,Y) and b(X,Y)
     */
    public static double getDistance(final double aX, final double aY, final double bX, final double bY) {
        return Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
    }

    /**
     * Calculates the euclidean distance to this point
     *
     * @param p2 point two
     * @return Euclidean distance between this point and the other (p2)
     * 
     * @see #getDistance(Point, Point)
     */
    public double getDistance(final Point p2) {
        return getDistance(this, Objects.requireNonNull(p2));
    }

    /**
     * Calculates the euclidean distance to the given point b(X,Y)
     *
     * @param bX x coordinate of Point b
     * @param bY y coordinate of Point b
     * 
     * @return Euclidean distance between this point and the other (b(X,Y))
     * 
     * @see #getDistance(double, double, double, double)
     */
    public double getDistance(final double bX, final double bY) {
        return getDistance(this.getX(), this.getY(), bX, bY);
    }

    /**
     * Calculates the "walkable" distance of this point to another based on
     * max-metric.
     * 
     * @param b The point to calculate the distance to
     * 
     * @return The distance
     */
    public int getKingDistance(final Point b) {
        return getKingDistance(this, b);
    }

    /**
     * Calculates the "walkable" distance of two points based on max-metric.
     * 
     * @param a The first point
     * @param b The second point
     * @return The distance
     */
    public static int getKingDistance(final Point a, final Point b) {
        return Math.max(Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
    }

    // Metric namings:

    /**
     * Uses {@link #getDistance(Point, Point)} and is just a naming alias for
     * convenience
     * 
     * @param a The first point
     * @param b The second point
     * 
     * @return The euclidean distance
     */
    public static double euclideanMetric(final Point a, final Point b) {
        return getDistance(a, b);
    }

    /**
     * Uses {@link #getKingDistance(Point, Point)} and is just a naming alias for
     * convenience
     * 
     * @param a The first point
     * @param b The second point
     * 
     * @return The king distance
     */
    public static double kingMetric(final Point a, final Point b) {
        return getKingDistance(a, b);
    }

    // neighbours

    /**
     * Checks if p1 and p2 are neighbours. Any point will be a neighbour of itself.
     *
     * @param p1 p1 the first point
     * @param p2 p2 the second point
     *
     * @return True if the points are neighbours, false otherwise
     */
    public static boolean isNeighbour(final Point p1, final Point p2) {
        return Math.abs(p1.getX() - p2.getX()) <= 1 && Math.abs(p1.getY() - p2.getY()) <= 1;
    }

    /**
     * Checks if the given Point is a neighbor of this Point. Any point will be a
     * neighbour of itself.
     *
     * @param p2 the point to check
     * @return True if the points are neighbours, false otherwise.
     * 
     * @see #isNeighbour(Point, Point)
     */
    public boolean isNeighbour(final Point p2) {
        return isNeighbour(this, p2);
    }

    private static final Point[] NEIGHBOUR_POINTS = new Point[] { new Point(-1, -1), new Point(0, -1), new Point(1, -1),
            new Point(-1, 0), new Point(1, 0), new Point(-1, 1), new Point(0, 1), new Point(1, 1) };

    /**
     * @param p The point to get the neighbours from
     * 
     * @return Array of all neighbours of the given point. This will always
     *         <i>exclude</i> the point itself.
     */
    public static Point[] getNeighbours(final Point p) {
        final Point[] result = new Point[8];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Point(p, NEIGHBOUR_POINTS[i]);
        }
        return result;
    }

    /**
     * @return Array of all neighbours of this point. This will always
     *         <i>exclude</i> the point itself.
     * 
     * @see #getNeighbours(Point)
     */
    public Point[] getNeighbours() {
        return getNeighbours(this);
    }

    /**
     * Returns the absolute value of the point, which can be interpreted as the
     * distance to (0,0). (length of the vector pointing to this Point)
     *
     * @param p1 the point to calculate the abs value for
     *
     * @return abs value of the point
     */
    public static Double abs(final Point p1) {
        return Math.sqrt(Math.pow(p1.getX(), 2) + Math.pow(p1.getY(), 2));
    }

    /**
     * Calculates {@link #abs(Point)} for this point
     *
     * @return abs value of the point
     * 
     * @see #abs(Point)
     */
    public Double abs() {
        return abs(this);
    }

    /**
     * Checks if p1 is inside of the rectangle denoted by lowerLeft and upperRight.
     * Both points are inclusive.
     * 
     * @param p1         point to check
     * @param lowerLeft  lower left of the rectangle
     * @param upperRight upper right of the rectangle
     *
     * @return true if p1 is in the rectangle created by p1 and lowerLeft
     */
    public static boolean isInBounds(final Point p1, final Point lowerLeft, final Point upperRight) {
        // maybe sort points to get left and right automatically?
        final int p1X = p1.getX();
        final int p1Y = p1.getY();
        return p1X >= lowerLeft.getX() && p1X <= upperRight.getX() // x
                && p1Y >= lowerLeft.getY() && p1Y <= upperRight.getY(); // y
    }

    /**
     * Checks if this point is inside of the rectangle denoted by lowerLeft and
     * upperRight. Both points are inclusive.
     *
     * @param lowerLeft  lower left of the rectangle
     * @param upperRight upper right of the rectangle
     *
     * @return true if this point is in the rectangle created by p1 and lowerLeft
     * 
     * @see #isInBounds(Point, Point, Point)
     */
    public boolean isInBounds(final Point lowerLeft, final Point upperRight) {
        return isInBounds(this, lowerLeft, upperRight);
    }

    /**
     * Just validates that the given point is on the Field
     * 
     * @param p     The point to check
     * @param field The field to test this on
     * 
     * @return True if the point is on the field, False otherwise
     */
    public static boolean isOnField(final Point p, final AbstractGameField<?> field) {
        return isOnField(p, field.getField());
    }

    /**
     * Just validates that the point is on the Field
     * 
     * @param field The field to test this on
     * 
     * @return True if this point is on the field, False otherwise
     */
    public boolean isOnField(final AbstractGameField<?> field) {
        return isOnField(this, field.getField());
    }

    /**
     * Just validates that the given point is on the Field
     * 
     * @param p   The point to check
     * @param arr The field to test this on
     * 
     * @return True if the point is on the field, False otherwise
     */
    public static boolean isOnField(final Point p, final Object[][] arr) {
        final int x = p.getX();
        final int y = p.getY();
        // edge-cases basic
        if (arr == null || arr.length == 0 || y < 0 || x < 0)
            return false;
        // row is out of bounds, null or has no valid points
        if (y >= arr.length || arr[y] == null || arr[y].length == 0)
            return false;
        // is the point in the row or not
        return x < arr[y].length;
    }

    /**
     * Just validates that the given point is on the Field
     * 
     * @param arr The field to test this on
     * 
     * @return True if the point is on the field, False otherwise
     * @see #isOnField(Point, Object[][])
     */
    public boolean isOnField(final Object[][] arr) {
        return isOnField(this, arr);
    }

    /**
     * Returns the angle in perspective to the baseline between this and another
     * point (in degrees)
     * 
     * @param other The other point to check to
     * 
     * @return Angle in degrees (positive)
     * 
     * @see #getAngle(Point, Point)
     */
    public double getAngle(final Point other) {
        return getAngle(this, other);
    }

    /**
     * Returns the angle in perspective to the baseline between the two given points
     * (in degrees)
     * 
     * @param a Point a
     * @param b Point b
     * 
     * @return Angle in degrees (positive)
     */
    public static double getAngle(final Point a, final Point b) {
        // do it polar :d
        double angle = Math.toDegrees(Math.atan2(a.getY() - ((double) b.getY()), a.getX() - ((double) b.getX())));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * Checks, that c is on the line segment between b and c. This uses "near
     * enough" metrics, to compensate rounding problems
     * 
     * @param a first point
     * @param b second point
     * @param c point to check
     * 
     * @return True if in between, false otherwise (precision: {@code 0.00001D})
     */
    public static boolean isInBetween(final Point a, final Point b, final Point c) {
        return NumericHelper.closeEnough(getDistance(a, b), getDistance(a, c) + getDistance(c, b), 0.00001D);
    }

    /**
     * Checks, that c is on the line segment between b and c(X,Y). This uses "near
     * enough" metrics, to compensate rounding problems
     * 
     * @param a  first point
     * @param b  second point
     * @param cX x coordinate of point c
     * @param cY y coordinate of point C
     * 
     * @return True if in between, false otherwise (precision: {@code 0.00001D})
     */
    public static boolean isInBetween(final Point a, final Point b, final double cX, final double cY) {
        return NumericHelper.closeEnough(getDistance(a, b), a.getDistance(cX, cY) + b.getDistance(cX, cY), 0.00001D);
    }

    /**
     * This method will use a modified 4 connected Bresenham's line algorithm to
     * rasterize the line between the given points. It uses some minor optimizations
     * to handle direct lines and will account for error values using fp-precision
     * which should be more than enough for most cases on the field. Furthermore it
     * will include "cut" fields into the calculation.
     * <p>
     * The line will always be drawn from start to end - there will be no change of
     * points performed if end is 'lower' than start.
     * 
     * @param end The ending point of the line
     * 
     * @return the path from start to end -- will contain start and (if differ) end.
     * 
     * 
     * @see #getLine(Point, Point)
     * @see #getLine(int, int, int, int)
     */
    public Path getLine(final Point end) {
        return getLine(this, end);
    }

    /**
     * This method will use a modified 4 connected Bresenham's line algorithm to
     * rasterize the line between the given points. It uses some minor optimizations
     * to handle direct lines and will account for error values using fp-precision
     * which should be more than enough for most cases on the field. Furthermore it
     * will include "cut" fields into the calculation.
     * <p>
     * The line will always be drawn from start to end - there will be no change of
     * points performed if end is 'lower' than start.
     * 
     * @param start The starting point of the line
     * @param end   The ending point of the line
     * 
     * @return the path from start to end -- will contain start and (if differ) end.
     * 
     * @see #getLine(int, int, int, int)
     */
    public static Path getLine(final Point start, final Point end) {
        return getLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * This method will use a modified 4 connected Bresenham's line algorithm to
     * rasterize the line between the given points. It uses some minor optimizations
     * to handle direct lines and will account for error values using fp-precision
     * which should be more than enough for most cases on the field. Furthermore it
     * will include "cut" fields into the calculation.
     * <p>
     * The line will always be drawn from start to end - there will be no change of
     * points performed if end is 'lower' than start.
     * 
     * @param startX Starting x coordinate
     * @param startY Starting y coordinate
     * @param endX   Ending x coordinate
     * @param endY   Ending y coordinate
     * 
     * @return the path from start to end -- will contain start and (if differ) end.
     */
    public static Path getLine(final int startX, final int startY, final int endX, final int endY) {
        return PointAlgorithms.getLine(startX, startY, endX, endY);
    }

    /**
     * Will perform an indexed traversal around the current point but only accepting
     * points if their distance to the center is &lt;= the given radius. Will have
     * no tolerance. <i>This uses the
     * {@link Point#getDistance(Point, Point)}-Metric.</i>
     * 
     * @param radius Include all inner points with this radius &gt;= 0
     * 
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(Point, int)
     */
    public List<Point> getCircle(final int radius) {
        return getCircle(this, radius);
    }

    /**
     * Will perform an indexed traversal around the current point but only accepting
     * points if their distance to the center is &lt;= the given radius. Will have
     * no tolerance.
     * 
     * @param radius Include all inner points with this radius &gt;= 0
     * @param metric The metric that shall be used to check the radius against
     * 
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(Point, int, DistanceMetric)
     */
    public List<Point> getCircle(final int radius, final DistanceMetric metric) {
        return getCircle(this, radius, metric);
    }

    /**
     * Will perform an indexed traversal around the current point but only accepting
     * points if their distance to the center is &lt;= the given radius. <i>This
     * uses the {@link Point#getDistance(Point, Point)}-Metric.</i>
     * 
     * @param radius    Include all inner points with this radius &gt;= 0
     * @param tolerance Tolerance to be used when checking to match radius. Please
     *                  note, that using values greater than one will not increase
     *                  the total circle but make the point-shape approximate a
     *                  rectangle
     *
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(int)
     * @see #getCircle(Point, int)
     * @see #getCircle(Point, int, double, DistanceMetric)
     */
    public List<Point> getCircle(final int radius, final double tolerance) {
        return getCircle(this, radius, tolerance, Point::euclideanMetric);
    }

    /**
     * Will perform an indexed traversal around the current point but only accepting
     * points if their distance to the center is &lt;= the given radius.
     * 
     * @param radius    Include all inner points with this radius &gt;= 0
     * @param tolerance Tolerance to be used when checking to match radius. Please
     *                  note, that using values greater than one will not increase
     *                  the total circle but make the point-shape approximate a
     *                  rectangle
     * @param metric    The metric that shall be used to check the radius against
     * 
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(Point, int, double, DistanceMetric)
     */
    public List<Point> getCircle(final int radius, final double tolerance, final DistanceMetric metric) {
        return getCircle(this, radius, tolerance, metric);
    }

    /**
     * Will perform an indexed traversal but only accepting points if their distance
     * to the center is &lt;= the given radius. Will have no tolerance. <i>This uses
     * the {@link Point#getDistance(Point, Point)}-Metric.</i>
     * 
     * @param center The central point the circle shall be be constructed around
     * @param radius Include all inner points with this radius &gt;= 0
     * 
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(Point, int, double, DistanceMetric)
     */
    public static List<Point> getCircle(final Point center, final int radius) {
        return getCircle(center, radius, 0D, Point::euclideanMetric);
    }

    /**
     * Will perform an indexed traversal but only accepting points if their distance
     * to the center is &lt;= the given radius. Will have no tolerance.
     * 
     * @param center The central point the circle shall be be constructed around
     * @param radius Include all inner points with this radius &gt;= 0
     * @param metric The metric that shall be used to check the radius against
     * 
     * @return List of all the points in the given radius
     * 
     * @see #getCircle(Point, int, double, DistanceMetric)
     */
    public static List<Point> getCircle(final Point center, final int radius, final DistanceMetric metric) {
        return getCircle(center, radius, 0D, metric);
    }

    /**
     * Will perform an indexed traversal but only accepting points if their distance
     * to the center is &lt;= the given radius.
     * 
     * @param center    The central point the circle shall be be constructed around
     * @param radius    Include all inner points with this radius &gt;= 0
     * @param tolerance Tolerance to be used when checking to match radius. Please
     *                  note, that using values greater than one will not increase
     *                  the total circle but make the point-shape approximate a
     *                  rectangle
     * @param metric    The metric that shall be used to check the radius against
     * 
     * @return List of all the points in the given radius
     */
    public static List<Point> getCircle(final Point center, final int radius, final double tolerance,
            final DistanceMetric metric) {
        return PointAlgorithms.getCircle(center, radius, tolerance, metric);
    }

    /**
     * Will parse the point from a String in various formats -- you may use "(x,
     * y)", "&lt;x, y&gt;" or "x/y" but are not allowed to prepend it with '+' or
     * '-' to perform shifts -- if you want shifts use
     * {@link #fromString(String, Point)}
     * 
     * @param data The data to parse
     * 
     * @return The resulting point
     * 
     * @since 1.2
     * 
     * @throws PointParseException If the given data invalidated the format
     * 
     * @see #fromString(String, Point)
     * @see #fromString(String, Point, boolean, boolean)
     */
    public static Point fromString(String data) throws PointParseException {
        return fromString(data, null, false, true);
    }

    /**
     * Will parse the point from a String in various formats -- you may use "(x,
     * y)", "&lt;x, y&gt;" or "x/y" and are allowed to prepend it with '+' or '-' to
     * perform shifts.
     * 
     * @param data The data to parse
     * @param base The base point -- set this to null if you want not shift
     * 
     * @return The resulting point
     * 
     * @since 1.2
     * 
     * @throws PointParseException If the given data invalidated the format
     * 
     * @see #fromString(String)
     * @see #fromString(String, Point, boolean, boolean)
     */
    public static Point fromString(String data, final Point base) throws PointParseException {
        return fromString(data, base, false, true);
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
     * 
     * @see #fromString(String)
     * @see #fromString(String, Point)
     */
    protected static Point fromString(String data, final Point base, final boolean doShift, final boolean shiftSign)
            throws PointParseException {
        return PointParser.fromString(data, base, doShift, shiftSign);
    }

    /**
     * Convert the String in a format, parsable by {@link #fromString(String)} and
     * it's overloads
     * 
     * @return String-representation of the point
     */
    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        final Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

}
