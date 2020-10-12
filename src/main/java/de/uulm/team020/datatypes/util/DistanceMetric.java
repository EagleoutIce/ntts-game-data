package de.uulm.team020.datatypes.util;

/**
 * A Metric which is used by {@link Point} to calculate circles where the radius
 * is applied and checked by the given metric.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/08/2020
 */
@FunctionalInterface
public interface DistanceMetric {

    /**
     * Metric which calculates the distance between a and b
     * 
     * @param a The first point
     * @param b The second point
     * 
     * @return The distance
     */
    double getDistance(final Point a, final Point b);

}