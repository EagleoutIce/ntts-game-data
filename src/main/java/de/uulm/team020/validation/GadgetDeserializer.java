package de.uulm.team020.validation;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;

/**
 * Deserializer for {@link Gadget} in order to parse the inherited
 * gadget-classes.
 *
 * @author Florian Sihler
 * 
 * @version 1.1, 06/07/2020
 */
public class GadgetDeserializer implements JsonDeserializer<Gadget> {

    @Override
    public Gadget deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String type = jsonObject.get("gadget").getAsString();

        final GadgetEnum gadgetType = GadgetEnum.valueOf(type);
        if (gadgetType.hasSubclass()) {
            return context.deserialize(json, gadgetType.getTargetClass());
        } else {
            return GameDataGson.getBare().fromJson(json, typeOfT);
        }
    }

}
