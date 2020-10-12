package de.uulm.team020.datatypes.util;

/**
 * Simple implementation to showcase {@link AbstractPair}, this Variant is
 * mutable.
 * 
 * @see ImmutablePair
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class Pair<K, V> extends AbstractPair<K, V> {

    /**
     * Initialize a new Pair, will set Key and Value both to {@code null}. As this
     * is a Mutable Pair, this shouldn't make any Problems.
     */
    public Pair() {
        super(null, null);
    }

    /**
     * Initialize a new Pair
     * 
     * @param key   the key of Type K
     * @param value the value of Type V
     */
    public Pair(K key, V value) {
        super(key, value);
    }

    /**
     * Build a new Pair. No Magic just a different way.
     * 
     * @param <K>   Type of the Key
     * @param <V>   Type of the value
     * @param key   The desired Key
     * @param value The desired Value
     * @return New Object via the constructor
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public K getKey() {
        return this.getLeft();
    }

    public void setKey(K key) {
        this.setLeft(key);
    }

    public V getValue() {
        return this.getRight();
    }

    public void setValue(V value) {
        this.setRight(value);
    }

}