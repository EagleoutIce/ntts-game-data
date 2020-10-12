package de.uulm.team020.helper;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Just a simple class to help with common random-tasks in java.
 * 
 * @author Florian Sihler
 * @version 2.0, 05/27/2020
 */
public class RandomHelper {

    private static Random random = null;

    // Hide the default constructor
    private RandomHelper() {
    }

    /**
     * Inject a seed. This will cause {@link #getRandom()} to be synchronized
     * 
     * @param seed The seed to inject
     */
    public static void injectSeed(long seed) {
        random = new Random(seed);
    }

    /**
     * If {@link #injectSeed(long)} was called this will return the synchronized
     * Random running that seed. Otherwise it will call the ThreadLocal-helper
     * 
     * @return A Random-Object to use for getting pseudo-random numbers
     */
    public static Random getRandom() {
        return random == null ? ThreadLocalRandom.current() : random;
    }

    public static final String DEFAULT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz0123456789ßäöüÄÖÜ";

    /**
     * Return a thread-save random integer
     * 
     * @param exclusiveUpper The exclusive upper bound
     * @return Random integer in [0, exclusiveUpper).
     */
    public static int rndInt(int exclusiveUpper) {
        return getRandom().nextInt(exclusiveUpper);
    }

    /**
     * Return a synchronized or non synchronized random integer -- depends on
     * {@link #injectSeed(long)} is called.
     * 
     * @param inclusiveLower The inclusive lower bound.
     * @param exclusiveUpper The exclusive upper bound.
     * @return Random integer in [inclusiveLower, exclusiveUpper).
     */
    public static int rndInt(int inclusiveLower, int exclusiveUpper) {
        return getRandom().nextInt(exclusiveUpper - inclusiveLower) + inclusiveLower;
    }

    /**
     * The classic coin flip
     * 
     * @return True or false by 50:50 chance
     */
    public static boolean flip() {
        return rndInt(2) == 1;
    }

    /**
     * Perform a custom flip
     * 
     * @param percentage The chance (p in [0, 1]) to be true
     * 
     * @return True or false by chance
     */
    public static boolean flip(double percentage) {
        return rndInt(100) < (percentage * 100);
    }

    /**
     * Return a random Element from a list - this will issue <i>one</i> get-access
     * 
     * @param <T>  The type of the list elements.
     * @param list The list to pick from.
     * @return The picked Element or null if empty
     */
    public static <T> T rndPick(List<T> list) {
        return list.isEmpty() ? null : list.get(rndInt(list.size()));
    }

    /**
     * Return a random Element from a set - this will have to skip for unwanted
     * 
     * @param <T> The type of the list elements.
     * @param set The set to pick from.
     * @return The picked Element or null if empty
     */
    public static <T> T rndPick(Set<T> set) {
        return set.isEmpty() ? null : set.stream().skip(rndInt(set.size())).findFirst().orElse(null);
    }

    /**
     * Return a random Element from an Array
     * 
     * @param <T> The type of the array elements.
     * @param arr The array to pick from.
     * @return The picked Element or null if empty
     */
    public static <T> T rndPick(T[] arr) {
        return arr.length > 0 ? arr[rndInt(arr.length)] : null;
    }

    /**
     * Return a random character from a String
     * 
     * @param str The String to pick from.
     * @return The picked Character or null if empty
     */
    public static char rndPick(String str) {
        return str.length() > 0 ? str.charAt(rndInt(str.length())) : null;
    }

    /**
     * Generate a random String, using {@value #DEFAULT_CHARACTERS}
     * 
     * @param length The length of the desired string, has to be positive
     * 
     * @return a string confirming to the constraints otherwise
     */
    public static String randomString(int length) {
        return randomString(length, length);
    }

    /**
     * Generate a random String, using {@value #DEFAULT_CHARACTERS}
     * 
     * @param minLen Minimum length of the string.
     * @param maxLen Maximum length of the string.
     * 
     * @return null if minLen &gt; maxLen, a string confirming to the constraints
     *         otherwise
     */
    public static String randomString(int minLen, int maxLen) {
        return randomString(minLen, maxLen, DEFAULT_CHARACTERS);
    }

    /**
     * Generate a random String
     * 
     * @param minLen        Minimum length of the string.
     * @param maxLen        Maximum length of the string.
     * @param characterPool The pool to choose from
     * 
     * @return null if minLen &gt; maxLen, a string confirming to the constraints
     *         otherwise
     */
    public static String randomString(final int minLen, final int maxLen, final String characterPool) {
        if (minLen > maxLen)
            return null;
        return randomString(rndInt(minLen, maxLen + 1), characterPool);
    }

    /**
     * Generate a String of finite length using the given pool
     * 
     * @param length        The length, negative means 0
     * @param characterPool The pool to use to generate the characters from
     * @return The string confirming to the constraints
     */
    public static String randomString(final int length, final String characterPool) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(rndPick(characterPool));
        }
        return stringBuilder.toString();
    }

}