package de.uulm.team020.networking.messages;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

import java.util.UUID;

/**
 * The GameStarted-Message, send by a server
 * to notify the start of the equipment phase,
 * and therefore the (Prepare-)Game-Logic
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class GameStartedMessage extends MessageContainer {
    
    private static final long serialVersionUID = 7630983891460330082L;

    private UUID playerOneId;
    private UUID playerTwoId;
    private String playerOneName;
    private String playerTwoName;
    private UUID sessionId;

    /**
     * Construct a new GameStarted-Message which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param playerOneId UUID of the first player
     * @param playerTwoId UUID of the second player
     * @param playerOneName Name of the first player
     * @param playerTwoName Name of the second player
     * @param sessionId UUID of the current session
     * 
     * @see #GameStartedMessage(UUID, UUID, UUID, String, String, UUID, String)
     */
    public GameStartedMessage(UUID clientId, UUID playerOneId, UUID playerTwoId,
            String playerOneName, String playerTwoName, UUID sessionId) {
        this(clientId, playerOneId, playerTwoId, playerOneName, playerTwoName, sessionId, "");
    }

    /**
     * Construct a new GameStartedMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId the uuid of the target-client
     * @param playerOneId UUID of the first player
     * @param playerTwoId UUID of the second player
     * @param playerOneName Name of the first player
     * @param playerTwoName Name of the second player
     * @param sessionId UUID of the current session
     * @param debugMessage optional debug message
     */
    public GameStartedMessage(UUID clientId, UUID playerOneId, UUID playerTwoId,
            String playerOneName, String playerTwoName, UUID sessionId,
            String debugMessage) {
        super(MessageTypeEnum.GAME_STARTED, clientId, debugMessage);
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.sessionId = sessionId;
    }

    public UUID getPlayerOneId() {
        return playerOneId;
    }

    public UUID getPlayerTwoId() {
        return playerTwoId;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * Allows to exchange the clientId, can be used by the server
     *
     * @param newClientId the new clientId
     */
    public void exchangeClientId(UUID newClientId) {
        this.setClientId(newClientId);
    }

    @Override
    public String toString() {
        return "GameStartedMessage [<container>=" + super.toString() + ", playerOneId=" + playerOneId + 
                ", playerOneName=" + playerOneName + ", playerTwoId=" + playerTwoId + ", playerTwoName=" + 
                playerTwoName + ", sessionId=" + sessionId + "]";
    }
}
