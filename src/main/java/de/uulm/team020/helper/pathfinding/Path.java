package de.uulm.team020.helper.pathfinding;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import de.uulm.team020.datatypes.IAmTransparent;
import de.uulm.team020.datatypes.blueprints.AbstractGameField;
import de.uulm.team020.datatypes.util.Point;

/**
 * Construction of Points to be iterated upon and to be created by the
 * pathfinder.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/28/2020
 */
public class Path implements Collection<Path.WayPoint>, Serializable {

    private static final long serialVersionUID = -7208584152950099329L;

    /**
     * Point in a Path
     */
    public static class WayPoint extends Point {

        private static final long serialVersionUID = 2831499674535418697L;

        // Or mother...
        private WayPoint father = null;
        private WayPoint child = null;

        // cost 'guessed' heuristic
        private int costHeuristic = 0;
        // final cost
        private int cost = 0;

        /**
         * Construct a new WayPoint!
         * 
         * @param xCord  X coordinate
         * @param yCord  Y coordinate
         * @param father The father to use
         */
        public WayPoint(Integer xCord, Integer yCord, WayPoint father) {
            super(xCord, yCord);
            this.father = father;
        }

        /**
         * Construct a new WayPoint without a father!
         * 
         * @param xCord X coordinate
         * @param yCord Y coordinate
         */
        public WayPoint(Integer xCord, Integer yCord) {
            this(xCord, yCord, null);
        }

        /**
         * Construct a new WayPoint using the data from a default Point. Will leave the
         * father blank
         * 
         * @param old The old point
         */
        public WayPoint(Point old) {
            super(old);
        }

        /**
         * Simple copy-constructor
         * 
         * @param old The old WayPoint to copy
         */
        public WayPoint(WayPoint old) {
            super(old);
            this.father = old.father;
        }

        /**
         * Construct a new WayPoint at (0,0) with no father.
         */
        public WayPoint() {
            super();
        }

        @Override
        public String toString() {
            return "WayPoint [<point>=" + super.toString() + ", cost=" + cost + ", costHeuristic=" + costHeuristic
                    + ", father=" + father + "]";
        }

        public WayPoint getFather() {
            return father;
        }

        protected void assignFather(WayPoint father) {
            if (this.father != null)
                this.father.child = null; // clear old linking
            this.father = father;
            this.father.child = this;
        }

        protected void setFather(WayPoint father) {
            this.father = father;
        }

        /**
         * Returns the {@link WayPoint} coming after this one. If the point is embedded
         * into a {@link Path} this is the next Point in the Path
         * 
         * @return Associated child.
         */
        public WayPoint getChild() {
            return child;
        }

        protected void assignChild(WayPoint child) {
            if (this.child != null)
                this.child.setFather(null); // clear old linking
            this.child = child;
            this.child.father = this;
        }

        public int getCostHeuristic() {
            return this.costHeuristic;
        }

        protected void setCostHeuristic(int costHeuristic) {
            this.costHeuristic = costHeuristic;
        }

        protected int getCost() {
            return this.cost;
        }

        protected void setCost(int cost) {
            this.cost = cost;
        }

        /**
         * Sets father and child to null
         */
        protected void eraseFamily() {
            this.father = this.child = null;
        }

        /**
         * Identifies an invalid way point (as maps will only hold positive indices this
         * cannot be mistaken for a normal point)
         */
        protected static final WayPoint INVALID = new WayPoint(-1, -1);
    }

    private int size;

    private WayPoint start;
    private WayPoint end;

    /**
     * Construct a new path
     */
    public Path() {
        start = end = null;
        size = 0;
    }

    /**
     * Construct a path using the given point as end point of a calculation
     * 
     * @param end The point to create the path
     */
    public Path(WayPoint end) {
        this(); // init empty
        while (end != null) {
            prepend(new WayPoint(end));
            end = end.getFather();
        }
    }

    /**
     * Will create a reversed variant of the given Path, by creating a new Path
     * object - will NOT alter the path object
     * 
     * @param path The path to reverse
     * 
     * @return The reversed path
     */
    public static Path reversed(final Path path) {
        Path reversed = new Path();
        for (WayPoint wayPoint : path) {
            reversed.prepend(wayPoint);
        }
        return reversed;
    }

    /**
     * Add a point to the path, will init the path if there is none.
     * 
     * @param point The point to append, will get father and child mapped correctly.
     * 
     * @return True. As this append does not fail (it will throw exceptions).
     * 
     * @see #add(WayPoint)
     * @see #addAll(Collection)
     */
    public boolean append(WayPoint point) {
        Objects.requireNonNull(point, "Point is never to be null.").eraseFamily();
        if (start == null) {
            start = end = point;
        } else {
            end.assignChild(point);
            end = point;
        }
        size += 1;
        return true;
    }

    /**
     * Construct a new Path out of coordinates, will be mutable and similar to the
     * constructor
     * 
     * @return The create path
     */
    public static Path of() {
        return new Path();
    }

    /**
     * Construct a new Path out of coordinates - amount of coordinates MUST be even
     * 
     * @param points Points to construct the path from
     * 
     * @return The create path
     */
    public static Path of(int... points) {
        assureAmountOfVariablesIsEven(points);
        Path path = new Path();
        for (int i = 0; i < points.length; i += 2) {
            path.append(new WayPoint(points[i], points[i + 1]));
        }
        return path;
    }

    private static void assureAmountOfVariablesIsEven(int... points) {
        if (points.length % 2 != 0) {
            throw new IllegalArgumentException("Path must get an even amount of points which is not given for: "
                    + points.length + " with: " + Arrays.toString(points));
        }
    }

    /**
     * Construct a new Path out of a set of points
     * 
     * @param points Points to construct the path from
     * 
     * @return The create path
     */
    public static Path of(Point... points) {
        Path path = new Path();
        for (Point point : points) {
            path.append(new WayPoint(point));
        }
        return path;
    }

    /**
     * Construct a new Path out of a set of way-points
     * 
     * @param points Way-points to construct the path from
     * 
     * @return The create path
     */
    public static Path of(WayPoint... points) {
        Path path = new Path();
        for (WayPoint point : points) {
            path.append(point);
        }
        return path;
    }

    /**
     * Checks if the path ends at the same point as it started
     * 
     * @return True if start of path equals end of path.
     */
    public boolean sameStartEnd() {
        return Objects.equals(this.start, this.end);
    }

    /**
     * Add a point to the start of the path, will init the path if there is none.
     * 
     * @param point The point to prepend, will get father and child mapped
     *              correctly.
     * 
     * @return True. As this prepend does not fail (it will throw exceptions).
     * 
     */
    public boolean prepend(WayPoint point) {
        Objects.requireNonNull(point, "Point is never to be null.").eraseFamily();
        if (start == null) {
            start = end = point;
        } else {
            start.assignFather(point);
            start = point;
        }
        size += 1;
        return true;
    }

    public WayPoint getStart() {
        return this.start;
    }

    public WayPoint getEnd() {
        return this.end;
    }

    /**
     * @return The amount of points, will return 1 even if invalid!
     * @see #invalid()
     */
    public int size() {
        return this.size;
    }

    /**
     * Alias for {@link #size()}
     * 
     * @return The amount of points, will return 1 even if invalid!
     * @see #invalid()
     */
    public int length() {
        return size();
    }

    @Override
    public Iterator<WayPoint> iterator() {
        return new Iterator<Path.WayPoint>() {

            private WayPoint current = start;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public WayPoint next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                WayPoint mark = current;
                current = current.getChild();
                return mark;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean add(WayPoint w) {
        return this.append(w);
    }

    @Override
    public boolean addAll(Collection<? extends WayPoint> ws) {
        for (WayPoint w : ws) {
            if (!append(w))
                return false;
        }
        return true;
    }

    /**
     * Just checks if none of the points in this path are blocking the line of
     * sight. Uses {@link IAmTransparent}. Fields which are not on the given map are
     * considered to be opaque and therefore line of sight blocking. Will not check
     * start and end
     * 
     * @param map The map to lay the path on
     * 
     * @return true If this path is completely in line of sight, false If there is
     *         any field passed which blocks the line of sight
     */
    public boolean isLineOfSight(final AbstractGameField<? extends IAmTransparent> map) {
        return isLineOfSight(this, map);
    }

    /**
     * Just checks if none of the points in this path are blocking the line of
     * sight. Uses {@link IAmTransparent}. Fields which are not on the given map are
     * considered to be opaque and therefore line of sight blocking.
     * 
     * @param map           The map to lay the path on
     * @param checkStartEnd should start and endpoint of the path be checked too?
     * 
     * @return true If this path is completely in line of sight, false If there is
     *         any field passed which blocks the line of sight
     */
    public boolean isLineOfSight(final AbstractGameField<? extends IAmTransparent> map, boolean checkStartEnd) {
        return isLineOfSight(this, map, checkStartEnd);
    }

    /**
     * Just checks if none of the points in the path are blocking the line of sight.
     * Uses {@link IAmTransparent}. Fields which are not on the given map are
     * considered to be opaque and therefore line of sight blocking. Will not check
     * start and end.
     * 
     * @param path The path to check for on the given field
     * @param map  The map to lay the path on
     * 
     * @return true If the path is completely in line of sight, false If there is
     *         any field passed which blocks the line of sight
     */
    public static boolean isLineOfSight(final Path path, final AbstractGameField<? extends IAmTransparent> map) {
        return isLineOfSight(path, map, false);
    }

    /**
     * Just checks if none of the points in the path are blocking the line of sight.
     * Uses {@link IAmTransparent}. Fields which are not on the given map are
     * considered to be opaque and therefore line of sight blocking.
     * 
     * @param path          The path to check for on the given field
     * @param map           The map to lay the path on
     * @param checkStartEnd should start and endpoint of the path be checked too?
     * 
     * @return true If the path is completely in line of sight, false If there is
     *         any field passed which blocks the line of sight
     */
    public static boolean isLineOfSight(final Path path, final AbstractGameField<? extends IAmTransparent> map,
            boolean checkStartEnd) {
        for (WayPoint point : path) {
            // ignore if start or end and start/end shall not be checked
            if (!checkStartEnd && (point.equals(path.getStart()) || point.equals(path.getEnd()))) {
                continue;
            }
            final IAmTransparent field = map.getSpecificField(point);
            if (field == null || field.blocksLOS()) {
                return false; // blocks
            }
        }
        return true;
    }

    @Override
    public void clear() {
        start = end = null; // clear-up father and child?
        // This method shouldn't be called anyway
    }

    /**
     * Returns if and only if the path contains this point
     * 
     * @param p The point you want to check for
     * @return True if the path contains the point, false otherwise
     */
    public boolean contains(Point p) {
        for (Point point : this) {
            if (point.equals(p))
                return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object obj) {
        for (WayPoint point : this) {
            if (point.equals(obj))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        for (Object object : arg0) {
            if (!contains(object))
                return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return start == null;
    }

    private static final String REMOVE_ERROR = "Cannot remove points from path";

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException(REMOVE_ERROR);
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException(REMOVE_ERROR);
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException(REMOVE_ERROR);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (WayPoint w = start; w != null; w = w.getChild())
            result[i++] = w;

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] arr) {
        // create to length
        if (arr.length < size)
            arr = (T[]) java.lang.reflect.Array.newInstance(arr.getClass().getComponentType(), size);

        int i = 0;
        Object[] result = arr;
        for (WayPoint w = start; w != null; w = w.getChild()) {
            result[i++] = w;
        }

        if (arr.length > size) // cut off if longer
            arr[size] = null;
        return arr;
    }

    protected static final Path INVALID_PATH = new Path(WayPoint.INVALID);

    public boolean invalid() {
        return this.equals(INVALID_PATH);
    }

    /**
     * Wil return the invalid path
     * 
     * @return The invalid Path
     */
    public static Path getInvalid() {
        return INVALID_PATH;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(size * 6);
        builder.append("Path [length=").append(size).append(", path:");
        WayPoint current = start;
        while (current != null) {
            current = printCurrentElementAndGetNext(builder, current);
        }
        return builder.append("]").toString();
    }

    private WayPoint printCurrentElementAndGetNext(StringBuilder builder, WayPoint current) {
        builder.append("(").append(current.getX()).append(",").append(current.getY());
        if (current.getChild() != null) {
            builder.append(") -> ");
        } else {
            builder.append(")");
        }
        current = current.getChild();
        return current;
    }

    // maybe include _whole_ path?
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + size;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Path))
            return false;
        Path other = (Path) obj;
        if (size != other.size)
            return false;
        WayPoint[] pts = this.toArray(WayPoint[]::new);
        WayPoint[] otherPts = other.toArray(WayPoint[]::new);
        return Arrays.equals(pts, otherPts);
    }

}