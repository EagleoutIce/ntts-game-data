package de.uulm.team020.parser.expander;

/**
 * State of an Expander while it is processing data
 * 
 * @author Florian Sihler
 * @version 1.0, 05/29/2020
 * 
 * @since 1.1
 */
enum ExpanderState {
    /** await KEY */
    READ_NORMAL,
    /** await END */
    READ_EXP,
    /** await OPT or CLOSE */
    READ_BRACED_EXP,
    /** await CLOSE */
    READ_OPT_EXP
}