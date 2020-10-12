package de.uulm.team020.networking.messages;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The Replay-Message, send by the server to present the replay
 * 
 * @author Florian Sihler
 * @version 1.0, 05/05/2020
 */
public class ReplayMessage extends MessageContainer {

    private static final long serialVersionUID = -8000788733367539764L;

    private UUID sessionId;
    private Date gameStart;
    private Date gameEnd;

    private UUID playerOneId;
    private UUID playerTwoId;

    private String playerOneName;
    private String playerTwoName;

    private int rounds;

    private Scenario level;
    private Matchconfig settings;
    private CharacterDescription[] characterSettings;

    private String[] messages;

    /**
     * Construct a new ReplayMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId          The uuid of the target-client
     * @param sessionId         Id of the active session
     * @param gameStart         Timestamp of game start
     * @param gameEnd           Timestamp of game end
     * @param playerOneId       Id of player one
     * @param playerTwoId       Id of player two
     * @param playerOneName     Name of player one
     * @param playerTwoName     Name of player two
     * @param rounds            Total number of player rounds
     * @param level             Played Scenario
     * @param settings          Used Matchconfig
     * @param characterSettings Used Character-Descriptions
     * @param messages          All Messages sent for the specific client target
     * 
     */
    public ReplayMessage(UUID clientId, UUID sessionId, Date gameStart, Date gameEnd, UUID playerOneId,
            UUID playerTwoId, String playerOneName, String playerTwoName, int rounds, Scenario level,
            Matchconfig settings, CharacterDescription[] characterSettings, String[] messages) {
        this(clientId, sessionId, gameStart, gameEnd, playerOneId, playerTwoId, playerOneName, playerTwoName, rounds,
                level, settings, characterSettings, messages, "");
    }

    /**
     * Construct a new ReplayMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param clientId          The uuid of the target-client
     * @param sessionId         Id of the active session
     * @param gameStart         Timestamp of game start
     * @param gameEnd           Timestamp of game end
     * @param playerOneId       Id of player one
     * @param playerTwoId       Id of player two
     * @param playerOneName     Name of player one
     * @param playerTwoName     Name of player two
     * @param rounds            Total number of player rounds
     * @param level             Played Scenario
     * @param settings          Used Matchconfig
     * @param characterSettings Used Character-Descriptions
     * @param messages          All Messages sent for the specific client target
     * @param debugMessage      Optional debug message
     * 
     */
    public ReplayMessage(UUID clientId, UUID sessionId, Date gameStart, Date gameEnd, UUID playerOneId,
            UUID playerTwoId, String playerOneName, String playerTwoName, int rounds, Scenario level,
            Matchconfig settings, CharacterDescription[] characterSettings, String[] messages, String debugMessage) {
        super(MessageTypeEnum.REPLAY, clientId, debugMessage);
        this.sessionId = sessionId;
        this.gameStart = gameStart;
        this.gameEnd = gameEnd;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.rounds = rounds;
        this.level = level;
        this.settings = settings;
        this.characterSettings = characterSettings;
        this.messages = messages;
    }

    /**
     * @return the sessionId
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @return the gameStart
     */
    public Date getGameStart() {
        return gameStart;
    }

    /**
     * @return the gameEnd
     */
    public Date getGameEnd() {
        return gameEnd;
    }

    /**
     * @return the playerOneId
     */
    public UUID getPlayerOneId() {
        return playerOneId;
    }

    /**
     * @return the playerTwoId
     */
    public UUID getPlayerTwoId() {
        return playerTwoId;
    }

    /**
     * @return the playerOneName
     */
    public String getPlayerOneName() {
        return playerOneName;
    }

    /**
     * @return the playerTwoName
     */
    public String getPlayerTwoName() {
        return playerTwoName;
    }

    /**
     * @return the rounds
     */
    public int getRounds() {
        return rounds;
    }

    /**
     * @return the level
     */
    public Scenario getLevel() {
        return level;
    }

    /**
     * @return the settings
     */
    public Matchconfig getSettings() {
        return settings;
    }

    /**
     * @return the characterSettings
     */
    public CharacterDescription[] getCharacterSettings() {
        return characterSettings;
    }

    /**
     * @return the messages
     */
    public String[] getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReplayMessage [<container>=" + super.toString() + ", characterSettings=")
                .append(Arrays.toString(characterSettings)).append(", gameEnd=").append(gameEnd).append(", gameStart=")
                .append(gameStart).append(", level=").append(level).append(", messages=")
                .append(Arrays.toString(messages)).append(", playerOneId=").append(playerOneId)
                .append(", playerOneName=").append(playerOneName).append(", playerTwoId=").append(playerTwoId)
                .append(", playerTwoName=").append(playerTwoName).append(", rounds=").append(rounds)
                .append(", sessionId=").append(sessionId).append(", settings=").append(settings).append("]");
        return builder.toString();
    }
}