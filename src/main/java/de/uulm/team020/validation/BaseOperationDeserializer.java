package de.uulm.team020.validation;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.enumerations.OperationEnum;

/**
 * Deserializer for {@link BaseOperation} in order to parse the inherited
 * operations. Based on some stuff Florian provided.
 *
 * @author Lennart Altenhof
 * @author Florian Sihler
 * 
 * @version 1.1, 06/07/2020
 */
public class BaseOperationDeserializer implements JsonDeserializer<BaseOperation> {

    @Override
    public BaseOperation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();

        final OperationEnum opType = OperationEnum.valueOf(type);
        final Class<?> targetClass = opType.getTargetClass();
        if (targetClass == BaseOperation.class) {
            return GameDataGson.getBare().fromJson(json, typeOfT);
        } else { // lett the deserializer work :D
            return context.deserialize(json, targetClass);
        }
    }

}
