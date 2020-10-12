package de.uulm.team020.helper.pathfinding;

import java.util.Objects;
import java.util.PriorityQueue;

import de.uulm.team020.datatypes.IAmWalkable;
import de.uulm.team020.datatypes.RingBuffer;
import de.uulm.team020.datatypes.SimpleGameField;
import de.uulm.team020.datatypes.blueprints.AbstractGameField;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.pathfinding.Path.WayPoint;
import de.uulm.team020.logging.Magpie;

/**
 * Basically a path-finder using A*-Algorithm to calculate the optimal path
 * between two points in any two dimensional field. As the field does not change
 * too often i did not implement LPA* and others.
 * 
 * @author Florian Sihler
 * @version 1.1, 04/28/2020
 */
public class Pathfinder<T extends IAmWalkable> {

    private static Magpie magpie = Magpie.createMagpieSafe("Pathfinder");

    private static final int DEFAULT_CACHE_SIZE = 12;

    private T[][] field;
    private final boolean canWalkDiagonal;
    private final boolean flying; // does not care if walkable

    private static class BufferElement extends ImmutablePair<ImmutablePair<Point, Point>, WayPoint> {

        public BufferElement(final Point start, final Point end, final WayPoint value) {
            super(new ImmutablePair<>(start, end), value);
        }

        public BufferElement(final ImmutablePair<Point, Point> key, final WayPoint value) {
            super(key, value);
        }

        /**
         * Returns the cached value for the search if found
         * 
         * @param cache  The cache to search.
         * @param search The Pair you search for
         * @return The target point or null if no cache or not found
         */
        public static WayPoint get(final RingBuffer<BufferElement> cache, final ImmutablePair<Point, Point> search) {
            if (cache == null)
                return null;
            for (final BufferElement bufferElement : cache) {
                if (Objects.equals(bufferElement.getKey(), search))
                    return bufferElement.getValue();
            }
            return null;
        }
    }

    private final RingBuffer<BufferElement> cache;

    private final int width;
    private final int height;

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. This will use an {@link SimpleGameField} to produce the valid
     * arguments. Will be grounded.
     * 
     * @param field The field to use
     */
    public Pathfinder(final T[][] field) {
        this(field, true, false);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. This will use an {@link SimpleGameField} to produce the valid
     * arguments. Will be grounded.
     * 
     * @param field           The field to use
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     */
    public Pathfinder(final T[][] field, boolean canWalkDiagonal) {
        this(field, canWalkDiagonal, false);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. This will use an {@link SimpleGameField} to produce the valid
     * arguments.
     * 
     * @param field           The field to use
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     * @param canFly          Will be true if the Pathfinder can fly - this means it
     *                        will not check fields for being walkable.
     */
    public Pathfinder(final T[][] field, boolean canWalkDiagonal, final boolean canFly) {
        this(new SimpleGameField<>(field), canWalkDiagonal, canFly);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. Will be grounded.
     * 
     * @param field     The field to use
     * @param maxWidth  Maximum width of the field
     * @param maxHeight Maximum Height of the field
     */
    public Pathfinder(final T[][] field, final int maxWidth, final int maxHeight) {
        this(field, maxWidth, maxHeight, DEFAULT_CACHE_SIZE, true, false);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. Will be grounded.
     * 
     * @param field           The field to use
     * @param maxWidth        Maximum width of the field
     * @param maxHeight       Maximum Height of the field
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     */
    public Pathfinder(final T[][] field, final int maxWidth, final int maxHeight, boolean canWalkDiagonal) {
        this(field, maxWidth, maxHeight, DEFAULT_CACHE_SIZE, canWalkDiagonal, false);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation. Will be grounded.
     * 
     * @param field     The field to use
     * @param maxWidth  Maximum width of the field
     * @param maxHeight Maximum Height of the field
     * @param cacheSize Size of the internal cache, use 0 to disable
     */
    public Pathfinder(final T[][] field, final int maxWidth, final int maxHeight, final int cacheSize) {
        this(field, maxWidth, maxHeight, cacheSize, true, false);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation. Will be grounded.
     * 
     * @param field The field to get the data from
     */
    public Pathfinder(final AbstractGameField<T> field) {
        this(field.getField(), field.getMaxWidth(), field.getMaxHeight(), true);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation. Will be grounded.
     * 
     * @param field           The field to get the data from
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     */
    public Pathfinder(final AbstractGameField<T> field, boolean canWalkDiagonal) {
        this(field, canWalkDiagonal, false);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation.
     * 
     * @param field           The field to get the data from
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     * @param canFly          Will be true if the Pathfinder can fly - this means it
     *                        will not check fields for being walkable.
     */
    public Pathfinder(final AbstractGameField<T> field, boolean canWalkDiagonal, boolean canFly) {
        this(field.getField(), field.getMaxWidth(), field.getMaxHeight(), DEFAULT_CACHE_SIZE, canWalkDiagonal, canFly);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation.
     * 
     * @param field     The field to get the data from
     * @param cacheSize Size of the internal cache, use 0 to disable
     */
    public Pathfinder(final AbstractGameField<T> field, final int cacheSize) {
        this(field.getField(), field.getMaxWidth(), field.getMaxHeight(), cacheSize);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation. Will be grounded.
     * 
     * @param field           The field to get the data from
     * @param cacheSize       Size of the internal cache, use 0 to disable
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     */
    public Pathfinder(final AbstractGameField<T> field, final int cacheSize, boolean canWalkDiagonal) {
        this(field.getField(), field.getMaxWidth(), field.getMaxHeight(), cacheSize, canWalkDiagonal, false);
    }

    /**
     * Will reference the embedded field for path finding, this means that all
     * changes to the field will be known by the pathfinder and considered with the
     * <i>next</i> calculation.
     * 
     * @param field           The field to get the data from
     * @param cacheSize       Size of the internal cache, use 0 to disable
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     * @param canFly          Will be true if the Pathfinder can fly - this means it
     *                        will not check fields for being walkable.
     */
    public Pathfinder(final AbstractGameField<T> field, final int cacheSize, boolean canWalkDiagonal,
            final boolean canFly) {
        this(field.getField(), field.getMaxWidth(), field.getMaxHeight(), cacheSize, canWalkDiagonal, canFly);
    }

    /**
     * Will reference this field for path finding, this means that all changes to
     * the field will be known by the pathfinder and considered with the <i>next</i>
     * calculation.
     * 
     * @param field           The field to use
     * @param maxWidth        Maximum width of the field
     * @param maxHeight       Maximum Height of the field
     * @param cacheSize       Size of the internal cache, use 0 to disable
     * @param canWalkDiagonal Should the Pathfinder allow diagonal movement?
     * @param canFly          Will be true if the Pathfinder can fly - this means it
     *                        will not check fields for being walkable.
     */
    public Pathfinder(final T[][] field, final int maxWidth, final int maxHeight, final int cacheSize,
            boolean canWalkDiagonal, boolean canFly) {
        this.field = field;
        this.width = maxWidth;
        this.height = maxHeight;
        if (cacheSize > 0) {
            cache = new RingBuffer<>(cacheSize);
        } else {
            cache = null;
        }
        this.canWalkDiagonal = canWalkDiagonal;
        this.flying = canFly;
    }

    public T[][] getField() {
        return this.field;
    }

    /**
     * To be called, whenever the field changes, this will prevent faulty caches
     * from being used.
     */
    public void invalidateCache() {
        this.cache.clear();
    }

    private PriorityQueue<WayPoint> constructQueue() {
        // compare
        return new PriorityQueue<>(
                (final WayPoint w1, final WayPoint w2) -> Integer.compare(w1.getCost(), w2.getCost()));
    }

    /**
     * Checks if there is a path between the two points
     * 
     * @param start The start of the path
     * @param end   The end of the path
     * 
     * @return True if there is a path, false otherwise.
     */
    public boolean connected(final Point start, final Point end) {
        return !Objects.equals(find(start, end, false), WayPoint.INVALID);
    }

    /**
     * Checks if there is a path between the two points
     * 
     * @param start         The start of the path
     * @param end           The end of the path
     * @param checkStartEnd Should start and end be checked for being walkable?
     * 
     * @return True if there is a path, false otherwise.
     */
    public boolean connected(final Point start, final Point end, final boolean checkStartEnd) {
        return !Objects.equals(find(start, end, checkStartEnd), WayPoint.INVALID);
    }

    /**
     * Calculates the cheapest way between start and end using the supplied field.
     * Will not check if start and end are both walkable. Returns it as a path.
     * 
     * @param start The start of the path
     * @param end   The end of the path
     * 
     * @return The path starting with the start-point and ending with the end point.
     *         Returns {@link Path#INVALID_PATH} if there is no path
     * 
     * @see #find(Point, Point, boolean)
     */
    public Path findPath(final Point start, final Point end) {
        final WayPoint found = find(start, end, false);
        return Objects.equals(found, WayPoint.INVALID) ? Path.INVALID_PATH : new Path(found);
    }

    /**
     * Calculates the cheapest way between start and end using the supplied field.
     * Will not check if start and end are both walkable. Returns it as a path.
     * 
     * @param start         The start of the path
     * @param end           The end of the path
     * @param checkStartEnd Should start and end be checked for being walkable?
     * 
     * @return The path starting with the start-point and ending with the end point.
     *         Returns {@link Path#INVALID_PATH} if there is no path
     * 
     * @see #find(Point, Point, boolean)
     */
    public Path findPath(final Point start, final Point end, final boolean checkStartEnd) {
        final WayPoint found = find(start, end, checkStartEnd);
        return Objects.equals(found, WayPoint.INVALID) ? Path.INVALID_PATH : new Path(found);
    }

    /**
     * Calculates the cheapest way between start and end using the supplied field.
     * Will not check if start and end are both walkable.
     * 
     * @param start The start of the path
     * @param end   The end of the path
     * 
     * @return The populated 'end' of the path - feed upwards with the father, null
     *         if error, empty pat if no path found
     */
    public WayPoint find(final Point start, final Point end) {
        return find(start, end, false);
    }

    /**
     * Calculates the cheapest way between start and end using the supplied field.
     * 
     * @param start         The start of the path
     * @param end           The end of the path
     * @param checkStartEnd Should start and end be checked for being walkable?
     * 
     * @return The populated 'end' of the path - feed upwards with the father, will
     *         be {@link WayPoint#INVALID} if nothing found.
     */
    public WayPoint find(final Point start, final Point end, final boolean checkStartEnd) {
        // First: validate, that both points are on the field
        if (startOrEndAreInvalid(start, end)) {
            magpie.writeError(
                    "Points: start " + start + " and end " + end + " have both be to be in the field, they are not.",
                    "Find");
            return WayPoint.INVALID;
        }

        // should the finder check start and end to be walkable?
        if (startAndEndAreNotVisibleButShouldBe(start, end, checkStartEnd)) {
            magpie.writeError("Points: start " + start + " and end " + end
                    + " have both be to be walkable (set by flag: checkStartEnd), they are not.", "Find");
            return WayPoint.INVALID; // Start or end not walkable
        }
        // Is there something cached?
        final WayPoint target = BufferElement.get(cache, new ImmutablePair<>(start, end));
        if (target != null) {
            magpie.writeInfo("Using cached value for: start: " + start + " and end: " + end + ". Which is: " + target,
                    "Find");
            return target;
        }

        final PriorityQueue<WayPoint> queue = constructQueue();
        final WayPoint[][] calc = new WayPoint[height][width];

        // init with heuristic costs using simple 'guessing'
        for (int y = 0; y < calc.length; y++) {
            calculateHeuristicCostsForLine(end, calc, y);
        }

        // init start, maybe make access-wrapper?
        calc[start.getY()][start.getX()].setCost(0); // staying start is cheap :D

        // Perform main calculation -- run until target field reached
        // Who cares about performance ;) - hope for java to not use bitmaps :D
        final boolean[][] visited = new boolean[height][width];

        queue.add(new WayPoint(start));

        WayPoint current;
        int x;
        int y;
        while (!queue.isEmpty()) {
            current = queue.remove();
            x = current.getX();
            y = current.getY();
            visited[y][x] = true;

            if (current.equals(end)) { // Found end!
                return appendEndAndReturnFinalWayPoint(start, end, current);
            }
            // was a blocking end-check to toss away
            else if (fieldIsNotValidToWalkOn(start, current, x, y)) {
                // we do not traverse FROM this field but it COULD be the end if we allow the
                // end to be blocked
                continue;
            }

            // validate all surrounding fields, there is no point
            // in optimizing this with a for-loop as this is just checking
            // the 8 neighbour-fields if they are valid
            // maybe we will pre-request the lengths?
            checkStepsFromCurrentPoint(checkStartEnd, queue, calc, visited, current, x, y);
        }

        // not found
        magpie.writeInfo("No path found for start: " + start + " and end: " + end + ".", "Find");
        return WayPoint.INVALID;
    }

    private boolean fieldIsNotValidToWalkOn(final Point start, WayPoint current, int x, int y) {
        return !current.equals(start) && !flying && field[y][x].blocksWay();
    }

    private WayPoint appendEndAndReturnFinalWayPoint(final Point start, final Point end, WayPoint current) {
        cache.add(new BufferElement(start, end, current)); // cache it
        magpie.writeInfo("Found and cached: start: " + start + " end: " + end + " with found : " + current, "Find");
        return current;
    }

    private boolean startAndEndAreNotVisibleButShouldBe(final Point start, final Point end,
            final boolean checkStartEnd) {
        return checkStartEnd
                && (field[start.getY()][start.getX()].blocksWay() || field[end.getY()][end.getX()].blocksWay());
    }

    private boolean startOrEndAreInvalid(final Point start, final Point end) {
        return !Objects.requireNonNull(start, "Start").isOnField(field)
                || !Objects.requireNonNull(end, "End").isOnField(field);
    }

    private void calculateHeuristicCostsForLine(final Point end, final WayPoint[][] calc, int y) {
        for (int x = 0; x < field[y].length; x++) {
            calc[y][x] = new WayPoint(x, y);
            calc[y][x].setCostHeuristic(Math.abs(x - end.getX()) + Math.abs(y - end.getY()));
        }
        // fill rest with maximum cost:
        for (int x = field[y].length; x < calc[y].length; x++) {
            calc[y][x] = new WayPoint(x, y);
            calc[y][x].setCostHeuristic(Integer.MAX_VALUE);
        }
    }

    private void checkStepsFromCurrentPoint(final boolean checkStartEnd, final PriorityQueue<WayPoint> queue,
            final WayPoint[][] calc, final boolean[][] visited, WayPoint current, int x, int y) {
        // y - 1 linear
        if (y > 0 && x < field[y - 1].length - 1) { // y inner high
            updatePlausible(calc[y - 1][x], current, visited, queue, checkStartEnd);
        }

        // y linear
        // We have to make this additional check as we do not know if the line has data
        if (x > 0 && x < field[y].length) { // x inner eft
            updatePlausible(calc[y][x - 1], current, visited, queue, checkStartEnd);
        }

        if (x < field[y].length - 1) { // x inner right
            updatePlausible(calc[y][x + 1], current, visited, queue, checkStartEnd);
        }

        // y + 1 linear
        if (y + 1 < height && x < field[y + 1].length - 1) { // y inner low
            updatePlausible(calc[y + 1][x], current, visited, queue, checkStartEnd);
        }

        // Maybe this diagonal walking can be outsourced in the future
        // diagonals
        if (y > 0) {
            checkWalkDiagonalDown(checkStartEnd, queue, calc, visited, current, x, y);
        }

        if (y + 1 < height) {
            checkWalkDiagonalUp(checkStartEnd, queue, calc, visited, current, x, y);
        }
    }

    private void checkWalkDiagonalUp(final boolean checkStartEnd, final PriorityQueue<WayPoint> queue,
            final WayPoint[][] calc, final boolean[][] visited, WayPoint current, int x, int y) {
        // we have to guard this, as we do not now if the line has even data
        if (canWalkDiagonal && x > 0 && x < field[y + 1].length) { // x inner left
            updatePlausible(calc[y + 1][x - 1], current, visited, queue, checkStartEnd);
        }

        if (canWalkDiagonal && x < field[y + 1].length - 1) { // x inner right
            updatePlausible(calc[y + 1][x + 1], current, visited, queue, checkStartEnd);
        }
    }

    private void checkWalkDiagonalDown(final boolean checkStartEnd, final PriorityQueue<WayPoint> queue,
            final WayPoint[][] calc, final boolean[][] visited, WayPoint current, int x, int y) {
        // We have to make this additional check as we do not know if the line has data
        if (canWalkDiagonal && x > 0 && x < field[y - 1].length) { // x inner left
            updatePlausible(calc[y - 1][x - 1], current, visited, queue, checkStartEnd);
        }

        if (canWalkDiagonal && x < field[y - 1].length - 1) { // x inner right
            updatePlausible(calc[y - 1][x + 1], current, visited, queue, checkStartEnd);
        }
    }

    private void updatePlausible(final WayPoint check, final WayPoint current, final boolean[][] visited,
            final PriorityQueue<WayPoint> queue, boolean checkStartEnd) {
        if (visited[check.getY()][check.getX()])
            return; // already tried
        final IAmWalkable target = field[check.getY()][check.getX()];
        // We will check this later

        if (checkStartEnd && !this.flying && target.blocksWay())
            return; // is not walkable

        final int walkingCost = current.getCost() + check.getCostHeuristic() + target.wayCost();
        final boolean alreadyQueued = queue.contains(check);
        if (wayPointIsValidForChecking(check, walkingCost, alreadyQueued)) {
            // not already 'to-see' or (now) cheaper than the current one?
            updateWayPointDataAndAddHimToTheCandiates(check, current, queue, walkingCost, alreadyQueued);
        }
    }

    private void updateWayPointDataAndAddHimToTheCandiates(final WayPoint check, final WayPoint current,
            final PriorityQueue<WayPoint> queue, final int walkingCost, final boolean alreadyQueued) {
        check.setCost(walkingCost);
        check.setFather(current);
        if (!alreadyQueued) {
            queue.add(check); // add it - but do not be redundant
        }
    }

    private boolean wayPointIsValidForChecking(final WayPoint check, final int walkingCost,
            final boolean alreadyQueued) {
        return !alreadyQueued || walkingCost < check.getCost();
    }

}