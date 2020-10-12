package de.uulm.team020.networking.messages;

import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

import java.util.UUID;

/**
 * The GamePauseMessage-Message, send by the server to signal
 * the start or the end of a game-pause.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/02/2020
 */
public class GamePauseMessage extends MessageContainer {

    private static final long serialVersionUID = -3757342348889271801L;
    
    private Boolean gamePaused;
    private Boolean serverEnforced;


    /**
     * Construct a new GamePauseMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId the uuid of the target-client
     * @param gamePaused True, if game is paused now, false if the pause has ended.
     * @param serverEnforced True, if the server enforced this pause, false if this pause was started by
     *                       the player.
     */
    public GamePauseMessage(UUID clientId, Boolean gamePaused, Boolean serverEnforced) {
        this(clientId, gamePaused, serverEnforced, "");
    }

    /**
     * Construct a new RequestItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId the uuid of the sending-client
     * @param gamePaused True, if game is paused now, false if the pause has ended.
     * @param serverEnforced True, if the server enforced this pause, false if this pause was started by
     *                       the player.
     * @param debugMessage optional debug message
     */
    public GamePauseMessage(UUID clientId, Boolean gamePaused, Boolean serverEnforced, String debugMessage) {
        super(MessageTypeEnum.GAME_PAUSE, clientId, debugMessage);
        this.gamePaused = gamePaused;
        this.serverEnforced = serverEnforced;
    }

    public Boolean getGamePaused() {
        return this.gamePaused;
    }

    public Boolean getServerEnforced() {
        return this.serverEnforced;
    }

    @Override
    public String toString() {
        return "GamePauseMessage [<container>=" + super.toString() + ", gamePaused=" + gamePaused + ", serverEnforced=" + serverEnforced + "]";
    }
}