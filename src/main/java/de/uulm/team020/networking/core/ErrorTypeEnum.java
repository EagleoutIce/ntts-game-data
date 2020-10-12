package de.uulm.team020.networking.core;

/**
 * Basic identification of a network-error by {@link MessageTypeEnum#ERROR}.
 * 
 * @author Florian Sihler
 * @version 1.1, 03/25/2020
 */
public enum ErrorTypeEnum {
    NAME_NOT_AVAILABLE("The Name you wanted is already in use by another player."),
    ALREADY_SERVING("The server does already have enough players. Try to connect as a spectator instead."),
    SESSION_DOES_NOT_EXIST("The sessionId you asked for does not exist (anymore)s"),
    ILLEGAL_MESSAGE("The message send was illegal, consult the debugMessage or the logfiles for further information"),
    TOO_MANY_STRIKES("Strike-Limit exceeded."),
    GENERAL("A generic Error occurred, please consult the logfiles for more Information.");

    private String description;

    ErrorTypeEnum(String description) {
        this.description = description;
    }

    /**
     * @return A String describing the type of Error
     */
    public String getDescription() {
        return this.description;
    }
}
