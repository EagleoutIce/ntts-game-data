package de.uulm.team020.parser.expander;

/**
 * Will be called by the {@link Expandables} to provide the expanded String
 */
@FunctionalInterface
public interface iExpansionServant {

    /**
     * Producer to be called for a given variable
     *
     * @param key the key that was registered to call this servant
     * @param option the option, if given, can be used as default or for further arguments
     * @param level the recursion-level this expansion occurred on
     * @param expander the expander which has called this function, can be used for another expansion-call
     *
     * @return the String the key should expand to
     */
    String produce(final String key, final String option, final int level, final Expander expander);
}
