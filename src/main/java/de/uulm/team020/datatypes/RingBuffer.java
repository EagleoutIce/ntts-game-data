package de.uulm.team020.datatypes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Just a simple array-based ring buffer implementation without separated read
 * and write, iterations will leave the current index untouched.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/17/2020
 */
public class RingBuffer<T> implements Iterable<T> {

    private final int capacity;
    private int size;
    private int index;
    private final T[] buffer;

    /**
     * Construct a new RingBuffer with the given capacity
     * 
     * @param capacity Maximum number of elements
     */
    @SuppressWarnings("unchecked")
    public RingBuffer(final int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity (" + capacity + ") has to be greater than 0.");
        }
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.index = 0;
        this.size = 0;
    }

    /**
     * Returns the element hat the specific index. Will throw an
     * {@link IndexOutOfBoundsException} if not in bounds.
     * 
     * @param idx The index you desire
     * @return The element at this position, can be null
     */
    public T getSpecific(final int idx) {
        if (idx < 0 || idx >= size)
            throw new IndexOutOfBoundsException("Choose in: [0, " + size + "), not: " + idx);
        return buffer[idx];
    }

    /**
     * Add a new element to the ring buffer, will overwrite older elements
     * 
     * @param element The element to add
     */
    public void add(final T element) {
        buffer[index] = element;
        if (size < capacity) { // added and not overwritten
            size += 1;
        }
        index = (index + 1) % capacity;
    }

    /**
     * "Filled"-Size check with no recalculation
     * 
     * @return Size of the filled elements, after the first cycle, this will always
     *         equal the capacity
     */
    public int size() {
        return this.size;
    }

    /**
     * Empty-check as in collections
     * 
     * @return True, if there is no element in the buffer
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Just a getter for the set capacity
     * 
     * @return Maximum capacity of the ring buffer
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Provides raw access to the underlying array, use with care
     * 
     * @return The underlying array
     */
    public T[] getBuffer() {
        return this.buffer;
    }

    /**
     * Clear the buffer from all contained elements, this will <i>not</i> delete
     * them, they will not be even erased from the buffer, they will just be
     * overwritten and no more recognized by iterations etc, but they can still be
     * retrieved via {@link #getBuffer()}
     */
    public void clear() {
        this.size = 0;
        this.index = 0;
    }

    /**
     * Searches for an element in the buffer
     * 
     * @param element The element to search for, can be null
     * @return True if the element is in the populated part of the ring buffer,
     *         false otherwise
     */
    public boolean contains(final T element) {
        for (final T t : buffer) {
            if (Objects.equals(t, element))
                return true;
        }
        return false;
    }

    /**
     * Used for the iteration
     * 
     * @return Iterator for this instance
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            // if not all have been filled, this will be 0 to start with the olders
            // otherwise it will start with the 'next' in the ring
            private int currentIndex = (size == capacity) ? index : 0;
            private int visited = 0;

            @Override
            public boolean hasNext() {
                return visited < size;
            }

            @Override
            public T next() {
                if (visited >= size || currentIndex >= buffer.length)
                    throw new NoSuchElementException();
                final T result = buffer[currentIndex];
                currentIndex = (currentIndex + 1) % capacity;
                visited += 1;
                return result;
            }
        };
    }

    @Override
    public String toString() {
        return "RingBuffer [buffer=" + Arrays.toString(buffer) + ", capacity=" + capacity + ", (next) index=" + index
                + ", size=" + size + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(buffer);
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof RingBuffer<?>))
            return false;
        RingBuffer<?> other = (RingBuffer<?>) obj;
        if (!Arrays.deepEquals(buffer, other.buffer))
            return false;
        return index == other.index;
    }

}