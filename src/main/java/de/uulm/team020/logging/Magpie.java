package de.uulm.team020.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.uulm.team020.GameData;
import de.uulm.team020.datatypes.exceptions.MagpieException;
import de.uulm.team020.helper.NumericHelper;

/**
 * Offers the Logging-Interface for game-data and can be used by any other
 * including module.
 * <p>
 * This implementation wraps around the default java logger located at
 * {@link java.util.logging}.
 * 
 * @author Florian Sihler
 * @version 1.4b, 05/02/2020
 */
public class Magpie implements IMagpie {

    /**
     * The temporary directory this session will be recorded when using
     * {@link #constructFileHandler(String)}
     */
    private static final Path TARGET_DIR = constructTmpDir();

    /** Should logger-information be propagated to the console? */
    private static boolean mirrorToConsole = false;
    private boolean mirrorThisToConsole = false;
    private static boolean useRainbowForAll = false;
    private boolean useRainbow = false;

    /** Flag indicating if magpie is currently enabled */
    private static boolean globalLogEnabled = true;

    /** Default Logging-Level to be used if none supplied */
    private static Level loggerLevelDefault = Level.FINE;

    /** Flag indicating if this instance of magpie is currently enabled */
    private boolean logEnabled = true;

    /** The private logger-reference to use */
    private Logger logger;

    /** Absolute file-path to the logfile */
    private String logPath;

    /** Formatter to be used */
    private static MagpieFormatter formatter = new MagpieFormatter();

    /** Avoid unnecessary Recreations */
    private static ConcurrentMap<String, Magpie> hostedMagpies = new ConcurrentHashMap<>(4);

    private MagpieRainbowFormatter consoleFormatter;
    private Handler consoleHandler;
    private String className;

    /**
     * Full Constructor to deploy a new magpie-instance
     * <p>
     * Please Note that Magpie will enforce the Formatter to be
     * {@link MagpieFormatter}. If you want to change this behavior you can always
     * change the way that the Formatter-Variant operates.
     * <p>
     * This will always create a new Magpie, even if one for this handler does
     * already exist! If you do not like this behavior, use
     * {@link #createMagpieSafe(String)}.
     * 
     * @param className    name of the class that magpie should be deployed in
     * @param loggerLevel  the verbosity Level of the Logger
     * @param loggerTarget the garget file-handler that magpie should work on
     */
    protected Magpie(String className, Level loggerLevel, MagpieFileHandler loggerTarget) {
        if (className == null || className.isEmpty())
            throw new MagpieException("The name supplied to magpie should never be empty!");

        this.logger = Logger.getLogger(className);
        this.logger.setLevel(loggerLevel);
        this.logger.setUseParentHandlers(false);
        loggerTarget.setFormatter(formatter);
        this.logger.addHandler(loggerTarget);
        this.logPath = loggerTarget.getPattern();
        hostedMagpies.put(className, this);
        this.className = className;
        updateHandlers();
    }

    /**
     * Works as {@link #Magpie(String, Level, MagpieFileHandler)} but will construct
     * the File-Handler using {@link #constructFileHandler(String)} using the
     * temp-directory.
     * <p>
     * This will always create a new Magpie, even if one for this handler does
     * already exist! If you do not like this behavior, use
     * {@link #createMagpieSafe(String)}.
     * 
     * @param className   name of the hosting class
     * @param loggerLevel the verbosity the logger should operate on
     * 
     * @throws IOException if {@link #constructFileHandler(String)} fails, the error
     *                     will propagate
     */
    protected Magpie(String className, Level loggerLevel) throws IOException {
        this(className, loggerLevel, constructFileHandler(className));
    }

    /**
     * Works as {@link #Magpie(String, Level)} but will use
     * {@link #loggerLevelDefault} set by {@link #setDefaultLevel(Level)}.
     * <p>
     * This will always create a new Magpie, even if one for this handler does
     * already exist! If you do not like this behavior, use
     * {@link #createMagpieSafe(String)}.
     * 
     * @param className name of the hosting class
     * 
     * @throws IOException if {@link #constructFileHandler(String)} fails, the error
     *                     will propagate
     */
    protected Magpie(String className) throws IOException {
        this(className, loggerLevelDefault);
    }

    /**
     * Create a Magpie-Instance automatically handling the Exception.
     * <p>
     * Note: If the exception gets thrown, the thread/application relying on the
     * validator must and will terminate. If you do not like this behavior, please
     * use the regular constructor {@link #Magpie(String)}.
     * 
     * @param className name of the hosting class
     * @return the Magpie-Instance, if create-able
     */
    public static Magpie createMagpieSafe(String className) {
        if (hostedMagpies.containsKey(className)) {
            return hostedMagpies.get(className);
        }
        synchronized (hostedMagpies) {
            try {
                return createAndInsertANewMagpie(className);
            } catch (IOException ex) {
                writeOutThatMagpieCannotCreateFile(className, ex);
            }
        }
        throw new MagpieException("It was not possible to create a magpie-instance for name: " + className
                + ". This exception should not be thrown.");
    }

    private static Magpie createAndInsertANewMagpie(String className) throws IOException {
        Magpie magpie = new Magpie(className);
        hostedMagpies.put(className, magpie);
        return magpie;
    }

    private static void writeOutThatMagpieCannotCreateFile(String className, IOException ex) {
        System.err.println("Couldn't create Magpie-Instance for \"" + className + "\".");
        magpieShouldExitWithException(ex);
    }

    /**
     * Sets the default logger-level, used with {@link #Magpie(String)}
     * 
     * @param level the new default logging Level
     */
    public static void setDefaultLevel(Level level) {
        loggerLevelDefault = level;
        for (Magpie m : hostedMagpies.values()) {
            m.setThisDefaultLevel(level);
        }
    }

    /**
     * Sets the default logger-level for this instance
     * 
     * @param level the new default logging Level
     */
    public void setThisDefaultLevel(Level level) {
        this.logger.setLevel(level);
        updateHandlers();
    }

    public static Level getDefaultLevel() {
        return loggerLevelDefault;
    }

    /**
     * @return a temporary directory to use for this session
     */
    private static Path constructTmpDir() {
        formatter = new MagpieFormatter();
        try {
            return buildTheTmpDirectory();
        } catch (IOException ex) {
            writeOutThatMagpieCannotAcquireTempFolder(ex);
        }
        return null;
    }

    private static void writeOutThatMagpieCannotAcquireTempFolder(IOException ex) {
        System.err.println("Magpie wasn't able to acquire it's log-directory in the temp-folder.");
        magpieShouldExitWithException(ex);
    }

    private static void magpieShouldExitWithException(IOException ex) {
        System.err.println(ex.getMessage());
        System.exit(GameData.FAILURE);
    }

    private static Path buildTheTmpDirectory() throws IOException {
        File magpieTarget = Path.of(System.getProperty("java.io.tmpdir"), "Magpie").toFile();
        magpieTarget.mkdirs();
        return Files.createTempDirectory(magpieTarget.toPath(),
                "Magpie-" + formatter.folderDate.format(new Date()) + "--");
    }

    /**
     * Constructs a file-handler operating on a temporary file in the
     * temporary-folder
     * 
     * @param className name of the hosting class
     * @return the operating file-handler
     * 
     * @throws IOException if the creation of the temporary File or the accessing by
     *                     the FileHandler Fails. Permission-Errors won't be handled
     *                     extra.
     */
    public static MagpieFileHandler constructFileHandler(String className) throws IOException {
        Path targetPath = Path.of(TARGET_DIR.toString(), className + ".log");
        return new MagpieFileHandler(targetPath.toFile().getAbsolutePath());
    }

    /**
     * Identify the called-from method-signature *
     * 
     * @param lvlUp number of levels to go up, should be at least '2' to get
     *              'father' in trace
     * 
     * @return Signature of caller
     */
    public static String getNameOfCaller(int lvlUp) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String newLvl = elements[lvlUp].getClassName().replace("de.uulm.team020", "[team020]") + "."
                + elements[lvlUp].getMethodName();
        final int length = newLvl.length();
        if (length > MagpieFormatter.MAX_PACKAGE_LENGTH) {
            return "..." + newLvl
                    .substring(NumericHelper.getInBounds(length - MagpieFormatter.MAX_PACKAGE_LENGTH + 3, 0, length));
        } else {
            return newLvl;
        }
    }

    /**
     * This will <i>allow</i> magpie to write logs. Please note, that this will not
     * mean that every log is operated on. The explicit instance-based lock is
     * controlled with {@link #enableLog()}/{@link #disableLog()} by every instance.
     */
    public static void enableAllLogs() {
        globalLogEnabled = true;
    }

    /**
     * Disable <i>all</i> logs, no matter what the instance-based lock wants. This
     * can be reversed by {@link #enableAllLogs()}.
     */
    public static void disableAllLogs() {
        globalLogEnabled = false;
    }

    /**
     * Should Magpie mirror all log-information to a connected console of
     * applicable? This can be changed whenever you please. This will overwrite
     * Settings set by {@link #mirrorThisToConsole(boolean)} if called after those
     * calls.
     * 
     * @param mirror Should the log be mirrored, yes (true) or no (false)?
     */
    public static void mirrorToConsole(boolean mirror) {
        mirrorToConsole = mirror;
        for (Magpie m : hostedMagpies.values()) {
            m.mirrorThisToConsole(mirror);
        }
    }

    /**
     * Should Magpie mirror log-information from this log to a connected console of
     * applicable? This can be changed whenever you please.
     * 
     * @param mirror Should the log be mirrored, yes (true) or no (false)?
     */
    public void mirrorThisToConsole(boolean mirror) {
        mirrorThisToConsole = mirror;
        updateHandlers();
    }

    private void updateHandlers() {
        if (!(mirrorToConsole || mirrorThisToConsole)) {
            this.logger.removeHandler(this.consoleHandler);
            this.consoleHandler = null;
            return;
        }
        if (this.consoleHandler == null) { // update core
            updateAsThereIsNoConsoleHandler();
        }
        this.consoleHandler.setLevel(Level.ALL);
        // this.logger.setLevel(this.logger.getLevel()); // this.logger.getLevel()
    }

    private void updateAsThereIsNoConsoleHandler() {
        if (useRainbowForAll || useRainbow) {
            // construct if missing
            if (this.consoleFormatter == null) {
                this.consoleFormatter = new MagpieRainbowFormatter(this.className, this.logPath);
            }
            this.consoleHandler = new MagpieConsoleHandler(this.consoleFormatter);
        } else {
            this.consoleHandler = new MagpieConsoleHandler(new SimpleFormatter());
        }
        this.logger.addHandler(this.consoleHandler);
    }

    /**
     * Use rainbow colors in console, will only be active if
     * {@link #mirrorThisToConsole(boolean)} is active. This one will just call
     * {@link #useRainbow(boolean)} for every magpie-instance registered at the
     * time.
     * 
     * @param rainbow Flag indicating if random formatting shall be used - recalling
     *                this with true will not cause the color to change.
     * 
     * @see #useRainbow(boolean)
     */
    public static void useRainbowForAll(boolean rainbow) {
        useRainbowForAll = rainbow;
        for (Magpie m : hostedMagpies.values()) {
            m.useRainbow(rainbow);
        }
    }

    /**
     * Use rainbow colors in console, will only be active if
     * {@link #mirrorThisToConsole(boolean)} is active.
     * 
     * @param rainbow Flag indicating if random formatting shall be used - recalling
     *                this with true will not cause the color to change.
     */
    public void useRainbow(boolean rainbow) {
        this.useRainbow = rainbow;
        updateHandlers();
    }

    /**
     * Writes a Message to the logger, if this magpie-instance is currently enabled,
     * tosses it away otherwise.
     * <p>
     * Note: This means that {@link #logEnabled} <i>and</i>
     * {@link #globalLogEnabled} will be tested.
     * 
     * @param level   the level of the log-message
     * @param message the message to be logged
     */
    private synchronized void writeLogger(Level level, String message) {
        if (globalLogEnabled && this.logEnabled) {
            this.logger.log(level, message);
        }
    }

    /**
     * Prints a log-message using the wished Format, which is nice (:D)
     * <p>
     * It uses 4 as the lvlup (with {@link #getNameOfCaller(int)}) to get up
     * getNameOfCaller -> writeLogger -> caller (slave) -> method, logger was
     * invoked in
     * 
     * @param level  the logging-level
     * @param marker should the message be (optically) marked?
     * @param text   the text tu be logged
     * @param sender the sender, usually the worker we are logging in
     */
    private void writeLogger(Level level, boolean marker, String text, String sender) {
        writeLogger(level, String.format(MagpieFormatter.DEFAULT_OUT_FORMAT, getNameOfCaller(4), (marker ? "#" : " "),
                sender, text));
    }

    /**
     * Will <i>allow</i> magpie to write this log. Please note, that this option
     * will get ignored if {@link #disableAllLogs()} is set.
     */
    public void enableLog() {
        this.logEnabled = true;
    }

    /**
     * Disable this log, even if the global-log option is enabled with
     * {@link #enableAllLogs()}
     */
    public void disableLog() {
        this.logEnabled = false;
    }

    /**
     * @return Get the (absolute) path to the used logfile
     */
    public String getLogPath() {
        return this.logPath;
    }

    // Logger-Methods
    // ==========================================================================

    /**
     * Simple-Trace Message to be displayed in the logfile, can be used for
     * debugging-purposes or to just mark the beginning/end of a semantical block.
     * 
     * @param sender the sender of the trace-request.
     */
    public void trace(String sender) {
        writeLogger(Level.FINE, false, "Passed Trace-Statement ", sender);
    }

    /**
     * Write a custom log-message. This should not really be used. The only purpose
     * of this method is to grant the programmer the possibility to make his own
     * Log-Requests.
     * 
     * @param level  the logging-level
     * @param marker should the message be (optically) marked?
     * @param text   the text tu be logged
     * @param sender the sender, usually the worker we are logging in
     */
    public void writeCustom(Level level, boolean marker, String text, String sender) {
        writeLogger(level, marker, text, sender);
    }

    /**
     * Write a general debug(ging) message, can be used as a
     * 'fine'-debugging-instance.
     * <p>
     * Note this Message will be hidden with the default Log-Level! It is allocated
     * as {@link Level#FINER}.
     * 
     * @param text   the text to be written to the log
     * @param sender the sender, usually the worker we are logging in
     */
    public void writeDebug(String text, String sender) {
        writeLogger(Level.FINER, false, text, sender);
    }

    /**
     * Write an information. Yet to be standardized by the Team.
     * 
     * @param text   the text to be written to the log
     * @param sender the sender, usually the worker we are logging in
     */
    public void writeInfo(String text, String sender) {
        writeLogger(Level.INFO, false, text, sender);
    }

    /**
     * Write a warning. Yet to be standardized by the Team.
     * 
     * @param text   the text to be written to the log
     * @param sender the sender, usually the worker we are logging in
     */
    public void writeWarning(String text, String sender) {
        writeLogger(Level.WARNING, false, text, sender);
    }

    /**
     * Write a (severe) error. Yet to be standardized by the Team.
     * 
     * @param text   the text to be written to the log
     * @param sender the sender, usually the worker we are logging in
     */
    public void writeError(String text, String sender) {
        writeLogger(Level.SEVERE, true, text, sender);
    }

    /**
     * Writes an Exception at Info-Level.
     * 
     * @param ex     the exception
     * @param sender the sender of the exception
     */
    public void writeException(Throwable ex, String sender) {
        writeLogger(Level.SEVERE, true, ex.getMessage(), sender);
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] trace = ex.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            if (i > 0)
                sb.append("            ");
            sb.append(trace[i].toString()).append("\n");
        }
        writeLogger(Level.INFO, true, sb.toString(), sender);
    }

    /**
     * Writes an expected Exception at Info-Level. This means the Stack-Trace will
     * be limited to 8 Lines to prevent cluttering of the log.
     * 
     * @param ex     the exception
     * @param sender the sender of the exception
     */
    public void writeExceptionShort(Exception ex, String sender) {
        writeLogger(Level.SEVERE, true, ex.getMessage(), sender);
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] trace = ex.getStackTrace();

        final int lengthOfTrace = 8;
        appendStackTraceToBuilder(sb, trace, lengthOfTrace);

        if (trace.length > lengthOfTrace) {
            sb.append("[...]\n");
        }

        writeLogger(Level.INFO, true, sb.toString(), sender);
    }

    private void appendStackTraceToBuilder(StringBuilder sb, StackTraceElement[] trace, int lengthOfTrace) {
        for (int i = 0; i < trace.length && i < lengthOfTrace; i++) {
            if (i > 0) {
                sb.append("            ");
            }
            sb.append(trace[i].toString()).append("\n");
        }
    }

    public static MagpieFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {
        return "Magpie [log_enabled=" + logEnabled + ", log_path=" + logPath + ", logger=" + logger + "]";
    }

}