package de.uulm.team020.parser.expander;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestMethodOrder(OrderAnnotation.class)
class ExpanderTest {

    private static Expander expander;

    private static String someVariable = "Waffle";

    public static String fakeConstantExpander(final String key, final String option, final int level,
            final Expander expander) {
        return "Some epic constant.";
    }

    public static String someOptionExpander(final String key, final String option, final int level,
            final Expander expander) {
        switch (option) {
            case "Hallo":
            case "Wallo":
                return "Got: " + option;
        }
        return "Got not Hallo or Wallo. Was: " + option + "!";
    }

    public static String getOptExpander(final String key, final String option, final int level,
            final Expander expander) {
        return expander.expand(option);
    }

    @BeforeAll
    public static void setupExpander() {
        Expandables expandables = new Expandables();
        expandables.registerConstant("Test", "Test");
        expandables.registerConstant("", "Empty");
        expandables.registerConstant("T${Test}est", "Test"); // <- currently illegal
        expandables.registerConstant("tst", "${test}");
        expandables.registerExpansion("test", ExpanderTest::fakeConstantExpander);
        expandables.registerExpansion("lvl", Expandables::expandLevel);
        expandables.registerExpansion("date", Expandables::expandDate);
        expandables.registerExpansion("time", Expandables::expandTime);
        expandables.registerStaticClassReflection("expTest", ExpanderTest.class);
        expandables.registerExpansion("opt", ExpanderTest::getOptExpander);
        expandables.registerExpansion("someopt", ExpanderTest::someOptionExpander);
        expander = new Expander(expandables);
    }

    @Test
    @Tag("Util")
    @Order(1)
    @DisplayName("[Expander] Test no expansion.")
    void test_noExpansion() {
        String test = "There should be no expansion done here.";
        String got = expander.expand(test);
        Assertions.assertEquals("There should be no expansion done here.", test, "Should not change");
        Assertions.assertEquals(test, got, "Should not change, no expansion.");
    }

    private static Stream<Arguments> generate_simpleExpansion() {
        return Stream.of(Arguments.arguments("Test ${Test} ${lvl}", "Test Test 1"),
                Arguments.arguments("Test ${${Test}} ${lvl}", "Test Test 1"),
                Arguments.arguments("test$Test $lvl", "testTest 1"),
                Arguments.arguments("test\\$Test $lvl", "test$Test 1"),
                Arguments.arguments("test\\$Test \\{$lvl \\}", "test$Test {1 }"),
                Arguments.arguments("test\\$Test \\{$lvl\\}", "test$Test {"),
                Arguments.arguments("test\\$Test \\{${lvl}\\}", "test$Test {1}"),
                Arguments.arguments("test\\$Test \\{${opt:${lvl} X}\\}", "test$Test {2 X}"),
                Arguments.arguments("\"Jumping ${opt: from \\$lvl \\{to ${lvl}} \\$lvl nice! ${lvl}",
                        "\"Jumping  from $lvl {to 2 $lvl nice! 1"),
                Arguments.arguments("Wow${opt:${lvl}}Wow", "Wow2Wow"),
                Arguments.arguments("${opt:${opt:${opt:${lvl}}}}", "4"), Arguments.arguments(" ${} ", " Empty "),
                Arguments.arguments(
                        "${opt:Jumping from ${lvl} to ${opt:$lvl} then to lvl after that to $lvl at last to \\$lvl for \\\\$lvl} Sick $lvl man! This is $lvltastic, $lvl tastic and ${lvl}tastic. Nice \\$lvl Super \\${lvl} {} \\n$lvl",
                        "Jumping from 2 to 3 then to lvl after that to 2 at last to $lvl for \\2 Sick 1 man! This is  1 tastic and 1tastic. Nice $lvl Super ${lvl} {} \\n1"),
                Arguments.arguments(" ${IDon'tExist!}", " "), Arguments.arguments("ThisIs${T${Test}est}", "ThisIs"),
                Arguments.arguments("ThisIs${T${x}est}", "ThisIsTest"),
                // Should not expand => will be for ThisIs${Test} on first
                Arguments.arguments("\\$lvl $lv\\ l $lvl", "$lvl  1"),
                Arguments.arguments("\\$lvl $lv \n l $lvl", "$lvl  \n l 1"),
                Arguments.arguments(
                        "\\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}}",
                        "$lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl"),
                Arguments.arguments(
                        "\\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\$lvl ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} \\${lvl} \\$lv\\ l $lv\\ l\n \\n ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:${lvl} ${lvl}} ${lvl} ${opt:$lvl ${opt:$lvl \\$lvl}}} ${opt:$lvl ${opt:$lvl \\$lvl}}}",
                        "$lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl $lvl 2 3 3 2 3 4 $lvl ${lvl} $lv l  \\n 2 3 3 2 3 4 $lvl 2 3 3 2 3 4 4 3 4 5 $lvl 3 4 $lvl"));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(2)
    @DisplayName("[Expander] Test no expansion.")
    @MethodSource("generate_simpleExpansion")
    void test_simpleExpansion(String str, String exp) {
        String got = expander.expand(str);
        Assertions.assertEquals(exp, got, "Should be as expected.");
    }
    // Currently not supported:
    // Arguments.arguments("${opt:${opt:${opt:${Test}}}} ${opt:${opt:${lvl}}}",
    // "Test 3"),
}