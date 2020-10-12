package de.uulm.team020.parser.expander;

import java.util.Objects;

/**
 * First of all, getting the elephant out of the room, as there already is a
 * quit similar function embedded into the commons-text module of apache. But as
 * this module adds up to 700KB of library i do not intend to use it and instead
 * write my own implementation using Regex - just because i think it's funny and
 * the Expander will not be used in any scenario where it is timing critical,
 * the goal is to provide some easy string interpolation with support function
 * calls, which can provide recursion and fallback support (whilst not being to
 * inefficient, just a little bit of fun).
 * 
 * @author Florian Sihler
 * @version 2.1, 06/07/2020
 */
public class Expander {

    private int recursionCounter;

    private final Expandables expandables = new Expandables();

    /**
     * Construct a new Expander based on a set of expandables
     *
     * @param expandables the {@link Expandables} this instance should be created on
     */
    public Expander(final Expandables... expandables) {
        recursionCounter = 0;
        Objects.requireNonNull(expandables, "Expandables shall not be null");
        synchronized (this.expandables) {
            for (final Expandables exp : expandables) {
                this.expandables.appendServants(exp);
            }
        }
    }

    /**
     * This method will run through the string and expand it. See
     * {@link #expand(String, String, int, boolean)} for more detailed information.
     * This variant will expand the full string and perform un-expansion and erase
     * unknown data.
     * 
     * @param base The string to collect the data from it will not be altered
     *
     * @return The string expanded by the given rules
     * 
     * @see #expand(String, String)
     * @see #expand(String, String, int, boolean)
     */
    public String expand(final String base) {
        return expand(base, "");
    }

    /**
     * This method will run through the string and expand it. See
     * {@link #expand(String, String, int, boolean)} for more detailed information.
     * This variant will expand the full string and perform un-expansion.
     * 
     * @param base    The string to collect the data from it will not be altered
     * @param unknown The string to use if an expandable is detected, but the
     *                expansion is not known.
     * @return The string expanded by the given rules
     * 
     * @see #expand(String, String, int, boolean)
     */
    public String expand(final String base, final String unknown) {
        return expand(base, unknown, 0, true);
    }

    /**
     * This method will run through the string, starting with the given position. If
     * it collects a character of {@value ExpanderData#EXP_KEY}-Token it will start
     * collection. If the next character is of {@value ExpanderData#EXP_OPEN} it
     * will collect until it finds the corresponding {@value ExpanderData#EXP_CLOSE}
     * and split the collected tokens on the first {@value ExpanderData#EXP_OPT}
     * found. If the Expandable is not opened with a {@value ExpanderData#EXP_OPEN}
     * it will read until a {@value ExpanderData#EXP_END}-Character is read.
     * 
     * @param base      The string to collect the data from it will not be altered
     * @param unknown   The string to use if an expandable is detected, but the
     *                  expansion is not known.
     * @param startFrom The index to start from
     * @param unescape  Should the expander remove the escape-sequences so another
     *                  expansion-process may collect on them?
     * @return The string expanded by the given rules
     */
    public String expand(final String base, final String unknown, final int startFrom, final boolean unescape) {
        // recursive guard
        if (recursionCounter > ExpanderData.RECURSION_LIMIT) {
            throw new RecursiveExpansionException("Limit of " + ExpanderData.RECURSION_LIMIT + " reached.");
        }
        final int storeCounter = recursionCounter;
        recursionCounter += 1;
        // produce the output
        final String produced = expandRaw(base, unknown, startFrom, unescape);
        // recursive reset
        recursionCounter = storeCounter;
        return produced;
    }

    /**
     * This method will run through the string, starting with the given position. Do
     * not use this one as it just performs the raw interpolation, use
     * {@link #expand(String, String, int, boolean)} instead.
     * 
     * @param base      The string to collect the data from it will not be altered
     * @param unknown   The string to use if an expandable is detected, but the
     *                  expansion is not known.
     * @param startFrom The index to start from
     * @param unescape  Should the expander remove the escape-sequences so another
     *                  expansion-process may collect on them?
     * 
     * @return The string expanded by the given rules
     */
    protected String expandRaw(final String base, final String unknown, final int startFrom, final boolean unescape) {
        // produce the output
        ExpanderData data = new ExpanderData(base);

        // Iterate over the String
        final int length = base.length();

        // for every character...
        for (int i = startFrom; i < length; i++) {
            final char c = base.charAt(i);
            transitionOnCharacter(base, unknown, unescape, data, length, i, c);
        }

        // last execution
        if (data.getState() == ExpanderState.READ_EXP) {
            // expand we use i as exclusive upper as it shall not be replaced
            final String key = base.substring(data.getCurKeyStart() + 1, length);
            expandNoOpt(length, key, data, base, unknown, 1, 0);
        }
        return data.getOutput().toString();
    }

    private void transitionOnCharacter(final String base, final String unknown, final boolean unescape,
            ExpanderData data, final int length, int i, final char c) {
        // System.out.println("At: " + c + " in: " + state + " (" + output.toString() +
        // ", " + key.toString() + ", " + option.toString() + ") esc: " + isEscaped + ",
        // fce: " + firstCharExp + " braceCount: " + braceCounter)
        switch (data.getState()) {
            // Just collect until an unescaped '$' is found
            default:
            case READ_NORMAL:
                transitionReadNormal(i, c, length, base, data, unescape);
                break;
            // In: '$...'
            case READ_EXP:
                transitionReadExp(i, c, length, base, data, unknown, unescape);
                break;
            // In '${...'
            case READ_BRACED_EXP:
                transitionReadBracedExp(i, c, length, base, data, unknown, unescape);
                break;
            // In '${<key>:...'
            case READ_OPT_EXP:
                transitionReadOptExp(i, c, length, base, data, unknown, unescape);
                break;
        }
    }

    private boolean mayEscape(final int i, final int length, final String base) {
        return i < length - 1 && ExpanderData.isEscapeAble(base.charAt(i + 1));
    }

    private void mayUnescape(final ExpanderData data, final int i, final boolean unescape, final boolean buffer) {
        // remove escape-character if desired
        if (data.isEscaped() && unescape) {
            data.getOutput().deleteCharAt(i + data.getOffset());
            // we will buffer any offset decrements and append them on expansion
            data.decrementOffset(1);
            if (buffer) {
                data.doneEscape();
            }
        }
    }

    // will modify data
    private void transitionReadNormal(final int i, final char c, final int length, final String base,
            final ExpanderData data, final boolean unescape) {
        if (data.isEscaped()) {
            data.setEscaped(false);
        } else {
            if (c == ExpanderData.EXP_KEY) {
                // Switch to collect expandable
                data.setState(ExpanderState.READ_EXP);
                data.setCurKeyStart(i);
                return;
            } else if (c == ExpanderData.EXP_ESCAPE && mayEscape(i, length, base)) {
                data.setEscaped(true);
            }
        }
        mayUnescape(data, i, unescape, false);
    }

    private void transitionReadExp(final int i, final char c, final int length, final String base,
            final ExpanderData data, final String unknown, final boolean unescape) {
        if (data.isEscaped()) {
            data.setEscaped(false);
        } else {
            // Starts with brace so '${'
            if (data.getCurKeyStart() == i - 1 && c == ExpanderData.EXP_OPEN) {
                data.setState(ExpanderState.READ_BRACED_EXP);
                data.setBraceCounter(1);
            } else if (c == ExpanderData.EXP_ESCAPE && mayEscape(i, length, base)) {
                data.setEscaped(true);
            } else if (c == ExpanderData.EXP_END) {
                final String key = base.substring(data.getCurKeyStart() + 1, i);
                expandNoOpt(i, key, data, base, unknown, 1, 0); // 1 as offset pack to account for the dollar
            }
        }
        mayUnescape(data, i, unescape, true);
    }

    private void transitionReadBracedExp(final int i, final char c, final int length, final String base,
            final ExpanderData data, final String unknown, final boolean unescape) {
        if (data.isEscaped()) {
            data.setEscaped(false);
        } else {
            if (c == ExpanderData.EXP_OPEN) {
                data.setBraceCounter(data.getBraceCounter() + 1);
            } else if (c == ExpanderData.EXP_OPT && data.getBraceCounter() == 1) {
                data.setState(ExpanderState.READ_OPT_EXP);
                data.setCurKeyOpt(i);
                return; // don't collect option
            } else if (c == ExpanderData.EXP_ESCAPE && mayEscape(i, length, base)) {
                data.setEscaped(true);
            } else if (c == ExpanderData.EXP_CLOSE) {
                final int braceCounter = data.getBraceCounter();
                if (braceCounter > 1) {
                    data.setBraceCounter(braceCounter - 1);
                } else {
                    // expand we use i as exclusive upper as it shall not be replaced
                    final String key = base.substring(data.getCurKeyStart() + 2, i);
                    expandNoOpt(i, key, data, base, unknown, 3, 1); // add 3 for '${' and '}'; start & end
                    return;
                }
            }
        }
        mayUnescape(data, i, unescape, true);
    }

    // TODO: extract similar parts from transition read opt and braced?
    private void transitionReadOptExp(final int i, final char c, final int length, final String base,
            final ExpanderData data, final String unknown, final boolean unescape) {
        if (data.isEscaped()) {
            data.setEscaped(false);
        } else {
            if (c == ExpanderData.EXP_OPEN) {
                data.setBraceCounter(data.getBraceCounter() + 1);
            } else if (c == ExpanderData.EXP_ESCAPE && mayEscape(i, length, base)) {
                data.setEscaped(true);
            } else if (c == ExpanderData.EXP_CLOSE) {
                final int braceCounter = data.getBraceCounter();
                if (braceCounter > 1) {
                    data.setBraceCounter(braceCounter - 1);
                } else {
                    // expand
                    final String key = base.substring(data.getCurKeyStart() + 2, data.getCurKeyOpt());
                    final String opt = base.substring(data.getCurKeyOpt() + 1, i);
                    expandWithOpt(i, key, opt, data, unknown, 4); /* 4 for '${:}' */
                    return;
                }
            }
        }
        // remove escape-character if desired
        mayUnescape(data, i, unescape, true);
    }

    private void expandNoOpt(final int i, final String key, final ExpanderData data, final String base,
            final String unknown, int offsetPack, int lastConsume) {
        // expand we use i as exclusive upper as it shall not be replaced
        final String expandedKey = expandStep(key, null, unknown);
        // we will use offset start as this one holds the offset when init-token was
        // read
        data.getOutput().replace(data.getCurKeyOffsetStart(), i + data.getOffset() + lastConsume, expandedKey);
        data.incrementOffset(expandedKey.length() - (key.length() + offsetPack) + data.getAndResetEscapesDone());
        data.setCurKeyStart(-1);
        data.setState(ExpanderState.READ_NORMAL);
    }

    private void expandWithOpt(final int i, final String key, final String opt, final ExpanderData data,
            final String unknown, int offsetPack) {
        // expand we use i as exclusive upper as it shall not be replaced
        final String expandedKey = expandStep(key, opt, unknown);
        // we will use offset start as this one holds the offset when init-token was
        // read, consume is locked as for an option, there has to be a closing token
        data.getOutput().replace(data.getCurKeyOffsetStart(), i + data.getOffset() + 1, expandedKey);
        data.incrementOffset(
                expandedKey.length() - (key.length() + opt.length() + offsetPack) + data.getAndResetEscapesDone());
        data.setCurKeyStart(-1);
        data.setCurKeyOpt(-1);
        data.setState(ExpanderState.READ_NORMAL);
    }

    // Expands one Expandable
    private String expandStep(final String key, final String option, final String unknown) {
        // expand the key:
        final String expKey = expand(key);
        final String expansion = expandables.findExpansion(expKey, option, recursionCounter, this);
        return expansion == null ? unknown : expansion;
    }

    /**
     * Works like {@link #expand(String)} but will call multiple times if necessary
     * 
     * @param string The string to expand
     * @return The fully expanded String
     */
    public String expandFull(String string) {
        String was = "";
        // make option expandable ;)
        while (true) {
            string = expand(string);
            // break if did not change
            if (Objects.equals(string, was)) {
                return string;
            }
            was = string;
        }
    }

    /**
     * Returns the core expandables for further modifications - this will be just a
     * direct access.
     * 
     * @return Local expandables
     */
    public Expandables getOwned() {
        synchronized (this.expandables) {
            return this.expandables;
        }
    }
}
