package de.uulm.team020.datatypes.enumerations;

import de.uulm.team020.datatypes.*;

/**
 * Representing the type of an Operation
 *
 * @author Florian Sihler
 * @author Lennart Altenhof
 * @version 1.0, 04/04/2020
 * @since 1.2
 */
public enum OperationEnum {

    GADGET_ACTION(GadgetAction.class, true), SPY_ACTION(Operation.class, true), GAMBLE_ACTION(GambleAction.class, true),
    PROPERTY_ACTION(PropertyAction.class, true), MOVEMENT(Movement.class, true), CAT_ACTION(BaseOperation.class, false),
    JANITOR_ACTION(BaseOperation.class, false), EXFILTRATION(Exfiltration.class, false), RETIRE(Operation.class, true);

    private final boolean sendByClient;

    private final Class<? extends BaseOperation> targetClass;

    OperationEnum(Class<? extends BaseOperation> targetClass, boolean sendByClient) {
        this.targetClass = targetClass;
        this.sendByClient = sendByClient;
    }

    public boolean getSendByClient() {
        return this.sendByClient;
    }

    public Class<? extends BaseOperation> getTargetClass() {
        return targetClass;
    }
}