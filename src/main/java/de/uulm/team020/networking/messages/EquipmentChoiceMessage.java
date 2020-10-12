package de.uulm.team020.networking.messages;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * The EquipmentChoiceMessage-Message, send by a server to request an 
 * equipment-choice while in the equipping-phase.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/02/2020
 */
public class EquipmentChoiceMessage extends MessageContainer {

    private static final long serialVersionUID = -1626389169798815251L;
    
    private Map<UUID, List<GadgetEnum>> equipment;

    /**
     * Construct a new EquipmentChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId the uuid of the target-client, which has to be a player.
     *                 This has to be guaranteed in the policies
     * @param equipment mapping of character-uuids to their equipped gadgets
     */
    public EquipmentChoiceMessage(UUID clientId, Map<UUID, List<GadgetEnum>> equipment) {
        this(clientId, equipment, "");
    }

    /**
     * Construct a new EquipmentChoiceMessage which can be serialized by
     * {@link de.uulm.team020.datatypes.IAmJson#toJson() iAmJson}.
     * 
     * @param clientId the uuid of the target-client, which has to be a player.
     *                 This has to be guaranteed in the policies
     * @param equipment mapping of character-uuids to their equipped gadgets

     * @param debugMessage optional debug message
     */
    public EquipmentChoiceMessage(UUID clientId, Map<UUID, List<GadgetEnum>> equipment,
                String debugMessage) {
        super(MessageTypeEnum.EQUIPMENT_CHOICE, clientId, debugMessage);
        this.equipment = equipment;
    }

    public Map<UUID, List<GadgetEnum>> getEquipment(){
        return this.equipment;
    }

    @Override
    public String toString() {
        return "EquipmentChoiceMessage [<container>=" + super.toString() + ", equipment=" + equipment + "]";
    }

    

}