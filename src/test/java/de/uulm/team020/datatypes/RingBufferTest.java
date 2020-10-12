package de.uulm.team020.datatypes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Data")
public class RingBufferTest {

    private <T> RingBuffer<T> get(int size) {
        RingBuffer<T> ringBuffer = new RingBuffer<>(size);
        Assertions.assertEquals(size, ringBuffer.capacity(), "Capacity as set.");
        Assertions.assertEquals(0, ringBuffer.size(), "No element added.");
        for (T t : ringBuffer) {
            Assertions.fail("The element: " + t + " shouldn't be in the buffer, it should be empty");
        }
        Assertions.assertTrue(ringBuffer.isEmpty(), "Should be empty as nothing has been added");
        return ringBuffer;
    }

    @Test
    @Tag("Util")
    @Order(1)
    @DisplayName("[RingBuffer] Test construction.")
    void test_construct() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> get(-3));
        Assertions.assertThrows(IllegalArgumentException.class, () -> get(-2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> get(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> get(0));

        for (int i = 1; i < 12; i++) {
            get(i);
        }
    }

    @Test
    @Tag("Util")
    @Order(2)
    @DisplayName("[RingBuffer] Test simple fill without wrap.")
    void test_simpleFill() {
        RingBuffer<Integer> ringBuffer = get(4);

        for (int i = 0; i < 4; i++) {
            ringBuffer.add(i);
        }

        Assertions.assertEquals(4, ringBuffer.size(), "Has to be 4, as 4 have been added");
        Assertions.assertEquals(4, ringBuffer.capacity(), "Has to be 4 (still), as set.");
        Assertions.assertArrayEquals(new Integer[] { 0, 1, 2, 3 }, ringBuffer.getBuffer(), "Should be as set");


        int idx = 0;
        final Integer[] wanted = new Integer[] { 0, 1, 2, 3 };
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted for idx: " + idx);
        }

        Assertions.assertFalse(ringBuffer.isEmpty(), "Shouldn't be empty as something has been added");
    }

    @Test
    @Tag("Util")
    @Order(3)
    @DisplayName("[RingBuffer] Test use with wrap.")
    void test_replaceUse() {
        RingBuffer<Integer> ringBuffer = get(4);

        for (int i = 0; i < 9; i++) {
            ringBuffer.add(i);
        }

        Assertions.assertEquals(4, ringBuffer.size(), "Has to be 4, even if 8 have been added");
        Assertions.assertEquals(4, ringBuffer.capacity(), "Has to be 4 (still), as set.");
        Assertions.assertArrayEquals(new Integer[] { 8, 5, 6, 7 }, ringBuffer.getBuffer(), "Should be as set");

        int idx = 0;
        final Integer[] wanted = new Integer[] { 5, 6, 7, 8 };
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted for idx: " + idx);
        }

        Assertions.assertFalse(ringBuffer.isEmpty(), "Shouldn't be empty as something has been added");
    }

    @Test
    @Tag("Util")
    @Order(4)
    @DisplayName("[RingBuffer] Test use of foreach when not completed")
    void test_incompleteForEach() {
        RingBuffer<Integer> ringBuffer = get(6);

        for (int i = 0; i < 5; i++) {
            ringBuffer.add(i);
        }
        Assertions.assertFalse(ringBuffer.isEmpty(), "Shouldn't be empty as something has been added");

        Assertions.assertEquals(5, ringBuffer.size(), "Has to be 5, as 5 have been added");
        Assertions.assertEquals(6, ringBuffer.capacity(), "Has to be 6 (still), as set.");
        Assertions.assertArrayEquals(new Integer[] { 0, 1, 2, 3, 4, null }, ringBuffer.getBuffer(), "Should be as set");

        int idx = 0;
        final Integer[] wanted = new Integer[] { 0, 1, 2, 3, 4 };
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted for idx: " + idx);
        }

        // should work again => not remove
        idx = 0;
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted (again) for idx: " + idx);
        }
        Assertions.assertEquals(5, ringBuffer.size(), "Has to be 5, as 5 have been added");
        Assertions.assertFalse(ringBuffer.isEmpty(), "Shouldn't be empty as something has been added");
    }

    @Test
    @Tag("Util")
    @Order(5)
    @DisplayName("[RingBuffer] Test use of clear")
    void test_clear() {
        RingBuffer<Integer> ringBuffer = get(6);

        for (int i = 0; i < 4; i++) {
            ringBuffer.add(i);
        }

        Assertions.assertEquals(4, ringBuffer.size(), "Has to be 4, as 4 have been added");
        Assertions.assertEquals(6, ringBuffer.capacity(), "Has to be 6 (still), as set.");
        Assertions.assertArrayEquals(new Integer[] { 0, 1, 2, 3, null, null }, ringBuffer.getBuffer(), "Should be as set");

        int idx = 0;
        Integer[] wanted = new Integer[] { 0, 1, 2, 3 };
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted for idx: " + idx);
        }
        
        ringBuffer.clear();
        Assertions.assertEquals(0, ringBuffer.size(), "Has to be 0, as it has been cleared");
        Assertions.assertEquals(6, ringBuffer.capacity(), "Has to be 6 (still), as set.");
        for (Integer i : ringBuffer) {
            Assertions.fail("The element: " + i + " shouldn't be in the buffer, it should be empty");
        }
        Assertions.assertTrue(ringBuffer.isEmpty(), "Should be empty as nothing has been added");


        for (int i = 0; i < 142; i++) {
            ringBuffer.add(i);
        }

        Assertions.assertEquals(6, ringBuffer.size(), "Has to be 6, as enough elements have been added :D");
        Assertions.assertEquals(6, ringBuffer.capacity(), "Has to be 6 (still), as set.");
        Assertions.assertArrayEquals(new Integer[] { 138, 139, 140, 141, 136, 137 }, ringBuffer.getBuffer(), "Should be as set");

        idx = 0;
        wanted = new Integer[] { 136, 137, 138, 139, 140, 141 };
        for (Integer integer : ringBuffer) {
            Assertions.assertEquals(wanted[idx++], integer, "Should be as wanted for idx: " + idx);
        }
        Assertions.assertFalse(ringBuffer.isEmpty(), "Shouldn't be empty as something has been added");
    }
}