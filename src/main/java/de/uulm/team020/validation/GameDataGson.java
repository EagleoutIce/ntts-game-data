package de.uulm.team020.validation;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.helper.InternalResources;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;

/**
 * Provides the GSON-Instance statically, as i hate class-based recreation,
 * sorry.
 * <p>
 * Not sorry.
 * 
 * @author Florian Sihler
 * @version 1.2, 06/28/2020
 */
public class GameDataGson implements IAmJson {

    private static final long serialVersionUID = 4557091939567733144L;

    private static Magpie magpie = Magpie.createMagpieSafe("GameData");

    private static final String DATE_STRING = "dd.MM.yyyy kk:mm:ss";

    /** No instance allowed :D */
    private GameDataGson() {
    }

    /** the global gson instance */
    private static final Gson gson = createGsonInstance();

    /** the global bare gson instance */
    private static final Gson bareGson = createBareGsonInstance();
    private static final Gson prettyGson = createBareGsonInstanceWithPretty();

    /**
     * Access the gson-instance directly. Use {@link #createGsonInstance()} if you
     * need to own one separately.
     * 
     * @return game-data' gson-reference
     */
    public static Gson get() {
        return GameDataGson.gson;
    }

    /**
     * Access the (bare) gson-instance directly. Use
     * {@link #createBareGsonInstance()} if you need to own one separately.
     * 
     * @return bare gson-reference
     */
    protected static Gson getBare() {
        return GameDataGson.bareGson;
    }

    /**
     * Get the pretty printing gson-instance which wont have deserializers
     * registered
     * 
     * @return pretty gson-reference
     */
    public static Gson getPrettyGson() {
        return prettyGson;
    }

    /**
     * Use this for the default gson instance
     * 
     * @return A new Gson-Object abiding by the standard.
     */
    public static Gson createGsonInstance() {
        return new GsonBuilder().setDateFormat(DATE_STRING).serializeNulls()
                .registerTypeAdapter(BaseOperation.class, new BaseOperationDeserializer())
                .registerTypeAdapter(Gadget.class, new GadgetDeserializer()).create();
    }

    /**
     * This may be used by the deserializer(s) to get a context without recursive
     * calls
     * 
     * @return A new Gson-Object abiding by the standard.
     */
    protected static Gson createBareGsonInstance() {
        return new GsonBuilder().setDateFormat(DATE_STRING).serializeNulls().create();
    }

    /**
     * This may be used by the deserializer(s) to get a context without recursive
     * calls but using pretty print
     * 
     * @return A new Gson-Object abiding by the standard.
     */
    protected static Gson createBareGsonInstanceWithPretty() {
        return new GsonBuilder().setDateFormat(DATE_STRING).serializeNulls().setPrettyPrinting().create();
    }

    /**
     * This method serializes the specified object into its equivalent Json
     * representation. This method should be used when the specified object is not a
     * generic type. This method uses {@link Class#getClass()} to get the type for
     * the specified object, but the {@code getClass()} loses the generic type
     * information because of the Type Erasure feature of Java. Note that this
     * method works fine if the any of the object fields are of generic type, just
     * the object itself should not be of a generic type. If the object is of
     * generic type, use {@link Gson#toJson(Object, java.lang.reflect.Type)}
     * instead. If you want to write out the object to a {@link java.io.Writer}, use
     * {@link Gson#toJson(Object, Appendable)} instead.
     *
     * @param src the object for which Json representation is to be created setting
     *            for Gson
     * @return Json representation of {@code src}.
     */
    public static String toJson(final Object src) {
        return getBare().toJson(src);
    }

    /**
     * This method deserializes the specified Json into an object of the specified
     * class. It is not suitable to use if the specified class is a generic type
     * since it will not have the generic type information because of the Type
     * Erasure feature of Java. Therefore, this method should not be used if the
     * desired type is a generic type. Note that this method works fine if the any
     * of the fields of the specified object are generics, just the object itself
     * should not be a generic type. For the cases when the object is of generic
     * type, invoke {@link Gson#fromJson(String, java.lang.reflect.Type)}. If you
     * have the Json in a {@link java.io.Reader} instead of a String, use
     * {@link Gson#fromJson(java.io.Reader, Class)} instead.
     *
     * @param <T>      the type of the desired object
     * @param json     the string from which the object is to be deserialized
     * @param classOfT the class of T
     * @return an object of type T from the string. Returns {@code null} if
     *         {@code json} is {@code null} or if {@code json} is empty.
     */
    public static <T> T fromJson(final String json, final Class<T> classOfT) {
        return get().fromJson(json, classOfT);
    }

    /**
     * This method deserializes the specified Json into an object of the specified
     * class. It is not suitable to use if the specified class is a generic type
     * since it will not have the generic type information because of the Type
     * Erasure feature of Java. Therefore, this method should not be used if the
     * desired type is a generic type. Note that this method works fine if the any
     * of the fields of the specified object are generics, just the object itself
     * should not be a generic type. For the cases when the object is of generic
     * type, invoke {@link Gson#fromJson(String, java.lang.reflect.Type)}. If you
     * have the Json in a {@link java.io.Reader} instead of a String, use
     * {@link Gson#fromJson(java.io.Reader, Class)} instead.
     * <p>
     * This one will extract a specific field.
     *
     * @param <T>      The type of the desired object
     * @param json     The string from which the object is to be deserialized
     * @param field    The desired subfield of the host object
     * @param classOfT The class of T
     * @return an object of type T from the string. Returns {@code null} if
     *         {@code json} is {@code null} or if {@code json} is empty.
     */
    public static <T> T fromJson(final String json, String field, final Class<T> classOfT) {
        return get().fromJson(extractField(json, field), classOfT);
    }

    /**
     * Extracts a json field from a json object (top-level)
     * 
     * @param json  The json data to parse
     * @param field The field desired
     * 
     * @return The json data for the specific field
     */
    public static String extractField(final String json, String field) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        return object.get(field).toString();
    }

    /**
     * Loads a json File from internal resources, can be used in tests as in
     * release.
     * <p>
     * Note, that this implementation will truncate any newlines present in the
     * json-data and replace them with a space. It will therefore upkeep the
     * validity and semantic of JSON-Files, while making them available for a wider
     * range of parsers.
     * 
     * @param path The desired path
     * @return The json data, if available
     * 
     * @throws IOException If the file was not available/accessible
     */
    public static String loadInternalJson(final String path) throws IOException {
        return InternalResources.getFile(path, s -> s.collect(Collectors.joining(" ")));
    }

    /**
     * The Container of the message, if it is in valid {@link MessageContainer}
     * format.
     * 
     * @param json the json-data to parse
     * @return {@link MessageContainer} if it was possible to parse, {@code null}
     *         otherwise.
     */
    public static MessageContainer getContainer(final String json) {
        if (json == null || json.isBlank()) // No Data supplied
            return null;

        final ValidationReport report = Validator.validateObject(json, SchemaProvider.MESSAGE_CONTAINER_SCHEMA);
        if (!report.isValid()) {
            magpie.writeError(
                    "Tried to get (message-)type for '" + json + "' but the message was not in valid containerformat.",
                    "Get");
            return null;
        }

        return fromJson(json, MessageContainer.class);
    }

    /**
     * The Type of the message, if it is in valid {@link MessageContainer} format.
     * 
     * @param json the json-data to parse
     * @return {@link MessageTypeEnum} if it was possible to parse, {@code null}
     *         otherwise.
     * 
     * @see #getContainer(String)
     */
    public static MessageTypeEnum getType(final String json) {
        final MessageContainer container = getContainer(json);
        return container == null ? null : container.getType();
    }
}