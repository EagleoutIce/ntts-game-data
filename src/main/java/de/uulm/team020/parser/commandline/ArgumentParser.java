package de.uulm.team020.parser.commandline;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.uulm.team020.GameData;
import de.uulm.team020.logging.Magpie;

/**
 * Convenient Wrapper around the 'common-cli' by apache, it offers some
 * workarounds to achieve the standardized behaviour.
 * <p>
 * As right now the wrapper doesn't do much, there are no tests required as it
 * relies logically on common-cli.
 * 
 * @author Florian Sihler
 * @version 1.0b, 03/16/2020
 */
public class ArgumentParser {

    /** List of options available for the Parser */
    private Options options = new Options();
    /** Default parser which will scan the Arguments */
    private CommandLineParser parser = new DefaultParser();

    /** Help-Formatter to be used, if help is wanted */
    private HelpFormatter formatter = null;

    /** Should the help-argument be allowed? */
    private boolean hasHelp;

    /** Name of the program running */
    private String prgname;

    /** Logging-Instance */
    private static Magpie magpie = Magpie.createMagpieSafe("GameData");

    /**
     * Construct a new Argument parser
     *
     * @param prgName name of the Program like 'ki020', ...
     * @param hasHelp should the '--help/-h' Option be generated?
     */
    public ArgumentParser(String prgName, boolean hasHelp) {
        if (hasHelp) {
            this.formatter = new HelpFormatter();
            this.formatter.setWidth(160);
            Option help = Option.builder("h").longOpt("help").desc("Prints this help").build();
            options.addOption(help);

        }
        this.hasHelp = hasHelp;
        this.prgname = prgName;
    }

    /**
     * Simple Getter
     * 
     * @return the available options
     */
    public Options getOptions() {
        return this.options;
    }

    /**
     * Places the '--x "key" "value"' option, as standardized
     * 
     * @return {@link #options}
     */
    public Options hasX() {
        Option option = Option.builder().longOpt("x").numberOfArgs(2).hasArgs().required(false).valueSeparator(' ')
                .argName("key> <value").desc("Additional Key-Value-Pairs").build();
        magpie.writeInfo("Will use the 'x'-Option", "Create");
        return options.addOption(option);
    }

    /**
     * Just prints the Help-Menu
     */
    public void printHelp() {
        this.formatter.printHelp(this.prgname, "The following Arguments are available:\n\n", this.options,
                "\nRunning 'game-data' v." + GameData.VERSION / 1000.0f, true);
    }

    /**
     * Parse the supplied Arguments
     * 
     * @param arguments the arguments to be parsed
     * @return the Line-Reference to query
     * 
     * @throws ParseException see {@link DefaultParser#parse(Options, String[])}
     */
    public CommandLine parse(String[] arguments) throws ParseException {
        magpie.writeInfo("Parsing: " + Arrays.toString(arguments), "Parser");
        try {
            CommandLine cli = this.parser.parse(this.options, arguments);
            if (hasHelp && cli.hasOption("help"))
                printHelp();
            return cli;
        } catch (MissingArgumentException | MissingOptionException ex) {
            System.err.println(ex.getMessage() + "\n");
            printHelp();
        }
        return null;
    }

}