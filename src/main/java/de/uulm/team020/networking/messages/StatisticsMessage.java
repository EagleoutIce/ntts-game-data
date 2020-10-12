package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.Statistics;
import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The HelloReply-Message, send by servers to accept the connection setup
 * initiated by a {@link HelloMessage}
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StatisticsMessage extends MessageContainer {

    private static final long serialVersionUID = 7630983891460330082L;

    private final Statistics statistics;
    private final UUID winner;
    private final VictoryEnum reason;
    private final boolean hasReplay;

    /**
     * Construct a new StatisticsMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId   The uuid of the target-client
     * @param statistics Statistics for round end
     * @param winner     The winner of the game
     * @param reason     The reason the game ended and the victor was announced
     * @param hasReplay  Does this server offer the replay feature?
     * 
     * @see #StatisticsMessage(UUID, Statistics, UUID, VictoryEnum, boolean, String)
     */
    public StatisticsMessage(UUID clientId, Statistics statistics, UUID winner, VictoryEnum reason, boolean hasReplay) {
        this(clientId, statistics, winner, reason, hasReplay, "");
    }

    /**
     * Construct a new StatisticsMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId     the uuid of the target-client
     * @param statistics   Statistics for round end
     * @param winner       The winner of the game
     * @param reason       The reason the game ended and the victor was announced
     * @param hasReplay    Does this server offer the replay feature?
     * @param debugMessage optional debug message
     * 
     */
    public StatisticsMessage(UUID clientId, Statistics statistics, UUID winner, VictoryEnum reason, boolean hasReplay,
            String debugMessage) {
        super(MessageTypeEnum.STATISTICS, clientId, debugMessage);
        this.statistics = statistics;
        this.winner = winner;
        this.reason = reason;
        this.hasReplay = hasReplay;
    }

    /**
     * @return the statistics
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * @return the winner
     */
    public UUID getWinner() {
        return winner;
    }

    /**
     * @return the reason
     */
    public VictoryEnum getReason() {
        return reason;
    }

    /**
     * @return the hasReplay
     */
    public boolean hasReplay() {
        return hasReplay;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StatisticsMessage [<container>=").append(super.toString()).append(", hasReplay=")
                .append(hasReplay).append(", reason=").append(reason).append(", statistics=").append(statistics)
                .append(", winner=").append(winner).append("]");
        return builder.toString();
    }

}