package de.uulm.team020.networking.messages;

import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestItemChoice-Message, send by a server to request an item-choice
 * while in the equipping-phase.
 * 
 * 
 * @author Florian Sihler
 * @version 1.1, 04/02/2020
 */
public class RequestItemChoiceMessage extends MessageContainer {

    private static final long serialVersionUID = 5143277470306586114L;

    private List<UUID> offeredCharacterIds;
    private List<GadgetEnum> offeredGadgets;

    /**
     * Construct a new RequestItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId            the uuid of the target-client, which has to be a
     *                            player. This has to be guaranteed in the policies
     * @param offeredCharacterIds uuids of the possible characters the player can
     *                            choose from
     * @param offeredGadgets      identifier of the gadgets the player can choose
     *                            from
     */
    public RequestItemChoiceMessage(UUID clientId, List<UUID> offeredCharacterIds, List<GadgetEnum> offeredGadgets) {
        this(clientId, offeredCharacterIds, offeredGadgets, "");
    }

    /**
     * Construct a new RequestItemChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId            the uuid of the target-client, which has to be a
     *                            player. This has to be guaranteed in the policies
     * @param offeredCharacterIds uuids of the possible characters the player can
     *                            choose from
     * @param offeredGadgets      identifier of the gadgets the player can choose
     *                            from
     * @param debugMessage        optional debug message
     */
    public RequestItemChoiceMessage(UUID clientId, List<UUID> offeredCharacterIds, List<GadgetEnum> offeredGadgets,
            String debugMessage) {
        super(MessageTypeEnum.REQUEST_ITEM_CHOICE, clientId, debugMessage);
        this.offeredCharacterIds = offeredCharacterIds;
        this.offeredGadgets = offeredGadgets;
    }

    public List<UUID> getOfferedCharacterIds() {
        return offeredCharacterIds;
    }

    public List<GadgetEnum> getOfferedGadgets() {
        return offeredGadgets;
    }

    @Override
    public String toString() {
        return "RequestItemChoiceMessage [<container>=" + super.toString() + ", offeredCharacterIds="
                + offeredCharacterIds + ", offeredGadgets=" + offeredGadgets + "]";
    }

}