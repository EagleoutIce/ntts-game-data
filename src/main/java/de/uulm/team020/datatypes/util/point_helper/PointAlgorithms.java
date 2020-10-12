package de.uulm.team020.datatypes.util.point_helper;

import java.util.ArrayList;
import java.util.List;

import de.uulm.team020.datatypes.util.DistanceMetric;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.NumericHelper;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.helper.pathfinding.Path.WayPoint;

/**
 * This class holds some algorithms that can be used with points. It will act as
 * a static helper
 * 
 * @author Florian Sihler
 * @version 1.0, 07/07/2020
 */
public class PointAlgorithms {

    // Hide default one
    private PointAlgorithms() {
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
        // compute initial deltas
        final int deltaX = endX - startX;
        final int deltaY = endY - startY;
        // compute signum to get correct directions on iter
        final int sigX = deltaX >= 0 ? +1 : -1; // if == 0 we will guard otherwise
        final int sigY = deltaY >= 0 ? +1 : -1; // if == 0 we will guard otherwise
        // error for calculation
        // the resulting path -- will be populated in the process
        Path result = new Path();
        // if it is a direct line construct path from there
        if (deltaX == 0) {
            // is direct vertical as no x movement
            for (int y = startY; sigY > 0 ? y < endY : y > endY; y += sigY) {
                result.append(new WayPoint(startX, y));
            }
        } else if (deltaY == 0) {
            // is direct horizontal as no y movement
            for (int x = startX; sigX > 0 ? x < endX : x > endX; x += sigX) {
                result.append(new WayPoint(x, startY));
            }
        } else {
            result = walkDiagonal(startX, startY, endX, endY, deltaX, deltaY);
        }
        result.append(new WayPoint(endX, endY));
        return result;
    }

    private static Path walkDiagonal(final int startX, final int startY, final int endX, final int endY, int deltaX,
            int deltaY) {
        // Stores the constructed path - might omit the end point which will get added
        // at last
        final Path result = new Path();
        // calculate with error
        int currentError = 0;
        // recompute the signs - reduces number of method args :D
        final int sigX = deltaX >= 0 ? +1 : -1; // if == 0 we will guard otherwise
        final int sigY = deltaY >= 0 ? +1 : -1; // if == 0 we will guard otherwise
        // We just need abs deltas from now one so we abs them here:
        deltaX = Math.abs(deltaX);
        deltaY = Math.abs(deltaY);
        // For end calculation, this helps to calculate if the field-edge is on the line
        // drawn by start and end
        final double distStartEnd = Point.getDistance(startX, startY, endX, endY);
        // for iteration and to keep start and end coordinates untouched
        int currentX = startX;
        int currentY = startY;
        // should the next coordinate - the strut - be skipped?
        boolean skipNext = false;
        for (int i = 0; i < deltaX + deltaY; i++) {
            // only append the point, if its edge in line direction does not directly fit on
            // to the line
            final double cX = currentX + sigX / 2.0; // get edge coordinate
            final double cY = currentY + sigY / 2.0;
            // we see if the edge-point 'c' is on the field by check if the sum of the
            // distances to start and end is the same as the distance between start and end
            final double distAC = Point.getDistance(startX, startY, cX, cY);
            final double distCB = Point.getDistance(cX, cY, endX, endY);
            // if this strut should not be skipped
            if (!skipNext) {
                // add the point
                result.append(new WayPoint(currentX, currentY));
                // If we have a edge coordinate directly on the line, we can skip the next strut
                // as the line is not crossing the strut-fields found
                skipNext = NumericHelper.closeEnough(distStartEnd, distAC + distCB, 0.0001);
            } else {
                // The next field is not to be skipped, as it is not possible for a line to
                // contain any space
                skipNext = false;
            }

            // we compute the next direction we will walk to
            final int e1 = currentError + deltaY;
            final int e2 = currentError - deltaX;
            // calculate the better one and update the current error delta
            if (Math.abs(e1) < Math.abs(e2)) {
                currentX += sigX;
                currentError = e1;
            } else {
                currentY += sigY;
                currentError = e2;
            }
        }
        return result;
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
        if (radius < 0) {
            throw new IllegalArgumentException("The radius must be >= 0, not: " + radius);
        }
        // We do not really care about rounding errors, as it is just a sloppy guess to
        // prevent unnecessary reallocations
        final List<Point> result = new ArrayList<>((int) (Math.PI * radius * radius));
        final int xMiddle = center.getX();
        final int xStart = xMiddle - radius;
        final int xEnd = xMiddle + radius;
        final int yStart = center.getY() - radius;
        final int yEnd = center.getY() + radius;

        // lets start having fun
        for (int y = yStart; y <= yEnd; y++) {
            // if the middle point isn't in the circle we can skip the line
            final Point yCenter = new Point(xMiddle, y);
            if (!isInCircleRadius(center, yCenter, radius, tolerance, metric)) {
                continue;
            }
            // check from left until found one:
            final int xLineStart = findRadiusBetween(center, y, xStart, xMiddle, 1, radius, tolerance, metric);

            // check from right until found one:
            final int xLineEnd = findRadiusBetween(center, y, xEnd, xMiddle, -1, radius, tolerance, metric);

            // now add everything from line-start to line-end to the set:
            for (int x = xLineStart; x <= xLineEnd; x++) {
                result.add(new Point(x, y));
            }
        }
        return result;
    }

    private static int findRadiusBetween(final Point center, final int y, final int start, final int end,
            final int direction, final int radius, final double tolerance, final DistanceMetric metric) {
        final Point p = new Point(start, y);
        for (int x = start; direction > 0 ? x <= end : x >= end; x += direction, p.setX(x)) {
            if (isInCircleRadius(center, p, radius, tolerance, metric)) {
                return x;
            }
        }
        return start;
    }

    private static boolean isInCircleRadius(final Point center, final Point p, final int radius, final double tolerance,
            final DistanceMetric metric) {
        return metric.getDistance(center, p) - tolerance <= radius;
    }

}