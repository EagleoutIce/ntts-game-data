package de.uulm.team020.datatypes.util;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.validation.GameDataGson;

/**
 * Testing the implementation of {@link Point}.
 *
 * @author Florian Sihler
 * @version 1.0, 03/29/2020
 */
class PointTest {

    @Test
    @Tag("Util")
    @Order(1)
    @DisplayName("[Point] Test Equals.")
    void test_equals() {
        Point a = new Point(), b = new Point();
        Assertions.assertEquals(a, b, "Points should be the same");
        a = new Point(3, 4);
        Assertions.assertNotEquals(a, b, "Points should not be the same");
        b = new Point(3, 4);
        Assertions.assertEquals(a, b, "Points should be the same");
        a = b;
        Assertions.assertEquals(a, b, "Points are identical");
        a = new Point(-1, -4);
        Assertions.assertNotEquals(a, b, "Points should not be the same");
        b = new Point(-1, -4);
        Assertions.assertEquals(a, b, "Points should be the same");
    }

    private static Stream<Arguments> generate_testPoints() {
        return Stream.of(Arguments.arguments(new Point(), 0, 0), Arguments.arguments(new Point(-12, 12), -12, 12),
                Arguments.arguments(new Point(new Point(1, -2)), 1, -2));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(2)
    @DisplayName("[Point] Test Getter.")
    @MethodSource("generate_testPoints")
    void test_getters(Point point, int expectedX, int expectedY) {
        Assertions.assertEquals(expectedX, point.getX(), "x-coordinate should be as stated");
        Assertions.assertEquals(expectedY, point.getY(), "y-coordinate should be as stated");
    }

    private static Stream<Arguments> generate_testSetters() {
        return Stream.of(Arguments.arguments(new Point(), 0, 0, new Point()),
                Arguments.arguments(new Point(), 1, 0, new Point(1, 0)),
                Arguments.arguments(new Point(), 2, -4, new Point(2, -4)),
                Arguments.arguments(new Point(4, 5), 3, -6, new Point(3, -6)),
                Arguments.arguments(new Point(new Point(-5, 12)), 1, -2, new Point(1, -2)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(3)
    @DisplayName("[Point] Test Move.")
    @MethodSource("generate_testSetters")
    void test_setters(Point base, int nx, int ny, Point expected) {
        // double set:
        Point target = new Point(base).set(nx, ny);
        Assertions.assertEquals(expected, target, "(1) Target point should be as calculated");
        // separate set:
        target = new Point(base).setX(nx).setY(ny);
        Assertions.assertEquals(expected, target, "(2) Target point should be as calculated");
        // swapped set:
        target = new Point(base).setY(ny).setX(nx);
        Assertions.assertEquals(expected, target, "(3) Target point should be as calculated");
    }

    private static Stream<Arguments> generate_testMove() {
        return Stream.of(Arguments.arguments(new Point(), 0, 0, new Point()),
                Arguments.arguments(new Point(), 1, 0, new Point(1, 0)),
                Arguments.arguments(new Point(), 2, -4, new Point(2, -4)),
                Arguments.arguments(new Point(4, 5), 3, -6, new Point(7, -1)),
                Arguments.arguments(new Point(new Point(-5, 12)), 1, -2, new Point(-4, 10)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(4)
    @DisplayName("[Point] Test Move.")
    @MethodSource("generate_testMove")
    void test_movers(Point base, int dx, int dy, Point expected) {
        // double move:
        Point target = new Point(base).move(dx, dy);
        Assertions.assertEquals(expected, target, "(1) Target point should be as calculated");
        // separate move:
        target = new Point(base).moveX(dx).moveY(dy);
        Assertions.assertEquals(expected, target, "(2) Target point should be as calculated");
        // swapped move:
        target = new Point(base).moveY(dy).moveX(dx);
        Assertions.assertEquals(expected, target, "(3) Target point should be as calculated");
        // static move:
        target = new Point(base);
        Assertions.assertEquals(expected, Point.move(target, new Point(dx, dy)),
                "(4.1) Target point should be as calculated");
        Assertions.assertEquals(expected, target, "(4.2) Target point should be as calculated");
        // static move with deltas:
        target = new Point(base);
        Assertions.assertEquals(expected, Point.move(target, dx, dy), "(5.1) Target point should be as calculated");
        Assertions.assertEquals(expected, target, "(5.2) Target point should be as calculated");
    }

    private static Stream<Arguments> generate_testAbs() {
        return Stream.of(Arguments.arguments(new Point(), 0, 0), Arguments.arguments(new Point(42, 0), 42, 0),
                Arguments.arguments(new Point(2, -4), 4.4721359, 0.0001),
                Arguments.arguments(new Point(4, 5), 6.4031242, 0.0001),
                Arguments.arguments(new Point(new Point(-5, 12)), 13, 0));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(5)
    @DisplayName("[Point] Test Move.")
    @MethodSource("generate_testAbs")
    void test_abs(Point base, double expectedAbs, double delta) {
        // simple call
        Assertions.assertEquals(expectedAbs, base.abs(), delta, "(1) Absolute value should be as calculated");
        // static call
        Assertions.assertEquals(expectedAbs, Point.abs(base), delta, "(2) Absolute value should be as calculated");
    }

    private static Stream<Arguments> generate_testDistance() {
        return Stream.of(Arguments.arguments(new Point(42, 42), new Point(42, 42), 0, 0),
                Arguments.arguments(new Point(42, 0), new Point(), 42, 0),
                Arguments.arguments(new Point(2, -4), new Point(1, 2), 6.0827625, 0.0001),
                Arguments.arguments(new Point(1, 1), new Point(-1, -1), 2.8284271, 0.0001),
                Arguments.arguments(new Point(new Point(-1, 0)), new Point(3, 3), 5, 0));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(6)
    @DisplayName("[Point] Test Distance.")
    @MethodSource("generate_testDistance")
    void test_distance(Point pointA, Point pointB, double expectedDist, double delta) {
        // simple call
        Assertions.assertEquals(expectedDist, pointA.getDistance(pointB), delta,
                "(1.1) The distance should be as calculated");
        Assertions.assertEquals(expectedDist, pointB.getDistance(pointA), delta,
                "(1.2) The distance should be as calculated");
        // static call
        Assertions.assertEquals(expectedDist, Point.getDistance(pointA, pointB), delta,
                "(2) The distance should be as calculated");
        Assertions.assertEquals(expectedDist, Point.getDistance(pointB, pointA), delta,
                "(2) The distance should be as calculated");

    }

    private static Stream<Arguments> generate_testNeighbours() {
        return Stream.of(Arguments.arguments(new Point(),
                new Point[] { new Point(-1, -1), new Point(0, -1), new Point(1, -1), new Point(-1, 0), new Point(0, 0),
                        new Point(1, 0), new Point(-1, 1), new Point(0, 1), new Point(1, 1), },
                new Point[] { new Point(-2, -1), new Point(0, -2), new Point(-4, -1), new Point(12, 0), new Point(0, 2),
                        new Point(3, 0), new Point(42, 1), new Point(5, 7), new Point(14, -9), new Point(2, 2), }),
                Arguments.arguments(new Point(5, 12),
                        new Point[] { new Point(5 - 1, 12 - 1), new Point(5 + 0, 12 - 1), new Point(5 + 1, 12 - 1),
                                new Point(5 - 1, 12 + 0), new Point(5 + 0, 12 + 0), new Point(5 + 1, 12 + 0),
                                new Point(5 - 1, 12 + 1), new Point(5 + 0, 12 + 1), new Point(5 + 1, 12 + 1), },
                        new Point[] { new Point(-2, -1), new Point(0, -2), new Point(-4, -1), new Point(12, 0),
                                new Point(0, 2), new Point(3, 0), new Point(42, 1), new Point(5, 7), new Point(14, 9),
                                new Point(2, 2), })

        );
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(7)
    @DisplayName("[Point] Test Neighbours.")
    @MethodSource("generate_testNeighbours")
    void test_neighbours(Point pointA, Point[] neighbours, Point[] noNeighbours) {
        for (Point n : neighbours) {
            // simple call
            Assertions.assertTrue(pointA.isNeighbour(n), pointA + "  and " + n + " have to be neighbours");
            // static call
            Assertions.assertTrue(Point.isNeighbour(pointA, n), pointA + "  and " + n + " have to be neighbours");
        }
        for (Point n : noNeighbours) {
            // simple call
            Assertions.assertFalse(pointA.isNeighbour(n),
                    pointA + "  and " + n + " should not be neighbours neighbours");
            // static call
            Assertions.assertFalse(Point.isNeighbour(pointA, n),
                    pointA + "  and " + n + " should not be neighbours neighbours");
        }
    }

    private static Stream<Arguments> generate_testInBounds() {
        return Stream.of(Arguments.arguments(new Point(0, 0), new Point(0, 0), new Point(0, 0), true),
                Arguments.arguments(new Point(0, 0), new Point(1, 0), new Point(0, 0), false),
                Arguments.arguments(new Point(0, 0), new Point(-1, -1), new Point(1, 1), true),
                Arguments.arguments(new Point(2, 1), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(-2, 1), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, 1), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, -1), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, 3), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, 0), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, -2), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(0, 2), new Point(-1, -1), new Point(1, 1), false),
                Arguments.arguments(new Point(2, 1), new Point(-1, -1), new Point(4, 1), true),
                Arguments.arguments(new Point(2, -1), new Point(-1, -2), new Point(6, 1), true),
                Arguments.arguments(new Point(2, 0), new Point(-2, -1), new Point(2, 1), true),
                Arguments.arguments(new Point(1, 0), new Point(-2, -3), new Point(1, 1), true),
                Arguments.arguments(new Point(2, -2), new Point(-1, -2), new Point(2, 1), true),
                Arguments.arguments(new Point(0, 2), new Point(-1, -1), new Point(42, 42), true));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(8)
    @DisplayName("[Point] Test inBounds.")
    @MethodSource("generate_testInBounds")
    void test_isinbounds(Point pointA, Point lowerLeft, Point upperRight, boolean inBounds) {
        // simple call
        Assertions.assertEquals(inBounds, pointA.isInBounds(lowerLeft, upperRight),
                pointA + " " + (inBounds ? "should" : "should not") + " be inside the rectangle of " + lowerLeft
                        + " and " + upperRight);
    }

    private static Stream<Arguments> generate_lineCalculation() {
        return Stream.of(//
                Arguments.arguments(new Point(0, 0), new Point(0, 0), Path.of(0, 0)),
                Arguments.arguments(new Point(0, 0), new Point(1, 0), Path.of(0, 0, 1, 0)),
                Arguments.arguments(new Point(0, 0), new Point(2, 0), Path.of(0, 0, 1, 0, 2, 0)),
                Arguments.arguments(new Point(0, 0), new Point(3, 0), Path.of(0, 0, 1, 0, 2, 0, 3, 0)),
                Arguments.arguments(new Point(2, 0), new Point(4, 0), Path.of(2, 0, 3, 0, 4, 0)),
                Arguments.arguments(new Point(7, 0), new Point(5, 0), Path.of(7, 0, 6, 0, 5, 0)),
                Arguments.arguments(new Point(0, 1), new Point(0, 0), Path.of(0, 1, 0, 0)),
                Arguments.arguments(new Point(0, 2), new Point(0, 0), Path.of(0, 2, 0, 1, 0, 0)),
                Arguments.arguments(new Point(0, 3), new Point(0, 0), Path.of(0, 3, 0, 2, 0, 1, 0, 0)),
                Arguments.arguments(new Point(0, 4), new Point(0, 5), Path.of(0, 4, 0, 5)),
                Arguments.arguments(new Point(0, 5), new Point(0, 7), Path.of(0, 5, 0, 6, 0, 7)),
                Arguments.arguments(new Point(0, 0), new Point(2, 2), Path.of(0, 0, 1, 1, 2, 2)),
                Arguments.arguments(new Point(0, 0), new Point(-2, 2), Path.of(0, 0, -1, 1, -2, 2)),
                Arguments.arguments(new Point(0, 0), new Point(2, -2), Path.of(0, 0, 1, -1, 2, -2)),
                Arguments.arguments(new Point(0, 0), new Point(-2, -2), Path.of(0, 0, -1, -1, -2, -2)),
                Arguments.arguments(new Point(1, 1), new Point(2, 2), Path.of(1, 1, 2, 2)),
                Arguments.arguments(new Point(1, 1), new Point(-2, 2), Path.of(1, 1, 0, 1, -1, 2, -2, 2)),
                Arguments.arguments(new Point(1, 1), new Point(2, -2), Path.of(1, 1, 1, 0, 2, -1, 2, -2)),
                Arguments.arguments(new Point(1, 1), new Point(-2, -2), Path.of(1, 1, 0, 0, -1, -1, -2, -2)),
                Arguments.arguments(new Point(1, 2), new Point(2, 2), Path.of(1, 2, 2, 2)),
                Arguments.arguments(new Point(0, 0), new Point(3, 6),
                        Path.of(0, 0, 0, 1, 1, 1, 1, 2, 1, 3, 2, 3, 2, 4, 2, 5, 3, 5, 3, 6)),
                Arguments.arguments(new Point(0, 0), new Point(-3, 6),
                        Path.of(0, 0, 0, 1, -1, 1, -1, 2, -1, 3, -2, 3, -2, 4, -2, 5, -3, 5, -3, 6)),
                Arguments.arguments(new Point(0, 0), new Point(3, -6),
                        Path.of(0, 0, 0, -1, 1, -1, 1, -2, 1, -3, 2, -3, 2, -4, 2, -5, 3, -5, 3, -6)),
                Arguments.arguments(new Point(0, 0), new Point(-3, -6),
                        Path.of(0, 0, 0, -1, -1, -1, -1, -2, -1, -3, -2, -3, -2, -4, -2, -5, -3, -5, -3, -6)),
                Arguments.arguments(new Point(0, 0), new Point(3, 3), Path.of(0, 0, 1, 1, 2, 2, 3, 3)),
                Arguments.arguments(new Point(-1, -1), new Point(3, 6),
                        Path.of(-1, -1, -1, 0, 0, 0, 0, 1, 0, 2, 1, 2, 1, 3, 2, 3, 2, 4, 2, 5, 3, 5, 3, 6)),
                Arguments.arguments(new Point(1, 1), new Point(4, 1), Path.of(1, 1, 2, 1, 3, 1, 4, 1)),
                Arguments.arguments(new Point(1, 1), new Point(-2, 1), Path.of(1, 1, 0, 1, -1, 1, -2, 1)),
                Arguments.arguments(new Point(1, 1), new Point(1, 4), Path.of(1, 1, 1, 2, 1, 3, 1, 4)),
                Arguments.arguments(new Point(1, 1), new Point(1, -2), Path.of(1, 1, 1, 0, 1, -1, 1, -2)),
                // line diag line
                Arguments.arguments(new Point(1, 1), new Point(4, 2), Path.of(1, 1, 2, 1, 3, 2, 4, 2)),
                Arguments.arguments(new Point(1, 1), new Point(4, 0), Path.of(1, 1, 2, 1, 3, 0, 4, 0)),
                Arguments.arguments(new Point(1, 1), new Point(-2, 2), Path.of(1, 1, 0, 1, -1, 2, -2, 2)),
                Arguments.arguments(new Point(1, 1), new Point(-2, 0), Path.of(1, 1, 0, 1, -1, 0, -2, 0)),
                // down-wards
                Arguments.arguments(new Point(1, 1), new Point(2, 4), Path.of(1, 1, 1, 2, 2, 3, 2, 4)),
                Arguments.arguments(new Point(1, 1), new Point(0, 4), Path.of(1, 1, 1, 2, 0, 3, 0, 4)),
                Arguments.arguments(new Point(1, 1), new Point(2, -2), Path.of(1, 1, 1, 0, 2, -1, 2, -2)),
                Arguments.arguments(new Point(1, 1), new Point(0, -2), Path.of(1, 1, 1, 0, 0, -1, 0, -2)),
                Arguments.arguments(new Point(1, 1), new Point(-1, 5),
                        Path.of(1, 1, 1, 2, 0, 2, 0, 3, 0, 4, -1, 4, -1, 5)),
                Arguments.arguments(new Point(-1, 5), new Point(1, 1),
                        Path.reversed(Path.of(1, 1, 1, 2, 0, 2, 0, 3, 0, 4, -1, 4, -1, 5))),
                // specials
                Arguments.arguments(new Point(3, 0), new Point(6, 1), Path.of(3, 0, 4, 0, 5, 1, 6, 1)),
                Arguments.arguments(new Point(3, 0), new Point(6, -1), Path.of(3, 0, 4, 0, 5, -1, 6, -1)),
                Arguments.arguments(new Point(3, 0), new Point(0, 1), Path.of(3, 0, 2, 0, 1, 1, 0, 1)),
                Arguments.arguments(new Point(3, 0), new Point(0, -1), Path.of(3, 0, 2, 0, 1, -1, 0, -1)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(9)
    @DisplayName("[Point] Test line-calculation.")
    @MethodSource("generate_lineCalculation")
    void test_lineCalculation(Point a, Point b, Path expected) {
        Assertions.assertEquals(expected, Point.getLine(a, b), "Path should be as wanted for: " + a + " and " + b);
    }

    private static Stream<Arguments> generate_lineOfSight() {
        return Stream.of(Arguments.arguments(new Point(0, 0), new Point(0, 0), true, false),
                Arguments.arguments(new Point(0, 0), new Point(0, 0), false, true),
                Arguments.arguments(new Point(2, 2), new Point(6, 2), true, false),
                Arguments.arguments(new Point(2, 2), new Point(5, 2), true, true),
                Arguments.arguments(new Point(2, 2), new Point(6, 2), false, true),
                Arguments.arguments(new Point(5, 3), new Point(5, 6), true, false), // pass null
                Arguments.arguments(new Point(5, 3), new Point(5, 6), false, false),
                Arguments.arguments(new Point(4, 7), new Point(1, 4), true, true),
                Arguments.arguments(new Point(4, 7), new Point(1, 4), false, true),
                Arguments.arguments(new Point(4, 2), new Point(5, 1), true, true),
                Arguments.arguments(new Point(2, 3), new Point(1, 2), true, true),
                Arguments.arguments(new Point(2, 3), new Point(3, 2), true, true),
                Arguments.arguments(new Point(2, 3), new Point(4, 2), true, true),
                Arguments.arguments(new Point(2, 3), new Point(0, 2), false, true),
                Arguments.arguments(new Point(5, 7), new Point(3, 6), false, false),
                Arguments.arguments(new Point(3, 1), new Point(1, 2), false, false));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(10)
    @DisplayName("[Point] Test line-of-sight.")
    @MethodSource("generate_lineOfSight")
    void test_lineOfSight(Point a, Point b, boolean checkstartend, boolean inLos) throws IOException {
        String json = GameDataGson.loadInternalJson("json/files/scenario/edge.scenario");

        // Create an example scenario
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);
        Path path = Point.getLine(a, b);
        Assertions.assertEquals(inLos, path.isLineOfSight(scenario, checkstartend),
                "Should be as stated for LOS on: " + path + " (checkSE: " + checkstartend + ")");
    }

    private static Stream<Arguments> generate_fromStringValid() {
        return Stream.of(
                // Legacy
                Arguments.arguments("0/0", null, new Point(0, 0)), Arguments.arguments("1/2", null, new Point(1, 2)),
                Arguments.arguments("3/0", null, new Point(3, 0)), Arguments.arguments("0/-4", null, new Point(0, -4)),
                // braced
                Arguments.arguments("(-1,-1)", null, new Point(-1, -1)),
                Arguments.arguments("(0,0)", null, new Point(0, 0)),
                Arguments.arguments("(0, 12)", null, new Point(0, 12)),
                Arguments.arguments("(15, 36)", null, new Point(15, 36)),
                Arguments.arguments("(-4,42)", null, new Point(-4, 42)),
                Arguments.arguments("(-0,     9)", null, new Point(0, 9)),
                Arguments.arguments("(-0,    -9)", null, new Point(0, -9)),
                // angled
                Arguments.arguments("<-1,-1>", null, new Point(-1, -1)),
                Arguments.arguments("<0,0>", null, new Point(0, 0)),
                Arguments.arguments("<0, 12>", null, new Point(0, 12)),
                Arguments.arguments("<15, 36>", null, new Point(15, 36)),
                Arguments.arguments("<-4,42>", null, new Point(-4, 42)),
                Arguments.arguments("<-0,     9>", null, new Point(0, 9)),
                Arguments.arguments("<-0,    -9>", null, new Point(0, -9)),
                // with shift (no legacy)
                Arguments.arguments("(-1,-1)", new Point(0, 0), new Point(-1, -1)),
                Arguments.arguments("+(-1,-1)", new Point(0, 0), new Point(-1, -1)),
                Arguments.arguments("-(-1,-1)", new Point(0, 0), new Point(1, 1)),
                Arguments.arguments("(2, 3)", new Point(-1, -3), new Point(2, 3)),
                Arguments.arguments("+(2, 3)", new Point(-1, -3), new Point(1, 0)),
                Arguments.arguments("-<2, 3>", new Point(-1, -3), new Point(-3, -6)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(11)
    @DisplayName("[Point] Test fromString-Cast.")
    @MethodSource("generate_fromStringValid")
    void test_fromStringValid(String data, Point shift, Point expected) throws IOException, PointParseException {
        final Point got = Point.fromString(data, shift);
        Assertions.assertEquals(expected, got, "Point should be as wanted for shift: " + shift);
    }

    private static Stream<Arguments> generate_fromStringInvalid() {
        return Stream.of(Arguments.arguments("", null), Arguments.arguments("<", null), Arguments.arguments("+", null),
                Arguments.arguments("(", null), Arguments.arguments("[", null), Arguments.arguments("[1,2]", null),
                Arguments.arguments("(1,", null), Arguments.arguments("(1,3", null), Arguments.arguments("(1,3>", null),
                Arguments.arguments("<1,3)", null), Arguments.arguments("/", null), Arguments.arguments("1/", null),
                Arguments.arguments("+(1,3)", null), Arguments.arguments("-<1,3>", null),
                Arguments.arguments("+(1,3)", null), Arguments.arguments("-+<1,3>", new Point(0, 1)),
                Arguments.arguments("-(1,3>", new Point(0, 1)), Arguments.arguments("+-<1,3>", new Point(0, 1)),
                Arguments.arguments("+-(1,3)", new Point(0, 1)), Arguments.arguments("-+1/3", new Point(0, 1)),
                Arguments.arguments("--(1,3)", new Point(0, 1)));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(12)
    @DisplayName("[Point] Test fromString-Cast for fail.")
    @MethodSource("generate_fromStringInvalid")
    void test_fromStringInvalid(String data, Point shift) throws IOException, PointParseException {
        Assertions.assertThrows(PointParseException.class, () -> Point.fromString(data, shift));
    }
}