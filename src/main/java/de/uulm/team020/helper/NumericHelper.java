package de.uulm.team020.helper;

/**
 * Just a helper, offering some numeric operations combining some of the Math
 * functions of the java.lang-core.
 *
 *
 * @author Florian Sihler
 * @version 1.0, 03/29/2020
 */
public class NumericHelper {

    /* Hide the public one */
    private NumericHelper() {
    }

    /**
     * Clamps the number between the given minimum and maximum
     *
     * @param num number to clamp
     * @param min the minimum value the number is allowed to have
     * @param max the maximum value the number is allowed to have
     * @return num if min &lt;= min &lt;= max, min if num &lt; min and max if num
     *         &gt; max
     */
    public static double getInBounds(double num, double min, double max) {
        return Math.min(max, Math.max(min, num));
    }

    /**
     * Clamps the number between the given minimum and maximum
     *
     * @param num number to clamp
     * @param min the minimum value the number is allowed to have
     * @param max the maximum value the number is allowed to have
     * @return num if min &lt;= min &lt;= max, min if num &lt; min and max if num
     *         &gt; max
     */
    public static int getInBounds(int num, int min, int max) {
        return Math.min(max, Math.max(min, num));
    }

    /**
     * Clamps the number between the given minimum and maximum
     *
     * @param num number to clamp
     * @param min the minimum value the number is allowed to have
     * @param max the maximum value the number is allowed to have
     * @return num if min &lt;= min &lt;= max, min if num &lt; min and max if num
     *         &gt; max
     */
    public static long getInBounds(long num, long min, long max) {
        return Math.min(max, Math.max(min, num));
    }

    /**
     * Just a simple checker, if a double is close enough to another
     * 
     * @param expected The double expected
     * @param got      The double got
     * @param error    The maximum error allowed
     * @return True if constraint fulfilled, false otherwise
     */
    public static boolean closeEnough(double expected, double got, double error) {
        return Math.abs(expected - got) <= error;
    }
}
