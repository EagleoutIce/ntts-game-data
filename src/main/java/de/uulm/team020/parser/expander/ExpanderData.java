package de.uulm.team020.parser.expander;

import java.util.Set;

/**
 * This is a simple data model used by the {@link Expander} for performing expansion.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/29/2020
 * 
 * @since 1.1
 */
final class ExpanderData {

    /** Maximum number of follow-up calls inside of one expandable */
    public static final int RECURSION_LIMIT = 32;

    /** Char to initialize an expandable */
    public static final char EXP_KEY = '$';
    /** Char to start an expansion-group */
    public static final char EXP_OPEN = '{';
    /** Char to determine an operation-splitter */
    public static final char EXP_OPT = ':';
    /** Char to end an expansion-group */
    public static final char EXP_CLOSE = '}';
    /** Char to end an expansion which is not guarded by a group */
    public static final char EXP_END = ' ';
    /** Char which may be used to escape one of {@link #ESCAPE_ABLE} */
    public static final char EXP_ESCAPE = '\\';
    /**
     * All tokens which may be escaped, for all others escape char will not trigger!
     */
    public static final Set<Character> ESCAPE_ABLE = Set.of(EXP_KEY, EXP_OPEN, EXP_OPT, EXP_CLOSE, EXP_END, EXP_ESCAPE);

    /**
     * Checks if a given char is capable of escape
     * 
     * @param c The character to be checked
     * 
     * @return True if the character is Escapable
     */
    public static final boolean isEscapeAble(final char c) {
        return ESCAPE_ABLE.contains(c);
    }

    private final StringBuilder output;
    // produce current expansion
    private int curKeyStart = -1;
    private int curKeyOpt = -1;
    private int curKeyOffsetStart = -1;
    private int curKeyOffsetOpt = -1;
    // is it escaped?
    private boolean isEscaped = false;
    // used to find matching brace
    private int braceCounter = 0;
    // State
    private ExpanderState state;
    // offset
    private int offset;
    private int escapesDone;

    /**
     * Initialize a new ExpanderData-Object to be used by the {@link #Expander}
     * 
     * @param base The String to be used by this expander-object
     */
    public ExpanderData(final String base) {
        this.output = new StringBuilder(base);
        // initial state
        state = ExpanderState.READ_NORMAL;
        offset = 0;
        escapesDone = 0;
    }

    public StringBuilder getOutput() {
        return output;
    }

    public int getCurKeyStart() {
        return curKeyStart;
    }

    public void setCurKeyStart(int curKeyStart) {
        this.curKeyStart = curKeyStart;
        this.curKeyOffsetStart = curKeyStart + offset;
    }

    public int getCurKeyOffsetStart() {
        return curKeyOffsetStart;
    }

    public int getCurKeyOpt() {
        return curKeyOpt;
    }

    public void setCurKeyOpt(int curKeyOpt) {
        this.curKeyOpt = curKeyOpt;
        this.curKeyOffsetOpt = curKeyOpt + offset;
    }

    public int getCurKeyOffsetOpt() {
        return curKeyOffsetOpt;
    }

    public boolean isEscaped() {
        return isEscaped;
    }

    public void setEscaped(boolean isEscaped) {
        this.isEscaped = isEscaped;
    }

    public int getBraceCounter() {
        return braceCounter;
    }

    public void setBraceCounter(int braceCounter) {
        this.braceCounter = braceCounter;
    }

    public ExpanderState getState() {
        return state;
    }

    public void setState(ExpanderState state) {
        this.state = state;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void incrementOffset(int incOffset) {
        this.offset += incOffset;
    }


    public void decrementOffset(int incOffset) {
        this.offset -= incOffset;
    }

    public void doneEscape() {
        escapesDone += 1;
    }

    public int getAndResetEscapesDone() {
        final int done = escapesDone;
        escapesDone = 0;
        return done;
    }
}