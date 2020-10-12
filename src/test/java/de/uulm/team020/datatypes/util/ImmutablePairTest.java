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
 * Tests {@link ImmutablePair} and therefor {@link AbstractPair}.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class ImmutablePairTest {
    
    @Test @Tag("Util") @Order(1)
    @DisplayName("[IPair] Test getters and therefore construction of argument-ctor.")
    public void test_ctor_args() {  
        final String key = "Waffel";
        final Integer value = 42;
        ImmutablePair<String, Integer> pair = new ImmutablePair<>(key, value);

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
    @DisplayName("[IPair] Test Hashing.")
    @MethodSource("hashing_tests")
    public void test_hashing(final String a, final String b, boolean shouldEq) {  
        ImmutablePair<String, String> iPairA = new ImmutablePair<>(a, b);
        ImmutablePair<String, String> iPairB = new ImmutablePair<>(b, a);

        Assertions.assertEquals(a, iPairA.getKey(), "should be as initialized");
        Assertions.assertEquals(b, iPairA.getValue(), "should be as initialized");
        Assertions.assertEquals(b, iPairB.getKey(), "should be as initialized");
        Assertions.assertEquals(a, iPairB.getValue(), "should be as initialized");

        Assertions.assertEquals(shouldEq, iPairA.hashCode() == iPairB.hashCode(),  "Hashes should be " + (shouldEq?"same":"different"));
    }

    @Test @Tag("Util") @Order(4)
    @DisplayName("[IPair] Test Setters.")
    public void test_setter() {  
        final String a = "ValueA";
        final String b = "ValueB";
        ImmutablePair<String, String> pair = new ImmutablePair<>("", "");

        int hash1 = pair.hashCode();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> pair.setKey(a));

        int hash2 = pair.hashCode();

        Assertions.assertNotEquals(a, pair.getKey(), "shouldn't be as set");
        Assertions.assertEquals("", pair.getValue(), "should be initialized as empty String");

        Assertions.assertThrows(UnsupportedOperationException.class, () -> pair.setValue(a));
        int hash3 = pair.hashCode();

        Assertions.assertNotEquals(a, pair.getKey(), "shouldn't be as set");
        Assertions.assertNotEquals(b, pair.getValue(), "shouldn't be as set");

        Assertions.assertEquals(hash1, hash2, "Hashes should not differ");
        Assertions.assertEquals(hash2, hash3, "Hashes should not differ");
    }
}