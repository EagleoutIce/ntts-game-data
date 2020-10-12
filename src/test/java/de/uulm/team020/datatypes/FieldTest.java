package de.uulm.team020.datatypes;

import de.uulm.team020.datatypes.enumerations.FieldStateEnum;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testing the implementation of {@link Field}.
 *
 * @author Florian Sihler
 * @version 1.0, 03/30/2020
 */
class FieldTest {
    @Test
    @Tag("Util")
    @Order(1)
    @DisplayName("[Field] Test validity implementation.")
    void test_isValid() {
        Field f;
        // Test default Builder (shortcut for eliminating safe :P)
        for (FieldStateEnum s : FieldStateEnum.values()) {
            if (s == FieldStateEnum.SAFE)
                continue;
            f = new Field(s);
            Assertions.assertTrue(f.isValid(), f.getState() + " should be initialized correctly");
        }
        // Test Safe-Builder:
        Assertions.assertThrows(RuntimeException.class, () -> new Field(FieldStateEnum.SAFE),
                "The safeIndex has to be given");
        f = new Field(2);

        Assertions.assertTrue(f.isValid(), f.getState() + " should be initialized correctly with a safeIndex.");

        // Let's destroy a free field!
        f = new Field(FieldStateEnum.FREE);
        Assertions.assertTrue(f.isValid(), f.getState() + " should be initialized correctly.");
        f.setDestroyed(true);
        List<String> errors = f.checkValidity();
        Assertions.assertFalse(errors.isEmpty(), "There should be errors.");
        Assertions.assertFalse(f.isValid(), "The Field should not be valid.");
        Assertions.assertEquals(1, errors.size(), "There should be one error.");
        f.setDestroyed(false);
        Assertions.assertTrue(f.isValid(), f.getState() + " should be valid again.");
    }

    // here more test should appear when scenario construction is done...
}