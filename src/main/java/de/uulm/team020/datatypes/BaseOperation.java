package de.uulm.team020.datatypes;

import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;

/**
 * Base-Type to represent an Operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 * @since 1.2
 */
public class BaseOperation implements IAmJson {

    private static final long serialVersionUID = 2935234500458615018L;

    private OperationEnum type;
    private boolean successful;
    private Point target;

    public BaseOperation(OperationEnum type, boolean successful, Point target) {
        this.type = type;
        this.successful = successful;
        this.target = target;
    }

    public OperationEnum getType() {
        return type;
    }

    public boolean getSuccessful() {
        return successful;
    }

    public Point getTarget() {
        return target;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BaseOperation [successful=").append(successful).append(", target=").append(target)
                .append(", type=").append(type).append("]");
        return builder.toString();
    }
}