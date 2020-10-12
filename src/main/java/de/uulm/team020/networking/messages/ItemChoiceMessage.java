package de.uulm.team020.networking.messages;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The ItemChoiceMessage-Message, send by a client to answer
 * an item-choice while in the equipping-phase.
 * 
 * @author Florian Sihler
 * @version 1.1, 04/02/2020
 */
public class ItemChoiceMessage extends MessageContainer {

    private static final long serialVersionUID = 4098000019677506345L;

    private UUID chosenCharacterId;
    private GadgetEnum chosenGadget;

    /**
     * Construct a new ItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId the uuid of the sending-client, which has to be a player.
     *                 This has to be guaranteed in the policies
     * @param chosenCharacterId uuid of the chosen character, if not null, {@code chosenGadget} has to be null.
     * @param chosenGadget identifier of the chosen gadget, if not null {@code chosenCharacterId} has to be null.
     */
    public ItemChoiceMessage(UUID clientId, UUID chosenCharacterId, GadgetEnum chosenGadget) {
        this(clientId, chosenCharacterId, chosenGadget, "");
    }

    /**
     * Construct a new ItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId the uuid of the target-client, which has to be a player.
     *                 This has to be guaranteed in the policies
     * @param chosenCharacterId uuids of the possible characters the player can
     *                 choose from
     * @param chosenGadget identifier of the gadgets the player can choose from
     * @param debugMessage optional debug message
     */
    public ItemChoiceMessage(UUID clientId, UUID chosenCharacterId, GadgetEnum chosenGadget,
            String debugMessage) {
        super(MessageTypeEnum.ITEM_CHOICE, clientId, debugMessage);
        // if((chosenCharacterId == null && chosenGadget == null)
        //     || (chosenCharacterId != null && chosenGadget != null))
        //     throw new RuntimeException("You've created an ItemChoiceMessage, where the chosenCharacterId (" + chosenCharacterId + ") and the chosenGadget + (" + chosenGadget + ") are not exclusively null!");
        this.chosenCharacterId = chosenCharacterId;
        this.chosenGadget = chosenGadget;
    }

    public UUID getChosenCharacterId() {
        return chosenCharacterId;
    }

    public GadgetEnum getChosenGadget() {
        return chosenGadget;
    }

    /**
     * Will return if and only if one of both choosable values is not null.
     * 
     * @return True if the message is valid from an syntactical perspective, false otherwise
     */
    public boolean isValid() {
        return (getClientId() != null) && (chosenCharacterId == null ^ chosenGadget == null);

    }

    @Override
    public String toString() {
        return "ItemChoiceMessage [<container>=" + super.toString() + ", chosenCharacterId=" + chosenCharacterId + 
                ", chosenGadget=" + chosenGadget + "]";
    }
    
}