package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The Strike-Message, send by a server
 * to signal a(nother) strike..
 * 
 * @author Florian Sihler
 * @version 1.0, 04/14/2020
 */
public class StrikeMessage extends MessageContainer {

    private static final long serialVersionUID = -6978104307077274825L;

    private int strikeNr;
    private int strikeMax;
    String reason;

    /**
     * Construct a new StrikeMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param strikeNr Strike number
     * @param strikeMax Maximum number of allowed strikes
     * 
     * @see #StrikeMessage(UUID, int, int, String)
     */
    public StrikeMessage(UUID clientId, int strikeNr, int strikeMax) {
        this(clientId, strikeNr, strikeMax, "");
    }

    /**
     * Construct a new StrikeMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param strikeNr Strike number
     * @param strikeMax Maximum number of allowed strikes
     * @param reason Textual representation of the strike-reason
     * 
     * @see #StrikeMessage(UUID, int, int, String, String)
     */
    public StrikeMessage(UUID clientId, int strikeNr, int strikeMax, String reason) {
        this(clientId, strikeNr, strikeMax, reason, "");
    }

    /**
     * Construct a new StrikeMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param strikeNr Strike number
     * @param strikeMax Maximum number of allowed strikes
     * @param reason Textual representation of the strike-reason
     * @param debugMessage optional debug message
     */
    public StrikeMessage(UUID clientId, int strikeNr, int strikeMax, String reason, String debugMessage) {
        super(MessageTypeEnum.STRIKE, clientId, debugMessage);
        this.strikeNr = strikeNr;
        this.strikeMax = strikeMax;
        this.reason = reason;
    }

    public int getStrikeNr() {
        return this.strikeNr;
    }

    public int getStrikeMax() {
        return this.strikeMax;
    }

    public String getReason() {
        return this.reason;
    }

    /**
     * @return True if the strike limit is reached
     */
    public boolean limitReached() {
        return this.strikeNr >= this.strikeMax;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StrikeMessage [<container>=").append(super.toString()).append(", reason=").append(reason).append(", strikeMax=").append(strikeMax)
                .append(", strikeNr=").append(strikeNr).append("]");
        return builder.toString();
    }

}