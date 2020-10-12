package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestGamePause-Message, send by a human player to request a game-pause or
 * to request the end of an existing game pause. If there are no human players
 * in the game, this message has no further effect.
 * 
 * @author Florian Sihler
 * @version 1.0, 02/04/2020
 */
public class RequestGamePauseMessage extends MessageContainer {


    private static final long serialVersionUID = -3663653570354784435L;

    private boolean gamePause;

    /**
     * Construct a new RequestGamePauseMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId the uuid of the sending-client
     * @param gamePause True, if a pause is request, false if the end of a pause is desired.
     */
    public RequestGamePauseMessage(UUID clientId, boolean gamePause) {
        this(clientId, gamePause, "");
    }

    /**
     * Construct a new RequestItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     *
     * @param clientId the uuid of the sending-client
     * @param gamePause True, if a pause is request, false if the end of a pause is desired.
     * @param debugMessage optional debug message
     */
    public RequestGamePauseMessage(UUID clientId, boolean gamePause, String debugMessage) {
        super(MessageTypeEnum.REQUEST_GAME_PAUSE, clientId, debugMessage);
        this.gamePause = gamePause;
    }

    public boolean getGamePause() {
        return this.gamePause;
    }

    @Override
    public String toString() {
        return "RequestGamePauseMessage [<container>=" + super.toString() + ", gamePause=" + gamePause + "]";
    }
}