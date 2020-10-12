package de.uulm.team020.datatypes;

import java.util.Arrays;

/**
 * Main class to hold the Statistics that will be shipped out when the game
 * ends. The creation of these Statistics is completely optional - but they will
 * be provided.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class Statistics implements IAmJson {

    private static final long serialVersionUID = 7862895521874350301L;

    private StatisticsEntry[] entries;

    /**
     * Construct a new Statistics
     * 
     * @param entries The statistics to ship to all clients
     */
    public Statistics(StatisticsEntry[] entries) {
        this.entries = entries;
    }

    public StatisticsEntry[] getStatistics() {
        return this.entries;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Statistics [entries=").append(Arrays.toString(entries)).append("]");
        return builder.toString();
    }

}