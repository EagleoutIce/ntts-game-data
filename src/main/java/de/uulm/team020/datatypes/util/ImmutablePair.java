package de.uulm.team020.datatypes.util;

/**
 * Simple implementation to showcase {@link AbstractPair}, this Variant is
 * immutable.
 * 
 * @see Pair
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class ImmutablePair<K, V> extends AbstractPair<K, V> {

    /**
     * Initialize a new Pair
     * 
     * @param key   the key of Type K
     * @param value the value of Type V
     */
    public ImmutablePair(K key, V value) {
        super(key, value);
    }

    /**
     * Build a new ImmutablePair. No Magic just a different way.
     * 
     * @param <K>   Type of the Key
     * @param <V>   Type of the value
     * @param key   The desired Key
     * @param value The desired Value
     * @return New Object via the constructor
     */
    public static <K, V> ImmutablePair<K, V> of(K key, V value) {
        return new ImmutablePair<>(key, value);
    }

    public K getKey() {
        return this.getLeft();
    }

    /**
     * Not supported for an immutable Variant
     * 
     * @param ignored -ignored-
     */
    public void setKey(K ignored) {
        throw new UnsupportedOperationException("This pair is immutable");
    }

    public V getValue() {
        return this.getRight();
    }

    /**
     * Not supported for an immutable Variant
     * 
     * @param ignored -ignored-
     */
    public void setValue(V ignored) {
        throw new UnsupportedOperationException("This pair is immutable");
    }

}