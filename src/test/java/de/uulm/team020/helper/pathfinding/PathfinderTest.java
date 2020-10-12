package de.uulm.team020.helper.pathfinding;

// As i am such a lazy typer: 
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.BAR_SEAT;
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.BAR_TABLE;
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.FIREPLACE;
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.FREE;
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.SAFE;
import static de.uulm.team020.datatypes.enumerations.FieldStateEnum.WALL;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.util.Point;

/**
 * Testing if the pathfinder works :D
 */
public class PathfinderTest {

    // There are scenarios too, but for the simple tests we will use
    // this "training-board":

    public static final FieldStateEnum[][] TRAINING_BOARD = new FieldStateEnum[][] {
            // 0 1 2 3 4 5 6 7
            new FieldStateEnum[] { WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL }, // 0
            new FieldStateEnum[] { WALL, FREE, BAR_SEAT, FREE, FREE, WALL, FREE, WALL }, // 1
            new FieldStateEnum[] { WALL, FREE, BAR_TABLE, FREE, FREE, WALL, FIREPLACE, WALL }, // 2
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, SAFE, WALL, FREE, WALL }, // 3
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, FREE, WALL, FREE, WALL }, // 4
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, FREE, FREE, FREE, WALL }, // 5
            new FieldStateEnum[] { WALL, FREE, WALL, WALL, WALL, WALL, FREE, WALL }, // 6
            new FieldStateEnum[] { WALL, FREE, FREE, WALL, FREE, FREE, FREE, WALL }, // 7
            new FieldStateEnum[] { WALL, FREE, FREE, WALL, FREE, FREE, FREE, WALL }, // 8
            new FieldStateEnum[] { WALL, WALL, FREE, FREE, FREE, BAR_SEAT, FREE, WALL }, // 9
            new FieldStateEnum[] { WALL, FREE, FREE, WALL, FREE, FREE, FREE, WALL }, // 10
            new FieldStateEnum[] { WALL, FREE, FREE, WALL, FREE, FREE, FREE, WALL }, // 11
            new FieldStateEnum[] { WALL, FREE, WALL, WALL, FREE, FREE, FREE, WALL }, // 12
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, FREE, WALL, WALL, WALL }, // 13
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, FREE, FREE, FREE, WALL }, // 14
            new FieldStateEnum[] { WALL, FREE, FIREPLACE, FREE, FREE, FREE, FREE, WALL }, // 15
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, FREE, FREE, FREE, WALL }, // 16
            new FieldStateEnum[] { WALL, WALL, WALL, WALL, WALL, WALL, FREE, WALL }, // 17
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, WALL, FREE, FREE, WALL }, // 18
            new FieldStateEnum[] { WALL, FREE, FREE, SAFE, WALL, FREE, WALL, WALL }, // 19
            new FieldStateEnum[] { WALL, FREE, FIREPLACE, FREE, WALL, WALL, FREE, WALL }, // 20
            new FieldStateEnum[] { WALL, FREE, FREE, FREE, WALL, SAFE, FREE, WALL }, // 21
            new FieldStateEnum[] { WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL }, // 22
    };

    public static final FieldStateEnum[][] TRAINING_BOARD_FREE = new FieldStateEnum[][] {
            // 0 1 2 3
            new FieldStateEnum[] { FREE, FREE, FREE, FREE }, // 0
            new FieldStateEnum[] { FREE, FREE, FREE, FREE }, // 1
            new FieldStateEnum[] { FREE, FREE, FREE, FREE }, // 2
            new FieldStateEnum[] { FREE, FREE, FREE }, // 3
    };

    public static final FieldStateEnum[][] TRAINING_BOARD_MEAN = new FieldStateEnum[][] {
            // 0 1 2
            new FieldStateEnum[] {}, // 0
            new FieldStateEnum[] { FREE, FREE, FREE }, // 1
            new FieldStateEnum[] { WALL, FREE }, // 2
            new FieldStateEnum[] {}, // 3
            new FieldStateEnum[] { WALL, FREE }, // 4
    };

    @Test
    @Tag("Util")
    @Order(1)
    @DisplayName("[Find] Test simple path-findings.")
    void test_SimplePath() {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD);
        Path path = find.findPath(new Point(1, 3), new Point(1, 16));

        Point[] fastest = new Point[] { new Point(1, 3), new Point(1, 4), new Point(1, 5), new Point(1, 6),
                new Point(1, 7), new Point(1, 8), new Point(2, 9), new Point(1, 10), new Point(1, 11), new Point(1, 12),
                new Point(1, 13), new Point(1, 14), new Point(1, 15), new Point(1, 16) };
        Assertions.assertEquals(fastest.length, path.size(), "14 points");
        Assertions.assertArrayEquals(fastest, path.toArray(), "Should be equal!");
        Assertions.assertTrue(find.connected(new Point(1, 3), new Point(1, 16)),
                "Should be connected as there is a path.");
    }

    @Test
    @Tag("Util")
    @Order(2)
    @DisplayName("[Find] Test simple path on same field.")
    void test_simplePathSamePoints() {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD);
        Path path = find.findPath(new Point(1, 2), new Point(1, 2));
        Point[] fastest = new Point[] { new Point(1, 2) };
        Assertions.assertEquals(1, path.size(), "Only 1 point as same");

        Assertions.assertArrayEquals(fastest, path.toArray(), "Should be equal!");
        Assertions.assertTrue(find.connected(new Point(1, 2), new Point(1, 2)), "Should be connected as same field");
    }

    public static Stream<Arguments> generate_noPathsBetween() {
        return Stream.of(Arguments.arguments(new Point(0, 0), new Point(0, 22)),
                Arguments.arguments(new Point(0, 0), new Point(0, 0)),
                Arguments.arguments(new Point(0, 0), new Point(0, 1)),
                Arguments.arguments(new Point(0, 0), new Point(7, 21)),
                Arguments.arguments(new Point(1, 1), new Point(1, 21)),
                Arguments.arguments(new Point(1, 2), new Point(1, 21)),
                Arguments.arguments(new Point(1, 2), new Point(1, 22)),
                Arguments.arguments(new Point(3, 1), new Point(3, 17)),
                Arguments.arguments(new Point(2, 21), new Point(6, 11)),
                Arguments.arguments(new Point(3, 19), new Point(5, 19)),
                Arguments.arguments(new Point(3, 18), new Point(5, 16)), // same check diagonal
                Arguments.arguments(new Point(-1, -1), new Point(1, 1)),
                Arguments.arguments(new Point(-1, -1), new Point(-1, -1)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(3)
    @DisplayName("[Find] Test simple path on same field.")
    @MethodSource("generate_noPathsBetween")
    void test_noPathsBetween(Point start, Point end) {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD);
        Path path = find.findPath(start, end, true);
        Assertions.assertEquals(1, path.size(), "Only 1 point as same, but: " + path);
        // connected only if not equals?
        Assertions.assertEquals(Path.INVALID_PATH, path, "Should be illegal!");
        Assertions.assertTrue(path.invalid(), "Should be illegal => invalid!");
        Assertions.assertFalse(find.connected(start, end, true),
                "Should not be connected, for: " + start + " and " + end);
    }

    public static Stream<Arguments> generate_invalidPointsForFree() {
        return Stream.of(Arguments.arguments(new Point(-1, -1), new Point(1, 1)),
                Arguments.arguments(new Point(-1, -1), new Point(-1, -1)),
                Arguments.arguments(new Point(3, 3), new Point(3, 3)),
                Arguments.arguments(new Point(2, 3), new Point(3, 3)),
                Arguments.arguments(new Point(3, 3), new Point(3, 2)),
                Arguments.arguments(new Point(0, 0), new Point(3, 3)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(3)
    @DisplayName("[Find] Test invalid paths on free field")
    @MethodSource("generate_invalidPointsForFree")
    void test_invalidPointsOnFreeBoard(Point start, Point end) {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD_FREE);
        Path path = find.findPath(start, end, true);
        Assertions.assertEquals(1, path.size(), "Only 1 point as same, but: " + path);
        // connected only if not equals?
        Assertions.assertEquals(Path.INVALID_PATH, path, "Should be illegal!");
        Assertions.assertTrue(path.invalid(), "Should be illegal => invalid!");
        Assertions.assertFalse(find.connected(start, end, true),
                "Should not be connected, for: " + start + " and " + end);
    }

    public static Stream<Arguments> generate_validPointsForFree() {
        return Stream.of(
                Arguments.arguments(new Point(0, 0), new Point(1, 1), new Point[] { new Point(0, 0), new Point(1, 1) }),
                Arguments.arguments(new Point(0, 0), new Point(0, 3),
                        new Point[] { new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3) }),
                Arguments.arguments(new Point(1, 1), new Point(1, 2),
                        new Point[] { new Point(1, 1), new Point(1, 2) }));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(4)
    @DisplayName("[Find] Test valid paths on free field.")
    @MethodSource("generate_validPointsForFree")
    void test_validPointsOnFreeBoard(Point start, Point end, Point[] expected) {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD_FREE);
        Path path = find.findPath(start, end, true);
        Assertions.assertEquals(expected.length, path.size(), "Should have expected, but: " + path);
        Assertions.assertArrayEquals(expected, path.toArray(Point[]::new), "Should be legal for: " + path);
        Assertions.assertFalse(path.invalid(), "Should not be illegal => valid!");
        Assertions.assertTrue(find.connected(start, end, true), "Should be connected, for: " + start + " and " + end);
    }

    @Test
    @Tag("Util")
    @Order(4)
    @DisplayName("[Find] Test valid paths on free field not walking diagonal.")
    void test_validPointsOnFreeBoardSquare() {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD_FREE, false);
        Path path = find.findPath(new Point(0, 3), new Point(3, 0), true);

        final Point[] expected = new Point[] { new Point(0, 3), new Point(0, 2), new Point(1, 2), new Point(1, 1),
                new Point(1, 0), new Point(2, 0), new Point(3, 0) };

        Assertions.assertEquals(expected.length, path.size(), "Should have expected, but: " + path);
        Assertions.assertArrayEquals(expected, path.toArray(Point[]::new), "Should be legal for: " + path);
        Assertions.assertFalse(path.invalid(), "Should not be illegal => valid!");
        Assertions.assertTrue(find.connected(new Point(0, 3), new Point(3, 0), true),
                "Should be connected, for points");
    }

    @Test
    @Tag("Util")
    @Order(5)
    @DisplayName("[Find] Test paths on the nicest board ever (out of bounds check)")
    void test_checkEdgeCasesOnMean() {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD_MEAN);
        Path path = find.findPath(new Point(0, 3), new Point(3, 0), true);
        Assertions.assertTrue(path.invalid(), "Should be invalid, but: " + path);

        path = find.findPath(new Point(0, 1), new Point(1, 1), true);
        Assertions.assertFalse(path.invalid(), "Should be valid, but: " + path);

        path = find.findPath(new Point(0, 1), new Point(1, 2), true);
        Assertions.assertFalse(path.invalid(), "Should be valid, but: " + path);

        for (int i = 0; i < 3; i++) {
            path = find.findPath(new Point(i, 1), new Point(1, 4), true);
            Assertions.assertTrue(path.invalid(), "Should be invalid, but: " + path);
        }

        path = find.findPath(new Point(3, 2), new Point(0, 2), true);
        Assertions.assertTrue(path.invalid(), "Should be invalid, but: " + path);
    }

    @Test
    @Tag("Util")
    @Order(5)
    @DisplayName("[Find] Test paths connected if start and or end are invalid")
    void test_startEndInvalid() {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD);
        Path path = find.findPath(new Point(6, 2), new Point(2, 15), false);

        final Point[] expected = new Point[] { new Point(6, 2), new Point(6, 3), new Point(6, 4), new Point(5, 5),
                new Point(6, 6), new Point(5, 7), new Point(4, 8), new Point(3, 9), new Point(2, 10), new Point(2, 11),
                new Point(1, 12), new Point(2, 13), new Point(2, 14), new Point(2, 15) };

        Assertions.assertEquals(expected.length, path.size(), "Should have expected, but: " + path);
        Assertions.assertArrayEquals(expected, path.toArray(Point[]::new), "Should be legal for: " + path);
        Assertions.assertFalse(path.invalid(), "Should not be illegal => valid!");
        Assertions.assertTrue(find.connected(new Point(6, 2), new Point(2, 15), false),
                "Should be connected, for points, but: " + path);

        Assertions.assertTrue(find.connected(new Point(5, 21), new Point(4, 3), false),
                "Should be connected, for points");

        Assertions.assertTrue(find.connected(new Point(6, 2), new Point(2, 15), false),
                "Should be connected, for points");

        Assertions.assertTrue(find.connected(new Point(1, 3), new Point(4, 3), false),
                "Should be connected, for points");

        Assertions.assertFalse(find.connected(new Point(2, 20), new Point(3, 16), false),
                "Should not be connected for last points");

    }

    public static Stream<Arguments> generate_flyPaths() {
        return Stream.of(Arguments.arguments(new Point(1, 1), new Point(1, 21), 21),
                Arguments.arguments(new Point(2, 1), new Point(6, 20), 20),
                Arguments.arguments(new Point(3, 18), new Point(5, 16), 3));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(6)
    @DisplayName("[Find] Test paths when flying")
    @MethodSource("generate_flyPaths")
    void test_pathFindWithFly(Point start, Point end, int length) {
        Pathfinder<FieldStateEnum> find = new Pathfinder<>(TRAINING_BOARD, true, true);
        Path path = find.findPath(start, end, false);
        Assertions.assertEquals(length, path.size(), "Should have expected, but: " + path);
    }
}