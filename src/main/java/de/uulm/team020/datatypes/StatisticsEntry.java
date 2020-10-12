package de.uulm.team020.datatypes;

/**
 * Entry for the {@link Statistics}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StatisticsEntry implements IAmJson {

    private static final long serialVersionUID = 4255139848876486432L;

    private final String title;
    private final String description;
    private final String valuePlayer1;
    private final String valuePlayer2;

    /**
     * Construct a new Entry
     * 
     * @param title        Title of the statistics
     * @param description  Short description, what the statistic is for
     * @param valuePlayer1 Value for playerOne
     * @param valuePlayer2 Value for playerTwo
     */
    public StatisticsEntry(String title, String description, String valuePlayer1, String valuePlayer2) {
        this.title = title;
        this.description = description;
        this.valuePlayer1 = valuePlayer1;
        this.valuePlayer2 = valuePlayer2;
    }

    /**
     * Title of the statistics
     * 
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Description of the statistics
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Value for player one
     * 
     * @return the valuePlayer1
     */
    public String getValuePlayer1() {
        return valuePlayer1;
    }

    /**
     * Value for player two
     * 
     * @return the valuePlayer2
     */
    public String getValuePlayer2() {
        return valuePlayer2;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StatisticsEntry [description=").append(description).append(", title=").append(title)
                .append(", valuePlayer1=").append(valuePlayer1).append(", valuePlayer2=").append(valuePlayer2)
                .append("]");
        return builder.toString();
    }

}