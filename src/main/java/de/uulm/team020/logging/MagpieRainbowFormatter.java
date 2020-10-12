package de.uulm.team020.logging;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import de.uulm.team020.helper.RandomHelper;

/**
 * Formatter for {@link Magpie}. It will define the style of the logfiles when
 * printed to the console.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/02/2020
 */
public class MagpieRainbowFormatter extends MagpieFormatter {

    // Depends on the ansi code vt100
    /**
     * Reset Sequence on vt100 Terminals
     */
    public static final String FORMAT_RESET = "\033[0m";
    public static final String FORMAT_SEVERE = "\033[1m";

    /**
     * All colors to be chosen in a random rotation
     */
    public static final List<String> ALL_COLORS = List.of(
            // default colors:
            "\033[38;32m", "\033[38;33m", "\033[38;34m", "\033[38;35m", "\033[38;36m", 
            "\033[38;37m", //
            // custom colors:
            "\033[38;2;98;139;72m", // RGB(98, 139, 72)
            "\033[38;2;92;149;255m", // RGB(92, 149, 255)
            "\033[38;2;246;174;45m", // RGB(246, 174, 45)
            "\033[38;2;175;0;117m", // RGB(175, 0, 117)
            "\033[38;2;216;130;157m", // RGB(216, 130, 157)
            "\033[38;2;173;173;39m", // RGB(173,173,39)
            "\033[38;2;73;46;225m", // RGB(73,46,225)
            "\033[38;2;211;56;211m", // RGB(211,56,211)
            "\033[38;2;51;187;200m", // RGB(51,187,200)
            "\033[38;2;18;38;58m", // RGB(18, 38, 58)
            "\033[38;2;106;181;71m", // RGB(106, 181, 71)
            "\033[38;2;115;130;144m", // RGB(115, 130, 144)
            "\033[38;2;65;34;52m", // RGB(65, 34, 52)
            "\033[38;2;105;162;151m", // RGB(105, 162, 151)
            "\033[38;2;80;36;25m", // RGB(80, 36, 25)
            "\033[38;2;110;37;148m" // RGB(110, 37, 148)
    );
    /**
     * Colors left in the rotation. If empty, there will be chosen one at random.
     */
    protected static final List<String> colorsLeft = new LinkedList<>(ALL_COLORS);

    // chosen by this one
    private String formatColor;
    private String name;
    private String path;

    /**
     * Construct a new Magpie-Formatter. Probably you do not need to do this but you
     * may if you wish to use this one otherwise.
     * 
     * @param name The name to be used for this formatter. It will be added to the
     *             output of a statement.
     * 
     * @param path The path the file this one is writing to is located it may be
     *             shown as well so a user might find the underlying file with ease.
     */
    public MagpieRainbowFormatter(String name, String path) {
        super();
        synchronized (colorsLeft) {
            // Generate random color
            if (!colorsLeft.isEmpty()) {
                formatColor = colorsLeft.get(0); // deterministic
                colorsLeft.remove(formatColor);
            } else {
                formatColor = RandomHelper.rndPick(ALL_COLORS);
            }
        }
        this.name = name;
        this.path = path;
    }

    /**
     * The Format
     * 
     * @return The formatColor
     */
    public String getFormatColor() {
        return formatColor;
    }

    /**
     * Formats a log-entry for writing
     * 
     * @param record the logging-record to typeset
     * @return the formatted String
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(BUFFER_SIZE);
        builder.append(">    ").append(formatDate.format(new Date(record.getMillis()))).append(" ");
        builder.append("(").append(padAndCrop(record.getLevel().toString(), MAX_LEVEL_LENGTH)).append(") -- ")
                .append(String.format("%-17s", this.name)).append(" [").append(this.path).append("]\n");
        builder.append(formatColor);
        if (record.getLevel().equals(Level.SEVERE)) {
            builder.append(FORMAT_SEVERE);
        }
        builder.append(formatMessage(record)).append(FORMAT_RESET);
        builder.append("\n");
        // System.out.println("format: " + record + ": " + builder.toString())
        return builder.toString();
    }

}