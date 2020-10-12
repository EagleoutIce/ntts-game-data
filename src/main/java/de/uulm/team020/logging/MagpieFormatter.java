package de.uulm.team020.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import de.uulm.team020.helper.NumericHelper;

/**
 * Formatter for {@link Magpie}. It will define the style of the logfiles
 * written by magpie.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/17/2020
 */
public class MagpieFormatter extends Formatter {

    /** Buffer size to be used in {@link #format(LogRecord)} */
    public static final short BUFFER_SIZE = 850;

    /** Maximum length of record-level in {@link #format(LogRecord)} */
    public static final short MAX_LEVEL_LENGTH = 9;

    /**
     * Maximum length for package-identifiers. Must be greater than or equal to
     * three
     */
    public static final short MAX_PACKAGE_LENGTH = 35;

    /**
     * Sender length, please note that this will not be cropped, it's just for
     * design-purposes
     */
    public static final short SENDER_LENGTH = 14;

    /**
     * Format to be used by Magpie on write, will use normal format
     */
    public static final String DEFAULT_OUT_FORMAT = "%-" + MAX_PACKAGE_LENGTH + "s %s  %-" + SENDER_LENGTH + "s: %s";

    /** The Date-Format to be used in the log */
    public final DateFormat formatDate = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss.SSS");

    /** The Date-Format to be used for the temporary Folder */
    public final DateFormat folderDate = new SimpleDateFormat("yyyy-MM-dd--hh-mm-ss");

    /**
     * Pads a String to the left whilst cropping it in case it exceeds the maximum
     * allowed size.
     * <p>
     * <i>Important:</i> in case cropping is necessary it will add three dots to the
     * string. To ensure the maximum allowed Size, it will replace the last three
     * characters with dots to signal the cropping. Therefore the size has to be at
     * least three.
     * 
     * @param string the string to pad and crop
     * @param size   the maximum size, has to be at least 3
     * 
     * @return processed String
     */
    public static String padAndCrop(String string, int size) {
        if (string.length() < size) {// Fits into default padding
            return String.format("%-" + size + "s", string);
        } else {
            return String.format("%-" + size + "s...",
                    string.substring(NumericHelper.getInBounds(string.length() - size + 3, 0, size)));
        }
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
        builder.append(formatDate.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(padAndCrop(record.getLevel().toString(), MAX_LEVEL_LENGTH)).append("] ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

}