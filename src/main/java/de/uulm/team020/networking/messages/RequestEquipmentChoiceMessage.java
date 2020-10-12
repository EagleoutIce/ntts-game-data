package de.uulm.team020.networking.messages;

import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The RequestEquipmentChoiceMessage-Message, send by a server to request an
 * equipment-choice while in the equipping-phase.
 * 
 * 
 * @author Florian Sihler
 * @version 1.0, 03/26/2020
 */
public class RequestEquipmentChoiceMessage extends MessageContainer {

    private static final long serialVersionUID = 8841078609507960041L;

    private List<UUID> chosenCharacterIds;
    private List<GadgetEnum> chosenGadgets;

    /**
     * Construct a new RequestEquipmentChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId           the uuid of the target-client, which has to be a
     *                           player. This has to be guaranteed in the policies
     * @param chosenCharacterIds all characters the player-client has chosen
     * @param chosenGadgets      all gadgets the player-client has chosen
     */
    public RequestEquipmentChoiceMessage(UUID clientId, List<UUID> chosenCharacterIds, List<GadgetEnum> chosenGadgets) {
        this(clientId, chosenCharacterIds, chosenGadgets, "");
    }

    /**
     * Construct a new RequestEquipmentChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId           the uuid of the target-client, which has to be a
     *                           player. This has to be guaranteed in the policies
     * @param chosenCharacterIds all characters the player-client has chosen
     * @param chosenGadgets      all gadgets the player-client has chosen
     * @param debugMessage       optional debug message
     */
    public RequestEquipmentChoiceMessage(UUID clientId, List<UUID> chosenCharacterIds, List<GadgetEnum> chosenGadgets,
            String debugMessage) {
        super(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, clientId, debugMessage);
        this.chosenCharacterIds = chosenCharacterIds;
        this.chosenGadgets = chosenGadgets;
    }

    public List<UUID> getChosenCharacterIds() {
        return chosenCharacterIds;
    }

    public List<GadgetEnum> getChosenGadgets() {
        return chosenGadgets;
    }

    @Override
    public String toString() {
        return "RequestEquipmentChoiceMessage [<container>=" + super.toString() + ", chosenCharacterIds="
                + chosenCharacterIds + ", chosenGadgets=" + chosenGadgets + "]";
    }
}