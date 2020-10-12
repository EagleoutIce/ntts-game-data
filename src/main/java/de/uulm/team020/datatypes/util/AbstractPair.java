package de.uulm.team020.datatypes.util;

import java.util.Objects;

/**
 * Somewhat sophisticated Pair-Class managing all important Operations. It is up
 * to the deriving classes to allocate meaning to the capsuled Values.
 *
 * @param <L> Data type of the Left/Key-Element
 * @param <R> Data type of the Right/Value-Element
 *
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public abstract class AbstractPair<L, R> {

    /** Left/Key element */
    private L left;
    /** Right/Value element */
    private R right;

    /**
     * Left-value of the pair
     * 
     * @return {@link #left}
     */
    protected L getLeft() {
        return this.left;
    }

    /**
     * Sets left, don't use in child, if immutable
     * 
     * @param left New left value
     */
    protected void setLeft(L left) {
        this.left = left;
    }

    /**
     * Right-value of the pair
     * 
     * @return {@link #right}
     */
    protected R getRight() {
        return this.right;
    }

    /**
     * Sets right, don't use in child, if immutable
     * 
     * @param right New right value
     */
    protected void setRight(R right) {
        this.right = right;
    }

    /**
     * Creates a new pair with no left and no right key (they will be null)
     */
    protected AbstractPair() {
        this.left = null;
        this.right = null;
    }

    /**
     * Creates a new pair
     * 
     * @param left  left/key element
     * @param right right/value element
     */
    protected AbstractPair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "AbstractPair [left=" + left + ", right=" + right + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractPair)) {
            return false;
        }
        AbstractPair<?, ?> other = (AbstractPair<?, ?>) obj;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

}