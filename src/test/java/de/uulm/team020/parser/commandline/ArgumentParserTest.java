package de.uulm.team020.parser.commandline;

import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Validates the functionality of the
 * {@link ArgumentParser}.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class ArgumentParserTest {

    private static Stream<Arguments> simple_line_generator() {
        return Stream.of(
            Arguments.arguments("", false, false),  
            Arguments.arguments("-w", false, true),  
            Arguments.arguments("--waffel", false, true),  
            Arguments.arguments("-w --waffel", false, true),  
            Arguments.arguments("-t", true, false),
            Arguments.arguments("--test", true, false),
            Arguments.arguments("-t --test", true, false),
            Arguments.arguments("-t -w", true, true),
            Arguments.arguments("-w -t", true, true),
            Arguments.arguments("--waffel -t", true, true),
            Arguments.arguments("--waffel --test", true, true),
            Arguments.arguments("--test --waffel", true, true)
        );
    }

    @ParameterizedTest @Tag("Util") @Order(1)
    @DisplayName("[Parser] Simple Commandline.")
    @MethodSource("simple_line_generator")

    public void test_simple_cmdline(String line, boolean presentT, boolean presentW) throws Exception {  
        ArgumentParser parser = new ArgumentParser("test", false);
        parser.getOptions().addOption("t", "test", false, "testflag")
                           .addOption("w", "waffel", false, "test-waffle (tasty)");

        CommandLine cli = parser.parse(line.split("\\s+"));

        Assertions.assertNotNull(cli, "Should never be null with test data.");

        Assertions.assertEquals(presentT, cli.hasOption("t"), "'t' should be " + (presentT?"set":"not set") + " in: '" + line + "'");
        Assertions.assertEquals(presentW, cli.hasOption("w"), "'w' should be " + (presentW?"set":"not set") + " in: '" + line + "'");
    }
    
    private static Stream<Arguments> complex_line_generator() {
        return Stream.of(
            Arguments.arguments("", null, null, false),  
            Arguments.arguments("-w", null, null, true),  
            Arguments.arguments("--waffel", null, null, true),  
            Arguments.arguments("-w --waffel", null, null, true),  
            Arguments.arguments("-t Huhu", "Huhu", null, false),
            Arguments.arguments("--test yeahXtwea", "yeahXtwea", null, false),
            Arguments.arguments("-t a --test b", "a", null,false),
            Arguments.arguments("-t ; -w", ";", null, true),
            Arguments.arguments("-w -t dietaa", "dietaa", null, true),
            Arguments.arguments("--waffel -t x", "x", null, true),
            Arguments.arguments("--waffel --test 42", "42", null, true),
            Arguments.arguments("--test hu? --waffel", "hu?", null, true),
            Arguments.arguments("--test hu? -x KEY VALUE", "hu?", new String[] {"KEY", "VALUE"}, false),
            Arguments.arguments("-x Alabama Betabama --test hu? -x meta drama -w", "hu?", new String[] {"Alabama", "Betabama", "meta", "drama"}, true)
        );
    }

    @ParameterizedTest @Tag("Util") @Order(2)
    @DisplayName("[Parser] Complex Commandline.")
    @MethodSource("complex_line_generator")
    public void test_complex_cmdline(String line, String valueT, String[] valuesX, boolean presentW) throws Exception {  
        ArgumentParser parser = new ArgumentParser("test", true);
        parser.hasX().addOption("t", "test", true, "testflag")
                           .addOption("w", "waffel", false, "test-waffle (tasty)");

        CommandLine cli = parser.parse(line.split("\\s+"));

        Assertions.assertNotNull(cli, "Should never be null with test data.");

        Assertions.assertEquals(valueT, cli.getOptionValue("t"), "'t' should have the expected Value with '" + line + "'");
        Assertions.assertEquals(presentW, cli.hasOption("w"), "'w' should be " + (presentW?"set":"not set") + " in: '" + line + "'");
        Assertions.assertArrayEquals(valuesX, cli.getOptionValues("x"), "'x' should have the expected Values with '" + line + "'");
        
    }
}