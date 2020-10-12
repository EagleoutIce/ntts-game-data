package de.uulm.team020.networking.messages;

import java.util.Objects;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The classic Hello-Message, send by clients to establish a Connection to the
 * Server.
 * 
 * @author Florian Sihler
 * @version 1.0b, 03/27/2020
 */
public class HelloMessage extends MessageContainer {

    private static final long serialVersionUID = 7630983891460330082L;

    private String name;
    private RoleEnum role;

    /**
     * Construct a new HelloMessage which can be serialized by
     * {@link IAmJson#toJson()}. The clientId is enforced to be null.
     * 
     * @param name the name of the client
     * @param role the role of the client
     * 
     * @see #HelloMessage(String, RoleEnum, String)
     */
    public HelloMessage(String name, RoleEnum role) {
        this(name, role, "");
    }

    /**
     * Construct a new HelloMessage which can be serialized by
     * {@link IAmJson#toJson()}.
     * 
     * @param name         the name of the client
     * @param role         the role of the client
     * @param debugMessage optional debug message
     * 
     */
    public HelloMessage(String name, RoleEnum role, String debugMessage) {
        super(MessageTypeEnum.HELLO, null, debugMessage);
        this.name = name;
        this.role = role;
    }

    /** @return The supplied name */
    public String getName() {
        return this.name;
    }

    /** @return The supplied role */
    public RoleEnum getRole() {
        return this.role;
    }

    @Override
    public String toString() {
        return "HelloMessage [<container>=" + super.toString() + ", name=" + name + ", role=" + role + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof HelloMessage))
            return false;
        HelloMessage other = (HelloMessage) obj;
        return Objects.equals(name, other.name) && role == other.role && super.equals(obj);
    }


}