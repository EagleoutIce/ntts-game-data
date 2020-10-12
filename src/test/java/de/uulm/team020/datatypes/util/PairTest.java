package de.uulm.team020.datatypes.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link Pair} and therefor {@link AbstractPair}.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class PairTest {

    @Test @Tag("Util") @Order(1)
    @DisplayName("[Pair] Test getters and therefore construction of default ctor.")
    public void test_ctor_default() {  
        Pair<String, Integer> pair = new Pair<>();

        Assertions.assertNull(pair.getKey(), "should be initialized as null");
        Assertions.assertNull(pair.getValue(), "should be initialized as null");
    }
    
    @Test @Tag("Util") @Order(2)
    @DisplayName("[Pair] Test getters and therefore construction of argument-ctor.")
    public void test_ctor_args() {  
        final String key = "Waffel";
        final Integer value = 42;
        Pair<String, Integer> pair = new Pair<>(key, value);

        Assertions.assertEquals(key, pair.getKey(), "should be as initialized");
        Assertions.assertEquals(value, pair.getValue(), "should be as initialized");
    }

    private static Stream<Arguments> hashing_tests() {
        return Stream.of(
            Arguments.arguments("Waffel", "Baffel", false),  
            Arguments.arguments("Waffel", "Waffel", true),
            Arguments.arguments(null, "Waffel", false)
        );
    }

    @ParameterizedTest @Tag("Util") @Order(3)
    @DisplayName("[Pair] Test Hashing.")
    @MethodSource("hashing_tests")
    public void test_hashing(final String a, final String b, boolean shouldEq) {  
        Pair<String, String> pairA = new Pair<>(a, b);
        Pair<String, String> pairB = new Pair<>(b, a);

        Assertions.assertEquals(a, pairA.getKey(), "should be as initialized");
        Assertions.assertEquals(b, pairA.getValue(), "should be as initialized");
        Assertions.assertEquals(b, pairB.getKey(), "should be as initialized");
        Assertions.assertEquals(a, pairB.getValue(), "should be as initialized");

        Assertions.assertEquals(shouldEq, pairA.hashCode() == pairB.hashCode(),  "Hashes should be " + (shouldEq?"same":"different"));
    }

    @Test @Tag("Util") @Order(4)
    @DisplayName("[Pair] Test Setters.")
    public void test_setter() {  
        final String a = "ValueA";
        final String b = "ValueB";
        Pair<String, String> pair = new Pair<>();

        int hash1 = pair.hashCode();

        pair.setKey(a);

        int hash2 = pair.hashCode();

        Assertions.assertEquals(a, pair.getKey(), "should be as set");
        Assertions.assertNull(pair.getValue(), "should be initialized as null");

        pair.setValue(b);
        int hash3 = pair.hashCode();

        Assertions.assertEquals(a, pair.getKey(), "should be as set");
        Assertions.assertEquals(b, pair.getValue(), "should be as set");

        Assertions.assertNotEquals(hash1, hash2, "Hashes should differ");
        Assertions.assertNotEquals(hash2, hash3, "Hashes should differ");
        Assertions.assertNotEquals(hash1, hash3, "Hashes should differ");
    }
}